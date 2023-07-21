package dev.ken.backend.services.implementation;

import dev.ken.backend.dto.MovieDTO;
import dev.ken.backend.entity.Genre;
import dev.ken.backend.entity.Movie;
import dev.ken.backend.repository.GenreRepository;
import dev.ken.backend.repository.MovieRepository;
import dev.ken.backend.repository.UserMoviesRepository;
import dev.ken.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMoviesRepository userMoviesRepository;

    @Autowired
    private GenreRepository genreRepository;

    public Movie addMovie(MovieDTO movieDTO) throws Exception {

        Movie newMovie = new Movie();
        // Set movie details based on movieDTO
        newMovie.setTitle(movieDTO.getTitle());
        newMovie.setPosterPath(movieDTO.getPosterPath());
        newMovie.setSummary(movieDTO.getSummary());
        newMovie.setReleaseYear(movieDTO.getReleaseYear());
        newMovie.setTmdbID(movieDTO.getTmdbID());

        // Map list of genres to set of Genre objects then add to newMovie
        Set<Genre> genres = movieDTO.getGenres().stream()
                .map(genreName -> genreRepository.findByName(genreName)
                        .orElseGet(() -> new Genre(genreName)))
                .collect(Collectors.toSet());
        newMovie.setGenres(genres);

        // Save the new movie to the Movie table
        movieRepository.save(newMovie);

        return newMovie;
    }
}
