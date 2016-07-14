package com.project.mzglinicki96.deltaDraw.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.SettingModel;

import java.util.List;

/**
 * Created by mzglinicki.96 on 08.07.2016.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsViewHolder> {

    private final ClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<SettingModel> settingModels;

    public SettingsAdapter(final Context context, final List<SettingModel> settingModels, final ClickListener clickListener) {
        inflater = LayoutInflater.from(context);
        this.settingModels = settingModels;
        this.clickListener = clickListener;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.settings_layout_model, parent, false);
        return new SettingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SettingsViewHolder holder, final int position) {

        final SettingModel settingModel = settingModels.get(position);

        final TextView settingTitle = holder.getSettingTitle();
        settingTitle.setText(settingModel.getSettingTitle());
        holder.getSettingTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(settingModel);
            }
        });

        final CheckBox checkBox = holder.getCheckBox();
        checkBox.setChecked(settingModel.isMark());
        if (settingModel.isCheckBoxEnable()) {

            checkBox.setVisibility(View.VISIBLE);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onCheckBoxClick(settingModel);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return settingModels.size();
    }

    public void update() {
        for (SettingModel settingModel : settingModels) {
            settingModel.setMark(true);
        }
        this.notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(final SettingModel settingModel);
        void onCheckBoxClick(final SettingModel settingModel);
    }
}