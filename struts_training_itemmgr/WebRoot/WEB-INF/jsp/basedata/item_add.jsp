<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>       
<%@ taglib prefix="drp" uri="http://www.bjsxt.com/drp/functions"%>  
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>   
<html>
<head><meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title></title>
<link rel="stylesheet" href="../style/drp.css">
<script src="../script/client_validate.js"></script>
<script type="text/javascript">
	
	function addItem() {
		if (trim(document.getElementById("itemNo").value) == "") {
			alert("物料代码不能为空!");
			document.getElementById("itemNo").focus();
			return;
		} 
	
		if (trim(document.getElementById("itemName").value) == "") {
			alert("物料名称不能为空!");
			document.getElementById("itemName").focus();
			return;
		}
		with (document.getElementById("itemForm")) {
			method = "post";
			action = "item.do?command=add&pageNo=${itemForm.pageNo}&pageSize=${itemForm.pageSize}";
			submit();
		}
	}
	
	function goBack() {
		window.self.location = "item.do?command=list&pageNo=${itemForm.pageNo}&pageSize=${itemForm.pageSize}";
	}
	
	
</script>
</head>

<body  class="body1">
<form  name="itemForm" target="_self" id="itemForm">
  <div align="center"> 
    <table width="95%" border="0" cellspacing="2" cellpadding="2">
    <tr>
      <td>&nbsp;</td>
    </tr>
  </table>
  <table width="95%" border="0" cellspacing="0" cellpadding="0" height="25">
    <tr> 
        <td width="522" class="p1" height="25" nowrap><img src="../images/mark_arrow_03.gif" width="14" height="14">&nbsp;<b><bean:message key="drp.basedata"/>&gt;&gt;<bean:message key="drp.basedata.item"/>&gt;&gt;<bean:message key="drp.button.add"/></b></td>
    </tr>
  </table>
    <hr width="97%" align="center" size=0>
    <table width="95%" border="0" cellpadding="0" cellspacing="0">
      <tr> 
        <td width="22%" height="29"> <div align="right"><font color="#FF0000">*</font><bean:message key="drp.basedata.item.itemNo"/>:&nbsp; 
          </div></td>
        <td width="78%"><input name="itemNo" type="text"  class="text1" id="itemNo" size="10" maxlength="10"></td>
      </tr>
      <tr> 
        <td height="26"><div align="right"><font color="#FF0000">*</font><bean:message key="drp.basedata.item.itemName"/>:&nbsp;</div></td>
        <td><input name="itemName" type="text"  class="text1" id="itemName" size="20" maxlength="20"></td>
      </tr>
      <tr> 
        <td height="26"><div align="right"><bean:message key="drp.basedata.item.spec"/>:&nbsp;</div></td>
        <td><label>
          <input name="spec" type="text"  class="text1" id="spec" size="20" maxlength="20">
        </label></td>
      </tr>
      <tr> 
        <td height="26"> <div align="right"><bean:message key="drp.basedata.item.pattern"/>:&nbsp;</div></td>
        <td><input name="pattern" type="text"  class="text1" id="pattern" size="20" maxlength="20">        </td>
      </tr>
      <tr>
        <td height="26"><div align="right"><font color="#FF0000">*</font><bean:message key="drp.basedata.item.category"/>:&nbsp;</div></td>
        <td><select name="categoryId" class="select1" id="categoryId">
         	<c:forEach items="${drp:getItemCategoryList()}" var="ic" >
         		<option value="${ic.id}">${ic.name}</option>
        	</c:forEach>
        </select></td>
      </tr>
      <tr>
        <td height="26"><div align="right"><font color="#FF0000">*</font><bean:message key="drp.basedata.item.unit"/>:&nbsp;</div></td>
        <td><select name="unitId" class="select1" id="unitId">
         	<c:forEach items="${drp:getItemUnitList()}" var="iu" >
         		<option value="${iu.id}">${iu.name}</option>
        	</c:forEach>
        </select></td>
      </tr>
    </table>
    <hr width="97%" align="center" size=0>
    <div align="center">
      <input name="btnAdd" class="button1" type="button" id="btnAdd" value="<bean:message key="drp.button.add"/>" onclick="addItem()" >
      &nbsp;&nbsp;&nbsp;&nbsp; 
      <input name="" class="button1" type="button" id="btnBack" value="<bean:message key="drp.button.goback"/>"  onClick="goBack()">
    </div>
  </div>
</form>
</body>
</html>