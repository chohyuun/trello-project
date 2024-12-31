package com.example.trelloproject.card;

import com.example.trelloproject.card.enums.ActionType;
import com.example.trelloproject.global.entity.BaseEntity;
import com.example.trelloproject.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "card_history")
@NoArgsConstructor
@Getter
public class CardHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private Date dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "action_type")
    @Enumerated(EnumType.STRING)
    private ActionType actionType; // CREATE, UPDATE, DELETE


    // 생성자 추가
    public CardHistory(Card card, String title, String description, Date dueDate, Member member, ActionType actionType) {
        this.card = card;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.member = member;
        this.actionType = actionType;
    }
}
