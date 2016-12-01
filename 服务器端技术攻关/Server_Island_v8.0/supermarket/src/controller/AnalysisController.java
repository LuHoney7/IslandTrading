package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.attribute.standard.DateTimeAtCompleted;
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
	 * 
	 * 2016-11-27  ��Ʒ��ѯ
	 * ����pId��pName�������Ʒ����ϸ��Ϣ
	 * ������ʽ��http://localhost:8080/supermarket/analysis/lookupprice?pName={pName:��Ϊ}
	 * App��ע�ͣ�   App����Ҫ����json���󡢽���
	 * 			App����������Я��json��������keyΪ "pId"����"pName"��
	 * 			App��pId��pNameΪkeyֵ��������
	 * 			pIdĬ��Ϊ123456����ʱ��ΪAppû��Я��pId���������⣬��̨���Ҫ�ܿ���pId��
	 * 			pNameĬ��Ϊ��Default��,��ʱ��ΪAppû��Я��pName������
	 * 
	 * ����ע�ͣ�	App����������Я��json��������keyΪ "pId"����"pName"��
	 * 			pIdĬ��Ϊ123456����ʱ��ΪAppû��Я��pId���������⣬��̨���Ҫ�ܿ���pId��
	 * 			pNameĬ��Ϊ��Default��,��ʱ��ΪAppû��Я��pName������
	 * 			jsonContent_pId��jsonContent_pName��ͨ�������õ�����Ʒ��ϸ��Ϣ��(����ֻȡ��һ)
	 * 			ͨ��jsonContent_pId �� jsonContent_pName�½�JSONObject��������һ�������ݿ����ж�Ӧ���ɲ�ѯ�������
	 * 			ͨ�� res �ؼ��ּ�����ѯ�����"ok"Ϊ��ѯ����Ʒ��"no"Ϊû�в�ѯ��ָ����Ʒ
	 * 			һ��ע�� ���� ���⣡����ֻ���漰����ַ�е�����!!!
	 * 
	 * @throws UnsupportedEncodingException 
	 */
	public void lookupprice() throws UnsupportedEncodingException {

//		String jsonContent = this.getPara("goodsCode"); //goodsCode �� pid ��һ������		
		String jsonContent_pId = this.getPara("pId");
		String jsonContent_pName = this.getPara("pName");
		//String jsonContent = "{pid:123458}";	//�����������ʲô���ӣ�����Ľӿ���Ӧ��Ҳһ��
		String jsonContent = jsonContent_pId == null?jsonContent_pName:jsonContent_pId;

		String pid = "123456";	//Ĭ��pid��Ӧ����Ϊ�˷�ֹ��ȡ����pId���õ�
		String pName = "Default";	//Ĭ��pName��Ϊ�˷�ֹ��ȡ����pName���õ�
		String res = "ok";	//��ǣ���ѯ�ɹ�������ʱ�����Դ�keyΪ��ѯ�����okΪ������Ӧ��Ʒ��noΪ������
		float price;
//		String name;
		
		//���ڴ洢��ѯ����������
		String str_des;
		String str_type;
		String str_loc;
		Date date_time;
		String str_time;
		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();
		
		HttpServletResponse response = this.getResponse();
		
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		if(jsonContent_pId != null){
			System.out.println("Я����pId��"+jsonContent_pId + "  pName��" + jsonContent_pName);
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent);
				pid = jsonObject.getString("pId");	//�õ���Ʒpid
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("lookup price pId:" + pid + "  lookup pname pName:" + pName);
			Record rec_pId = analysisService.lookupprice(pid);
			
			//���û��ָ����Ʒ��ִֹͣ��
			if(rec_pId == null){
				res = "no";
				System.out.println("-----û��ָ����Ʒ lookupprice���� pId:" + pid);
				writer.write("û��ָ����Ʒ" + pid);
				this.renderNull();	
				return;
			}
			
			
			// ��ȡ��Ʒ�ֶ���Ϣ
			price = rec_pId.getFloat("price");
			//price=Float.parseFloat(new java.text.DecimalFormat("#.00").format(rec.getFloat("price")));
			pName = rec_pId.getStr("name");
			str_des = rec_pId.getStr("description");
			str_type = rec_pId.getStr("type");
			str_loc = rec_pId.getStr("location");
			date_time = rec_pId.getDate("time");
			str_time = (new SimpleDateFormat("yyyy-MM-dd")).format(date_time);
			
		}
		else{
			System.out.println("Я����pId��"+jsonContent_pId + "  pName��" + jsonContent_pName);
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent);
				String pName_temp = jsonObject.getString("pName");	//�õ���ƷpName
				pName = new String(pName_temp.getBytes("iso-8859-1"),"UTF-8");
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("lookup pId:" + pid + "  lookup pName:" + pName);
			Record rec_pName = analysisService.lookup_pName(pName);
			
			//���û��ָ����Ʒ��ִֹͣ��
			if(rec_pName == null){
				res = "no";
				System.out.println("-----û��ָ����Ʒ lookupprice���� pName:" + pName);
				writer.write("û��ָ����Ʒ" + pName);
				this.renderNull();	
				return;
			}
			// ��ȡ��Ʒ�ֶ���Ϣ
			price = rec_pName.getFloat("price");
			//price=Float.parseFloat(new java.text.DecimalFormat("#.00").format(rec.getFloat("price")));
			String pid_temp = String.valueOf(rec_pName.getInt("pid"));
			str_des = rec_pName.getStr("description");
			str_type = rec_pName.getStr("type");
			str_loc = rec_pName.getStr("location");
			date_time = rec_pName.getDate("time");
			str_time = (new SimpleDateFormat("yyyy-MM-dd")).format(date_time);
		}
		// ��װJson��
		try {

			// {"pPrice":3.200000047683716,"pName":"ţ��","pID":"123458"}
			System.out.println("name"+pName);
			content.put("pId", pid);
			content.put("pName", pName);
			content.put("pPrice", price);
			content.put("pDes", str_des);
			content.put("pType", str_type);
			content.put("pLoc", str_loc);
			content.put("pTime", str_time);
			

			// {"res":"1","content":{"pPrice":3.200000047683716,"pName":"ţ��","pID":"123458"}}
			json.put("res", res);
			json.put("content", content);
			good.put("good", json);

			System.out.println("----ret:" + good.toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		writer.write(good.toString());
		writer.flush();
		writer.close();
		// this.renderJson(jsonText);
		this.renderNull();
	}

	/*
	 * ����ɸѡ
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ������ʽ��http://localhost:8080/supermarket/analysis/type_collection?pType={pType:����}
	 * App��ע�ͣ��������ͼƬ����ȡ����ؼ��֣���ѯ����ͬ���͵�������Ʒ��
	 * ����ע�ͣ�ע�⣬��ַ�漰�����ģ�����ֻ������ַ���ļ��ɣ�
	 * 		 ע���޸�AnalysisBiz����õķ�����
	 * 		 Ŀǰֻ�ܵõ�һ����Ʒ��Ϣ��ͨ��ѭ����ﵽ��������ܣ�
	 * */
	public void type_collection(){
		String jsonContent = this.getPara("pType");	//�õ�Я���Ĳ���pType
		String type = "default";	//����Ĭ������
		String res = "ok";	//Ĭ�ϲ�ѯ���
		
		//���ڴ洢���ĳ��Ʒ����ϸ��Ϣ
		String str_pid = null;
		String str_name = null;
		float price = 0;
		String str_des = null;
		String str_loc = null;
		Date date_time;
		String str_time = null;
		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();
		System.out.println("----jsonContent:"+jsonContent.toString());
		if(jsonContent != null){
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent);
				String str_temp = jsonObject.getString("pType");
				type = new String(str_temp.getBytes("iso-8859-1"),"UTF-8");
				System.out.println("----���ķ�type:"+type);	//�ɹ��õ�����
				List<Record> list_Record_type = new ArrayList<>();
				list_Record_type = analysisService.lookup_type(type);
				
				PrintWriter writer;
				HttpServletResponse response = this.getResponse();
				
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();
				
				if(list_Record_type.size() == 0){
					res = "no";
					System.out.println("type_collection()------�����Ϊ�գ�"+type);
					writer.write(type + "�����û����Ʒ��");
					this.renderNull();
				}
				//ѭ�����ұ�
				for(Record rec_pType: list_Record_type){
					//��ȡ��Ʒ�ֶ���Ϣ
					str_pid = String.valueOf(rec_pType.getInt("pid"));
					str_name = rec_pType.getStr("name");
					price = rec_pType.getFloat("price");
					str_des = rec_pType.getStr("desciption");
					str_loc = rec_pType.getStr("location");
					date_time = rec_pType.getDate("time");
					str_time = (new SimpleDateFormat("yyyy-MM-dd")).format(date_time);
					//��װjson��
					try {
						System.out.println("name"+str_name);
						content.put("pId", str_pid);
						content.put("name", str_name);
						content.put("pPrice", price);
						content.put("pDes", str_des);
						content.put("pType", type);
						content.put("pLoc", str_loc);
						content.put("pTime", str_time);
						
						json.put("res", res);
						json.put("content", content);
						good.put("good", json);

						System.out.println("----good��:" + good.toString());
						
						writer.append(good.toString());
						
						// this.renderJson(jsonText);
						this.renderNull();		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//for
				writer.flush();
				writer.close();
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
			
			
			
				
		}//if(jsonContent != null)
	}
	
	/*
	 * �����б�
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ������ʽ��localhost:8080/supermarket/analysis/request_acts
	 * ����������Ҫ����
	 * ʵ�֣�App�����ȡ���л
	 * */
	public void request_acts(){
		String res = "ok";	//Ĭ�ϲ�ѯ���
		
		//���ڴ洢��õĻ��ϸ��Ϣ
		String str_id;
		String str_name;
		String str_dec;
		String str_organizer;
		Date date_temp;
		String str_time;
		String str_site;
		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();
			try {
				List<Record> list_Record_act = new ArrayList<>();
				list_Record_act = analysisService.lookup_act();
				
				PrintWriter writer;
				HttpServletResponse response = this.getResponse();
				
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();
				if(list_Record_act.size() == 0){
					System.out.println("request_acts()------��ʱû�л��");
					writer.write("��ʱû�л��");
					this.renderNull();
				}
				//ѭ�����ұ�
				for(Record rec_pType: list_Record_act){
					//��ȡ��Ʒ�ֶ���Ϣ
					str_id = String.valueOf(rec_pType.getInt("id"));
					str_name = rec_pType.getStr("name");
					str_dec = rec_pType.getStr("desciption");
					str_organizer = rec_pType.getStr("organizer");
					date_temp = rec_pType.getDate("time");
					str_time = (new SimpleDateFormat("yyyy-MM-dd")).format(date_temp);
					str_site = rec_pType.getStr("site");
					
					//��װjson��
					try {
						System.out.println("name"+str_name);
						content.put("pId", str_id);
						content.put("name", str_name);
						content.put("pDes", str_dec);
						content.put("organizer", str_organizer);
						content.put("pLoc", str_site);
						content.put("pTime", str_time);
						
						json.put("res", res);
						json.put("content", content);
						good.put("good", json);

						System.out.println("----good��:" + good.toString());
						
						writer.append(good.toString());
						
						// this.renderJson(jsonText);
						this.renderNull();		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//for
				writer.flush();
				writer.close();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		
		
		
	}
	
	/*
	 * �����ҵ��ղ�
	 * ���ߣ�������
	 * ���ڣ�2016-11-28
	 * ������ʽ��http://localhost:8080/supermarket/analysis/request_col?user={user:20161130}
	 * App��ע�ͣ�Я��id_user��id_goods����id�����ܻ�ȡ׼ȷ��Ʒ
	 * 			����ֻЯ��id_user���ﵽ��ȡĳ���ղص�Ч��
	 * ����ע�ͣ���������ȷ��׼ȷ��Ʒ��user����������Ʒ��
	 * 		 ��Ҫ����Я���Ĳ�����������α�ʾ������user�ش������ԣ����ж�goods_id����
	 * 		����ҳ����ʾ��ʾ��Ϣ��ͨ�� writer.write("  ");
	 * 		this.renderNull();֪ͨjfinal��ת���ĸ�ҳ�棬��仰Ӧ���ǲ���ת��
	 * 		Sql��ͨ��bit�洢java�е�booleanֵ����ֱ��  getboolean(coloum)
	 * 		
	 * */
	public void request_col(){
		String jsonContent_goods = this.getPara("goods");	
		String jsonContent_user = this.getPara("user");
		String goods = "default";
		String user = "default";
		String res = "ok";
		String str_goods_id = null;	//���ܲ�ʹ��
		boolean b_status;	//sql��bit���ͣ���Ҫת����
		Date time_temp;
		String str_time;
		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();
//		System.out.println("----jsonContent_user:"+jsonContent_user.toString() + "  " 
//		+ "----jsonContent_user:" +jsonContent_goods.toString());
		if(jsonContent_goods != null){
		try {
			JSONObject jsonObject_temp = new JSONObject(jsonContent_goods);
			goods = jsonObject_temp.getString("goods");
		} catch (JSONException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		}
		//���û��Я��goods_id
		if(goods.equals("default")){	//���û��Я��goods_id������Ʒid��ֻ����user����
			res = "no";
			System.out.println("----ִ��if��");
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent_user);
				user = jsonObject.getString("user");
				System.out.println("----���ķ�type:"+user);	//�ɹ��õ�����
				List<Record> list_Record_type = new ArrayList<>();
				list_Record_type = analysisService.lookup_col(user);
								
				PrintWriter writer;
				HttpServletResponse response = this.getResponse();
				
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();
				
				//����û����Ƿ�����ʾ û���ղ��κ���Ʒ
				if(list_Record_type.size() == 0){
					System.out.println("-----û��ƥ������ request_col if��֧.");
					writer.write("�û���"+user+" û���ղ��κ���Ʒ!");
					this.renderNull();
				}
				//ѭ�����ұ�
				for(Record rec_pType: list_Record_type){
					//��ȡ��Ʒ�ֶ���Ϣ
					str_goods_id = String.valueOf(rec_pType.getInt("id_goods"));					
					time_temp = rec_pType.getDate("date");
					str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(time_temp);
					b_status = rec_pType.getBoolean("status");
					//��װjson��
					try {
						content.put("pId", str_goods_id);
						content.put("user", user);
						content.put("pTime", str_time);
						content.put("status", b_status);
						
						json.put("res", res);
						json.put("content", content);
						good.put("good", json);

						System.out.println("----good��:" + good.toString());
						
						writer.append(good.toString());
						
						// this.renderJson(jsonText);
						this.renderNull();		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("-----�����쳣   e1 !!!");
						e.printStackTrace();
					}
				}//for
				writer.flush();
				writer.close();
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e2 !!!");
				e2.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e3 !!!");
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e4 !!!");
				e1.printStackTrace();
			} 
		}
		else{	//Я����goods_id������Ʒid��������������
			System.out.println("----ִ��else��");
			JSONObject jsonObject_goods;
			JSONObject jsonObject_user;
			try {
				System.out.println("----else�еõ��Ĳ�����jsonContent_user" + 
						jsonContent_user.toString() + "  jsonObject_goods:" + 
						jsonContent_goods.toString());
				jsonObject_user = new JSONObject(jsonContent_user);
				jsonObject_goods = new JSONObject(jsonContent_goods);
				user = jsonObject_user.getString("user");
				goods = jsonObject_goods.getString("goods");
				System.out.println("-----user:"+user+"  goods:"+goods);
				List<Record> list_Record_type = new ArrayList<>();
				list_Record_type = analysisService.lookup_col(user, goods);
				System.out.println(list_Record_type.toString()+"-----");	//list��û����
				PrintWriter writer;
				HttpServletResponse response = this.getResponse();
				
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();
				
				//����û����Ƿ�����ʾ û���ղ��κ���Ʒ
				if(list_Record_type.size() == 0){
					res = "no";
					System.out.println("-----û��ƥ������ request_col if��֧.");
					writer.write("��Ʒ�Ѳ����ڻ���������Ʒid�Ƿ���");
					this.renderNull();
				}
				//ѭ�����ұ�
				for(Record rec_pType: list_Record_type){
					//��ȡ��Ʒ�ֶ���Ϣ
					str_goods_id = String.valueOf(rec_pType.getInt("id_goods"));					
					time_temp = rec_pType.getDate("date");
					str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(time_temp);
					b_status = rec_pType.getBoolean("status");
					//��װjson��
					try {
						content.put("pId", str_goods_id);
						content.put("user", user);
						content.put("pTime", str_time);
						content.put("status", b_status);
						
						json.put("res", res);
						json.put("content", content);
						good.put("good", json);

						System.out.println("----good��:" + good.toString());
						
						writer.append(good.toString());
						
						// this.renderJson(jsonText);
						this.renderNull();		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("-----�����쳣   e1 !!!");
						e.printStackTrace();
					}
				}//for
				writer.flush();
				writer.close();
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e2 !!!");
				e2.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e3 !!!");
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("-----�����쳣   e4 !!!");
				e1.printStackTrace();
			} 
		}//else
		

	}
	
	/*
	 * �ύ������
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * App��ע�ͣ���ȡ�û���д����Ϣ��
	 * 		         ��Ҫ�淶�ϴ������ڸ�ʽ��
	 * ������ʽ��http://localhost:8080/supermarket/analysis/submit_fb?checkout={"id":"1","user_id":"2","content":"���","time":"2016-11-25 11:13:01"}
	 * ����ע�ͣ�Я������ checkout 
	 * */
	public void submit_fb(){
		String jsonContent = this.getPara("checkout");
		String str_id;	//������Ϣid���������������
		String str_user_id;	//�û�id������������ݶ��ش�
		String str_content;	//�������ݣ��ش�
//		Date time_date;	//ʱ�䣬�ش���
		String str_time;	
		boolean b_status;	//�����Ƿ��Ѿ������ˣ��ͻ��˲���Ҫ�ϴ���Ϊ�˱��������˴���һ��Ĭ��ֵ false
		try {
			JSONObject jsonObject = new JSONObject(jsonContent);
			str_id = jsonObject.getString("id");
			str_user_id = jsonObject.getString("user_id");
			String str_content_temp = jsonObject.getString("content");
			str_content = new String(str_content_temp.getBytes("iso-8859-1"),"UTF-8");
			str_time = jsonObject.getString("time");
			System.out.println("----str_time:"+str_time);
			b_status = false;	//ʲôʱ��ת���� bit��Ҫ���洢��ʱ��ת��һ�£������ύ����ַ֮�󣬻���ʲô����
			boolean b = orderService.subfb(str_id, str_user_id, str_content, str_time, b_status);
			System.out.println("-----submit_fb()��õĲ���jsonContent:" + jsonContent+"\nת�����Ч�� content:" + str_content);
			
			if(b == true){
				System.out.println("�����ύ�ɹ���id" + str_id + "  user_id:" + str_user_id);
				this.renderHtml("�����ύ�ɹ���");
				return;
			}
			else{
				System.out.println("�����ύʧ�ܣ�id" + str_id + "  user_id:" + str_user_id);
				this.renderHtml("�����ύʧ�ܣ�");
				return;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("-----submit_fb() �����쳣e1");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * ����û�ע�ᡢ��½������
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * ������ʽ��http://localhost:8080/supermarket/analysis/reg_log_user?mode=register&user=%E5%91%A8%E6%9D%B0%E4%BC%A6&pwd=123abcdefg
	 * App��ע�ͣ�	App��Я��mode��������֤��½check  ���� ע��register
	 * 			ע��ʱ��App���ܻ�ȡ��Ҫע���˺š����벢�ύ
	 * 			��¼ʱ��App���ύ�û�������û����������ύ�������ݿ�������֤����App�˷���
	 * ����ע�ͣ�
	 * */
	public void reg_log_user(){
		String str_mode = this.getPara("mode");	//������֤��ʽ����½����ע��
		
		//1.��̨��֤
		if(str_mode.equals("check")){
			String user_temp = this.getPara("user");	//ֱ�ӵõ����������ˣ�����json��
			String user = null;
			try {
				user = new String(user_temp.getBytes("iso-8859-1"),"UTF-8");
				System.out.println("----reg_log_user()�õ��Ĳ���--user:"+user);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String pwd = this.getPara("pwd");
			
			String str_nickname;
			int power;
			String res = "";	//���״̬���  ok , no
			
			//JSONObject����������װ�õ��Ĳ��������״̬��ǵ���Ϣ��
			JSONObject json = new JSONObject();
			JSONObject content = new JSONObject();
			JSONObject good = new JSONObject();
			
			//����ҳ����Ⱦ��Ϣ������̫�������
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
			Record mRecord = analysisService.lookup_user(user);
			if(mRecord == null){	//�û�������
				System.out.println("-----reg_log_user()--����ֻ��˵��������û�������" + user);
				writer.write("�û�������");
				writer.flush();
				writer.close();
				this.renderNull();
			}
			else{	//�û�����ȷ
				System.out.println("-----reg_log_user()�õ��ļ�¼:"+mRecord.toString());
				
				//�û�����֤ͨ�������������ݿ��е���Ϣ��������֤�����Ƿ���ȷ
				String str_pwd = mRecord.getStr("password");
				if(str_pwd.equals(pwd)){	//�û�����������ȷ
					System.out.println("�û�����������ȷ������");
					str_nickname = mRecord.getStr("nickname");
					power = mRecord.getInt("power");
					res = "ok";
					try {
						content.put("username", user);
						content.put("password", pwd);
						content.put("nickname", str_nickname);
						content.put("power", power);
						json.put("res", res);
						json.put("content", content);
						good.put("good", json);
						System.out.println("----reg_log_user������ good:"+good.toString());
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					writer.write(good.toString());
					writer.flush();
					writer.close();
					this.renderNull();
				}
				else{	//�û������������
					writer.write("�������");
					this.renderNull();
				}
			}
				
		}
		//2.����˻� http://localhost:8080/supermarket/analysis/reg_log_user?mode=check&user=%E7%94%A8%E6%88%B7%E5%90%8D&pwd=123a
		else if(str_mode.equals("register")){	
			String str_pwd = this.getPara("pwd");
			String str_nick_temp = this.getPara("nick");
			String str_user_temp = this.getPara("user");		//Я����һ�Ѳ���
			String str_user = null;
			String str_nick = "default nick";
			try {
				str_user = new String(str_user_temp.getBytes("iso-8859-1"),"UTF-8");
				if(!str_nick.equals("default nick")){
					str_nick = new String(str_nick_temp.getBytes("iso-8859-1"),"UTF-8");
				}
				boolean b_res = orderService.reg_user(str_user, str_pwd, str_nick);
				System.out.println("----ע��reg_log_user����  - ����˺�" + str_user);
				//��������ҳ����Ⱦ��Ϣ
				HttpServletResponse response = this.getResponse();
				response.setContentType("application/json;charset=utf-8");
				PrintWriter writer = response.getWriter();
				if(b_res == true){			//���˸��ܵͼ��Ĵ������� = ������
					writer.write("ע��ɹ��� user:" + str_user);
					writer.flush();
					writer.close();
					this.renderNull();
				}
				else{
					System.out.println("str_user::::" + str_user);
					writer.write("ע��ʧ�ܣ� user:" + str_user);
					writer.flush();
					writer.close();
					this.renderNull();
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}//else if
	}	
	
	/**
	 * �û������ύ
	 * ���ߣ�������
	 * ���ڣ�2016-11-29
	 * ������total��Ʒ������record��Ʒ��Ϣ��oSum�����ܼۣ�address���׵�ַ��telphone��ҵ绰
	 * 		����������Ʒid����һһ�����˺ö��أ�������������ƾͲ��ܶ��򣡣���
	 * 		
	 * �������磺/order={osm:30.0,address:"���ѧԺ",telphone:1234566666,user_id:700,pid:7878788}
	 * localhost:8080/supermarket/analysis/oreder?order={osm:30.0,address:"���ѧԺ",telphone:1234566666,user_id:700,pid:7878788}
	 */
	public void oreder() {
		
		String jsonContent = this.getPara("order");	//�õ�һ�Ѳ���
//		String jsonContent = "{ oId:A20160816001, total:3,record:["
//				+ "{pId:123462,pNum:1},"
//				+ "{pId:123460,pNum:2},"
//				+ "{pId:123461,pNum:1}]," + "oSum:30.5}";
		String oid = "";	//�������
		float osum = 0;		//�����ܼ�
		String address = "";	//���׵�ַ
		String telphone = "";	//��ҵ绰
		String user_id = "";	//��� user_id
		String pid = "";
		Date date = new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat oidformat=new SimpleDateFormat("yyMMddHHmmss");
		String time=format.format(date);	//������ʱ��ת��Stringʱ��
		oid = oidformat.format(date);	//������ʱ��ת�ɶ�����oid
		System.out.println("----time:"+time);
		System.out.println("----oid:"+oid);
		try {
			JSONObject jsonObject = new JSONObject(jsonContent);
			osum = Float.parseFloat(jsonObject.getString("osm"));
			String str_address = jsonObject.getString("address");
			String str_telphone = jsonObject.getString("telphone");
			String str_user_id = jsonObject.getString("user_id");
			pid = jsonObject.getString("pid");
			try {
				address = new String(str_address.getBytes("iso-8859-1"),"UTF-8");
				telphone = new String(str_telphone.getBytes("iso-8859-1"),"UTF-8");
				user_id = new String(str_user_id.getBytes("iso-8859-1"),"UTF-8");
//				pid = new String(str_pid.getBytes("iso-8856-1"),"UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			System.out.println("�õ���  ---address:" + address + "telphone:" 
					+ telphone + "user_id:" + user_id + "pid:" + pid);

			// ���涩����¼
			boolean b_res = orderService.save(oid, osum, address, telphone,user_id,pid);
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			if(b_res == true){	//��������ɹ�			
				writer.write("�����ύ�ɹ���" + oid);
				writer.flush();
				writer.close();
				this.renderNull();
			}
			else{	//��������ʧ��
				writer.write("�����ύʧ�ܣ�����" + oid);
				writer.flush();
				writer.close();
				this.renderNull();
			}
			
			
			
//			JSONArray jsonrecos = jsonObject.getJSONArray("record");	//��¼����Ʒ��Ϣ
//			JSONObject jsonreco = null;	//��ʱ����
//			List<Record> recordList = new ArrayList<Record>();	//������ĸ���Ʒ��Ϣ
			

//			// ������� ��Ʒ������Ʒ�۸���������������
//			String pID;
//			String pName;
//			float pPrice;
//			String pNum;
//
//			// ���������м�¼
//			for (int i = 0; i < total; i++) {
//				
//				jsonreco = jsonrecos.getJSONObject(i);
//
//				// ������Ʒ�ţ������Ʒ���Ƽ�����
//				pID = jsonreco.getString("pid");
//				System.out.println("pid:"+pID);
//				
//				Record product = productService.findByID(pID);
//
//				// ��ȡ��Ʒ��Ϣ
//				pName = product.getStr("name");
//				pPrice = product.getFloat("price");
//				pNum = jsonreco.getString("num");
//
//				System.out.println("name:" + pName + "price:" + pPrice
//						+ "pNum:" + pNum + "oid:" + oid);
//
//				// ����Ʒ��Ϣ��װ�ɼ�¼
//				Record record = new Record().set("name", pName).set("price", pPrice)
//						.set("number", pNum).set("oid", oid);
//				System.out.println("name:" + i+":"+record.getStr("name"));
//				recordList.add(i, record);
//			}
//			
//			for (Record record2 : recordList) {
//				System.out.println("name:" + record2.getStr("name"));
//			}
//
//			// �����洢������ϸ��Ϣ��¼
//			int[] res = orderService.batchsave(recordList, total);
//			JSONObject resjson = new JSONObject();
//			JSONObject order = new JSONObject();
//			if (res.length > 0) {
//				resjson.put("res", "ok");
//			} else {
//				resjson.put("res", "no");
//			}
//			order.put("order", resjson);
//			// ��Ӧ
//			try {
//				HttpServletResponse response = this.getResponse();
//				response.setContentType("application/json;charset=utf-8");
//				PrintWriter writer = response.getWriter();
//				writer.write(order.toString());
//				writer.flush();
//				writer.close();
//				// this.renderJson(jsonText);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.renderNull();
	}
	
	
	/**
	 * �����ύ
	 *  
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
//			orderService.save(oid, time, osum);

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
		String pId = "123459";	//Ĭ��pId
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
			pPrice = jsonObject_goods.getDouble("pPrice");  
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
