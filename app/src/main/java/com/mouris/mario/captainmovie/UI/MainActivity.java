package com.mouris.mario.captainmovie.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        ImageView thinkingImageView = findViewById(R.id.thinkingImageView);
        viewModel.isLoading().observe(this, isLoading -> {
            if (isLoading) {
                animateBlinking(thinkingImageView);
                thinkingImageView.setVisibility(View.VISIBLE);
            } else {
                thinkingImageView.setVisibility(View.GONE);
                stopBlinking(thinkingImageView);
            }
        });

        mCaptainLayout = findViewById(R.id.captainLayout);
        mMovieLayout = findViewById(R.id.movieLayout);
        mSkyImageView = findViewById(R.id.sky);
        FloatingActionButton cancelFab = findViewById(R.id.cancelFAB);
        Button mAskCaptainButton = findViewById(R.id.askCaptainButton);

        cancelFab.setOnClickListener(v-> viewModel.removeCurrentMovie());
        mAskCaptainButton.setOnClickListener(v-> {
            if (NetworkUtils.isNetworkConnected(this)) {
                viewModel.loadRandomMovie();
            } else {
                Snackbar.make(mSkyImageView,
                        R.string.no_internet_message, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void animateBlinking(ImageView imageView) {
        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        imageView.startAnimation(animation);
    }

    private void stopBlinking(ImageView imageView) {
        imageView.clearAnimation();
    }

    private void bindDataToMovieLayout(Movie movie) {
        TextView titleTV = findViewById(R.id.titleTextView);
        TextView genresTV = findViewById(R.id.generesTextView);
        TextView overviewTV = findViewById(R.id.overviewTextView);
        TextView rateTV = findViewById(R.id.rateTextView);
        ImageView posterImageView = findViewById(R.id.posterImageView);

        new Handler().postDelayed(()-> {
            titleTV.setText(movie.title);
            overviewTV.setText(movie.overview);

            String rateString = String.format(getResources().getString(R.string.movie_vote_average),
                    String.valueOf((int) movie.vote_average));
            rateTV.setText(rateString);

            String genresString = String.format(getResources().getString(R.string.movie_genres),
                    movie.genres);
            genresTV.setText(genresString);
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
