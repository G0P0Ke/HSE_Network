package com.andreev.coursework.dto;

import com.andreev.coursework.entity.security.RoleName;

/**
 * todo Document type StudentAddDto
 */
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
