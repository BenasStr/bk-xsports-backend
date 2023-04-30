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

import static com.ktu.xsports.api.util.ApiVersionPrefix.*;
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
        variantEndpoints(http);
        difficultyEndpoints(http);
        publishEndpoints(http);
        imageEndpoints(http);
        videoEndpoints(http);
        statisticsEndpoints(http);
        healthEndpoint(http);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void authorizationEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(API_V1 + "/auth/**").permitAll();
    }

    private void userEndpoints(HttpSecurity http) throws Exception {
        //Basic user endpoints
        http.authorizeHttpRequests()
            .requestMatchers(GET, API_V1 + "/users/basic/{id}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, API_V1 + "/users/me").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(PUT, API_V1 + "/users/me").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/users/me/image").hasAnyAuthority(USER, ADMIN, MODERATOR);

        //Administration endpoints
        http.authorizeHttpRequests()
            .requestMatchers(GET, API_V1 + "/users").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(GET, API_V1 + "/users/{id}").hasAnyAuthority(ADMIN)
            .requestMatchers(POST, API_V1 + "/users").hasAnyAuthority(ADMIN)
            .requestMatchers(PUT, API_V1 + "/users").hasAnyAuthority(ADMIN)
            .requestMatchers(DELETE, API_V1 + "/users/{id}").hasAnyAuthority(ADMIN);
    }

    private void sportsEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET,API_V1 + "/sports").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, API_V1 + "/sports/my_list").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, API_V1 + "/sports/{id}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, API_V1 + "/sports/my_list/explore").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/sports").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/sports/my_list").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/sports/{id}/image").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, API_V1 + "/sports/{id}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(DELETE, API_V1 + "/sports/my_list").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(DELETE, API_V1 + "/sports/{id}").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void categoriesEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, API_V1 + "/sports/{sportId}/categories").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, API_V1 + "/sports/{sportId}/categories/{id}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/sports/{sportId}/categories/{categoryId}/image").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/sports/{sportId}/categories").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, API_V1 + "/sports/{sportId}/categories/{id}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(DELETE, API_V1 + "/sports/{sportId}/categories/{id}").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void tricksEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(GET, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks/{trickId}/variant").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks/{trickVariantId}/video").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks/{trickId}/variant/{variantId}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks/{trickId}/progress").hasAnyAuthority(USER, ADMIN, MODERATOR)
            .requestMatchers(DELETE, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(DELETE, API_V1 + "/sports/{sportId}/categories/{categoryId}/tricks/{trickId}/variant/{variantId}").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void variantEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, API_V1 + "/variants").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/variants").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(PUT, API_V1 + "/variants/{variantId}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(DELETE, API_V1 + "/variants/{variantId}").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void difficultyEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, API_V1 + "/difficulties").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void publishEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, API_V1 + "/publish").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(GET, API_V1 + "/publish/categories").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/publish").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(POST, API_V1 + "/publish/now/sport/{sportId}/category/{categoryId}").hasAnyAuthority(ADMIN)
            .requestMatchers(PUT, API_V1 + "/publish/{id}").hasAnyAuthority(ADMIN, MODERATOR)
            .requestMatchers(DELETE, API_V1 + "/publish/{id}").hasAnyAuthority(ADMIN, MODERATOR);
    }

    private void healthEndpoint(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, API_V1 + "/health").permitAll();
    }

    private void imageEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/images/{fileName}").permitAll();
    }

    private void videoEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, "/api/videos/{fileName}").permitAll();
    }

    private void statisticsEndpoints(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers(GET, API_V1 + "/statistics").hasAnyAuthority(USER, ADMIN, MODERATOR);
    }
}
