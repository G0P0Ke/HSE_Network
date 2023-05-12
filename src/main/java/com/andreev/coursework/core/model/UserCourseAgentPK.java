package com.andreev.coursework.core.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserCourseAgentPK implements Serializable {

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "course_id")
    private int course_id;

    @Column(name = "role_id")
    private int role_id;

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }
}
