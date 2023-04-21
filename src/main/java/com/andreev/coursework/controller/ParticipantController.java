package com.andreev.coursework.controller;

import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.dto.ProfileDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.entity.UserTaskAgent;
import com.andreev.coursework.exception.paricipant.NoParticipantRightsException;
import com.andreev.coursework.exception.paricipant.NoSuchObjectException;
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
        Participant participant = participantService.getUser(id);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + id
                    + " in Database");
        }
        participantService.updateProfileUser(participant, profileDto);
        return ResponseEntity.ok("Profile updated");
    }

    @GetMapping("/getCourse")
    @Operation(summary = "Получить список всех курсов пользователя")
    public List<SimpleCourseResponseDto> getAllCourse(Authentication authentication) {
        Participant participant = participantService.findByMail(authentication.getName());
        List<Course> courseList = participantService.getAllCourses(participant);

        List<SimpleCourseResponseDto> answer = new ArrayList<>();
        for (Course course : courseList) {
            String name = courseService.getCreatorName(course);
            answer.add(new SimpleCourseResponseDto(course.getId(), course.getName(), course.getDescription(), name));
        }

        return answer;
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
            @Valid @RequestBody CourseDto courseDto
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
