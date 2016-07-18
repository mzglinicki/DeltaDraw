package com.project.mzglinicki96.deltaDraw.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.mzglinicki96.deltaDraw.R;

import java.util.List;

/**
 * Created by mzglinicki.96 on 22.04.2016.
 */
public class BluetoothDeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private final List<BluetoothDevice> listOfDevices;
    private final ClickListener clickListener;

    public BluetoothDeviceListAdapter(final Context context, final List<BluetoothDevice> listOfDevices,ClickListener clickListener) {
        super(context, -1, listOfDevices);
        this.listOfDevices = listOfDevices;
        this.clickListener = clickListener;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final BluetoothDevice device = listOfDevices.get(position);

        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bluetooth_device_model, parent, false);
        }
        final TextView bluetoothTitleText = (TextView) convertView.findViewById(R.id.bluetoothTitleText);
        bluetoothTitleText.setText(device.getName());
        bluetoothTitleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListener != null){
                    clickListener.onClick(position);
                }
            }
        });
        return convertView;
    }
//
//    @Override
//    public long getItemId(final int position) {
//        return super.getItemId(position);
//    }

    @Override
    public BluetoothDevice getItem(final int position) {
        return listOfDevices.get(position);
    }

    public interface ClickListener{
        void onClick(final int position);
    }
}