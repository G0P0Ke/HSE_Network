package com.andreev.coursework.entity;

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

    @Column(name = "isTeacher")
    private boolean isTeacher;

    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "creator"
        , fetch = FetchType.LAZY)
    private List<Chat> createdChatList;

    @ManyToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_chat"
        , joinColumns = @JoinColumn(name = "user_id")
        , inverseJoinColumns = @JoinColumn(name = "chat_id")
    )
    private List<Chat> chatList;

    @OneToMany(mappedBy = "participant")
    private Set<UserCourseAgent> userCourseSet;

    @OneToMany(mappedBy = "student")
    private Set<UserTaskAgent> taskList = new HashSet<>();

    private String code;

    public Participant() {
    }

    public Participant(String firstName, String secondName, String patronymic,
        String mail, boolean isTeacher) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.patronymic = patronymic;
        this.mail = mail;
        this.isTeacher = isTeacher;
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
}
