package com.wf.qingniao.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import com.wf.qingniao.R;
import com.wf.qingniao.bean.AreaCode;
import com.wf.qingniao.bean.MyLocation;
import com.wf.qingniao.service.GpsLocation;
import com.wf.qingniao.service.GpsLocation.LocationListener;
import com.wf.qingniao.utils.MyProperUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
@ContentView(R.layout.activity_mian)
public class QianNiaoCenterActivity extends BaseActivity implements OnChartGestureListener, OnChartValueSelectedListener{
	@ViewInject(R.id.chart1)
	private LineChart mChart;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		InitChart();
//		SMS();
		
//		DbInit();
		
	}
	//��������д���ļ�
	void AreaWriteFile(){
		if(!new File("/sdcard/qingniao1.db").exists()){
			try {
				InputStream in = getResources().openRawResource(R.raw.qingniao);
				FileOutputStream fos = new FileOutputStream("/sdcard/qingniao1.db");
				byte[] b = new byte[1024];
				while((in.read(b)) != -1){
					fos.write(b);
				}
				in.close();
				fos.close();
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		DbUtils db = DbUtils.create(getActivity(),"/sdcard/","qingniao1.db");
		try {
			List<AreaCode> aa = db.findAll(Selector.from(AreaCode.class).where(WhereBuilder.b("area", "=", "����ʡ")));
			System.out.println(aa.get(0).getCode());
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void DbInit(){
		DbUtils db = DbUtils.create(getActivity(),"/sdcard/","qingniao.db");
		 
		
		try {
			if(!db.tableIsExist(AreaCode.class)){
				Properties properties = MyProperUtil.getProperties(getApplicationContext());
		        
		        Enumeration enu2=properties.propertyNames();
		        int a = 0;
		        while(enu2.hasMoreElements()){
		            String key = (String)enu2.nextElement();
		            String value = properties.getProperty(key);
		            AreaCode areacode = new AreaCode();
		            areacode.setArea(value);
					areacode.setCode(key);
					db.save(areacode);
					areacode = null;
					System.out.println("a = "+a);
					a++;
		        }
				
				
				
				
				
			}
			
			List<AreaCode> are = db.findAll(Selector.from(AreaCode.class));
			Log.i("huwei", "are size ="+are.size());
		
		
		
		
		
		
		
		
		
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
/*	void SMS(){
		//��ע��ҳ��
		RegisterPage registerPage = new RegisterPage();
			registerPage.setRegisterCallback(new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				// ����ע����
				if (result == SMSSDK.RESULT_COMPLETE) {
				@SuppressWarnings("unchecked")
				HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
				String country = (String) phoneMap.get("country");
				String phone = (String) phoneMap.get("phone"); 
		
				// �ύ�û���Ϣ
				}
			}
		});
		registerPage.show(getActivity());
	}*/
	
	private void showShare() {
		 ShareSDK.initSDK(this);
		 OnekeyShare oks = new OnekeyShare();
		 //�ر�sso��Ȩ
		 oks.disableSSOWhenAuthorize(); 

		// ����ʱNotification��ͼ�������  2.5.9�Ժ�İ汾�����ô˷���
		 //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		 oks.setTitle(getString(R.string.share));
		 // titleUrl�Ǳ�����������ӣ�������������QQ�ռ�ʹ��
		 oks.setTitleUrl("http://sharesdk.cn");
		 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
		 oks.setText("���Ƿ����ı�");
		 // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		 //oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		 oks.setUrl("http://sharesdk.cn");
		 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		 oks.setComment("���ǲ��������ı�");
		 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		 oks.setSite(getString(R.string.app_name));
		 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
		 oks.setSiteUrl("http://sharesdk.cn");

		// ��������GUI
		 oks.show(this);
		 }
	void InitChart(){
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");
        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(1);
        
        setData(new String[]{"2016-01","2016-02"}, new String[]{"13","16"});
	}
	private void setData(String [] dataX,String [] dataY) {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < dataX.length; i++) {
        	String time = null;
        		time = dataX[i];
            xVals.add(time);
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();
     
        for (int i = 0; i < dataY.length; i++) {

            float val = Float.parseFloat(dataY[i]);// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        String DataSet = "DataSet";
        LineDataSet set1 = new LineDataSet(yVals, DataSet);
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
//        set1.enableDashedLine(10f, 5f, 0f);
//        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.GREEN);
        set1.setCircleColor(Color.GREEN);
        set1.setLineWidth(1f);
        set1.setCircleSize(2f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        //�����Ƿ���ʾÿ���ڵ��ϵ�ֵ
        data.setDrawValues(false);
        // set data
        mChart.setData(data);
    }
	@Override
	public void onChartDoubleTapped(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChartFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChartGestureEnd(MotionEvent arg0, ChartGesture arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChartGestureStart(MotionEvent arg0, ChartGesture arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChartLongPressed(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChartScale(MotionEvent arg0, float arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChartSingleTapped(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onChartTranslate(MotionEvent arg0, float arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onValueSelected(Entry arg0, int arg1, Highlight arg2) {
		// TODO Auto-generated method stub
		
	}
}
