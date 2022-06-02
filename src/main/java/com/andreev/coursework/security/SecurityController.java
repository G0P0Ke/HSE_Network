package com.andreev.coursework.security;

import com.andreev.coursework.dto.EntryDto;
import com.andreev.coursework.security.jwt.JwtProvider;
import com.andreev.coursework.security.jwt.JwtResponse;
import com.andreev.coursework.service.participant.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/security")
public class SecurityController {
    private final ParticipantService participantService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public SecurityController(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
        ParticipantService participantService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.participantService = participantService;
    }

    @PostMapping(
        value = "/login",
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Получение кода для активации пользователя")
    public ResponseEntity<String> login(@RequestBody EntryDto entryDto) {
        boolean checkMail = mailValidation(entryDto.getEmail());
        if (!checkMail) {
            return ResponseEntity.badRequest().body("Invalid email");
        }
        this.participantService.loginUser(entryDto.getEmail());
        return ResponseEntity.ok("Code send successfully!");
    }

    @PostMapping(
        value = "/entry",
        consumes = "application/json"
    )
    @Operation(summary = "Аутентификация пользователя")
    public ResponseEntity<JwtResponse> authenticateEmployee(@RequestBody EntryDto entryDto) {
        boolean entryFlag = participantService.isActive(entryDto.getEmail(), entryDto.getCode());
        if (entryFlag) {
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(entryDto.getEmail(), entryDto.getEmail());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtProvider.generateJwtToken(authentication);

            return ResponseEntity.ok(new JwtResponse(jwtToken));
        } else {
            return ResponseEntity.badRequest().body(new JwtResponse(""));
        }
    }

    private static boolean mailValidation(String email) {
        String regex = "^[-.A-Za-zА-Яа-я_\\d]+@edu\\.hse\\.ru$";
        return Pattern.matches(regex, email);
    }

}
