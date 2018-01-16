package com.mouris.mario.captainmovie.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mouris.mario.captainmovie.R;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int ANIMATION_DURATION = 600;
    private static final int SHORT_ANIMATION_DURATION = 300;

    private boolean inCaptainMode = true;

    private Button mAskCaptainButton;
    private FloatingActionButton mCancelFAB;
    private ImageView mCaptainImageView;
    private LinearLayout mMovieLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getRandomMovie().observe(this, movie -> {
            if (movie != null) {
                ((TextView) findViewById(R.id.titleTextView))
                        .setText(movie.title);
                ImageView movieImageView = findViewById(R.id.posterImageView);
                Picasso.with(this).load(movie.poster_path).into(movieImageView);
            }
        });

        mAskCaptainButton = findViewById(R.id.askCaptainButton);
        mCaptainImageView = findViewById(R.id.captain);
        mMovieLayout = findViewById(R.id.movieLayout);
        mCancelFAB = findViewById(R.id.cancelFAB);

        mCancelFAB.setOnClickListener(v-> goToCaptainMode());

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
        mMovieLayout.animate().translationY(-1550).setDuration(ANIMATION_DURATION)
                .withEndAction(() -> mMovieLayout.setVisibility(View.GONE));
    }

    private void goToMovieMode() {
        inCaptainMode = false;
        //Hide the captain
        mCaptainImageView.animate().translationY(1250).setDuration(ANIMATION_DURATION);
        mCaptainImageView.animate().alpha(0).setDuration(ANIMATION_DURATION);
        //Hide the askCaptainButton
        mAskCaptainButton.animate().translationY(200).setDuration(SHORT_ANIMATION_DURATION);
        //Show the movie layout
        mMovieLayout.setTranslationY(-1550);
        mMovieLayout.setVisibility(View.VISIBLE);
        mMovieLayout.animate().translationY(0).setDuration(ANIMATION_DURATION);
    }
}
