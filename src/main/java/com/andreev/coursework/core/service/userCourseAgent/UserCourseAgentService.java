package com.andreev.coursework.core.service.userCourseAgent;

import com.andreev.coursework.core.model.Course;
import com.andreev.coursework.core.model.Participant;
import com.andreev.coursework.core.model.UserCourseAgent;

public interface UserCourseAgentService {

    UserCourseAgent findUserCourseAgent(Course course, Participant participant);
}
