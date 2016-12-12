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
	 * 
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
	 * 2016-12-8�޸�
	 * ������ʽ��http://localhost:8080/supermarket/analysis/lookupprice?pId={PRODUCT_ID:3}
	 * 		http://localhost:8080/supermarket/analysis/lookupprice?pName={PRODUCT_NAME:Apple}
	 * @throws UnsupportedEncodingException 
	 */
	public void lookupprice() throws UnsupportedEncodingException {

//		String jsonContent = this.getPara("goodsCode"); //goodsCode �� pid ��һ������	
		//String jsonContent = "{pid:123458}";	//�����������ʲô���ӣ�����Ľӿ���Ӧ��Ҳһ��
		String jsonContent_pId = this.getPara("pId");	//�õ�����
		String jsonContent_pName = this.getPara("pName");	//�õ�����
		String jsonContent = jsonContent_pId == null?jsonContent_pName:jsonContent_pId;	//���Ȳ�ѯpid

		long pid = 0;	//Ĭ��pid��Ӧ����Ϊ�˷�ֹ��ȡ����pId���õ�
		String pName = "Default";	//Ĭ��pName��Ϊ�˷�ֹ��ȡ����pName���õ�
		String res = "ok";	//��ǣ���ѯ�ɹ�������ʱ�����Դ�keyΪ��ѯ�����okΪ������Ӧ��Ʒ��noΪ������
		float price;
//		String name;
		
		//���ڴ洢��ѯ����������
		String str_des;
		String str_type;  //��Ҫ�м���ѯ
		
		//��������
		String str_site;
		int hit = 0;
		int favour = 0;
		boolean status = false;
		boolean top = false;
		
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
				pid = jsonObject.getInt("PRODUCT_ID");	//�õ���Ʒpid
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.out.println("lookup price PRODUCT_ID:" + pid + "  lookup pname PRODUCT_NAME:" + pName);
			Record rec_pId = analysisService.lookupprice(pid);
			
			//���û��ָ����Ʒ��ִֹͣ��
			if(rec_pId == null){
				res = "no";
				System.out.println("-----û��ָ����Ʒ lookupprice���� PRODUCT_ID:" + pid);
				try {
					content.put("res", res);
					content.put("tip", "û��ָ����Ʒ   PRODUCT_ID:" + pid);
					good.put("PRODUCT", content);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				writer.write("û��ָ����Ʒ   PRODUCT_ID:" + pid);
				writer.write(good.toString());
				this.renderNull();	
				return;
			}
			
			
			// ��ȡ��Ʒ�ֶ���Ϣ
			price = rec_pId.getFloat("PRODUCT_PRICE");
			//price=Float.parseFloat(new java.text.DecimalFormat("#.00").format(rec.getFloat("price")));
			pName = rec_pId.getStr("PRODUCT_NAME");
			str_des = rec_pId.getStr("PRODUCT_DESCRIBE");
			str_type = analysisService.getClassify(pid);	//��Ҫ��ѯ�м��
			str_site = rec_pId.getStr("PRODUCT_SITE");
			date_time = rec_pId.getDate("RRODUCT_TIME");
			str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(date_time);
			str_site = rec_pId.getStr("PRODUCT_SITE");
			hit = rec_pId.getInt("PRODUCT_HIT");	//���ݿ����޸ĳ���smallint
			favour = rec_pId.getInt("PRODUCT_FAVOUR");	//���ݿ����޸ĳ���smallint
			status = rec_pId.getBoolean("PRODUCT_STATUS");
			top = rec_pId.getBoolean("PRODUCT_TOP");
		}
		else{	//ֻЯ����pName
			System.out.println("Я����pId��"+jsonContent_pId + "  pName��" + jsonContent_pName);
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent);
				String pName_temp = jsonObject.getString("PRODUCT_NAME");	//�õ���ƷpName
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
				writer.write("û��ָ����Ʒ PRODUCT_NEME:" + pName);
				this.renderNull();	
				return;
			}
			// ��ȡ��Ʒ�ֶ���Ϣ
			price = rec_pName.getFloat("PRODUCT_PRICE");
			//price=Float.parseFloat(new java.text.DecimalFormat("#.00").format(rec.getFloat("price")));
//			String pid_temp = String.valueOf(rec_pName.getLong("PRODUCT_ID"));
			pid = rec_pName.getLong("PRODUCT_ID");
			str_des = rec_pName.getStr("PRODUCT_DESCRIBE");
			str_type = analysisService.getClassify(pid);
			str_site = rec_pName.getStr("PRODUCT_SITE");
			date_time = rec_pName.getDate("RRODUCT_TIME");
			str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(date_time);
			
			str_site = rec_pName.getStr("PRODUCT_SITE");
			hit = rec_pName.getInt("PRODUCT_HIT");
			favour = rec_pName.getInt("PRODUCT_FAVOUR");
			status = rec_pName.getBoolean("PRODUCT_STATUS");
			top = rec_pName.getBoolean("PRODUCT_TOP");
		}
		// ��װJson��
		try {

			// {"pPrice":3.200000047683716,"pName":"ţ��","pID":"123458"}
			System.out.println("name"+pName);
			
			content.put("PRODUCT_ID", pid);
			content.put("PRODUCT_NAME", pName);
			content.put("PRODUCT_PRICE", price);
			content.put("PRODUCT_DESCRIBE", str_des);
			content.put("CLASSIFY_NAME", str_type);
			content.put("PRODUCT_SITE", str_site);
			content.put("PRODUCT_TIME", str_time);
			
			//����������
			content.put("PRODUCT_FAVOUR", favour);
			content.put("PRODUCT_HIT", hit);
			content.put("PRODUCT_STATUS", status);
			content.put("PRODUCT_TOP", top);
			content.put("PRODUCT_TYPE", str_type);
			

			// {"res":"1","content":{"pPrice":3.200000047683716,"pName":"ţ��","pID":"123458"}}
			json.put("res", res);
			json.put("content", content);
			good.put("PRODUCT", json);

			System.out.println("----ƴ�ӵ�PRODUCT:" + good.toString());
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
	 * 
	 * 2016-12-8�޸�,
	 * bug���֣�����ɸѡ����������json����Ҫ��json����������  []
	 * ������ʽ��http://localhost:8080/supermarket/analysis/type_collection?pType={pType:����PC}
	 * ����ֵ��JSONArray
	 * */
	public void type_collection(){
		String jsonContent = this.getPara("pType");	//�õ�Я���Ĳ���pType
		String str_type = "default";	//����Ĭ������
		String res = "ok";	//Ĭ�ϲ�ѯ���
		
		//���ڴ洢���ĳ��Ʒ����ϸ��Ϣ
		long pid = 0;
		String pName = null;
		float price = 0;
		String str_des = null;
		String str_site = null;
		Date date_time;
		String str_time = null;
		
		//��������
		int hit = 0;
		int favour = 0;
		boolean status = false;
		boolean top = false;
		
		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();
		JSONArray goods = new JSONArray();
		System.out.println("----�õ���jsonContent:"+jsonContent.toString());
		if(jsonContent != null){
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent);
				String str_temp = jsonObject.getString("pType");
				str_type = new String(str_temp.getBytes("iso-8859-1"),"UTF-8");
				System.out.println("----���ķ� type:"+str_type);	//�ɹ��õ�����
				List<Record> list_Record_type = new ArrayList<>();
				list_Record_type = analysisService.lookup_type(str_type);
				
				PrintWriter writer;
				HttpServletResponse response = this.getResponse();
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();
				
				if(list_Record_type.size() == 0){
					res = "no";
					System.out.println("type_collection()------���������ƷΪ�գ�"+str_type);
					writer.write(str_type + "�����û����Ʒ��");
					this.renderNull();
				}
//				int num = 0;
				//ѭ�����ұ�
				for(Record rec_pType: list_Record_type){
					content = null;
					json = null;
					good = null;
					content = new JSONObject();
					json = new JSONObject();
					good = new JSONObject();
					//��ȡ��Ʒ�ֶ���Ϣ
					pid = rec_pType.getLong("PRODUCT_ID");	
					pName = rec_pType.getStr("PRODUCT_NAME");
					price = rec_pType.getFloat("PRODUCT_PRICE");
					str_des = rec_pType.getStr("PRODUCT_DESCRIBE");
					str_site = rec_pType.getStr("PRODUCT_SITE");
					date_time = rec_pType.getDate("RRODUCT_TIME");
					str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(date_time);
					
					hit = rec_pType.getInt("PRODUCT_HIT");
					favour = rec_pType.getInt("PRODUCT_FAVOUR");
					status = rec_pType.getBoolean("PRODUCT_STATUS");
					top = rec_pType.getBoolean("PRODUCT_TOP");
					//��װjson��
					try {
						System.out.println("name"+pName);
						content.put("PRODUCT_ID", pid);
						content.put("PRODUCT_NAME", pName);
						content.put("PRODUCT_PRICE", price);
						content.put("PRODUCT_DESCRIBE", str_des);
						content.put("CLASSIFY_NAME", str_type);
						content.put("PRODUCT_SITE", str_site);
						content.put("PRODUCT_TIME", str_time);
						
						content.put("PRODUCT_HIT", hit);
						content.put("PRODUCT_FAVOUR", favour);
						content.put("PRODUCT_STATUS", status);
						content.put("PRODUCT_TOP", top);
						
						json.put("res", res);
						json.put("content", content);
						good.put("good", json);
						goods.put(good);
						
						System.out.println("----good��:" + good.toString());
						
//						writer.append(good.toString());
						
						// this.renderJson(jsonText);
						this.renderNull();		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//for
				writer.write(goods.toString());
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
	 * 
	 * 2016-12-8�޸�
	 * ������ʽ��localhost:8080/supermarket/analysis/request_acts
	 * 
	 * */
	public void request_acts(){
		String res = "ok";	//Ĭ�ϲ�ѯ���
		
		//���ڴ洢��õĻ��ϸ��Ϣ
		long id;
		String str_name;
		String str_content;
		String str_organizer;
		Date date_temp;
		String str_time;
		String str_site;
		
		JSONObject json = new JSONObject();
		JSONObject content = new JSONObject();
		JSONObject good = new JSONObject();
		JSONArray goods = new JSONArray();
			try {
				List<Record> list_Record_act = new ArrayList<>();
				list_Record_act = analysisService.lookup_act();
				
				PrintWriter writer;
				HttpServletResponse response = this.getResponse();			
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();
				if(list_Record_act.size() == 0){
					res = "no";
					System.out.println("request_acts()------��ʱû�л��");
					writer.write("��ʱû�л��");
					this.renderNull();
				}
				int num = 0;
				//ѭ�����ұ�
				for(Record rec_pType: list_Record_act){
					content = null;
					json = null;
					good = null;
					content = new JSONObject();
					json = new JSONObject();
					good = new JSONObject();
					System.out.println("-----��Χfor��"+rec_pType.toString());
					//��ȡ��Ʒ�ֶ���Ϣ
					id = rec_pType.getLong("ACTIVITY_ID");
					str_name = rec_pType.getStr("ACTIVITY_NAME");
					str_content = rec_pType.getStr("ACTIVITY_CONTENT");
					str_organizer = rec_pType.getStr("ACTIVITY_ORGANIZER");
					date_temp = rec_pType.getDate("ACTIVITY_TIME");
					str_time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(date_temp);
					str_site = rec_pType.getStr("ACTIVITY_SITE");
					
					//��װjson��
					try {
						System.out.println("name"+str_name);
						content.put("ACTIVITY_ID", id);
						content.put("ACTIVITY_NAME", str_name);
						content.put("ACTIVITY_CONTENT", str_content);
						content.put("ACTIVITY_ORGANIZER", str_organizer);
						content.put("ACTIVITY_SITE", str_site);
						content.put("ACTIVITY_TIME", str_time);
						
						json.put("res", res);
						json.put("content", content);
						good.put("good", json);
						goods.put(num++,good);
						System.out.println("----good��:" + good.toString());
						
//						writer.append(good.toString());
						
						// this.renderJson(jsonText);
						this.renderNull();		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//for
				writer.write(goods.toString());
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
	 * �༭�
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ������act 
	 * ������ʽ��http://localhost:8080/supermarket/analysis/editActs?act={ACTIVITY_ID:1,ACTIVITY_CONTENT:������޸�,ACTIVITY_ORGANIZER:���֯�޸�,ACTIVITY_TIME:'2016-12-12 12:12:12',ACTIVITY_SITE:��ص��޸�,ACTIVITY_NAME:����޸�}
	 * ������
	 * */
	public void editActs(){
		String jsonContent = this.getPara("act");
		System.out.println("----act���:"+jsonContent);
		
//		//�洢Ҫ���ĵ�����
		String ACTIVITY_ID;
		String ACTIVITY_CONTENT;
		String ACTIVITY_ORGANIZER;
		String ACTIVITY_TIME;
		String ACTIVITY_SITE;
		String ACTIVITY_NAME;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonContent);
			ACTIVITY_ID = jsonObject.getString("ACTIVITY_ID");
			ACTIVITY_CONTENT = new String((jsonObject.getString("ACTIVITY_CONTENT")).getBytes("iso-8859-1"),"UTF-8");
			ACTIVITY_ORGANIZER = new String((jsonObject.getString("ACTIVITY_ORGANIZER")).getBytes("iso-8859-1"),"UTF-8");
			ACTIVITY_TIME = jsonObject.getString("ACTIVITY_TIME");
			ACTIVITY_SITE = new String((jsonObject.getString("ACTIVITY_SITE")).getBytes("iso-8859-1"),"UTF-8");
			ACTIVITY_NAME = new String((jsonObject.getString("ACTIVITY_NAME")).getBytes("iso-8859-1"),"UTF-8");
			
//			System.out.println("��õĲ�������ô��-----"+ACTIVITY_CONTENT + "  " + 
//					ACTIVITY_ORGANIZER + " " + ACTIVITY_TIME + " " + 
//					ACTIVITY_SITE);
			
			int res = orderService.edit_act(ACTIVITY_ID, ACTIVITY_CONTENT, ACTIVITY_ORGANIZER, 
					ACTIVITY_TIME, ACTIVITY_SITE, ACTIVITY_NAME);
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			if(res != 0){	//�༭��ɹ�
				writer.write("��༭�ɹ���ACTIVITY_ID��" + ACTIVITY_ID);
				writer.flush();
				writer.close();
				this.renderNull();
				
			}
			else{
				writer.write("��༭ʧ�ܣ�ACTIVITY_ID��" + ACTIVITY_ID);
				writer.flush();
				writer.close();
				this.renderNull();
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * ɾ������û�������
	 * ���ߣ�������
	 * ���ڣ�2016-12-11
	 * ע�ͣ�APP��ƴ������ӿڣ�APP�˻�ȡ�id���϶����ԣ�
	 * 		�жϻ�ǲ��Ǵ��û�������
	 * ������
	 * ������ʽ��http://localhost:8080/supermarket/analysis/deleteAct?ACTIVITY_ID=1&USER_ID=1
	 * */
	public void deleteAct(){
		String ACTIVITY_ID = this.getPara("ACTIVITY_ID");
		String USER_ID = this.getPara("USER_ID");
		String user_id = orderService.fetch_User_By_Act(ACTIVITY_ID);	//�ҵ���ACTIVITY_ID�ķ�����
		
		HttpServletResponse response = this.getResponse();
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(user_id.equals(USER_ID) ){	//��activity�����ߺ�APP��½��userid��ͬ
			boolean res = orderService.del_act(USER_ID, ACTIVITY_ID);
			if(res == true){
				writer.write("ɾ���ɹ�! ACTIVITY_ID��" + ACTIVITY_ID);
				writer.flush();
				writer.close();
				this.renderNull();
			}
			else{
				writer.write("ɾ��ʧ��! ACTIVITY_ID��" + ACTIVITY_ID);
				writer.flush();
				writer.close();
				this.renderNull();
			}
		}//if(user_id == USER_ID)
		else{	//APP��½�߲���ɾ�����˷����Ļ������Ҳ�߲��������֧����
			writer.write("����ɾ�����˵Ļ! ACTIVITY_ID��" + ACTIVITY_ID);
			writer.flush();
			writer.close();
			this.renderNull();
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
	 * 2016-12-8�޸�
	 * ������ʽ��http://localhost:8080/supermarket/analysis/request_col?user_id={user_id:20161130}
	 * 		http://localhost:8080/supermarket/analysis/request_col?user_id={user_id:20161130}&goods_id={goods_id:2}
	 * 		
	 * */
	public void request_col(){
		String jsonContent_goods = this.getPara("goods_id");	
		String jsonContent_user = this.getPara("user_id");
		String goods_id = "default";	//�õ�goods_id
		String user_id = null;
		
		//�洢��Ʒ��Ϣ��
		long PRODUCT_ID = 0;
		String PRODUCT_NAME = null;
		float PRODUCT_PRICE = 0;
		String PRODUCT_DESCRIBE = null;
		Date time_temp;
		String RRODUCT_TIME = null;
		String PRODUCT_SITE = null;
		int PRODUCT_HIT = 0;
		int PRODUCT_FAVOUR = 0;
		boolean PRODUCT_STATUS = false;
		boolean PRODUCT_TOP = false;
		
		JSONObject json = null;
		JSONObject content = null;
		JSONObject good = null;
		JSONArray goods = new JSONArray();
		if(jsonContent_goods != null){
		try {
			JSONObject jsonObject_temp = new JSONObject(jsonContent_goods);
			goods_id = jsonObject_temp.getString("goods_id");
		} catch (JSONException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		}
		//���û��Я��goods_id
		if(goods_id.equals("default")){	//û��Я��goods_id��ֻ����user����
//			res = "ok";
			System.out.println("----ִ��if��");
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonContent_user);
				user_id = jsonObject.getString("user_id");
				List<Record> list_Record_Col = new ArrayList<>();
				list_Record_Col = analysisService.lookup_col(user_id);
								
				PrintWriter writer;
				HttpServletResponse response = this.getResponse();			
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();
				
				//����û����Ƿ�����ʾ û���ղ��κ���Ʒ
				if(list_Record_Col.size() == 0){
					writer.write("�û���"+user_id+" û���ղ��κ���Ʒ!");
					this.renderNull();
					return;
				}
				//ѭ�����ұ�
				for(Record rec_pType: list_Record_Col){
					json = new JSONObject();
					content = new JSONObject();
					good = new JSONObject();
					//��ȡ��Ʒ�ֶ���Ϣ
					PRODUCT_ID = rec_pType.getLong("PRODUCT_ID");
					PRODUCT_NAME = rec_pType.getStr("PRODUCT_NAME");
					PRODUCT_PRICE = rec_pType.getFloat("PRODUCT_PRICE");
					PRODUCT_DESCRIBE = rec_pType.getStr("PRODUCT_DESCRIBE");
					RRODUCT_TIME = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(rec_pType.getDate("RRODUCT_TIME"));
					PRODUCT_SITE = rec_pType.getStr("PRODUCT_SITE");
					PRODUCT_HIT = rec_pType.getInt("PRODUCT_HIT");
					PRODUCT_FAVOUR = rec_pType.getInt("PRODUCT_FAVOUR");
					PRODUCT_STATUS = rec_pType.getBoolean("PRODUCT_STATUS");
					PRODUCT_TOP = rec_pType.getBoolean("PRODUCT_TOP");
					
				
					//��װjson��
					try {
						content.put("PRODUCT_ID", PRODUCT_ID);
						content.put("PRODUCT_NAME", PRODUCT_NAME);	
						content.put("PRODUCT_PRICE", PRODUCT_PRICE);
						content.put("PRODUCT_DESCRIBE", PRODUCT_DESCRIBE);
						content.put("RRODUCT_TIME", RRODUCT_TIME);
						content.put("PRODUCT_SITE", PRODUCT_SITE);
						content.put("PRODUCT_HIT", PRODUCT_HIT);
						content.put("PRODUCT_FAVOUR", PRODUCT_FAVOUR);
						content.put("PRODUCT_STATUS", PRODUCT_STATUS);
						content.put("PRODUCT_TOP", PRODUCT_TOP);
						
						json.put("content", content);
						good.put("good", json);
						goods.put(good);
						
						System.out.println("----good��:" + good.toString());
						
//						writer.append(good.toString());
						
						// this.renderJson(jsonText);
						this.renderNull();		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("-----�����쳣   e1 !!!");
						e.printStackTrace();
					}
				}//for
				writer.write(goods.toString());
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
			content = new JSONObject();
			json = new JSONObject();
			good = new JSONObject();
			
			System.out.println("----ִ��else��");
			JSONObject jsonObject_goods;
			JSONObject jsonObject_user;
			try {
				System.out.println("----else�еõ��Ĳ�����jsonContent_user" + 
						jsonContent_user.toString() + "  jsonObject_goods:" + 
						jsonContent_goods.toString());
				jsonObject_user = new JSONObject(jsonContent_user);
				jsonObject_goods = new JSONObject(jsonContent_goods);
				user_id = jsonObject_user.getString("user_id");
				goods_id = jsonObject_goods.getString("goods_id");
				System.out.println("-----user:"+user_id+"  goods:"+goods_id);
//				List<Record> list_Record_type = new ArrayList<>();
				Record rec_pType = analysisService.lookup_col(user_id, goods_id);
				System.out.println(rec_pType.toString()+"-----");	//list��û����
				PrintWriter writer;
				HttpServletResponse response = this.getResponse();				
				response.setContentType("application/json;charset=utf-8");
				writer = response.getWriter();
				
				//����û����Ƿ�����ʾ û���ղ��κ���Ʒ
				if(rec_pType == null){
					System.out.println("-----û��ƥ������ request_col if��֧.");
					writer.write("��Ʒ�Ѳ����ڻ���������Ʒid�Ƿ���");
					this.renderNull();
				}
				//ѭ�����ұ�
//				for(Record rec_pType: list_Record_type){
					//��ȡ��Ʒ�ֶ���Ϣ
					PRODUCT_ID = rec_pType.getLong("PRODUCT_ID");
					PRODUCT_NAME = rec_pType.getStr("PRODUCT_NAME");
					PRODUCT_PRICE = rec_pType.getFloat("PRODUCT_PRICE");
					PRODUCT_DESCRIBE = rec_pType.getStr("PRODUCT_DESCRIBE");
					RRODUCT_TIME = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(rec_pType.getDate("RRODUCT_TIME"));
					PRODUCT_SITE = rec_pType.getStr("PRODUCT_SITE");
					PRODUCT_HIT = rec_pType.getInt("PRODUCT_HIT");
					PRODUCT_FAVOUR = rec_pType.getInt("PRODUCT_FAVOUR");
					PRODUCT_STATUS = rec_pType.getBoolean("PRODUCT_STATUS");
					PRODUCT_TOP = rec_pType.getBoolean("PRODUCT_TOP");
					//��װjson��
					try {
						content.put("PRODUCT_ID", PRODUCT_ID);
						content.put("PRODUCT_NAME", PRODUCT_NAME);	
						content.put("PRODUCT_PRICE", PRODUCT_PRICE);
						content.put("PRODUCT_DESCRIBE", PRODUCT_DESCRIBE);
						content.put("RRODUCT_TIME", RRODUCT_TIME);
						content.put("PRODUCT_SITE", PRODUCT_SITE);
						content.put("PRODUCT_HIT", PRODUCT_HIT);
						content.put("PRODUCT_FAVOUR", PRODUCT_FAVOUR);
						content.put("PRODUCT_STATUS", PRODUCT_STATUS);
						content.put("PRODUCT_TOP", PRODUCT_TOP);
						
						json.put("content", content);
						good.put("good", json);
						goods.put(good);
						System.out.println("----good��:" + good.toString());
						
						writer.write(good.toString());
						
						// this.renderJson(jsonText);
						this.renderNull();		
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						System.out.println("-----�����쳣   e1 !!!");
						e.printStackTrace();
					}
//				}//for
//				writer.write(goods.toString());
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
	 * 
	 * 2016-12-8�޸�
	 * �޸ģ��ؼ��ָ�Ϊ  fb;statusΪfalse��true�����ݿ��Ӧ�洢Ϊ0��1
	 * ������ʽ��http://localhost:8080/supermarket/analysis/submit_fb?fb={"FB_ID":"4","FB_CONTENT":"���Ŀ���","FB_CONTACT":"1523015666","FB_TIME":%"2016-12-9 20:20:39","FB_STATUS":"false"}
	 * 
	 * */
	public void submit_fb(){
		String jsonContent = this.getPara("fb");	
		
		String FB_ID;
		String FB_CONTENT;
		String FB_CONTACT;
		String FB_TIME;
		String FB_STATUS;
		
		try {
			JSONObject jsonObject = new JSONObject(jsonContent);
			FB_ID = jsonObject.getString("FB_ID");
			FB_CONTENT = new String((jsonObject.getString("FB_CONTENT")).getBytes("iso-8859-1"),"UTF-8");
			FB_CONTACT = jsonObject.getString("FB_CONTACT");
			FB_TIME = jsonObject.getString("FB_TIME");	
			FB_STATUS = jsonObject.getString("FB_STATUS");
			
			boolean b = orderService.subfb(FB_ID, FB_CONTENT, FB_CONTACT, FB_TIME, FB_STATUS);
			System.out.println("-----submit_fb()��õĲ���jsonContent:" + jsonContent+"\nת�����Ч�� content:" + jsonContent);
			
			if(b == true){
				System.out.println("�����ύ�ɹ���id" + FB_ID);
				this.renderHtml("�����ύ�ɹ���");
				return;
			}
			else{
				System.out.println("�����ύʧ�ܣ�id" + FB_ID);
				this.renderHtml("�����ύʧ�ܣ�id��" + FB_ID);
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
	 * ������ʽ��http://localhost:8080/supermarket/analysis/reg_log_user?mode=check&USER_USERNAME=������&USER_PASSWORD=123abc
	 * App��ע�ͣ�	App��Я��mode��������֤��½check  ���� ע��register
	 * 			ע��ʱ��App���ܻ�ȡ��Ҫע���˺š����벢�ύ
	 * 			��¼ʱ��App���ύ�û�������û����������ύ�������ݿ�������֤����App�˷���
	 * ����ע�ͣ�
	 * 
	 * 2016-12-8�޸�
	 * ע��ֻ�������ݿ�д����½�����ݿ��������֤�Ƿ���ȷ
	 * ������ʽ��http://localhost:8080/supermarket/analysis/reg_log_user?mode=check&USER_USERNAME=������&USER_PASSWORD=123abc
	 * 		http://localhost:8080/supermarket/analysis/reg_log_user?mode=register&USER_NICKNAME=����&USER_USERNAME=�������û���&USER_PASSWORD=1234&USER_TAKINGID=15686565&USER_CONTACT=15230153136
	 * */
	public void reg_log_user(){
		String str_mode = this.getPara("mode");	//������֤��ʽ����½����ע��
		
		//1.��̨��֤
		if(str_mode.equals("check")){
			String user_temp = this.getPara("USER_USERNAME");	//ֱ�ӵõ����������ˣ�����json��
			
			String pwd = this.getPara("USER_PASSWORD");
			String user = null;
			try {
				user = new String(user_temp.getBytes("iso-8859-1"),"UTF-8");
				System.out.println("----reg_log_user()�õ��Ĳ���--user:"+user);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String str_nickname;
			
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
				writer.write("�û��������ڣ�");
				writer.flush();
				writer.close();
				this.renderNull();
			}
			else{	//�û�����ȷ
				System.out.println("-----reg_log_user()�õ��ļ�¼:"+mRecord.toString());
				
				//�û�����֤ͨ�������������ݿ��е���Ϣ��������֤�����Ƿ���ȷ
				String str_pwd = mRecord.getStr("USER_PASSWORD");
				if(str_pwd.equals(pwd)){	//�û�����������ȷ
					System.out.println("�û�����������ȷ������");
					str_nickname = mRecord.getStr("USER_NICKNAME");
//					try {
//						content.put("USER_USERNAME", user);
//						content.put("USER_PASSWORD", pwd);
//						content.put("USER_NICKNAME", str_nickname);
////						content.put("power", power);
////						json.put("res", res);
//						json.put("content", content);
//						good.put("good", json);
//						System.out.println("----reg_log_user������ good:"+good.toString());
						
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					writer.write("�û���¼�ɹ���");
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
		//2.����˻� http://localhost:8080/supermarket/analysis/reg_log_user?mode=register&USER_NICKNAME=����&USER_USERNAME=�������û���&USER_PASSWORD=1234&USER_TAKINGID=15686565&USER_CONTACT=15230153136
		else if(str_mode.equals("register")){	
			try {
				String USER_NICKNAME = new String((this.getPara("USER_NICKNAME")).getBytes("iso-8859-1"),"UTF-8");
				String USER_USERNAME = new String((this.getPara("USER_USERNAME")).getBytes("iso-8859-1"),"UTF-8");
				String USER_PASSWORD = this.getPara("USER_PASSWORD");
				String USER_TAKINGID = this.getPara("USER_TAKINGID");
				String USER_CONTACT = this.getPara("USER_CONTACT");
				boolean res = orderService.reg_user(USER_NICKNAME,USER_USERNAME,USER_PASSWORD,
						USER_TAKINGID,USER_CONTACT);
				
				//��������ҳ����Ⱦ��Ϣ
				HttpServletResponse response = this.getResponse();
				response.setContentType("application/json;charset=utf-8");
				PrintWriter writer = response.getWriter();
				
				if(res == true){			
					writer.write("ע��ɹ��� USER_USERNAME:" + USER_USERNAME);
					writer.flush();
					writer.close();
					this.renderNull();
				}
				else{
					System.out.println("str_user::::" + USER_USERNAME);
					writer.write("ע��ʧ�ܣ� USER_USERNAME:" + USER_USERNAME);
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
			
			
			
			
//			String str_nick_temp = this.getPara("nick");
//			String str_user_temp = this.getPara("user");		//Я����һ�Ѳ���
//			String str_user = null;
//			String str_nick = "default nick";
//			try {
//				str_user = new String(str_user_temp.getBytes("iso-8859-1"),"UTF-8");
//				if(!str_nick.equals("default nick")){
//					str_nick = new String(str_nick_temp.getBytes("iso-8859-1"),"UTF-8");
//				}
//				boolean b_res = orderService.reg_user(str_user, str_pwd, str_nick);
//				System.out.println("----ע��reg_log_user����  - ����˺�" + str_user);
//				//��������ҳ����Ⱦ��Ϣ
//				HttpServletResponse response = this.getResponse();
//				response.setContentType("application/json;charset=utf-8");
//				PrintWriter writer = response.getWriter();
//				if(b_res == true){			//���˸��ܵͼ��Ĵ������� = ������
//					writer.write("ע��ɹ��� user:" + str_user);
//					writer.flush();
//					writer.close();
//					this.renderNull();
//				}
//				else{
//					System.out.println("str_user::::" + str_user);
//					writer.write("ע��ʧ�ܣ� user:" + str_user);
//					writer.flush();
//					writer.close();
//					this.renderNull();
//				}
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

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
	 * 
	 * 2016-12-8�޸�
	 * localhost:8080/supermarket/analysis/oreder?order={ORDER_ID:1001,ORDER_SITE:�ӱ�ʦ��,ORDER_TIME:'2016-12-8 20:46:55',OEDER_STATUS:true}
	 * 
	 */
	public void oreder() {
		
		String jsonContent = this.getPara("order");	//�õ�һ�Ѳ���
//		String jsonContent = "{ oId:A20160816001, total:3,record:["
//				+ "{pId:123462,pNum:1},"
//				+ "{pId:123460,pNum:2},"
//				+ "{pId:123461,pNum:1}]," + "oSum:30.5}";
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(jsonContent);
			//�ύ���ݣ�������ΪString
			String ORDER_ID = jsonObject.getString("ORDER_ID");
			String ORDER_SITE = new String((jsonObject.getString("ORDER_SITE")).getBytes("iso-8859-1"),"UTF-8");
			String ORDER_TIME = jsonObject.getString("ORDER_TIME");
			String OEDER_STATUS = jsonObject.getString("OEDER_STATUS");
			boolean res = orderService.save(ORDER_ID, ORDER_SITE, ORDER_TIME, OEDER_STATUS);
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			
			if(res == true){
				writer.write("�����ύ�ɹ���" + ORDER_ID);
				writer.flush();
				writer.close();
				this.renderNull();
			}
			else{
				writer.write("�����ύʧ�ܣ�����" + ORDER_ID);
				writer.flush();
				writer.close();
				this.renderNull();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
		
		
//		String oid = "";	//�������
//		float osum = 0;		//�����ܼ�
//		String address = "";	//���׵�ַ
//		String telphone = "";	//��ҵ绰
//		String user_id = "";	//��� user_id
//		String pid = "";
//		Date date = new Date();
//		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		DateFormat oidformat=new SimpleDateFormat("yyMMddHHmmss");
//		String time=format.format(date);	//������ʱ��ת��Stringʱ��
//		oid = oidformat.format(date);	//������ʱ��ת�ɶ�����oid
//		System.out.println("----time:"+time);
//		System.out.println("----oid:"+oid);
//		try {
//			JSONObject jsonObject = new JSONObject(jsonContent);
//			osum = Float.parseFloat(jsonObject.getString("osm"));
//			String str_address = jsonObject.getString("address");
//			String str_telphone = jsonObject.getString("telphone");
//			String str_user_id = jsonObject.getString("user_id");
//			pid = jsonObject.getString("pid");
//			try {
//				address = new String(str_address.getBytes("iso-8859-1"),"UTF-8");
//				telphone = new String(str_telphone.getBytes("iso-8859-1"),"UTF-8");
//				user_id = new String(str_user_id.getBytes("iso-8859-1"),"UTF-8");
////				pid = new String(str_pid.getBytes("iso-8856-1"),"UTF-8");
//			} catch (UnsupportedEncodingException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			
//			System.out.println("�õ���  ---address:" + address + "telphone:" 
//					+ telphone + "user_id:" + user_id + "pid:" + pid);
//
//			// ���涩����¼
//			boolean b_res = orderService.save(oid, osum, address, telphone,user_id,pid);
//			HttpServletResponse response = this.getResponse();
//			response.setContentType("application/json;charset=utf-8");
//			PrintWriter writer = response.getWriter();
//			if(b_res == true){	//��������ɹ�			
//				writer.write("�����ύ�ɹ���" + oid);
//				writer.flush();
//				writer.close();
//				this.renderNull();
//			}
//			else{	//��������ʧ��
//				writer.write("�����ύʧ�ܣ�����" + oid);
//				writer.flush();
//				writer.close();
//				this.renderNull();
//			}
			
			
			
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
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

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
	 * 
	 * 2016-12-8�޸�
	 * ������ʽ��http://localhost:8080/supermarket/analysis/deleteById?goodsCode={PRODUCT_ID:123456}	Ӣ�ķ��ţ�����
	 * */
	public void deleteById(){
		String jsonContent = this.getPara("goodsCode");//����"{pid:123458}"
		String pId = "123456";	//Ĭ��pId
		JSONObject jsonObject;	
		try {
			jsonObject = new JSONObject(jsonContent);
			pId = jsonObject.getString("PRODUCT_ID");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean delete_result = analysisService.deleteMyGood(pId);
		if(delete_result == true){
			System.out.println("deleteById"+"ɾ���ɹ���");
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
	 * 
	 * 2016-12-8�޸�
	 * ����ʱ��ʼ��Ϊ�༭ʱ�䣬��̨�Զ����ã�
	 * �����Ա༭����������������ö���
	 * Я�����ٲ�������Ӧ������Ĭ�ϲ���
	 * ������ʽ��http://localhost:8080/supermarket/analysis/editGoods?goodsCode={PRODUCT_ID:123456,PRODUCT_NAME:�ֻ�,PRODUCT_PRICE:50,PRODUCT_DESCRIBE:�ݻ��ֻ�,PRODUCT_SITE:�����ص�,PRODUCT_STATUS:true}	
	 * */
	public void editGoods(){
		
		String jsonContent = this.getPara("goodsCode");
		
		String PRODUCT_ID;
		String PRODUCT_NAME;
		String PRODUCT_PRICE;
		String PRODUCT_DESCRIBE;
		String PRODUCT_SITE;
		String PRODUCT_STATUS;
		try {
			JSONObject jsonObject = new JSONObject(jsonContent);
			PRODUCT_ID = jsonObject.getString("PRODUCT_ID");
			PRODUCT_NAME = new String((jsonObject.getString("PRODUCT_NAME")).getBytes("iso-8859-1"),"UTF-8");
			PRODUCT_PRICE = jsonObject.getString("PRODUCT_PRICE");
			PRODUCT_DESCRIBE = new String((jsonObject.getString("PRODUCT_DESCRIBE")).getBytes("iso-8859-1"),"UTF-8");
			PRODUCT_SITE = new String((jsonObject.getString("PRODUCT_SITE")).getBytes("iso-8859-1"),"UTF-8");
			PRODUCT_STATUS = jsonObject.getString("PRODUCT_STATUS");
			int i = analysisService.editMyGood(PRODUCT_ID, PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_DESCRIBE, PRODUCT_SITE, PRODUCT_STATUS);
			
			HttpServletResponse response = this.getResponse();
			response.setContentType("application/json;charset=utf-8");
			PrintWriter writer = response.getWriter();
			if(i == 1){
				writer.write("��Ʒ�༭�ɹ���PRODUCT_ID��" + PRODUCT_ID);
				writer.flush();
				writer.close();
				this.renderNull();
			}
			else{
				writer.write("��Ʒ�༭ʧ�ܣ�PRODUCT_ID��" + PRODUCT_ID);
				writer.flush();
				writer.close();
				this.renderNull();
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		String pId = new String();
//		String pName = new String();
//		double pPrice = 0;
//		JSONObject jsonObject_goods;
//		
//		try {
//			jsonObject_goods = new JSONObject(jsonContent);
//			pId = jsonObject_goods.getString("pId");
//			pName = jsonObject_goods.getString("pName");
//			pName = new String(pName.getBytes("iso-8859-1"),"GBK");
//			pPrice = jsonObject_goods.getDouble("pPrice");  
//			System.out.println("����editGoods���----"+pId + "," + pName + "," + pPrice);
//		} catch (JSONException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		int edit_result = analysisService.editMyGood(pId, pName, (float)pPrice);
//		if(edit_result == 1){
//			System.out.println("�༭�ɹ���"+pId + "," + pName + "," + pPrice);
//			this.renderHtml("�༭�ɹ���");
//		}
//		else{
//			System.out.println("�༭ʧ�ܣ�"+pId + "," + pName + "," + pPrice);
//			this.renderHtml("�༭ʧ�ܣ�");
//		}
	}
	
	
	
}
