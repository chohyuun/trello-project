package com.example.trelloproject.list;

import com.example.trelloproject.board.Board;
import com.example.trelloproject.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "list")
public class List extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "list_index")
    private Integer index;

    @Setter
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public List() {}

    public List(String title, int index) {
        this.title = title;
        this.index = index;
    }
}
