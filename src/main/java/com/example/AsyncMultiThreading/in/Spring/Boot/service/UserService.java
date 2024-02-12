package com.example.AsyncMultiThreading.in.Spring.Boot.service;

import com.example.AsyncMultiThreading.in.Spring.Boot.entity.User;
import com.example.AsyncMultiThreading.in.Spring.Boot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Async(value = "taskExecutor")
    public CompletableFuture<List<User>> saveUser(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();
        List<User> users = parseCsv(file);
        logger.info("saving list of users of size {}", users.size(), "" + Thread.currentThread().getName());
        users = userRepository.saveAll(users);
        long end = System.currentTimeMillis();
        logger.info("Total time {}", end - start);
        return CompletableFuture.completedFuture(users);
    }

    @Async(value = "taskExecutor")
    public CompletableFuture<List<User>> findAllUsers() {

        List<User> users = userRepository.findAll();
        logger.info("saving list of users of size {}"+Thread.currentThread().getName());
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parseCsv(final MultipartFile file) throws Exception {

        final List<User> users = new ArrayList<>();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                final String[] data = line.split(",");
                final User user = new User(data[0], data[1], data[2]);
                users.add(user);
            }
            return users;
        } catch (final IOException e) {
            throw new Exception("Failed to parse CSV file {}", e);
        }

    }
}
