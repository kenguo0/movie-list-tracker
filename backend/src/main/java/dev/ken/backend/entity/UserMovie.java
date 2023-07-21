package dev.ken.backend.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "user_movies")
public class UserMovie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private Boolean watched;

    @Column(nullable = true)
    private Long rating;

    private Boolean watchlist;

    public void setRating(Long rating) {
        if (rating > 1) {
            this.rating = rating;
        } else {
            this.rating = null;
        }

    }
}
