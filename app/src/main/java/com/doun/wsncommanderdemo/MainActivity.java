package com.doun.wsncommanderdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public LocationClient mLocationClient;
    private TextView positionTest;
    private MapView mapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate=true;

    // 普通折线，点击时改变宽度
    Polyline mPolyline;
    // 多颜色折线，点击时消失
    Polyline mColorfulPolyline;
    // 纹理折线，点击时获取折线上点数及width
    Polyline mTexturePolyline;
<<<<<<< HEAD


    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    ||bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            }
            navigateTo(bdLocation);
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append(" ");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
            currentPosition.append("国家：").append(bdLocation.getCountry()).append(" ");
            currentPosition.append("省：").append(bdLocation.getProvince()).append(" ");
            currentPosition.append("市：").append(bdLocation.getCity()).append(" ");
            currentPosition.append("区：").append(bdLocation.getDistrict()).append(" ");
            currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
=======
>>>>>>> 4e748727ac3efb235efc7ad87792c0471c6bb6c4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onResume();
        baiduMap.setMyLocationEnabled(false);
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

    //显示location所在区域
    private void navigateTo(BDLocation location){
        if (isFirstLocate){
//            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
//            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
//            baiduMap.animateMapStatus(update);
//            update = MapStatusUpdateFactory.zoomTo(16f);
//            baiduMap.animateMapStatus(update);
//            isFirstLocate = false;

            Toast.makeText(this, "nav to " + location.getAddrStr(), Toast.LENGTH_SHORT).show();
            isFirstLocate = false;
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
<<<<<<< HEAD
            builder.target(ll).zoom(12.0f);
=======
            builder.target(ll).zoom(13.0f);
>>>>>>> 4e748727ac3efb235efc7ad87792c0471c6bb6c4
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        }
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
        LatLng p111 = new LatLng(mystation.getLatitude(), mystation.getLongitude());
        LatLng p211 = new LatLng(mystation.getLatitude()+0.1, mystation.getLongitude());
        LatLng p311 = new LatLng(mystation.getLatitude(), mystation.getLongitude()+0.1);
        LatLng p411 = new LatLng(mystation.getLatitude()-0.1, mystation.getLongitude());

        // 添加文字标示
        List<OverlayOptions> overlayList = new ArrayList<OverlayOptions>();
        overlayList.add(new TextOptions().bgColor(0xAA000000).fontSize(48).fontColor(0xFFFFFFFF).text("设备A").position(p111));
        overlayList.add(new TextOptions().bgColor(0xAA000000).fontSize(48).fontColor(0xFFFFFFFF).text("设备B").position(p211));
        overlayList.add(new TextOptions().bgColor(0xAA000000).fontSize(48).fontColor(0xFFFFFFFF).text("设备C").position(p311));
        overlayList.add(new TextOptions().bgColor(0xAA000000).fontSize(48).fontColor(0xFFFFFFFF).text("设备D").position(p411));
        baiduMap.addOverlays(overlayList);

        // 添加多纹理分段的折线绘制

        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        BitmapDescriptor mRedTexture = BitmapDescriptorFactory.fromAsset("icon_road_red_arrow.png");
        BitmapDescriptor mGreenTexture = BitmapDescriptorFactory.fromAsset("icon_road_green_arrow.png");
        BitmapDescriptor mYellowTexture = BitmapDescriptorFactory.fromAsset("icon_road_yellow_arrow.png");
        BitmapDescriptor mHalfRedTexture = BitmapDescriptorFactory.fromAsset("icon_road_halfred_arrow.png");
        BitmapDescriptor mHalfGreenTexture = BitmapDescriptorFactory.fromAsset("icon_road_halfgreen_arrow.png");
        BitmapDescriptor mHalfYellowTexture = BitmapDescriptorFactory.fromAsset("icon_road_halfyellow_arrow.png");
        textureList.add(mRedTexture);
        textureList.add(mGreenTexture);
        textureList.add(mYellowTexture);
        textureList.add(mHalfRedTexture);
        textureList.add(mHalfGreenTexture);
        textureList.add(mHalfYellowTexture);

        List<LatLng> points11 = new ArrayList<LatLng>();
        points11.add(p111);
        points11.add(p211);
        points11.add(p311);
        points11.add(p411);
        List<Integer> textureIndexs = new ArrayList<Integer>();
        textureIndexs.add(4);
        textureIndexs.add(4);
        textureIndexs.add(5);
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

<<<<<<< HEAD
        positionTest = (TextView) findViewById(R.id.position_text_view);
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

        addCustomElementsDemo();

    }

    /**
     * 添加点、线、多边形、圆、文字
     */
    public void addCustomElementsDemo() {
        // 添加多纹理分段的折线绘制
        LatLng p111 = new LatLng(31.265, 121.444);
        LatLng p211 = new LatLng(31.45, 121.494);
        LatLng p311 = new LatLng(31.255, 121.534);
        LatLng p411 = new LatLng(31.205, 121.594);
        List<LatLng> points11 = new ArrayList<LatLng>();
        points11.add(p111);
        points11.add(p211);
        points11.add(p311);
        points11.add(p411);
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();

        BitmapDescriptor mRedTexture = BitmapDescriptorFactory.fromAsset("icon_road_red_arrow.png");
        BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");
        BitmapDescriptor mGreenTexture = BitmapDescriptorFactory.fromAsset("icon_road_green_arrow.png");
        BitmapDescriptor mYellowTexture = BitmapDescriptorFactory.fromAsset("icon_road_yellow_arrow.png");
        BitmapDescriptor mmyTexture = BitmapDescriptorFactory.fromAsset("compass_arrow.png");

        textureList.add(mRedTexture);
        textureList.add(mBlueTexture);
        textureList.add(mGreenTexture);
        textureList.add(mYellowTexture);
        textureList.add(mmyTexture);
        List<Integer> textureIndexs = new ArrayList<Integer>();
        textureIndexs.add(1);
        textureIndexs.add(3);
        textureIndexs.add(3);
        OverlayOptions ooPolyline11 = new PolylineOptions().width(20)
                .points(points11)
                .dottedLine(true)
                .customTextureList(textureList).textureIndex(textureIndexs);
        mTexturePolyline = (Polyline) baiduMap.addOverlay(ooPolyline11);


        // 添加普通折线绘制
        LatLng p1 = new LatLng(31.37923, 121.357428);
        LatLng p2 = new LatLng(31.34923, 121.397428);
        LatLng p3 = new LatLng(31.37923, 121.437428);
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        OverlayOptions ooPolyline = new PolylineOptions().width(10)
                .color(0x00FF0000)
                .points(points);
        mPolyline = (Polyline)baiduMap.addOverlay(ooPolyline);


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



        // 添加文字
        LatLng llTextA = new LatLng(31.395064, 121.241406);
        OverlayOptions ooTextA = new TextOptions().bgColor(0xAA000000)
                .fontSize(24).fontColor(0xFFFFFFFF).text("设备A")
                .position(llTextA);
        baiduMap.addOverlay(ooTextA);
        // 添加文字
        LatLng llTextB = new LatLng(31.195064, 121.241406);
        OverlayOptions ooTextB = new TextOptions().bgColor(0xAA000000)
                .fontSize(24).fontColor(0xFFFFFFFF).text("设备B")
//                .rotate(-30)
                .position(llTextB);
        baiduMap.addOverlay(ooTextB);



=======
>>>>>>> 4e748727ac3efb235efc7ad87792c0471c6bb6c4
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
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //移动地图
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    ||bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            }
            navigateTo(bdLocation);

            //显示文字信息
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append(" ");
            currentPosition.append("经度：").append(bdLocation.getLongitude()).append("\n");
            currentPosition.append("国家：").append(bdLocation.getCountry()).append(" ");
            currentPosition.append("省：").append(bdLocation.getProvince()).append(" ");
            currentPosition.append("市：").append(bdLocation.getCity()).append(" ");
            currentPosition.append("区：").append(bdLocation.getDistrict()).append(" ");
            currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");

            currentPosition.append("定位方式：");
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                currentPosition.append("GPS");
            } else {
                currentPosition.append("网络");
            }
            positionTest.setText(currentPosition);

            //添加折线
            baiduMap.clear();
            addCustomElementsDemo(bdLocation);
        }
    }
}
