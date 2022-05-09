package com.andreev.coursework.controller;

import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.exception.paricipant.NoSuchParticipantException;
import com.andreev.coursework.service.participant.ParticipantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping()
    public List<Participant> showAllUsers() {
        return participantService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Participant findParticipantById(@PathVariable int id) {
        return participantService.getUser(id);
    }

    @GetMapping("/{id}/tasks")
    public List<Task> getAllTasksByParticipantId(@PathVariable int id) {
        Participant participant = participantService.getUser(id);;
        if (participant == null) {
            throw new NoSuchParticipantException("There is no participant with ID = " + id
                + " in Database");
        }
//        return participant.getTaskList();
        return null;
    }
}
