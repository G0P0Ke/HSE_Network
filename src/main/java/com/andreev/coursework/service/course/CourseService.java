package com.andreev.coursework.service.course;

import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;

public interface CourseService {

    Course findById(int courseId);

    void addStudent(Course course, Participant student, String roleName);

    void addTaskToCourse(Task task, Course course);
}
