package com.mouris.mario.captainmovie.Data;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.mouris.mario.captainmovie.Utils.QueryUtils;

public class RemoteDataSource {

    private static RemoteDataSource sInstance;

    private MutableLiveData<Movie> mMovieLiveData;
    private MutableLiveData<Boolean> mIsLoading;

    private RemoteDataSource() {
        mMovieLiveData = new MutableLiveData<>();
        mIsLoading = new MutableLiveData<>();
        mIsLoading.setValue(false);
    }

    public static RemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new RemoteDataSource();
        }

        return sInstance;
    }

    public void removeCurrentMovie() {
        mMovieLiveData.setValue(null);
    }

    public LiveData<Movie> getRandomMovie() {
        return mMovieLiveData;
    }

    public LiveData<Boolean> isLoading() {
        return mIsLoading;
    }

    @SuppressLint("StaticFieldLeak")
    public void loadRandomMovie() {
        mIsLoading.setValue(true);
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
                mIsLoading.setValue(false);
                mMovieLiveData.setValue(movie);
            }
        }.execute();
    }

//    public LiveData<Movie> getMovieById(int movieId) {
//        loadMovieById(movieId);
//        return mMovieLiveData;
//    }
//
//    @SuppressLint("StaticFieldLeak")
//    private void loadMovieById(final int movieId) {
//        new AsyncTask<Void, Void, Movie>() {
//            @Override
//            protected Movie doInBackground(Void... voids) {
//                return QueryUtils.fetchMovie(movieId);
//            }
//
//            @Override
//            protected void onPostExecute(Movie movie) {
//                mMovieLiveData.setValue(movie);
//            }
//        }.execute();
//    }
}
