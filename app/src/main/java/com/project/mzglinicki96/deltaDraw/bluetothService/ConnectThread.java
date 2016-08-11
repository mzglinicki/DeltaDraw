package com.project.mzglinicki96.deltaDraw.bluetothService;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by mzglinicki.96 on 22.04.2016.
 */
public class ConnectThread extends Thread {

    private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
    private final BluetoothSocket socket;
    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final byte[] dataToSend;
    private ConnectedThread connectedThread;
    private Handler handler = new Handler();

    public ConnectThread(final BluetoothDevice device, final byte[] dataToSend) {
        BluetoothSocket bluetoothSocket = null;

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.socket = bluetoothSocket;
        this.dataToSend = dataToSend;
    }

    public void run() {
        bluetoothAdapter.cancelDiscovery();
        try {
            socket.connect();
        } catch (IOException connectException) {
            try {
                socket.close();
                GimBus.getInstance().post(new UnavailableConnectEvent());
            } catch (IOException closeException) {
                closeException.printStackTrace();
            }
            return;
        }
        connectedThread = new ConnectedThread(socket, dataToSend, handler);
        connectedThread.start();
    }

    public void cancel() {
        try {
            connectedThread.cancel();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}