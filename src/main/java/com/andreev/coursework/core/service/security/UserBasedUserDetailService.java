package com.andreev.coursework.core.service.security;

import com.andreev.coursework.core.model.dao.ParticipantRepository;
import com.andreev.coursework.core.model.Participant;
import com.andreev.coursework.core.service.security.vo.UserPrinciple;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserBasedUserDetailService implements UserDetailsService {

    private final ParticipantRepository participantRepository;

    public UserBasedUserDetailService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Participant user = participantRepository.findParticipantByMail(email);
            return UserPrinciple.build(user);
        } catch (EntityNotFoundException e) {
            throw new UsernameNotFoundException("User not found", e);
        }
    }
}
