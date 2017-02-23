package com.doun.wsncommanderdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class FrameDataRecService extends Service {

    private final String TAG = "FrameDataRecService";
    Handler uiHandle = null;
    private final int port = 5858;
    public boolean receiveOnTime = true;

    public DatagramSocket udpSocketRec = null;
    DatagramPacket udpPacket = null;
    byte[] data = new byte[128];

    UDPTestOnTimeBinder mBinder=new UDPTestOnTimeBinder();

    public FrameDataRecService() {
    }

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
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        receiveOnTime = false;
        if (udpSocketRec!=null) udpSocketRec.close();
        return super.onUnbind(intent);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG, "onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    udpSocketRec = new DatagramSocket(port);
                    udpPacket = new DatagramPacket(data, data.length);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                while (receiveOnTime) {
                    try {
                        Log.i(TAG, "try to receive data");
                        udpSocketRec.receive(udpPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                    //接收到的byte[]
                    byte[] m = Arrays.copyOf(udpPacket.getData(), udpPacket.getLength());

                    Message message1 = new Message();
                    message1.what = 1;
                    Bundle bundle1 = new Bundle();
                    bundle1.putByteArray("data", m);
                    message1.setData(bundle1);
                    if (uiHandle!=null){
                        uiHandle.sendMessage(message1);
                    }
                }
                try {
                    udpSocketRec.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    class UDPTestOnTimeBinder extends Binder {
        public void setUIHandle(Handler handle){
            uiHandle = handle;
        }

        public void stopReceive(){
            receiveOnTime = false;
            stopSelf();
        }
    }

}
