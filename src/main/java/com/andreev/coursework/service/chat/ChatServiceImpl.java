package com.andreev.coursework.service.chat;

import com.andreev.coursework.dao.ChatRepository;
import com.andreev.coursework.dao.MessageRepository;
import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dto.MessageDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Message;
import com.andreev.coursework.entity.Participant;
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
