package com.andreev.coursework.controller;

import com.andreev.coursework.dto.ProfileDto;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.exception.paricipant.NoSuchParticipantException;
import com.andreev.coursework.service.participant.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/profile/update")
    public ResponseEntity<String> updateUserProfile(@PathVariable int id,
        @RequestBody ProfileDto profileDto) {
        Participant participant = participantService.getUser(id);
        if (participant == null) {
            throw new NoSuchParticipantException("There is no participant with ID = " + id
                + " in Database");
        }
        participantService.updateProfileUser(participant, profileDto);
        return ResponseEntity.ok("Profile updated");
    }

    @GetMapping("/{id}/tasks")
    public List<Task> getAllTasksByParticipantId(@PathVariable int id) {
        Participant participant = participantService.getUser(id);
        if (participant == null) {
            throw new NoSuchParticipantException("There is no participant with ID = " + id
                + " in Database");
        }
//        return participant.getTaskList();
        return null;
    }
}
