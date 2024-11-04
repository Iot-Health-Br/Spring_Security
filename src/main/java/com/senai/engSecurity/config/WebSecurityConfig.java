package com.senai.engSecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String URL = "/api";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder authenticationManager) throws Exception {
        authenticationManager
                .userDetailsService(userDetailsService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")) // Modificada a configuração do CSRF para ignorar requisições do console H2:
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())) // Adiciona a configuração para permitir frames da mesma origem:
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(URL + "/").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "user").permitAll()
                        .requestMatchers(HttpMethod.POST, "user/login").permitAll()
                        .requestMatchers(URL + "api/user").hasAnyRole("USER", "ADM")
                        .requestMatchers(URL + "/adm").hasAnyRole("ADM")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}