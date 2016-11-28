package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import service.AnalysisBiz;
import service.OrderBiz;
import service.ProductBiz;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

public class AnalysisController extends Controller {
	AnalysisBiz analysisService = this.enhance(AnalysisBiz.class);
	OrderBiz orderService = this.enhance(OrderBiz.class);
	ProductBiz productService = this.enhance(ProductBiz.class);

	/**
	 * �۸��ѯ
	 */
	public void lookupprice() {

		String jsonContent = this.getPara("goodsCode"); //goodsCode �� pid ��һ������
		//String jsonContent = "{pid:123458}";	//�����������ʲô���ӣ�����Ľӿ���Ӧ��Ҳһ��
		System.out.println("����ʲô"+jsonContent);
		String pid = "123457";	//Ĭ��pid��Ӧ����Ϊ�˷�ֹ��ȡ����goodsCode���õ�
		// ����json����
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonContent);
			pid = jsonObject.getString("pId");	//�õ���Ʒpid
			System.out.println("����ʲôpid"+pid);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} //�õ�json��
	
		System.out.println("lookup price pId:" + pid);

		// ���Ҽ�¼
		Record rec = analysisService.lookupprice(pid);

		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();
		float price;
		String name;

		// ���ؽ�� 1 ���ڣ�0 ������
		String res = "ok";

		if (rec != null) {
			// ��ȡ��Ʒ�ֶ���Ϣ
			price = rec.getFloat("price");
			//price=Float.parseFloat(new java.text.DecimalFormat("#.00").format(rec.getFloat("price")));
			name = rec.getStr("name");
			res = "ok";

		} else {
			price = 0;
			name = null;
			res = "no";
		}

		// ��װJson��
		try {

			// {"pPrice":3.200000047683716,"pName":"ţ��","pID":"123458"}
			System.out.println("name"+name);
			content.put("pId", pid);
			content.put("pName", name);
			content.put("pPrice", price);

			// {"res":"1","content":{"pPrice":3.200000047683716,"pName":"ţ��","pID":"123458"}}
			json.put("res", res);
			json.put("content", content);
			good.put("good", json);

			System.out.println("ret:" + good.toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// ��Ӧ
		try {
			HttpServletResponse response = this.getResponse();
			
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			writer.write(good.toString());
			writer.flush();
			writer.close();
			// this.renderJson(jsonText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.renderNull();
	}

	/**
	 * �����ύ
	 */
	public void palceoreder() {
		// �������� ������(oid)����¼����(total)����Ʒ������(pId)����Ʒ����(pNum)�������ܼ�(oSum)
		// ��������ʱ�䣬���뵽�����������š�ʱ�䡢�ܼۣ�
		// ��ѯ��Ʒ���õ���Ʒ������Ʒ����
		// ���� ��Ʒ�������Ʒ������Ʒ���ۡ���Ʒ�����������ţ�
		/*
		 * { ��oId��:��00001��, ��total��:3,��record��:[ {��pId��:��050043��,�� pNum��:��1�� },
		 * {��pId��:��050044��,�� pNum��:��2�� }, {��pId��:��050045��,�� pNum��:��1��
		 * }],��oSum��:��30.5��}
		 */
		String jsonContent = this.getPara("checkout");
//		String jsonContent = "{ oId:A20160816001, total:3,record:["
//				+ "{pId:123462,pNum:1},"
//				+ "{pId:123460,pNum:2},"
//				+ "{pId:123461,pNum:1}]," + "oSum:30.5}";
		String oid = "A20160816001";
		float osum = 0;
		int total = 0;
		Date date = new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat oidformat=new SimpleDateFormat("yyMMddHHmmss");
		String time=format.format(date);
		oid = oidformat.format(date);
		System.out.println("p2:"+time);
		System.out.println("oid:"+oid);
		try {
			// ����json����,��ȡ�����ţ������м�¼���������ܼ�
			JSONObject jsonObject = new JSONObject(jsonContent);

			//oid = jsonObject.getString("oId");
			total = Integer.parseInt(jsonObject.getString("total"));
			osum = Float.parseFloat(jsonObject.getString("oSum"));

			System.out.println("oid:" + oid + "total:" + total + "osum:" + osum);

			// ���涩����¼
			orderService.save(oid, time, osum);

			JSONArray jsonrecos = jsonObject.getJSONArray("record");
			JSONObject jsonreco = null;
			List<Record> recordList = new ArrayList<Record>();
			

			// ������� ��Ʒ������Ʒ�۸���������������
			String pID;
			String pName;
			float pPrice;
			String pNum;

			// ���������м�¼
			for (int i = 0; i < total; i++) {
				
				jsonreco = jsonrecos.getJSONObject(i);

				// ������Ʒ�ţ������Ʒ���Ƽ�����
				pID = jsonreco.getString("pId");
				System.out.println("pID:"+pID);
				
				Record product = productService.findByID(pID);

				// ��ȡ��Ʒ��Ϣ
				pName = product.getStr("name");
				pPrice = product.getFloat("price");
				pNum = jsonreco.getString("pNum");

				System.out.println("name:" + pName + "price:" + pPrice
						+ "pNum:" + pNum + "oid:" + oid);

				// ����Ʒ��Ϣ��װ�ɼ�¼
				Record record = new Record().set("name", pName).set("price", pPrice)
						.set("number", pNum).set("oid", oid);
				System.out.println("name:" + i+":"+record.getStr("name"));
				recordList.add(i, record);
			}
			
			for (Record record2 : recordList) {
				System.out.println("name:" + record2.getStr("name"));
			}

			// �����洢������ϸ��Ϣ��¼
			int[] res = orderService.batchsave(recordList, total);
			JSONObject resjson = new JSONObject();
			JSONObject order = new JSONObject();
			if (res.length > 0) {
				resjson.put("res", "ok");
			} else {
				resjson.put("res", "no");
			}
			order.put("order", resjson);
			// ��Ӧ
			try {
				HttpServletResponse response = this.getResponse();
				response.setContentType("application/json;charset=utf-8");
				PrintWriter writer = response.getWriter();
				writer.write(order.toString());
				writer.flush();
				writer.close();
				// this.renderJson(jsonText);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.renderNull();
	}
	
	/*
	 * ���ߣ�����
	 * ɾ����Ʒ
	 * */
	public void deleteById(){
		String jsonContent = this.getPara("goodsCode");//����"{pid:123458}"
		String pId = "123459";
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonContent);
			pId = jsonObject.getString("pId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean delete_result = analysisService.deleteMyGood(pId);
		if(delete_result == true){
			System.out.println("deleteById������"+"ɾ���ɹ���");
			this.renderHtml("ɾ���ɹ���" + pId);
		}
		else{
			System.out.println("deleteById������"+"ɾ��ʧ�ܣ�");
			this.renderHtml("ɾ��ʧ�ܣ�" + pId);
		}		
	}
	/*
	 * ���ߣ�����
	 * �༭��Ʒ
	 * */
	public void editGoods(){
		
		//ע��˴��ؼ���!!!!!!!
		String jsonContent = this.getPara("goods");
		
		String pId = new String();
		String pName = new String();
		double pPrice = 0;
		JSONObject jsonObject_goods;
		
		try {
			jsonObject_goods = new JSONObject(jsonContent);
			pId = jsonObject_goods.getString("pId");
			pName = jsonObject_goods.getString("pName");
			pName = new String(pName.getBytes("iso-8859-1"),"GBK");
			pPrice = jsonObject_goods.getDouble("pPrice"); //�ǵ�ת����float��ʽ�� 
			System.out.println("����editGoods���----"+pId + "," + pName + "," + pPrice);
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		int edit_result = analysisService.editMyGood(pId, pName, (float)pPrice);
		if(edit_result == 1){
			System.out.println("�༭�ɹ���"+pId + "," + pName + "," + pPrice);
			this.renderHtml("�༭�ɹ���");
		}
		else{
			System.out.println("�༭ʧ�ܣ�"+pId + "," + pName + "," + pPrice);
			this.renderHtml("�༭ʧ�ܣ�");
		}
	}
	
}
