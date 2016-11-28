package service;

import java.util.Date;
import java.util.List;

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
	
	//���涩��
	public boolean save(String oID,String time,float total){
		Record order = new Record().set("oid", oID).set("date", time).set("total", total);
		boolean res = Db.save("t_order", order);
		return res;
	}
	
	//�������涩�������¼
	public int[] batchsave(List<Record> recordList,int batchSize){
		int[] res = Db.batchSave("t_detail", recordList, batchSize);
		return res;
	}
}
