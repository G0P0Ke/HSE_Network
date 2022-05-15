package com.andreev.coursework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "surname")
    private String secondName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "mail")
    private String mail;

    @JsonIgnore
    @Column(name = "is_teacher")
    private boolean isTeacher;

    @JsonIgnore
    @Column(name = "is_active")
    private boolean isActive;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "creator"
        , fetch = FetchType.LAZY)
    private List<Chat> createdChatList;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_chat"
        , joinColumns = @JoinColumn(name = "user_id")
        , inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private List<Chat> chatList;

    @JsonIgnore
    @OneToMany(mappedBy = "participant")
    private Set<UserCourseAgent> userCourseSet;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private Set<UserTaskAgent> taskList = new HashSet<>();

    @JsonIgnore
    @Column(name = "code")
    private String code;

    public Participant() {
    }

    public Participant(String firstName, String secondName, String patronymic,
        String mail, boolean isTeacher, String password) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.patronymic = patronymic;
        this.mail = mail;
        this.isTeacher = isTeacher;
        this.password = password;
    }

    public void addChatToParticipant(Chat chat) {
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
        chatList.add(chat);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public Set<UserTaskAgent> getTaskList() {
        return taskList;
    }

    public List<Chat> getChatList() {
        return chatList;
    }

    public Set<UserCourseAgent> getUserCourseSet() {
        return userCourseSet;
    }
}
