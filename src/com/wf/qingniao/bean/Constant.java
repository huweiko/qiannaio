package com.wf.qingniao.bean;

import android.content.Context;
import android.content.SharedPreferences;

public class Constant {

	public static final long MIN_LOGINTIME = 0;
	public static final String PgyerAPPID="fcc1b6ca59e3713b5831b9bcbc350fa0";// 集成蒲公英sdk应用的appId
	public static final String MobSMSappkey = "116a1b153d620";
	public static final String MobSMSAppSecret = "22cf8d79f186d908ff2aec546951036f";
	public static class XPreference {

		public static final String UNAME = "UNAME";
		public static final String PWD = "PWD";

		public static SharedPreferences getSharedPreferences(Context context) {
			return context.getSharedPreferences("CloudContactSharePref", Context.MODE_PRIVATE);
		}
	}
}
