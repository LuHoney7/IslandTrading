package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class OrderInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("ORDER_ID", "idMsg", "�����붩��ID");
		validateRequiredString("ORDER_SITE", "siteMsg", "�����붩��λ��");
		validateRequiredString("ORDER_TIME", "timeMsg", "�����붩��ʱ��");
		validateRequiredString("ORDER_STATUS", "statusMsg", "�����붩��״̬");
	}

	protected void handleError(Controller controller) {
		controller.keepPara("ORDER_ID");
		controller.keepPara("ORDER_SITE");
		controller.keepPara("ORDER_TIME");
		controller.keepPara("ORDER_STATUS");

		String actionKey = getActionKey();
		if (actionKey.equals("/order/find"))
			controller.render("/findOrder.jsp");
	}
}
