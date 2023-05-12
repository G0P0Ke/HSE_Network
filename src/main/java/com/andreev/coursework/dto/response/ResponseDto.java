package com.andreev.coursework.dto.response;

import org.springframework.http.HttpStatus;

public record ResponseDto(HttpStatus status, String message) {}
