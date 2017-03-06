package com.project.mzglinicki96.deltaDraw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.project.mzglinicki96.deltaDraw.R;
import com.github.glomadrian.roadrunner.DeterminateRoadRunner;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by marcin on 12.05.16.
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Bind(R.id.determinate)
    protected DeterminateRoadRunner determinateRoadRunner;

    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        determinateRoadRunner.buildDrawingCache();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                determinateRoadRunner.setVisibility(View.INVISIBLE);
                startActivity(new Intent(SplashScreenActivity.this, DatabaseActivity.class));
                overridePendingTransition(R.animator.trans_left_in, R.animator.trans_left_out);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }
}