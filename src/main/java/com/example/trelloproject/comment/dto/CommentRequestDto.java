package com.example.trelloproject.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotEmpty(message = "댓글은 빈칸일 수 없습니다.")
    private final String contents;

    public CommentRequestDto(String contents) {
        this.contents = contents;
    }
}
