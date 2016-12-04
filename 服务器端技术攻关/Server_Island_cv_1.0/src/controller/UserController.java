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
public class UserController extends Controller {
	ProductBiz productService = this.enhance(ProductBiz.class);

	@Before(ProductInfoValidator.class)
	public void save() {
		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String pID = this.getPara("proID");
		String pName = this.getPara("proName");
		String price = this.getPara("proPrice");
		String pDescribe = this.getPara("proT_describe");
		String pImage = this.getPara("proImage");
		String pClassify = this.getPara("proClassify");
		String pSite = this.getPara("proSite");
		String pTime = this.getPara("proTime");

		float pPrice = 0;
		String result = "��Ʒ��Ϣ����Ϊ��";

		if (price != null && price != "") {
			pPrice = Float.parseFloat(price);
		}

		if ((pID != null && pID.trim() != "") && (pName != null && pName.trim() != "")) {
			Record rec = productService.findByID(pID);
			if (rec == null) {
				boolean res = productService.save(pID, pName, pPrice, pDescribe, pImage, pClassify, pSite, pTime);
				// this.setAttr("result", result);

				if (res) {
					result = "��Ʒ��ӳɹ�,�������";
				} else {
					result = "��Ʒ���ʧ�ܣ��������";
				}
			} else {
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
			e.printStackTrace();
		}

		String pID = this.getPara("proID");
		String pName = this.getPara("proName");
		String pPrice = this.getPara("proPrice");
		// float pPrice = Float.parseFloat(this.getPara("proPrice"));
		String pDescribe = this.getPara("proT_describe");
		String pImage = this.getPara("proImage");
		String pClassify = this.getPara("proClassify");
		String pSite = this.getPara("proSite");
		String pTime = this.getPara("proTime");

		String result = "��Ʒ������Ϣ����Ϊ��";
		int res = productService.update(pID, pName, pPrice, pDescribe, pImage, pClassify, pSite, pTime);
		if (res > 0) {
			result = "��Ʒ���³ɹ�";
		} else {
			result = "��Ʒ����ʧ��";
			System.out.println("1");
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	@Before(POST.class)
	public void findByID() {
		String id = this.getPara("pid");
		Record rec = productService.findByID(id);
		this.setAttr("product", rec);
		this.render("/findProduct.jsp");
	}

}
