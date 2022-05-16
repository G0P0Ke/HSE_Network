package com.andreev.coursework.controller;

import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.service.participant.ParticipantService;
import com.andreev.coursework.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;
    private final ParticipantService participantService;

    public TaskController(TaskService taskService, ParticipantService participantService) {
        this.taskService = taskService;
        this.participantService = participantService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Информация о задании")
    public Task showTask(@PathVariable int id) {
        return taskService.showTaskById(id);
    }

    @PostMapping("/{taskId}/add")
    @Operation(summary = "добавление задания себе")
    public ResponseEntity<String> addTaskToStudent(
        @PathVariable int taskId,
        Authentication authentication
    ) {
        Task task = taskService.showTaskById(taskId);
        if (task == null) {
            return ResponseEntity.badRequest().body("There is no task with ID = " + taskId + " in database");
        }
        Participant student = participantService.findByMail(authentication.getName());
        boolean tryAdd = participantService.addTaskToStudent(student, task);
        if (tryAdd) {
            return ResponseEntity.ok("Task added to student with ID = " + student.getId());
        }
        return ResponseEntity.badRequest().body("Can not add task to student with ID = " + student.getId());
    }
}
