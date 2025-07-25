package com.onetouch.delinight.Config;

import com.onetouch.delinight.Util.CustomUserDetails;
import jakarta.servlet.annotation.WebListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@WebListener
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }




    @Bean
    @Order(3)
    SecurityFilterChain FilterChain1(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(

                authorize -> authorize
                        .requestMatchers("/users/home" , "/users/login", "/css/**", "/js/**", "/images/**", "/**").permitAll()
                        .anyRequest().permitAll()
        ).csrf((csrf) -> csrf.disable())

        .formLogin(formLogin -> formLogin
                .loginPage("/users/login")
                .loginProcessingUrl("/users/login")
                .defaultSuccessUrl("/users/home")
                .failureHandler(new CustomAuthenticationFailureHandler()) // 로그인 실패 핸들러 추가
                .usernameParameter("email")
        )
        .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutUrl("/logout")
                .logoutSuccessUrl("/users/welcome")
                .invalidateHttpSession(true)
        )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/users/login")
                        .maximumSessions(1).expiredUrl("/users/login"));

        return http.build();
    }

    @Bean
    @Order(1)
    SecurityFilterChain FilterChain2(HttpSecurity http) throws Exception {

        http.securityMatcher("/members/**")
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/members/adminhome", "/members/account/login", "/members/account/adminlogout-success", "/**").permitAll()
                                .anyRequest().permitAll()
                ).csrf((csrf) -> csrf.disable())

                .formLogin(formLogin -> formLogin
                        .loginPage("/members/account/login")
                        .loginProcessingUrl("/members/account/login")
                        .defaultSuccessUrl("/members/account/dashHub")
                        .failureHandler(new CustomAuthenticationFailureHandler()) // 로그인 실패 핸들러 추가
                        .usernameParameter("email")
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutUrl("/members/account/logout")
                        .logoutSuccessUrl("/members/account/logout-success")
                        .invalidateHttpSession(true)
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/members/account/login")
                        .maximumSessions(1).expiredUrl("/users/login"));

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain FilterChain3(HttpSecurity http) throws Exception {

        http.securityMatcher("/guests/**")
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/users/home" , "/users/login" , "/**").permitAll()
                                .anyRequest().permitAll()
                ).csrf((csrf) -> csrf.disable())

                .formLogin(formLogin -> formLogin
                        .loginPage("/users/login")
                        .loginProcessingUrl("/guests/login")
                        .defaultSuccessUrl("/users/home")
                        .failureHandler(new CustomAuthenticationFailureHandler()) // 로그인 실패 핸들러 추가
                        .usernameParameter("phone")
                )
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/users/welcome")
                        .invalidateHttpSession(true)
                );
        return http.build();
    }

}
