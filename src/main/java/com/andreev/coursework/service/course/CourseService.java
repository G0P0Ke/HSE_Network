package com.andreev.coursework.service.course;

import com.andreev.coursework.dto.ChatDto;
import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.dto.ResponseDto;
import com.andreev.coursework.dto.StudentAddDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.entity.security.RoleName;
import com.andreev.coursework.response.ChatResponseDto;
import com.andreev.coursework.response.CourseResponseDto;
import com.andreev.coursework.response.TaskAddResponseDto;
import com.andreev.coursework.service.chat.ChatService;
import com.andreev.coursework.service.participant.ParticipantService;
import com.andreev.coursework.service.task.TaskService;
import com.andreev.coursework.service.userCourseAgent.UserCourseAgentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface CourseService {

    Course findById(int courseId);

    String getCreatorName(Course course);

    void addStudent(Course course, Participant student, RoleName roleName);

    ResponseDto getCourse(int courseId, Authentication authentication,
                          ParticipantService participantService,
                          UserCourseAgentService userCourseAgentService);

    ResponseDto addTask(int courseId, int taskId,
                               String date, MultipartFile solution,
                               Authentication authentication,
                               ParticipantService participantService,
                               TaskService taskService,
                               UserCourseAgentService userCourseAgentService);

    CourseResponseDto addStudentForCourse(int courseId, StudentAddDto studentAddDto,
                                          Authentication authentication,
                                          ParticipantService participantService,
                                          UserCourseAgentService userCourseAgentService);

    TaskAddResponseDto addTaskToCourse(int courseId, String description, String date, MultipartFile file,
                                              Authentication authentication,
                                              ParticipantService participantService,
                                              UserCourseAgentService userCourseAgentService,
                                              TaskService taskService);

    ChatResponseDto addChat(int courseId, ChatDto chatDto, Authentication authentication,
                                   ParticipantService participantService,
                                   UserCourseAgentService userCourseAgentService,
                                   ChatService chatService);

    CourseResponseDto updateCourse(int courseId, CourseDto courseDto, Authentication authentication,
                                   ParticipantService participantService, UserCourseAgentService userCourseAgentService);

    Course checkCourseAndUserData(int courseId, Authentication authentication,
                                  ParticipantService participantService,
                                  UserCourseAgentService userCourseAgentService);

    void addTaskToCourse(Task task, Course course);

    Chat addChat(Course course, ChatDto chatDto, Participant participant);

    List<Chat> getChat(Course course);

    Course updateCourseInfo(Course course, CourseDto courseDto);
}
