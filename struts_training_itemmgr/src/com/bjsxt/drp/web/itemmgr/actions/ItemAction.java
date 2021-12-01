package com.bjsxt.drp.web.itemmgr.actions;

import java.io.FileOutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import com.bjsxt.drp.business.itemmgr.manager.ItemManager;
import com.bjsxt.drp.business.itemmgr.model.Item;
import com.bjsxt.drp.business.itemmgr.model.ItemCategory;
import com.bjsxt.drp.business.itemmgr.model.ItemUnit;
import com.bjsxt.drp.business.util.PageModel;
import com.bjsxt.drp.web.itemmgr.forms.ItemActionForm;

/**
 * 统一处理所有的请求
 * @author Administrator
 *
 */
public class ItemAction extends BaseAction {

	
	/**
	 * 如果没有传递任何标识参数（如command参数），则默认调用unspecified方法
	 */
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("ItemAction=>>unspecified()");
		ActionForward listActionForward = new ActionForward("/index.jsp", true);
		return listActionForward;
	}

	/**
	 * 添加物料
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取从页面表单中提交过来的值
		ItemActionForm iaf = (ItemActionForm)form;
		
		//创建Item实体对象，并将ActionForm中的内容设置到Item对象中
		Item item = new Item();
		
		//将属性值从ItemActionForm对象拷贝到Item对象
		BeanUtils.copyProperties(item, iaf);
		
		//设置ItemCategory
		ItemCategory ic = new ItemCategory();
		ic.setId(iaf.getCategoryId());
		item.setCategory(ic);
		
		//设置itemUnit
		ItemUnit iu = new ItemUnit();
		iu.setId(iaf.getUnitId());
		item.setUnit(iu);
		
		//调用业务逻辑操作
		ItemManager.getInstance().addItem(item);
		ActionForward af = new ActionForward("item.do?command=list&pageNo=" + 
				                              iaf.getPageNo() + 
				                              "&pageSize=" + iaf.getPageSize(), true);
		return af;
	}

	/**
	 * 删除物料
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward del(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取从页面表单中提交过来的值
		ItemActionForm iaf = (ItemActionForm)form;
		
		//调用业务逻辑操作
		ItemManager.getInstance().deleteItem(iaf.getSelectFlag());
		ActionForward af = new ActionForward("item.do?command=list&pageNo=" + 
                iaf.getPageNo() + 
                "&pageSize=" + iaf.getPageSize(), true);
		return af;
	}

	/**
	 * 根据物料代码查询需要修改的物料
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modifyDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取从页面表单中提交过来的值
		ItemActionForm iaf = (ItemActionForm)form;
		
		//调用业务逻辑操作
		Item item = ItemManager.getInstance().findItemById(iaf.getItemNo());
		
		//将查询结果放到request中
		request.setAttribute("item", item);
		return mapping.findForward("modify_detail");
	}

	/**
	 * 修改物料
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取从页面表单中提交过来的值
		ItemActionForm iaf = (ItemActionForm)form;
		
		//创建Item实体对象，并将ActionForm中的内容设置到Item对象中
		Item item = new Item();
		
		//将属性值从ItemActionForm对象拷贝到Item对象
		BeanUtils.copyProperties(item, iaf);
				
		//设置ItemCategory
		ItemCategory ic = new ItemCategory();
		ic.setId(iaf.getCategoryId());
		item.setCategory(ic);
		
		//设置itemUnit
		ItemUnit iu = new ItemUnit();
		iu.setId(iaf.getUnitId());
		item.setUnit(iu);
		
		//调用业务逻辑操作
		ItemManager.getInstance().modifyItem(item);
		ActionForward af = new ActionForward("item.do?command=list&pageNo=" + 
                iaf.getPageNo() + 
                "&pageSize=" + iaf.getPageSize(), true);
		return af;
	}

	/**
	 * 根据物料代码查询物料
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward findDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取从页面表单中提交过来的值
		ItemActionForm iaf = (ItemActionForm)form;
		
		//调用业务逻辑操作
		Item item = ItemManager.getInstance().findItemById(iaf.getItemNo());
		
		//将查询结果放到request中
		request.setAttribute("item", item);

		return mapping.findForward("find_detail");
	}

	/**
	 * 查询全部物料
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward list(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取从页面表单中提交过来的值
		ItemActionForm iaf = (ItemActionForm)form;
		
		//调用业务逻辑操作
		PageModel pageModel = ItemManager.getInstance().findAllItem(iaf.getPageNo(), iaf.getPageSize(), iaf.getClientIdOrName());
	
		//将查询结果放到request中
		request.setAttribute("pagemodel", pageModel);
	
		return mapping.findForward("list_success");
	}

	/**
	 * upload页面详细信息
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward uploadDetail(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		//获取从页面表单中提交过来的值
		ItemActionForm iaf = (ItemActionForm)form;
		
		//调用业务逻辑操作
		Item item = ItemManager.getInstance().findItemById(iaf.getItemNo());
		
		//将查询结果放到request中
		request.setAttribute("item", item);
		
		return mapping.findForward("upload_detail");
	}

	/**
	 * 上传图片
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward upload(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//获取从页面表单中提交过来的值
		ItemActionForm iaf = (ItemActionForm)form;
		FormFile myFile = iaf.getFileName();
		if(myFile != null){
			//将上传的文件流保存到磁images/item下
			//String realPath =request.getSession().getServletContext().getRealPath("/images/item");
			//System.out.println(realPath);
			//FileOutputStream fos = new FileOutputStream(realPath+iaf.getItemNo() + ".gif");
			 FileOutputStream fos = new FileOutputStream("C:\\Tomcat 5.0\\webapps\\struts_training_itemmgr\\images\\item\\"+iaf.getItemNo() + ".gif");
			fos.write(myFile.getFileData());
			fos.flush();
			fos.close();
		}
		ActionForward af = new ActionForward("item.do?command=list&pageNo=" + 
                iaf.getPageNo() + 
                "&pageSize=" + iaf.getPageSize(), true);
		return af;
	}

}