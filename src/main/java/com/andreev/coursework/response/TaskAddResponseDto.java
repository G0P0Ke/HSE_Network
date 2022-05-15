package com.andreev.coursework.response;

import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;

import java.util.Date;

public class TaskAddResponseDto {
    private String description;
    private Date dateFinish;
    private byte[] pdf;
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
}
