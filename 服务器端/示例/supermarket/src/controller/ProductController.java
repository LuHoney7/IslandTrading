package controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import interceptor.LoginInterceptor;
import service.ProductBiz;
import validate.ProductInfoValidator;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;

@Before(LoginInterceptor.class)
public class ProductController extends Controller {
	ProductBiz productService = this.enhance(ProductBiz.class);

	//
	//�����û�����
	// public void add() {
	// this.render("/addUser.jsp");
	// }

	// �����û���Ϣ
	@Before(ProductInfoValidator.class)
	public void save() {
		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pID = this.getPara("proID");
		String pName = this.getPara("proName");
		String price = this.getPara("proPrice");
		float pPrice = 0;
		String result = "��Ʒ��Ϣ����Ϊ��";

		if (price != null && price != "") {
			pPrice = Float.parseFloat(price);
		}

		if ((pID != null && pID.trim() != "")
				&& (pName != null && pName.trim() != "")) {
			// String result =
			// "Hi,"+this.getSession().getAttribute("userName")+"! you have save user ("+name+","+sex+","+age+","+pwd+","+") success!";
			
			Record rec = productService.findByID(pID);
			if(rec==null){
				boolean res = productService.save(pID, pName, pPrice);
				// this.setAttr("result", result);

				if (res) {
					result = "��Ʒ��ӳɹ�,�������";
				} else {
					result = "��Ʒ���ʧ�ܣ��������";
				}
			}
			else{
				result = "����ƷID�Ѿ�����,�������";
			}
			
			this.setAttr("result", result);
			this.render("/addProduct.jsp");
		} else {
			this.render("/addProduct.jsp");
		}
	}

	public void deleteByID() {
		String id = this.getPara(0);
		boolean res = productService.deleteByID(id);
		String result;
		if (res) {
			result = "��Ʒɾ���ɹ�";
		} else {
			result = "��Ʒɾ��ʧ��";
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	public void list() {
		List<Record> prolist = productService.findAll();
		this.setSessionAttr("prolist", prolist);
		this.render("/productList.jsp");
	}

	@Before(ProductInfoValidator.class)
	public void update() {

		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pID = this.getPara("proID");
		String pName = this.getPara("proName");
		float pPrice = Float.parseFloat(this.getPara("proPrice"));
		String result = "��Ʒ������Ϣ����Ϊ��";
		int res = productService.update(pID, pName, pPrice);
		if (res > 0) {
			result = "��Ʒ���³ɹ�";
		} else {
			result = "��Ʒ����ʧ��";
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
		// this.render("/product/list");
	}

	@Before(POST.class)
	public void findByID() {
		String id = this.getPara("pid");
		System.out.println(" find by id:" + id);
		Record rec = productService.findByID(id);
		this.setAttr("product", rec);
		this.render("/findProduct.jsp");
	}

}
