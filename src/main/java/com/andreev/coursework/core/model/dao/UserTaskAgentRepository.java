package com.andreev.coursework.core.model.dao;

import com.andreev.coursework.core.model.Participant;
import com.andreev.coursework.core.model.Task;
import com.andreev.coursework.core.model.UserTaskAgent;
import com.andreev.coursework.core.model.UserTaskAgentPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskAgentRepository extends JpaRepository<UserTaskAgent, UserTaskAgentPK> {
    UserTaskAgent findUserTaskAgentByTaskAndStudent(Task task, Participant student);
}
