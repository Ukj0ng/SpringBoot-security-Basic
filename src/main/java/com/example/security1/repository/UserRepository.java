package com.example.security1.repository;

import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고 있음.
// @Repository라는 어노테이션이 없어도 IoC가 됨 -> JpaRepository를 상속받았기 때문에
public interface UserRepository extends JpaRepository<User, Integer> {
    // findBy규칙 -> Username, Jpa query methods
    // select * from user where username = 1?
    User findByUsername(String username);
}
