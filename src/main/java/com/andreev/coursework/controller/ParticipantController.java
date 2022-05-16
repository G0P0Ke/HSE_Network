package com.andreev.coursework.controller;

import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.dto.ProfileDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.entity.UserTaskAgent;
import com.andreev.coursework.exception.paricipant.NoParticipantRightsException;
import com.andreev.coursework.exception.paricipant.NoSuchObjectException;
import com.andreev.coursework.service.participant.ParticipantService;
import com.andreev.coursework.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
public class ParticipantController {

    private final ParticipantService participantService;
    private final TaskService taskService;

    public ParticipantController(ParticipantService participantService, TaskService taskService) {
        this.participantService = participantService;
        this.taskService = taskService;
    }

    @GetMapping()
    @Operation(summary = "Получение списка всех пользователей")
    public List<Participant> showAllUsers() {
        return participantService.getAllUsers();
    }

    @GetMapping("/{id}/profile")
    @Operation(summary = "Получение профиля пользователя по id")
    public Participant findParticipantById(@PathVariable int id) {
        return participantService.getUser(id);
    }

    @PutMapping("/{id}/profile/update")
    @Operation(
        summary = "Обновление профиля пользователя",
        description = "id - id пользователя, поля могут быть пустыми"
    )
    public ResponseEntity<String> updateUserProfile(
        @PathVariable int id,
        @RequestBody ProfileDto profileDto
    ) {
        Participant participant = participantService.getUser(id);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + id
                + " in Database");
        }
        participantService.updateProfileUser(participant, profileDto);
        return ResponseEntity.ok("Profile updated");
    }

    @GetMapping("/{id}/task")
    @Operation(
        summary = "Получение всех заданий пользователя",
        description = "id - id пользователя"
    )
    public Set<UserTaskAgent> getAllTasksByParticipantId(@PathVariable int id) {
        Participant participant = participantService.getUser(id);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + id
                + " in Database");
        }
        return participant.getTaskList();
    }

    @GetMapping("/{id}/chat")
    @Operation(
        summary = "Получение всех чатов пользователя",
        description = "id - id пользователя"
    )
    public Set<Chat> getAllChatsByParticipantId(@PathVariable int id) {
        Participant participant = participantService.getUser(id);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + id
                + " in Database");
        }
        return participant.getChatList();
    }

    @PostMapping("/{id}/addCourse")
    @Operation(
        summary = "добавить курс",
        description = "добавлять курс могут только учителя"
    )
    public ResponseEntity<String> addCourse(
        @PathVariable int id,
        @RequestBody CourseDto courseDto
    ) {
        Participant participant = participantService.getUser(id);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + id
                + " in Database");
        } else if (!participant.isTeacher()) {
            throw new NoParticipantRightsException("User with ID = " + id
                + " is not a teacher");
        }
        participantService.addCourse(participant, courseDto);
        return ResponseEntity.ok("Course added");
    }

    @PutMapping("/{userId}/{taskId}/grade")
    @Operation(summary = "выставление студенту оценки за задание")
    public ResponseEntity<String> gradeTask(
        @PathVariable int userId,
        @PathVariable int taskId,
        @RequestBody int grade
    ) {
        Participant participant = participantService.getUser(userId);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + userId
                + " in Database");
        }
        Task task = taskService.showTaskById(taskId);
        if (task == null) {
            throw new NoSuchObjectException("There is no task with ID = " + taskId
                + " in Database");
        }
        participantService.gradeParticipantByTask(participant, task, grade);
        return ResponseEntity.ok("Student with ID = " + userId + " graded by " + grade);
    }
}
