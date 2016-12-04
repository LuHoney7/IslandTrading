package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class FeedbackInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("proID", "idMsg", "�����뷴��ID!");
		validateRequiredString("proUser_id", "user_idMsg", "�������û�ID!");
		validateRequiredString("proContent", "contentMsg", "�����뷴������!");
		validateRequiredString("proContact", "contactMsg", "�����뷴������ϵ��ʽ!");
		validateRequiredString("proTime", "timeMsg", "�����뷴��ʱ��!");
		validateRequiredString("proStatus", "statusMsg", "�����뷴��״̬!");
	}

	protected void handleError(Controller controller) {
		controller.keepPara("proID");
		controller.keepPara("proUser_id");
		controller.keepPara("proContent");
		controller.keepPara("proContact");
		controller.keepPara("proTime");
		controller.keepPara("proStatus");

		String actionKey = getActionKey();
		if (actionKey.equals("/feedback/find"))
			controller.render("/addFeedback.jsp");
	}

}
