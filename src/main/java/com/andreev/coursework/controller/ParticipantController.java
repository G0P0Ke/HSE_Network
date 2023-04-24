package com.andreev.coursework.controller;

import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.dto.ProfileDto;
import com.andreev.coursework.dto.ResponseDto;
import com.andreev.coursework.entity.*;
import com.andreev.coursework.response.SimpleCourseResponseDto;
import com.andreev.coursework.service.course.CourseService;
import com.andreev.coursework.service.participant.ParticipantService;
import com.andreev.coursework.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class ParticipantController {

    private final ParticipantService participantService;
    private final TaskService taskService;
    private final CourseService courseService;

    public ParticipantController(ParticipantService participantService, TaskService taskService,
                                 CourseService courseService) {
        this.participantService = participantService;
        this.taskService = taskService;
        this.courseService = courseService;
    }

    @GetMapping()
    @Operation(summary = "Получение списка всех пользователей")
    public List<Participant> showAllUsers() {
        return participantService.getAllUsers();
    }

    @GetMapping("/profile")
    @Operation(summary = "Получение профиля пользователя по id")
    public Participant findParticipantById(Authentication authentication) {
        return participantService.findByMail(authentication.getName());
    }

    @PutMapping("/{id}/profile/update")
    @Operation(
            summary = "Обновление профиля пользователя",
            description = "id - id пользователя, поля могут быть пустыми"
    )
    public ResponseEntity<String> updateUserProfile(
            @PathVariable int id,
            @Valid @RequestBody ProfileDto profileDto
    ) {
        ResponseDto response = participantService.updateUserProfile(id, profileDto);
        return ResponseEntity.status(response.status()).body(response.message());
    }

    @GetMapping("/getCourse")
    @Operation(summary = "Получить список всех курсов пользователя")
    public List<SimpleCourseResponseDto> getAllCourse(Authentication authentication) {
        return participantService.getAllCourses(authentication, courseService);
    }

    @GetMapping("/{id}/task")
    @Operation(
            summary = "Получение всех заданий пользователя",
            description = "id - id пользователя"
    )
    public Set<UserTaskAgent> getAllTasksByParticipantId(@PathVariable int id) {
        return participantService.getAllTasksByParticipantId(id);
    }

    @GetMapping("/{id}/chat")
    @Operation(
            summary = "Получение всех чатов пользователя",
            description = "id - id пользователя"
    )
    public Set<Chat> getAllChatsByParticipantId(@PathVariable int id) {
        return participantService.getAllChatsByParticipantId(id);
    }

    @PostMapping("/{id}/addCourse")
    @Operation(
            summary = "добавить курс",
            description = "добавлять курс могут только учителя"
    )
    public ResponseEntity<String> addCourse(
            @PathVariable int id,
            @Valid @RequestBody CourseDto courseDto
    ) {
        ResponseDto response = participantService.addCourse(id, courseDto);
        return ResponseEntity.ok(response.message());
    }

    @PutMapping("/{userId}/{taskId}/grade")
    @Operation(summary = "выставление студенту оценки за задание")
    public ResponseEntity<String> gradeTask(
            @PathVariable int userId,
            @PathVariable int taskId,
            @RequestBody int grade
    ) {
        ResponseDto response = participantService.gradeTask(userId, taskId, grade, taskService, participantService);
        return ResponseEntity.ok(response.message());
    }
}
