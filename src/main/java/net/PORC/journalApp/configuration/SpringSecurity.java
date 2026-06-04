package net.PORC.journalApp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ── PUBLIC ──
                        .requestMatchers(
                                "/login", "/register", "/customUser/register",
                                "/RESET1", "/ResetPassword/**",
                                "/css/**", "/js/**", "/images/**", "/music/**",

                                "/info", "/health_check"
                        ).permitAll()

                        // ── ADMIN ──
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // ── AUTHENTICATED ──
                        .requestMatchers(
                                "/home", "/journals",
                                "/journal/**", "/JUournal/**",
                                "/User/**", "/Webuser",
                                "/api/chat/**",
                                "/apiv2/**"
                        ).authenticated()

                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}