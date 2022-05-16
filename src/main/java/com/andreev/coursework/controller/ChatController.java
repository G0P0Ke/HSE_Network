package com.andreev.coursework.controller;

import com.andreev.coursework.dto.MessageDto;
import com.andreev.coursework.dto.StudentAddDto;
import com.andreev.coursework.entity.Chat;
import com.andreev.coursework.entity.Message;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.service.chat.ChatService;
import com.andreev.coursework.service.participant.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ParticipantService participantService;
    private final ChatService chatService;

    public ChatController(ParticipantService participantService, ChatService chatService) {
        this.participantService = participantService;
        this.chatService = chatService;
    }

    @DeleteMapping("/{chatId}")
    @Operation(summary = "Удаление чата по его id")
    public ResponseEntity<String> deleteChat(@PathVariable int chatId) {
        if (chatService.deleteChatById(chatId)) {
            return ResponseEntity.ok("Chat deleted");
        }
        return ResponseEntity.badRequest().body("There is no chat with id = " + chatId + " in database");
    }

    @PostMapping("/{chatId}/addMember")
    @Operation(summary = "добавить участника в чат")
    public ResponseEntity<String> addMember(
        @PathVariable int chatId,
        @RequestBody StudentAddDto studentAddDto,
        Authentication authentication) {
        Chat chat = chatService.getChatById(chatId);
        if (chat == null) {
            return ResponseEntity.badRequest().body("There is no chat with id = " + chatId + " in database");
        }
        boolean hasRole = participantService.checkUserRoleInCourse(chat.getCourse(), authentication);
        if (!hasRole) {
            return ResponseEntity.badRequest().body("User doesn't have the rights of a teacher or assistant");
        }
        boolean tryAdd = chatService.addMember(studentAddDto.getStudentId(), chat);
        if (tryAdd) {
            return ResponseEntity.ok("Member added");
        }
        return ResponseEntity.badRequest().body("Can not add member");
    }

    @PostMapping("/{chatId}/addMessage")
    @Operation(summary = "отправить сообщение в чат")
    public ResponseEntity<String> addMessage(
        @PathVariable int chatId,
        @RequestBody MessageDto messageDto,
        Authentication authentication
    ) {
        Chat chat = chatService.getChatById(chatId);
        if (chat == null) {
            return ResponseEntity.badRequest().body("There is no chat with id = " + chatId + " in database");
        }
        Participant sender = participantService.findByMail(authentication.getName());
        boolean tryAdd = chatService.addMessage(messageDto, chat, sender);
        if (checkMember(chat, sender) && tryAdd) {
            return ResponseEntity.ok("Message sent");
        }
        return ResponseEntity.badRequest().body("Can not send message");
    }

    @GetMapping("/{chatId}/message")
    @Operation(summary = "получить все сообщения в чате")
    public List<Message> getAllMessage(@PathVariable int chatId) {
        return chatService.getAllMessage(chatId);
    }

    private boolean checkMember(Chat chat, Participant sender) {
        return chatService.findParticipant(chat, sender);
    }
}
