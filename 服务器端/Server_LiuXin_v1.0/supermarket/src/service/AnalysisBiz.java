package service;

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
