package com.andreev.coursework.input.rest.controller;

import com.andreev.coursework.dto.request.MessageDto;
import com.andreev.coursework.dto.response.ResponseDto;
import com.andreev.coursework.dto.request.StudentAddDto;
import com.andreev.coursework.core.model.Message;
import com.andreev.coursework.core.service.chat.ChatService;
import com.andreev.coursework.core.service.participant.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
        ResponseDto response = chatService.deleteChat(chatId);
        return ResponseEntity.status(response.status()).body(response.message());
    }

    @PostMapping("/{chatId}/addMember")
    @Operation(summary = "добавить участника в чат")
    public ResponseEntity<String> addMember(
        @PathVariable int chatId,
        @Valid @RequestBody StudentAddDto studentAddDto,
        Authentication authentication
    ) {
        ResponseDto response = chatService.addMember(chatId, studentAddDto.getStudentId(), authentication, participantService);
        return ResponseEntity.status(response.status()).body(response.message());
    }

    @PostMapping("/{chatId}/addMessage")
    @Operation(summary = "отправить сообщение в чат")
    public ResponseEntity<String> addMessage(
            @PathVariable int chatId,
            @Valid @RequestBody MessageDto messageDto,
            Authentication authentication
    ) {
        ResponseDto response = chatService.addMessage(chatId, messageDto, authentication, participantService);
        return ResponseEntity.status(response.status()).body(response.message());
    }

    @GetMapping("/{chatId}/message")
    @Operation(summary = "получить все сообщения в чате")
    public List<Message> getAllMessage(@PathVariable int chatId) {
        return chatService.getAllMessage(chatId);
    }

}
