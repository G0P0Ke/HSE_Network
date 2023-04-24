package com.andreev.coursework.service.course;

import com.andreev.coursework.dao.ChatRepository;
import com.andreev.coursework.dao.CourseRepository;
import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dao.RoleRepository;
import com.andreev.coursework.dao.UserCourseAgentRepository;
import com.andreev.coursework.dto.ChatDto;
import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.dto.ResponseDto;
import com.andreev.coursework.dto.StudentAddDto;
import com.andreev.coursework.entity.Answer;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.entity.UserCourseAgent;
import com.andreev.coursework.entity.security.Role;
import com.andreev.coursework.entity.security.RoleName;
import com.andreev.coursework.exception.course.NoSuchCourseException;
import com.andreev.coursework.exception.paricipant.NoParticipantRightsException;
import com.andreev.coursework.exception.paricipant.NoSuchObjectException;
import com.andreev.coursework.exception.paricipant.ParticipantRegistrationException;
import com.andreev.coursework.response.ChatResponseDto;
import com.andreev.coursework.response.CourseResponseDto;
import com.andreev.coursework.response.TaskAddResponseDto;
import com.andreev.coursework.service.chat.ChatService;
import com.andreev.coursework.service.participant.ParticipantService;
import com.andreev.coursework.service.task.TaskService;
import com.andreev.coursework.service.userCourseAgent.UserCourseAgentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final UserCourseAgentRepository userCourseAgentRepository;
    private final RoleRepository roleRepository;
    private final CourseRepository courseRepository;
    private final ParticipantRepository participantRepository;
    private final ChatRepository chatRepository;

    public CourseServiceImpl(CourseRepository courseRepository, RoleRepository roleRepository, UserCourseAgentRepository userCourseAgentRepository,
                             ParticipantRepository participantRepository, ChatRepository chatRepository) {
        this.courseRepository = courseRepository;
        this.roleRepository = roleRepository;
        this.userCourseAgentRepository = userCourseAgentRepository;
        this.participantRepository = participantRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public ResponseDto getCourse(int courseId, Authentication authentication,
                                 ParticipantService participantService,
                                 UserCourseAgentService userCourseAgentService) {
        Course course = checkCourseAndUserData(courseId, authentication, participantService, userCourseAgentService);
        return new ResponseDto(HttpStatus.OK,
                (new CourseResponseDto(course.getName(), course.getDescription(), course.getCourseParticipants())).toString());
    }

    @Override
    public Course findById(int courseId) {
        return courseRepository.getById(courseId);
    }

    @Override
    public String getCreatorName(Course course) {
        Participant teacher = null;
        for (UserCourseAgent el : course.getParticipants()) {
            if (el.getParticipant().isTeacher()) {
                teacher = el.getParticipant();
                break;
            }
        }
        return String.format("%s %s %s", teacher.getSecondName(), teacher.getFirstName(), teacher.getPatronymic());
    }

    @Override
    public void addStudent(Course course, Participant student, RoleName roleName) {
        UserCourseAgent userCourseAgent = course.addParticipant(student, validateAndGetRegisteredRoles(roleName));
        userCourseAgentRepository.save(userCourseAgent);
    }

    @Override
    public void addTaskToCourse(Task task, Course course) {
        course.addToTaskList(task);
        courseRepository.save(course);
    }

    @Override
    public Chat addChat(Course course, ChatDto chatDto, Participant participant) {
        Chat chat = new Chat(chatDto.getDescription());

        participant.addChatToParticipant(chat);
        participant.addCreatedChatToList(chat);
        course.addChatToList(chat);
        chat.setCreator(participant);
        courseRepository.save(course);

        participantRepository.save(participant);

        return chat;
    }

    @Override
    public List<Chat> getChat(Course course) {
        return course.getChatList();
    }

    @Override
    public Course updateCourseInfo(Course course, CourseDto courseDto) {
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        courseRepository.save(course);
        return course;
    }

    public Course checkCourseAndUserData(int courseId, Authentication authentication,
                                         ParticipantService participantService,
                                         UserCourseAgentService userCourseAgentService) {
        Course course = findById(courseId);
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

    @Override
    public ResponseDto addTask(int courseId, int taskId,
                               String date, MultipartFile solution,
                               Authentication authentication,
                               ParticipantService participantService,
                               TaskService taskService,
                               UserCourseAgentService userCourseAgentService) {
        Course course = checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
        Participant student = participantService.findByMail(authentication.getName());
        Task task = taskService.showTaskById(taskId);
        if (task == null) {
            throw new NoSuchObjectException("There is no task with ID = " + taskId
                    + " in Database");
        }
        Answer answer = taskService.addAnswer(date, solution, task, student);
        if (answer == null) {
            return new ResponseDto(HttpStatus.BAD_REQUEST, "Can not add answer");
        }
        return new ResponseDto(HttpStatus.OK, "Answer added");
    }

    @Override
    public CourseResponseDto addStudentForCourse(int courseId, StudentAddDto studentAddDto,
                                                 Authentication authentication,
                                                 ParticipantService participantService,
                                                 UserCourseAgentService userCourseAgentService) {
        Course course = checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
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
        addStudent(course, student, studentAddDto.getRoleName());
        return new CourseResponseDto(course.getName(), course.getDescription(), course.getCourseParticipants());
    }

    @Override
    public TaskAddResponseDto addTaskToCourse(int courseId, String description, String date, MultipartFile file,
                                              Authentication authentication,
                                              ParticipantService participantService,
                                              UserCourseAgentService userCourseAgentService,
                                              TaskService taskService) {
        Course course = checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
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
        return taskAddResponseDto;
    }

    @Override
    public ChatResponseDto addChat(int courseId, ChatDto chatDto, Authentication authentication,
                                   ParticipantService participantService,
                                   UserCourseAgentService userCourseAgentService,
                                   ChatService chatService) {
        Course course = checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
        if (!checkUserRoleInCourse(course, authentication, participantService, userCourseAgentService)) {
            throw new NoParticipantRightsException("This user does not have the role of teacher and assistant");
        }
        Participant creator = participantService.findByMail(authentication.getName());
        Chat chat = addChat(course, chatDto, creator);
        Chat infoChat = chatService.getChatByCourseAndDescription(chat.getDescription(), course);
        return new ChatResponseDto(infoChat.getId(), infoChat.getDescription(), infoChat.getCreator().getMail());
    }

    @Override
    public CourseResponseDto updateCourse(int courseId, CourseDto courseDto, Authentication authentication,
                                          ParticipantService participantService, UserCourseAgentService userCourseAgentService) {
        Course course = checkCourseAndUserData(courseId, authentication,
                participantService, userCourseAgentService);
        Participant teacher = participantService.findByMail(authentication.getName());
        if (!teacher.isTeacher()) {
            throw new NoParticipantRightsException("User with ID = " + teacher.getId()
                    + " is not a teacher");
        }
        Course updatedCourse = updateCourseInfo(course, courseDto);
        CourseResponseDto courseResponseDto = new CourseResponseDto();
        courseResponseDto.setDescription(updatedCourse.getDescription());
        courseResponseDto.setName(updatedCourse.getName());
        return courseResponseDto;
    }

    private RoleName extractRoleNameFromRoleString(String roleString) {
        return switch (roleString.trim().toLowerCase()) {
            case "student" -> RoleName.ROLE_STUDENT;
            case "teacher" -> RoleName.ROLE_TEACHER;
            case "assistant" -> RoleName.ROLE_ASSISTANT;
            default -> throw new ParticipantRegistrationException("Invalid role was given for registration");
        };
    }

    private Role validateAndGetRegisteredRoles(RoleName registeredRoleName) {
        return roleRepository.findByName(registeredRoleName);
    }

    private boolean checkUserRoleInCourse(
        Course course,
        Authentication authentication,
        ParticipantService participantService,
        UserCourseAgentService userCourseAgentService
    ) {
        Participant participant = participantService.findByMail(authentication.getName());
        UserCourseAgent userCourseAgent = userCourseAgentService.findUserCourseAgent(course, participant);
        return switch (userCourseAgent.getRole().getName()) {
            case ROLE_ASSISTANT -> true;
            case ROLE_TEACHER -> true;
            default -> false;
        };
    }

}
