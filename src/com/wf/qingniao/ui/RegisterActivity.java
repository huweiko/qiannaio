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
	// ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPSECRET
	private static String APPSECRET = "129d8b5cbc75678b87f3f90d55869b38";
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btnVerify:// ��ȡ��֤�� 
			getVerifyCode();
			break;
		case R.id.btnSubmit: // �ύ������Ϣ
			String phoneverify = phoneVerify.getText().toString().trim();
			if (!TextUtils.isEmpty(phoneverify)) {
				SMSSDK.submitVerificationCode("86", phonenumber,phoneverify);
				
			}else {
				Toast.makeText(getActivity(), "��֤�벻��Ϊ��", 0).show();
				return;
			}
			
			break;
		case R.id.tvBirthday: // ��ȡ����
			WheelViewUtil.showWheelView(v.getContext(), (View)tvBirthday, ageListener, 
					getNowYear(), "ѡ������", false);
			break;
		case R.id.tvLocation: // ��ȡλ��
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
		
		setTitle("ע��");
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
	
			SMSSDK.registerEventHandler(eventHandler); // ע����Żص�
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	protected void onDestroy() {
		
		// ���ٻص������ӿ�
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
	 * ��ȡ��֤��
	 */
	private void getVerifyCode() {
		// ��ȡ֧�ַ�����֤��Ĺ����б�
		// Ŀǰ�й��϶�֧�ֵģ����ԾͲ���֤�й��ˡ�
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
			//����ע��ɹ��󣬷���MainActivity,Ȼ����ʾ�º���
			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//�ύ��֤��ɹ�
				SubmitAllUserInfo();
				//register(user, pwd, usernum, gjz, deviceNum);
			} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
				TostHelper.ToastSht(R.string.verify_code_success, getApplicationContext());
//						textView2.setText("��֤���Ѿ�����");
				cancelRequestDialog();
			}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//����֧�ַ�����֤��Ĺ����б�
				TostHelper.ToastSht(R.string.get_country_list_success, getApplicationContext());
				cancelRequestDialog();
			}
		} else {
			((Throwable) data).printStackTrace();
			TostHelper.ToastSht(R.string.verify_code_error, RegisterActivity.this);
			cancelRequestDialog();
		}
	}
	//�ύ�û���Ϣ
	private	void SubmitAllUserInfo(){
			
		// �Ա�
		String gender = "2";
		if(rgGender.getCheckedRadioButtonId() == R.id.rbMale){
			gender = "1";
		}
		mStructBaseUserInfo.setSex(gender);
		
		// λ��
		String detailAdd = tvLocation.getText().toString();
		if(TextUtils.isEmpty(detailAdd)){
			TostHelper.ToastSht(R.string.please_get_location, getActivity());
			return;
		}
		
		// λ��
		if(mLocation != null){
			TostHelper.ToastSht(R.string.please_get_location, getActivity());
			return;
		}
		mStructBaseUserInfo.setProvince(mLocation.getProvince());
		mStructBaseUserInfo.setCity(mLocation.getCity());
		mStructBaseUserInfo.setDistrict(mLocation.getDistrict());
		// ���� 
		String birthday = tvBirthday.getText().toString();
		if(TextUtils.isEmpty(birthday)){
			TostHelper.ToastSht(R.string.choose_birthday, getActivity());
			return;
		}
		mStructBaseUserInfo.setBirthday(birthday);
		// �ǳ�
		String nickName = etUserName.getText().toString();
		if(TextUtils.isEmpty(nickName)){
			TostHelper.ToastSht(R.string.please_input_nickname, getActivity());
			return;
		}
		mStructBaseUserInfo.setNickname(nickName);
	}
	/* ע��ӿ�
	 * */
	 private void register(final String user, final String pwd,final String usernum,final String gjz, final String deviceNum) {

	        // ��½
	        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pwd)){
	            TostHelper.ToastLg( "�û���������Ϊ��",getBaseContext());
	            return;
	        }
	        final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("ע����...");
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
	    					
	    					if(response.getResult()){//ע��ɹ�
	    						TostHelper.ToastLg(response.getMessage(), getActivity());
	    					}else if(response.getCode().equals("E01")){//���û����Ѿ�ע��
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
			//����ע��ɹ��󣬷���MainActivity,Ȼ����ʾ�º���
			if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//�ύ��֤��ɹ�
				SubmitAllUserInfo();
				//register(user, pwd, usernum, gjz, deviceNum);
			} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
				TostHelper.ToastSht(R.string.verify_code_success, getApplicationContext());
//						textView2.setText("��֤���Ѿ�����");
				cancelRequestDialog();
			}else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//����֧�ַ�����֤��Ĺ����б�
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
