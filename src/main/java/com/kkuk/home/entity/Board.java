package com.kkuk.home.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 게시판번호
	
	private String title; // 게시판제목
	
	private String content; // 게시판내용
	
	@CreationTimestamp // 자동으로 insert 시 현재 날짜 시간 삽입
	private LocalDateTime createDate;
	
	@ManyToOne // N : 1 관계 -> 게시판글 : 유저
	private SiteUser author; // 게시판 글쓴이
	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
}
