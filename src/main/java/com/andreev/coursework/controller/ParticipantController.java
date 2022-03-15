package com.andreev.coursework.controller;

import com.andreev.coursework.dto.SignUpFormDto;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.exception.paricipant.ParticipantRegistrationException;
import com.andreev.coursework.exception.paricipant.NoSuchParticipantException;
import com.andreev.coursework.service.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class ParticipantController {
    @Autowired
    private ParticipantService participantService;

    @GetMapping()
    public List<Participant> showAllUsers() {
        List<Participant> allUsers = participantService.getAllUsers();
        return allUsers;
    }

    @GetMapping("/{id}")
    public Participant findParticipantById(@PathVariable int id) {
        return participantService.getUser(id);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<String> createNewUser(@RequestBody SignUpFormDto signUpRequest) {
        try {
            participantService.createEmployee(
                signUpRequest.getFirstName(),
                signUpRequest.getSecondName(),
                signUpRequest.getPatronymic(),
                signUpRequest.getMail(),
                signUpRequest.getHashPassword(),
                signUpRequest.getEnabled(),
                signUpRequest.getRole());
        } catch (ParticipantRegistrationException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        return ResponseEntity.ok("Participant registered successfully!");
    }

    @GetMapping("/{id}/tasks")
    public List<Task> getAllTasksByParticipantId(@PathVariable int id) {
        Participant participant = participantService.getUser(id);;
        if (participant == null) {
            throw new NoSuchParticipantException("There is no participant with ID = " + id
                + " in Database");
        }
        List<Task> tasks = participant.getTaskList();
        return tasks;
    }
}
