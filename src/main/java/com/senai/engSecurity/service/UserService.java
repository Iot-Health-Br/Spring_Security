package com.senai.engSecurity.service;

import com.senai.engSecurity.Exception.NotFoundUserList;
import com.senai.engSecurity.Exception.UserWasRegistred;
import com.senai.engSecurity.dto.LoginResponse;
import com.senai.engSecurity.dto.UserDetailsDTO;
import com.senai.engSecurity.model.User;
import com.senai.engSecurity.repository.UserRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public String saveAdm(User user) throws UserWasRegistred {
        Optional<User> foundUser = userRepository.findByUsername(
                user.getUsername()
        );
        if (foundUser.isPresent()) {
            throw new UserWasRegistred("Username já cadastrado!");}
        else {
            userRepository.save(user);
            return "Adm cadastrado com sucesso!";}
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
                        authenticatedUser.getRoles(), // Agora retornamos a lista de roles
                        authenticatedUser.getId(),        // Novo campo
                        authenticatedUser.getNome()   // Novo campo
                );
            } else {
                return new LoginResponse(
                        false,
                        "Usuário ou senha inválidos",
                        null,
                        null,
                        null,
                        null
                );
            }
        } catch (Exception e) {
            return new LoginResponse(
                    false,
                    "Erro ao processar a autenticação: " + e.getMessage(),
                    null,
                    null,
                    null,
                    null
            );
        }
    }

    public List<UserDetailsDTO> getAllUsers()throws NotFoundUserList {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new NotFoundUserList("Nenhum usuário encontrado");}
        else{
            return users.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());}
    }
    private UserDetailsDTO convertToDTO(User user) {
        UserDetailsDTO dto = new UserDetailsDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
