package com.mouris.mario.captainmovie.Data;

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
}
