package com.senai.engSecurity.controller;

import com.senai.engSecurity.dto.LoginResponse;
import com.senai.engSecurity.model.User;
import com.senai.engSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST}) // Aplica CORS para todos os métodos desse controlador
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User save(@RequestBody User user) {
        return userService.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        System.out.println("Login Recebido Controller: " + user.getUsername());
        System.out.println("A Senha Recebida Controller: " + user.getPassword());
        LoginResponse response = userService.findByUsernameAndPassword(user);
        System.out.println("Retorno da Consulta: " + response);
        return ResponseEntity.ok(response);
    }
}
