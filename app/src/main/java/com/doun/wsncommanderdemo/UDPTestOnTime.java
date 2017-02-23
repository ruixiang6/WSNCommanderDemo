package com.doun.wsncommanderdemo;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static java.lang.Thread.sleep;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UDPTestOnTime extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.doun.wsncommanderdemo.action.FOO";
    private static final String ACTION_BAZ = "com.doun.wsncommanderdemo.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.doun.wsncommanderdemo.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.doun.wsncommanderdemo.extra.PARAM2";

    public UDPTestOnTime() {
        super("UDPTestOnTime");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UDPTestOnTime.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UDPTestOnTime.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }
    public DatagramSocket udpSocket;

    private static final String TAG = "BroadCastUdp";
    private byte[] getData = null;
    private boolean sendontime = true;


//    public BroadCastUdp(byte[] data) {
//        this.getData = data;
//    }
    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        DatagramPacket dataPacket = null;
        try {
            udpSocket = new DatagramSocket();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        while (sendontime) {
            try {
                getData = new byte[]{'a','b'};
                dataPacket = new DatagramPacket(getData, getData.length);
                dataPacket.setData(getData);
                dataPacket.setLength(getData.length);
                dataPacket.setPort(5858);
                dataPacket.setAddress(InetAddress.getByName("127.0.0.1"));//202.11.4.67测试IP
//                Log.e(TAG, "udpSocket.send(dataPacket);");

                udpSocket.send(dataPacket);
                sleep(8000);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        udpSocket.close();

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        sendontime = false;
    }

}
