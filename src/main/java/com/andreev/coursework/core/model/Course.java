package com.andreev.coursework.core.model;

import com.andreev.coursework.core.model.security.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "course"
        , fetch = FetchType.LAZY)
    private List<Chat> chatList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "course"
        , fetch = FetchType.LAZY)
    private List<Task> taskList;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private Set<UserCourseAgent> participants;

    public Course() {
    }

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UserCourseAgent addParticipant(Participant participant, Role role) {
        UserCourseAgent userCourseAgent = new UserCourseAgent();
        userCourseAgent.setCourse(this);
        userCourseAgent.setParticipant(participant);
        userCourseAgent.setRole(role);

        UserCourseAgentPK userCourseAgentPK = new UserCourseAgentPK();
        userCourseAgentPK.setCourse_id(this.getId());
        userCourseAgentPK.setUser_id(participant.getId());
        userCourseAgentPK.setRole_id(role.getId());

        userCourseAgent.setId(userCourseAgentPK);

        this.getParticipants().add(userCourseAgent);
        participant.getUserCourseSet().add(userCourseAgent);

        return userCourseAgent;
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

    public Set<Participant> getCourseParticipants() {
        if (participants == null) {
            participants = new HashSet<>();
        }
        Set<Participant> courseParticipant = new HashSet<>();
        for (var el : participants) {
            courseParticipant.add(el.getParticipant());
        }
        return courseParticipant;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void addToTaskList(Task task) {
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        taskList.add(task);
        task.setCourse(this);
    }

    public void addChatToList(Chat chat) {
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
        chatList.add(chat);
        chat.setCourse(this);
    }
}
