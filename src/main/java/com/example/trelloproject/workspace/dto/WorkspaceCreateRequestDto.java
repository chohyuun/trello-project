package com.example.trelloproject.workspace.dto;

import lombok.Getter;

@Getter
public class WorkspaceCreateRequestDto {
    private final String title;

    private final String description;

    private final String slackCode;

    public WorkspaceCreateRequestDto(String title, String description, String slackCode) {
        this.title = title;
        this.description = description;
        this.slackCode = slackCode;
    }
}
