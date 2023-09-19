package com.maveric.citi.config;

//import com.currencyexchange.filter.JwtAuthenticationFilter;
//import com.currencyexchange.service.JwtUserService;
import com.maveric.citi.filter.JwtAuthenticationFilter;
import com.maveric.citi.service.JwtUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private JwtUserService userService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        logger.info("This is security filter chaing");
        http.csrf(AbstractHttpConfigurer::disable).
                cors(cors -> cors.configurationSource(corsOrginConfig()))


                .authorizeHttpRequests(request -> request.requestMatchers("/authenticate/login","/v3/api-docs/**",
                                "/swagger-ui/**").permitAll()

                        .requestMatchers("/account/fetchAll/**").hasAnyAuthority("SUPERUSER","CUSTOMER")

                        .requestMatchers("/customer/**","/account/**").hasAuthority("SUPERUSER")

                        .requestMatchers("/orders/**").hasAuthority("CUSTOMER")



//                        .requestMatchers("/authenticate/signin").hasAuthority("CUSTOMER")
//                        .requestMatchers("/admin/**").hasAuthority("SUPERUSER")






//
                         .anyRequest().authenticated())

                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        logger.info("Security filter chain endss!!");
        return http.build();



    }

    @Bean
    public CorsConfigurationSource corsOrginConfig() {
        System.out.println("allowing cros");
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://172.16.238.163:4200","/authenticate/login"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }



}
