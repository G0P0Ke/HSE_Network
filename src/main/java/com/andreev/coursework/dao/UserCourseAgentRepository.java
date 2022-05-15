package com.andreev.coursework.dao;

import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.UserCourseAgent;
import com.andreev.coursework.entity.UserCourseAgentPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseAgentRepository extends JpaRepository<UserCourseAgent, UserCourseAgentPK> {
    UserCourseAgent findUserCourseAgentByCourseAndParticipant(Course course, Participant participant);
}
