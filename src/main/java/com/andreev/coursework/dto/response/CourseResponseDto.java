package com.andreev.coursework.dto.response;

import com.andreev.coursework.core.model.Participant;

import java.util.Set;

public class CourseResponseDto {
    private String name;
    private String description;
    private Set<Participant> participantSet;

    public CourseResponseDto() {
    }

    public CourseResponseDto(String name, String description, Set<Participant> participantSet) {
        this.name = name;
        this.description = description;
        this.participantSet = participantSet;
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

    public Set<Participant> getParticipantSet() {
        return participantSet;
    }

    public void setParticipantSet(Set<Participant> participantSet) {
        this.participantSet = participantSet;
    }
}
