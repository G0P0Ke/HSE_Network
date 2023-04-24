package com.andreev.coursework.core.model.dao;

import com.andreev.coursework.core.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
