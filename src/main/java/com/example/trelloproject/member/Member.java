package com.example.trelloproject.member;

import com.example.trelloproject.global.entity.BaseEntity;
import com.example.trelloproject.user.User;
import com.example.trelloproject.workspace.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'READONLY'")
    private MemberRole role;

    @Setter
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isActive;

    public Member() {}

    public Member(Workspace workspace, User user, MemberRole role) {
        this.workspace = workspace;
        this.user = user;
        this.role = role;
    }
}
