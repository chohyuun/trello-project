package com.example.trelloproject.card.enums;

import lombok.Getter;

@Getter
public enum ActionType {
    CREATE("생성"),
    UPDATE("수정"),
    DELETE("삭제"),
    FILE_UPLOAD("파일 업로드"),
    FILE_DELETE("파일 삭제");

    private final String description;

    ActionType(String description) {
        this.description = description;
    }
}
