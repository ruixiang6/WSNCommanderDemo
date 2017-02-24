package com.doun.wsncommanderdemo;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

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
    private static final String ACTION_SEND = "com.doun.wsncommanderdemo.action.SEND";
    private static final String ACTION_RECEIVE = "com.doun.wsncommanderdemo.action.RECEIVE";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.doun.wsncommanderdemo.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.doun.wsncommanderdemo.extra.PARAM2";


    private final int port = 5858;

    public DatagramSocket udpSocketSend;
    private static final String TAG = "UDPTestOnTime";
    private byte[] getData = null;
    public boolean sendOnTime = true;

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

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
    public static void startActionSend(Context context, String param1, String param2) {
        Intent intent = new Intent(context, UDPTestOnTime.class);
        intent.setAction(ACTION_SEND);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
        ComponentName name = context.startService(intent);
        if (name!=null){
            Log.i(TAG, name.toShortString());
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SEND.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            }
        }
    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        Log.i(TAG, "handleActionFoo");
        DatagramPacket dataPacket = null;
        try {
            udpSocketSend = new DatagramSocket();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        while (sendOnTime) {
            try {
                getData = new byte[]{'a','b'};
                dataPacket = new DatagramPacket(getData, getData.length);
                dataPacket.setData(getData);
                dataPacket.setLength(getData.length);
                dataPacket.setPort(port);
                dataPacket.setAddress(InetAddress.getByName("127.0.0.1"));//202.11.4.67测试IP

                udpSocketSend.send(dataPacket);
                sleep(100);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        udpSocketSend.close();
        stopSelf();
    }
}
