package com.andreev.coursework.core.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserTaskAgentPK implements Serializable {

    @Column(name = "student_id")
    private int user_id;

    @Column(name = "task_id")
    private int task_id;

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }
}
