package com.kkuk.home.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kkuk.home.dto.BoardDto;
import com.kkuk.home.entity.Board;
import com.kkuk.home.entity.SiteUser;
import com.kkuk.home.repository.BoardRepository;
import com.kkuk.home.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/board")
public class BoardController {

	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	//전체 게시글 조회
	@GetMapping
	public List<Board> list() {
		return boardRepository.findAll();
	}
	
	//게시글 작성
//	@PostMapping
//	public ResponseEntity<?> write(@RequestBody Board req, Authentication auth) {
//		
//		//auth.getName(); -> 로그인한 유저 이름
//		SiteUser siteUser = userRepository.findByUsername(auth.getName())
//				.orElseThrow(()->new UsernameNotFoundException("사용자 없음"));
//		//siteUser -> 현재 로그인한 유저의 레코드
//		
//		Board board = new Board();
//		board.setTitle(req.getTitle()); // 유저가 입력한 글 제목
//		board.setContent(req.getContent()); // 유저가 입력한 글 내용
//		board.setAuthor(siteUser);
//		
//		boardRepository.save(board);
//		
//		return ResponseEntity.ok(board);
//	}
	
	//게시글작성 (유효성체크 -> 제목, 내용 최소글자제한)
	@PostMapping
	public ResponseEntity<?> write(@Valid @RequestBody BoardDto boardDto, Authentication auth , BindingResult bindingResult) {
		
		
		//사용자 로그인 여부 확인
		if(auth == null) { //참이면 로그인x -> 글쓰기 권한없음 -> 에러코드반환
			return ResponseEntity.status(401).body("로그인 후 이용바랍니다.");
		}
		
		if(bindingResult.hasErrors()) { //참일시 유효성 체크 실패 -> error 
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());
				}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		//auth.getName(); -> 로그인한 유저 이름
		SiteUser siteUser = userRepository.findByUsername(auth.getName())
				.orElseThrow(()->new UsernameNotFoundException("사용자 없음"));
		//siteUser -> 현재 로그인한 유저의 레코드
		
		Board board = new Board();
		board.setTitle(boardDto.getTitle()); // 유저가 입력한 글 제목
		board.setContent(boardDto.getContent()); // 유저가 입력한 글 내용
		board.setAuthor(siteUser);
		
		boardRepository.save(board);
		
		return ResponseEntity.ok(board);
	}
	
	//특정 게시글 번호(id)로 글 조회(글 상세보기)
	@GetMapping("/{id}")
	public ResponseEntity<?> getPost(@PathVariable("id") Long id) {
//		Board board = boardRepository.findById(id)
//				.orElseThrow(()->new EntityNotFoundException("해당 글 없음"));
		
		Optional<Board> _board = boardRepository.findById(id);
		if(_board.isPresent()) { // ture =  글 조회 성공
			return ResponseEntity.ok(_board.get()); // 해당 아이디의 글 반환
		} else {
			return ResponseEntity.status(404).body("해당 게시글은 존재하지않습니다.");
		}	
	}
	
	//특정 게시글 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id, Authentication auth) {
		Optional<Board> optional = boardRepository.findById(id);
		
		if(optional.isEmpty()) {
			return ResponseEntity.status(404).body("해당 게시글은 존재하지않습니다.");
		}
		
		if(auth == null || !auth.getName().equals(optional.get().getAuthor().getUsername())) {
			return ResponseEntity.status(403).body("해당 권한이 없습니다.");
		}
		
		
			boardRepository.delete(optional.get());
			return ResponseEntity.ok("삭제완료");
		
	}
	
	// 게시글 수정 (권한 설정 ->로그인 후 본인 작성 글만 수정 가능)
	@PutMapping("/{id}")
	public ResponseEntity<?> updatePost(@PathVariable("id") Long id, @RequestBody Board updateBoard, Authentication auth) {
		Optional<Board> optional = boardRepository.findById(id);
		
		if(optional.isEmpty()) {
			return ResponseEntity.status(404).body("해당 게시글이 존재하지 않습니다.");
		}
		
		if(auth == null || !auth.getName().equals(optional.get().getAuthor().getUsername())) {
			return ResponseEntity.status(403).body("해당 권한이 없습니다.");
		}
		
		Board oldPost = optional.get(); // 기존 게시글
		
		oldPost.setTitle(updateBoard.getTitle());
		oldPost.setContent(updateBoard.getContent());
		
		boardRepository.save(oldPost);
		
		return ResponseEntity.ok(oldPost);
	}
	
}
