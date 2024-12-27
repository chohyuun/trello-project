package com.example.trelloproject.workspace.dto;

import lombok.Getter;

@Getter
public class WorkspaceRequestDto {

    private final String title;

    private final String description;

    public WorkspaceRequestDto(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
