/**
 * Function:FeedbackInfoValidator
 * Date:2016.12.11
 * Author:LiuXin
 */
package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class FeedbackInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("FEEDBACK_ID", "idMsg", "�����뷴��ID");
		validateRequiredString("FEEDBACK_CONTENT", "contentMsg", "�����뷴��");
		validateRequiredString("FEEDBACK_CONTACT", "contactMsg", "�����뷴����ϵ��ʽ");
		validateRequiredString("FEEDBACK_TIME", "timeMsg", "�����뷴��ʱ��");
		validateRequiredString("FEEDBACK_STATUS", "statusMsg", "�����뷴��״̬");
	}

	protected void handleError(Controller controller) {
		controller.keepPara("FEEDBACK_ID");
		controller.keepPara("FEEDBACK_CONTENT");
		controller.keepPara("FEEDBACK_CONTACT");
		controller.keepPara("FEEDBACK_TIME");
		controller.keepPara("FEEDBACK_STATUS");

		String actionKey = getActionKey();
		if (actionKey.equals("/feedback/find"))
			controller.render("/addFeedback.jsp");
	}

}
