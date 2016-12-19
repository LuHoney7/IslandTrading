/**
 * Function:OrderBiz
 * Date:2016.12.11
 * Author:LiuXin
 */
package service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class OrderBiz {
	/**
	 * ���Ҷ���
	 */
	public List<Record> findAll() {
		List<Record> orders = Db.find("select * from islandtrading_order order by Order_Id desc");
		return orders;
	}

	/**
	 * ���Ҷ���
	 */
	public Record findByID(String oid) {
		Record rec = Db.findById("islandtrading_order", "Order_Id", oid);
		return rec;
	}

	/**
	 * ���Ҷ�������
	 */
	public List<Record> findDetailByID(String oid) {
		String sql = "select * from islandtrading_order where Order_Id='" + oid + "'";
		List<Record> orders = Db.find(sql);
		return orders;
	}

	// ���涩��
	public boolean save(String oID, Date time, String status, String site) {
		Record order = new Record().set("Order_Id", oID).set("Order_Time", time).set("Order_Status", status)
				.set("Order_Site", site);
		boolean res = Db.save("islandtrading_order", order);
		return res;
	}

	// �������涩�������¼
	public int[] batchsave(List<Record> recordList, int batchSize) {
		int[] res = Db.batchSave("islandtrading_order", recordList, batchSize);
		return res;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ʵ�֣�ͨ��act�༭�����
	 * ���������ĸ��ֲ���
	 * ����ֵ��int
	 * */
	public int edit_act(String Activity_Id,
			String ACTIVITY_CONTENT,
			String ACTIVITY_ORGANIZER,
			String ACTIVITY_TIME,
			String ACTIVITY_SITE,
			String ACTIVITY_NAME){
		int res = 0;		
		System.out.println("-----����++"+ACTIVITY_CONTENT+ACTIVITY_TIME);
//		int act_id = Integer.valueOf(Activity_Id);	//���ţ�����Ҳ����Ҫ 
//			Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ACTIVITY_TIME);	//�����ݿ���д����Ҫ���˷��˺ö�ʱ��...
		String sql = "update islandtrading_activity set ACTIVITY_CONTENT = '" + ACTIVITY_CONTENT + 
				"', ACTIVITY_ORGANIZER='" + ACTIVITY_ORGANIZER + 
				"', ACTIVITY_TIME='" + ACTIVITY_TIME + 
				"', ACTIVITY_SITE='" + ACTIVITY_SITE +
				"', ACTIVITY_NAME='" + ACTIVITY_NAME +
				"' where Activity_Id=" + Activity_Id;
		res = Db.update(sql);
		return res;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ʵ�֣�ͨ��Activity_Id�ҵ����¼USER_ID,��USER_IDƥ��APP��USER_ID
	 * ������Activity_Id
	 * */
	public String fetch_User_By_Act(String Activity_Id){
//		String sql = "select USER_ID from activity_user where Activity_Id='" + Activity_Id + "'";
		int activity_id = Integer.valueOf(Activity_Id).intValue();
		Record mRecord = Db.findById("re_activity_user", "Activity_Id", activity_id);
		System.out.println("----fetch_User_By_Act()�õ��� USER_ID��" + mRecord.getInt("User_Id"));
		return mRecord.getInt("User_Id") + "";
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ʵ�֣��û�ͨ���ֻ�ɾ�������Ļ
	 * ������Activity_Id
	 * */
	public boolean del_act(String USER_ID, String Activity_Id){
		boolean res = Db.deleteById("islandtrading_activity", "Activity_Id", Activity_Id);
		return res;
	}
	
	/*
	 * �����û��ķ�����Ϣ
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * ������
	 * ����ֵ��boolean �����ύ���
	 * ����ע�ͣ���ȡ����ע��ת�룡����
	 * 
	 * 2016-12-8�޸�
	 * �޸��˲���
	 * */
	public boolean subfb( String content, 
			String contact, String time){
//		int i_id = Integer.parseInt(id);
//		boolean b_status = Boolean.valueOf(status).booleanValue();
		java.sql.Date time_sql = null;
		Date time_date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			time_date = sdf.parse(time);	//�����ݿ��в������ָ�ʽ��ȷ
			time_sql = new java.sql.Date(time_date.getTime());	//�����ݿ��в������ָ�ʽ����ȷ
		} catch (ParseException | java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Record order = new Record().
				set("Fb_Content", content).
				set("Fb_Contact", contact).
				set("Fb_Time", time_date).
				set("Fb_Status", false);
		System.out.println("��ɵ�order------"+order.toString());
		boolean res = false;
		try {
			res = Db.save("islandtrading_feedback", order);
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
	 * 
	 * 2016-12-8�޸�
	 * �û������ݶ� ����ҪЯ��������Ĭ��Ϊ��ͼ���0������Աͨ����̨�޸�
	 * ������ʽ��
	 * 
	 * 
	 * */
	public boolean reg_user(String User_Nickname, String User_Username, String User_Password,
							String User_TakingId, String User_Tel, String Hx_Username,String Hx_Password){
//		System.out.println("reg_user()�еĲ��� user:" + user + " pwd:" + pwd + " nick:" + nick);
		
		//ע���ʱ���û����ظ���ʱ����ʾ�Ѵ���
		Record mRecord_user = Db.findById("islandtrading_user", "User_Username", User_Username);
		if(mRecord_user != null){
			System.out.println("----ע��ʧ�ܣ��û����Ѵ��ڣ�");
			return false;
		}
		
		Record mRecord = new Record().set("User_Nickname", User_Nickname)
				.set("User_Username", User_Username)
				.set("User_Password", User_Password)
				.set("User_TakingId", User_TakingId)
				.set("User_Power", 0)
				.set("User_Tel", User_Tel)
				.set("Hx_Username", Hx_Username)
				.set("Hx_Password", Hx_Password);
		boolean res = false;
		try {
			res = Db.save("islandtrading_user", mRecord);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ע��ʧ��ඣ�");
			e.printStackTrace();
		}
		return res;
	}
	
	/*
	 * ���涩����Ϣ
	 * ���ߣ�������	ԭsave�����Ѹ���
	 * ���ڣ�2016-11-29
	 * ������һ��
	 * */
	public boolean save(String oid, String address,
					String time, String status){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date time_date = null;
		boolean b_status = Boolean.valueOf(status).booleanValue();
		try {
			time_date = sdf.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Record order = new Record().set("Order_Id", oid)
				.set("Order_Site", address)
				.set("Order_Time", time_date)
				.set("Order_Status", b_status);
		boolean res = false;
		try {
			res = Db.save("islandtrading_order", order);
			return res;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("��������ʧ�ܣ�����");
			e.printStackTrace();
		}
		return res;
	}
	
	
	
}
