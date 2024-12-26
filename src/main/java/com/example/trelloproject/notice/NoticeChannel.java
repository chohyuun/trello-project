package com.example.trelloproject.notice;

import lombok.Getter;

@Getter
public enum NoticeChannel {
    NOTICE_TEST("C086B8U6QMA"),
    WORKSPACE("C086X2T0UBB"),
    COMMENT("C086MDLSRND"),
    BOARD("C0863QDD96K"),
    CARD("C086X2S07DX");

    public final String channel;

    NoticeChannel(String channel) {
        this.channel = channel;
    }
}
