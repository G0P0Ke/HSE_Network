package com.andreev.coursework.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "creator_id")
    private Participant creator;

    @ManyToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_chat"
        , joinColumns = @JoinColumn(name = "chat_id")
        , inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Participant> participantList;

    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "chat"
        , fetch = FetchType.LAZY)
    private List<Message> messages;

    public Chat() {
    }


}
