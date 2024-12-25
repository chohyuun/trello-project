package com.example.trelloproject.card.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class CardSearchRequestDto {
    private String keyword;
    private Long listId;
    private Long boardId;
    private Date startDate;
    private Date endDate;

    public CardSearchRequestDto(String keyword, Long listId, Date startDate, Date endDate ,Long boardId) {
        this.keyword = keyword;
        this.listId = listId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.boardId = boardId;
    }
}
