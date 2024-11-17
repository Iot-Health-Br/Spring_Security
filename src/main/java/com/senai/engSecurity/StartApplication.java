package com.senai.engSecurity;


import com.senai.engSecurity.model.User;
import com.senai.engSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class StartApplication implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        // Criação do usuário ADMIN
        createUserIfNotExists("admin", "administrator", "123", "ADMIN");

        // Criação do usuário USER
        createUserIfNotExists("user", "user", "123", "USER");

        // Criação do usuário MANAGER
        createUserIfNotExists("igor", "Igor Leonor Macedo", "123", "MANAGER");
    }

    private void createUserIfNotExists(String username, String name, String password, String role) {
        Optional<User> optionalUser = repository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            User user = new User();
            user.setUsername(username);
            user.setNome(name);
            user.setPassword(password);
            //user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.getRoles().add(role);
            repository.save(user);
        }
    }
}
