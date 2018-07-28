package com.example.amit.bakingapp;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RecipeDetailFragment extends Fragment {
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private TextView stepDescrView;
    public static String STEP_ID_STR = "step_id";
    public static String RECIPE_ID_STR = "recipe_id";
    int step_id = -1;
    int recipe_id = -1;
    RecipeInfo recipeInfo;
    Steps step;
    Context context = null;

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
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            mPlayerView.setUseController(true);
            mPlayerView.requestFocus();
            mPlayerView.setPlayer(mExoPlayer);
        }

        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(context, "ClassicalMusicQuiz");
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                context, userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail,
                container, false);

        Log.e("RecipeDetailFragment", "onCreateView called!!:");

        // Initialize the player view.
        mPlayerView = view.findViewById(R.id.playerView);
        stepDescrView = view.findViewById(R.id.step_description);

        return (view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        Log.e("RecipeDetailFragment", "onActivityCreated called!!:");
        if (bundle != null) {
            // Get the Intent that started this activity and extract the string
            step_id = bundle.getInt(StepDetailActivity.STEP_ID_STR, -1);
            recipe_id = bundle.getInt(StepDetailActivity.RECIPE_ID_STR, -1);

            if (step_id == -1 || recipe_id == -1) {
                Toast.makeText(context, "Invalid Position entered", Toast.LENGTH_LONG).show();
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
        } else {
            Log.e("RecipeDetailFragment", "BUNDLE IS NULL!!:");
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("RecipeDetailFragment", "!!!onDestroy called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("RecipeDetailFragment", "onPause called");
        if (mExoPlayer != null) {
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
