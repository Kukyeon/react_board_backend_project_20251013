package com.kkuk.home.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kkuk.home.dto.SiteUserDto;
import com.kkuk.home.entity.SiteUser;
import com.kkuk.home.repository.UserRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//회원가입
//	@PostMapping("/signup")
//	public ResponseEntity<?> signup(@RequestBody SiteUser req) {
//		// 이미 존재하는 사용자 이름(username) 인지 확인
//		if(userRepository.findByUsername(req.getUsername()).isPresent()) {
//			//참이면 -> 이미 해당 username(아이디)가 존재함으로 가입 불가
//			return ResponseEntity.badRequest().body("이미 존재하는 사용자명 입니다");
//		}
//		req.setPassword(passwordEncoder.encode(req.getPassword())); 
//		// 비밀번호 암호화해서 엔티티에 다시 넣기
//		userRepository.save(req);
//		
//		return ResponseEntity.ok("회원가입 성 공");
//		//return ResponseEntity.ok(req); // 가입 성공 후 엔티티 반환
//	}
	
	// validation 적용 회원 가입 (빈칸 입력 방지, 최소 글자 개수)
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SiteUserDto siteUserDto, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) { //참일시 유효성 체크 실패 -> error 
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
				err -> {
					errors.put(err.getField(), err.getDefaultMessage());
				}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		SiteUser siteUser = new SiteUser(); // entity 객체선언
		siteUser.setUsername(siteUserDto.getUsername());
		//사용자가 입력한 username(dto에 존재)를 entity 객체에 삽입
		siteUser.setPassword(siteUserDto.getPassword());
		//사용자가 입력한 password(dto에 존재)를 entity 객체에 삽입
		
		// 이미 존재하는 사용자 이름(username) 인지 확인
		if(userRepository.findByUsername(siteUser.getUsername()).isPresent()) {
			Map<String, String> error = new HashMap<>();
			error.put("iderror", "이미 존재하는 사용자명 입니다");
			//참이면 -> 이미 해당 username(아이디)가 존재함으로 가입 불가
			return ResponseEntity.badRequest().body(error);
		}
		siteUser.setPassword(passwordEncoder.encode(siteUser.getPassword())); 
		// 비밀번호 암호화해서 엔티티에 다시 넣기
		userRepository.save(siteUser);
		
		return ResponseEntity.ok("회원가입 성 공");
		//return ResponseEntity.ok(req); // 가입 성공 후 엔티티 반환
	}
	
	//로그인
	@GetMapping("/me") // 현재 로그인한 사용자 정보 가저와라 하는 요청
	public ResponseEntity<?> me(Authentication auth){ 
		return ResponseEntity.ok(Map.of("username", auth.getName()));
	}
}
