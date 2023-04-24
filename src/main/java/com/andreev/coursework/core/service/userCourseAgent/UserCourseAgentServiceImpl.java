package com.andreev.coursework.core.service.userCourseAgent;

import com.andreev.coursework.core.model.dao.UserCourseAgentRepository;
import com.andreev.coursework.core.model.Course;
import com.andreev.coursework.core.model.Participant;
import com.andreev.coursework.core.model.UserCourseAgent;
import org.springframework.stereotype.Service;

@Service
public class UserCourseAgentServiceImpl implements UserCourseAgentService {

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
