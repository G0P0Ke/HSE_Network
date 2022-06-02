package com.andreev.coursework.response;

public class SimpleCourseResponseDto {
    private int id;
    private String name;
    private String description;
    private String creatorName;

    public SimpleCourseResponseDto(int id, String name, String description, String creatorName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorName = creatorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

}
