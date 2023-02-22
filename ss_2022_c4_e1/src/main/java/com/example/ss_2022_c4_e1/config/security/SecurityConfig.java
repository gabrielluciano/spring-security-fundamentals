package com.example.ss_2022_c4_e1.config.security;

import com.example.ss_2022_c4_e1.config.security.filters.ApiKeyFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig /* extends WebSecurityConfigurerAdapter */ {

//    Old way to get access do the Authentication Manager. This was used when extends
//    the WebSecurityConfigurerAdapter was possible

//    @Override
//    @Bean
//    protected AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManager();
//    }

    @Value("${private.api.key}")
    private String key;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.httpBasic()
                // .and().authenticationManager()   or  by adding a @Bean of type AuthenticationManager
                // .and().authenticationProvider()  it doesn't override the AuthenticationProvider, it adds one more to the collection
                .and()
                .addFilterBefore(new ApiKeyFilter(key), BasicAuthenticationFilter.class)
                .authorizeRequests().anyRequest().authenticated()
                .and().build();
    }

}
