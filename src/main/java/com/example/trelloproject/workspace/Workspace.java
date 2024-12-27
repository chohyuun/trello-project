package com.example.trelloproject.workspace;

import com.example.trelloproject.board.Board;
import com.example.trelloproject.global.entity.BaseEntity;
import com.example.trelloproject.member.Member;
import com.example.trelloproject.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "workspace")
public class Workspace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public Workspace() {}

    public Workspace(User user, String title, String description) {
        this.user = user;
        this.title = title;
        this.description = description;
    }

    public void updateWorkspace(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
