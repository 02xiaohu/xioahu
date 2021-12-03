package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.ActionInputs;
import org.eclipse.wst.ws.internal.explorer.platform.engine.ActionEngine;
import org.eclipse.wst.ws.internal.explorer.platform.engine.ActionDataParser;
import org.eclipse.wst.ws.internal.explorer.platform.engine.data.ScenarioDescriptor;
import org.eclipse.wst.ws.internal.explorer.platform.engine.data.TransactionDescriptor;
import org.eclipse.wst.ws.internal.explorer.platform.engine.data.ActionDescriptor;
import org.eclipse.wst.ws.internal.explorer.platform.util.XMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.util.HTMLUtils;
import org.eclipse.wst.ws.internal.explorer.platform.util.MultipartFormDataParser;
import java.util.List;
import java.util.Iterator;

public final class actionengine_jsp extends org.apache.jasper.runtime.HttpJspBase
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
 * Copyright (c) 2003, 2004 IBM Corporation and others.
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
      out.write('\n');

String mode = request.getParameter(ActionInputs.ACTION_ENGINE_MODE);
String scenario = null;
try
{
  MultipartFormDataParser parser = new MultipartFormDataParser();
  parser.parseRequest(request, HTMLUtils.UTF8_ENCODING);
  scenario = parser.getParameter(ActionInputs.ACTION_ENGINE_SCENARIO);
}
catch (Throwable t)
{
}
ActionEngine actionEngine = controller.getActionEngine();
if (mode != null)
{
  actionEngine.setMode(ActionEngine.MODE_DISABLED);
  try
  {
    actionEngine.setMode(Byte.parseByte(mode));
  }
  catch (NumberFormatException nfe)
  {
  }
}
if (scenario != null && scenario.length() > 0)
{
  ScenarioDescriptor scenarioDescriptor = null;
  try
  {
    ActionDataParser parser = new ActionDataParser();
    scenarioDescriptor = parser.parseScenario(XMLUtils.stringToElement(scenario));
    actionEngine.executeScenario(scenarioDescriptor);
  }
  catch (Throwable t)
  {
  }
  if (scenarioDescriptor != null)
  {
    TransactionDescriptor[] transactionDescriptors = scenarioDescriptor.getTransactionDescriptors();
    for (int i = 0; i < transactionDescriptors.length; i++)
    {
      ActionDescriptor[] actionDescriptors = transactionDescriptors[i].getActionDescriptors();
      for (int j = 0; j < actionDescriptors.length; j++)
      {
        
      out.write("\n");
      out.write("        <!--\n");
      out.write("        ");
      out.print(actionDescriptors[j].getId());
      out.write("\n");
      out.write("        ");
      out.print(actionDescriptors[j].getStatusId());
      out.write("\n");
      out.write("        ");

        List status = actionDescriptors[j].getStatus();
        if (status != null)
        {
          for (Iterator it = status.iterator(); it.hasNext();)
          {
            
      out.write("\n");
      out.write("            ");
      out.print(it.next().toString());
      out.write("\n");
      out.write("            ");

          }
        }
        
      out.write("\n");
      out.write("        -->\n");
      out.write("        ");

      }
    }
  }
}

      out.write('\n');
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
