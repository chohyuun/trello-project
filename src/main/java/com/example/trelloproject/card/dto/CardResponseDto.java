package com.example.trelloproject.card.dto;

import com.example.trelloproject.card.Card;
import com.example.trelloproject.card.CardFile;
import com.example.trelloproject.list.ListEntity;
import com.example.trelloproject.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class CardResponseDto {

    private Long id;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    private Member member;
    private ListEntity list;
    private CardFile cardFile;


    public CardResponseDto(Card card){
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
        this.member = card.getMember();
        this.list = card.getList();
        this.cardFile = card.getCardFile();
    }
}
