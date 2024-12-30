package com.example.trelloproject.card.dto;

import com.example.trelloproject.card.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class CardResponseDto {
    private Long id;
    private String title;
    private String description;
    private Date dueDate;
    private Long memberId;
    private Long listId;

    public CardResponseDto(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
        this.memberId = card.getMember() != null ? card.getMember().getId() : null;
        this.listId = card.getList() != null ? card.getList().getId() : null;
    }

    // null 처리를 위한 새로운 생성자
    public CardResponseDto(Card card, boolean ignoreList) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
        this.memberId = card.getMember() != null ? card.getMember().getId() : null;
        this.listId = null; // 리스트가 없는 경우
    }
}