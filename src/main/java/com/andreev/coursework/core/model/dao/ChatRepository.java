package com.andreev.coursework.core.model.dao;

import com.andreev.coursework.core.model.Chat;
import com.andreev.coursework.core.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Chat findChatByCourseAndDescription(Course course, String description);

    void deleteChatById(int chatId);
}
