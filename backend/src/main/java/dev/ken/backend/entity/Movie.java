package dev.ken.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String posterPath;
    private String backdropPath;
    @Column(columnDefinition="MEDIUMTEXT")
    private String summary;
    private Integer releaseYear;
    private Integer tmdbID;
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private List<String> genres;
}
