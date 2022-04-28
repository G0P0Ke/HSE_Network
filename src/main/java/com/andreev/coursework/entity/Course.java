package com.andreev.coursework.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "course"
        , fetch = FetchType.LAZY)
    private List<Chat> chatList;

    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "course"
        , fetch = FetchType.LAZY)
    private List<Task> taskList;

    @OneToMany(mappedBy = "course")
    private Set<UserCourseAgent> participants;

    public Course() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Chat> getChatList() {
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
        return chatList;
    }

    public List<Task> getTaskList() {
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        return taskList;
    }

    public Set<UserCourseAgent> getParticipants() {
        if (participants == null) {
            participants = new HashSet<>();
        }
        return participants;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
