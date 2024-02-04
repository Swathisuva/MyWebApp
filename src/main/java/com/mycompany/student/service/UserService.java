package com.mycompany.student.service;

import com.mycompany.student.user.UserInfo;
import com.mycompany.student.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository repository;

    @Autowired
    public UserService(UserInfoRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }
    @CacheEvict(value = "userInfoCache", allEntries = true)
    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User added to system.";
    }
}