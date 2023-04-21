package com.andreev.coursework.dto;

import javax.validation.constraints.Size;

public class CourseDto {
    @Size(min = 1, max = 50)
    private String name;
    @Size(min = 1, max = 500)
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
