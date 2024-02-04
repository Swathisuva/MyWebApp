package com.mycompany.student.config;

import com.mycompany.student.repository.UserInfoRepository;
import com.mycompany.student.security.JwtAuthFilter;
import com.mycompany.student.user.UserInfoUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private JwtAuthFilter authFilter;
    private UserInfoRepository userInfoRepository;

    @Autowired
    public SecurityConfig(JwtAuthFilter authFilter, UserInfoRepository userInfoRepository) {
        this.authFilter = authFilter;
        this.userInfoRepository = userInfoRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserInfoUserDetailsService(userInfoRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeRequests(auth -> auth
                        .requestMatchers("/home").authenticated()
                        .requestMatchers("/auth/login", "/v3/api-docs/**", "swagger-ui/**", "/swagger-ui.html",
                                "/publishMessage", "/students/new", "/students/authenticate", "/auth/new",
                                "/auth/authenticate", "/actuator/health", "/actuator/**", "/students/addstudent").permitAll()
                        .requestMatchers("/publishMessage").hasRole("user") // Add the new path and role requirement
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
