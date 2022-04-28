package com.andreev.coursework.service.participant;

import com.andreev.coursework.entity.Participant;

import java.util.List;

public interface ParticipantService {

    Participant findByFirstName(String firstName);

    void loginUser(String email);

    List<Participant> getAllUsers();

    void saveUser(Participant user);

    Participant getUser(int id);

    void deleteUser(int id);
}
