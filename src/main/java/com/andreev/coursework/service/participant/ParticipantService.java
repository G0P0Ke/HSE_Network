package com.andreev.coursework.service.participant;

import com.andreev.coursework.entity.Participant;

import java.util.List;

public interface ParticipantService {

    public Participant findByFirstName(String FirstName);

    public List<Participant> getAllUsers();

    public void saveUser(Participant user);

    public Participant getUser(int id);

    public void deleteUser(int id);

    public void createEmployee(String firstName, String secondName, String patronymic,
        String mail, String hashPassword, byte enabled, String roleString);
}
