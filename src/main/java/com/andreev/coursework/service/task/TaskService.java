package com.andreev.coursework.service.task;

import com.andreev.coursework.entity.Answer;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import org.springframework.web.multipart.MultipartFile;

public interface TaskService {

    Task showTaskById(int id);

    void downloadTaskById(int id, String path);

    Task createTask(String description, String dateFinish, MultipartFile file, int creatorId, Course course);

    Answer addAnswer(String dateSend, MultipartFile solution, Task task, Participant student);
}
