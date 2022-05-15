package com.andreev.coursework.service.course;

import com.andreev.coursework.dto.ChatDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;

import java.util.List;

public interface CourseService {

    Course findById(int courseId);

    void addStudent(Course course, Participant student, String roleName);

    void addTaskToCourse(Task task, Course course);

    Chat addChat (Course course, ChatDto chatDto, Participant participant);

    List<Chat> getChat(Course course);
}
