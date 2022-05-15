package com.andreev.coursework.service.participant;

import com.andreev.coursework.dao.CourseRepository;
import com.andreev.coursework.dao.ParticipantRepository;
import com.andreev.coursework.dao.RoleRepository;
import com.andreev.coursework.dao.UserCourseAgentRepository;
import com.andreev.coursework.dto.CourseDto;
import com.andreev.coursework.dto.ProfileDto;
import com.andreev.coursework.entity.Course;
import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.entity.UserCourseAgent;
import com.andreev.coursework.entity.security.RoleName;
import com.andreev.coursework.service.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    private UserCourseAgentRepository userCourseAgentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MailSender mailSender;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public List<Participant> getAllUsers() {
        return participantRepository.findAll();
    }

    @Override
    public void saveUser(Participant user) {
        participantRepository.save(user);
    }

    @Override
    public void updateProfileUser(Participant participant, ProfileDto profileDto) {
        if (!profileDto.getFirstName().isEmpty())  {
            participant.setFirstName(profileDto.getFirstName());
        }
        if (!profileDto.getSurname().isEmpty()) {
            participant.setSecondName(profileDto.getSurname());
        }
        if (!profileDto.getPatronymic().isEmpty()) {
            participant.setPatronymic(profileDto.getPatronymic());
        }
        if (!profileDto.getMail().isEmpty()) {
            participant.setMail(profileDto.getMail());
        }
        participantRepository.save(participant);
    }

    @Override
    public void addCourse(Participant participant, CourseDto courseDto) {
        Course course = new Course(courseDto.getName(), courseDto.getDescription());
        courseRepository.save(course);
        UserCourseAgent userCourseAgent = course.addParticipant(participant, roleRepository.findByName(RoleName.ROLE_TEACHER));
        userCourseAgentRepository.save(userCourseAgent);
    }

    @Override
    public Participant getUser(int id) {
        Participant user = null;
        Optional<Participant> optional = participantRepository.findById(id);
        if (optional.isPresent()) {
            user = optional.get();
        }
        return user;
    }

    @Override
    public Participant findByMail(String email) {
        return participantRepository.findParticipantByMail(email);
    }

    @Override
    public void loginUser(String mail) {
        Participant user = participantRepository.findParticipantByMail(mail);
        if (user == null) {
            user = new Participant("", "", "", mail,
                false, passwordEncoder.encode(mail));
        }
        user.setCode(generateCode());
        participantRepository.save(user);

        String message = String.format(
            "Hello, %s! \n" +
                "Your activation code: %s",
            user.getMail(),
            user.getCode()
        );

        mailSender.send(user.getMail(), "Activation code", message);
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void deleteUser(int id) {
        participantRepository.deleteById(id);
    }

    @Override
    public boolean isActive(String email, String code) {
        Participant user = participantRepository.findParticipantByMail(email);
        if (user == null) {
            return false;
        }
        if (Objects.equals(code, user.getCode())) {
            user.setActive(true);
            participantRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUserRoleInCourse(Course course, Authentication authentication) {
        Participant participant = findByMail(authentication.getName());
        UserCourseAgent userCourseAgent = userCourseAgentRepository
            .findUserCourseAgentByCourseAndParticipant(course, participant);
        return switch (userCourseAgent.getRole().getName()) {
            case ROLE_ASSISTANT -> true;
            case ROLE_TEACHER -> true;
            default -> false;
        };
    }
}
