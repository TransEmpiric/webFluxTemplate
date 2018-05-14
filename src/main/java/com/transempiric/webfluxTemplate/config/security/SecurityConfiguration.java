package com.transempiric.webfluxTemplate.config.security;

import com.transempiric.webfluxTemplate.service.CustomReactiveUserDetailsService;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.util.Assert;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
    private static final String[] AUTH_WHITELIST = {
            "/resources/**",
            "/webjars/**",
            "/auth/**",
            "/test/ws",
            "/favicon.ico"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http,
                                                            final JwtAuthenticationWebFilter authenticationWebFilter,
                                                            final UnauthorizedAuthenticationEntryPoint entryPoint) {
        // We must override AuthenticationEntryPoint because if AuthenticationWebFilter didn't kicked in
        // (i.e. there are no required headers) then default behavior is to display HttpBasicAuth,
        // so we just return unauthorized to override it.
        // Filter tries to authenticate each request if it contains required headers.
        // Finally, we disable all default security.
        http
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint)
                .and()
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .authorizeExchange()
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .anyExchange().authenticated()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable();
        return http.build();
    }

    @Bean
    public WebSessionServerSecurityContextRepository securityContextRepository() {
        return new WebSessionServerSecurityContextRepository();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new CustomPasswordEncoder();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil("test", 100000L);
    }
}