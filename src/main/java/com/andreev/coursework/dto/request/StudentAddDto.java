package com.andreev.coursework.dto.request;

import com.andreev.coursework.core.model.security.RoleName;


public class StudentAddDto {
    int studentId;
    RoleName roleName;

    public StudentAddDto() {
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}
