package com.andreev.coursework.core.model.dao;

import com.andreev.coursework.core.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
