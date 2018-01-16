package com.mouris.mario.captainmovie.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mouris.mario.captainmovie.Data.Movie;
import com.mouris.mario.captainmovie.R;
import com.mouris.mario.captainmovie.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
//    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int ANIMATION_DURATION = 600;
    private static final int SHORT_ANIMATION_DURATION = 300;

    private Button mAskCaptainButton;
    private ImageView mCaptainImageView;
    private ImageView mSkyImageView;
    private LinearLayout mMovieLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //To update the mode we're in
        viewModel.getRandomMovie().observe(this, movie -> {
            if (movie != null) {
                //Go to movie mode
                bindDataToMovieLayout(movie);
                goToMovieMode();
            } else {
                //Go to captain mode
                goToCaptainMode();
            }
        });

        //To update the loading state
        ProgressBar progressBar = findViewById(R.id.progressBar);
        viewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        mAskCaptainButton = findViewById(R.id.askCaptainButton);
        mCaptainImageView = findViewById(R.id.captain);
        mMovieLayout = findViewById(R.id.movieLayout);
        mSkyImageView = findViewById(R.id.sky);
        FloatingActionButton cancelFab = findViewById(R.id.cancelFAB);

        cancelFab.setOnClickListener(v-> viewModel.removeCurrentMovie());
        mAskCaptainButton.setOnClickListener(v-> {
            if (NetworkUtils.isNetworkConnected(this)) {
                viewModel.loadRandomMovie();
            } else {
                Snackbar.make(mCaptainImageView,
                        R.string.no_internet_message, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void bindDataToMovieLayout(Movie movie) {
        TextView titleTV = findViewById(R.id.titleTextView);
        TextView genresTV = findViewById(R.id.generesTextView);
        TextView overviewTV = findViewById(R.id.overviewTextView);
        TextView rateTV = findViewById(R.id.rateTextView);
        ImageView movieImageView = findViewById(R.id.posterImageView);

        titleTV.setText(movie.title);
        overviewTV.setText(movie.overview);

        String rateString = String.format(getResources().getString(R.string.movie_vote_average),
                String.valueOf((int) movie.vote_average));
        rateTV.setText(rateString);

        String genresString = String.format(getResources().getString(R.string.movie_genres),
                movie.genres);
        genresTV.setText(genresString);

        Picasso.with(this).load(movie.poster_path)
                .placeholder(R.drawable.poster_placeholder)
                .into(movieImageView);
    }

    private void goToCaptainMode() {
        //Show the captain
        mCaptainImageView.animate().translationY(0).setDuration(ANIMATION_DURATION);
        mCaptainImageView.animate().alpha(1).setDuration(ANIMATION_DURATION);
        mAskCaptainButton.animate().translationY(0).setDuration(ANIMATION_DURATION);
        //Show the askCaptainButton
        mAskCaptainButton.animate().translationY(0).setDuration(ANIMATION_DURATION);
        //Hide the movie layout
        mMovieLayout.animate().translationY(-1550).setDuration(ANIMATION_DURATION)
                .withEndAction(() -> mMovieLayout.setVisibility(View.GONE));
        //Zoom out of the sky
        mSkyImageView.animate().scaleXBy(-0.8f).setDuration(ANIMATION_DURATION);
        mSkyImageView.animate().scaleYBy(-0.8f).setDuration(ANIMATION_DURATION);
    }

    private void goToMovieMode() {
        //Hide the captain
        mCaptainImageView.animate().translationY(1250).setDuration(ANIMATION_DURATION);
        mCaptainImageView.animate().alpha(0).setDuration(ANIMATION_DURATION);
        //Hide the askCaptainButton
        mAskCaptainButton.animate().translationY(200).setDuration(SHORT_ANIMATION_DURATION);
        //Show the movie layout
        mMovieLayout.setTranslationY(-1550);
        mMovieLayout.setVisibility(View.VISIBLE);
        mMovieLayout.animate().translationY(0).setDuration(ANIMATION_DURATION);
        //Zoom into the sky
        mSkyImageView.animate().scaleXBy(0.8f).setDuration(ANIMATION_DURATION);
        mSkyImageView.animate().scaleYBy(0.8f).setDuration(ANIMATION_DURATION);
    }
}
