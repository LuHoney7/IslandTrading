package controller;

import service.ProductBiz;

import com.jfinal.core.Controller;

public class LoginController extends Controller {
	ProductBiz userService = this.enhance(ProductBiz.class);
	public void index() {
		this.render("/login.jsp");
	}

	public void login() {

		String userName = this.getPara("userName");
		String userPwd   = this.getPara("userPwd");

		this.setAttr("userName", userName);
		this.setAttr("userPwd",userPwd);
		//�û��Ѿ���¼��UserName ����session��
		this.setSessionAttr("userName", userName);
		
		//���û���Ϊadmin������Ϊadminʱ��ת���������
		if(userName.equals("admin") && userPwd.equals("admin"))
		{
			this.redirect("/product/list");
		}
		else
		{
			this.render("login.jsp");
		}

	}

}
