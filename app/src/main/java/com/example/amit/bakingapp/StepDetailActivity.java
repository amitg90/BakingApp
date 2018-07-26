package com.example.amit.bakingapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class StepDetailActivity  extends Activity {

    public RecipeDetailAdapter recipeDetailAdapter = null;
    public static RecyclerView recyclerView;
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private TextView stepDescrView;
    public static String STEP_ID_STR = "step_id";
    public static String RECIPE_ID_STR = "recipe_id";
    int step_id = -1;
    int recipe_id = -1;
    RecipeInfo recipeInfo;
    Steps step;

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            Log.e("Amit", "initializePlayer: mExoPlayer NULL\n");

            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setUseController(true);
            mPlayerView.requestFocus();
            mPlayerView.setPlayer(mExoPlayer);
        }

        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(this, "ClassicalMusicQuiz");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        // Initialize the player view.
        mPlayerView = findViewById(R.id.playerView);
        stepDescrView = findViewById(R.id.step_description);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        step_id = intent.getIntExtra(StepDetailActivity.STEP_ID_STR, -1);
        recipe_id = intent.getIntExtra(StepDetailActivity.RECIPE_ID_STR, -1);

        if (step_id == -1 || recipe_id == -1) {
            Toast.makeText(this, "Invalid Position entered", Toast.LENGTH_LONG).show();
            return;
        }

        recipeInfo = RecipeDb.recipeInfoArrayList.get(recipe_id);
        step = recipeInfo.steps.get(step_id);
        Log.e("Amit", "Step Info:" + step.shortDescription);

        if (stepDescrView != null)
            stepDescrView.setText(step.shortDescription);

        if (step.videoURL.isEmpty() == false) {
            Log.e("Amit", "Video URL Exist");
            // we have veodop URL
            initializePlayer(Uri.parse(step.videoURL));
        } else {
            Log.e("Amit", "NO Video URL Exist");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelable("DETAIL_ACTIVITY_SAVED_LAYOUT_MANAGER", getLayoutManager().onSaveInstanceState());
        Log.d("MovieApp", "!!!Saving Position:");
    }

    public void prevClick(View view) {
        Log.e("Amit", "Prev clicked");
        if (step_id > 0) {
            step_id--;
            step = recipeInfo.steps.get(step_id);

            if (stepDescrView != null)
                stepDescrView.setText(step.shortDescription);

            if (step.videoURL.isEmpty() == false) {
                Log.e("Amit", "Video URL Exist");
                // we have veodop URL
                initializePlayer(Uri.parse(step.videoURL));
            } else {
                mExoPlayer.release();
                mExoPlayer.clearVideoSurface();
            }
        }
    }

    public void nextClick(View view) {
        Log.e("Amit", "Next clicked");
        if (step_id < recipeInfo.steps.size()) {
            step_id++;
            step = recipeInfo.steps.get(step_id);

            if (stepDescrView != null)
                stepDescrView.setText(step.shortDescription);
            if (step.videoURL.isEmpty() == false) {
                Log.e("Amit", "Video URL Exist");
                // we have veodop URL
                initializePlayer(Uri.parse(step.videoURL));
            } else {
                Log.e("Amit", "NO Video URL Exist");
            }
        }
    }
}
