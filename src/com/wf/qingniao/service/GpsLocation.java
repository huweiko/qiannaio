package com.wf.qingniao.service;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.wf.qingniao.bean.MyLocation;

public class GpsLocation implements OnGetGeoCoderResultListener{
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private MyLocation myLocation = new MyLocation();
	private Context mContext;
	public interface LocationListener{
		public void onReceiveLocation(MyLocation myLocation);
	};
	
	LocationListener listener;
	public GpsLocation(Context context) {
		mContext = context;
	}
	private void locationInit() {
		// 定位初始化
		mLocClient = new LocationClient(mContext);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
	}
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location != null){
				myLocation.setLatitude(location.getLatitude());
				myLocation.setLongitude(location.getLongitude());
				
				if(myLocation.getLatitude() == 0.0 && myLocation.getLongitude() == 0.0){
					if(mLocClient.isStarted()){
						mLocClient.stop();
						mLocClient.unRegisterLocationListener(this);
					}
				} 
				LatLng ptCenter = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
				// 反Geo搜索
				mSearch.reverseGeoCode(new ReverseGeoCodeOption()
						.location(ptCenter));
				
				Log.d("huwei", "地理位置更新，纬度 = " + location.getLatitude()+"，经度 = "+location.getLongitude());
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	public void startLocation(LocationListener listener) {
		this.listener = listener;
		locationInit();
		if(!mLocClient.isStarted()){
			mLocClient.start();
		}
	}
	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		Log.d("qingniao", arg0.getAddress());
		myLocation.setProvince(arg0.getAddressDetail().province);
		myLocation.setCity(arg0.getAddressDetail().city);
		myLocation.setDistrict(arg0.getAddressDetail().district);
		if(listener != null){
			listener.onReceiveLocation(myLocation);
		}
	}
	
}
