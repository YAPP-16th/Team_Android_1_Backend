package com.eroom.erooja.features.auth.config;

import com.eroom.erooja.domain.enums.MemberRole;
import com.eroom.erooja.features.auth.jwt.JwtAuthenticationEntryPoint;
import com.eroom.erooja.features.auth.jwt.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtRequestFilter jwtRequestFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
                /* GUEST, PUBLIC */
                .antMatchers("/api/v1/auth/**")
                    .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/jobGroup/**")
                    .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/jobInterest/**")
                    .permitAll()
                /* GENERAL MEMBERS */
                .antMatchers("/api/v1/**")
                    .hasAuthority(MemberRole.ROLE_USER.getAuthority())
                /* DEVELOPER */
                .antMatchers("/api/**")
                    .hasAuthority(MemberRole.ROLE_DEVELOPER.getAuthority())
                /* ADMIN */
                .antMatchers("/admin/**")
                    .hasAuthority(MemberRole.ROLE_ADMIN.getAuthority())
                .anyRequest()
                    .authenticated()
            .and()
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
