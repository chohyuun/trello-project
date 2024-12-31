package com.example.trelloproject.notice;

import lombok.Getter;

@Getter
public enum NoticeChannel {
    NOTICE_TEST("알림 테스트"),
    WORKSPACE("워크스페이스 생성"),
    WORKSPACE_CHANGE("워크스페이스 변경"),
    MEMBER("멤버 추가"),
    COMMENT("댓글 생성"),
    COMMENT_CHANGE("댓글 수정"),
    BOARD("보드 생성"),
    BOARD_CHANGE("보드 수정"),
    CARD("카드 추가"),
    CARD_CHANGE("카드 수정");

    public final String channel;

    NoticeChannel(String channel) {
        this.channel = channel;
    }
}
