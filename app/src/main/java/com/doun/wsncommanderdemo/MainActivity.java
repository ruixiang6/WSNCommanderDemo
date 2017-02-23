package com.doun.wsncommanderdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.ArcOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.DotOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tomylocation:
                navigateToMe();
//                Toast.makeText(this, "tomylocation", Toast.LENGTH_SHORT).show();
                break;
            case R.id.monitor:
                startActivity(new Intent(MainActivity.this, MonitorActivity.class));
//                Toast.makeText(this, "monitor", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public LocationClient mLocationClient;
    private TextView positionTest;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate=true;
    private BDLocation myLocation = null;

    // 普通折线，点击时改变宽度
    Polyline mPolyline;
    // 多颜色折线，点击时消失
    Polyline mColorfulPolyline;
    // 纹理折线，点击时获取折线上点数及width
    Polyline mTexturePolyline;

    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;

    BitmapDescriptor bdA = null;
    BitmapDescriptor bdB = null;

    private InfoWindow mInfoWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //隐藏ActionBar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
        Button monitorbutton = (Button) findViewById(R.id.monitor);
        monitorbutton.setOnClickListener(this);
        Button mylocationbutton = (Button) findViewById(R.id.tomylocation);
        mylocationbutton.setOnClickListener(this);

        //自定位信息对象
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        //获取mapview对象
        mapView = (MapView)findViewById(R.id.bmapView);
        //获取baidumap对象，并进行初始设置
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        //获取textview对象
        positionTest = (TextView) findViewById(R.id.position_text_view);

        //获取permission
        List<String> permissionList = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()){
            String []permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }

        // 初始化全局 bitmap 信息，不用时及时 recycle
        bdA = BitmapDescriptorFactory.fromResource(R.drawable.strawberry_pic);
        bdB = BitmapDescriptorFactory.fromResource(R.drawable.watermelon_pic);


        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {
                Button button = new Button(getApplicationContext());
                //button.setBackgroundResource(R.drawable.popup);
                InfoWindow.OnInfoWindowClickListener listener = null;
                if (marker == mMarkerA) {
                    button.setText("ID：458\n频点：258K\n时隙：165\n经纬度：N31.3960 E121.2453");
                } else if (marker == mMarkerB) {
                    button.setText("ID：288\n频点：258K\n时隙：155\n经纬度：N31.4060 E121.2453");
                } else if (marker == mMarkerC) {
                    button.setText("ID：589\n频点：258K\n时隙：89\n经纬度：N31.3960 E121.2553");
                } else if (marker == mMarkerD) {
                    button.setText("ID：112\n频点：258K\n时隙：215\n经纬度：N31.3860 E121.2453");
                }
                button.setTextColor(Color.WHITE);
                button.setBackgroundColor( Color.argb(180,79,79,79) );
                button.setWidth( 300 );
                listener = new InfoWindow.OnInfoWindowClickListener() {
                    public void onInfoWindowClick() {
                        baiduMap.hideInfoWindow();
                    }
                };
                LatLng ll = marker.getPosition();
                mInfoWindow = new InfoWindow(BitmapDescriptorFactory.fromView(button), ll, -47, listener);
                baiduMap.showInfoWindow(mInfoWindow);
                return true;
            }
        });



    }


    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    //获取wifi ip 和 mac
    public String getLocalMacAddress() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启,wifi未开启时，返回的ip为0.0.0.0
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String Ip = intToIp(wifiInfo.getIpAddress());
        String Mac = wifiInfo.getMacAddress();
        return "WiFi->Ip:" + Ip + "\nWiFi->Mac:" + Mac;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onResume();
        baiduMap.setMyLocationEnabled(false);

        // 回收 bitmap 资源
        bdA.recycle();
        bdB.recycle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    //申请自定位
    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }

    //初始化自定位相关设置
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        mLocationClient.setLocOption(option);
    }

    private void navigateToMe(){
        if (myLocation != null){
            navigateTo(myLocation);
        }
    }

    //显示location所在区域
    private void navigateTo(BDLocation location){

//            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//            baiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomTo(16f);
//            baiduMap.animateMapStatus(update);
//            isFirstLocate = false;

//            Toast.makeText(this, "nav to " + location.getAddrStr(), Toast.LENGTH_SHORT).show();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(13.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));


        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }



    /**
     * 添加点、线、多边形、圆、文字
     */
    public void addCustomElementsDemo(BDLocation mystation) {
        //定义坐标点
        LatLng p111 = new LatLng(mystation.getLatitude()+0.2, mystation.getLongitude());
        LatLng p211 = new LatLng(mystation.getLatitude()+0.1, mystation.getLongitude());
        LatLng p311 = new LatLng(mystation.getLatitude(), mystation.getLongitude()+0.1);
        LatLng p411 = new LatLng(mystation.getLatitude()-0.1, mystation.getLongitude());

        // 添加文字标示
        List<OverlayOptions> overlayList = new ArrayList<OverlayOptions>();
        overlayList.add(new TextOptions().bgColor(0x00000000).typeface(Typeface.DEFAULT_BOLD)
                .fontSize(40).fontColor(0xFF000000).text("                设备A").position(p111));
        overlayList.add(new TextOptions().bgColor(0x00000000).typeface(Typeface.DEFAULT_BOLD)
                .fontSize(40).fontColor(0xFF000000).text("                设备B").position(p211));
        overlayList.add(new TextOptions().bgColor(0x00000000).typeface(Typeface.DEFAULT_BOLD)
                .fontSize(40).fontColor(0xFF000000).text("                设备C").position(p311));
        overlayList.add(new TextOptions().bgColor(0x00000000).typeface(Typeface.DEFAULT_BOLD)
                .fontSize(40).fontColor(0xFF000000).text("                设备D").position(p411));
        baiduMap.addOverlays(overlayList);

        // 添加多纹理分段的折线绘制
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        BitmapDescriptor mHalfRedTexture = BitmapDescriptorFactory.fromAsset("icon_road_halfred_arrow.png");
        BitmapDescriptor mHalfGreenTexture = BitmapDescriptorFactory.fromAsset("icon_road_halfgreen_arrow.png");
        BitmapDescriptor mHalfYellowTexture = BitmapDescriptorFactory.fromAsset("icon_road_halfyellow_arrow.png");
        textureList.add(mHalfRedTexture);
        textureList.add(mHalfGreenTexture);
        textureList.add(mHalfYellowTexture);

        List<LatLng> points11 = new ArrayList<LatLng>();
        points11.add(p111);
        points11.add(p211);
        points11.add(p311);
        points11.add(p411);
        List<Integer> textureIndexs = new ArrayList<Integer>();
        textureIndexs.add(1);
        textureIndexs.add(1);
        textureIndexs.add(2);
        OverlayOptions ooPolyline11 = new PolylineOptions().width(20)
                .points(points11)
                .dottedLine(true)
                .customTextureList(textureList).textureIndex(textureIndexs);
        mTexturePolyline = (Polyline) baiduMap.addOverlay(ooPolyline11);


        List<LatLng> points22 = new ArrayList<LatLng>();
        points22.add(p411);
        points22.add(p311);
        points22.add(p211);
        points22.add(p111);
        OverlayOptions ooPolyline22 = new PolylineOptions().width(20)
                .points(points22)
                .dottedLine(true)
                .customTextureList(textureList).textureIndex(textureIndexs);
        mTexturePolyline = (Polyline) baiduMap.addOverlay(ooPolyline22);

        List<LatLng> points33 = new ArrayList<LatLng>();
        points33.add(p111);
        points33.add(p311);
        OverlayOptions ooPolyline33 = new PolylineOptions().width(20)
                .points(points33)
                .dottedLine(true).customTexture(mHalfRedTexture);
        mTexturePolyline = (Polyline) baiduMap.addOverlay(ooPolyline33);

        List<LatLng> points44 = new ArrayList<LatLng>();
        points44.add(p311);
        points44.add(p111);
        OverlayOptions ooPolyline44 = new PolylineOptions().width(20)
                .points(points44).dottedLine(true).customTexture(mHalfRedTexture);
        mTexturePolyline = (Polyline) baiduMap.addOverlay(ooPolyline44);


        //增加图标
        MarkerOptions ooA = new MarkerOptions().position(p111).icon(bdA).anchor(0.7f, 0.5f);
//        ooA.animateType(MarkerOptions.MarkerAnimateType.grow);// 掉下动画
        mMarkerA = (Marker) (baiduMap.addOverlay(ooA));
        MarkerOptions ooB = new MarkerOptions().position(p211).icon(bdA).anchor(0.7f, 0.5f);
        mMarkerB = (Marker) (baiduMap.addOverlay(ooB));
        MarkerOptions ooC = new MarkerOptions().position(p311).icon(bdB).anchor(0.7f, 0.5f);
        mMarkerC = (Marker) (baiduMap.addOverlay(ooC));
        MarkerOptions ooD = new MarkerOptions().position(p411).icon(bdB).anchor(0.7f, 0.5f);
        mMarkerD = (Marker) (baiduMap.addOverlay(ooD));


        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            public void onMarkerDrag(Marker marker) {
            }

            public void onMarkerDragEnd(Marker marker) {
                Toast.makeText(
                        MainActivity.this,
                        "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
                                + marker.getPosition().longitude,
                        Toast.LENGTH_LONG).show();
            }

            public void onMarkerDragStart(Marker marker) {
            }
        });


//        // 添加普通折线绘制
//        LatLng p1 = new LatLng(31.37923, 121.357428);
//        LatLng p2 = new LatLng(31.34923, 121.397428);
//        LatLng p3 = new LatLng(31.37923, 121.437428);
//        List<LatLng> points = new ArrayList<LatLng>();
//        points.add(p1);
//        points.add(p2);
//        points.add(p3);
//        OverlayOptions ooPolyline = new PolylineOptions().width(10)
//                .color(0x00FF0000)
//                .points(points);
//        mPolyline = (Polyline)baiduMap.addOverlay(ooPolyline);


//        // 添加多颜色分段的折线绘制
//        LatLng p11 = new LatLng(31.365, 121.444);
//        LatLng p21 = new LatLng(31.325, 121.494);
//        LatLng p31 = new LatLng(31.355, 121.534);
//        LatLng p41 = new LatLng(31.305, 121.594);
//        LatLng p51 = new LatLng(31.365, 121.644);
//        List<LatLng> points1 = new ArrayList<LatLng>();
//        points1.add(p11);
//        points1.add(p21);
//        points1.add(p31);
//        points1.add(p41);
//        points1.add(p51);
//        List<Integer> colorValue = new ArrayList<Integer>();
//        colorValue.add(0xAAFF0000);
//        colorValue.add(0xAA00FF00);
//        colorValue.add(0xAA0000FF);
//        OverlayOptions ooPolyline1 = new PolylineOptions().width(10)
//                .color(0xAAFF0000).points(points1).colorsValues(colorValue);
//        mColorfulPolyline = (Polyline) baiduMap.addOverlay(ooPolyline1);



//        // 添加弧线
//        OverlayOptions ooArc = new ArcOptions().color(0xAA00FF00).width(4)
//                .points(p1, p2, p3);
//        baiduMap.addOverlay(ooArc);
//
//        // 添加圆
//        LatLng llCircle = new LatLng(31.30923, 121.447428);
//        OverlayOptions ooCircle = new CircleOptions().fillColor(0x000000FF)
//                .center(llCircle).stroke(new Stroke(5, 0xAA000000))
//                .radius(1400);
//        baiduMap.addOverlay(ooCircle);
//
//        LatLng llDot = new LatLng(31.38923, 121.397428);
//        OverlayOptions ooDot = new DotOptions().center(llDot).radius(6)
//                .color(0xFF0000FF);
//        baiduMap.addOverlay(ooDot);
//
//        // 添加多边形
//        LatLng pt1 = new LatLng(31.33923, 121.357428);
//        LatLng pt2 = new LatLng(31.31923, 121.327428);
//        LatLng pt3 = new LatLng(31.29923, 121.347428);
//        LatLng pt4 = new LatLng(31.29923, 121.367428);
//        LatLng pt5 = new LatLng(31.31923, 121.387428);
//        List<LatLng> pts = new ArrayList<LatLng>();
//        pts.add(pt1);
//        pts.add(pt2);
//        pts.add(pt3);
//        pts.add(pt4);
//        pts.add(pt5);
//        OverlayOptions ooPolygon = new PolygonOptions().points(pts)
//                .stroke(new Stroke(5, 0xAA00FF00)).fillColor(0xAAFFFF00);
//        baiduMap.addOverlay(ooPolygon);
//
//        // 添加文字
//        LatLng llText = new LatLng(31.26923, 121.397428);
//        OverlayOptions ooText = new TextOptions().bgColor(0xAA000000)
//                .fontSize(24).fontColor(0xFFFFFFFF).text("百度地图SDK")
//                .rotate(-30)
//                .position(llText);
//        baiduMap.addOverlay(ooText);

    }

    /**
     * 清除所有Overlay
     *
     * @param view
     */
    public void clearOverlay(View view) {
        baiduMap.clear();
        mMarkerA = null;
        mMarkerB = null;
        mMarkerC = null;
        mMarkerD = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for (int result:grantResults){
                        if (result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意", Toast.LENGTH_SHORT).show();
                            finish();
                            return;

                        }
                    }
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public class MyLocationListener implements BDLocationListener{
        BDLocation lastlocation = null;
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (lastlocation != null
                    && lastlocation.getLatitude() == bdLocation.getLatitude()
                    && lastlocation.getLongitude() == bdLocation.getLongitude()){
                return;
            }else{
                lastlocation = bdLocation;
            }

            //移动地图
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    ||bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            }
            if (myLocation==null){
                myLocation = new BDLocation();
            }
            myLocation.setLatitude(bdLocation.getLatitude());
            myLocation.setLongitude(bdLocation.getLongitude());
            if (isFirstLocate) {
                navigateToMe();
                isFirstLocate = false;
            }

                //显示文字信息
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append(" ");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
            currentPosition.append(bdLocation.getCountry()).append(" ");
            currentPosition.append(bdLocation.getProvince()).append(" ");
            currentPosition.append(bdLocation.getCity()).append(" ");
            currentPosition.append(bdLocation.getDistrict()).append(" ");
            currentPosition.append(bdLocation.getStreet()).append("\n");

            currentPosition.append("定位方式：");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else {
                currentPosition.append("网络");
            }
            positionTest.setText(currentPosition);

            //添加折线
            clearOverlay(null);
            addCustomElementsDemo(bdLocation);
        }
    }


}
