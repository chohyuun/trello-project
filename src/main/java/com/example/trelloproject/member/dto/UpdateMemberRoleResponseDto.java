package com.example.trelloproject.member.dto;

import lombok.Getter;

@Getter
public class UpdateMemberRoleResponseDto {

    private Long memberId;

    private String role;

    public UpdateMemberRoleResponseDto(Long memberId, String role) {
        this.memberId = memberId;
        this.role = role;
    }
}
