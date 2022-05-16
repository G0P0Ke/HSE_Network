package com.andreev.coursework.dao;

import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.entity.UserTaskAgent;
import com.andreev.coursework.entity.UserTaskAgentPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskAgentRepository extends JpaRepository<UserTaskAgent, UserTaskAgentPK> {
    UserTaskAgent findUserTaskAgentByTaskAndStudent(Task task, Participant student);
}
