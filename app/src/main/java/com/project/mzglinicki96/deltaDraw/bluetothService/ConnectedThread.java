package com.project.mzglinicki96.deltaDraw.bluetothService;

import android.bluetooth.BluetoothSocket;

import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mzglinicki.96 on 22.04.2016.
 */
public class ConnectedThread extends Thread {

    private final BluetoothSocket socket;
    private final OutputStream outStream;
    private final byte[] dataToSend;

    public ConnectedThread(final BluetoothSocket socket, final byte[] dataToSend) {

        this.socket = socket;

        OutputStream outStream = null;
        try {
            outStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.outStream = outStream;
        this.dataToSend = dataToSend;
    }

    public void run() {
        try {
            outStream.write(dataToSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GimBus.getInstance().post(new ConnectedEvent());
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}