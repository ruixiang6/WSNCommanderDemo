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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

public class MonitorActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "MonitorActivity";
    private final int PORT = 5858;
    TextView mTextView;
    FrameDataRecService.UDPTestOnTimeBinder mServiceBinder =null;
    private boolean mReceiving = true;
    FloatingActionButton mFabPlay = null;
    FloatingActionButton mFabBottom = null;
    boolean mScrollBottom = true;

    ServiceConnection serviceConnection = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceBinder = (FrameDataRecService.UDPTestOnTimeBinder)service;
            mServiceBinder.setUIHandle(handler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private List<FrameData> fruitList = new ArrayList<FrameData>();
    FrameDataAdapter adapter = null;
    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        //获取本地ip
        mTextView = (TextView)findViewById(R.id.text_ip_show);
        String str = getLocalIpAddress();
        if (str!=null)
        {
            mTextView.setText("本机UDP接收地址："+str+" : "+ PORT);
        }

        //浮动按钮
        mFabPlay = (FloatingActionButton) findViewById(R.id.fab_play);
        mFabPlay.setOnClickListener(this);
        mFabBottom = (FloatingActionButton) findViewById(R.id.fab_bottom);
        mFabBottom.setOnClickListener(this);
        mFabBottom.hide();

        //初始化列表
//        initFruits();
        recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        adapter = new FrameDataAdapter(fruitList);
        recyclerView.setAdapter(adapter);
        //取消动画效果
        recyclerView.getItemAnimator().setAddDuration(0);
        recyclerView.getItemAnimator().setRemoveDuration(0);
        recyclerView.getItemAnimator().setMoveDuration(0);
        recyclerView.getItemAnimator().setChangeDuration(0);
        //设置滚动条
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            /**
             * 最后一个的位置
             */
            private int[] lastPositions;

            /**
             * 最后一个可见的item的位置
             */
            private int lastVisibleItemPosition;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                Log.i("onScrollStateChanged", "visibleItemCount" + visibleItemCount);
                Log.i("onScrollStateChanged", "lastVisibleItemPosition" + lastVisibleItemPosition);
                Log.i("onScrollStateChanged", "totalItemCount" + totalItemCount);
                if (visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition == totalItemCount - 1) {
                    mScrollBottom = true;
                    mFabBottom.hide();
                    Toast.makeText(MonitorActivity.this, "到底了哦", Toast.LENGTH_SHORT).show();
                }else {
                    mScrollBottom = false;
                    mFabBottom.show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("onScrolled", dx + "   " + dy);


                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
            }

            private int findMax(int[] lastPositions) {
                int max = lastPositions[0];
                for (int value : lastPositions) {
                    if (value > max) {
                        max = value;
                    }
                }
                return max;
            }
        });

        //开启服务 模拟UDP发送 (不需要时可将其注释掉或删除)
        UDPTestOnTime.startActionSend(this, null, null);

        //开启服务 UDP接收
        Intent intent = new Intent(this, FrameDataRecService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }



    private void initFruits() {
        for (int i = 0; i < 10; i++) {
            byte str[] = new byte[]{11,22};
            byte str1[] = new byte[]{34,22};
            byte str2[] = new byte[]{26,44};
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            FrameData banana = new FrameData(date, str, ""+str.length,R.drawable.banana_pic);
            fruitList.add(banana);
            FrameData watermelon = new FrameData(date, str1, ""+str1.length, R.drawable.watermelon_pic);
            fruitList.add(watermelon);
            FrameData strawberry = new FrameData(date, str2, ""+str2.length, R.drawable.strawberry_pic);
            fruitList.add(strawberry);
        }
    }

    private String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(10) + 1;
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
//                    Toast.makeText(getApplicationContext(), Arrays.toString(bundle3.getByteArray("data")), Toast.LENGTH_SHORT).show();
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss.SSS");
                    String date = sDateFormat.format(new java.util.Date());
                    int length = bundle3.getByteArray("data").length;
                    int id = length%3;
                    int imageID = 0;
                    switch (id){
                        case 0:
                            imageID = R.drawable.banana_pic;
                            break;
                        case 1:
                            imageID = R.drawable.strawberry_pic;
                            break;
                        case 2:
                            imageID = R.drawable.watermelon_pic;
                            break;
                        default:
                            break;
                    }
                    FrameData banana = new FrameData(date, bundle3.getByteArray("data"), ""+length, imageID);
                    fruitList.add(banana);
                    adapter.notifyItemInserted(fruitList.size()-1);
                    if (mScrollBottom == true){
                        recyclerView.scrollToPosition(fruitList.size()-1);
                    }

                    break;
                case 2:
//                    Intent intent = new Intent(MonitorActivity.class, FrameDataRecService.class);
//                    startService(intent);
//                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_play:
                if (mReceiving == true){
                    mServiceBinder.stopReceive();
                    mReceiving = false;
                    mFabPlay.setImageResource(android.R.drawable.ic_media_play);
                    Snackbar.make(view, "UDP接收已停止", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                } else {
                    startService(new Intent(this, FrameDataRecService.class));
                    mReceiving = true;
                    mFabPlay.setImageResource(android.R.drawable.ic_media_pause);
                    Snackbar.make(view, "UDP接收已开始", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
                break;
            case R.id.fab_bottom:
                recyclerView.scrollToPosition(fruitList.size()-1);
                mScrollBottom = true;
                mFabBottom.hide();
                break;
            default:
                break;
        }
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
