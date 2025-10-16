package com.kkuk.home.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kkuk.home.dto.CommentDto;
import com.kkuk.home.entity.Board;
import com.kkuk.home.entity.Comment;
import com.kkuk.home.entity.SiteUser;
import com.kkuk.home.repository.BoardRepository;
import com.kkuk.home.repository.CommentRepository;
import com.kkuk.home.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	//댓글작성 ->
	@PostMapping("/{boardId}")
	public ResponseEntity<?> writeComment(@PathVariable("boardId") Long boardID,
										  @Valid @RequestBody CommentDto commentDto, 
										  Authentication auth) {
		
		Optional<Board> _board = boardRepository.findById(boardID);
		if(_board.isEmpty()) {
			return ResponseEntity.badRequest().body("해당 게시글이 존재하지 않습니다.");
		}
		
		SiteUser user = userRepository.findByUsername(auth.getName()).orElseThrow();
		
		Comment comment = new Comment();
		comment.setBoard(_board.get());
		comment.setAuthor(user);
		comment.setContent(commentDto.getContent());
		
		commentRepository.save(comment);
		
		return ResponseEntity.ok(comment); // db에 등록된 댓글 객체를 200 응답과 반환
	}
	
}
