package com.mouris.mario.captainmovie;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    public String title;
    public double vote_average;
    public String homepage;
    public String overview;
    public String release_date;
    public String poster_path;
    public List<String > genres;

    public Movie() {
        genres = new ArrayList<>();
    }

    public Movie(String title, String poster_path, double vote_average,
                 String homepage, String overview,
                 String release_date, List<String> genres) {
        this.title = title;
        this.poster_path = poster_path;
        this.vote_average = vote_average;
        this.homepage = homepage;
        this.overview = overview;
        this.release_date = release_date;
        this.genres = genres;
    }
}
