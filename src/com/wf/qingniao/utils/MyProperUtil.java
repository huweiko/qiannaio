package com.wf.qingniao.utils;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class MyProperUtil {
	private static Properties urlProps;
    public static Properties getProperties(Context c){
           Properties props = new Properties();
           try {
           //����һ��ͨ��activity�е�context��ȡsetting.properties��FileInputStream
           InputStream in = c.getAssets().open("AreaCodeTable.properties");
           //��������ͨ��class��ȡsetting.properties��FileInputStream
           //InputStream in = PropertiesUtill.class.getResourceAsStream("/assets/  setting.properties ")); 
           props.load(in);
           } catch (Exception e1) {
           // TODO Auto-generated catch block
               e1.printStackTrace();
           }
            
           urlProps = props;
           return urlProps;
           }
}
