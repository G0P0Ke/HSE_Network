package com.andreev.coursework.core.model.dao;

import com.andreev.coursework.core.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
