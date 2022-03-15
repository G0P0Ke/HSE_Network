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
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ParticipantService participantService;

    @PostMapping()
    @PreAuthorize("hasRole('TEACHER') or hasRole('ASSISTANT')")
    public ResponseEntity<String> createNewTask(@RequestParam("file")MultipartFile file,
        @RequestParam("description") String description, @RequestParam("creator_id") int creatorId,
        @RequestParam("date_finish") String date) {
        Participant participant = participantService.getUser(creatorId);;
        if (participant == null) {
            throw new NoSuchParticipantException("There is no creator with ID = " + creatorId
                + " in Database");
        }
        taskService.createTask(description, date, file, creatorId);
        return ResponseEntity.ok("Task created successfully!");
    }

    @GetMapping("/{id}")
    public Task showTask(@PathVariable int id) {
        return taskService.showTaskById(id);
    }

    @GetMapping("/download/{id}")
    public void downloadTask(@PathVariable int id) {
        taskService.downloadTaskById(id);
    }

}
