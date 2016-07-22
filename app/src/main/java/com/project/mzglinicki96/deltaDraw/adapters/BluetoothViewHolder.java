package com.project.mzglinicki96.deltaDraw.adapters;

import android.view.View;
import android.widget.TextView;

import com.project.mzglinicki96.deltaDraw.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzglinicki.96 on 22.07.2016.
 */
public class BluetoothViewHolder {

    @Bind(R.id.bluetoothTitleText)
    TextView bluetoothTitleText;

    public BluetoothViewHolder(View convertView) {
        ButterKnife.bind(this, convertView);
    }

    public TextView getBluetoothTitleText() {
        return bluetoothTitleText;
    }
}