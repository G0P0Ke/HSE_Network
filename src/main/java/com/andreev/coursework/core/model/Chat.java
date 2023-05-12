package com.andreev.coursework.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "creator_id")
    private Participant creator;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST},
        fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_chat"
        , joinColumns = @JoinColumn(name = "chat_id")
        , inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Participant> participantList;

    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "chat"
        , fetch = FetchType.LAZY)
    private List<Message> messages;

    public Chat() {
    }

    public Chat(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Participant getCreator() {
        return creator;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setCreator(Participant creator) {
        this.creator = creator;
    }

    public Course getCourse() {
        return course;
    }

    public void addMember(Participant participant) {
        if (participantList == null) {
            participantList = new HashSet<>();
        }
        participantList.add(participant);
    }

    public Set<Participant> getParticipantList() {
        if (participantList == null) {
            participantList = new HashSet<>();
        }
        return participantList;
    }

    public List<Message> getMessages() {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        return messages;
    }
}
