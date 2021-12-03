package org.apache.jsp.wsexplorer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.WSExplorerContext;
import java.util.Enumeration;
import java.util.Hashtable;
import org.eclipse.wst.ws.internal.explorer.platform.util.URLUtils;

public final class launch_005foptions_005fregistry_jsp extends org.apache.jasper.runtime.HttpJspBase
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
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060515   128602 gilberta@ca.ibm.com - Gilbert Andrews
 *******************************************************************************/

      out.write("\n");
      out.write("\n");
      out.write("\n");

  request.setCharacterEncoding("UTF-8"); 
  String key = null;
  Hashtable options = new Hashtable();
  Enumeration paramNames = request.getParameterNames();
  while (paramNames.hasMoreElements())
  {
    String paramName = (String)paramNames.nextElement();
    String[] paramValues = request.getParameterValues(paramName);
    if (paramValues != null && paramValues.length > 0)
    {
      if (key == null && URLUtils.decode(paramName).equals(WSExplorerContext.ID))
        key = paramValues[0];
      else
        options.put(paramName, paramValues);
    }
  }
  if (key != null)
  {
    LaunchOptionsManager manager = LaunchOptionsManager.getInstance();
    manager.manage(key, options, application);
  }

      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">\n");
      out.write("<html lang=\"");
      out.print(response.getLocale().getLanguage());
      out.write("\">\n");
      out.write("<head>\n");
      out.write("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("</head>\n");
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
