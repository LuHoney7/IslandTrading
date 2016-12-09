package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class CollectInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("proProduct_id", "product_idMsg", "��������ƷID!");
		validateRequiredString("proUser_id", "user_idMsg", "�������û�ID!");
		validateRequiredString("proStatus", "statusMsg", "�������ղ�״̬!");
		validateRequiredString("proDate", "dateMsg", "�������ղ�����!");
	}

	protected void handleError(Controller controller) {
		controller.keepPara("proProduct_id");
		controller.keepPara("proUser_id");
		controller.keepPara("proStatus");
		controller.keepPara("proDate");

		String actionKey = getActionKey();
		if (actionKey.equals("/collect/update"))
			controller.render("/editCollect.jsp");
		else if (actionKey.equals("/collect/find"))
			controller.render("/findCollect.jsp");
	}

}
