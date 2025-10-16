package com.kkuk.home.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

	@NotBlank(message = "댓글 내용은 필수 항목입니다.")
	@Size(min = 5, message = "댓글 내용은 최소 5글자 이상이여야합니다.")
	private String content;
}
