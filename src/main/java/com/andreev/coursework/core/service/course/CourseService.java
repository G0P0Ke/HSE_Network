package com.andreev.coursework.core.service.course;

import com.andreev.coursework.dto.request.ChatDto;
import com.andreev.coursework.dto.request.CourseDto;
import com.andreev.coursework.dto.response.ResponseDto;
import com.andreev.coursework.dto.request.StudentAddDto;
import com.andreev.coursework.core.model.Chat;
import com.andreev.coursework.core.model.Course;
import com.andreev.coursework.core.model.Participant;
import com.andreev.coursework.core.model.Task;
import com.andreev.coursework.core.model.security.RoleName;
import com.andreev.coursework.dto.response.ChatResponseDto;
import com.andreev.coursework.dto.response.CourseResponseDto;
import com.andreev.coursework.dto.response.TaskAddResponseDto;
import com.andreev.coursework.core.service.chat.ChatService;
import com.andreev.coursework.core.service.participant.ParticipantService;
import com.andreev.coursework.core.service.task.TaskService;
import com.andreev.coursework.core.service.userCourseAgent.UserCourseAgentService;
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
