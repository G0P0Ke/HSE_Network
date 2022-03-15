package com.andreev.coursework.dao;

import com.andreev.coursework.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    Participant findByFirstName(String login);
}
