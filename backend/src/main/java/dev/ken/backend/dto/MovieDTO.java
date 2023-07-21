package dev.ken.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class MovieDTO {

    private Integer tmdbID;
    private String title;
    private String summary;
    private String posterPath;
    private int releaseYear;
    private List<String> genres;
    private boolean watched;
    private long rating;
    private boolean watchlist;
}
