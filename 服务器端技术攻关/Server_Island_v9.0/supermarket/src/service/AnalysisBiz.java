package service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
/** 
 * @author ����Ƽ
 * @version 1.0
 * 2016-8-19 
 * �ͻ���������ҵ����
 */
public class AnalysisBiz {
	/**
	 * �����ύ
	 */
	public boolean palceoreder() {
//		List<Record> orderlist = analysisService.findAll();
//		this.setSessionAttr("orderlist", orderlist);
//		this.render("/orderList.jsp");
		
		//�������� ������(oid)����¼����(total)����Ʒ������(pId)����Ʒ����(pNum)�������ܼ�(oSum)

		//��������ʱ�䣬���뵽�����������š�ʱ�䡢�ܼۣ�
		
		//��ѯ��Ʒ���õ���Ʒ������Ʒ����
		
		//���� ��Ʒ�������Ʒ������Ʒ���ۡ���Ʒ�����������ţ�
		return true;
	}

	/**  
	 * ������ѯ
	 * ������������
	 * ����ֵ�����ؼ۸�
	 * 
	 * �޸����ڣ�2016-12-8
	 * ���ߣ�����
	 * �޸��������int�޸�Ϊlong
	 */
	public Record lookupprice(long pid){		
		//���ݲ�ѯ
		Record Record = Db.findById("islandtrading_product","PRODUCT_ID", pid);
		return Record;		
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-1-27
	 * ʵ�֣�ͨ��pName��ѯ��Ʒ
	 * ����ֵ��������Ʒ��Ϣ
	 * */
	public Record lookup_pName(String pName){
		//���ݲ�ѯ
		Record Record = Db.findById("islandtrading_product","PRODUCT_NAME", pName); 
		return Record;		
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��type��ѯ��Ʒ���ﵽ����Ч��
	 * ����ֵ��������Ʒ��Ϣ
	 * 
	 * �޸����ڣ�2016-12-8
	 * 
	 * */
	public List<Record> lookup_type(String pType){
		//���ݲ�ѯ
		System.out.println("----pType����:" + pType);
		List<Record> list_Record = new ArrayList<>();
		String sql = "select * from islandtrading_product where PRODUCT_ID in (" +
				"select PRODUCT_ID from product_classify where CLASSIFY_ID=(" +
				"select CLASSIFY_ID from islandtrading_classify where CLASSIFY_NAME=" + "'" +
				pType + "'" + ")" + ")";
		list_Record = Db.find(sql);
		return list_Record;		
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��act��ѯ���л��ϸ��Ϣ
	 * ����������Ҫ����
	 * ����ֵ���������л����
	 * */
	public List<Record> lookup_act(){
		List<Record> list_Record = new ArrayList<>();
		list_Record = Db.find("select * from islandtrading_activity");
		return list_Record;
	}
	
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��user��goods��ѯ���л��ϸ��Ϣ
	 * ������user goods
	 * ����ֵ�����ؾ���ĳ���ղص�id_goodsΪ**��һ����Ʒ����Ϊ�о�����Ʒid
	 * 
	 * 2016-12-8�޸�
	 * ����ֵ�޸�ΪRecord
	 * */
	public Record lookup_col(String user, String goods){
		System.out.println("Я����goods����! user:"+user+"  goods:"+goods);
			
		int i_user = Integer.valueOf(user);
		int i_goods = Integer.valueOf(goods);
		String sql = "select * from islandtrading_product where PRODUCT_ID=" + "'" + goods + "'";
//		String sql = "select * from islandtrading_product where PRODUCT_ID=(" + 
//				"select PRODUCT_ID from collect_product_user where USER_ID='" + user + 
//				" ' and PRODUCT_ID=" + "'" + goods + "')";  
				
		Record mRecord = Db.findFirst(sql);
		return mRecord;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��user��ѯ���л��ϸ��Ϣ
	 * ������user
	 * ����ֵ�����ؾ���ĳ���ղص�id_goodsΪ**��һ����Ʒ����Ϊ�о�����Ʒid
	 * */
	public List<Record> lookup_col(String user_id){
		System.out.println("û��goods!");
		List<Record> list_Record = new ArrayList<>();
		String sql = "select * from islandtrading_product where PRODUCT_ID in (" + 
				"select PRODUCT_ID from collect_product_user where USER_ID=" + "'" +
				user_id + "'" + ")";
		list_Record = Db.find(sql);
		return list_Record;
	}
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * ʵ�֣������֤��¼
	 * ������user,ȡ����¼���ɣ���AnalysisController�������֤;
	 * ����ֵ�����ؾ����¼ �� null
	 * */
	public Record lookup_user(String user){
		Record mRecord = Db.findById("islandtrading_user", "USER_USERNAME",user);
		return mRecord;
	}
	
	
	
	/*
	 * ���ߣ�������
	 * ʵ�֣�ɾ��ָ����Ʒ
	 * ����ֵ��ɾ�����
	 * */
	public boolean deleteMyGood(String pid){
		int i_pid = Integer.valueOf(pid).intValue();
		boolean res = Db.deleteById("islandtrading_product","PRODUCT_ID", i_pid);
		return res;
	}
	/* ���ߣ�������
	 * ʵ�֣��༭ָ����Ʒ
	 * ����ֵ���༭���
	 * 2016-12-8�޸�
	 * ������ʽ��http://localhost:8080/supermarket/analysis/editGoods?goodsCode={PRODUCT_ID:2,PRODUCT_NAME:����,PRODUCT_PRICE:50,PRODUCT_DESCRIBE:����,PRODUCT_SITE:�����ص�,PRODUCT_STATUS:false}
	 * */
	public int editMyGood(String PRODUCT_ID, String PRODUCT_NAME,
						String PRODUCT_PRICE,
						String PRODUCT_DESCRIBE,
						String PRODUCT_SITE, 
						String PRODUCT_STATUS){
		float price = Float.valueOf(PRODUCT_PRICE).floatValue();
		boolean b_status = Boolean.valueOf(PRODUCT_STATUS).booleanValue();
		
//		System.out.println("update("+pName+","+pID+","+ ") success!");
		String sql = "UPDATE islandtrading_product SET PRODUCT_NAME='"+PRODUCT_NAME+"',PRODUCT_PRICE="+price+ 
					", PRODUCT_DESCRIBE='" + PRODUCT_DESCRIBE  + "' ,PRODUCT_SITE='" +
					PRODUCT_SITE + "', PRODUCT_STATUS=" + b_status + " WHERE PRODUCT_ID='"+PRODUCT_ID+"'";
		int res = Db.update(sql);
		return res;
	}
	
	/*
	 * ʵ�֣�ͼƬ�ϴ����ʱ��ͼƬ�����ص�ַд�뵽���ݿ���
	 * */
	public boolean addimg(String downloadUrl, String pid){
		String sql = "UPDATE islandtrading_product SET img='" + downloadUrl + "' where pid='" + pid + "'";
		int res = Db.update(sql);
		if(res == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*
	 * ʵ�֣�ͨ��pid��ѯpType
	 * ���ߣ�����
	 * ���ڣ�2016-12-8
	 * �漰��product_classify��islandtrading_product
	 * */
	public String getClassify(long PRODUCT_ID){
		String sql = "select CLASSIFY_ID from product_classify where PRODUCT_ID=" + PRODUCT_ID;
		int CLASSIFY_ID;
		String CLASSIFY_NAME = null;
		CLASSIFY_ID = Db.queryInt(sql);
		String sql1 = "select CLASSIFY_NAME from islandtrading_classify where CLASSIFY_ID=" + CLASSIFY_ID;
		CLASSIFY_NAME = Db.queryStr(sql1);
		
		return CLASSIFY_NAME;
	}
}
