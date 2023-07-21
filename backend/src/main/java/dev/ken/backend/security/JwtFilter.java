package dev.ken.backend.security;

import dev.ken.backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final List<String> excludedPaths = Arrays.asList("/api/auth/login", "/api/auth/register", "/api/auth/verify");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String requestPath = request.getRequestURI();
        return excludedPaths.contains(requestPath);
    }
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {
        try {
            // Get cookie header and validate
            String accessToken = "";
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("jwt_auth")) {
                        accessToken = cookie.getValue();
                        if (accessToken == null) return;
                    }
                }
            }

            // Get user identity
            UserDetails userDetails = userRepository
                    .findByUsername(jwtUtil.getUsernameFromToken(accessToken))
                    .orElse(null);

            // Validate token
            if (userDetails != null && !jwtUtil.validateToken(accessToken, userDetails)) {
                chain.doFilter(request, response); // passes on to next filter in chain
                return;
            }

            // Set and store authentication details in security context to be accessible throughout application
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, null);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.toString());
        }
    }
}
