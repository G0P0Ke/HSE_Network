package com.andreev.coursework.dto;

import javax.validation.constraints.Pattern;

public class EntryDto {
    @Pattern(regexp = "^[-.A-Za-zА-Яа-я_\\d]+@edu\\.hse\\.ru$", message = "Mail must be the edu.hse.ru type!")
    private String email;
    private String code;

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }
}
