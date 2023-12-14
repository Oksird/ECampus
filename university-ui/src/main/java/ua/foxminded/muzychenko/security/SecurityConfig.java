package ua.foxminded.muzychenko.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import ua.foxminded.muzychenko.config.ServiceConfiguration;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Import(ServiceConfiguration.class)
public class SecurityConfig {

    private UserDetailsServiceImpl userDetailsService;
    private static final String ADMIN_ROLE = "ADMIN";
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(
                authorize -> authorize
                    .requestMatchers("/css/**").permitAll()
                    .requestMatchers("/js/**").permitAll()
                    .requestMatchers("/webjars/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/register").permitAll()
                    .requestMatchers(HttpMethod.POST, "/register").permitAll()
                    .requestMatchers(HttpMethod.GET, "/pending-users/profile").hasRole("PENDING")
                    .requestMatchers(HttpMethod.GET, "/students/profile").hasRole("STUDENT")
                    .anyRequest().hasRole(ADMIN_ROLE)
            )
            .formLogin(login -> login
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/admins/cpanel", true)
                .failureUrl("/login?error")
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
