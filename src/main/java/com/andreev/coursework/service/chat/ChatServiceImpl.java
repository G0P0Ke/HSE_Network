package com.andreev.coursework.service.chat;

import com.andreev.coursework.dao.ChatRepository;
import com.andreev.coursework.dao.MessageRepository;
import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dto.MessageDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Message;
import com.andreev.coursework.entity.Participant;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService{

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
    public boolean addMessage(MessageDto messageDto, Chat chat, Participant participant) {
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date dateSend = date.parse(messageDto.getDateSend());
            Message message = new Message();
            message.setChat(chat);
            message.setDateSend(dateSend);
            message.setContent(messageDto.getContent());
            message.setSender(participant);
            messageRepository.save(message);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean findParticipant(Chat chat, Participant sender) {
        for (var par: chat.getParticipantList()) {
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
