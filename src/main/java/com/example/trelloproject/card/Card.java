package com.example.trelloproject.card;

import com.example.trelloproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "card")
@Entity
@Getter
@NoArgsConstructor
public class Card extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private Long listId;

    @Column(name = "title" , nullable = false )
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    @Temporal(TemporalType.DATE)
    private Date dueDate;





}
