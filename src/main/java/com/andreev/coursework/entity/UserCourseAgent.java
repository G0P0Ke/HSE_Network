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

    public Course getCourse() {
        return course;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(UserCourseAgentPK id) {
        this.id = id;
    }
}
