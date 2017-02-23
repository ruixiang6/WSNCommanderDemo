package com.doun.wsncommanderdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

public class MonitorActivity extends AppCompatActivity {

    private final String TAG = "MonitorActivity";
    private final int port = 5858;
    TextView textView;
    FrameDataRecService.UDPTestOnTimeBinder serviceBinder=null;

    ServiceConnection serviceConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceBinder = (FrameDataRecService.UDPTestOnTimeBinder)service;
            serviceBinder.setUIHandle(handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private List<FrameData> fruitList = new ArrayList<FrameData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        //获取本地ip
        textView = (TextView)findViewById(R.id.text_ip_show);
        String str = getLocalIpAddress();
        if (str!=null)
        {
            textView.setText("本机UDP接收地址："+str+" : "+port);
        }

        //浮动按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //初始化列表
        initFruits();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        FrameDataAdapter adapter = new FrameDataAdapter(fruitList);
        recyclerView.setAdapter(adapter);

        //开启服务 模拟UDP发送 (不需要时可将其注释掉或删除)
        UDPTestOnTime.startActionSend(this, null, null);

        //开启服务 UDP接收
        Intent intent = new Intent(this, FrameDataRecService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void initFruits() {
        for (int i = 0; i < 2; i++) {
            FrameData apple = new FrameData(getRandomLengthName("Apple"), R.drawable.apple_pic);
            fruitList.add(apple);
            FrameData banana = new FrameData(getRandomLengthName("Banana"), R.drawable.banana_pic);
            fruitList.add(banana);
            FrameData orange = new FrameData(getRandomLengthName("Orange"), R.drawable.orange_pic);
            fruitList.add(orange);
            FrameData watermelon = new FrameData(getRandomLengthName("Watermelon"), R.drawable.watermelon_pic);
            fruitList.add(watermelon);
            FrameData pear = new FrameData(getRandomLengthName("Pear"), R.drawable.pear_pic);
            fruitList.add(pear);
            FrameData grape = new FrameData(getRandomLengthName("Grape"), R.drawable.grape_pic);
            fruitList.add(grape);
            FrameData pineapple = new FrameData(getRandomLengthName("Pineapple"), R.drawable.pineapple_pic);
            fruitList.add(pineapple);
            FrameData strawberry = new FrameData(getRandomLengthName("Strawberry"), R.drawable.strawberry_pic);
            fruitList.add(strawberry);
            FrameData cherry = new FrameData(getRandomLengthName("Cherry"), R.drawable.cherry_pic);
            fruitList.add(cherry);
            FrameData mango = new FrameData(getRandomLengthName("Mango"), R.drawable.mango_pic);
            fruitList.add(mango);
        }
    }

    private String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(name);
        }
        return builder.toString();
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle3 = msg.getData();
                    Toast.makeText(getApplicationContext(), Arrays.toString(bundle3.getByteArray("data")), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        unbindService(serviceConnection);
        Intent intent = new Intent(this, FrameDataRecService.class);
        stopService(intent);
        super.onDestroy();
    }


    //获取ip
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }
}
