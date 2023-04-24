package com.andreev.coursework.service.chat;

import com.andreev.coursework.dto.MessageDto;
import com.andreev.coursework.dto.ResponseDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Message;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.service.participant.ParticipantService;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ChatService {
    Chat getChatByCourseAndDescription(String description, Course course);

    ResponseDto deleteChat(int chatId);

    ResponseDto addMember(int chatId, int studentId, Authentication authentication, ParticipantService service);

    ResponseDto addMessage(int chatId, MessageDto message, Authentication authentication, ParticipantService service);

    boolean deleteChatById(int chatId);

    Chat getChatById(int chatId);

    boolean addMember(int userId, Chat chat);

    void addMessage(MessageDto messageDto, Chat chat, Participant participant);

    boolean findParticipant(Chat chat, Participant participant);

    List<Message> getAllMessage(int chatId);
}
