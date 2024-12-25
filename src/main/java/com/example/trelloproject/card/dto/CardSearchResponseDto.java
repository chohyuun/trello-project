package com.example.trelloproject.card.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class CardSearchResponseDto {
    private String keyword;
    private Long listId;
    private Date startDate;
    private Date endDate;
    private String memberName;
}
