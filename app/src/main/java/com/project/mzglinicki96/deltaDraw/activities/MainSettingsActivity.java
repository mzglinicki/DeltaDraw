package com.project.mzglinicki96.deltaDraw.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.project.mzglinicki96.deltaDraw.Constants;
import com.project.mzglinicki96.deltaDraw.FloatingColorMenuHelper;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.SettingModel;
import com.project.mzglinicki96.deltaDraw.SettingsHelper;
import com.project.mzglinicki96.deltaDraw.SettingsManager;
import com.project.mzglinicki96.deltaDraw.adapters.SettingsAdapter;
import com.project.mzglinicki96.deltaDraw.database.DatabaseHelper;

import java.util.List;

/**
 * Created by mzglinicki.96 on 04.07.2016.
 */
public class MainSettingsActivity extends AppCompatActivity implements SettingsAdapter.ClickListener {

    private final DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
    private final SettingsManager settingsManager = SettingsManager.getInstance(this);
    private SettingsAdapter adapter;
    private List<SettingModel> settingModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingModels = settingsManager.getListOfSettings();
        adapter = new SettingsAdapter(this, settingModels, this);

        setupRecycleView();
    }

    private void setupRecycleView() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.settingsListRecycleView);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
    }

    @Override
    public void onClick(final SettingModel settingModel) {

        if (settingModel.getSettingId() == SettingsHelper.DELETE_ALL.ordinal()) {
            deleteAllImagesDialog();
        } else if (settingModel.getSettingId() == SettingsHelper.COLORS_AMOUNT.ordinal()) {
            setDefaultColor();
        } else {
            clearSharedPreferences();
        }
    }

    @Override
    public void onCheckBoxClick(final SettingModel settingModel) {
        settingModel.toggleMark();
    }

    private void setDefaultColor() {

        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_APPEND);
        boolean isBlackEnable = sharedPreferences.getBoolean(Constants.BLACK_COLOR_KEY, true);
        boolean isGreenEnable = sharedPreferences.getBoolean(Constants.GREEN_COLOR_KEY, true);
        boolean isBlueEnable = sharedPreferences.getBoolean(Constants.BLUE_COLOR_KEY, true);
        boolean isRedEnable = sharedPreferences.getBoolean(Constants.RED_COLOR_KEY, true);
        boolean isOrangeEnable = sharedPreferences.getBoolean(Constants.ORANGE_COLOR_KEY, true);
        boolean isYellowEnable = sharedPreferences.getBoolean(Constants.YELLOW_COLOR_KEY, true);

        final String arrayOfColors[] = getResources().getStringArray(R.array.colors);
        boolean isColorChecked[] = {isBlackEnable, isGreenEnable, isBlueEnable, isRedEnable, isOrangeEnable, isYellowEnable};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.setting_select_color_title))
                .setCancelable(false)
                .setMultiChoiceItems(arrayOfColors, isColorChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        for (final FloatingColorMenuHelper floatingColorMenuHelper : FloatingColorMenuHelper.values()) {
                            if (which == floatingColorMenuHelper.ordinal()) {
                                editor.putBoolean(floatingColorMenuHelper.getColorKey(), isChecked);
                            }
                        }
                        editor.apply();
                    }
                })
                .setPositiveButton(getString(R.string.ok), null);
        builder.create();
        builder.show();
    }

    private void clearSharedPreferences() {
        final SharedPreferences sharedPreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        settingModels.get(SettingsHelper.COLOR_MENU_VISIBILITY.ordinal()).setMark(true);
        adapter.update();
    }

    private void deleteAllImagesDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.deleteAllQuestion))
                .setPositiveButton(R.string.yes_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseHelper.deleteAllData();
                        Toast.makeText(MainSettingsActivity.this, R.string.allDeleted, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.no_btn, null);
        final AlertDialog alert = builder.create();
        alert.show();
    }
}