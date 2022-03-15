package com.andreev.coursework.service.task;

import com.andreev.coursework.entity.Task;
import org.springframework.web.multipart.MultipartFile;

public interface TaskService {

    public Task showTaskById(int id);

    public void downloadTaskById(int id);

    public void createTask(String description, String dateFinish, MultipartFile file, int creatorId);
}
