package com.stores.stridestar.access;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/", "/home", "/fontawesome/**")
                                                .permitAll()
                                                .requestMatchers("/admin/**", "/assets/admin/**")
                                                .hasRole("ADMIN") // Use hasRole instead of hasAnyAuthority
                                                .requestMatchers("/api/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .permitAll())
                                .logout(logout -> logout.permitAll())
                                .build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
                UserDetails user = User.withDefaultPasswordEncoder()
                                .username("user")
                                .password("123")
                                .roles("USER")
                                .build();
                UserDetails admin = User.withDefaultPasswordEncoder()
                                .username("admin")
                                .password("123")
                                .roles("ADMIN")
                                .build();

                return new InMemoryUserDetailsManager(user, admin);
        }
}
