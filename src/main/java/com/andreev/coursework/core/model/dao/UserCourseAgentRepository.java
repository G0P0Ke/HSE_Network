package com.andreev.coursework.core.model.dao;

import com.andreev.coursework.core.model.Course;
import com.andreev.coursework.core.model.Participant;
import com.andreev.coursework.core.model.UserCourseAgent;
import com.andreev.coursework.core.model.UserCourseAgentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseAgentRepository extends JpaRepository<UserCourseAgent, UserCourseAgentPK> {
    UserCourseAgent findUserCourseAgentByCourseAndParticipant(Course course, Participant participant);
}
