package com.example.CollectiveProject.Config;

import com.example.CollectiveProject.Domain.User;
import com.example.CollectiveProject.Service.UserService;
import com.example.CollectiveProject.Utilities.JwtUtilities;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtilities jwtUtilities;
    private final UserService userService;
    public JwtAuthorizationFilter(JwtUtilities jwtUtil, UserService userService) {
        this.jwtUtilities = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();

        try {
            String accessToken = jwtUtilities.resolveToken(request);
            if (accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }
            Claims claims = jwtUtilities.resolveClaims(request);

            if (claims != null & jwtUtilities.validateClaims(claims)) {
                String email = claims.getSubject();
                User user = userService.getUserByEmail(email);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email, user, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details", e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        }

        filterChain.doFilter(request, response);
    }
}