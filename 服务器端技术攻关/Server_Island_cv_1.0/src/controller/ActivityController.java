package controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;

import interceptor.LoginInterceptor;
import service.ActivityBiz;
import validate.ActivityInfoValidator;
import validate.ProductInfoValidator;

@Before(LoginInterceptor.class)
public class ActivityController extends Controller {
	ActivityBiz activityService = this.enhance(ActivityBiz.class);

	@Before(ActivityInfoValidator.class)
	public void save() {
		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String pID = this.getPara("proID");
		String pName = this.getPara("proName");
		String pContent = this.getPara("proContent");
		String pOrganizer = this.getPara("proOrganizer");
		String pTime = this.getPara("proTime");
		String pSite = this.getPara("proSite");

		String result = "���Ϣ����Ϊ��";

		if ((pID != null && pID.trim() != "") && (pName != null && pName.trim() != "")) {

			Record rec = activityService.findByID(pID);
			if (rec == null) {
				boolean res = activityService.save(pID, pName, pContent, pOrganizer, pTime, pSite);

				if (res) {
					result = "���ӳɹ�,�������";
				} else {
					result = "����ʧ�ܣ��������";
				}
			} else {
				result = "�ûID�Ѿ�����,�������";
			}

			this.setAttr("result", result);
			this.render("/addActivity.jsp");
		} else {
			this.render("/addActivity.jsp");
		}
	}

	public void deleteByID() {
		String id = this.getPara(0);
		boolean res = activityService.deleteByID(id);
		String result;
		if (res) {
			result = "�ɾ���ɹ�";
		} else {
			result = "�ɾ��ʧ��";
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	public void list() {
		List<Record> prolist = activityService.findAll();
		this.setSessionAttr("prolist", prolist);
		this.render("/ActivityList.jsp");
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
		String pContent = this.getPara("proContent");
		String pOrganizer = this.getPara("proOrganizer");
		String PTime = this.getPara("proTime");
		String pSite = this.getPara("proSite");

		String result = "�������Ϣ����Ϊ��";
		int res = activityService.update(pID, pName, pContent, pOrganizer, PTime, pSite);
		if (res > 0) {
			result = "����³ɹ�";
		} else {
			result = "�����ʧ��";
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	@Before(POST.class)
	public void findByID() {
		String id = this.getPara("pid");
		Record rec = activityService.findByID(id);
		this.setAttr("activity", rec);
		this.render("/findActivity.jsp");
	}
}
