package com.project.mzglinicki96.deltaDraw.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.project.mzglinicki96.deltaDraw.R;

/**
 * Created by mzglinicki.96 on 04.07.2016.
 */
public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(CreditsActivity.this, DatabaseActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
        finish();
    }
}