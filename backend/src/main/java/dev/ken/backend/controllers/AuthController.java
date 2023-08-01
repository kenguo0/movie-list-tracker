package dev.ken.backend.controllers;

import dev.ken.backend.dto.RegistrationDTO;
import dev.ken.backend.dto.UserDTO;
import dev.ken.backend.entity.User;
import dev.ken.backend.repository.UserRepository;
import dev.ken.backend.security.JwtUtil;
import dev.ken.backend.services.implementation.UserDetailsServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "https://watchverse-movielist.netlify.app")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO) {
        if (registrationDTO.getUsername().isEmpty() && registrationDTO.getPassword().isEmpty()) {
            return new ResponseEntity<>("Empty credentials", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            return new ResponseEntity<>("Username is taken", HttpStatus.BAD_REQUEST);
        }

        if(!registrationDTO.getPassword().equals(registrationDTO.getPasswordRe())) {
            return new ResponseEntity<>("Passwords entered do not match", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDto, HttpServletResponse response) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userDto.getUsername(), userDto.getPassword()
                            )
                    );

            User user = (User) authenticate.getPrincipal();
            String token = jwtUtil.generateToken(user);
            System.out.println(jwtUtil.getExpirationDateFromToken(token));

            // store JWT in cookie
            final Cookie cookie = new Cookie("jwt_auth", token);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(30 * 24 * 60 * 60);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseEntity.ok().header("Username", user.getUsername()).body("Login successful");
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@CookieValue(value="jwt_auth", required=false) String token) {
        String username = "";

        if (token != null) {
            username = jwtUtil.getUsernameFromToken(token);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails)) {
                return ResponseEntity.ok()
                        .header("isAuthenticatedHeader", "true")
                        .body("User is authenticated");

            }
        }

        return ResponseEntity.badRequest().body("User is not authenticated");
    }
}
