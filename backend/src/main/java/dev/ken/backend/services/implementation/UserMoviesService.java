package dev.ken.backend.services.implementation;

import dev.ken.backend.dto.MovieDTO;
import dev.ken.backend.entity.Movie;
import dev.ken.backend.entity.User;
import dev.ken.backend.entity.UserMovie;
import dev.ken.backend.repository.MovieRepository;
import dev.ken.backend.repository.UserMoviesRepository;
import dev.ken.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserMoviesService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserMoviesRepository userMoviesRepository;

    public void addUserMovie(String username, MovieDTO movieDTO) throws Exception {
        Optional<Movie> existingMovie = movieRepository.findByTmdbID(movieDTO.getTmdbID());

        // If the movie does not exist, add it to the Movie table
        if (existingMovie.isEmpty()) {
            existingMovie = Optional.ofNullable(movieService.addMovie(movieDTO));
        }

        // Associate the user with the movie and add record to the UserMovie table
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            UserMovie userMovie = userMoviesRepository.findByUserIdAndMovieId(user.getId(), existingMovie.get().getId()).orElse(null);

            // Only add movie to user's list if it does not exist already
            if (userMovie == null) {
                userMovie = new UserMovie();
                userMovie.setUser(user);
                userMovie.setMovie(existingMovie.get());
                userMovie.setWatchStatus(movieDTO.getWatchStatus());
                if (movieDTO.getWatchStatus().equals("watched")) userMovie.setRating(movieDTO.getRating());

                // Save the new user_movie to the UserMovie table
                userMoviesRepository.save(userMovie);
            } else {
                throw new Exception("Movie already exists in user's list");
            }

        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    public void removeUserMovie(String username, Integer tmdbID) throws Exception {
        // Check if user and movie exists
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Movie movie = movieRepository.findByTmdbID(tmdbID).orElseThrow(() -> new Exception("Movie not found with ID: " + tmdbID));
        // Check if movie exists in user list
        UserMovie userMovie = userMoviesRepository.findByUserIdAndMovieId(user.getId(), movie.getId())
                .orElseThrow(() -> new Exception("Movie not found in user's list."));

        userMoviesRepository.delete(userMovie);
    }

    public UserMovie getUserMovie(String username, Integer tmdbID) throws Exception {
        // Check if user and movie exists
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Movie movie = movieRepository.findByTmdbID(tmdbID).orElseThrow(() -> new Exception("Movie not found with ID: " + tmdbID));
        // Check if movie exists in user list
        UserMovie userMovie = userMoviesRepository.findByUserIdAndMovieId(user.getId(), movie.getId()).orElse(null);

        return userMovie;
    }

    public List<MovieDTO> getUserMovies(String username, String listType) {
        Integer userID = userDetailsService.getUserIdFromUsername(username);
        List<UserMovie> userMovies;

        if (listType.equals("watched")) {
            userMovies = userMoviesRepository.getWatchedMovies(userID);
        } else if (listType.equals("watchlist")) {
            userMovies = userMoviesRepository.getWatchlist(userID);
        } else {
            throw new IllegalArgumentException("Invalid list type: " + listType);
        }
        List<MovieDTO> watchedMovies = new ArrayList<>();

        for(UserMovie um : userMovies) {
            Movie movie = um.getMovie();
            MovieDTO movieInfo = new MovieDTO();
            movieInfo.setTmdbID(movie.getTmdbID());
            movieInfo.setTitle(movie.getTitle());
            movieInfo.setSummary(movie.getSummary());
            movieInfo.setPosterPath(movie.getPosterPath());
            movieInfo.setBackdropPath(movie.getBackdropPath());
            movieInfo.setReleaseYear(movie.getReleaseYear());
            movieInfo.setGenres(movie.getGenres());
            movieInfo.setWatchStatus(um.getWatchStatus());
            if (um.getRating() != null) movieInfo.setRating(um.getRating());
            watchedMovies.add(movieInfo);
        }

        return watchedMovies;
    }

    public void updateUserMovieWatchStatus(String username, Integer tmdbID, String watchStatus) throws Exception {
        // Check if user and movie exists
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Movie movie = movieRepository.findByTmdbID(tmdbID).orElseThrow(() -> new Exception("Movie not found with ID: " + tmdbID + " in user's list"));
        userMoviesRepository.updateWatchStatus(user.getId(), movie.getId(), watchStatus);
    }

    public void updateUserMovieRating(String username, Integer tmdbID, Integer rating) throws Exception {
        // Check if user and movie exists
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        Movie movie = movieRepository.findByTmdbID(tmdbID).orElseThrow(() -> new Exception("Movie not found with ID: " + tmdbID + " in user's list"));
        userMoviesRepository.updateRating(user.getId(), movie.getId(), rating);
    }
}
