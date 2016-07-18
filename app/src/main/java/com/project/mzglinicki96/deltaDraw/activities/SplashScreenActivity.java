package com.project.mzglinicki96.deltaDraw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.project.mzglinicki96.deltaDraw.R;
import com.github.glomadrian.roadrunner.DeterminateRoadRunner;

/**
 * Created by marcin on 12.05.16.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000;
    private DeterminateRoadRunner determinateRoadRunner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        determinateRoadRunner = (DeterminateRoadRunner) findViewById(R.id.determinate);
        assert determinateRoadRunner != null;
        determinateRoadRunner.buildDrawingCache();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                determinateRoadRunner.setVisibility(View.INVISIBLE);
                final Intent intent = new Intent(SplashScreenActivity.this, DatabaseActivity.class);
                startActivity(intent);
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