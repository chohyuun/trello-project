package com.example.trelloproject.board.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class BoardRequestDto {

    private String title;

    private MultipartFile file;
}
