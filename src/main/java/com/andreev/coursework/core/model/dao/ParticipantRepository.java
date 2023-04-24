package com.andreev.coursework.core.model.dao;

import com.andreev.coursework.core.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    Participant findByFirstName(String login);

    Participant findParticipantByMail(String mail);
}
