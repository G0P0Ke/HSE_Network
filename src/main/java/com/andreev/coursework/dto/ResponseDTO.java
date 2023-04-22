package com.andreev.coursework.dto;

public record ResponseDTO(int status, String message) {

    public class HttpStatus {
        public static int OK = 200;
        public static int BAD_REQUEST = 400;
        public static int UNAUTHORIZED = 401;
        public static int NOT_FOUND = 404;
    }
}
