package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class ProductInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("proID", "idMsg", "������ID!");
		validateRequiredString("proName", "nameMsg", "����������!");
		validateRequiredString("proPrice", "priceMsg", "�������Ʒ�۸�!");
		validateRequiredString("proPrice", "priceMsg", "��Ʒ�۸�Ӧ��Ϊ����!");
		validateRequiredString("proImage", "imageMsg", "������ͼƬurl!");
		validateRequiredString("proT_describe", "t_describeMsg", "��������Ʒ����!");
		validateRequiredString("proClassify", "classifyMsg", "��������Ʒ����!");
		validateRequiredString("proSite", "siteMsg", "��������Ʒ�����ص�!");
		validateRequiredString("proTime", "timeMsg", "��������Ʒ����ʱ��!");
	}
	
	protected void handleError(Controller controller) {
		//controller.getModel(Blog.class);
		controller.keepPara("proID");
		controller.keepPara("proName");
		controller.keepPara("proPrice");
		controller.keepPara("proImage");
		controller.keepPara("proT_describe");
		controller.keepPara("proClassify");
		controller.keepPara("proSite");
		controller.keepPara("proTime");
		
		String actionKey = getActionKey();
		if (actionKey.equals("/product/save"))
				controller.render("/addProduct.jsp");
		else if (actionKey.equals("/product/update"))
			controller.render("/editProduct.jsp");
		else if (actionKey.equals("/product/find"))
		controller.render("/findProduct.jsp");
	}

}
