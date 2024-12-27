package com.example.trelloproject.board;

import com.example.trelloproject.global.entity.BaseEntity;
import com.example.trelloproject.list.ListEntity;
import com.example.trelloproject.workspace.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "board")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Setter
    @Column(name = "image_path", length = 500)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ListEntity> lists = new ArrayList<>();

    public Board() {}

    public Board(Workspace workspace, String title) {
        this.workspace = workspace;
        this.title = title;
    }
}
