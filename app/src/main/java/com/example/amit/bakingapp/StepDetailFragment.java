package com.example.amit.bakingapp;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class StepDetailFragment extends Fragment {
    public static SimpleExoPlayer mExoPlayer = null;
    private SimpleExoPlayerView mPlayerView;
    private TextView stepDescrView;
    private ImageView stepImageView;
    public static String STEP_ID_STR = "step_id";
    public static String RECIPE_ID_STR = "recipe_id";
    public static int recipe_id = -1;
    RecipeInfo recipeInfo;
    Steps step;
    Context context = null;
    public static long lastPlayerPosition = 0;
    public int step_id_position = -1;

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            Log.e("StepDetailFragment", "initializePlayer: mExoPlayer\n");

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
        mExoPlayer.seekTo(lastPlayerPosition);
        Log.e("StepDetailFragment", "Last Position:" + lastPlayerPosition);
        mExoPlayer.setPlayWhenReady(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail,
                container, false);

        Log.e("StepDetailFragment", "onCreateView called!!:");

        // Initialize the player view.
        mPlayerView = view.findViewById(R.id.playerView);
        stepDescrView = view.findViewById(R.id.step_description);
        stepImageView = view.findViewById(R.id.recipe_step_image_view);

        if (stepDescrView != null) {
            Log.e("StepDetailFragment", "stepDescrView NOT NULL!!:");
        } else {
            Log.e("StepDetailFragment", "stepDescrView is NULL!!:");
        }


        return (view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void getDataFromBundle(Bundle bundle) {
        // Get the Intent that started this activity and extract the string
        recipe_id = bundle.getInt(RecipeDetail.RECIPE_ID_STR, -1);
        step_id_position = bundle.getInt(RecipeDetail.STEP_ID_POSITION_STR, -1);
        lastPlayerPosition = bundle.getLong(RecipeDetail.LAST_PLAYER_POSITION_STR, 0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        Log.e("StepDetailFragment", "onActivityCreated called!!:");

        if (bundle != null) {

            getDataFromBundle(bundle);

            Log.e("StepDetailFragment", "Bundle returned step_id_position: !!:" + step_id_position);
            if (step_id_position == -1 || recipe_id == -1) {
                Toast.makeText(context, "Invalid Position entered", Toast.LENGTH_LONG).show();
                return;
            }

            recipeInfo = RecipeDb.recipeInfoArrayList.get((recipe_id - 1));

            if (step_id_position > recipeInfo.steps.size() ||
                    step_id_position < 1) {
                return;
            }
            step = recipeInfo.steps.get((step_id_position -1));
            Log.e("StepDetailFragment", "Step Info:" + step.shortDescription + ":" + step.description);

            if (stepDescrView != null) {
                stepDescrView.setText(step.description);
                stepDescrView.setMovementMethod(new ScrollingMovementMethod());
            }

            if (stepImageView != null) {

                String default_image = "https://www.fnordware.com/superpng/pnggrad16rgb.png";
                if (step.thumbnailURL.isEmpty() == false) {
                   // Log.e("StepDetailFragment", "Loading Image:" + step.thumbnailURL);
                    Picasso.with(context).load(step.thumbnailURL).into(stepImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            //Log.e("Amit", "Loading Image: Success");
                        }

                        @Override
                        public void onError() {
                            Log.e("StepDetailFragment", "Loading Image: Failed");
                        }
                    });
                } else {
                    //Log.e("StepDetailFragment", "Loading Default Image:" + recipeInfo.name);
                    Picasso.with(context).load(default_image).into(stepImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                           // Log.e("Amit", "Loading Image: Success");
                        }

                        @Override
                        public void onError() {
                            Log.e("StepDetailFragment", "Loading Image: Failed");
                        }
                    });
                }

            }

            if (step.videoURL.isEmpty() == false) {
                Log.e("StepDetailFragment", "Initializing Player for step");
                // we have veodop URL
                initializePlayer(Uri.parse(step.videoURL));
            } else {
                Log.e("StepDetailFragment", "NO Video to show");
            }
        } else {
            Log.e("StepDetailFragment", "NO BUNDLE!!:");
        }

       // saveFragmentInfoInSharedMemory(true);
    }

//    public void saveFragmentInfoInSharedMemory(boolean defaults) {
//        if (sharedPreferences != null) {
//            if (defaults == true) {
//                Log.e("StepDetailFragment", "Saving DEFAULT State in Shared Memory");
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt(RecipeDetail.STEP_ID_POSITION_STR, -1);
//                editor.putLong(RecipeDetail.LAST_PLAYER_POSITION_STR, 0);
//                editor.putInt(RecipeDetail.RECIPE_ID_STR, -1);
//                editor.apply();
//            } else {
//                Log.e("StepDetailFragment", "Saving Current State in Shared Memory");
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt(RecipeDetail.STEP_ID_POSITION_STR, (step_id_position + 1));
//                editor.putLong(RecipeDetail.LAST_PLAYER_POSITION_STR, lastPlayerPosition);
//                editor.putInt(RecipeDetail.RECIPE_ID_STR, recipe_id);
//                editor.apply();
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("StepDetailFragment", "!!!onDestroy called");
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("StepDetailFragment", "onPause called");
        releasePlayer();
    }

    public static void releasePlayer() {
        if (mExoPlayer != null) {
            Log.e("StepDetailFragment", "RELEASING PLAYER");
            lastPlayerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStop() {
        Log.e("StepDetailFragment", "onStop called");
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("StepDetailFragment", "onResume called");

        if (step != null) {
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
