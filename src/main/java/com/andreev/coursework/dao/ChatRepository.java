package com.andreev.coursework.dao;

import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    Chat findChatByCourseAndDescription(Course course, String description);

    void deleteChatById(int chatId);
}
