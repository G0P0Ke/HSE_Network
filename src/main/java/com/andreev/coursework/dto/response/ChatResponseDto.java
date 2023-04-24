package com.andreev.coursework.dto.response;

public class ChatResponseDto {
    private int id;
    private String description;
    private String creatorMail;

    public ChatResponseDto(int id, String description, String creatorMail) {
        this.id = id;
        this.description = description;
        this.creatorMail = creatorMail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorMail() {
        return creatorMail;
    }

    public void setCreatorMail(String creatorMail) {
        this.creatorMail = creatorMail;
    }
}
