package dev.ken.backend.services.implementation;

import dev.ken.backend.dto.MovieDTO;
import dev.ken.backend.entity.Movie;
import dev.ken.backend.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie addMovie(MovieDTO movieDTO) {

        Movie newMovie = new Movie();
        // Set movie details based on movieDTO
        newMovie.setTitle(movieDTO.getTitle());
        newMovie.setPosterPath(movieDTO.getPosterPath());
        newMovie.setBackdropPath(movieDTO.getBackdropPath());
        newMovie.setSummary(movieDTO.getSummary());
        newMovie.setReleaseYear(movieDTO.getReleaseYear());
        newMovie.setTmdbID(movieDTO.getTmdbID());
        newMovie.setGenres(movieDTO.getGenres());

        // Save the new movie to the Movie table
        movieRepository.save(newMovie);

        return newMovie;
    }
}
