package com.gavblaze.android.transitionsandanimations;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Pair;
import android.view.View;
import android.view.Window;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String TRANSITION_TYPE = "transition_type";
    private static final String ANDROID_TRANSITION = "switchAndroid";
    private static final String BLUE_TRANSITION = "switchBlue";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && b != null) {

            switch (Objects.requireNonNull(b.getString(TRANSITION_TYPE))) {
                case "Explode":
                    getWindow().setEnterTransition(new Explode());
                    break;
                case "Fade":
                    getWindow().setEnterTransition(new Fade());
                    break;
                default:
                    break;
            }
        }
    }

    /*One of the colored blocks relaunches the Activity using the Explode animation for both the enter and exit transitions.*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleClick(View view) {

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra(TRANSITION_TYPE, "Explode"); // so we can determine which transition when Activity restarts
        getWindow().setExitTransition(new Explode());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, "redBlock");
        startActivity(intent, options.toBundle());

    }

    /*Relaunch the Activity from another colored block, this time using the Fade transition.*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void lineClick(View view) {

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.putExtra(TRANSITION_TYPE, "Fade");
        getWindow().setExitTransition(new Fade());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, view, "blueBlock");
        startActivity(intent, options.toBundle());
    }


    /*Touching the third colored block starts an in-place animation of the View (such as a rotation).*/
    public void squareClick(View view) {

        ObjectAnimator animation = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f);
        animation.setDuration(1000);
        animation.setRepeatMode(ValueAnimator.REVERSE);
        animation.setRepeatCount(1);
        animation.start();
    }

    /*Finally, touching the Android icon starts a secondary Activity with a Shared Element Transition swapping the Android block with one of the other blocks.*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void iconClick(View view) {

        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(findViewById(R.id.androidBlock), ANDROID_TRANSITION),
                Pair.create(findViewById(R.id.blueBlock), BLUE_TRANSITION));
        startActivity(intent, options.toBundle());
    }

}
