package com.andreev.coursework.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_task")
public class UserTaskAgent {

    @EmbeddedId
    private UserTaskAgentPK id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "student_id")
    private Participant student;

    @ManyToOne
    @MapsId("task_id")
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "grade")
    private int grade;

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
