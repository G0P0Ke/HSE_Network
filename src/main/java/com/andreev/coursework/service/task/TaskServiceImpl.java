package com.andreev.coursework.service.task;

import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dao.TaskRepository;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public void createTask(String description, String dateFinish, MultipartFile file, int creatorId) {
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date finishDate = date.parse(dateFinish);
            byte[] pdf = file.getBytes();
            Participant creator = participantRepository.getById(creatorId);
            Task task = new Task(description, finishDate, pdf, creator);
            taskRepository.save(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task showTaskById(int id) {
        Task task = null;
        Optional<Task> optional = taskRepository.findById(id);
        if (optional.isPresent()) {
            task = optional.get();
        }
        return task;
    }

    @Override
    public void downloadTaskById(int id) {
        Task task = null;
        Optional<Task> optional = taskRepository.findById(id);
        if (optional.isPresent()) {
            task = optional.get();
        }
        if (task != null) {
            try (OutputStream out = new FileOutputStream("/Users/antonandreev/Downloads/out.pdf")) {
                byte[] buffer = task.getPdf();
                out.write(buffer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
