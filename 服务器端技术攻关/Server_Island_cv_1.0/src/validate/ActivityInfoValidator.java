package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class ActivityInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("proID", "idMsg", "������ID!");
		validateRequiredString("proName", "nameMsg", "����������!");
		validateRequiredString("proContent", "contentMsg", "����������!");
		validateRequiredString("proOrganizer", "origanizerMsg", "��������֯��λ!");
		validateRequiredString("proTime", "timeMsg", "����������ʱ��!");
		validateRequiredString("proSite", "siteMsg", "����������λ��!");
	}

	protected void handleError(Controller controller) {
		controller.keepPara("proID");
		controller.keepPara("proName");
		controller.keepPara("proContent");
		controller.keepPara("proOrganizer");
		controller.keepPara("proTime");
		controller.keepPara("proSite");

		String actionKey = getActionKey();
		if (actionKey.equals("/activity/save"))
			controller.render("/addActivity.jsp");
		else if (actionKey.equals("/activity/update"))
			controller.render("/editActivity.jsp");
		else if (actionKey.equals("/activity/find"))
			controller.render("/findActivity.jsp");
	}

}
