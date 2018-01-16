package com.mouris.mario.captainmovie.Data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.mouris.mario.captainmovie.Utils.QueryUtils;

public class RemoteDataSource {

    private static RemoteDataSource sInstance;

    private MutableLiveData<Movie> mMovieLiveData;

    private RemoteDataSource() {
        mMovieLiveData = new MutableLiveData<>();
    }

    public static RemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new RemoteDataSource();
        }

        return sInstance;
    }

    public LiveData<Movie> getMovieById(int movieId) {
        loadMovieById(movieId);
        return mMovieLiveData;
    }

    public LiveData<Movie> getRandomMovie() {
        loadRandomMovie();
        return mMovieLiveData;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadMovieById(final int movieId) {
        new AsyncTask<Void, Void, Movie>() {
            @Override
            protected Movie doInBackground(Void... voids) {
                return QueryUtils.fetchMovie(movieId);
            }

            @Override
            protected void onPostExecute(Movie movie) {
                mMovieLiveData.setValue(movie);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadRandomMovie() {
        new AsyncTask<Void, Void, Movie>() {
            @Override
            protected Movie doInBackground(Void... voids) {
                Movie randomMovie;
                do {
                    randomMovie = QueryUtils.fetchRandomMovie();
                } while (randomMovie == null
                        || randomMovie.adult
                        || !randomMovie.original_language.equals("en"));

                return randomMovie;
            }

            @Override
            protected void onPostExecute(Movie movie) {
                mMovieLiveData.setValue(movie);
            }
        }.execute();
    }
}
