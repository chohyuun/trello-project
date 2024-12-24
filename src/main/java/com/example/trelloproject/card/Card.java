package com.example.trelloproject.card;

import com.example.trelloproject.global.entity.BaseEntity;
import com.example.trelloproject.list.List;
import com.example.trelloproject.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Table(name = "card")
@Entity
@Getter
@Builder
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

    @Column(name = "title" , nullable = false )
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






}
