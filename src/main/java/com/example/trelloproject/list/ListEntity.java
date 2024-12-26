package com.example.trelloproject.list;

import com.example.trelloproject.board.Board;
import com.example.trelloproject.card.Card;
import com.example.trelloproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "list")
public class ListEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Setter
    @Column(name = "list_index")
    private Integer index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "list", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Card> cards = new ArrayList<>();

    public ListEntity() {}

    public ListEntity(Board board, String title) {
        this.board = board;
        this.title = title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
