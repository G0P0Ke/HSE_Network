package com.andreev.coursework.dto;

/**
 * todo Document type StudentAddDto
 */
public class StudentAddDto {
    int studentId;
    String roleName;

    public StudentAddDto() {
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
