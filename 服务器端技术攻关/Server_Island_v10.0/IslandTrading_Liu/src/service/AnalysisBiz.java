package service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class AnalysisBiz {
	/**
	 * �����ύ
	 */
	public boolean palceoreder() {
		// List<Record> orderlist = analysisService.findAll();
		// this.setSessionAttr("orderlist", orderlist);
		// this.render("/orderList.jsp");

		// �������� ������(oid)����¼����(total)����Ʒ������(pId)����Ʒ����(pNum)�������ܼ�(oSum)

		// ��������ʱ�䣬���뵽�����������š�ʱ�䡢�ܼۣ�

		// ��ѯ��Ʒ���õ���Ʒ������Ʒ����

		// ���� ��Ʒ�������Ʒ������Ʒ���ۡ���Ʒ�����������ţ�
		return true;
	}

	/**  
	 * 
	 * ��ѯ�۸�
	 * �޸����ڣ�2016-12-8
	 * ���ߣ�����
	 * �޸��������int�޸�Ϊlong
	 */
	public Record lookupprice(long pid){		
		//���ݲ�ѯ
		Record Record = Db.findById("islandtrading_product","Product_Id", pid);
		return Record;		
	}
	

	/*
	 * ʵ�֣�ɾ��ָ����Ʒ ����ֵ��ɾ�����
	 */
	public boolean deleteMyGood(String pid) {
		boolean res = Db.deleteById("islandtrading_product", "Product_Id", pid);
		return res;
	}

	/*
	 *  ʵ�֣��༭ָ����Ʒ ����ֵ���༭���
	 */
	public int editMyGood(String pID, String pName, float pPrice) {

		System.out.println("update(" + pName + "," + pID + "," + ") success!");
		String sql = "UPDATE t_product SET name='" + pName + "',price=" + pPrice + " WHERE pid='" + pID + "'";
		int res = Db.update(sql);
		return res;
	}
	
	/*
	 * ʵ�֣�ͨ��pid��ѯpType
	 * ���ߣ�����
	 * ���ڣ�2016-12-8
	 * �漰��product_classify��islandtrading_product
	 * */
	public String getClassify(long Product_Id){
		String sql = "select Classify_Id from re_product_classify where Product_Id=" + Product_Id;
		int Classify_Id;
		String Classify_Name = null;
		Classify_Id = Db.queryInt(sql);
		String sql1 = "select Classify_Name from islandtrading_classify where Classify_Id=" + Classify_Id;
		Classify_Name = Db.queryStr(sql1);
		
		return Classify_Name;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-1-27
	 * ʵ�֣�ͨ��pName��ѯ��Ʒ
	 * ����ֵ��������Ʒ��Ϣ
	 * */
	public Record lookup_pName(String pName){
		//���ݲ�ѯ
		Record Record = Db.findById("islandtrading_product","Product_Name", pName); 
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
		String sql = "select * from islandtrading_product where Product_Id in (" +
				"select Product_Id from re_product_classify where Classify_Id=(" +
				"select Classify_Id from islandtrading_classify where Classify_Name=" + "'" +
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
	 * ʵ�֣�ͨ��user��goods��ѯ�����ղ���ϸ��Ϣ
	 * ������user goods
	 * ����ֵ�����ؾ���ĳ���ղص�id_goodsΪ**��һ����Ʒ����Ϊ�о�����Ʒid
	 * 
	 * 2016-12-8�޸�
	 * ����ֵ�޸�ΪRecord
	 * */
	public Record lookup_col(String user, String goods){	//�Ȳ�goods��û����user�ղ���
		System.out.println("Я����goods����! user:"+user+"  goods:"+goods);
		String sql = "select * from re_collect_product_user where User_Id='" + 
						user + "'";
		List<Record> list = new ArrayList<>();
		list = Db.find(sql);	//�õ�user�ղ�������Ʒ
		System.out.println("-----listΪ��" + list.toString());
		int goods_id = Integer.valueOf(goods);
		int Product_Id;
		for (Record mRecord : list){
			Product_Id = mRecord.getInt("Product_Id");
			if(goods_id == Product_Id){
				System.out.println("-----lookup_col()�в�ѯ��ĳuser��ĳgoods��ִ��if");
				Record mRecord_res = Db.findById("islandtrading_product", "Product_Id", goods_id);
				return mRecord_res;
			}
		}
		
//		Record mRecord = Db.findFirst(sql);
		return null;
	}
	
	/*
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ʵ�֣�ͨ��user��ѯ�����ղ���Ʒ��ϸ��Ϣ
	 * ������user
	 * ����ֵ�����ؾ���ĳ���ղص�id_goodsΪ**��һ����Ʒ����Ϊ�о�����Ʒid
	 * */
	public List<Record> lookup_col(String user_id){
		System.out.println("û��goods!");
		List<Record> list_Record = new ArrayList<>();
		String sql = "select * from islandtrading_product where Product_Id in (" + 
				"select Product_Id from re_collect_product_user where User_Id=" + "'" +
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
		Record mRecord = Db.findById("islandtrading_user", "User_Username",user);
		return mRecord;
	}
	
	/* ���ߣ�������
	 * ʵ�֣��༭ָ����Ʒ
	 * ����ֵ���༭���
	 * 2016-12-8�޸�
	 * ������ʽ��http://localhost:8080/supermarket/analysis/editGoods?goodsCode={Product_Id:2,PRODUCT_NAME:����,PRODUCT_PRICE:50,PRODUCT_DESCRIBE:����,PRODUCT_SITE:�����ص�,PRODUCT_STATUS:false}
	 * */
	public int editMyGood(String Product_Id, String PRODUCT_NAME,
						String PRODUCT_PRICE,
						String PRODUCT_DESCRIBE,
						String PRODUCT_SITE, 
						String PRODUCT_STATUS){
		float price = Float.valueOf(PRODUCT_PRICE).floatValue();
		boolean b_status = Boolean.valueOf(PRODUCT_STATUS).booleanValue();
		
//		System.out.println("update("+pName+","+pID+","+ ") success!");
		String sql = "UPDATE islandtrading_product SET PRODUCT_NAME='"+PRODUCT_NAME+"',PRODUCT_PRICE="+price+ 
					", PRODUCT_DESCRIBE='" + PRODUCT_DESCRIBE  + "' ,PRODUCT_SITE='" +
					PRODUCT_SITE + "', PRODUCT_STATUS=" + b_status + " WHERE Product_Id='"+Product_Id+"'";
		int res = Db.update(sql);
		return res;
	}
	
	
	/*
	 * ʵ�֣������Ʒ,ֻ�����Ʒ����
	 * ���ߣ�������
	 * ���ڣ�2016-12-13
	 * �漰��islandtrading_product
	 * */
	public boolean add_Goods(Record mRecord){
		
		boolean res = Db.save("islandtrading_product", mRecord);	
		return res;
	}
	
	/*
	 * �����Ʒ������
	 * ���ߣ�������
	 * ���ڣ�2016-12-13
	 * �漰��product_user
	 * ���ҳ�pid�����ҳ�userid
	 * */
	public boolean add_Goods_User(String pName, String userName){
//		System.out.println("----����   pName:" + pName + "  userName:" + userName);
		Record mRecord = Db.findById("islandtrading_product", "Product_Name", pName);
		int Product_Id = mRecord.getInt("Product_Id");	//�ҵ� Product_Id
		Record mRecord_user = Db.findById("islandtrading_user", "User_Username", userName);
		if(mRecord_user == null){
			System.out.println("-----��islandtrading_user�����ڴ�user��" + userName);
			return false;
		}
		int USER_ID = mRecord_user.getInt("User_Id"); 
		Record myRecord = new Record().set("User_Id", USER_ID)
									.set("Product_Id", Product_Id);
		boolean res = Db.save("re_product_user", myRecord);
		return res;
	}
	
	/*
	 * ���ͼƬ���ص�ַ
	 * ���ߣ�������
	 * ���ڣ�2016-12-15
	 * ���������뱾�ط��������ļ�����
	 * */
	public boolean add_img_url(String img_Name){
		
		return false;
	}
	
	/*
	 * ͨ��Product_Id�õ���ƷͼƬ����
	 * ���ߣ�������
	 * ���ڣ�2016-12-15
	 * */
	public String getImg(int pid){
		Record mRecord = Db.findById("islandtrading_product", "Product_Id", pid);
		String Product_Image = mRecord.getStr("Product_Image");
		System.out.println("-----getImg()�ҵ���ͼƬ����" + Product_Image);
		
		return Product_Image;
	}
	
}
