package com.example.trelloproject.comment.dto;

import com.example.trelloproject.comment.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private final Long commentId;
    private final Long memberId;
    private final String contents;

    public CommentResponseDto(Long commentId, Long memberId, String contents) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.contents = contents;
    }

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.contents = comment.getContents();
    }
}
