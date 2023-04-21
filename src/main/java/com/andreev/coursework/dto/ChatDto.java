package com.andreev.coursework.dto;

import javax.validation.constraints.Size;

public class ChatDto {
    @Size(min = 1, max = 500)
    private String description;

    public String getDescription() {
        return description;
    }
}
