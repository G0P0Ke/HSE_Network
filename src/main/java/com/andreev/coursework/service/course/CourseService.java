package com.andreev.coursework.service.course;

import com.andreev.coursework.dto.ChatDto;
import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.entity.security.RoleName;

import java.util.List;

public interface CourseService {

    Course findById(int courseId);

    String getCreatorName(Course course);

    void addStudent(Course course, Participant student, RoleName roleName);

    void addTaskToCourse(Task task, Course course);

    Chat addChat(Course course, ChatDto chatDto, Participant participant);

    List<Chat> getChat(Course course);

    Course updateCourseInfo(Course course, CourseDto courseDto);
}
