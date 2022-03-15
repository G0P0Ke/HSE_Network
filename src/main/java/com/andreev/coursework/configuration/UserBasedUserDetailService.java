package com.andreev.coursework.configuration;

import com.andreev.coursework.entity.Participant;
import com.andreev.coursework.service.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserBasedUserDetailService implements UserDetailsService {

    @Autowired
    private ParticipantService participantService;

    @Override
    public UserDetails loadUserByUsername(String firstName) throws UsernameNotFoundException {
        Participant user = participantService.findByFirstName(firstName);

        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<GrantedAuthority> authorities = Stream.of(user.getRole()).map(role ->
            new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());

        return new User(user.getFirstName(), user.getHashPassword(), authorities);
    }
}
