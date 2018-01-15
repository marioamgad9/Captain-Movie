package com.mouris.mario.captainmovie;


import android.content.AsyncTaskLoader;
import android.content.Context;

public class MovieLoader extends AsyncTaskLoader<Movie>{

    private int movieId;

    public MovieLoader(Context context, int movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Movie loadInBackground() {
        return QueryUtils.fetchMovie(movieId);
    }
}
