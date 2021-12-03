package org.apache.jsp.scripts;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.util.*;

public final class panes_jsp extends org.apache.jasper.runtime.HttpJspBase
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

      out.write('\n');
      out.write('\n');
      org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller controller = null;
      synchronized (session) {
        controller = (org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller) _jspx_page_context.getAttribute("controller", PageContext.SESSION_SCOPE);
        if (controller == null){
          controller = new org.eclipse.wst.ws.internal.explorer.platform.perspective.Controller();
          _jspx_page_context.setAttribute("controller", controller, PageContext.SESSION_SCOPE);
        }
      }
      out.write("\n");
      out.write("<script language=\"javascript\">\n");
      out.write("  var perspectiveWorkArea = top.frames[\"");
      out.print(FrameNames.PERSPECTIVE_WORKAREA);
      out.write("\"];\n");
      out.write("  var perspectiveToolbar = top.frames[\"");
      out.print(FrameNames.PERSPECTIVE_TOOLBAR);
      out.write("\"];\n");
      out.write("  var perspectiveContent = top.frames[\"");
      out.print(FrameNames.PERSPECTIVE_CONTENT);
      out.write("\"];\n");
      out.write("\n");
      out.write("  function getPerspectiveContentFrameset()\n");
      out.write("  {\n");
      out.write("    return perspectiveContent.document.getElementsByTagName(\"frameset\").item(0);\n");
      out.write("  }\n");
      out.write("\n");
      out.write("  function toggleDoubleClickColumnTitle()\n");
      out.write("  {\n");
      out.write("    var doubleClickColumn = document.getElementById(\"doubleclickcolumn\");\n");
      out.write("    if (doubleClickColumn == null)\n");
      out.write("      return;\n");

   String jsAltRestore = HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_RESTORE"));

      out.write("\n");
      out.write("    if (doubleClickColumn.title == \"");
      out.print(jsAltRestore);
      out.write("\")\n");
      out.write("      doubleClickColumn.title = \"");
      out.print(HTMLUtils.JSMangle(controller.getMessage("ALT_DOUBLE_CLICK_TO_MAXIMIZE")));
      out.write("\";\n");
      out.write("    else\n");
      out.write("      doubleClickColumn.title = \"");
      out.print(jsAltRestore);
      out.write("\";\n");
      out.write("  }\n");
      out.write("</script>\n");
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
