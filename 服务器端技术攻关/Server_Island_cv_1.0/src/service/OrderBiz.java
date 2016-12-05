package service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/** 
 * ��������ҵ����
 */
public class OrderBiz {
	
	/**  
	 * ���Ҷ���
	 * ����ֵ���������ж���
	 */
	public List<Record> findAll(){
		//List<Record> orders = Db.find("select * from order_b order by id desc");
		List<Record> orders = Db.find("select * from order_b order by id desc");
		return orders;
	}
	/**  
	 * ���Ҷ���
	 * ������������
	 * ����ֵ�����ض���
	 */
	public Record findByID(String oid){
		Record rec = Db.findById("order_b","id", oid);
		return rec;
	}
	
	/**  
	 * ���Ҷ�������
	 * ������������
	 * ����ֵ�����ض�����ϸ��Ϣ
	 */
	public List<Record> findDetailByID(String oid){
		String sql="select * from order_b where id='"+oid+"'";		
		List<Record> orders = Db.find(sql);
		return orders;
	}
	
	//���涩��
	public boolean save(String oID,String time,float total){
		Record order = new Record().set("id", oID).set("date", time).set("total", total);
		boolean res = Db.save("order_b", order);
		return res;
	}
	
	//�������涩�������¼
	public int[] batchsave(List<Record> recordList,int batchSize){
		int[] res = Db.batchSave("order_b", recordList, batchSize);
		return res;
	}
}
