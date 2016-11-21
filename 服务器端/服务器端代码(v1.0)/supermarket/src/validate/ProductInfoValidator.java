package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class ProductInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("proID", "idMsg", "��������ƷID!");
		validateRequiredString("proName", "nameMsg", "��������Ʒ��!");
		validateRequiredString("proPrice", "priceMsg", "�������Ʒ�۸�!");
		validateInteger("proPrice", "priceMsg", "��Ʒ�۸�Ӧ��Ϊ����!");
	}
	
	protected void handleError(Controller controller) {
		//controller.getModel(Blog.class);
		controller.keepPara("proID");
		controller.keepPara("proName");
		controller.keepPara("proPrice");
		
		String actionKey = getActionKey();
		if (actionKey.equals("/product/save"))
				controller.render("/addProduct.jsp");
		else if (actionKey.equals("/product/update"))
			controller.render("/editProduct.jsp");
	}

}
