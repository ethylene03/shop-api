package com.princess.shopapi.config

import com.princess.shopapi.dto.Role
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(private val jwt: JWTConfig) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun securityFilterChain(http: HttpSecurity): DefaultSecurityFilterChain {
        log.debug("Adding jwt filter..")
        http.addFilterBefore(jwt, UsernamePasswordAuthenticationFilter::class.java)

        log.debug("Filter paths with no authentication")
        http.csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers(
                    "/auth/**",
                    "/"
                ).permitAll()
                auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                auth.requestMatchers(HttpMethod.POST, "/products").hasRole("SELLER")
                auth.requestMatchers(HttpMethod.PUT, "/products/**").hasRole("SELLER")
                auth.requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("SELLER")
                auth.requestMatchers(HttpMethod.PUT, "/orders/*/status").hasRole("SELLER")

                auth.requestMatchers("/carts/*/items/**").hasRole("BUYER")

                auth.anyRequest().fullyAuthenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        log.debug("Security filter chain configured.")
        return http.build()
    }
}