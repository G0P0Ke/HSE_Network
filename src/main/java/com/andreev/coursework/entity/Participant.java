package com.andreev.coursework.entity;

import com.andreev.coursework.entity.security.Role;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "hash_password")
    private String hashPassword;

    @Column(name = "enabled")
    private byte enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_task"
        , joinColumns = @JoinColumn(name = "student_id")
        , inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> taskList;

    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "creator"
        , fetch = FetchType.LAZY)
    private List<Task> createdTasks;

    public Participant() {
    }

    public Participant(String firstName, String secondName, String patronymic,
        String mail, String hashPassword, byte enabled) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.patronymic = patronymic;
        this.mail = mail;
        this.hashPassword = hashPassword;
        this.enabled = enabled;
    }

    public void addTaskToList(Task task) {
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        taskList.add(task);
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public byte getEnabled() {
        return enabled;
    }

    public void setEnabled(byte enabled) {
        this.enabled = enabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
