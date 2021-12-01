package com.bjsxt.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**   
 * 用户登录的Action
 * @author Administrator
 *
 */
public class LoginAction extends Action {
 
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		LoginActionForm laf = (LoginActionForm)form;
		String username = laf.getUsername();
		String password = laf.getPassword();

		ActionMessages messages = new ActionMessages();
		try {
			UserManager.getInstance().login(username, password);
			
			//创建国际化消息文本
			ActionMessage message = new ActionMessage("user.login.success", username);
			//ActionMessage message = new ActionMessage("user.login.success", new Object[]{username});
			messages.add("loginSuccess1", message);

			ActionMessage message1 = new ActionMessage("user.login.success", username);
			messages.add("loginSuccess2", message1);
			
			//传递国际化消息文本
			this.saveMessages(request, messages);
			return mapping.findForward("success");
		}catch(UserNotFoundException unfe) {
			unfe.printStackTrace();
			
			//创建国际化消息文本
			ActionMessage message = new ActionMessage("user.not.found", username);
			messages.add("error1", message);
			
			//传递国际化消息文本
			this.saveErrors(request, messages);
		}catch(PasswordErrorException pee) {
			pee.printStackTrace();
			//创建国际化消息文本
			ActionMessage message = new ActionMessage("user.password.error");
			messages.add("error2", message);
			
			//传递国际化消息文本
			this.saveErrors(request, messages);
		}
		return mapping.findForward("error");
	}

}
