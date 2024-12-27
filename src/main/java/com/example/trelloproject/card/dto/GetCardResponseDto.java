package com.example.trelloproject.card.dto;

import com.example.trelloproject.board.dto.ListDto;
import com.example.trelloproject.card.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
@Getter
@NoArgsConstructor
public class GetCardResponseDto {
    private Long id;
    private String title;
    private String description;
    private Date dueDate;
    private GetCardMemberDto member;
    //private ListDto list;
    private GetCardFileDto cardFile;

    public GetCardResponseDto(Card card) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
        this.member = card.getMember() != null ? new GetCardMemberDto(card.getMember()) : null;
       // this.list = new ListDto(card.getList());
        this.cardFile = card.getCardFile() != null ? new GetCardFileDto(card.getCardFile()) : null;
    }
}
