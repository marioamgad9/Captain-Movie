package com.mouris.mario.captainmovie.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mouris.mario.captainmovie.R;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int ANIMATION_DURATION = 600;

    private boolean inCaptainMode = true;

    private Button mAskCaptainButton;
    private Button mGoBackButton;
    private ImageView mCaptainImageView;
    private ConstraintLayout mMovieConstraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRandomMovie().observe(this, movie -> {
            if (movie != null) {
                ((TextView) mMovieConstraintLayout.findViewById(R.id.titleTextView))
                        .setText(movie.title);
                ImageView movieImageView = mMovieConstraintLayout.findViewById(R.id.posterImageView);
                Picasso.with(this).load(movie.poster_path).into(movieImageView);
            }
        });

        mAskCaptainButton = findViewById(R.id.askCaptainButton);
        mCaptainImageView = findViewById(R.id.captain);
        mMovieConstraintLayout = findViewById(R.id.movieLayout);
        mGoBackButton = mMovieConstraintLayout.findViewById(R.id.goBackButton);

        mGoBackButton.setOnClickListener(v-> goToCaptainMode());

        mAskCaptainButton.setOnClickListener(v-> goToMovieMode());

    }

    private void goToCaptainMode() {
        inCaptainMode = true;
        //Show the captain
        mCaptainImageView.animate().translationY(0).setDuration(ANIMATION_DURATION);
        mCaptainImageView.animate().alpha(1).setDuration(ANIMATION_DURATION);
        mAskCaptainButton.animate().translationY(0).setDuration(ANIMATION_DURATION);
        //Show the askCaptainButton
        mAskCaptainButton.animate().translationY(0).setDuration(ANIMATION_DURATION);
        //Hide the movie layout
        mMovieConstraintLayout.animate().translationY(-1550).setDuration(ANIMATION_DURATION)
                .withEndAction(() -> mMovieConstraintLayout.setVisibility(View.GONE));
    }

    private void goToMovieMode() {
        inCaptainMode = false;
        //Hide the captain
        mCaptainImageView.animate().translationY(1250).setDuration(ANIMATION_DURATION);
        mCaptainImageView.animate().alpha(0).setDuration(ANIMATION_DURATION);
        //Hide the askCaptainButton
        mAskCaptainButton.animate().translationY(200).setDuration(ANIMATION_DURATION);
        //Show the movie layout
        mMovieConstraintLayout.setTranslationY(-1550);
        mMovieConstraintLayout.setVisibility(View.VISIBLE);
        mMovieConstraintLayout.animate().translationY(0).setDuration(ANIMATION_DURATION);
    }
}
