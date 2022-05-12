package com.andreev.coursework.service.participant;

import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dao.RoleRepository;
import com.andreev.coursework.dto.ProfileDto;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.security.Role;
import com.andreev.coursework.entity.security.RoleName;
import com.andreev.coursework.exception.paricipant.ParticipantRegistrationException;
import com.andreev.coursework.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MailSender mailSender;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<Participant> getAllUsers() {
        return participantRepository.findAll();
    }

    @Override
    public void saveUser(Participant user) {
        participantRepository.save(user);
    }

    @Override
    public void updateProfileUser(Participant participant, ProfileDto profileDto) {
        if (!profileDto.getFirstName().isEmpty())  {
            participant.setFirstName(profileDto.getFirstName());
        }
        if (!profileDto.getSurname().isEmpty()) {
            participant.setSecondName(profileDto.getSurname());
        }
        if (!profileDto.getPatronymic().isEmpty()) {
            participant.setPatronymic(profileDto.getPatronymic());
        }
        if (!profileDto.getMail().isEmpty()) {
            participant.setMail(profileDto.getMail());
        }
        participantRepository.save(participant);
    }

    @Override
    public Participant getUser(int id) {
        Participant user = null;
        Optional<Participant> optional = participantRepository.findById(id);
        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }

    @Override
    public Participant findByMail(String email) {
        return participantRepository.findParticipantByMail(email);
    }

    @Override
    public void loginUser(String mail) {
        Participant user = participantRepository.findParticipantByMail(mail);
        if (user == null) {
            user = new Participant("", "", "", mail,
                false, passwordEncoder.encode(mail));
        }
        user.setCode(generateCode());
        participantRepository.save(user);

        String message = String.format(
            "Hello, %s! \n" +
                "Your activation code: %s",
            user.getMail(),
            user.getCode()
        );

        mailSender.send(user.getMail(), "Activation code", message);
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void deleteUser(int id) {
        participantRepository.deleteById(id);
    }

    @Override
    public boolean isActive(String email, String code) {
        Participant user = participantRepository.findParticipantByMail(email);
        if (user == null) {
            return false;
        }
        if (Objects.equals(code, user.getCode())) {
            user.setActive(true);
            participantRepository.save(user);
            return true;
        }
        return false;
    }

    private Role validateAndGetRegisteredRoles(String roleString) {

        RoleName registeredRoleName = extractRoleNameFromRoleString(roleString);
        Role registeredRole = roleRepository.findByName(registeredRoleName);
        return registeredRole;
    }

    private RoleName extractRoleNameFromRoleString(String roleString) {
        switch (roleString.trim().toLowerCase()) {
            case "student":
                return RoleName.ROLE_STUDENT;
            case "teacher":
                return RoleName.ROLE_TEACHER;
            case "assistant":
                return RoleName.ROLE_ASSISTANT;
            default:
                throw new ParticipantRegistrationException("Invalid role was given for registration");
        }
    }
}
