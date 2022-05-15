package com.andreev.coursework.exception.course;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CourseGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CourseIncorrectData> handleException(
        NoSuchCourseException noSuchCourseException
    ) {
        CourseIncorrectData courseIncorrectData = new CourseIncorrectData();
        courseIncorrectData.setInfo(noSuchCourseException.getMessage());

        return new ResponseEntity<>(courseIncorrectData, HttpStatus.NOT_FOUND);
    }
}
