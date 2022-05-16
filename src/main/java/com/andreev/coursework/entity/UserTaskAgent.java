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

    public UserTaskAgentPK getId() {
        return id;
    }

    public void setId(UserTaskAgentPK id) {
        this.id = id;
    }

    public Participant getStudent() {
        return student;
    }

    public void setStudent(Participant student) {
        this.student = student;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
