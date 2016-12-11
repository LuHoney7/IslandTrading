package controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import interceptor.LoginInterceptor;
import service.ProductBiz;
import service.UserBiz;
import validate.ProductInfoValidator;
import validate.UserInfoValidator;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;

@Before(LoginInterceptor.class)
public class UserController extends Controller {
	UserBiz userService = this.enhance(ProductBiz.class);

	@Before(UserInfoValidator.class)
	public void save() {
		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String pID = this.getPara("USER_ID");
		String pNickname = this.getPara("USER_NICKNAME");
		String pUsername = this.getPara("USER_USERNAME");
		String pPassword = this.getPara("USER_PASSWORD");
		String pImage = this.getPara("USER_IMAGE");
		String pPower = this.getPara("USER_POWER");
		String pTakingid = this.getPara("USER_TAKINGID");
		String pTel = this.getPara("USER_TEL");
		String pHx_username = this.getPara("HX_USERNAME");
		String pHx_password = this.getPara("HX_PASSWORD");

		String result;

		if ((pID != null && pID.trim() != "") && (pUsername != null && pUsername.trim() != "")) {
			Record rec = userService.findByID(pID);
			if (rec == null) {
				boolean res = userService.save(pID, pNickname, pUsername, pPassword, pImage, pPower, pTakingid, pTel,
						pHx_username, pHx_password);
				if (res) {
					result = "�û���ӳɹ�";
				} else {
					result = "�û����ʧ��";
				}
			} else {
				result = "���û�ID�Ѿ�����,�������";
			}

			this.setAttr("result", result);
			this.render("/addUser.jsp");
		} else {
			this.render("/addUser.jsp");
		}
	}

	public void deleteByID() {
		String id = this.getPara(0);
		boolean res = userService.deleteByID(id);
		String result;
		if (res) {
			result = "�û�ɾ���ɹ�";
		} else {
			result = "�û�ɾ��ʧ��";
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	public void list() {
		List<Record> prouser = userService.findAll();
		this.setSessionAttr("prouser", prouser);
		this.render("/userList.jsp");
	}

	@Before(ProductInfoValidator.class)
	public void update() {

		try {
			this.getRequest().setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String pID = this.getPara("USER_ID");
		String pNickname = this.getPara("USER_NICKNAME");
		String pUsername = this.getPara("USER_USERNAME");
		String pPassword = this.getPara("USER_PASSWORD");
		String pImage = this.getPara("USER_IMAGE");
		String pPower = this.getPara("USER_POWER");
		String pTakingid = this.getPara("USER_TAKINGID");
		String pTel = this.getPara("USER_TEL");
		String pHx_username = this.getPara("HX_USERNAME");
		String pHx_password = this.getPara("HX_PASSWORD");

		String result;
		int res = userService.update(pID, pNickname, pUsername, pPassword, pImage, pPower, pTakingid, pTel,
				pHx_username, pHx_password);
		if (res > 0) {
			result = "�û����³ɹ�";
		} else {
			result = "�û�����ʧ��";
			System.out.println("1");
		}
		this.setAttr("result", result);
		this.render("/result.jsp");
	}

	@Before(POST.class)
	public void findByID() {
		String id = this.getPara("pid");
		Record rec = userService.findByID(id);
		this.setAttr("user", rec);
		this.render("/findUser.jsp");
	}

}
