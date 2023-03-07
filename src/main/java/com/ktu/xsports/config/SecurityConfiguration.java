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

import static com.ktu.xsports.api.util.Role.*;
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
        healthEndpoint(http);
        imageEndpoints(http);
        videoEndpoints(http);

        testEndpoint(http);

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
            .requestMatchers(GET, "/api/users/basic").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, "/api/users/basic/{id}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, "/api/users/me").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(PUT, "/api/users/me").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, "/api/users/me/image").hasAnyAuthority(USER, ADMIN, MODERATOR);

        //Administration endpoints
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/users").hasAnyAuthority(ADMIN)
            .requestMatchers(GET, "/api/users/{id}").hasAnyAuthority(ADMIN)
            .requestMatchers(POST, "/api/users").hasAnyAuthority(ADMIN)
            .requestMatchers(PUT, "/api/users").hasAnyAuthority(ADMIN)
            .requestMatchers(DELETE, "/api/users").hasAnyAuthority(ADMIN);
    }

    private void sportsEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET,"/api/sports").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, "/api/sports/my_list").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, "/api/sports/{id}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, "/api/sports/my_list/explore").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, "/api/sports").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(POST, "/api/sports/my_list").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, "/api/sports/{id}/image").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, "/api/sports/{id}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(DELETE, "api/sports/my_list").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(DELETE, "/api/sports/{id}").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void categoriesEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/sports/{sportId}/categories").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, "/api/sports/{sportId}/categories/{id}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, "/api/sports/{sportId}/categories").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, "/api/sports/{sportId}/categories/{id}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(DELETE, "/api/sports/{sportId}/categories/{id}").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void tricksEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/sports/{sportId}/categories/{categoryId}/tricks").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, "/api/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, "/api/sports/{sportId}/categories/{categoryId}/tricks").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, "/api/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, "/api/sports/{sportId}/categories/{categoryId}/tricks/{trickId}/progress").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(DELETE, "/api/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void lessonsEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/lessons").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, "/api/lessons/{id}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, "/api/lessons").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, "/api/lessons/{id}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(DELETE, "/api/lessons/{id}").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void healthEndpoint(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/health").permitAll();
    }

    private void imageEndpoints(HttpSecurity http) throws Exception {
        //User based requests
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/images/{fileName}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, "/api/images/user").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(PUT, "/api/images/user").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(DELETE, "/api/images/user").hasAnyAuthority(USER, ADMIN, MODERATOR);


    }

    private void videoEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/videos/{fileName}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, "/api/videos").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, "/api/videos").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(DELETE, "/api/videos").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void testEndpoint(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(POST, "/api/users/test").permitAll();
    }
}
