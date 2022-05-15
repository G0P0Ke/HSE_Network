package com.andreev.coursework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "date_ending")
    private Date dateFinish;

    @Column(name = "data")
    @Type(type="org.hibernate.type.MaterializedBlobType")
    private byte[] pdf;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "creator_id")
    private Participant creator;

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinColumn(name = "course_id")
    private Course course;

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private Set<UserTaskAgent> userList;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL
        , mappedBy = "task"
        , fetch = FetchType.LAZY)
    private List<Answer> answerList;

    public Task() {
    }

    public Task(String description, Date dateFinish, byte[] pdf, Participant creator) {
        this.description = description;
        this.dateFinish = dateFinish;
        this.pdf = pdf;
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }

    public Participant getCreator() {
        return creator;
    }

    public void setCreator(Participant creator) {
        this.creator = creator;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<UserTaskAgent> getUserList() {
        if (userList == null) {
            userList = new HashSet<>();
        }
        return userList;
    }

    public void setUserList(Set<UserTaskAgent> userList) {
        this.userList = userList;
    }

    public List<Answer> getAnswerList() {
        if (answerList == null) {
            answerList = new ArrayList<>();
        }
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }
}
