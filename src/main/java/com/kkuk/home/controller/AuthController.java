package com.kkuk.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kkuk.home.entity.SiteUser;
import com.kkuk.home.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody SiteUser req) {
		// 이미 존재하는 사용자 이름(username) 인지 확인
		if(userRepository.findByUsername(req.getUsername()).isPresent()) {
			//참이면 -> 이미 해당 username(아이디)가 존재함으로 가입 불가
			return ResponseEntity.badRequest().body("이미 존재하는 사용자명 입니다");
		}
		req.setPassword(passwordEncoder.encode(req.getPassword())); 
		// 비밀번호 암호화해서 엔티티에 다시 넣기
		userRepository.save(req);
		
		return ResponseEntity.ok("회원가입 성 공");
		//return ResponseEntity.ok(req); // 가입 성공 후 엔티티 반환
	}
	
	
}
