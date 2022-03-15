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

    @OneToOne
    @JoinColumn(name = "from_id")
    private Participant userFromId;

    @OneToOne
    @JoinColumn(name = "to_id")
    private Participant userToId;

    @OneToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    public Message() {
    }

    public Message(String content, Date dateSend, Participant userFromId, Participant userToId, Channel channel) {
        this.content = content;
        this.dateSend = dateSend;
        this.userFromId = userFromId;
        this.userToId = userToId;
        this.channel = channel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public Participant getUserFromId() {
        return userFromId;
    }

    public void setUserFromId(Participant userFromId) {
        this.userFromId = userFromId;
    }

    public Participant getUserToId() {
        return userToId;
    }

    public void setUserToId(Participant userToId) {
        this.userToId = userToId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
