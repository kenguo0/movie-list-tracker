package dev.ken.backend.services.implementation;

import dev.ken.backend.entity.User;
import dev.ken.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);

        return userOpt.orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
    }

    public String getUsernameFromSecurityContext() {
        String username = "";
        try {
            // Get the current authentication object from the SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                // Retrieve the principal
                Object principal = authentication.getPrincipal();

                if (principal instanceof UserDetails userDetails) {
                    // Set username
                    username = userDetails.getUsername();

                }
            }
        } catch (Exception e) {
            System.out.println("Error when getting username from security context: " + e);
        }

        return username;
    }

    public Integer getUserIdFromUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return user.getId();
    }
}
