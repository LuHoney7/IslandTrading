package com.daomaidaomai.islandtrading.util;
/*���ðٶȵ�ͼ����*/
import android.content.Context;
import android.content.Intent;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;


public class BDGpsServiceListener implements BDLocationListener {

	private Context context;
	public static double longitude;
	public static double latitude;
	public static String time;
	
	public BDGpsServiceListener(){
		super();
	}
	public BDGpsServiceListener(Context context){
		super();
		this.context = context;
	}

	//���͹㲥����ʾ���½���
	private void sendToActivity(String str){
		Intent intent = new Intent();
		intent.putExtra("newLoca", str);
		intent.setAction("NEW LOCATION SENT");
		context.sendBroadcast(intent);
	}
	@Override
	public void onReceiveLocation(BDLocation location) {
		// TODO Auto-generated method stub
		if(location == null){return;}
		StringBuffer sb = new StringBuffer();
		sb.append("����=").append(location.getLongitude());
		sb.append("\nγ��=").append(location.getLatitude());
		sb.append("\nʱ��=").append(location.getTime());
		if (location.hasRadius()){
		}
		if (location.getLocType() == BDLocation.TypeGpsLocation){
		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
		}
		//�ַ���
		sendToActivity(sb.toString());
		//���
		longitude=location.getLongitude();
		latitude=location.getLatitude();
		time=location.getTime();
	}
}
