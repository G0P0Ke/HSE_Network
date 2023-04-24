package com.andreev.coursework.dto.response;

import com.andreev.coursework.core.model.Course;
import com.andreev.coursework.core.model.Participant;

import java.util.Date;

public class TaskAddResponseDto {
    private String description;
    private Date dateFinish;
    private Participant creator;
    private Course course;

    public TaskAddResponseDto() {
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
}
