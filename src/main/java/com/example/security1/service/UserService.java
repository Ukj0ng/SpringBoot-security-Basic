package com.example.security1.service;

import com.example.security1.model.User;
import com.example.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public int 회원가입(User user) {
        String rawPassword = user.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        user.setRole("ROLE_USER");
        user.setPassword(encPassword);
        try {
            userRepository.save(user);
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

}
