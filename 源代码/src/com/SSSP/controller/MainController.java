package com.SSSP.controller;
import org.springframework.stereotype.Controller;

import javax.servlet.http.*;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.SSSP.bean.*;
import com.SSSP.service.*;


import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class MainController {
	@Autowired
private UserService userservice;
	@RequestMapping("/add.do")
	public String indexUser() {
		AllUserBean user=new AllUserBean();
		user.setUser_name("����");
		user.setUser_password("jej1www.x1");
		user.setUser_flag(1);
		user.setBalance(10);
		userservice.saveUser(user);
		System.out.println("����ɹ�");
		return "redirect:Hello.jsp";
	}
	@RequestMapping("/Login.do")
	public ModelAndView Login(UserBean u,int admin,HttpServletRequest request) throws Exception{
		HttpSession session=request.getSession();
		ModelAndView view=new ModelAndView();
		System.out.println("�û�����"+u.getName()+" ���룺"+u.getPassword());
		
		
		
		
if(admin==0) {
	boolean b=userservice.findByNaPa(u.getName(),u.getPassword());
	int flag=userservice.getFlag(u.getName());
	System.out.println(flag);
	if(flag==0) {
		view=new ModelAndView("page/freezed");
		return view;
	}
	if(b) {
		view =new ModelAndView("page/Login_success");
		session.setAttribute("UserBean",u);
	}
	else view=new ModelAndView("page/Login_error");
	return  view;
	}
if(admin==1) {
	boolean c=userservice.findAdmin(u.getName());
	if(c) {
		view=new ModelAndView("page/Admin_success");
		session.setAttribute("UserBean",u);
	}
	else view=new ModelAndView("page/Login_error");
}
return view;
	}
	@RequestMapping("/register.do")
	public ModelAndView register(UserBean user,HttpServletRequest request) {
		ModelAndView view=new ModelAndView();

		HttpSession session=request.getSession();
		System.out.println("�û�����"+user.getName()+" ���룺"+user.getPassword());
		boolean b=userservice.register(user.getName(),user.getPassword());
		if(b) {
			view =new ModelAndView("page/register_success");
			session.setAttribute("UserBean",user);
	}else  view=new ModelAndView("page/register_error");
		return view;
	}
	
	@RequestMapping("/back.do")
	public String back() {
		return "redirect:ssc.jsp";
	}
	@RequestMapping("/function.do")
	public String dofunction() {
		return "page/function";
	}
	@RequestMapping("/queryMoney.do")
	public String queryMoney(HttpServletRequest request) {
		HttpSession session=request.getSession();
		UserBean user=(UserBean)session.getAttribute("UserBean");
		AllUserBean u=userservice.findByName(user.getName());
		double money=userservice.inquiry(user.getName());
		userservice.inquiryLog(u);
	    session.setAttribute("money",money);
		return "page/inquiry";
	}
	@RequestMapping("/deposit.do")
	public String deposit() {
		return "redirect:deposit.jsp";
	}
	@RequestMapping("/withdrawal.do")
	public String withdrawal() {
		return "redirect:withdrawal.jsp";
	}
	@RequestMapping("/transfer.do")
	public String transfer() {
		return "redirect:transfer.jsp";
	}
	@RequestMapping("/webchatroom.do")
	public String webcaht() {
		return "page/webchatroom";
	}

	@RequestMapping("/upload.do")
	public String upload() {
		return "page/upload";
	}
	@RequestMapping("/depositdd.do")
	public String d_deposit(double much,double reMuch,HttpServletRequest request
			,HttpServletResponse response) throws IOException{
		HttpSession session=request.getSession();
		PrintWriter out=response.getWriter();
		UserBean user=(UserBean)session.getAttribute("UserBean");
		AllUserBean u=userservice.findByName(user.getName());
		if(much!=reMuch||much<0) {
			session.setAttribute("Error","������Ч�����ٴμ��");
			return "redirect:deposit.jsp";
				}
				else 
					userservice.deposit(user.getName(),much);
		userservice.depositLog(u,much);
			return "page/deposit_success";
			}
	@RequestMapping("/withdrawl.do")
	public String w_withdrawl(double much,double reMuch,HttpServletRequest request
			,HttpServletResponse response) throws IOException{
		HttpSession session=request.getSession();
		UserBean user=(UserBean)session.getAttribute("UserBean");
		AllUserBean u=userservice.findByName(user.getName());
		if(much!=reMuch||much<0) {
			session.setAttribute("Error","������Ч�����ٴμ��");
			return "redirect:withdrawal.jsp";
				}
				else userservice.withdrawal(user.getName(),much);
		userservice.withdrawalLog(u,much);
			return "page/withdrawal_success";
			}
	@RequestMapping("/transferr.do")	
		public String transferr(double much,double reMuch,String toWhom,HttpServletRequest request) {
		HttpSession session=request.getSession();
		UserBean host_user=(UserBean)session.getAttribute("UserBean");//ת���˵�bean
		AllUserBean host_bean=userservice.findByName(host_user.getName());//ת���˵�AllUserbean
     double  host_money=userservice.inquiry(host_user.getName());
		if(much!=reMuch||much<0) {
        	session.setAttribute("Error","������Ч�����ٴμ��");
			return "redirect:transfer.jsp";
	}
		if(much>host_money) {
			session.setAttribute("MoneyError","�ܱ�Ǹ����û����ô��Ǯ��");
			return "redirect:transfer.jsp";
		}
		AllUserBean toWhom_user=userservice.findByName(toWhom);//�ҵ�ת���˵�ʵ����
	System.out.println("ת���ˣ�"+toWhom_user.getUser_name());
		if(toWhom_user.getUser_name()==null) {
			session.setAttribute("AccountError","���޴��ˣ���������ȷ���û�");
			return "redirect:transfer.jsp";
		}
		 userservice.deposit(toWhom_user.getUser_name(),much);
		 userservice.withdrawal(host_user.getName(),much);
		 userservice.transferLog(host_bean,toWhom_user,much);
		 return "page/transfer_success";
	}
@RequestMapping("/messageShow.do")
public String messageshow(String charname,String message,HttpServletRequest request,HttpServletResponse response) {
	  List speak;
	  HttpSession session=request.getSession();
	 Map Receive;
	 Map PUBLIC;
	 List<String> say=new ArrayList();
	 List<String> talk=new ArrayList();
		 PUBLIC=(Map)request.getServletContext().getAttribute("PUBLIC_MSG");
		 speak=(List)PUBLIC.get("public");//��speak������ָ��PUBLIC��Ϊ"public"���ַ���
		 Receive=(Map)request.getServletContext().getAttribute("allmap");
	     UserBean user=(UserBean)session.getAttribute("UserBean");//���˵���Ϣ
	     String host_name=user.getName();
		 String chat_name=charname;//˽�ĵ�����
		 String content=message;//��������
		 if(content==null) {
			 session.setAttribute("Empty","������ݲ���Ϊ�գ�");
			 return "page/webchatroom";
		 }
		 if("".equals(chat_name)) {
			 String Final_msg=host_name+"�Դ��˵��"+content;
			speak.add(Final_msg);
			PUBLIC.put("public",speak);
			
		 }else {
		 Set s=(Set)request.getServletContext().getAttribute("alluser");
		 Iterator iter=s.iterator();
		 while(iter.hasNext()) {
				Object o=iter.next();
				if(o instanceof UserBean){
		         UserBean user_other=(UserBean)o;
			 if(user_other.getName().equals(chat_name)) {
				 String Final_msg="��˽�ģ�"+chat_name+",˵�ˣ�"+content;
				 String s1=host_name+"˽�Ķ���˵��"+content;
				talk=(List)Receive.get(user);//��talk������ָ��Receive�ļ�Ϊuser�Ķ���
				say=(List)Receive.get(user_other);//��say������ָ��Receive�ļ�Ϊuser_other�Ķ���
				 say.add(s1);
				 talk.add(Final_msg);
				 Receive.put(user,talk);//����Լ�˵�Ļ�
				 Receive.put(user_other,say);//��ŷ���˽�Ķ���˵�Ļ�
				 
				 /*һ��UserBean��˽�Ľ����Ҫ��ʾ�Լ��������˵���Ϣ
				  * �ڱ��˷����ҵ�˽����Ϣ*/
			 }
		 }
		 }
		 }
		 request.getServletContext().setAttribute("PUBLIC_MSG",PUBLIC);
		
		 request.getServletContext().setAttribute("allmap",Receive);
		 return "redirect:webchatroomshow.jsp";
	
}
@RequestMapping("/exit.do")
public String exit(HttpServletRequest request) {
	HttpSession session=request.getSession();
	session.invalidate();//�ֶ�����session
	return "redirect:exit.jsp";
}
@RequestMapping("/findUserLog.do")
public String findUserLog(HttpServletRequest request)throws Exception {
	HttpSession session=request.getSession();
	UserBean user=(UserBean)session.getAttribute("UserBean");
List list=userservice.findUserLog(user.getName());
Integer id=(Integer)userservice.getId(user.getName());
session.setAttribute("List",list);
session.setAttribute("id",id);
return "page/findUserLog";
}
@RequestMapping("/Admin.do")
public String AdminFunction() {
	return "page/admin_function";
}

@RequestMapping("/findAllUserLogs.do")
public String findAllUserLogs(HttpServletRequest request) {
	HttpSession session=request.getSession();
	String pageNoS=request.getParameter("pageNo");
	int pageNo=Integer.parseInt(pageNoS);
	Page<logBean> page=userservice.findAllUserLogs(pageNo,4);
	session.setAttribute("AllUserLogs",page);
	return "page/showAllUserLogs";
}
@RequestMapping("/findAllUsers.do")
public String findAllUsers(HttpServletRequest request) {
	HttpSession session=request.getSession();
	List<AllUserBean> list=userservice.findAllUsers();

	session.setAttribute("AllUsers",list);
	return "page/showAllUsers";
}
@RequestMapping("/freeze.do")
public String freezeUser() {
	return "page/freeze";
}
@RequestMapping("/goTofreeze.do")
public String freezze(String user,HttpServletRequest request) {
	HttpSession session=request.getSession();
	System.out.println("user��name:"+user);
	AllUserBean a=userservice.findByName(user);
	System.out.println("���ݿ����Ƿ�������ˣ�"+a.getUser_name());
	if(a.getUser_name()==null) {
		session.setAttribute("freezeError","���޴���");
		return "page/freeze";
	}
	
	session.setAttribute("user",user);
	return "page/freezing";
}
@RequestMapping("/freezeUser.do")
public String freezeUser (HttpServletRequest request) {
	HttpSession session=request.getSession();
	String user=(String)session.getAttribute("user");
	System.out.println(user);
userservice.freeze(user); 
return "page/freeze_success";
}
@RequestMapping("/unfreeze.do")
public String unfreeze(HttpServletRequest request) {
	HttpSession session=request.getSession();
	String user=(String)session.getAttribute("user");
userservice.unfreeze(user); 
return "page/unfreeze";
}
}