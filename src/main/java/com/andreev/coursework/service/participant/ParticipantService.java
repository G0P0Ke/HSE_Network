package com.andreev.coursework.service.participant;

import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.dto.ProfileDto;
import com.andreev.coursework.dto.ResponseDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.entity.UserTaskAgent;
import com.andreev.coursework.response.SimpleCourseResponseDto;
import com.andreev.coursework.service.course.CourseService;
import com.andreev.coursework.service.task.TaskService;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface ParticipantService {

    Participant findByMail(String email);

    void loginUser(String email);

    List<Participant> getAllUsers();

    List<Course> getAllCourses(Participant participant);

    void updateProfileUser(Participant participant, ProfileDto profileDto);

    void addCourse(Participant participant, CourseDto courseDto);

    Participant getUser(int id);

    void deleteUser(int id);

    boolean isActive(String email, String code);

    boolean checkUserRoleInCourse(Course course, Authentication authentication);

    boolean addTaskToStudent(Participant student, Task task);

    void gradeParticipantByTask(Participant student, Task task, int grade);

    ResponseDto updateUserProfile(int id, ProfileDto profileDto);

    List<SimpleCourseResponseDto> getAllCourses(Authentication authentication, CourseService courseService);

    Set<UserTaskAgent> getAllTasksByParticipantId(int id);

    Set<Chat> getAllChatsByParticipantId(int id);

    ResponseDto addCourse(int id, CourseDto courseDto);

    ResponseDto gradeTask(int userId, int taskId, int grade, TaskService taskService,
                            ParticipantService participantService);
}
