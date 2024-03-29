package com.andreev.coursework.core.exception.paricipant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ParticipantGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ParticipantIncorrectData> handleException(
        NoSuchObjectException exception) {
        ParticipantIncorrectData data = new ParticipantIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ParticipantIncorrectData> handleException(
        Exception exception) {
        ParticipantIncorrectData data = new ParticipantIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ParticipantIncorrectData> handleException(
        NoParticipantRightsException exception
    ) {
        ParticipantIncorrectData data = new ParticipantIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
    }
}
