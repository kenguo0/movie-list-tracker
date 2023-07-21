package dev.ken.backend.services.implementation;

import dev.ken.backend.dto.MovieDTO;
import dev.ken.backend.entity.Movie;
import dev.ken.backend.entity.User;
import dev.ken.backend.entity.UserMovie;
import dev.ken.backend.repository.GenreRepository;
import dev.ken.backend.repository.MovieRepository;
import dev.ken.backend.repository.UserMoviesRepository;
import dev.ken.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserMoviesService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieService movieService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMoviesRepository userMoviesRepository;

    @Autowired
    private GenreRepository genreRepository;

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
                userMovie.setWatched(movieDTO.isWatched());
                userMovie.setRating(movieDTO.getRating());
                userMovie.setWatchlist(movieDTO.isWatchlist());

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

    public List<String> getUserMovies(Integer userID) {
       List<String> userMovies =  userMoviesRepository.getUserMovies(userID);
       for(String movie: userMovies) {
           System.out.println(movie);
       }
       return null;
    }

//    public UserMovie saveUserMovie(UserMovie userMovie) {
//        return userMoviesRepository.save(userMovie);
//    }
//
//    public void removeUserMovie(Integer userMovieId) {
//        userMoviesRepository.deleteById(userMovieId);
//    }
}
