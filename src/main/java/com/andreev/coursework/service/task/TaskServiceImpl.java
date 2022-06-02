package com.andreev.coursework.service.task;

import com.andreev.coursework.dao.AnswerRepository;
import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dao.TaskRepository;
import com.andreev.coursework.entity.Answer;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ParticipantRepository participantRepository;
    private final CourseService courseService;
    private final AnswerRepository answerRepository;

    public TaskServiceImpl(TaskRepository taskRepository, ParticipantRepository participantRepository,
        CourseService courseService, AnswerRepository answerRepository) {
        this.taskRepository = taskRepository;
        this.participantRepository = participantRepository;
        this.courseService = courseService;
        this.answerRepository = answerRepository;
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
            return task;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public Answer addAnswer(String dateSend, MultipartFile solution, Task task, Participant student) {
        DateFormat date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Answer answer = new Answer();
        try {
            Date dateFinish = date.parse(dateSend);
            byte[] pdf = solution.getBytes();
            answer = new Answer(pdf, dateFinish, task, student);
            answerRepository.save(answer);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return answer;
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
