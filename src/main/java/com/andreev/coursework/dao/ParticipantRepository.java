package com.andreev.coursework.dao;

import com.andreev.coursework.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    Participant findByFirstName(String login);

    Participant findParticipantByMail(String mail);
}
