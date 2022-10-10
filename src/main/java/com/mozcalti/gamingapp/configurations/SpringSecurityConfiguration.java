package com.mozcalti.gamingapp.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().ignoringAntMatchers("/institucion/**");
        http.csrf().ignoringAntMatchers("/perfil/**");
        http.csrf().ignoringAntMatchers("/torneo/**");

        return http.build();
    }

}
