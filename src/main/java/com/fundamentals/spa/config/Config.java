package com.fundamentals.spa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class Config {
    public static final ZoneId SPA_ZONE = ZoneId.of("Europe/Sofia");

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Clock spaClock() {
        return Clock.system(SPA_ZONE);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers(
                                "/", "/login", "/register", "/error", "/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());
        return http.build();
    }
}
