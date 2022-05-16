package com.andreev.coursework.service.userCourseAgent;

import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.UserCourseAgent;

public interface UserCourseAgentService {

    UserCourseAgent findUserCourseAgent(Course course, Participant participant);
}
