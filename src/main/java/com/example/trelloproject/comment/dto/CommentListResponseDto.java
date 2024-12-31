package com.example.trelloproject.comment.dto;

import lombok.Getter;

@Getter
public class CommentListResponseDto {
    private final Long commentId;
    private final Long memberId;
    private final String memberName;
    private final String contents;

    public CommentListResponseDto(Long commentId, Long memberId, String memberName, String contents) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.contents = contents;
    }
}
