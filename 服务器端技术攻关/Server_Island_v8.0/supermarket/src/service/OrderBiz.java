package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/** 
 * @author ����Ƽ
 * @version 1.0
 * 2016-8-18 
 * ��������ҵ����
 */
public class OrderBiz {
	
	/**  
	 * ���Ҷ���
	 * ����ֵ���������ж���
	 */
	public List<Record> findAll(){
		List<Record> orders = Db.find("select * from t_order order by date desc");
		return orders;
	}
	/**  
	 * ���Ҷ���
	 * ������������
	 * ����ֵ�����ض���
	 */
	public Record findByID(String oid){
		Record rec = Db.findById("t_order","oid", oid);
		return rec;
	}
	
	/**  
	 * ���Ҷ�������
	 * ������������
	 * ����ֵ�����ض�����ϸ��Ϣ
	 */
	public List<Record> findDetailByID(String oid){
		String sql="select * from t_detail where oid='"+oid+"'";		
		List<Record> orders = Db.find(sql);
		return orders;
	}
	
	/*
	 * ���涩����Ϣ
	 * ���ߣ�������	ԭsave�����������ˣ��õĻ����¸���һ��
	 * ���ڣ�2016-11-29
	 * ������һ��
	 * */
	public boolean save(String oid, double osum, String address,
			String telphone, String user_id, String pid){
		Record order = new Record().set("oid", oid)
				.set("osum", osum)
				.set("address", address)
				.set("telphone", telphone)
				.set("user_id",user_id)
				.set("pid", pid);
		boolean res = false;
		try {
			res = Db.save("order", order);
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("��������ʧ�ܣ�����");
			e.printStackTrace();
		}
		return res;
	}
	
	//�������涩�������¼
	public int[] batchsave(List<Record> recordList,int batchSize){
		int[] res = Db.batchSave("t_detail", recordList, batchSize);
		return res;
	}
	
	/*
	 * �����û��ķ�����Ϣ
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * ������
	 * ����ֵ��boolean �����ύ���
	 * ����ע�ͣ���ȡ����ע��ת�룡����
	 * */
	public boolean subfb(String str_id, String str_user_id, 
			String str_content, String str_time, boolean b_status){	
		java.sql.Date time_sql = null;
		Date time_date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			time_date = sdf.parse(str_time);
			time_sql = new java.sql.Date(time_date.getTime());
//			System.out.println("subfb():"+ str_time +"\n"+time_date.toString() + "\n  " + time_sql.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Record order = new Record().set("id", str_id).
				set("user_id", str_user_id).
				set("content", str_content).
				set("time", time_date).
				set("status", b_status);
		System.out.println("��ɵ�order------"+order.toString());
		boolean res = false;
		try {
			res = Db.save("feedback", order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("----OrderBiz.java�����������쳣������");
		}
		return res;
	}

	/*
	 * ע���û���Ϣ
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * ������user��pwd
	 * ����ֵ��Boolean ע����
	 * App��ע�ͣ�powerĬ��Ϊ0�����Ȩ�ޣ�
	 * 			������Ҫ�϶࣬�������֤�˻��������ţ���ַ��߼Ӹ� ���� nick�ͺ���
	 * ����ע�ͣ�ע������ת��
	 * */
	public boolean reg_user(String user, String pwd, String nick){
		System.out.println("reg_user()�еĲ��� user:" + user + " pwd:" + pwd + " nick:" + nick);
		Record mRecord = new Record().set("username", user)
				.set("password", pwd)
				.set("nickname", nick)
				.set("power", 0);
		boolean res = false;
		try {
			res = Db.save("user", mRecord);
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ע��ʧ��ඣ�");
			e.printStackTrace();
		}
		return res;
	}

}
