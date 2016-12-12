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
		}
		Record order = new Record().set("ORDER_ID", oid)
				.set("ORDER_SITE", address)
				.set("ORDER_TIME", time_date)
				.set("OEDER_STATUS", b_status);
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
	 * 
	 * 2016-12-8�޸�
	 * �޸��˲���
	 * */
	public boolean subfb(String id, String content, 
			String contact, String time, String status){
		int i_id = Integer.parseInt(id);
		boolean b_status = Boolean.valueOf(status).booleanValue();
		java.sql.Date time_sql = null;
		Date time_date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			time_date = sdf.parse(time);	//�����ݿ��в������ָ�ʽ��ȷ
			time_sql = new java.sql.Date(time_date.getTime());	//�����ݿ��в������ָ�ʽ����ȷ
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Record order = new Record().set("FB_ID", i_id).
				set("FB_CONTENT", content).
				set("FB_CONTACT", contact).
				set("FB_TIME", time_date).
				set("FB_STATUS", b_status);
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
	public boolean reg_user(String USER_NICKNAME, String USER_USERNAME, String USER_PASSWORD,
							String USER_TAKINGID, String USER_CONTACT	){
//		System.out.println("reg_user()�еĲ��� user:" + user + " pwd:" + pwd + " nick:" + nick);
		Record mRecord = new Record().set("USER_NICKNAME", USER_NICKNAME)
				.set("USER_USERNAME", USER_USERNAME)
				.set("USER_PASSWORD", USER_PASSWORD)
				.set("USER_TAKINGID", USER_TAKINGID)
				.set("USER_POWER", 0)
				.set("USER_CONTACT", USER_CONTACT);
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
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ʵ�֣�ͨ��act�༭�����
	 * ���������ĸ��ֲ���
	 * ����ֵ��int
	 * */
	public int edit_act(String ACTIVITY_ID,
			String ACTIVITY_CONTENT,
			String ACTIVITY_ORGANIZER,
			String ACTIVITY_TIME,
			String ACTIVITY_SITE,
			String ACTIVITY_NAME){
		int res = 0;		
		System.out.println("-----����++"+ACTIVITY_CONTENT+ACTIVITY_TIME);
//		int act_id = Integer.valueOf(ACTIVITY_ID);	//���ţ�����Ҳ����Ҫ 
//			Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(ACTIVITY_TIME);	//�����ݿ���д����Ҫ���˷��˺ö�ʱ��...
		String sql = "update islandtrading_activity set ACTIVITY_CONTENT = '" + ACTIVITY_CONTENT + 
				"', ACTIVITY_ORGANIZER='" + ACTIVITY_ORGANIZER + 
				"', ACTIVITY_TIME='" + ACTIVITY_TIME + 
				"', ACTIVITY_SITE='" + ACTIVITY_SITE +
				"', ACTIVITY_NAME='" + ACTIVITY_NAME +
				"' where ACTIVITY_ID=" + ACTIVITY_ID;
		res = Db.update(sql);
		return res;
	}

	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ʵ�֣�ͨ��ACTIVITY_ID�ҵ����¼USER_ID,��USER_IDƥ��APP��USER_ID
	 * ������ACTIVITY_ID
	 * */
	public String fetch_User_By_Act(String ACTIVITY_ID){
//		String sql = "select USER_ID from activity_user where ACTIVITY_ID='" + ACTIVITY_ID + "'";
		Record mRecord = Db.findById("activity_user", "ACTIVITY_ID", ACTIVITY_ID);
		return mRecord.getInt("USER_ID") + "";
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ʵ�֣��û�ͨ���ֻ�ɾ�������Ļ
	 * ������ACTIVITY_ID
	 * */
	public boolean del_act(String USER_ID, String ACTIVITY_ID){
		boolean res = Db.deleteById("islandtrading_activity", "ACTIVITY_ID", ACTIVITY_ID);
		return res;
	}
	
}
