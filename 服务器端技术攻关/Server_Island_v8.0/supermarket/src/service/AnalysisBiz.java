package service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
	 */
	public Record lookupprice(String pid){		
		//���ݲ�ѯ
		Record Record = Db.findById("t_product","pid", pid);
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
		Record Record = Db.findById("t_product","name", pName); 
		return Record;		
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��type��ѯ��Ʒ���ﵽ����Ч��
	 * ����ֵ��������Ʒ��Ϣ
	 * */
	public List<Record> lookup_type(String pType){
		//���ݲ�ѯ
		System.out.println("----pType����:" + pType);
		List<Record> list_Record = new ArrayList<>();
		list_Record = Db.find("select * from " + "t_product" + " where type = '" + pType+ "'");	//�����ţ��ó�ʱ��Ž��
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
		list_Record = Db.find("select * from " + "activities");
		return list_Record;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��user��goods��ѯ���л��ϸ��Ϣ
	 * ������user goods
	 * ����ֵ�����ؾ���ĳ���ղص�id_goodsΪ**��һ����Ʒ����Ϊ�о�����Ʒid
	 * */
	public List<Record> lookup_col(String user, String goods){
		System.out.println("Я����goods����! user:"+user+"  goods:"+goods);
		List<Record> list_Record = new ArrayList<>();
		int i_user = Integer.valueOf(user);
		int i_goods = Integer.valueOf(goods);
		list_Record = Db.find("select * from " + "collection " + "where id_goods="
		+ i_goods + " and " + "id_user=" + i_user);
		System.out.println("sql���:----"+"select * from " + "collection " + "where id_goods="
		+ i_goods + " and " + "id_user=" + i_user);
		return list_Record;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��user��ѯ���л��ϸ��Ϣ
	 * ������user
	 * ����ֵ�����ؾ���ĳ���ղص�id_goodsΪ**��һ����Ʒ����Ϊ�о�����Ʒid
	 * */
	public List<Record> lookup_col(String user){
		System.out.println("û��goods!");
		List<Record> list_Record = new ArrayList<>();
		list_Record = Db.find("select * from " + "collection " + "where id_user=" + "'" + user + "'");
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
		Record mRecord = Db.findById("user", "username",user);
		return mRecord;
	}
	
	
	
	/*
	 * ���ߣ�������
	 * ʵ�֣�ɾ��ָ����Ʒ
	 * ����ֵ��ɾ�����
	 * */
	public boolean deleteMyGood(String pid){
		boolean res = Db.deleteById("t_product","pid", pid);
		return res;
	}
	/* ���ߣ�������
	 * ʵ�֣��༭ָ����Ʒ
	 * ����ֵ���༭���
	 * */
	public int editMyGood(String pID,String pName,float pPrice){
		
		System.out.println("update("+pName+","+pID+","+ ") success!");
		String sql = "UPDATE t_product SET name='"+pName+"',price="+pPrice+" WHERE pid='"+pID+"'";
		int res = Db.update(sql);
		return res;
	}
}
