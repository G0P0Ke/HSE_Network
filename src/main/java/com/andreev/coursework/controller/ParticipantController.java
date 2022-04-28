package com.andreev.coursework.controller;

import com.andreev.coursework.dto.SignUpFormDto;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.exception.paricipant.ParticipantRegistrationException;
import com.andreev.coursework.exception.paricipant.NoSuchParticipantException;
import com.andreev.coursework.service.participant.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String email) {
        this.participantService.loginUser(email);
        return ResponseEntity.ok("Code send successfully!");
    }


//    @PostMapping("")
//    @PreAuthorize("hasRole('TEACHER')")
//    public ResponseEntity<String> createNewUser(@RequestBody SignUpFormDto signUpRequest) {
//        try {
//            participantService.createEmployee(
//                signUpRequest.getFirstName(),
//                signUpRequest.getSecondName(),
//                signUpRequest.getPatronymic(),
//                signUpRequest.getMail(),
//                signUpRequest.isTeacher());
//        } catch (ParticipantRegistrationException ex) {
//            return ResponseEntity.badRequest().body(ex.getMessage());
//        }
//
//        return ResponseEntity.ok("Participant registered successfully!");
//    }

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
