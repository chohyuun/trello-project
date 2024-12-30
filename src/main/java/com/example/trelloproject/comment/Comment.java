package com.example.trelloproject.comment;

import com.example.trelloproject.card.Card;
import com.example.trelloproject.global.entity.BaseEntity;
import com.example.trelloproject.global.exception.BusinessException;
import com.example.trelloproject.global.exception.ExceptionType;
import com.example.trelloproject.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comment")
@Getter
@Entity
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    public Comment(String contents, Member member, Card card) {
        this.contents = contents;
        this.member = member;
        this.card = card;
    }

    public void update(String contents) {
        this.contents = contents;
    }

    /**
     * 해당 글 작성자인지 확인
     */
    public void isUserComment(String memberEmail) {
        if (!member.getUser().getEmail().equals(memberEmail)) {
            throw new BusinessException(ExceptionType.NOT_CONSTRUCTOR);
        }
    }

    /**
     * 카드와 댓글의 연관 관계 확인
     */
    public void isCardToComment(Long cardId) {
        if (!card.getId().equals(cardId)) {
            throw new BusinessException(ExceptionType.NOT_CARD_TO_COMMENT);
        }
    }
}
