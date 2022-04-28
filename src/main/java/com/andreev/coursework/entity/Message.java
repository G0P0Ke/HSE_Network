package com.andreev.coursework.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "content")
    private String content;

    @Column(name = "date_send")
    private Date dateSend;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "user_id")
    private Participant sender;

    public Message() {
    }
}
