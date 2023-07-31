package dev.ken.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MovieDTO {

    private Integer tmdbID;
    private String title;
    private String summary;
    private String posterPath;
    private String backdropPath;
    private int releaseYear;
    private List<String> genres;
    private String watchStatus;
    private Integer rating;
}
