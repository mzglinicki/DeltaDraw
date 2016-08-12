package com.project.mzglinicki96.deltaDraw.bluetothService;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.project.mzglinicki96.deltaDraw.eventBus.GimBus;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mzglinicki.96 on 22.04.2016.
 */
public class ConnectedThread extends Thread {

    final Handler handler;
    private final BluetoothSocket socket;
    private final OutputStream outStream;
    private final byte[] dataToSend;

    public ConnectedThread(final BluetoothSocket socket, final byte[] dataToSend, final Handler handler) {

        this.socket = socket;
        this.handler = handler;
        OutputStream outStream = null;
        try {
            outStream = socket.getOutputStream();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        this.outStream = outStream;
        this.dataToSend = dataToSend;
    }

    public void run() {
        try {
            outStream.write(dataToSend);
            //TODO add eof
        } catch (final IOException e) {
            e.printStackTrace();
            cancel();
        }
        GimBus.getInstance().post(new ConnectedEvent());
    }

    public void cancel() {
        try {
            socket.close();
            outStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}