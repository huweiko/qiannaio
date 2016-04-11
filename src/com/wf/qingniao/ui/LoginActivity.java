package com.wf.qingniao.ui;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.pgyersdk.update.PgyUpdateManager;
import com.wf.qingniao.R;
import com.wf.qingniao.bean.Constant;
import com.wf.qingniao.bean.Constant.XPreference;
import com.wf.qingniao.bean.Response;
import com.wf.qingniao.bean.Urls;
import com.wf.qingniao.bean.UserInfo;
import com.wf.qingniao.utils.NetworkUtils;
import com.wf.qingniao.utils.TostHelper;
import com.wf.qingniao.view.InputDialog;

public class LoginActivity extends BaseActivity {

	EditText etUserName, etPwd;
	Button btnSubmit;
	public static UserInfo mUserInfo = new UserInfo();
	private Context context;
	String uname, pwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PgyUpdateManager.register(this,Constant.PgyerAPPID);
		setContentView(R.layout.activity_login);
		this.context = this;
		initView();
	}

	private void initView() {
		uname = preferences.getString(XPreference.UNAME, null);
		pwd = preferences.getString(XPreference.PWD, null);
		etUserName = (EditText)findViewById(R.id.etUserName);
		etPwd = (EditText)findViewById(R.id.etPwd);
		btnSubmit = (Button)findViewById(R.id.btnSubmit);
		 
		etUserName.setText(uname);
		etPwd.setText(pwd);
		setTitle(R.string.app_name);
		addRightBtn(R.string.register, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), RegisterActivity.class);
				startActivity(intent);
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String userName = etUserName.getText().toString().replace(" ", "");
				String passwd = etPwd.getText().toString().replace(" ", "");
				if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(passwd)){
					Toast.makeText(v.getContext(), "用户名或密码为空", Toast.LENGTH_SHORT).show();
					return;
				} else {
					saveUser(userName,passwd);
					login(userName, passwd);
//					String t = AssetUtils.getDataFromAssets(context, "login.txt");
//					parseData(t);
				}
				
			}
		});
	}
	public void saveUser(String userName, String passwd) {
		preferences.edit().putString(XPreference.UNAME, userName).commit();
		preferences.edit().putString(XPreference.PWD, passwd).commit();
	}

	 /* 登录接口
	  * */
	private void login(String userName, String pwd) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("登录中...");
		dialog.show();
		
		/*AjaxParams param = new AjaxParams();
		param.put("buss", "login");
		param.put("username", userName);
		param.put("password", pwd);
		
		getFinalHttp().get(Urls.SERVER_IP,param, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				parseData(t);
				dialog.cancel();
			}

			private void parseData(String t) {
				Response<List<UserInfo>> response = new Gson().fromJson(t, 
						new TypeToken<Response<List<UserInfo>>>(){}.getType());
				
				if(response.getResult()){
					List<UserInfo> listUserInfo = response.getResponse();
					mUserInfo = listUserInfo.get(0);
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);
					startActivity(intent);
				}else{
					TostHelper.ToastLg(response.getMessage(), getActivity());
				}
			}

			@Override
			public void onFailure(Throwable t, 
					String strMsg) {
				super.onFailure(t, strMsg);
				Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT).show();
				dialog.cancel();
			}
			
		});*/
		RequestParams params = new RequestParams();
		params.addBodyParameter("buss", "login");
		params.addBodyParameter("username", userName);
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
        				Response<List<UserInfo>> response = new Gson().fromJson(t, 
        						new TypeToken<Response<List<UserInfo>>>(){}.getType());
        				
        				if(response.getResult()){
        					List<UserInfo> listUserInfo = response.getResponse();
        					mUserInfo = listUserInfo.get(0);
        					Intent intent = new Intent();
        					intent.setClass(LoginActivity.this, MainActivity.class);
        					startActivity(intent);
        				}else{
        					TostHelper.ToastLg(response.getMessage(), getActivity());
        				}
        			}
                    @Override
                    public void onFailure(HttpException error, String msg) {
                    	Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        				dialog.cancel();
                    }
                });
		
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PgyUpdateManager.unregister();
	}
	
}
