package com.permission.api.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration {
    @Autowired
    lateinit var filter: AuthenticationFilter

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.let {
            it.cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .requestMatchers("/").permitAll()
                // .anyRequest().hasAuthority("action:resource")
                .and()
                .addFilterBefore(filter, BasicAuthenticationFilter::class.java)
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }

        return http.build()
    }
}