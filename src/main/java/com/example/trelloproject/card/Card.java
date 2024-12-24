package com.example.trelloproject.card;

import com.example.trelloproject.global.entity.BaseEntity;
import com.example.trelloproject.list.List;
import com.example.trelloproject.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.Setter;


import java.util.Date;

@Table(name = "card")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private List list;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file")
    private CardFile cardFile;


    public void update(String title, String description, Date dueDate) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (dueDate != null) this.dueDate = dueDate;
    }

    public Card(Long id, String title, String description, Date dueDate, Member member, List list, CardFile cardFile) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.member = member;
        this.list = list;
        this.cardFile = cardFile;
    }

    public Card(String title, String description, Date dueDate, Member member, List list, CardFile cardFile) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.member = member;
        this.list = list;
        this.cardFile = cardFile;
    }


    public Card(String title, String description, Date dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }





}
