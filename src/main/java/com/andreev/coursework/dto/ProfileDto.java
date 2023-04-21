package com.andreev.coursework.dto;

import javax.validation.constraints.Size;

public class ProfileDto {
    @Size(min = 1, max = 30)
    private String firstName;
    @Size(min = 1, max = 35)
    private String surname;
    @Size(min = 1, max = 500)
    private String patronymic;
    @Size(min = 8, max = 64)
    private String mail;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
