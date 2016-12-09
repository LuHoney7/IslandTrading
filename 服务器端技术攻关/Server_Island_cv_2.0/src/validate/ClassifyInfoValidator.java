package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class ClassifyInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("proID", "idMsg", "������ID!");
		validateRequiredString("proType", "typeMsg", "��������Ʒ����!");
		validateRequiredString("proImage", "imageMsg", "��������ƷͼƬ!");
	}

	protected void handleError(Controller controller) {
		// controller.getModel(Blog.class);
		controller.keepPara("proID");
		controller.keepPara("proType");
		controller.keepPara("proImage");
		String actionKey = getActionKey();
		if (actionKey.equals("/classify/save"))
			controller.render("/addClassify.jsp");
		else if (actionKey.equals("/classify/update"))
			controller.render("/editClassify.jsp");
		else if (actionKey.equals("/classify/find"))
			controller.render("/findClassify.jsp");
	}

}
