package com.andreev.coursework.core.service.participant;

import com.andreev.coursework.core.model.dao.CourseRepository;
import com.andreev.coursework.core.model.dao.ParticipantRepository;
import com.andreev.coursework.core.model.dao.RoleRepository;
import com.andreev.coursework.core.model.dao.UserCourseAgentRepository;
import com.andreev.coursework.core.model.dao.UserTaskAgentRepository;
import com.andreev.coursework.dto.request.CourseDto;
import com.andreev.coursework.dto.request.ProfileDto;
import com.andreev.coursework.dto.response.ResponseDto;
import com.andreev.coursework.core.model.*;
import com.andreev.coursework.core.model.security.RoleName;
import com.andreev.coursework.core.exception.paricipant.NoParticipantRightsException;
import com.andreev.coursework.core.exception.paricipant.NoSuchObjectException;
import com.andreev.coursework.dto.response.SimpleCourseResponseDto;
import com.andreev.coursework.output.mail.MailSender;
import com.andreev.coursework.core.service.course.CourseService;
import com.andreev.coursework.core.service.task.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    private final UserTaskAgentRepository userTaskAgentRepository;
    private final UserCourseAgentRepository userCourseAgentRepository;
    private final CourseRepository courseRepository;
    private final ParticipantRepository participantRepository;
    private final RoleRepository roleRepository;
    private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ParticipantServiceImpl(UserTaskAgentRepository userTaskAgentRepository,
                                  UserCourseAgentRepository userCourseAgentRepository,
                                  CourseRepository courseRepository,
                                  ParticipantRepository participantRepository,
                                  RoleRepository roleRepository,
                                  MailSender mailSender
    ) {
        this.userTaskAgentRepository = userTaskAgentRepository;
        this.userCourseAgentRepository = userCourseAgentRepository;
        this.courseRepository = courseRepository;
        this.participantRepository = participantRepository;
        this.roleRepository = roleRepository;
        this.mailSender = mailSender;
    }

    @Override
    public List<Participant> getAllUsers() {
        return participantRepository.findAll();
    }

    @Override
    public List<Course> getAllCourses(Participant participant) {
        Set<UserCourseAgent> userCourseAgentList = participant.getUserCourseSet();
        List<Course> courseList = new ArrayList<>();
        for (UserCourseAgent el : userCourseAgentList) {
            courseList.add(el.getCourse());
        }
        return courseList;
    }

    @Override
    public boolean addTaskToStudent(Participant student, Task task) {
        UserTaskAgent userTaskAgent = student.addTaskToList(task);
        if (userTaskAgent != null) {
            userTaskAgentRepository.save(userTaskAgent);
            return true;
        }
        return false;
    }

    @Override
    public void gradeParticipantByTask(Participant student, Task task, int grade) {
        UserTaskAgent userTaskAgent = userTaskAgentRepository.findUserTaskAgentByTaskAndStudent(task, student);
        if (userTaskAgent != null) {
            userTaskAgent.setGrade(grade);
            userTaskAgentRepository.save(userTaskAgent);
        }
    }

    @Override
    public void updateProfileUser(Participant participant, ProfileDto profileDto) {
        participant.setFirstName(profileDto.getFirstName());
        participant.setSecondName(profileDto.getSurname());
        participant.setPatronymic(profileDto.getPatronymic());
        participant.setMail(profileDto.getMail());
        participantRepository.save(participant);
    }

    @Override
    public void addCourse(Participant participant, CourseDto courseDto) {
        Course course = new Course(courseDto.getName(), courseDto.getDescription());
        courseRepository.save(course);
        UserCourseAgent userCourseAgent = course.addParticipant(participant, roleRepository.findByName(RoleName.ROLE_TEACHER));
        userCourseAgentRepository.save(userCourseAgent);
    }

    @Override
    public Participant getUser(int id) {
        Participant user = null;
        Optional<Participant> optional = participantRepository.findById(id);
        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }

    @Override
    public Participant findByMail(String email) {
        return participantRepository.findParticipantByMail(email);
    }

    @Override
    public void loginUser(String mail) {
        Participant user = participantRepository.findParticipantByMail(mail);
        if (user == null) {
            user = new Participant("", "", "", mail,
                    false, passwordEncoder.encode(mail));
        }
        user.setCode(generateCode());
        participantRepository.save(user);

        String message = String.format(
                "Hello, %s! \n" +
                        "Your activation code: %s",
                user.getMail(),
                user.getCode()
        );

        mailSender.send(user.getMail(), "Activation code", message);
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void deleteUser(int id) {
        participantRepository.deleteById(id);
    }

    @Override
    public boolean isActive(String email, String code) {
        Participant user = participantRepository.findParticipantByMail(email);
        if (user == null) {
            return false;
        }
        if (Objects.equals(code, user.getCode())) {
            user.setActive(true);
            participantRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUserRoleInCourse(Course course, Authentication authentication) {
        Participant participant = findByMail(authentication.getName());
        UserCourseAgent userCourseAgent = userCourseAgentRepository
                .findUserCourseAgentByCourseAndParticipant(course, participant);
        return switch (userCourseAgent.getRole().getName()) {
            case ROLE_ASSISTANT -> true;
            case ROLE_TEACHER -> true;
            default -> false;
        };
    }

    @Override
    public ResponseDto updateUserProfile(int id, ProfileDto profileDto) {
        Participant participant = getUser(id);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + id
                    + " in Database");
        }
        updateProfileUser(participant, profileDto);
        return new ResponseDto(HttpStatus.OK, "Profile updated");
    }

    @Override
    public List<SimpleCourseResponseDto> getAllCourses(Authentication authentication, CourseService courseService) {
        Participant participant = findByMail(authentication.getName());
        List<Course> courseList = getAllCourses(participant);

        List<SimpleCourseResponseDto> answer = new ArrayList<>();
        for (Course course : courseList) {
            String name = courseService.getCreatorName(course);
            answer.add(new SimpleCourseResponseDto(course.getId(), course.getName(), course.getDescription(), name));
        }

        return answer;
    }

    @Override
    public Set<UserTaskAgent> getAllTasksByParticipantId(int id) {
        Participant participant = getUser(id);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + id
                    + " in Database");
        }
        return participant.getTaskList();
    }

    @Override
    public Set<Chat> getAllChatsByParticipantId(int id) {
        Participant participant = getUser(id);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + id
                    + " in Database");
        }
        return participant.getChatList();
    }

    @Override
    public ResponseDto addCourse(int id, CourseDto courseDto) {
        Participant participant = getUser(id);
        if (participant == null) {
            throw new NoSuchObjectException("There is no participant with ID = " + id
                    + " in Database");
        } else if (!participant.isTeacher()) {
            throw new NoParticipantRightsException("User with ID = " + id
                    + " is not a teacher");
        }
        addCourse(participant, courseDto);
        return new ResponseDto(HttpStatus.OK, "Course added");
    }

    @Override
    public ResponseDto gradeTask(int userId, int taskId, int grade, TaskService taskService,
                                 ParticipantService participantService) {
        Participant participant = getUser(userId);
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
        return new ResponseDto(HttpStatus.OK, "Student with ID = " + userId + " graded by " + grade);
    }
}
