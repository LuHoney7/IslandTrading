package service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/** 
 * @author ����Ƽ
 * @version 1.0
 * 2016-8-17
 * ��Ʒ����ҵ����
 */
public class ProductBiz {
	/**  
	 * ������Ʒ
	 * ��������ƷID����Ʒ������Ʒ�۸�
	 * ����ֵ��true����false
	 */
	public boolean save(String pID,String pName,float pPrice){
		Record pro = new Record().set("PRODUCT_ID", pID).set("PRODUCT_NAME", pName).set("PRODUCT_PRICE", pPrice);
		boolean res = Db.save("islandtrading_product", pro);
		return res;
	}
	/**  
	 * ɾ����Ʒ
	 * ��������ƷID
	 * ����ֵ��true����false
	 */
	public boolean deleteByID(String pid){
		boolean res = Db.deleteById("islandtrading_product","PRODUCT_ID", pid);
		return res;
	}
	/**  
	 * �޸���Ʒ��Ϣ
	 * ��������ƷID����Ʒ������Ʒ�۸�
	 * ����ֵ���޸ĳɹ��ļ�¼��
	 */
	public int update(String pID,String pName,float pPrice){
		System.out.println("update("+pName+","+pID+","+ ") success!");
		String sql = "UPDATE islandtrading_product SET PRODUCT_NAME='"+pName+"',PRODUCT_PRICE="+pPrice+" WHERE PRODUCT_ID='"+pID+"'";
		int res = Db.update(sql);
		return res;
	}

	public List<Record> findAll(){
		List<Record> pros = Db.find("select * from islandtrading_product");
		return pros;
	}
	/**  
	 * ������Ʒ
	 * ��������ƷID
	 * ����ֵ�����ؼ�¼
	 */
	public Record findByID(String pid){
		Record rec = Db.findById("islandtrading_product","PRODUCT_ID", pid);
		return rec;
	}
}
