package com.wf.qingniao.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wf.qingniao.R;
import com.wf.qingniao.bean.Constant;
import com.wf.qingniao.bean.MyLocation;
import com.wf.qingniao.bean.Response;
import com.wf.qingniao.bean.StructBaseUserInfo;
import com.wf.qingniao.bean.Urls;
import com.wf.qingniao.service.GpsLocation;
import com.wf.qingniao.service.GpsLocation.LocationListener;
import com.wf.qingniao.utils.TostHelper;
import com.wf.qingniao.view.WheelViewUtil;
import com.wf.qingniao.view.WheelViewUtil.OnWheelViewListener;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity implements OnClickListener,Callback{
	@ViewInject(R.id.etUserName)
	private EditText etUserName;
	
	@ViewInject(R.id.rgGender)
	private RadioGroup rgGender;
	
	@ViewInject(R.id.tvLocation)
	private TextView tvLocation;
	
	@ViewInject(R.id.tvBirthday)
	private TextView tvBirthday;
	
	@ViewInject(R.id.etPwd)
	private EditText etPwd;

	@ViewInject(R.id.etConfirmPwd)
	private EditText etConfirmPwd;
	
	@ViewInject(R.id.phonEditText)
	private EditText phonEditText;
	
	@ViewInject(R.id.phoneVerify)
	private EditText phoneVerify;
	
	@ViewInject(R.id.btnVerify)
	private Button btnVerify;
	
	@ViewInject(R.id.btnSubmit)
	private Button btnSubmit;
	
	
	protected static final int VERIFY_CODE = 10;
	StructBaseUserInfo mStructBaseUserInfo = new StructBaseUserInfo();
	MyLocation mLocation = null;
	private String phonenumber;
	
	private static String APPKEY = "10900ea2c087c";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "129d8b5cbc75678b87f3f90d55869b38";
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnVerify:// 获取验证码 
			getVerifyCode();
			break;
		case R.id.btnSubmit: // 提交个人信息
			String phoneverify = phoneVerify.getText().toString().trim();
			if (!TextUtils.isEmpty(phoneverify)) {
				SMSSDK.submitVerificationCode("86", phonenumber,phoneverify);
				
			}else {
				Toast.makeText(getActivity(), "验证码不能为空", 0).show();
				return;
			}
			
			break;
		case R.id.tvBirthday: // 获取生日
			WheelViewUtil.showWheelView(v.getContext(), (View)tvBirthday, ageListener, 
					getNowYear(), "选择生日", false);
			break;
		case R.id.tvLocation: // 获取位置
			showReqeustDialog(R.string.location);
			GpsLocation mGpsLocation = new GpsLocation(getActivity());
			mGpsLocation.startLocation(listener);
			break;
		}
	}
	public String getNowYear() {
		   Date currentTime = new Date();
		   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		   String dateString = formatter.format(currentTime);
		   return dateString;
		}
	OnWheelViewListener ageListener = new OnWheelViewListener() {
		
		@Override
		public void doubleConfirm(String selectLeft, String selectRight) {
			 
		}
		
		@Override
		public void dateConfirm(String selectyear, String selectmonth,
				String selectday, int year, int month, int day) {
			 tvBirthday.setText(selectyear + "-" + selectmonth + "-" + selectday);
		}
		
		@Override
		public void Confirm(String select, int index) {
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setTitle("注册");
		initLocation();
		InitView();
		initSDK();
	}
	
	private void initSDK() {
		try {
			SMSSDK.initSDK(this,APPKEY,APPSECRET,true);
			final Handler handler = new Handler(this);
			EventHandler eventHandler = new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
					Message msg = new Message();
					msg.arg1 = event;
					msg.arg2 = result;
					msg.obj = data;
					handler.sendMessage(msg);
				}
			};
	
			SMSSDK.registerEventHandler(eventHandler); // 注册短信回调
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	protected void onDestroy() {
		
		// 销毁回调监听接口
		SMSSDK.unregisterAllEventHandler();
		super.onDestroy();

	}
	void InitView(){
		btnVerify.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		tvBirthday.setOnClickListener(this);
		tvLocation.setOnClickListener(this);
	}
	private void initLocation() {
		
		showReqeustDialog(R.string.location);
		GpsLocation mGpsLocation = new GpsLocation(getActivity());
		mGpsLocation.startLocation(listener);
	}
	
	LocationListener listener = new LocationListener() {
			
			@Override
			public void onReceiveLocation(MyLocation myLocation) {
				mLocation = myLocation;
				cancelRequestDialog();
				tvLocation.setText(myLocation.getProvince()+myLocation.getCity()+myLocation.getDistrict());
			}
	};
	/**
	 * 获取验证码
	 */
	private void getVerifyCode() {
		// 获取支持发送验证码的国家列表
		// 目前中国肯定支持的，所以就不验证中国了。
		// SMSSDK.getSupportedCountries();
		phonenumber = phonEditText.getText().toString().trim();
		if(TextUtils.isEmpty(phonenumber)){
			TostHelper.ToastSht(R.string.input_phone_number, getActivity());
			return;
		}
		
		if(phonenumber.length() < 6 || phonenumber.length() > 16){
			TostHelper.ToastSht(R.string.phone_length_limit, getActivity());
			return;
		}
		
		showReqeustDialog(R.string.send_verify_code);
		SMSSDK.getVerificationCode("86",phonenumber);
	}
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case VERIFY_CODE:
				setMessageVerify(msg);
				break;
			default:
				break;
			}
		}
	};
	EventHandler event = new EventHandler(){
		
		  @Override
        public void afterEvent(int event, int result, Object data) {
			  
			  Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				msg.what = VERIFY_CODE;
				mHandler.sendMessage(msg);
		  }
	};
	private void setMessageVerify(Message msg) {
		int event =msg.arg1;
		int result=  msg.arg2;
		Object data = msg.obj;
		
		if (result == SMSSDK.RESULT_COMPLETE) {
			//短信注册成功后，返回MainActivity,然后提示新好友
			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
				SubmitAllUserInfo();
				//register(user, pwd, usernum, gjz, deviceNum);
			} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
				TostHelper.ToastSht(R.string.verify_code_success, getApplicationContext());
//						textView2.setText("验证码已经发送");
				cancelRequestDialog();
			}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
				TostHelper.ToastSht(R.string.get_country_list_success, getApplicationContext());
				cancelRequestDialog();
			}
		} else {
			((Throwable) data).printStackTrace();
			TostHelper.ToastSht(R.string.verify_code_error, RegisterActivity.this);
			cancelRequestDialog();
		}
	}
	//提交用户信息
	private	void SubmitAllUserInfo(){
			
		// 性别
		String gender = "2";
		if(rgGender.getCheckedRadioButtonId() == R.id.rbMale){
			gender = "1";
		}
		mStructBaseUserInfo.setSex(gender);
		
		// 位置
		String detailAdd = tvLocation.getText().toString();
		if(TextUtils.isEmpty(detailAdd)){
			TostHelper.ToastSht(R.string.please_get_location, getActivity());
			return;
		}
		
		// 位置
		if(mLocation != null){
			TostHelper.ToastSht(R.string.please_get_location, getActivity());
			return;
		}
		mStructBaseUserInfo.setProvince(mLocation.getProvince());
		mStructBaseUserInfo.setCity(mLocation.getCity());
		mStructBaseUserInfo.setDistrict(mLocation.getDistrict());
		// 生日 
		String birthday = tvBirthday.getText().toString();
		if(TextUtils.isEmpty(birthday)){
			TostHelper.ToastSht(R.string.choose_birthday, getActivity());
			return;
		}
		mStructBaseUserInfo.setBirthday(birthday);
		// 昵称
		String nickName = etUserName.getText().toString();
		if(TextUtils.isEmpty(nickName)){
			TostHelper.ToastSht(R.string.please_input_nickname, getActivity());
			return;
		}
		mStructBaseUserInfo.setNickname(nickName);
	}
	/* 注册接口
	 * */
	 private void register(final String user, final String pwd,final String usernum,final String gjz, final String deviceNum) {

	        // 登陆
	        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)){
	            TostHelper.ToastLg( "用户名或密码为空",getBaseContext());
	            return;
	        }
	        final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("注册中...");
			dialog.show();
			RequestParams params = new RequestParams();
			params.addBodyParameter("buss", "reg");
			params.addBodyParameter("username", user);
			params.addBodyParameter("password", pwd);
			getHttpUtils().send(HttpRequest.HttpMethod.POST,
					Urls.SERVER_IP,
	                params,
	                new RequestCallBack<String>() {

	                    @Override
	                    public void onStart() {
	                    }

	                    @Override
	                    public void onLoading(long total, long current, boolean isUploading) {
	                    }

	                    @Override
	                    public void onSuccess(ResponseInfo<String> responseInfo) {
	                    	parseData(responseInfo.result);
	        				dialog.cancel();
	                    }
	                    private void parseData(String t) {
	    					Response<?> response = new Gson().fromJson(t, 
	    							new TypeToken<Response<?>>(){}.getType());
	    					
	    					if(response.getResult()){//注册成功
	    						TostHelper.ToastLg(response.getMessage(), getActivity());
	    					}else if(response.getCode().equals("E01")){//该用户名已经注册
	    						TostHelper.ToastLg(response.getMessage(), getActivity());
	    					}else{
	    						TostHelper.ToastLg(response.getMessage(), getActivity());
	    					}
	    				}
	                    @Override
	                    public void onFailure(HttpException error, String msg) {
	                    	Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	        				dialog.cancel();
	                    }
	                });
	    }

	@Override
	public boolean handleMessage(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
		if (result == SMSSDK.RESULT_COMPLETE) {
			//短信注册成功后，返回MainActivity,然后提示新好友
			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
				SubmitAllUserInfo();
				//register(user, pwd, usernum, gjz, deviceNum);
			} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
				TostHelper.ToastSht(R.string.verify_code_success, getApplicationContext());
//						textView2.setText("验证码已经发送");
				cancelRequestDialog();
			}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
				TostHelper.ToastSht(R.string.get_country_list_success, getApplicationContext());
				cancelRequestDialog();
			}
		} else {
			((Throwable) data).printStackTrace();
			TostHelper.ToastSht(R.string.verify_code_error, RegisterActivity.this);
			cancelRequestDialog();
		}
		 return false;
	}
}
