package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class ActivityInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("ACTIVITY_ID", "idMsg", "������ID");
		validateRequiredString("ACTIVITY_CONTENT", "contentMsg", "����������");
		validateRequiredString("ACTIVITY_ORGANIZER", "organizerMsg", "��������֯��λ");
		validateRequiredString("ACTIVITY_TIME", "timeMsg", "��������չʱ��");
		validateRequiredString("ACTIVITY_SITE", "siteMsg", "����������λ��");
		validateRequiredString("ACTIVITY_NAME", "nameMsg", "����������");
	}

	protected void handleError(Controller controller) {
		controller.keepPara("ACTIVITY_ID");
		controller.keepPara("ACTIVITY_CONTENT");
		controller.keepPara("ACTIVITY_ORGANIZER");
		controller.keepPara("ACTIVITY_TIME");
		controller.keepPara("ACTIVITY_SITE");
		controller.keepPara("ACTIVITY_NAME");

		String actionKey = getActionKey();
		if (actionKey.equals("/activity/save"))
			controller.render("/addActivity.jsp");
		else if (actionKey.equals("/activity/update"))
			controller.render("/editActivity.jsp");
		else if (actionKey.equals("/activity/find"))
			controller.render("/findActivity.jsp");
	}
}
