package com.example.trelloproject.card;

import com.example.trelloproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "cardfile")
@NoArgsConstructor
public class CardFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "file", nullable = false)
    private String file;

    public CardFile(Card card, String file) {
        this.card = card;
        this.file = file;
    }

}
