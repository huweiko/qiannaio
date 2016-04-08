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
	private GeoCoder mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private MyLocation myLocation = new MyLocation();
	private static GpsLocation mGpsLocation;
	private GpsLocation() {
		
	}
	public static GpsLocation getInstance(){
		if(mGpsLocation == null){
			mGpsLocation = new GpsLocation();
		}
		return mGpsLocation;
	}
	public void locationInit(Context context) {
		// ��λ��ʼ��
		mLocClient = new LocationClient(context);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(5000);
		mLocClient.setLocOption(option);
		// ��ʼ������ģ�飬ע���¼�����
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		if(!mLocClient.isStarted()){
			mLocClient.start();
		}
	}
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view ���ٺ��ڴ����½��յ�λ��
			if (location != null){
				myLocation.setLatitude(location.getLatitude());
				myLocation.setLongitude(location.getLongitude());
				if(myLocation.getLatitude() == 0.0 && myLocation.getLongitude() == 0.0){
				}else{
					LatLng ptCenter = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
					// ��Geo����
					mSearch.reverseGeoCode(new ReverseGeoCodeOption()
							.location(ptCenter));	
				}
				Log.d("huwei", "����λ�ø��£�γ�� = " + location.getLatitude()+"������ = "+location.getLongitude());
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
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
	}
}
