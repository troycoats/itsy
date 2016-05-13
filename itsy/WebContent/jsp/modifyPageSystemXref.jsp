<!DOCTYPE HTML>
<jsp:useBean id="permission" type="com.itsy.session.Permission" scope="request" />
<%@page import="com.itsy.session.PageMode" %>
<%@page import="com.itsy.dataaccess.page.PageBO" %>
<%@page import="com.itsy.util.TextUtil" %>
<%@page import="com.itsy.util.NullUtil" %>
<%@page import="com.itsy.constants.Constants" %>
<%@page import="com.itsy.session.URLEncryption" %>
<%@page import="com.itsy.session.PageVariables" %>
<%
	PageBO pageBO = (PageBO) NullUtil.checkNull(request.getAttribute("PageInfo"), new PageBO()); 
	String errorMessage = pageBO.getAttribute("Error"); 
	
	PageMode mode = PageMode.resolveEnumFromString(URLEncryption.getParamAsString(request, "mode"));
	
	String title = (mode.equals(PageMode.ADD)       ? "Add " : "Edit ") + "System Area";
	String action = (mode.equals(PageMode.ADD) 		? "&action=addArea"  : "&action=editArea");
	
	boolean PAGE_OPTIONS_EDITABLE = "".equals(pageBO.getPageId());
	boolean AREA_OPTIONS_EDITABLE = "".equals(pageBO.getAttribute("areaid").trim());
%>
<html>
	<head>
		<title><%= title %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="GENERATOR" content="Rational Application Developer">
		<link rel="stylesheet" href="/itsy/css/standard2.css" type="text/css">
		<script language="javascript" src="/itsy/javascript/common.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script language="javascript">
			function deletePageSystemXrefEntry(pageId, areaId) {
				if (confirm("WARNING:\n* Are you sure you want to delete this entry ?")) {
					avoidDoubleClick();
					window.location = "/itsy/PageServlet?pageId=" + pageId + "&areaId=" + areaId + "&mode=deleteArea";		
					refreshParent();
				}
			}
			
			function avoidDoubleClick() {
				<% if (permission.hasUpdate()) { %>
					avoidDC('saveButton');
					<% if (permission.hasInsert()) { %>
						avoidDC('saveAndNewButton');
					<% } %>
				    avoidDC('saveAndCloseButton');
					<% if (PageMode.EDIT.equals(mode) && permission.hasDelete()) { %>
						avoidDC('deleteButton');
					<% } %>
				<% } %>
			}
			
		    function validate() {  
		    	
				avoidDoubleClick();
				
			  	var Error = "Error:";
			  	var submitOK = true;
			  	var focusElement = null;
			     
			    /* 
			    // start -- display order
			    var displayOrder = document.getElementById('displayOrder');
				if (displayOrder.value == "") {
			   		submitOK = false;
				 	Error += "\n* Display Order must be entered";
						 	focusElement = displayOrder;
			    }
			    // end -- display order
			    */
			    
				if (!submitOK) {
					alert(Error);
					if (focusElement != null) {
						focusElement.focus();
						if (focusElement.type == "text") 
							focusElement.select();
					}
			 	}
				return submitOK;
			}
		</script>
	</head>
	<body>
		<form name="detail" method="post" action="/itsy/PageServlet?mode=editArea<%= action %>" onsubmit="return validate()">
		   	<input type="hidden" name="saveNew" value="false" />
		   	<input type="hidden" name="closePopup" value="false" />
		   	
		   	<div class="darkGreyHeaderPanel">
				<table width="100%">
					<tr>
						<td class="appHeaderWhite" nowrap align="left"><%= title %></td>
						<td class="appHeaderWhite" nowrap align="right"></td>
					</tr>
				</table>
			</div>
			
			<% if (!TextUtil.isEmpty(errorMessage)) { %>
				<div class="errorMessageSection"><%= TextUtil.print(errorMessage) %></div>
			<% } %>
			
			<table width="100%" class="table-bottom">
				<tr>
					<td class="fixedSection" nowrap align="left">
						<% if (permission.hasUpdate()) { %>
							<% if (PageMode.EDIT.equals(mode) && permission.hasDelete()) {	%>
								&nbsp;
								<input type="button" class="appButtonBold" value="Delete" id="deleteButton" onclick="deletePageSystemXrefEntry('<%= pageBO.getPageId() %>', '<%= pageBO.getAttribute("areaid") %>');" tabindex="16"/>
							<% } %>
						<% } %>
					</td>
					<td class="fixedSection" nowrap align="right">
						<% if (permission.hasUpdate()) { %>
							<input type="button" class="appButtonBold" value="Save" id="saveButton" onclick="save();" tabindex="13"/>
							<% if (permission.hasInsert()) { %>
								<input type="button" class="appButtonBold" value="Save & New" id="saveAndNewButton" onclick="saveAndNew();" tabindex="14"/>
							<% } %>
							<input type="button" class="appButtonBold" value="Save & Close" id="saveAndCloseButton" onclick="saveAndClose();" tabindex="15"/>
						<% } %>
						<input type="button" value="Cancel" class="appButtonBold" onclick="window.close();" tabindex="17"/>
						&nbsp;
					</td>
				</tr>
			</table>
		   	
		   	<table border="0" class="appTableNoBorder" width="100%" align="center">
				<tr>
					<td width="100%" colspan="3" height="5"></td>
				</tr>
				<tr>
			    	<td width="5%">&nbsp;</td>
			        <td width="15%" class="appFieldTitle" style="font-weight: normal" align="right" nowrap>Page Description&nbsp;&nbsp;</td>
			        <% if (PAGE_OPTIONS_EDITABLE) { %> 			   
			        	<td width="80%"><select class="appField" name="pageId" id="pageId" tabindex="1"><%= pageBO.getAttribute("PageOptions") %></select></td>
			        <% } else { %>
						<td width="80%" class="appFieldTitle">
							<%= TextUtil.print(pageBO.getDescription()) %>
							<input type="hidden" name="pageId" value="<%= pageBO.getPageId() %>" />
						</td>
					<% } %>
				</tr>
				<tr class="oddRow">
			    	<td width="5%">&nbsp;</td>
			        <td width="15%" class="appFieldTitle" style="font-weight: normal" align="right" nowrap>Area&nbsp;&nbsp;</td>
			        <% if (AREA_OPTIONS_EDITABLE) { %>
			        	<td width="80%"><select class="appField" name="areaId" id="areaId" tabindex="1"><%= pageBO.getAttribute("AreaOptions") %></select></td>
			        <% } else { %> 	
			        	<td width="80%" class="appFieldTitle"><%= pageBO.getAttribute("AreaName") %> <input type="hidden" id="areaid" name="areaId" value="<%= pageBO.getAttribute("areaid") %>" /></td>
			        <% } %>
				</tr>
				<tr class="oddRow">
			    	<td width="5%">&nbsp;</td>
			        <td width="15%" class="appFieldTitle" style="font-weight: normal" align="right" nowrap>Menu Caption&nbsp;&nbsp;</td> 			   
					<td width="80%"><input type="text" class="appField" maxlength="255" name="parentMenu" id="parentMenu" tabindex="3" value="<%= pageBO.getAttribute("parentmenu") %>"></td>
				</tr>
			</table>
		</form>
	</body>
</html>
<%
	pageBO = null;
	mode = null;
%>