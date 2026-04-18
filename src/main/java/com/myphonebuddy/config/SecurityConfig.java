package com.myphonebuddy.config;

import com.myphonebuddy.handler.AuthFailureHandler;
import com.myphonebuddy.handler.OAuthSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
public class SecurityConfig {

    private final OAuthSuccessHandler successHandler;

    private final AuthFailureHandler authFailureHandler;

    public SecurityConfig(OAuthSuccessHandler successHandler, AuthFailureHandler authFailureHandler) {
        this.successHandler = successHandler;
        this.authFailureHandler = authFailureHandler;
    }

    // Main security filter-chain configuration.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll())
            .formLogin(form -> form
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginPage("/login")
                    .loginProcessingUrl("/authenticate")
                    .defaultSuccessUrl("/user/dashboard", true)
                    // same goes for failure URL
                    .failureUrl("/login?error=true")
                    .failureHandler(authFailureHandler))

            .logout(logout -> logout
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern(HttpMethod.GET, "/logout"))
                    .logoutSuccessUrl("/login?logout=true"))

            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .successHandler(successHandler));


        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
