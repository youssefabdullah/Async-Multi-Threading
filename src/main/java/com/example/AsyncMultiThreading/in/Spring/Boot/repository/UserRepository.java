package com.example.AsyncMultiThreading.in.Spring.Boot.repository;

import com.example.AsyncMultiThreading.in.Spring.Boot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
