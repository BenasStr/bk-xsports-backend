package com.ktu.xsports.config;

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

import static com.ktu.xsports.api.domain.enums.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();

        authorizationEndpoints(http);
        userEndpoints(http);
        sportsEndpoints(http);
        categoriesEndpoints(http);
        tricksEndpoints(http);
        lessonsEndpoints(http);
        imageEndpoint(http);
        healthEndpoint(http);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void authorizationEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll();
    }

    private void userEndpoints(HttpSecurity http) throws Exception {
        //Basic user endpoints
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/users/basic").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
            .requestMatchers(GET, "/api/users/basic/{id}").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
            .requestMatchers(GET, "/api/users/me").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
            .requestMatchers(PUT, "/api/users/me").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name());

        //Administration endpoints
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/users").hasAnyAuthority(ADMIN.name())
            .requestMatchers(GET, "/api/users/{id}").hasAnyAuthority(ADMIN.name())
            .requestMatchers(POST, "/api/users").hasAnyAuthority(ADMIN.name())
            .requestMatchers(PUT, "/api/users").hasAnyAuthority(ADMIN.name())
            .requestMatchers(DELETE, "/api/users").hasAnyAuthority(ADMIN.name());
    }

    private void sportsEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(GET,"/api/sports").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
                .requestMatchers(GET, "/api/sports/my_list").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
                .requestMatchers(GET, "/api/sports/{id}").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
                .requestMatchers(POST, "/api/sports").hasAnyAuthority(ADMIN.name(), MODERATOR.name())
                .requestMatchers(POST, "/api/sports/my_list").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
                .requestMatchers(PUT, "/api/sports/{id}").hasAnyAuthority(ADMIN.name(), MODERATOR.name())
                .requestMatchers(DELETE, "api/sports/my_list").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
                .requestMatchers(DELETE, "/api/sports/{id}").hasAnyAuthority(ADMIN.name(), MODERATOR.name());
    }

    private void categoriesEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(GET, "/api/sports/{sportId}/categories").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
                .requestMatchers(GET, "/api/sports/{sportId}/categories/{id}").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
                .requestMatchers(POST, "/api/sports/{sportId}/categories").hasAnyAuthority(ADMIN.name(), MODERATOR.name())
                .requestMatchers(PUT, "/api/sports/{sportId}/categories/{id}").hasAnyAuthority(ADMIN.name(), MODERATOR.name())
                .requestMatchers(DELETE, "/api/sports/{sportId}/categories/{id}").hasAnyAuthority(ADMIN.name(), MODERATOR.name());
    }

    private void tricksEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/sports/{sportId}/categories/{categoryId}/tricks").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
            .requestMatchers(GET, "/api/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
            .requestMatchers(POST, "/api/sports/{sportId}/categories/{categoryId}/tricks").hasAnyAuthority(ADMIN.name(), MODERATOR.name())
            .requestMatchers(PUT, "/api/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority(ADMIN.name(), MODERATOR.name())
            .requestMatchers(DELETE, "/api/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority(ADMIN.name(), MODERATOR.name());
    }

    private void lessonsEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/lessons").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
            .requestMatchers(GET, "/api/lessons/{id}").hasAnyAuthority(USER.name(), ADMIN.name(), MODERATOR.name())
            .requestMatchers(POST, "/api/lessons").hasAnyAuthority(ADMIN.name(), MODERATOR.name())
            .requestMatchers(PUT, "/api/lessons/{id}").hasAnyAuthority(ADMIN.name(), MODERATOR.name())
            .requestMatchers(DELETE, "/api/lessons/{id}").hasAnyAuthority(ADMIN.name(), MODERATOR.name());
    }

    private void imageEndpoint(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(POST, "/api/images/upload").permitAll();
    }

    private void healthEndpoint(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/health").permitAll();
    }
}
