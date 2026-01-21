package model;

import enums.MovieStatus;

/**
 * Represents a movie
 */
public class Movie {
    private String movieId;
    private String title;
    private String genre;
    private int durationMinutes;
    private String language;
    private String rating;
    private MovieStatus status;

    public Movie(String movieId, String title, String genre, int durationMinutes, 
                 String language, String rating) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.language = language;
        this.rating = rating;
        this.status = MovieStatus.NOW_SHOWING;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getLanguage() {
        return language;
    }

    public String getRating() {
        return rating;
    }

    public MovieStatus getStatus() {
        return status;
    }

    public void setStatus(MovieStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId='" + movieId + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + durationMinutes + " mins" +
                ", language='" + language + '\'' +
                ", rating='" + rating + '\'' +
                ", status=" + status +
                '}';
    }
}


