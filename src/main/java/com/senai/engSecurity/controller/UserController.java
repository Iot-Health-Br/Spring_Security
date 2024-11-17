package com.senai.engSecurity.controller;

import com.senai.engSecurity.Exception.UserWasRegistred;
import com.senai.engSecurity.dto.LoginResponse;
import com.senai.engSecurity.dto.UserDetailsDTO;
import com.senai.engSecurity.model.User;
import com.senai.engSecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST}) // Aplica CORS para todos os métodos desse controlador
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody User user) {
        try {
            String resultado = String.valueOf(userService.save(user));
            return ResponseEntity.ok(resultado);
        } catch (UserWasRegistred e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao salvar a pessoa.");
        }
    }
    @PostMapping("/saveAdm")
    public ResponseEntity<String> saveAdm(@RequestBody User user) {
        try {
            String resultado = String.valueOf(userService.saveAdm(user));
            return ResponseEntity.ok(resultado);
        } catch (UserWasRegistred e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao salvar a pessoa.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        System.out.println("Login Recebido Controller: " + user.getUsername());
        System.out.println("A Senha Recebida Controller: " + user.getPassword());
        LoginResponse response = userService.findByUsernameAndPassword(user);
        System.out.println("Retorno da Consulta: " + response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getUser")
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
