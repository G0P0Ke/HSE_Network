package com.andreev.coursework.service.participant;

import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dao.RoleRepository;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.security.Role;
import com.andreev.coursework.entity.security.RoleName;
import com.andreev.coursework.exception.paricipant.ParticipantRegistrationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Participant findByFirstName(String firstName) {
        return participantRepository.findByFirstName(firstName);
    }

    @Override
    public List<Participant> getAllUsers() {
        return participantRepository.findAll();
    }

    @Override
    public void saveUser(Participant user) {
        participantRepository.save(user);
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
    public void loginUser(String email) {
        Participant user = participantRepository.findParticipantByMail(email);
        if (user == null) {
            user = new Participant("", "", "", email, false);
            participantRepository.save(user);
        }
        user.setCode(generateCode());
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void deleteUser(int id) {
        participantRepository.deleteById(id);
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
