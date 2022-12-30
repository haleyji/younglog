package com.younglog.service;

import com.younglog.domain.Session;
import com.younglog.domain.User;
import com.younglog.exception.InvalidSigninInformation;
import com.younglog.repository.UserRepository;
import com.younglog.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public String login(Login request){
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();
        return session.getAccessToken();
    }
}
