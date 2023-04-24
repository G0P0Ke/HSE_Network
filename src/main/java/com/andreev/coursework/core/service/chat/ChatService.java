package com.andreev.coursework.core.service.chat;

import com.andreev.coursework.dto.request.MessageDto;
import com.andreev.coursework.dto.response.ResponseDto;
import com.andreev.coursework.core.model.Chat;
import com.andreev.coursework.core.model.Course;
import com.andreev.coursework.core.model.Message;
import com.andreev.coursework.core.model.Participant;
import com.andreev.coursework.core.service.participant.ParticipantService;
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
