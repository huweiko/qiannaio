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
		SDKInitializer.initialize(this);//�ٶȵ�ͼ��λ
		PgyCrashManager.register(this,Constant.PgyerAPPID);// �����ѹ�ӢsdkӦ�õ�appId
	}
	
	
}