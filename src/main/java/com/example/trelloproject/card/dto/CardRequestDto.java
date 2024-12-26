package com.example.trelloproject.card.dto;

import com.example.trelloproject.card.Card;
import com.example.trelloproject.card.CardFile;
import com.example.trelloproject.list.List;
import com.example.trelloproject.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@NoArgsConstructor
public class CardRequestDto {
    private Long id;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    private Member member;
    private List list;
    private MultipartFile cardFile;


    public CardRequestDto(Card card){
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
        this.member = card.getMember();
        this.list = card.getList();
    }
}

