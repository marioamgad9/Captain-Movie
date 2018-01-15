package com.mouris.mario.captainmovie.UI;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.mouris.mario.captainmovie.R;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int ANIMATION_DURATION = 600;

    private boolean inCaptainMode = true;

    private Button mAskCaptainButton;
    private ImageView mCaptainImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mAskCaptainButton = findViewById(R.id.askCaptainButton);
        mCaptainImageView = findViewById(R.id.captain);

        mAskCaptainButton.setOnClickListener(v->{
            if (inCaptainMode) {
                goToMovieMode();
            } else {
                goToCaptainMode();
            }
        });

    }

    private void goToCaptainMode() {
        inCaptainMode = true;
        //Show the captain
        mCaptainImageView.animate().translationY(0).setDuration(ANIMATION_DURATION);
        mCaptainImageView.animate().alpha(1).setDuration(ANIMATION_DURATION);
        mAskCaptainButton.animate().translationY(0).setDuration(ANIMATION_DURATION);
    }

    private void goToMovieMode() {
        inCaptainMode = false;
        //Hide the captain
        mCaptainImageView.animate().translationY(1250).setDuration(ANIMATION_DURATION);
        mCaptainImageView.animate().alpha(0).setDuration(ANIMATION_DURATION);
        //Show the askCaptainButton
        mAskCaptainButton.animate().translationY(200).setDuration(ANIMATION_DURATION);
    }
}
