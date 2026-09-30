package com.dimbisoapatrick.springjwt.config;
import com.dimbisoapatrick.springjwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean public AuthenticationProvider authenticationProvider(UserService userService) {
        var provider = new DaoAuthenticationProvider(userService);
        provider.setPasswordEncoder(passwordEncoder()); return provider;
    }
    @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Browser sessions need CSRF protection; only the public JSON signup is exempt.
        return http.csrf(csrf -> csrf.ignoringRequestMatchers("/req/signup"))
            .authorizeHttpRequests(auth -> auth.requestMatchers("/req/login", "/req/register", "/req/signup", "/req/signup/verify", "/req/js/**", "/req/css/**").permitAll().anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/req/login").loginProcessingUrl("/login").defaultSuccessUrl("/req/expenses", true).permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/req/login?logout")).build();
    }
}
