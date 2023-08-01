package dev.ken.backend.controllers;

import dev.ken.backend.dto.MovieDTO;
import dev.ken.backend.entity.UserMovie;
import dev.ken.backend.security.JwtUtil;
import dev.ken.backend.services.implementation.UserDetailsServiceImpl;
import dev.ken.backend.services.implementation.UserMoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/movie")
public class MovieListController {

    @Autowired
    private UserMoviesService userMoviesService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

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

    @GetMapping("getUserMovieDetails/{tmdbID}")
    public ResponseEntity<?> getUserMovie(@PathVariable Integer tmdbID) throws Exception {
        String username = userDetailsService.getUsernameFromSecurityContext();

        try {
            UserMovie userMovie = userMoviesService.getUserMovie(username, tmdbID);
            if (userMovie == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie is not in user's list");
            }
            // Create a map to represent the response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("watchStatus", userMovie.getWatchStatus());
            responseData.put("rating", userMovie.getRating());
            return ResponseEntity.ok(responseData);
        } catch (Exception e) {
            System.out.println("Error fetching user movie: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("updateWatchStatus/{tmdbID}")
    public ResponseEntity<?> updateUserMovieWatchStatus(@PathVariable Integer tmdbID, @RequestBody String watchStatus) {
        String username = userDetailsService.getUsernameFromSecurityContext();

        try {
            userMoviesService.updateUserMovieWatchStatus(username, tmdbID, watchStatus);
            return ResponseEntity.ok().body("Movie's watch status updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating movie's watch status: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("updateRating/{tmdbID}")
    public ResponseEntity<?> updateUserMovieRating(@PathVariable Integer tmdbID, @RequestBody Integer rating) {
        String username = userDetailsService.getUsernameFromSecurityContext();

        try {
            userMoviesService.updateUserMovieRating(username, tmdbID, rating);
            return ResponseEntity.ok().body("Movie's rating updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating movie's rating: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/getUserWatchedMovies")
    public ResponseEntity<?> getUserWatchedMovies(){
        String username = userDetailsService.getUsernameFromSecurityContext();

        try {
            return ResponseEntity.ok().body(userMoviesService.getUserMovies(username, "watched"));

        } catch (Exception e) {
            System.out.println("Error getting user's watched list: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getUserWatchlistMovies")
    public ResponseEntity<?> getUserWatchlistMovies(){
        String username = userDetailsService.getUsernameFromSecurityContext();

        try {

            return ResponseEntity.ok().body(userMoviesService.getUserMovies(username, "watchlist"));

        } catch (Exception e) {
            System.out.println("Error getting user's watchlist: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}