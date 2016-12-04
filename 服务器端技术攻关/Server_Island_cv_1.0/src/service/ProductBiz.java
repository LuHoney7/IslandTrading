package service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/** 
 * ��Ʒ����ҵ����
 */
public class ProductBiz {
	/**  
	 * ������Ʒ
	 */
	public boolean save(String pId,String pName,float pPrice,String pDescribe
			,String pImage,String pClassify_Id,String pSite,String pTime ){
		Record pro = new Record().set("id", pId).set("name", pName).set("price", pPrice).
				set("t_describe",pDescribe).set("image",pImage).set("classify_id",pClassify_Id)
				.set("site", pSite).set("time",pTime);
		boolean res = Db.save("product_b", pro);
		return res;
	}
	/**  
	 * ɾ����Ʒ
	 */
	public boolean deleteByID(String pid){
		boolean res = Db.deleteById("product_b","id", pid);
		return res;
	}
	/**  
	 * �޸���Ʒ��Ϣ
	 */
	public int update(String pId,String pName,String pPrice,String pDescribe
			,String pImage,String pClassify_Id,String pSite,String pTime){
		String sql = "UPDATE product_b SET"+ 
		" name='"+pName+"'," + 
		"price='"+pPrice+"',"+
		"t_describe='"+pDescribe+"',"+
		"image='"+pImage+"',"+
	    "classify_id='"+pClassify_Id+"',"+
		"site='"+pSite+"',"+
		"time='"+pTime +"'"+
		" WHERE id='"+pId+"'";
		int res = Db.update(sql);
		return res;
	}

	public List<Record> findAll(){
		List<Record> pros = Db.find("select * from product_b");
		return pros;
	}
	/**  
	 * ������Ʒ
	 */
	public Record findByID(String pid){
		Record rec = Db.findById("product_b","id", pid);
		return rec;
	}
}
