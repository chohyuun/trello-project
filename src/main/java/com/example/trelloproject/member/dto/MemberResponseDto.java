package com.example.trelloproject.member.dto;

import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long memberId;

    private String role;

    public MemberResponseDto(Long memberId, String role) {
        this.memberId = memberId;
        this.role = role;
    }
}
