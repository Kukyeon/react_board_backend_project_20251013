package com.kkuk.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkuk.home.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
