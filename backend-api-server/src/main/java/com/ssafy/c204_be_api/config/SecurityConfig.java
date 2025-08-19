package com.ssafy.c204_be_api.config;

import com.ssafy.c204_be_api.authentication.filter.JwtAuthenticationFilter;
import com.ssafy.c204_be_api.authentication.token.JwtManager;
import com.ssafy.c204_be_api.config.property.CorsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtManager jwtManager;
    private final AuthenticationEntryPoint entryPoint;
    private final CorsProperties corsProperties;

    private static final String adminPath = "/api/v1/admin/**";

    private static final String[] excludeGetPath = {
            "/actuator/**",

            "/swagger", "/swagger-ui.html", "/swagger-ui/**",
            "/api-docs", "/api-docs/**", "/v3/api-docs/**",
            "/favicon.ico", "/docs/**",

            "/api/health/**",

            "/api/v1/rooms/**", "/api/v1/ingame/**",
    };

    private static final String[] excludePostPath = {
            "/api/v1/auth/**", "/api/v1/webrtc/**",
            "/api/v1/problems/judge-result/**",
    };

    private static final String[] excludeOptionPath = {
            "/**"
    };

    private static final String[] excludeSocketPath = {  // Web Socket Hand Shake 과정에서의 인증 생략
//            "/ws/**", "/topic/**",  // 임시 허용
    };

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(excludeSocketPath).permitAll()
                .requestMatchers(HttpMethod.OPTIONS, excludeOptionPath).permitAll()
                .requestMatchers(HttpMethod.GET, excludeGetPath).permitAll()
                .requestMatchers(HttpMethod.POST, excludePostPath).permitAll()
                .requestMatchers(adminPath).hasRole("ADMIN")
                .anyRequest().authenticated()
        );
        http.addFilterBefore(new JwtAuthenticationFilter(jwtManager), UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint));

        return http.build();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsProperties.getAllowedOrigins());
        configuration.setAllowedMethods(corsProperties.getAllowedMethods());
        configuration.setAllowedHeaders(corsProperties.getAllowedHeaders());
        configuration.setExposedHeaders(corsProperties.getExposedHeaders());
        configuration.setAllowCredentials(corsProperties.getAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsProperties.getPathPattern(), configuration);
        return source;
    }

}
