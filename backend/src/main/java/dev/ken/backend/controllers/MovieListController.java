package dev.ken.backend.controllers;

import dev.ken.backend.dto.MovieDTO;
import dev.ken.backend.repository.MovieRepository;
import dev.ken.backend.security.JwtUtil;
import dev.ken.backend.services.implementation.MovieService;
import dev.ken.backend.services.implementation.UserDetailsServiceImpl;
import dev.ken.backend.services.implementation.UserMoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api/movie")
public class MovieListController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserMoviesService userMoviesService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${tmdb.key}")
    private String apiKey;

    @GetMapping("/key")
    public ResponseEntity<?> getTmdbKey(@CookieValue("jwt_auth") String token) {
        System.out.println("Sending tmdb api key");
        return ResponseEntity.ok()
                .header("isAuthenticatedHeader", "true")
                .body(apiKey);
    }

    @PostMapping("/addToList")
    public ResponseEntity<?> addMovieToList(@RequestBody MovieDTO movieDto) {
        // Get username from security context
        String username = userDetailsService.getUsernameFromSecurityContext();

        try {
            userMoviesService.addUserMovie(username, movieDto);
            return ResponseEntity.ok().body("Movie added successfully");
        } catch (Exception e) {
            System.out.println("Error when adding movie: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @DeleteMapping("/removeFromList/{tmdbID}")
    public ResponseEntity<?> removeMovieFromList(@PathVariable Integer tmdbID) {
        // Get username from security context
        String username = userDetailsService.getUsernameFromSecurityContext();

        try {
            userMoviesService.removeUserMovie(username, tmdbID);
            return ResponseEntity.ok().body("Movie removed successfully");
        } catch (Exception e) {
            System.out.println("Error when removing movie: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllUserMovies")
    public ResponseEntity<?> getAllUserMovies(){
        String username = userDetailsService.getUsernameFromSecurityContext();
        try {
            Integer userID = userDetailsService.getUserIdFromUsername(username);
            userMoviesService.getUserMovies(userID);
            return ResponseEntity.ok().body("Movie removed successfully");
        } catch (Exception e) {
            System.out.println("Error getting user movies: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}