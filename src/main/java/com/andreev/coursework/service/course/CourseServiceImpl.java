package com.andreev.coursework.service.course;

import com.andreev.coursework.dao.CourseRepository;
import com.andreev.coursework.dao.RoleRepository;
import com.andreev.coursework.dao.UserCourseAgentRepository;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.entity.UserCourseAgent;
import com.andreev.coursework.entity.security.Role;
import com.andreev.coursework.entity.security.RoleName;
import com.andreev.coursework.exception.paricipant.ParticipantRegistrationException;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl implements CourseService {

    private final UserCourseAgentRepository userCourseAgentRepository;
    private final RoleRepository roleRepository;
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository, RoleRepository roleRepository, UserCourseAgentRepository userCourseAgentRepository) {
        this.courseRepository = courseRepository;
        this.roleRepository = roleRepository;
        this.userCourseAgentRepository = userCourseAgentRepository;
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
