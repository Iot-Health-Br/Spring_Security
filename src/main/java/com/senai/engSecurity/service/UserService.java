package com.senai.engSecurity.service;

import com.senai.engSecurity.Exception.UserWasRegistred;
import com.senai.engSecurity.dto.LoginResponse;
import com.senai.engSecurity.model.User;
import com.senai.engSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String save(User user) throws UserWasRegistred {
        Optional<User> foundUser = userRepository.findByUsername(
                user.getUsername()
        );
        if (foundUser.isPresent()) {
            throw new UserWasRegistred("Username já cadastrado!");}
        else {
            user.setRoles(Collections.singletonList("USER"));
            userRepository.save(user);
            return "Usuário cadastrado com sucesso!";}
    }


    public LoginResponse findByUsernameAndPassword(User user) {
        System.out.println("Login Recebido Service: " + user.getUsername());
        System.out.println("A Senha Recebida Service: " + user.getPassword());
        //user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        try {
            Optional<User> foundUser = userRepository.findByUsernameAndPassword(
                    user.getUsername(),
                    user.getPassword()
            );

            System.out.println("Retorno da Consulta Service: " + foundUser);

            if (foundUser.isPresent()) {
                User authenticatedUser = foundUser.get();
                return new LoginResponse(
                        true,
                        "Usuário autenticado com sucesso",
                        authenticatedUser.getUsername(),
                        authenticatedUser.getRoles() // Agora retornamos a lista de roles
                );
            } else {
                return new LoginResponse(
                        false,
                        "Usuário ou senha inválidos",
                        null,
                        null
                );
            }
        } catch (Exception e) {
            return new LoginResponse(
                    false,
                    "Erro ao processar a autenticação: " + e.getMessage(),
                    null,
                    null
            );
        }
    }
}
