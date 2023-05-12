package com.andreev.coursework.dto.response;

import java.util.Date;

public class TaskResponseDto {
    private String description;
    private Date dateFinish;

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
}
