package com.mouris.mario.captainmovie.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mouris.mario.captainmovie.R;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMovie(12).observe(this, movie -> {
            if (movie != null) {
                Log.i(LOG_TAG, movie.title);
                Log.i(LOG_TAG, movie.poster_path);
                Log.i(LOG_TAG, movie.homepage);
            }
        });

    }
}
