package com.project.mzglinicki96.deltaDraw.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pgssoft.gimbus.Subscribe;
import com.project.mzglinicki96.deltaDraw.PointListHolder;
import com.project.mzglinicki96.deltaDraw.R;
import com.project.mzglinicki96.deltaDraw.adapters.BluetoothDeviceListAdapter;
import com.project.mzglinicki96.deltaDraw.bluetothService.ConnectThread;
import com.project.mzglinicki96.deltaDraw.bluetothService.ConnectedEvent;
import com.project.mzglinicki96.deltaDraw.bluetothService.UnavailableConnectEvent;
import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mzglinicki.96 on 11.07.2016.
 */
public class BluetoothConnectionActivity extends AppCompatActivity implements BluetoothDeviceListAdapter.ClickListener {

    @Bind(R.id.listViewOfDevices)
    protected ListView listView;
    @Bind(R.id.progress_view)
    protected ProgressBar progressView;

    private static final int TURN_BT_ON_REQUEST_CODE = 1;
    private BluetoothAdapter bluetoothAdapter;
    private List<BluetoothDevice> devices;
    private List<Point> coordinatesList = new ArrayList<>();
    private ConnectThread connectThread;
    private BluetoothDeviceListAdapter devicesListAdapter;
    private boolean discovering = false;
    private Point screenSizePoint;
    private Point endOfFilePoint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);
        ButterKnife.bind(this);
        coordinatesList = PointListHolder.getInstance().getCoordinatesList();

        setProgressViewVisibility(discovering);
        setupBluetoothStateAndVisibility();
    }

    @Override
    protected void onStart() {
        super.onStart();
        GimBus.getInstance().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GimBus.getInstance().unregister(this);
    }

    @Override
    public void onClick(final int position) {

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        screenSizePoint = new Point(width, height);
        endOfFilePoint = new Point(-10, -10);
        coordinatesList.add(screenSizePoint);
        coordinatesList.add(endOfFilePoint);
        connect(position);
    }

    private void connect(final int position) {

        setProgressViewVisibility(true);
        final BluetoothDevice deviceToConnect = devices.get(position);
        connectThread = new ConnectThread(deviceToConnect, coordinatesList.toString().getBytes());
        connectThread.start();
        coordinatesList.remove(screenSizePoint);
        coordinatesList.remove(endOfFilePoint);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.trans_right_in, R.animator.trans_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver.isOrderedBroadcast()) {
            unregisterReceiver(receiver);
        }
        try {
            connectThread.cancel();
        } catch (final NullPointerException threadIsNull) {
            threadIsNull.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (requestCode != TURN_BT_ON_REQUEST_CODE) {
            return;
        }
        devices = getListOfDevices();
        setupBluetoothDevicesAdapter();
    }

    @OnClick(R.id.discoverButton)
    public void onDiscoverButtonClickListener() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(receiver, filter);
        bluetoothAdapter.startDiscovery();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onConnectedEvent(final ConnectedEvent event) {
        onBluetoothConnectionEvent(false, R.string.connected);

    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onUnconnectedEvent(@Nullable final UnavailableConnectEvent event) {
        onBluetoothConnectionEvent(false, R.string.device_unavailable);
    }

    private void onBluetoothConnectionEvent(final boolean isProgressVisible, final int massageResId){
        setProgressViewVisibility(isProgressVisible);
        Toast.makeText(this, massageResId, Toast.LENGTH_SHORT).show();
    }

    private void setupBluetoothStateAndVisibility() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.bt_not_support_toast, Toast.LENGTH_SHORT).show();
        }

        if (!bluetoothAdapter.isEnabled()) {
            final Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(turnOn, TURN_BT_ON_REQUEST_CODE);
        } else {
            devices = getListOfDevices();
            setupBluetoothDevicesAdapter();
        }
    }

    private List<BluetoothDevice> getListOfDevices() {

        final List<BluetoothDevice> nameOfPairedDevices = new ArrayList<>();
        nameOfPairedDevices.addAll(bluetoothAdapter.getBondedDevices());
        return nameOfPairedDevices;
    }

    private void setupBluetoothDevicesAdapter() {
        devicesListAdapter = new BluetoothDeviceListAdapter(this, devices, this);
        listView.setAdapter(devicesListAdapter);
    }

    private void setProgressViewVisibility(final boolean discovering) {
        progressView.setVisibility(discovering ? View.VISIBLE : View.INVISIBLE);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!devices.contains(device)) {
                    devices.add(device);
                }
                devicesListAdapter.notifyDataSetChanged();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                setProgressViewVisibility(discovering = true);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressViewVisibility(discovering = false);
            }
        }
    };
}