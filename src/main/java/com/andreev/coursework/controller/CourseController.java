package com.andreev.coursework.controller;

import com.andreev.coursework.dto.ChatDto;
import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.dto.ResponseDto;
import com.andreev.coursework.dto.StudentAddDto;
import com.andreev.coursework.entity.*;
import com.andreev.coursework.response.ChatResponseDto;
import com.andreev.coursework.response.CourseResponseDto;
import com.andreev.coursework.response.TaskAddResponseDto;
import com.andreev.coursework.service.chat.ChatService;
import com.andreev.coursework.service.course.CourseService;
import com.andreev.coursework.service.participant.ParticipantService;
import com.andreev.coursework.service.task.TaskService;
import com.andreev.coursework.service.userCourseAgent.UserCourseAgentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private final TaskService taskService;
    private final CourseService courseService;
    private final ParticipantService participantService;
    private final UserCourseAgentService userCourseAgentService;
    private final ChatService chatService;

    public CourseController(
            CourseService courseService,
            ParticipantService participantService,
            UserCourseAgentService userCourseAgentService,
            TaskService taskService,
            ChatService chatService) {
        this.courseService = courseService;
        this.participantService = participantService;
        this.userCourseAgentService = userCourseAgentService;
        this.taskService = taskService;
        this.chatService = chatService;
    }

    @GetMapping("/{courseId}")
    @Operation(
            summary = "Получение информации о курсе по его Id",
            description = "Если пользователь не член курса, то доступа не будет"
    )
    public ResponseEntity<CourseResponseDto> getCourse(
            @PathVariable int courseId,
            Authentication authentication
    ) {
        Course course = courseService.checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
        return ResponseEntity.ok(
                new CourseResponseDto(course.getName(), course.getDescription(), course.getCourseParticipants())
        );
    }

    @PostMapping("/{courseId}/addStudent")
    @Operation(
            summary = "Добавление студента к курсу",
            description = "Если запрос к ручке делает не учитель, создавший курс, то доступа не будет"
    )
    public ResponseEntity<CourseResponseDto> addStudentToCourse(
            @PathVariable int courseId,
            Authentication authentication,
            @RequestBody StudentAddDto studentAddDto
    ) {
        CourseResponseDto response = courseService.addStudentForCourse(courseId, studentAddDto, authentication,
                participantService, userCourseAgentService);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{courseId}/participant")
    @Operation(
            summary = "Получение всего списка пользователей курса",
            description = "если запрос делает не член курса, то доступа не будет"
    )
    public Set<Participant> getCourseParticipant(
            @PathVariable int courseId,
            Authentication authentication
    ) {
        Course course = courseService.checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
        return course.getCourseParticipants();
    }

    @PostMapping("/{courseId}/addTask")
    @Operation(
            summary = "Добавление задания к курсу",
            description = "Может делать только учитель"
    )
    public ResponseEntity<TaskAddResponseDto> addTaskToCourse(
            @PathVariable int courseId,
            @RequestParam("description") String description,
            @RequestParam("date_finish") String date,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        TaskAddResponseDto taskAddResponseDto = courseService.addTaskToCourse(courseId, description, date, file,
                authentication, participantService, userCourseAgentService, taskService);
        return ResponseEntity.ok(taskAddResponseDto);
    }

    @GetMapping("/{courseId}/task")
    @Operation(
            summary = "Получить все задания в курсе",
            description = "courseId - id курса, подставляемый в url"
    )
    public List<Task> getAllTask(@PathVariable int courseId, Authentication authentication) {
        Course course = courseService.checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
        return course.getTaskList();
    }

    @PostMapping("/{courseId}/download/{taskId}")
    @Operation(
            summary = "Скачать задание по courseId & taskId",
            description = "Скачать могут только члены курса"
    )
    public void downloadTask(
            @PathVariable int courseId,
            @PathVariable int taskId,
            Authentication authentication,
            @RequestBody String path
    ) {
        Course course = courseService.checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
        taskService.downloadTaskById(taskId, path);
    }

    @PostMapping("/{courseId}/addChat")
    @Operation(summary = "Создание чата в курсе")
    public ResponseEntity<ChatResponseDto> addChat(
            @PathVariable int courseId,
            Authentication authentication,
            @RequestBody ChatDto chatDto
    ) {
        ChatResponseDto chatResponseDto = courseService.addChat(courseId, chatDto, authentication,
                participantService, userCourseAgentService, chatService);
        return ResponseEntity.ok(chatResponseDto);
    }

    @GetMapping("/{courseId}/chat")
    @Operation(summary = "Получение всех чатов курса по его Id")
    public List<Chat> getChat(@PathVariable int courseId, Authentication authentication) {
        Course course = courseService.checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
        return courseService.getChat(course);
    }

    @PutMapping("/{courseId}/ ")
    @Operation(summary = "Обновление инфомрации о курсе")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable int courseId,
            @RequestBody CourseDto courseDto,
            Authentication authentication
    ) {
        CourseResponseDto courseResponseDto = courseService.updateCourse(courseId, courseDto, authentication,
                participantService, userCourseAgentService);
        return ResponseEntity.ok(courseResponseDto);
    }

    @PostMapping("/{courseId}/{taskId}/addAnswer")
    @Operation(summary = "Добавление ответа на задание")
    public ResponseEntity<String> addTask(
            @PathVariable int courseId,
            @PathVariable int taskId,
            @RequestParam("date_send") String date,
            @RequestParam("file") MultipartFile solution,
            Authentication authentication
    ) {
        ResponseDto response = courseService.addTask(courseId, taskId, date, solution, authentication,
                participantService, taskService, userCourseAgentService);
        return ResponseEntity.status(response.status()).body(response.message());
    }


}
