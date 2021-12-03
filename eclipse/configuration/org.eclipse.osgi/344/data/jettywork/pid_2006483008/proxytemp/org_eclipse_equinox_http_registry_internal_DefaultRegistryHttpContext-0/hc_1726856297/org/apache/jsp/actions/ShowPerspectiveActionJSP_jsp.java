package org.apache.jsp.actions;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.actions.*;

public final class ShowPerspectiveActionJSP_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");


/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

      out.write("\n");
      out.write("\n");
      out.write("\n");
      org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller controller = null;
      synchronized (session) {
        controller = (org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller) _jspx_page_context.getAttribute("controller", PageContext.SESSION_SCOPE);
        if (controller == null){
          controller = new org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller();
          _jspx_page_context.setAttribute("controller", controller, PageContext.SESSION_SCOPE);
        }
      }
      out.write('\n');

   Perspective currentPerspective = controller.getCurrentPerspective();
   StringBuffer framesetsFile = new StringBuffer("/");
   framesetsFile.append(currentPerspective.getFramesetsFile());
   
   StringBuffer framesetsForm = new StringBuffer("/");
   framesetsForm.append(currentPerspective.getProcessFramesetsForm());
   
   int targetPerspectiveId;
   boolean isHistory;
   try
   {
     targetPerspectiveId = Integer.parseInt(request.getParameter(ActionInputs.PERSPECTIVE));
     isHistory = ("1".equals(request.getParameter(ActionInputs.ISHISTORY)));
   }
   catch (NumberFormatException e)
   {
     targetPerspectiveId = ActionInputs.PERSPECTIVE_UDDI;
     isHistory = false;
   }
   
   if (controller.isPerspectiveContentBlank())
   {
     controller.enablePerspectiveContentBlank(false);
     controller.addToHistory(currentPerspective.getPerspectiveId(),ShowPerspectiveAction.getActionLink(targetPerspectiveId,true));

      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/scripts/switchperspective.jsp", out, true);
      out.write('\n');

   }
   else
   {

      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">\n");
      out.write("<html lang=\"");
      out.print(response.getLocale().getLanguage());
      out.write("\">\n");
      out.write("<head>\n");
      out.write("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("  ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, framesetsFile.toString(), out, true);
      out.write("\n");
      out.write("</head>\n");
      out.write("<body dir=\"");
      out.print(org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir());
      out.write("\">\n");
      java.lang.StringBuffer formAction = null;
      synchronized (request) {
        formAction = (java.lang.StringBuffer) _jspx_page_context.getAttribute("formAction", PageContext.REQUEST_SCOPE);
        if (formAction == null){
          formAction = new java.lang.StringBuffer();
          _jspx_page_context.setAttribute("formAction", formAction, PageContext.REQUEST_SCOPE);
          out.write('\n');

   formAction.append(currentPerspective.getSwitchPerspectiveFormActionLink(targetPerspectiveId,isHistory));

          out.write('\n');
        }
      }
      out.write('\n');
      java.lang.StringBuffer formFrameName = null;
      synchronized (request) {
        formFrameName = (java.lang.StringBuffer) _jspx_page_context.getAttribute("formFrameName", PageContext.REQUEST_SCOPE);
        if (formFrameName == null){
          formFrameName = new java.lang.StringBuffer();
          _jspx_page_context.setAttribute("formFrameName", formFrameName, PageContext.REQUEST_SCOPE);
        }
      }
      out.write('\n');
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, framesetsForm.toString(), out, true);
      out.write("\n");
      out.write("<script language=\"javascript\">\n");
      out.write("  processFramesetSizes(document.forms[0]);\n");
      out.write("</script>\n");
      out.write("</body>\n");
      out.write("</html>\n");

   }

      out.write("   \n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
