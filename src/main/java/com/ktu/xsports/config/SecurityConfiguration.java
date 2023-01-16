package com.ktu.xsports.config;

import com.ktu.xsports.api.domain.enums.Role;
import com.ktu.xsports.config.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();

        authorizationEndpoints(http);
        sportsEndpoints(http);

        http.authorizeHttpRequests().anyRequest().authenticated();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void authorizationEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll();
    }

    private void sportsEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(GET,"/api/sports").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name(), Role.MODERATOR.name())
                .requestMatchers(GET, "/api/sports/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name(), Role.MODERATOR.name())
                .requestMatchers(POST, "/api/sports").hasAnyAuthority(Role.ADMIN.name(), Role.MODERATOR.name());
    }
}
