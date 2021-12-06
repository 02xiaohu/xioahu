package com.asm;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

//import entity.User;

@Controller  //类似Struts的Action
public class TestController {

	@RequestMapping("/login.do")  // 请求url地址映射，类似Struts的action-mapping
	public String testLogin(@RequestParam(value="username")String name, String password, HttpServletRequest request) {
		// @RequestParam一般用于当表单的请求参数名和方法中形参不一致时，将指定的请求参数付给方法中形参
		// @RequestParam可简写为：@RequestParam("username")
		 request.setAttribute("username", name);
	     request.setAttribute("password", password);
		if (!"admin".equals(name) || !"admin".equals(password)) {
			return "failure"; // 跳转页面路径（默认为转发），该路径不需要包含spring-servlet配置文件中配置的前缀和后缀
		}
		return "success";
	}
	
	
	@RequestMapping("/login2.do")
	public ModelAndView testLogin2(String username, String password,int age,HttpServletRequest request){
		// request和response不必非要出现在方法中，如果用不上的话可以去掉
		// 参数的名称是与页面控件的name相匹配，参数类型会自动被转换
		request.setAttribute("username", username);
		request.setAttribute("password", password);
		request.setAttribute("age", age);
		if ("admin".equals(username) &&"admin".equals(password)) {
			return new ModelAndView("success"); // 手动实例化ModelAndView完成跳转页面（转发），效果等同于上面的方法返回字符串
		}
		return new ModelAndView(new RedirectView("./index.jsp"));  // 采用重定向方式跳转页面
		// 重定向还有一种简单写法
		// return new ModelAndView("redirect:../index.jsp");
	}
	
	
	@RequestMapping("/login3.do")
	
	public ModelAndView testLogin3(User user) {
		// 同样支持参数为表单对象，User类似于Struts的ActionForm，User不需要任何配置，直接写即可,放到request内置对象中。
		String username = user.getUsername();
		String password = user.getPassword();
		int age = user.getAge();
		
		if (!"admin".equals(username) || !"admin".equals(password) ) {
			return new ModelAndView("error");
		}
		return new ModelAndView("success");
	}

	@Resource(name = "loginService")  // 获取applicationContext.xml中bean的id为loginService的，并注入
	private LoginService loginService;  //等价于spring传统注入方式写get和set方法，这样的好处是简洁工整，省去了不必要得代码

	@RequestMapping("/login4.do")
	public String testLogin4(User user) {
		if (loginService.login(user) == false) {
			return "error";
		}
		return "success";
	}
 }
