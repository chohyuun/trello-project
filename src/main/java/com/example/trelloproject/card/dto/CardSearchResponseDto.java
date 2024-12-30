package com.example.trelloproject.card.dto;

import com.example.trelloproject.card.Card;
import com.example.trelloproject.member.dto.MemberResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class CardSearchResponseDto {
    private Long id;
    private String title;
    private String description;
    private Date dueDate;
    private MemberResponseDto member;
    private CardFileResponseDto cardFile;

    public CardSearchResponseDto(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
        if (card.getMember() != null) {
            this.member = new MemberResponseDto(
                    card.getMember().getId(),
                    card.getMember().getUser().getUserName()
            );
        }
        // Card 엔티티의 파일 필드명이 'cardFile'인 경우:
        if (card.getCardFile() != null) {
            this.cardFile = new CardFileResponseDto(card.getCardFile());
        }
    }
}
