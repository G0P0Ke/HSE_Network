package com.andreev.coursework.service.course;

import com.andreev.coursework.dao.*;
import com.andreev.coursework.dto.ChatDto;
import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.entity.*;
import com.andreev.coursework.entity.security.Role;
import com.andreev.coursework.entity.security.RoleName;
import com.andreev.coursework.exception.paricipant.ParticipantRegistrationException;
import org.springframework.stereotype.Service;

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
    public Course findById(int courseId) {
        return courseRepository.getById(courseId);
    }

    @Override
    public void addStudent(Course course, Participant student, String roleName) {
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
        if (!courseDto.getName().isEmpty()) {
            course.setName(courseDto.getName());
        }
        if (!courseDto.getDescription().isEmpty()) {
            course.setDescription(courseDto.getDescription());
        }
        courseRepository.save(course);
        return course;
    }

    private Role validateAndGetRegisteredRoles(String roleString) {

        RoleName registeredRoleName = extractRoleNameFromRoleString(roleString);
        Role registeredRole = roleRepository.findByName(registeredRoleName);
        return registeredRole;
    }

    private RoleName extractRoleNameFromRoleString(String roleString) {
        switch (roleString.trim().toLowerCase()) {
            case "student":
                return RoleName.ROLE_STUDENT;
            case "teacher":
                return RoleName.ROLE_TEACHER;
            case "assistant":
                return RoleName.ROLE_ASSISTANT;
            default:
                throw new ParticipantRegistrationException("Invalid role was given for registration");
        }
    }
}
