package com.andreev.coursework.controller;

import com.andreev.coursework.dto.MessageDto;
import com.andreev.coursework.dto.ResponseDTO;
import com.andreev.coursework.dto.StudentAddDto;
import com.andreev.coursework.entity.Message;
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
        ResponseDTO response = chatService.deleteChat(chatId);
        return ResponseEntity.status(response.status()).body(response.message());
    }

    @PostMapping("/{chatId}/addMember")
    @Operation(summary = "добавить участника в чат")
    public ResponseEntity<String> addMember(
        @PathVariable int chatId,
        @RequestBody StudentAddDto studentAddDto,
        Authentication authentication
    ) {
        ResponseDTO response = chatService.addMember(chatId, studentAddDto.getStudentId(), authentication, participantService);
        return ResponseEntity.status(response.status()).body(response.message());
    }

    @PostMapping("/{chatId}/addMessage")
    @Operation(summary = "отправить сообщение в чат")
    public ResponseEntity<String> addMessage(
        @PathVariable int chatId,
        @RequestBody MessageDto messageDto,
        Authentication authentication
    ) {
        ResponseDTO response = chatService.addMessage(chatId, messageDto, authentication, participantService);
        return ResponseEntity.status(response.status()).body(response.message());
    }

    @GetMapping("/{chatId}/message")
    @Operation(summary = "получить все сообщения в чате")
    public List<Message> getAllMessage(@PathVariable int chatId) {
        return chatService.getAllMessage(chatId);
    }

}
