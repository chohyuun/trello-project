package com.example.trelloproject.card.dto;

import com.example.trelloproject.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetCardMemberDto {
    private Long id;
    private String userName;

    public GetCardMemberDto(Member member) {
        this.id = member.getId();
        this.userName = member.getUser().getUserName();
    }
}

