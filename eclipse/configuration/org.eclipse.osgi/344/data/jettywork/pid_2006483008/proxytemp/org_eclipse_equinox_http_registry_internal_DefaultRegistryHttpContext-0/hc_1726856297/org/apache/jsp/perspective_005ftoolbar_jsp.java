package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import org.eclipse.wst.ws.internal.explorer.platform.constants.*;
import org.eclipse.wst.ws.internal.explorer.platform.perspective.*;
import org.eclipse.wst.ws.internal.explorer.platform.actions.*;
import org.eclipse.wst.ws.internal.explorer.platform.engine.ActionEngine;

public final class perspective_005ftoolbar_jsp extends org.apache.jasper.runtime.HttpJspBase
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

StringBuffer hrefActionEngine = new StringBuffer();
hrefActionEngine.append(response.encodeURL(controller.getPathWithContext("actionengine_container.jsp")));
hrefActionEngine.append("?");
hrefActionEngine.append(ActionInputs.SESSIONID);
hrefActionEngine.append("=");
hrefActionEngine.append(session.getId());

      out.write("\n");
      out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">\n");
      out.write("<html lang=\"");
      out.print(response.getLocale().getLanguage());
      out.write("\">\n");
      out.write("<head>\n");
      out.write("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("  <title>");
      out.print(controller.getMessage("FRAME_TITLE_PERSPECTIVE_TOOLBAR"));
      out.write("</title>\n");
      out.write("  <link rel=\"stylesheet\" type=\"text/css\" href=\"");
      out.print(response.encodeURL(controller.getPathWithContext("css/toolbar.css")));
      out.write("\">\n");
      out.write("  <script language=\"javascript\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("scripts/toolbar.js")));
      out.write("\">\n");
      out.write("  </script>\n");
      out.write("  <script language=\"javascript\">\n");
      out.write("    function openActionEngineContainer()\n");
      out.write("    {\n");
      out.write("      var link = \"");
      out.print(hrefActionEngine.toString());
      out.write("\";\n");
      out.write("      var actionEngineContainer = window.open(link,\"actionEngineContainer\",\"height=100,width=350,status=yes,scrollbars=yes,resizable=yes\");\n");
      out.write("      if (actionEngineContainer.focus)\n");
      out.write("        actionEngineContainer.focus();\n");
      out.write("    }\n");
      out.write("  </script>\n");
      out.write("</head>\n");
      out.write("<body dir=\"");
      out.print(org.eclipse.wst.ws.internal.explorer.platform.util.DirUtils.getDir());
      out.write("\" class=\"gtoolbarbodymargin\">\n");
      out.write("<div id=\"globaltoolbar\">\n");
      out.write("<table width=\"100%\" cellpadding=3 cellspacing=0 border=0>\n");
      out.write("  <tr>\n");
      out.write("    <td class=\"text\" nowrap valign=\"middle\" width=\"100%\" height=25>");
      out.print(controller.getMessage("TITLE_WSEXPLORER"));
      out.write("</td>\n");
      out.write("    ");

    String altHistoryBack = controller.getMessage("ALT_BACK");
    String altHistoryForward = controller.getMessage("ALT_FORWARD");
    String altActionEngine = controller.getMessage("ALT_ACTION_ENGINE");
    String altUDDI = controller.getMessage("ALT_UDDI_PERSPECTIVE");
    String altWSIL = controller.getMessage("ALT_WSIL_PERSPECTIVE");
    String altWSDL = controller.getMessage("ALT_WSDL_PERSPECTIVE");
    String altFavorites = controller.getMessage("ALT_FAVORITES_PERSPECTIVE");
    
      out.write("   \n");
      out.write("    <td class=\"text\" nowrap valign=\"middle\" align=\"center\" width=16 height=26><a href=\"");
      out.print(response.encodeURL(controller.getPathWithContext(RetrieveHistoryAction.getActionLink(ActionInputs.JUMP_BACK))));
      out.write("\" target=\"");
      out.print(FrameNames.PERSPECTIVE_WORKAREA);
      out.write("\"><img class=\"normal\" alt=\"");
      out.print(altHistoryBack);
      out.write("\" title=\"");
      out.print(altHistoryBack);
      out.write("\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("images/back_enabled.gif")));
      out.write("\" onMouseOver=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/back_highlighted.gif")));
      out.write("';mouseover(this);\" onMouseOut=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/back_enabled.gif")));
      out.write("';mouseout(this)\" onMouseDown=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/back_highlighted.gif")));
      out.write("';mousedown(this)\" onMouseUp=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/back_enabled.gif")));
      out.write("';mouseup(this)\"></a></td>\n");
      out.write("    <td class=\"text\" nowrap valign=\"middle\" align=\"left\" height=25><a href=\"");
      out.print(response.encodeURL(controller.getPathWithContext(RetrieveHistoryAction.getActionLink(ActionInputs.JUMP_FORWARD))));
      out.write("\" target=\"");
      out.print(FrameNames.PERSPECTIVE_WORKAREA);
      out.write("\"><img class=\"normal\" alt=\"");
      out.print(altHistoryForward);
      out.write("\" title=\"");
      out.print(altHistoryForward);
      out.write("\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("images/forward_enabled.gif")));
      out.write("\" onMouseOver=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/forward_highlighted.gif")));
      out.write("';mouseover(this);\" onMouseOut=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/forward_enabled.gif")));
      out.write("';mouseout(this)\" onMouseDown=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/forward_highlighted.gif")));
      out.write("';mousedown(this)\" onMouseUp=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/forward_enabled.gif")));
      out.write("';mouseup(this)\"></a></td>\n");
      out.write("    ");

    ActionEngine actionEngine = controller.getActionEngine();
    if (actionEngine != null && actionEngine.getMode() != ActionEngine.MODE_DISABLED)
    {
    
      out.write("\n");
      out.write("      <td class=\"text\" nowrap valign=\"middle\" align=\"left\" height=25><a href=\"javascript:openActionEngineContainer()\"><img class=\"normal\" alt=\"");
      out.print(altActionEngine);
      out.write("\" title=\"");
      out.print(altActionEngine);
      out.write("\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif")));
      out.write("\" onMouseOver=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif")));
      out.write("';mouseover(this);\" onMouseOut=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif")));
      out.write("';mouseout(this)\" onMouseDown=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif")));
      out.write("';mousedown(this)\" onMouseUp=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/eview16/actionengine.gif")));
      out.write("';mouseup(this)\"></a></td>\n");
      out.write("    ");

    }
    
      out.write("\n");
      out.write("    <td class=\"text\" nowrap valign=\"middle\" align=\"left\" height=25><a href=\"");
      out.print(response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(ActionInputs.PERSPECTIVE_UDDI,false))));
      out.write("\" target=\"");
      out.print(FrameNames.PERSPECTIVE_WORKAREA);
      out.write("\"><img class=\"normal\" alt=\"");
      out.print(altUDDI);
      out.write("\" title=\"");
      out.print(altUDDI);
      out.write("\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("images/uddi_perspective_enabled.gif")));
      out.write("\" onMouseOver=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/uddi_perspective_highlighted.gif")));
      out.write("';mouseover(this);\" onMouseOut=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/uddi_perspective_enabled.gif")));
      out.write("';mouseout(this)\" onMouseDown=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/uddi_perspective_highlighted.gif")));
      out.write("';mousedown(this)\" onMouseUp=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/uddi_perspective_enabled.gif")));
      out.write("';mouseup(this)\"></a></td>\n");
      out.write("    <td class=\"text\" nowrap valign=\"middle\" align=\"left\" height=25><a href=\"");
      out.print(response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(ActionInputs.PERSPECTIVE_WSIL,false))));
      out.write("\" target=\"");
      out.print(FrameNames.PERSPECTIVE_WORKAREA);
      out.write("\"><img class=\"normal\" alt=\"");
      out.print(altWSIL);
      out.write("\" title=\"");
      out.print(altWSIL);
      out.write("\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsil_perspective_enabled.gif")));
      out.write("\" onMouseOver=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsil_perspective_highlighted.gif")));
      out.write("';mouseover(this);\" onMouseOut=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsil_perspective_enabled.gif")));
      out.write("';mouseout(this)\" onMouseDown=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsil_perspective_highlighted.gif")));
      out.write("';mousedown(this)\" onMouseUp=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsil_perspective_enabled.gif")));
      out.write("';mouseup(this)\"></a></td>\n");
      out.write("    <td class=\"text\" nowrap valign=\"middle\" align=\"left\" height=25><a href=\"");
      out.print(response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(ActionInputs.PERSPECTIVE_WSDL,false))));
      out.write("\" target=\"");
      out.print(FrameNames.PERSPECTIVE_WORKAREA);
      out.write("\"><img class=\"normal\" alt=\"");
      out.print(altWSDL);
      out.write("\" title=\"");
      out.print(altWSDL);
      out.write("\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_enabled.gif")));
      out.write("\" onMouseOver=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_highlighted.gif")));
      out.write("';mouseover(this);\" onMouseOut=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_enabled.gif")));
      out.write("';mouseout(this)\" onMouseDown=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_highlighted.gif")));
      out.write("';mousedown(this)\" onMouseUp=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/wsdl_perspective_enabled.gif")));
      out.write("';mouseup(this)\"></a></td>    \n");
      out.write("    <td class=\"text\" nowrap valign=\"middle\" align=\"left\" height=25><a href=\"");
      out.print(response.encodeURL(controller.getPathWithContext(ShowPerspectiveAction.getActionLink(ActionInputs.PERSPECTIVE_FAVORITES,false))));
      out.write("\" target=\"");
      out.print(FrameNames.PERSPECTIVE_WORKAREA);
      out.write("\"><img class=\"normal\" alt=\"");
      out.print(altFavorites);
      out.write("\" title=\"");
      out.print(altFavorites);
      out.write("\" src=\"");
      out.print(response.encodeURL(controller.getPathWithContext("images/favorites_perspective_enabled.gif")));
      out.write("\" onMouseOver=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/favorites_perspective_highlighted.gif")));
      out.write("';mouseover(this);\" onMouseOut=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/favorites_perspective_enabled.gif")));
      out.write("';mouseout(this)\" onMouseDown=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/favorites_perspective_highlighted.gif")));
      out.write("';mousedown(this)\" onMouseUp=\"src='");
      out.print(response.encodeURL(controller.getPathWithContext("images/favorites_perspective_enabled.gif")));
      out.write("';mouseup(this)\"></a></td>\n");
      out.write("  </tr>\n");
      out.write("</table>\n");
      out.write("</div>\n");
      out.write("</body>\n");
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
