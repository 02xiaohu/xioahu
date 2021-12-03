package org.apache.jsp.wsexplorer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils;
import java.util.Enumeration;
import java.net.*;
import java.io.*;

public final class wsexplorer_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {


    private void resetController(ServletContext application,String sessionId,HttpSession session,HttpServletRequest request,Controller controller){
	    // Add the session to the application. This allows us to resurrect the session even if the browser chooses not to participate.
	    application.setAttribute(sessionId,session);
    
	    // Set Max inactivity time out value to 30mins.
	    session.setMaxInactiveInterval(controller.getSessionTimeoutInMinutes() * 60);
	
	    // LaunchOptionManager (below) handles most options,
	    // but need to get state and install locations earlier,
	    // specifically before controller.init().
	    Enumeration paramNames = request.getParameterNames();
	    while (paramNames.hasMoreElements())
	    {
	      String paramName = (String)paramNames.nextElement();
	      String[] paramValues = request.getParameterValues(paramName);
	      if (paramValues != null && paramValues.length > 0)
	      {
	        String decodedParamName = URLUtils.decode(paramName);
	        if (decodedParamName.equals(LaunchOptions.DEFAULT_FAVORITES_LOCATION))
	        {
	          controller.setDefaultFavoritesLocation(paramValues[0]);
	        }
	        else if (decodedParamName.equals(LaunchOptions.STATE_LOCATION))
	        {
	          controller.setStateLocation(paramValues[0]);
	        }
	      }
	    }
	
	    // controller.init()
	    controller.init(sessionId,application,request.getContextPath());
    }
    
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
 * Copyright (c) 2001, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 * 20100414   306773 mahutch@ca.ibm.com - Mark Hutchinson, make session time out configurable
 *******************************************************************************/

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">\n");
      out.write("<html lang=\"");
      out.print(response.getLocale().getLanguage());
      out.write("\">\n");
      out.write("<head>\n");
      out.write("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("  <script language=\"javascript\">\n");
      out.write("    function initWindowName(name)\n");
      out.write("    {\n");
      out.write("      window.name = name;\n");
      out.write("    }\n");
      out.write("  </script>\n");
      out.write("  ");

  String sessionId = session.getId();
  
      out.write("\n");
      out.write("  ");
      org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller controller = null;
      synchronized (session) {
        controller = (org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller) _jspx_page_context.getAttribute("controller", PageContext.SESSION_SCOPE);
        if (controller == null){
          controller = new org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller();
          _jspx_page_context.setAttribute("controller", controller, PageContext.SESSION_SCOPE);
          out.write("\n");
          out.write("    ");
          out.write("\n");
          out.write("    ");
//resetController(application,sessionId,session,request,controller);
          out.write("\n");
          out.write("  ");
        }
      }
      out.write("\n");
      out.write("  ");

  // Check if session Controller needs to be re-initialized
  if (controller.getSessionId()==null){
	  resetController(application,sessionId,session,request,controller);
  }
 
  // preload from LaunchOptionManager
  String key = request.getParameter(URLUtils.encode(WSExplorerContext.ID));
  if (key != null && key.length() > 0)
  {
    LaunchOptionsManager manager = LaunchOptionsManager.getInstance();
    manager.manage(key, sessionId, application);
  }
    
  
      out.write("\n");
      out.write("  ");
      org.apache.jasper.runtime.JspRuntimeLibrary.include(request, response, "/actionengine.jsp", out, true);
      out.write("\n");
      out.write("  <title>");
      out.print(controller.getMessage("TITLE_WSEXPLORER"));
      out.write("</title>\n");
      out.write("</head>\n");

// reset perspective content to blank
controller.enablePerspectiveContentBlank(true);

      out.write("\n");
      out.write("<frameset rows=\"0,35,*\" border=0 onload=\"initWindowName('");
      out.print(FrameNames.WINDOW_NAME_WSEXPLORER_JSP);
      out.write("')\">\n");
      out.write("  <frame name=\"");
      out.print(FrameNames.PERSPECTIVE_WORKAREA);
      out.write("\" title=\"");
      out.print(controller.getMessage("FRAME_TITLE_PERSPECTIVE_WORKAREA"));
      out.write("\" frameborder=0 noresize>\n");
      out.write("  <frame name=\"");
      out.print(FrameNames.PERSPECTIVE_TOOLBAR);
      out.write("\" title=\"");
      out.print(controller.getMessage("FRAME_TITLE_PERSPECTIVE_TOOLBAR"));
      out.write("\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("perspective_toolbar.jsp")));
      out.write("\" marginwidth=0 marginheight=0 scrolling=\"no\" frameborder=0 noresize>\n");
      out.write("  <frame name=\"");
      out.print(FrameNames.PERSPECTIVE_CONTENT);
      out.write("\" title=\"");
      out.print(controller.getMessage("FRAME_TITLE_PERSPECTIVE_CONTENT"));
      out.write("\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("perspective_content.jsp")));
      out.write("\" marginwidth=0 marginheight=0 scrolling=\"no\" frameborder=0>\n");
      out.write("</frameset>\n");
      out.write("</html>\n");
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
