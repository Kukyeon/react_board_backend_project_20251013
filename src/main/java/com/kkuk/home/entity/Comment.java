package com.kkuk.home.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 500)
	private String content; // 댓글 내용 
	
	@CreationTimestamp
	private LocalDateTime createDate; // 댓글 입력 날자시간

	//로그인한 사용자의 이름 -> 댓글 작성한 사용자 
	@ManyToOne
	private SiteUser author;
	
	//댓글이 달릴 게시글의 id
	@ManyToOne
	private Board board;
}
