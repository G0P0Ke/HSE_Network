package com.andreev.coursework.entity;

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
}
