package com.example.trelloproject.global.exception;

public class CardInvalidFileFormatException extends CardFileException {
    public CardInvalidFileFormatException() {
        super("지원되지 않는 파일 형식입니다. (지원 형식: jpg, png, pdf, csv)");
    }
}
