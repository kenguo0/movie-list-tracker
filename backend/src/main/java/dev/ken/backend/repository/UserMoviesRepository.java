package dev.ken.backend.repository;

import dev.ken.backend.entity.UserMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserMoviesRepository extends JpaRepository<UserMovie, Integer> {
    Optional<UserMovie> findByUserIdAndMovieId(Integer userID, Integer movieID);

    @Query(value = "SELECT * FROM user_movies WHERE user_id = :userID AND watch_status = 'watched' ", nativeQuery = true)
    List<UserMovie> getWatchedMovies(@Param("userID") Integer userID);

    @Query(value = "SELECT * FROM user_movies WHERE user_id = :userID AND watch_status = 'watchlist' ", nativeQuery = true)
    List<UserMovie> getWatchlist(@Param("userID") Integer userID);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_movies SET watch_status = :watchStatus WHERE user_id = :userID AND movie_id = :movieID", nativeQuery = true)
    void updateWatchStatus(@Param("userID") Integer userID, @Param("movieID") Integer movieID, @Param("watchStatus") String watchStatus);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_movies SET rating = :rating WHERE user_id = :userID AND movie_id = :movieID", nativeQuery = true)
    void updateRating(@Param("userID") Integer userID, @Param("movieID") Integer movieID, @Param("rating") Integer rating);
}
