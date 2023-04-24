package com.andreev.coursework.core.model.dao;

import com.andreev.coursework.core.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
