//package com.ktu.xsports.api.security;
//
//import com.ktu.xsports.api.filter.CustomAuthenticationFilter;
//import com.ktu.xsports.api.filter.CustomAuthorizationFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import org.springframework.web.bind.annotation.CrossOrigin;
//
//import static org.springframework.http.HttpMethod.*;
//import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
//
//@Configuration
//@EnableWebSecurity
//@CrossOrigin
//@RequiredArgsConstructor
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final UserDetailsService userDetailsService;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
//        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
//        http.csrf().disable();
//        http.cors();
//        http.sessionManagement().sessionCreationPolicy(STATELESS).and().exceptionHandling();
//        http.authorizeRequests().antMatchers("/api/login/**" , "/api/token/refresh/**").permitAll();
//        http.authorizeRequests().antMatchers(GET, "/api/user/**").hasAnyAuthority("admin");
//        http.authorizeRequests().antMatchers(POST, "/api/users").permitAll();
//        sportsControllerPermissions(http);
//        categoriesControllerPermissions(http);
//        tricksControllerPermissions(http);
//        http.authorizeRequests().antMatchers(GET,"/difficulties").permitAll();
////        http.authorizeRequests().antMatchers(POST,"/api/sports/**").permitAll();
//        http.authorizeRequests().anyRequest().authenticated();
//        http.addFilter(customAuthenticationFilter);
//        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    private void sportsControllerPermissions(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers(GET,
//                "/api/sports/**").permitAll();
//        http.authorizeRequests().antMatchers(POST,
//                "/api/sports").hasAnyAuthority("admin");
//        http.authorizeRequests().antMatchers(PUT,
//                "/api/sports/{id}").hasAnyAuthority("admin");
//        http.authorizeRequests().antMatchers(DELETE,
//                "/api/sports/{id}").hasAnyAuthority("admin");
//    }
//
//    private void categoriesControllerPermissions(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers(GET,
//                "/api/sports/{sportId}/categories/**").permitAll();
//        http.authorizeRequests().antMatchers(POST,
//                "/api/sports/{sportId}/categories").hasAnyAuthority("admin");
//        http.authorizeRequests().antMatchers(PUT,
//                "/api/sports/{sportId}/categories/{categoryId}").hasAnyAuthority("admin");
//        http.authorizeRequests().antMatchers(DELETE,
//                "/api/sports/{sportId}/categories/{categoryId}").hasAnyAuthority("admin");
//    }
//
//    private void tricksControllerPermissions(HttpSecurity http) throws Exception {
//        http.authorizeRequests().antMatchers(GET,
//                "/api/sports/{sportId}/categories/{categoryId}/tricks/**").permitAll();
//        http.authorizeRequests().antMatchers(POST,
//                "/api/sports/{sportId}/categories/{categoryId}/tricks").hasAnyAuthority("admin");
//        http.authorizeRequests().antMatchers(PUT,
//                "/api/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority("admin");
//        http.authorizeRequests().antMatchers(DELETE,
//                "/api/sports/{sportId}/categories/{categoryId}/tricks/{trickId}").hasAnyAuthority("admin");
//    }
//
//}
