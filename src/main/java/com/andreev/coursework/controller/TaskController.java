package com.andreev.coursework.controller;

import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.exception.paricipant.NoSuchParticipantException;
import com.andreev.coursework.service.participant.ParticipantService;
import com.andreev.coursework.service.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ParticipantService participantService;


    @GetMapping("/{id}")
    public Task showTask(@PathVariable int id) {
        return taskService.showTaskById(id);
    }


}
