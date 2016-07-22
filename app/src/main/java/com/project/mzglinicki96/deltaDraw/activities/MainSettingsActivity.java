package com.project.mzglinicki96.deltaDraw.activities;

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

import com.project.mzglinicki96.deltaDraw.FloatingColorMenuHelper;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.SettingModel;
import com.project.mzglinicki96.deltaDraw.SettingsHelper;
import com.project.mzglinicki96.deltaDraw.SettingsManager;
import com.project.mzglinicki96.deltaDraw.adapters.SettingsAdapter;
import com.project.mzglinicki96.deltaDraw.database.DatabaseHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 04.07.2016.
 */
public class MainSettingsActivity extends AppCompatActivity implements SettingsAdapter.ClickListener {

    @Inject
    DatabaseHelper databaseHelper;
    @Inject
    SharedPreferences sharedPreferences;
    @Bind(R.id.settingsListRecycleView)
    RecyclerView recyclerView;

    private SettingsAdapter adapter;
    private List<SettingModel> settingModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        ((MyApplication) getApplication()).getComponent().inject(this);

        settingModels = SettingsManager.getInstance(this).getListOfSettings();
        adapter = new SettingsAdapter(this, settingModels, this);

        setupRecycleView();
    }

    private void setupRecycleView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
        finish();
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

        final String arrayOfColors[] = getResources().getStringArray(R.array.colors);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.setting_select_color_title))
                .setCancelable(false)
                .setMultiChoiceItems(arrayOfColors, isColorChecked(), new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        final SharedPreferences.Editor editor = sharedPreferences.edit();

                        for (final FloatingColorMenuHelper floatingColorMenuHelper : FloatingColorMenuHelper.values()) {
                            if (which == floatingColorMenuHelper.ordinal()) {
                                editor.putBoolean(floatingColorMenuHelper.getColorKey(), isChecked);
                            }
                        }
                        editor.apply();
                    }
                })
                .setPositiveButton(getString(R.string.ok), null)
                .create()
                .show();
    }

    private boolean[] isColorChecked() {
        final boolean isColorChecked[] = new boolean[FloatingColorMenuHelper.values().length];

        for (FloatingColorMenuHelper floatingColorMenuHelper : FloatingColorMenuHelper.values()) {
            isColorChecked[floatingColorMenuHelper.ordinal()] = getSharedPreferences(floatingColorMenuHelper.getColorKey());
        }
        return isColorChecked;
    }

    private boolean getSharedPreferences(String colorKey) {
        return sharedPreferences.getBoolean(colorKey, true);
    }

    private void clearSharedPreferences() {
        sharedPreferences.edit().clear().apply();
        settingModels.get(SettingsHelper.COLOR_MENU_VISIBILITY.ordinal()).setMarked();
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
                .setNegativeButton(R.string.no_btn, null)
                .create()
                .show();
    }
}