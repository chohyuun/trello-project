package com.example.trelloproject.card.dto;

import com.example.trelloproject.card.CardHistory;
import com.example.trelloproject.card.enums.ActionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
public class CardHistoryResponseDto {
    private Long id;
    private Long cardId;
    private String title;
    private String description;
    private Date dueDate;
    private String userName;    // 수정한 사용자 이름
    private ActionType actionType;
    private LocalDateTime createdAt;  // 변경된 시간

    public CardHistoryResponseDto(CardHistory cardHistory) {
        this.id = cardHistory.getId();
        this.cardId = cardHistory.getCard().getId();
        this.title = cardHistory.getTitle();
        this.description = cardHistory.getDescription();
        this.dueDate = cardHistory.getDueDate();
        this.userName = cardHistory.getMember().getUser().getUserName();
        this.actionType = cardHistory.getActionType();
        this.createdAt = cardHistory.getCreatedAt();
    }
}
