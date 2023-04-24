package com.andreev.coursework.input.rest.controller;

import com.andreev.coursework.dto.response.ResponseDto;
import com.andreev.coursework.core.model.Task;
import com.andreev.coursework.core.service.participant.ParticipantService;
import com.andreev.coursework.core.service.task.TaskService;
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
        ResponseDto response = taskService.addTaskToStudent(taskId, authentication, participantService);
        return ResponseEntity.status(response.status()).body(response.message());
    }
}
