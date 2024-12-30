package com.example.trelloproject.global.exception;

public class CardFileSizeExceededException extends CardFileException {
    public CardFileSizeExceededException() {
        super("파일 크기는 5MB를 초과할 수 없습니다.");
    }
}