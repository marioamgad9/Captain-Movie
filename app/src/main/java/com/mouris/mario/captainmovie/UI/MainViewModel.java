package com.mouris.mario.captainmovie.UI;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.mouris.mario.captainmovie.Data.Movie;
import com.mouris.mario.captainmovie.Data.RemoteDataSource;

public class MainViewModel extends ViewModel {

    private RemoteDataSource mRemoteDataSource;

    public MainViewModel() {
        mRemoteDataSource = RemoteDataSource.getInstance();
    }

    LiveData<Movie> getMovie(int movieId) {
        return mRemoteDataSource.getMovieById(movieId);
    }

}
