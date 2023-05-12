package com.andreev.coursework.core.service.chat;

import com.andreev.coursework.core.model.dao.ChatRepository;
import com.andreev.coursework.core.model.dao.MessageRepository;
import com.andreev.coursework.core.model.dao.ParticipantRepository;
import com.andreev.coursework.dto.request.MessageDto;
import com.andreev.coursework.dto.response.ResponseDto;
import com.andreev.coursework.core.model.Chat;
import com.andreev.coursework.core.model.Course;
import com.andreev.coursework.core.model.Message;
import com.andreev.coursework.core.model.Participant;
import com.andreev.coursework.core.service.participant.ParticipantService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ParticipantRepository participantRepository;
    private final MessageRepository messageRepository;

    public ChatServiceImpl(ChatRepository chatRepository, ParticipantRepository participantRepository,
                           MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.participantRepository = participantRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public ResponseDto deleteChat(int chatId) {
        if (deleteChatById(chatId)) {
            return new ResponseDto(HttpStatus.OK, "Chat deleted");
        }
        return new ResponseDto(HttpStatus.BAD_REQUEST, "There is no chat with id = " + chatId + " in database");
    }

    @Override
    public ResponseDto addMember(int chatId, int studentId, Authentication authentication, ParticipantService service) {
        Chat chat = getChatById(chatId);
        if (chat == null) {
            return new ResponseDto(HttpStatus.BAD_REQUEST, "There is no chat with id = " + chatId + " in database");
        }
        boolean hasRole = service.checkUserRoleInCourse(chat.getCourse(), authentication);
        if (!hasRole) {
            return new ResponseDto(HttpStatus.BAD_REQUEST, "User doesn't have the rights of a teacher or assistant");
        }
        boolean tryAdd = addMember(studentId, chat);
        if (tryAdd) {
            return new ResponseDto(HttpStatus.OK, "Member added");
        }
        return new ResponseDto(HttpStatus.BAD_REQUEST, "Can not add member");
    }

    @Override
    public ResponseDto addMessage(int chatId, MessageDto message, Authentication authentication, ParticipantService service) {
        Chat chat = getChatById(chatId);
        if (chat == null) {
            return new ResponseDto(HttpStatus.BAD_REQUEST, "There is no chat with id = " + chatId + " in database");
        }
        Participant sender = service.findByMail(authentication.getName());
        addMessage(message, chat, sender);
        if (findParticipant(chat, sender)) {
            return new ResponseDto(HttpStatus.OK, "Message sent");
        }
        return new ResponseDto(HttpStatus.BAD_REQUEST, "Can not send message");
    }

    @Override
    public Chat getChatByCourseAndDescription(String description, Course course) {
        return chatRepository.findChatByCourseAndDescription(course, description);
    }

    @Override
    @Transactional
    public boolean deleteChatById(int chatId) {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            return false;
        }
        chatRepository.deleteChatById(chatId);
        return true;
    }

    @Override
    public Chat getChatById(int chatId) {
        return chatRepository.getById(chatId);
    }

    @Override
    public boolean addMember(int userId, Chat chat) {
        Optional<Participant> participant = participantRepository.findById(userId);
        if (participant.isEmpty()) {
            return false;
        }
        chat.addMember(participant.get());
        chatRepository.save(chat);
        return true;
    }

    @Override
    public void addMessage(MessageDto messageDto, Chat chat, Participant participant) {
        Message message = new Message();
        message.setChat(chat);
        message.setDateSend(messageDto.getDateSend());
        message.setContent(messageDto.getContent());
        message.setSender(participant);
        messageRepository.save(message);
    }

    @Override
    public boolean findParticipant(Chat chat, Participant sender) {
        for (var par : chat.getParticipantList()) {
            if (par == sender) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Message> getAllMessage(int chatId) {
        List<Message> messageList = new ArrayList<>();
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isEmpty()) {
            return messageList;
        }
        messageList = chat.get().getMessages();
        return messageList;
    }
}
