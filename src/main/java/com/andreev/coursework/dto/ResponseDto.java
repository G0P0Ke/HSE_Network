package com.andreev.coursework.dto;

import org.springframework.http.HttpStatus;

public record ResponseDto(HttpStatus status, String message) {}
