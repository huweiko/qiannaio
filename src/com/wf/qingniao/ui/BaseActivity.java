package com.wf.qingniao.ui;

import net.tsz.afinal.FinalHttp;

import com.lidroid.xutils.ViewUtils;
import com.wf.qingniao.R;
import com.wf.qingniao.bean.Constant.XPreference;
import com.wf.qingniao.view.RequestDialog;
import com.wf.qingniao.view.SelfDefineActionBar;
import com.wf.qingniao.view.SelfDefineActionBar.IProvideActionBar;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager; 

/**
 * @author 
 * 
 */
public class BaseActivity extends FragmentActivity implements
		IProvideActionBar {
	public static String tag = BaseActivity.class.getSimpleName();
	public SharedPreferences preferences;
	protected SelfDefineActionBar actionBar;
	protected RequestDialog requestDialog;
	private FinalHttp mFinalHttp;
	@Override
	public SelfDefineActionBar getSelfDefActionBar() {
		actionBar = (SelfDefineActionBar) findViewById(R.id.tkActionBar);
		return actionBar;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
		tag = getClass().getSimpleName();
		// ��ֹ����
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (null == preferences) {
			preferences = XPreference.getSharedPreferences(this);
		} 
	}
	public Activity getActivity() {
		return this;
	}
	protected FinalHttp getFinalHttp() {
		if(mFinalHttp == null){
			mFinalHttp = new FinalHttp();
		}
		return mFinalHttp;
	}
	/**
	 * ��ʾ�Ի���
	 * @param strId
	 */
	public void showReqeustDialog(int strId){
		if(requestDialog == null){
			requestDialog = new RequestDialog(this);
		}
		requestDialog.setCancelable(false);
		requestDialog.setMessage(getString(strId));
		
		if(!requestDialog.isShowing()){
			requestDialog.show();
		}
	}
	
	/**
	 * ȡ���Ի���
	 */
	public void cancelRequestDialog(){
		if(requestDialog != null && requestDialog.isShowing()){
			requestDialog.cancel();
		}
	}
	
	
	/**
	 * ��ӱ���
	 * 
	 * @param title
	 *            -- ����
	 * @param listener
	 *            -- �����¼�
	 */
	@Override
	public void setTitle(CharSequence title, OnClickListener listener) {
		getSelfDefActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title, listener);
		}
	}
	
	@Override
	public void setTitle(int strId) {
		getSelfDefActionBar();
		if (actionBar != null) {
			actionBar.setTitle(strId, null);
		}
	}
	@Override
	public void setTitle(CharSequence title) {
		getSelfDefActionBar();
		if (actionBar != null) {
			actionBar.setTitle(title, null);
		}
	}
 
 
	/**
	 * ��Ӷ�����������෵�ذ�ť
	 * 
	 * @param listener
	 *            ���Ϊnull����pass Activity activity
	 */
	protected void addBackBtn(int strId,View.OnClickListener listener) {
		getSelfDefActionBar();
		if (actionBar != null) {
			if (listener == null) {
				listener = new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				};
			}
			actionBar.addBackText(strId, listener);
		}
	}

	/**
	 * ��Ӷ�����������෵�ذ�ť
	 * 
	 * @param listener
	 */
	protected void addBackImage(int drawId, OnClickListener listener) {
		getSelfDefActionBar();
		if (actionBar != null) {
			if (listener == null) {
				listener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				};
			}
			actionBar.addBackImage(drawId, listener);
		}
	}
	
	/**
	 * ��Ӷ�����������෵�ذ�ť
	 * 
	 * @param listener
	 *            ���Ϊnull����pass Activity activity
	 */
	protected void addRightBtn(int strId, OnClickListener listener) {
		getSelfDefActionBar();
		if (actionBar != null) {
			if (listener == null) {
				listener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				};
			}
			actionBar.addRightText(strId, listener);
		}
	}

	/**
	 * ��Ӷ�����������෵�ذ�ť
	 * 
	 * @param listener
	 */
	protected void addRightImage(int drawId, OnClickListener listener) {
		getSelfDefActionBar();
		if (actionBar != null) {
			if (listener == null) {
				listener = new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				};
			}
			actionBar.addRightImage(drawId, listener);
		}
	}



	/**
	 * �������뷨
	 */
	protected void hideInput() {
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
//
//	/**
//	 * ���ذ�ť
//	 * 
//	 * @param view
//	 */
//	public void onBackClick(View view) {
//		this.finish();
//	}
//
	/**
	 * ���ʹ��actionbar����Ҫ��������һ�������������actionbar�����ĵ�id����
	 */
	@Override
	public void setupSelfDefineActionBar(int resId) {
		actionBar = (SelfDefineActionBar) findViewById(resId);
	}

	/**
	 * ���ظ���������fragment�ϵĿ���ͼ--��ʾû�����ݵ���������Զ�̬���һ���Զ�����ͼ
	 * 
	 * @param rootView
	 *            -- fragment�ĸ���ͼ
	 */
//	public void hideEmptyView() {
//		View rootView = getWindow().getDecorView();
//		View emptyLayout = rootView.findViewById(R.id.rlEmptyView);
//		if (null != emptyLayout) {
//			emptyLayout.setVisibility(View.GONE);
//		}
//	}
//
//	public void setLoadingView() {
//		View rootView = getWindow().getDecorView();
//		RelativeLayout llEmptyLayout = (RelativeLayout) rootView
//				.findViewById(R.id.rlEmptyView);
//		if (null != llEmptyLayout) {
//			llEmptyLayout.setVisibility(View.VISIBLE);
//			if (llEmptyLayout.getChildCount() >= 1) {
//				llEmptyLayout.removeAllViews();
//			}
//			View emptyView = View.inflate(getActivity(),
//					R.layout.empty_text_view, null);
//			TextView emptyText = (TextView) emptyView
//					.findViewById(R.id.tvEmpty);
//			emptyText.setText("�����У����Ժ�...");
//			llEmptyLayout.addView(emptyView);
//		}
//	}
//
//	/**
//	 * ����������ͼû�����ݵ���������Զ�̬���һ���Զ�����ͼ
//	 * 
//	 * @param layoutId
//	 *            -- ����ͼ�Ĳ�����ͼ��ע��ᱻ����ȫ��Ļ��LinearLayout�С�)
//	 */
//	public void setEmptyView(int strId, int topDrawId) {
//		View rootView = getWindow().getDecorView();
//		RelativeLayout llEmptyLayout = (RelativeLayout) rootView
//				.findViewById(R.id.rlEmptyView);
//		if (null != llEmptyLayout) {
//			llEmptyLayout.setVisibility(View.VISIBLE);
//			if (llEmptyLayout.getChildCount() >= 1) {
//				llEmptyLayout.removeAllViews();
//			}
//			View emptyView = View.inflate(getActivity(),
//					R.layout.empty_call_log, null);
//			TextView emptyText = (TextView) emptyView
//					.findViewById(R.id.tvEmpty);
//			emptyText.setCompoundDrawablesWithIntrinsicBounds(0, topDrawId, 0,
//					0);
//			emptyText.setText(strId);
//			llEmptyLayout.addView(emptyView);
//		}
//	}
//
//	public Dialog mDialog = null;// �ȴ��Ի���

	@Override
	public void setTitle(int strId, OnClickListener listener) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		JPushInterface.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		JPushInterface.onResume(this);
	}
//	/**
//	 * ������ʾ�Ի���
//	 */
//	public void showRequestDialog(String tipText) {
//		try {
//			if (mDialog != null) {
//				mDialog.dismiss();
//				mDialog = null;
//			}
//			mDialog = DialogFactory.creatRequestDialog(this, tipText);
//			mDialog.show();
//		} catch (Exception e) {
//
//		}
//	}
//
//	/**
//	 * ������ʾ�Ի���
//	 */
//	public void showRequestDialog(Context context, String tipText) {
//		if (mDialog != null) {
//			mDialog.dismiss();
//			mDialog = null;
//		}
//		mDialog = DialogFactory.creatRequestDialog(context, tipText);
//		mDialog.show();
//	}
//
//	/**
//	 * ������ʾ�Ի���
//	 */
//	public void showRequestDialog(int resId) {
//		if (mDialog != null) {
//			mDialog.dismiss();
//			mDialog = null;
//		}
//		mDialog = DialogFactory.creatRequestDialog(this, getResources()
//				.getString(resId));
//		mDialog.show();
//	}

//	/**
//	 * ������ʾ�Ի���
//	 */
//	public void canceRequestDialog() {
//		try {
//			if (mDialog != null) {
//				if (mDialog.isShowing()) {
//					mDialog.dismiss();
//					mDialog = null;
//				}
//			}
//		} catch (Exception e) {
//
//		}
//	}

	/**
	 * ����ListView������״̬
	 */
//	public void resetListViewEmpty() {
//		TextView emptyText = (TextView) findViewById(R.id.tvEmpty);
//		ProgressBar pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
//		emptyText.setText("�����У����Ժ�...");
//		pbLoading.setVisibility(View.VISIBLE);
//	}

//	/**
//	 * ListView�������
//	 * 
//	 * @param tip
//	 */
//	public void setListViewEmpty(String tip) {
//		TextView emptyText = (TextView) findViewById(R.id.tvEmpty);
//		ProgressBar pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
//		if (null == emptyText || pbLoading == null)
//			return;
//		emptyText.setText(tip);
//		if (!TextUtils.isEmpty(tip)) {
//			Drawable top = getResources().getDrawable(
//					R.drawable.icon_no_calllog);
//			emptyText.setCompoundDrawablesWithIntrinsicBounds(null, top, null,
//					null);
//		} else {
//			emptyText.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
//					null);
//		}
//		pbLoading.setVisibility(View.GONE);
//	}
//
//	@Override
//	public void onAutoInstall() {
//		// TODO Auto-generated method stub
//		String app_name = getResources().getString(R.string.app_name) + ".apk";
//
//		File test = new File(new FileCache(this).getFileDirCache(), app_name);
//		if (null != test && test.exists()) {
//			Uri uri = Uri.fromFile(test);
//			Intent intent = new Intent(Intent.ACTION_VIEW);
//			intent.setDataAndType(uri,
//					"application/vnd.android.package-archive");
//			startActivity(intent);
//		}
//	}
}
