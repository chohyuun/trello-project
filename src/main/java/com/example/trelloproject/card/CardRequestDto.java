package com.example.trelloproject.card;

import com.example.trelloproject.list.List;
import com.example.trelloproject.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private CardFile cardFile;

}
