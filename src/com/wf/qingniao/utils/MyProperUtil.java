package com.wf.qingniao.utils;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class MyProperUtil {
	private static Properties urlProps;
    public static Properties getProperties(Context c){
           Properties props = new Properties();
           try {
           //方法一：通过activity中的context攻取setting.properties的FileInputStream
           InputStream in = c.getAssets().open("AreaCodeTable.properties");
           //方法二：通过class获取setting.properties的FileInputStream
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
