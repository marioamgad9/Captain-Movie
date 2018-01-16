package com.mouris.mario.captainmovie.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
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

    private ImageView mSkyImageView;
    private ConstraintLayout mCaptainLayout;
    private LinearLayout mMovieLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
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

        mCaptainLayout = findViewById(R.id.captainLayout);
        mMovieLayout = findViewById(R.id.movieLayout);
        mSkyImageView = findViewById(R.id.sky);
        FloatingActionButton cancelFab = findViewById(R.id.cancelFAB);
        Button askCaptainButton = findViewById(R.id.askCaptainButton);

        cancelFab.setOnClickListener(v-> {
            viewModel.removeCurrentMovie();
            askCaptainButton.setEnabled(true);
        });
        askCaptainButton.setOnClickListener(v-> {
            if (NetworkUtils.isNetworkConnected(this)) {
                askCaptainButton.setEnabled(false);
                viewModel.loadRandomMovie();
            } else {
                Snackbar.make(mSkyImageView,
                        R.string.no_internet_message, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void bindDataToMovieLayout(Movie movie) {
        TextView titleTV = findViewById(R.id.titleTextView);
        TextView genresTV = findViewById(R.id.generesTextView);
        TextView overviewTV = findViewById(R.id.overviewTextView);
        TextView rateTV = findViewById(R.id.rateTextView);
        ImageView posterImageView = findViewById(R.id.posterImageView);
        Button visitHomePageButton = findViewById(R.id.visitHomePageButton);

        new Handler().postDelayed(()-> {
            titleTV.setText(movie.title);
            overviewTV.setText(movie.overview);

            String rateString = String.format(getResources().getString(R.string.movie_vote_average),
                    String.valueOf((int) movie.vote_average));
            rateTV.setText(rateString);

            String genresString = String.format(getResources().getString(R.string.movie_genres),
                    movie.genres);
            genresTV.setText(genresString);

            if (movie.homepage.isEmpty()) {
                visitHomePageButton.setVisibility(View.GONE);
            } else {
                visitHomePageButton.setVisibility(View.VISIBLE);
                visitHomePageButton.setOnClickListener(v->{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.homepage));
                    startActivity(intent);
                });
            }
        }, 100);

        posterImageView.setImageResource(R.drawable.poster_placeholder);
        new Handler().postDelayed(()-> Picasso.with(this).load(movie.poster_path)
                .placeholder(R.drawable.poster_placeholder)
                .into(posterImageView), 700);
    }

    private void goToCaptainMode() {
        //Show the captain layout
        mCaptainLayout.animate().withLayer().translationY(0).setDuration(ANIMATION_DURATION);
        //Hide the movie layout
        mMovieLayout.animate().withLayer().translationY(-1550).setDuration(ANIMATION_DURATION);
        //Zoom out of the sky
        mSkyImageView.animate().withLayer().scaleXBy(-0.8f)
                .scaleYBy(-0.8f).setDuration(ANIMATION_DURATION);
    }

    private void goToMovieMode() {
        //Hide the captain layout
        mCaptainLayout.animate().withLayer().translationY(1250).setDuration(ANIMATION_DURATION);
        //Show the movie layout
        mMovieLayout.setTranslationY(-1550);
        mMovieLayout.setVisibility(View.VISIBLE);
        mMovieLayout.animate().withLayer().translationY(0).setDuration(ANIMATION_DURATION);
        //Zoom into the sky
        mSkyImageView.animate().withLayer().scaleXBy(0.8f)
                .scaleYBy(0.8f).setDuration(ANIMATION_DURATION);
    }
}
