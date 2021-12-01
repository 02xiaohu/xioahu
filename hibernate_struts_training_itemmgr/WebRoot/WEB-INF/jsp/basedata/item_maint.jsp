<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>          
<html>
<head><meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title></title>
<link rel="stylesheet" href="../style/drp.css">
<script src="../script/windows.js"></script>
<script type="text/javascript">

	function addItem() {
		window.self.location = "additem.do?pageNo=${itemForm.pageNo}&pageSize=${itemForm.pageSize}";
	}
	
	function modifyItem() {
		var count = 0;
		var j = 0;
		for (var i = 0; i < document.getElementsByName("selectFlag").length; i++) {
			if (document.getElementsByName("selectFlag")[i].checked) {
				j = i;
				count++;
			}
		}
		if (count == 0) {
			alert("请选择需要修改的物料！");
			return;
		}
		if (count > 1) {
			alert("一次只能修改一个物料！");
			return;
		}
		if (count == 1) {
			window.self.location = "item.do?command=modifyDetail&itemNo=" + 
			                        document.getElementsByName("selectFlag")[j].value + 
			                        "&pageNo=${itemForm.pageNo}&pageSize=${itemForm.pageSize}";
		}
	}
	
	function deleteItem() {
		var flag = false;
		for (var i = 0; i < document.getElementsByName("selectFlag").length; i++) {
			if (document.getElementsByName("selectFlag")[i].checked) {
				flag = true;
			}		
		}
		if (!flag) {
			alert("请选择需要删除的物料！");
			return;
		}
		if (window.confirm("确认删除吗？")) {
			with (document.getElementById("itemForm")) {
				method = "post";
				action = "item.do?command=del&pageNo=${itemForm.pageNo}&pageSize=${itemForm.pageSize}";
				submit();
			}
		}
	}
	
	function checkAll() {
		for (var i = 0; i < document.getElementsByName("selectFlag").length; i++) {
			document.getElementsByName("selectFlag")[i].checked = document.getElementById("ifAll").checked;
		}
	}
	
	function uploadPic4Item() {
		var count = 0;
		var j = 0;
		for (var i = 0; i < document.getElementsByName("selectFlag").length; i++) {
			if (document.getElementsByName("selectFlag")[i].checked) {
				j = i;
				count++;
			}
		}
		if (count == 0) {
			alert("请选择需要上传的物料！");
			return;
		}
		if (count > 1) {
			alert("一次只能上传一个物料！");
			return;
		}
		window.self.location = "item.do?command=uploadDetail&itemNo=" +
		                       document.getElementsByName("selectFlag")[j].value + 
		                       "&pageNo=${itemForm.pageNo}&pageSize=${itemForm.pageSize}";;
	}
	
	function queryItem() {
		with (document.getElementById("itemForm")) {
			method = "post";
			action = "item.do?command=list&pageNo=1&pageSize=${itemForm.pageSize}";
			submit();
		}
	}
	
	function resetItem() {
		document.getElementsByName("clientIdOrName").value = "";
	}
	
	function topPage() {
		window.self.location = "item.do?command=list&pageNo=${pagemodel.topPageNo}&pageSize=${itemForm.pageSize}&clientIdOrName=${ itemForm.clientIdOrName }"
	}
	
	function previousPage() {
		window.self.location = "item.do?command=list&pageNo=${pagemodel.previousPageNo}&pageSize=${itemForm.pageSize}&clientIdOrName=${ itemForm.clientIdOrName }"
	}	
	
	function nextPage() {
		window.self.location = "item.do?command=list&pageNo=${pagemodel.nextPageNo}&pageSize=${itemForm.pageSize}&clientIdOrName=${ itemForm.clientIdOrName }"
	}
	
	function bottomPage() {
		window.self.location = "item.do?command=list&pageNo=${pagemodel.bottomPageNo}&pageSize=${itemForm.pageSize}&clientIdOrName=${ itemForm.clientIdOrName }"
	}
	
	function myOnkeypress() {
		if (window.event.keyCode == 13) {
			queryItem();
		}
	}
	
</script>
</head>

<body  class="body1">
<form name="itemForm" id="itemForm">
  <div align="center">
    <table width="95%" border="0" cellspacing="2" cellpadding="2">
      <tr>
        <td>&nbsp;</td>
      </tr>
    </table>
    <table width="95%" border="0" cellspacing="0" cellpadding="0" height="8">
      <tr>
        <td width="522" class="p1" height="2" nowrap><img src="../images/mark_arrow_02.gif" width="14" height="14">&nbsp;<b><bean:message key="drp.basedata"/>&gt;&gt;<bean:message key="drp.basedata.item"/></b></td>
      </tr>
    </table>
    <hr width="97%" align="center" size=0>
    <table width="95%" border="0" cellpadding="0" cellspacing="0">
      <tr> 
        <td width="17%" height="29"> <div align="left"><bean:message key="drp.basedata.item.itemNo"/>/<bean:message key="drp.basedata.item.itemName"/>:</div></td>
        <td width="46%"><input name="clientIdOrName" type="text"  class="text1" id="clientIdOrName" value="${ itemForm.clientIdOrName }" size="50" maxlength="50" onkeypress="myOnkeypress()"> 
        </td>
        <td width="37%"> <div align="left"> 
            <input name="btnQuery" type="button" class="button1" id="btnQuery"  value="<bean:message key="drp.button.query"/>" onClick="queryItem()">
            &nbsp;
            <input name="btnReset" type="button" class="button1" id="btnReset"  value="<bean:message key="drp.button.reset"/>" onClick="resetItem()">
        </div></td>
      </tr>
      <tr> 
        <td height="16"> 
          <div align="right"></div></td>
        <td>&nbsp; </td>
        <td><div align="right"></div></td>
      </tr>
    </table>
    
  </div>
  <table width="95%" border="0" cellspacing="0" cellpadding="0"  class="rd1" align="center">
    <tr> 
      <td nowrap height="10" class="p2"><bean:message key="drp.basedata.item.title"/></td>
      <td nowrap  height="10" class="p3">&nbsp;</td>
    </tr>
  </table>
  <table width="95%" border="1" cellspacing="0" cellpadding="0" align="center" class="table1">
    <tr> 
      <td width="35" class="rd6"><input type="checkbox" name="ifAll" onClick="checkAll()" ></td>
      <td width="170" class="rd6"><bean:message key="drp.basedata.item.itemNo"/></td>
      <td width="222" class="rd6"><bean:message key="drp.basedata.item.itemName"/></td>
      <td width="195" class="rd6"><bean:message key="drp.basedata.item.spec"/></td>
      <td width="293" class="rd6"><bean:message key="drp.basedata.item.pattern"/></td>
      <td width="293" class="rd6"><bean:message key="drp.basedata.item.category"/></td>
      <td width="293" class="rd6"><bean:message key="drp.basedata.item.unit"/></td>
    </tr>
    <c:forEach items="${pagemodel.list}" var="item">
	    <tr> 
	      <td width="35" class="rd8" ><input type="checkbox" name="selectFlag" id="selectFlag" class="checkbox1" value="${item.itemNo }"></td>
	      <td width="170" class="rd8" ><a href="item.do?command=list&pageNo=${itemForm.pageNo}&pageSize=${itemForm.pageSize}" onclick="window.open('item.do?command=findDetail&itemNo=${item.itemNo}', '物料详细信息', 'width=450, height=400, scrollbars=no')">${ item.itemNo }</a></td>
	      <td width="222" class="rd8" >${ item.itemName }</td>
	      <td width="195" class="rd8" >${ item.spec }</td>
	      <td width="293" class="rd8" >${ item.pattern }</td>
	      <td width="293" class="rd8" >${ item.category.name }</td>
	      <td width="293" class="rd8" >${ item.unit.name }</td>
	    </tr>
    </c:forEach>
  </table>
  <table width="95%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="rd1">
    <tr> 
      <td  nowrap class="rd19" height="2" width="36%"> <div align="left"><font color="#FFFFFF">&nbsp;<bean:message key="drp.page.totalPages"/>:&nbsp${pagemodel.totalPages }</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color="#FFFFFF"><bean:message key="drp.page.currentPage"/>:</font>&nbsp<font color="#FF0000">${pagemodel.pageNo }</font>&nbsp<font color="#FFFFFF"></font> 
        </div></td>
      <td  nowrap class="rd19" width="64%"> <div align="right">
        <input name="btnTopPage" class="button1" type="button" id="btnTopPage" value="|&lt;&lt; "  title="<bean:message key="drp.button.topPage"/>" onClick="topPage()">
        <input name="btnPreviousPage" class="button1" type="button" id="btnPreviousPage" value=" &lt;  "  title="<bean:message key="drp.button.previousPage"/>" onClick="previousPage()">
        <input name="btnNext" class="button1" type="button" id="btnNext" value="  &gt; "  title="<bean:message key="drp.button.nextPage"/>" onClick="nextPage()">
        <input name="btnBottomPage" class="button1" type="button" id="btnBottomPage" value=" &gt;&gt;|"  title="<bean:message key="drp.button.bottomPage"/>" onClick="bottomPage()">
        <input name="btnAdd" type="button" class="button1" id="btnAdd"  value="<bean:message key="drp.button.add"/>" onClick="addItem()">
        <input name="btnDelete" class="button1" type="button" id="btnDelete" value="<bean:message key="drp.button.delete"/>" onClick="deleteItem()">
        <input name="btnModify" class="button1" type="button" id="btnModify" value="<bean:message key="drp.button.modify"/>"  onClick="modifyItem()">
        <input name="btnUpload" class="button1" type="button" id="btnUpload" value="<bean:message key="drp.button.uploadImage"/>"  onClick="uploadPic4Item()">
      </div></td>
    </tr>
  </table>
</form>
</body>
</html>