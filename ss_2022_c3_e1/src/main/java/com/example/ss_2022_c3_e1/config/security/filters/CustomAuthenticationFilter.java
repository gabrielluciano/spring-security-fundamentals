package com.example.ss_2022_c3_e1.config.security.filters;

import com.example.ss_2022_c3_e1.config.security.authentication.CustomAuthentication;
import com.example.ss_2022_c3_e1.config.security.managers.CustomAuthenticationManager;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final CustomAuthenticationManager customAuthenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Create an authentication filter which is not yet authenticated
        // 2. Delegate the authentication object to the manager
        // 3. Get back the authentication from the manager
        // 4. If the object is authenticated then send request to the next filter in the chain

        String key = String.valueOf(request.getHeader("key"));
        CustomAuthentication ca = new CustomAuthentication(false, key);

        Authentication auth = customAuthenticationManager.authenticate(ca);

        if(auth.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response); // only when authentication worked
        }
    }
}
