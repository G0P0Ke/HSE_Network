package com.andreev.coursework.service.userCourseAgent;

import com.andreev.coursework.dao.UserCourseAgentRepository;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.UserCourseAgent;
import org.springframework.stereotype.Service;

@Service
public class UserCourseAgentServiceImpl implements UserCourseAgentService{

    private final UserCourseAgentRepository userCourseAgentRepository;

    public UserCourseAgentServiceImpl(UserCourseAgentRepository userCourseAgentRepository) {
        this.userCourseAgentRepository = userCourseAgentRepository;
    }

    @Override
    public UserCourseAgent findUserCourseAgent(Course course, Participant participant) {
        return userCourseAgentRepository
            .findUserCourseAgentByCourseAndParticipant(course, participant);
    }
}
