package com.andreev.coursework.service.chat;

import com.andreev.coursework.dto.MessageDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Message;
import com.andreev.coursework.entity.Participant;

import java.util.List;

public interface ChatService {
    Chat getChatByCourseAndDescription(String description, Course course);

    boolean deleteChatById(int chatId);

    Chat getChatById(int chatId);

    boolean addMember(int userId, Chat chat);

    boolean addMessage(MessageDto messageDto, Chat chat, Participant participant);

    boolean findParticipant(Chat chat, Participant participant);

    List<Message> getAllMessage(int chatId);
}
