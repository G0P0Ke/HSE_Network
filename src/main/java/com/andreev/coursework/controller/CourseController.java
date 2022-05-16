package com.andreev.coursework.controller;

import com.andreev.coursework.dto.ChatDto;
import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.dto.StudentAddDto;
import com.andreev.coursework.entity.*;
import com.andreev.coursework.exception.course.NoSuchCourseException;
import com.andreev.coursework.exception.paricipant.NoParticipantRightsException;
import com.andreev.coursework.exception.paricipant.NoSuchObjectException;
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
        Course course = checkCourseAndUserData(courseId, authentication);
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
        Course course = checkCourseAndUserData(courseId, authentication);
        Participant teacher = participantService.findByMail(authentication.getName());
        if (!teacher.isTeacher()) {
            throw new NoParticipantRightsException("User with ID = " + teacher.getId()
                + " is not a teacher");
        }
        Participant student = participantService.getUser(studentAddDto.getStudentId());
        if (student == null) {
            throw new NoSuchObjectException("There isn't student with id " + studentAddDto.getStudentId()
                + " in Database");
        }
        courseService.addStudent(course, student, studentAddDto.getRoleName());
        return ResponseEntity.ok(
            new CourseResponseDto(course.getName(), course.getDescription(), course.getCourseParticipants())
        );
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
        Course course = checkCourseAndUserData(courseId, authentication);
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
        Course course = checkCourseAndUserData(courseId, authentication);
        Participant teacher = participantService.findByMail(authentication.getName());
        if (!teacher.isTeacher()) {
            throw new NoParticipantRightsException("User with ID = " + teacher.getId()
                + " is not a teacher");
        }
        Task task = taskService.createTask(description, date, file, teacher.getId(), course);
        TaskAddResponseDto taskAddResponseDto = new TaskAddResponseDto();
        taskAddResponseDto.setCourse(task.getCourse());
        taskAddResponseDto.setCreator(task.getCreator());
        taskAddResponseDto.setDescription(task.getDescription());
        taskAddResponseDto.setDateFinish(task.getDateFinish());
        taskAddResponseDto.setPdf(task.getPdf());
        return ResponseEntity.ok(taskAddResponseDto);
    }

    @GetMapping("/{courseId}/task")
    @Operation(
        summary = "Получить все задания в курсе",
        description = "courseId - id курса, подставляемый в url"
    )
    public List<Task> getAllTask(@PathVariable int courseId, Authentication authentication) {
        Course course = checkCourseAndUserData(courseId, authentication);
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
        Course course = checkCourseAndUserData(courseId, authentication);
        taskService.downloadTaskById(taskId, path);
    }

    @PostMapping("/{courseId}/addChat")
    @Operation(summary = "Создание чата в курсе")
    public ResponseEntity<ChatResponseDto> addChat(
        @PathVariable int courseId,
        Authentication authentication,
        @RequestBody ChatDto chatDto
    ) {
        Course course = checkCourseAndUserData(courseId, authentication);
        if (!checkUserRoleInCourse(course, authentication)) {
            throw new NoParticipantRightsException("This user does not have the role of teacher and assistant");
        }
        Participant creator = participantService.findByMail(authentication.getName());
        Chat chat = courseService.addChat(course, chatDto, creator);
        Chat infoChat = chatService.getChatByCourseAndDescription(chat.getDescription(), course);
        return ResponseEntity.ok(new ChatResponseDto(infoChat.getId(), infoChat.getDescription(), infoChat.getCreator().getMail()));
    }

    @GetMapping("/{courseId}/chat")
    @Operation(summary = "Получение всех чатов курса по его Id")
    public List<Chat> getChat(@PathVariable int courseId, Authentication authentication) {
        Course course = checkCourseAndUserData(courseId, authentication);
        return courseService.getChat(course);
    }

    @PutMapping("/{courseId}/update")
    @Operation(summary = "Обновление инфомрации о курсе")
    public ResponseEntity<CourseResponseDto> updateCourse(
        @PathVariable int courseId,
        @RequestBody CourseDto courseDto,
        Authentication authentication
    ) {
        Course course = checkCourseAndUserData(courseId, authentication);
        Participant teacher = participantService.findByMail(authentication.getName());
        if (!teacher.isTeacher()) {
            throw new NoParticipantRightsException("User with ID = " + teacher.getId()
                + " is not a teacher");
        }
        Course updatedCourse = courseService.updateCourseInfo(course, courseDto);
        CourseResponseDto courseResponseDto = new CourseResponseDto();
        courseResponseDto.setDescription(updatedCourse.getDescription());
        courseResponseDto.setName(updatedCourse.getName());
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
        Course course = checkCourseAndUserData(courseId, authentication);
        Participant student = participantService.findByMail(authentication.getName());
        Task task = taskService.showTaskById(taskId);
        if (task == null) {
            throw new NoSuchObjectException("There is no task with ID = " + taskId
                + " in Database");
        }
        Answer answer = taskService.addAnswer(date, solution, task, student);
        if (answer == null) {
            return ResponseEntity.badRequest().body("Can not add answer");
        }
        return ResponseEntity.ok("Answer added");
    }

    public boolean checkUserRoleInCourse(Course course, Authentication authentication) {
        Participant participant = participantService.findByMail(authentication.getName());
        UserCourseAgent userCourseAgent = userCourseAgentService.findUserCourseAgent(course, participant);
        return switch (userCourseAgent.getRole().getName()) {
            case ROLE_ASSISTANT -> true;
            case ROLE_TEACHER -> true;
            default -> false;
        };
    }

    public Course checkCourseAndUserData(int courseId, Authentication authentication) {
        Course course = courseService.findById(courseId);
        if (course == null) {
            throw new NoSuchCourseException("There is no course with ID = " + courseId
                + " in Database");
        }
        Participant participant = participantService.findByMail(authentication.getName());
        UserCourseAgent userCourseAgent = userCourseAgentService.findUserCourseAgent(course, participant);
        if (userCourseAgent == null) {
            throw new NoSuchObjectException("There isn't participant with id " + participant.getId()
                + " in course with id " + courseId);
        }
        return course;
    }
}
