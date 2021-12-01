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
 * ͳһ�������е�����
 * @author Administrator
 *
 */
public class ItemAction extends BaseAction {

	
	/**
	 * ���û�д����κα�ʶ��������command����������Ĭ�ϵ���unspecified����
	 */
	protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("ItemAction=>>unspecified()");
		ActionForward listActionForward = new ActionForward("/index.jsp", true);
		return listActionForward;
	}

	/**
	 * �������
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
		//��ȡ��ҳ������ύ������ֵ
		ItemActionForm iaf = (ItemActionForm)form;
		
		//����Itemʵ����󣬲���ActionForm�е��������õ�Item������
		Item item = new Item();
		
		//������ֵ��ItemActionForm���󿽱���Item����
		BeanUtils.copyProperties(item, iaf);
		
		//����ItemCategory
		ItemCategory ic = new ItemCategory();
		ic.setId(iaf.getCategoryId());
		item.setCategory(ic);
		
		//����itemUnit
		ItemUnit iu = new ItemUnit();
		iu.setId(iaf.getUnitId());
		item.setUnit(iu);
		
		//����ҵ���߼�����
		ItemManager.getInstance().addItem(item);
		ActionForward af = new ActionForward("item.do?command=list&pageNo=" + 
				                              iaf.getPageNo() + 
				                              "&pageSize=" + iaf.getPageSize(), true);
		return af;
	}

	/**
	 * ɾ������
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
		//��ȡ��ҳ������ύ������ֵ
		ItemActionForm iaf = (ItemActionForm)form;
		
		//����ҵ���߼�����
		ItemManager.getInstance().deleteItem(iaf.getSelectFlag());
		ActionForward af = new ActionForward("item.do?command=list&pageNo=" + 
                iaf.getPageNo() + 
                "&pageSize=" + iaf.getPageSize(), true);
		return af;
	}

	/**
	 * �������ϴ����ѯ��Ҫ�޸ĵ�����
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
		//��ȡ��ҳ������ύ������ֵ
		ItemActionForm iaf = (ItemActionForm)form;
		
		//����ҵ���߼�����
		Item item = ItemManager.getInstance().findItemById(iaf.getItemNo());
		
		//����ѯ����ŵ�request��
		request.setAttribute("item", item);
		return mapping.findForward("modify_detail");
	}

	/**
	 * �޸�����
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
		//��ȡ��ҳ������ύ������ֵ
		ItemActionForm iaf = (ItemActionForm)form;
		
		//����Itemʵ����󣬲���ActionForm�е��������õ�Item������
		Item item = new Item();
		
		//������ֵ��ItemActionForm���󿽱���Item����
		BeanUtils.copyProperties(item, iaf);
				
		//����ItemCategory
		ItemCategory ic = new ItemCategory();
		ic.setId(iaf.getCategoryId());
		item.setCategory(ic);
		
		//����itemUnit
		ItemUnit iu = new ItemUnit();
		iu.setId(iaf.getUnitId());
		item.setUnit(iu);
		
		//����ҵ���߼�����
		ItemManager.getInstance().modifyItem(item);
		ActionForward af = new ActionForward("item.do?command=list&pageNo=" + 
                iaf.getPageNo() + 
                "&pageSize=" + iaf.getPageSize(), true);
		return af;
	}

	/**
	 * �������ϴ����ѯ����
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
		//��ȡ��ҳ������ύ������ֵ
		ItemActionForm iaf = (ItemActionForm)form;
		
		//����ҵ���߼�����
		Item item = ItemManager.getInstance().findItemById(iaf.getItemNo());
		
		//����ѯ����ŵ�request��
		request.setAttribute("item", item);

		return mapping.findForward("find_detail");
	}

	/**
	 * ��ѯȫ������
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
		//��ȡ��ҳ������ύ������ֵ
		ItemActionForm iaf = (ItemActionForm)form;
		
		//����ҵ���߼�����
		PageModel pageModel = ItemManager.getInstance().findAllItem(iaf.getPageNo(), iaf.getPageSize(), iaf.getClientIdOrName());
	
		//����ѯ����ŵ�request��
		request.setAttribute("pagemodel", pageModel);
	
		return mapping.findForward("list_success");
	}

	/**
	 * uploadҳ����ϸ��Ϣ
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

		//��ȡ��ҳ������ύ������ֵ
		ItemActionForm iaf = (ItemActionForm)form;
		
		//����ҵ���߼�����
		Item item = ItemManager.getInstance().findItemById(iaf.getItemNo());
		
		//����ѯ����ŵ�request��
		request.setAttribute("item", item);
		
		return mapping.findForward("upload_detail");
	}

	/**
	 * �ϴ�ͼƬ
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
		//��ȡ��ҳ������ύ������ֵ
		ItemActionForm iaf = (ItemActionForm)form;
		FormFile myFile = iaf.getFileName();
		if(myFile != null){
			//���ϴ����ļ������浽��images/item��
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