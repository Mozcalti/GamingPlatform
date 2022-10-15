package com.mozcalti.gamingapp.configurations;

import com.mozcalti.gamingapp.secutiry.AccessDeniedHandlerImpl;
import com.mozcalti.gamingapp.secutiry.AuthencationFailureHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SpringSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                    .authorizeRequests()
                    .mvcMatchers("/api/login", "/api/logout").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginProcessingUrl("/api/login")
                    .failureUrl("/login_error")
                    .failureHandler(new AuthencationFailureHandlerImpl())
                    .defaultSuccessUrl("/miPerfil")
                .and()
                    .logout().logoutUrl("/api/logout")
                    .logoutSuccessUrl("/")
                .and()
                    .exceptionHandling()
                    .accessDeniedPage("/access_denied")
                    .accessDeniedHandler(new AccessDeniedHandlerImpl());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
    }

}
