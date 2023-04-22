package com.andreev.coursework.dto;

import org.springframework.http.HttpStatus;

public record ResponseDTO (HttpStatus status, String message) {}
