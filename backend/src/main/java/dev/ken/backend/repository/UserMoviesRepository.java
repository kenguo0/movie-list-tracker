package dev.ken.backend.repository;

import dev.ken.backend.entity.UserMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserMoviesRepository extends JpaRepository<UserMovie, Integer> {
    Optional<UserMovie> findByUserIdAndMovieId(Integer userID, Integer movieID);

    @Query(value = "SELECT * FROM user_movies WHERE user_id = :userID", nativeQuery = true)
    List<String> getUserMovies(@Param("userID") Integer userID);
}
