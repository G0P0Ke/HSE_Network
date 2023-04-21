package com.andreev.coursework.service.course;

import com.andreev.coursework.dao.ChatRepository;
import com.andreev.coursework.dao.CourseRepository;
import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dao.RoleRepository;
import com.andreev.coursework.dao.UserCourseAgentRepository;
import com.andreev.coursework.dto.ChatDto;
import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.entity.UserCourseAgent;
import com.andreev.coursework.entity.security.Role;
import com.andreev.coursework.entity.security.RoleName;
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
    public String getCreatorName(Course course) {
        Participant teacher = null;
        for (UserCourseAgent el : course.getParticipants()) {
            if (el.getParticipant().isTeacher()) {
                teacher = el.getParticipant();
                break;
            }
        }
        String name = String.format("%s %s %s", teacher.getSecondName(), teacher.getFirstName(), teacher.getPatronymic());
        return name;
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

    private Role validateAndGetRegisteredRoles(RoleName registeredRoleName) {
        return roleRepository.findByName(registeredRoleName);
    }
}
