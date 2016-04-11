package com.wf.qingniao;

import com.baidu.mapapi.SDKInitializer;
import com.pgyersdk.crash.PgyCrashManager;
import com.wf.qingniao.bean.Constant;

import android.app.Application;

public class MyApplication extends Application{
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SDKInitializer.initialize(this);//百度地图定位
		PgyCrashManager.register(this,Constant.PgyerAPPID);// 集成蒲公英sdk应用的appId
	}
	
	
}