package com.daomaidaomai.islandtrading.controller;
/*�ٶȵ�ͼ��ȡλ�õĺ���*/
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.daomaidaomai.islandtrading.util.BDGpsServiceListener;


public class BDGpsService extends Service {

	private static final int minTime = 1000;
	private LocationClient locationClient;
	private BDLocationListener locationListener;
	private LocationClientOption lco;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		lco = new LocationClientOption();
		lco.setLocationMode(LocationMode.Hight_Accuracy);
		//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		lco.setScanSpan(minTime);
		//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		lco.setCoorType("bd09ll");
		//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		lco.setOpenGps(true);
		//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		lco.setIsNeedAddress(true);
		locationListener = new BDGpsServiceListener(getApplicationContext());
		locationClient = new LocationClient(getApplicationContext());//����location��
		locationClient.setLocOption(lco);
		locationClient.registerLocationListener(locationListener);//ע���������
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if (locationClient != null && !locationClient.isStarted()){
			locationClient.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (locationClient != null && locationClient.isStarted()){
			locationClient.stop();
		}
		locationClient.unRegisterLocationListener(locationListener);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
