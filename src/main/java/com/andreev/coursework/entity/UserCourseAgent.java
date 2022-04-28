package com.andreev.coursework.entity;

import com.andreev.coursework.entity.security.Role;

import javax.persistence.*;

@Entity
@Table(name = "user_course_agent")
public class UserCourseAgent {

    @EmbeddedId
    private UserCourseAgentPK id;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private Participant participant;

    @ManyToOne
    @MapsId("course_id")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @MapsId("role_id")
    @JoinColumn(name = "role_id")
    private Role role;

    public Role getRole() {
        return role;
    }
}
