package com.andreev.coursework.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserTaskAgentPK implements Serializable {

    @Column(name = "student_id")
    private int user_id;

    @Column(name = "task_id")
    private int task_id;

}
