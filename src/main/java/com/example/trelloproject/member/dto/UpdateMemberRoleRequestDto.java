package com.example.trelloproject.member.dto;

import lombok.Getter;

@Getter
public class UpdateMemberRoleRequestDto {

    private String role;

    public UpdateMemberRoleRequestDto(String role) {
        this.role = role;
    }
}
