package com.andreev.coursework.service.task;

import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dao.TaskRepository;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.Task;
import com.andreev.coursework.service.course.CourseService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ParticipantRepository participantRepository;
    private final CourseService courseService;

    public TaskServiceImpl(TaskRepository taskRepository, ParticipantRepository participantRepository,
        CourseService courseService) {
        this.taskRepository = taskRepository;
        this.participantRepository = participantRepository;
        this.courseService = courseService;
    }

    @Override
    public Task createTask(String description, String dateFinish, MultipartFile file, int creatorId, Course course) {
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Task task = new Task();
        try {
            Date finishDate = date.parse(dateFinish);
            byte[] pdf = file.getBytes();
            Participant creator = participantRepository.getById(creatorId);
            task.setDescription(description);
            task.setDateFinish(finishDate);
            task.setPdf(pdf);
            task.setCreator(creator);
            courseService.addTaskToCourse(task, course);
            taskRepository.save(task);
            return task;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
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
    public void downloadTaskById(int id, String path) {
        Task task = null;
        Optional<Task> optional = taskRepository.findById(id);
        if (optional.isPresent()) {
            task = optional.get();
        }
        if (task != null) {
            try (OutputStream out = new FileOutputStream(path)) {
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
