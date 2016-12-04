package validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class UserInfoValidator extends Validator {

	protected void validate(Controller controller) {
		validateRequiredString("proID", "idMsg", "�������û�ID!");
		validateRequiredString("proNickname", "nicknameMsg", "�������û��ǳ�!");
		validateRequiredString("proUsername", "usernameMsg", "�������û��˻�!");
		validateRequiredString("proPassword", "passwordMsg", "�������û�����!");
		validateRequiredString("proGarvatar", "garvatarMsg", "�������û�ͷ��!");
		validateRequiredString("proPower", "powerMsg", "�������û�Ȩ��!");
	}

	protected void handleError(Controller controller) {
		controller.keepPara("proID");
		controller.keepPara("proNickname");
		controller.keepPara("proUsername");
		controller.keepPara("proPassword");
		controller.keepPara("proGravatar");
		controller.keepPara("proPower");

		String actionKey = getActionKey();
		if (actionKey.equals("/user/save"))
			controller.render("/addUser.jsp");
		else if (actionKey.equals("/user/update"))
			controller.render("/editUser.jsp");
		else if (actionKey.equals("/user/find"))
			controller.render("/findUser.jsp");
	}

}
