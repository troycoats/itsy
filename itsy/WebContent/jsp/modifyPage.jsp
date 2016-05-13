<!DOCTYPE HTML>
<jsp:useBean id="permission" type="com.itsy.session.Permission" scope="request" />
<%@page import="com.itsy.session.PageMode" %>
<%@page import="com.itsy.dataaccess.page.PageBO" %>
<%@page import="com.itsy.util.TextUtil" %>
<%@page import="com.itsy.util.NullUtil" %>
<%@page import="com.itsy.constants.Constants" %>
<%@page import="com.itsy.servlets.PageServlet" %>
<%@page import="com.itsy.session.URLEncryption" %>
<%@page import="com.itsy.session.PageVariables" %>
<%@page import="java.util.Collection" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.List" %>
<%
	PageBO pageBO = (PageBO) NullUtil.checkNull(request.getAttribute("PageInfo"), new PageBO()); 
	String errorMessage = pageBO.getAttribute("Error"); 
	String availableOptions = pageBO.getAttribute("AvailableOptions"); 
	
	List referencedRoles = (List) request.getAttribute("ReferencedRoles"); 
	List referencedAreas = (List) request.getAttribute("ReferencedAreas"); 
	
	PageMode mode = PageMode.determineMode(pageBO.getPageId());

	URLEncryption cryptor = new URLEncryption("/itsy/PageServlet");

	String addRolePopupUrl = "?mode=addRole&" + Constants.FIRST_TIME + "=Y&id=" + pageBO.getPageId();
	String addAreaPopupUrl = "?mode=addArea&" + Constants.FIRST_TIME + "=Y&id=" + pageBO.getPageId();

	String editRolePopupUrl = "?mode=editRole&" + Constants.FIRST_TIME + "=Y&id=" + pageBO.getPageId() + "&roleId=";
	String editAreaPopupUrl = "?mode=editArea&" + Constants.FIRST_TIME + "=Y&id=" + pageBO.getPageId() + "&areaId=";
	
	String title = (PageMode.ADD.equals(mode) ? "Add " : "Edit ") + "Page";
	
	String READ_ONLY = permission.isReadOnly() ? "DISABLED" : "";
%>
<html>
	<head>
		<title><%= title %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="GENERATOR" content="Rational Application Developer">
		<link rel="stylesheet" href="/itsy/css/standard1.css?nocache=<%= Constants.SYSTEM_VERSION %>" type="text/css">
		<script language="javascript" src="/itsy/javascript/common.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script language="javascript" src="/itsy/javascript/sorttable.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script language="javascript" src="/itsy/javascript/standard.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script language="javascript">
			function deletePageEntry(id) {
				if (confirm("WARNING:\n* Are you sure you want to delete this entry ?")) {
					avoidDoubleClick();
					window.location = "/itsy/PageServlet?pageId=" + id + "&mode=deletePage";		
					refreshParent();
				}
			}
	
			function avoidDoubleClick() {
				<% if (permission.hasUpdate() && TextUtil.isEmpty(READ_ONLY)) { %>
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
			     
			    // start -- description
			    var description = document.getElementById('entry_description');
				if (description.value == "") {
			   		submitOK = false;
				 	Error += "\n* Description cannot be blank!";
					focusElement = description;
			    }
			    // end -- description
				
			    // start -- url
			    var url = document.getElementById('pageUrl');
				if (url.value == "") {
			   		submitOK = false;
				 	Error += "\n* Page URL cannot be blank!";
					focusElement = url;
			    }
			    // end -- url
			    
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
		<form name="detail" method="post" action="/itsy/PageServlet?mode=editPage" onsubmit="return validate()">
		   	<input type="hidden" name="pageId" value="<%= pageBO.getPageId() %>" />
		   	<input type="hidden" name="saveNew" value="false" />
		   	<input type="hidden" name="closePopup" value="false" />
		   	<input type="hidden" name="mode" value="editPage" />
		   	
		   	<div class="darkGreyHeaderPanel">
				<table width="100%">
					<tr>
						<td class="appHeaderWhite" nowrap align="left"><%= title %></td>
					</tr>
				</table>
			</div>
			
			<% if (!TextUtil.isEmpty(errorMessage)) { %>
				<div class="errorMessageSection"><%= TextUtil.print(errorMessage) %></div>
			<% } %>
			
			<table width="100%" class="table-bottom">
				<tr>
					<td class="fixedSection" nowrap align="left">
						<% if (permission.hasUpdate() && TextUtil.isEmpty(READ_ONLY)) { %>
							<% if (PageMode.EDIT.equals(mode) && permission.hasDelete()) { %>
								&nbsp;
								<input type="button" class="appButtonBold" value="Delete" id="deleteButton" onclick="deletePageEntry('<%= pageBO.getPageId() %>');" tabindex="16"/>
							<% } %>
						<% } %>
					</td>
					<td class="fixedSection" nowrap align="right">
						<% if (permission.hasUpdate() && TextUtil.isEmpty(READ_ONLY)) { %>
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

		   	<table border="0" width="100%" align="center" cellspacing="0" cellpadding="0"><tr><td>
			   					
			    <table border="0" class="appTableNoBorder" width="100%" align="center">
					<tr>
						<td width="100%" colspan="3" height="5"></td>
					</tr>
					<tr>
				    	<td width="5%">&nbsp;</td>
				        <td class="appFieldTitle" style="font-weight: normal" width="15%" align="right">Description&nbsp;<%= Constants.ASTERISK %></td> 			   
						<td width="80%"><input <%= READ_ONLY %> class="appField" style="width: 375px" maxlength="30" type="text" name="entry_description" id="entry_description" value="<%=TextUtil.print(pageBO.getDescription())%>" tabindex="1">
						<% if (TextUtil.isEmpty(READ_ONLY)) { %><script language="javascript">document.getElementById('entry_description').focus()</script><% } %>
						&nbsp;&nbsp;<img src="/itsy/images/info.gif" title="Used to create the menu, alter only if you know what you're doing! &#13;&#13;You can change the menu caption by altering the Menu Caption in System Areas below.">  
						</td>
					</tr>
					<tr>
			    		<td>&nbsp;</td>
			        	<td class="appFieldTitle" style="font-weight: normal" align="right">URL&nbsp;<%= Constants.ASTERISK %></td> 			   
						<td><input <%= READ_ONLY %> class="appField" style="width: 375px" maxlength="200" type="text" name="pageUrl" id="pageUrl" value="<%= TextUtil.print(pageBO.getPageUrl()) %>" tabindex="2"></td>
					</tr>
					<tr>
			    		<td>&nbsp;</td>
			        	<td class="appFieldTitle" style="font-weight: normal" align="right" nowrap>Available ?&nbsp;&nbsp;</td> 			   
						<td><select <%= READ_ONLY %> class="appDropDown" style="width: 40px" id="isAvailable" name="isAvailable" tabindex="3"><%= availableOptions %></select></td>
					</tr>
					<tr><td height="5"></td></tr>
				</table>
			
				</td></tr><tr><td>
				
				<% if (PageMode.EDIT.equals(mode)) { %>
					
					<table class="appHeaderShortPanel" width="100%">
						<tr>
							<td class="appHeaderWhite" nowrap align="left">&nbsp;Roles</td>
							<td class="appHeaderWhite" nowrap align="right">
								<input <%= READ_ONLY %> type="button" class="darkButtonBold" value="Add" onclick="openPopup('<%= cryptor.urlEncrypt(addRolePopupUrl) %>', 'addRole', 550, 775);" tabindex="17"/>
								&nbsp;
							</td>
						</tr>
					</table>
				
					<table class="sortable" border="0" align="left" width="100%" cellspacing="0" cellpadding="1">
						<tr class="listTableHead" >
						 	<td width="1%"  align="left"   class="sorttable_nosort">&nbsp;</td>
						 	<td width="5%"  align="left"   class="sorttable_nosort">ID&nbsp;</td>
						 	<td width="40%" align="left"   nowrap>Description&nbsp;</td>
						 	<td width="44%" align="center" nowrap>Access&nbsp;Level&nbsp;</td>
							<td width="10%" align="right"  nowrap class="sorttable_nosort">&nbsp;&nbsp;</td>
						</tr>
						<%	
							PageBO bo = null;
							for (int i=0; i < (referencedRoles != null ? referencedRoles.size() : 0); i++) { 
								bo = (PageBO) referencedRoles.get(i);
						%>
					 	    <tr class="listTableVal" onclick="highlightTR(this);">
							    <td align="left"></td>
							    <td align="left"><a class="link" href="#" onclick="openPopup('<%= cryptor.urlEncrypt(editRolePopupUrl + bo.getAttribute("roleid")) %>', 'modifyRole', 390, 675);"><%= bo.getAttribute("roleid") %></a>&nbsp;</td>	
							    <td align="left"> <%= bo.getAttribute("roledescription") %></td>
							    <td align="center"> <%= bo.getAttribute("accessleveldescription") %> </td>
							    <td>&nbsp;</td>
							</tr>  
						<% } 
						   bo = null;
						%>		
					</table>
			
					</td></tr><tr><td>
			
					<br><br>
					<table class="appHeaderShortPanel" width="100%">
						<tr>
							<td class="appHeaderWhite" nowrap align="left">&nbsp;System&nbsp;Areas</td>
							<td class="appHeaderWhite" nowrap align="right">
								<% if (referencedAreas == null || referencedAreas.size() == 0) { %>
									<input <%= READ_ONLY %> type="button" class="darkButtonBold" value="Add" onclick="openPopup('<%= cryptor.urlEncrypt(addAreaPopupUrl) %>', 'addArea', 390, 675);" tabindex="18"/>
									&nbsp;
								<% } %>
							</td>
						</tr>
					</table>
				
					<table class="sortable" border="0" align="left" width="100%" cellspacing="0" cellpadding="1">
						<tr class="listTableHead" >
						 	<td width="1%"  align="left"   class="sorttable_nosort">&nbsp;</td>
						 	<td width="5%"  align="left"   class="sorttable_nosort">ID&nbsp;</td>
						 	<td width="20%" align="left"   nowrap>Description&nbsp;</td>
						 	<td width="64%" align="left"   nowrap>Menu&nbsp;</td>
							<td width="10%" align="right"  nowrap class="sorttable_nosort">&nbsp;&nbsp;</td>
						</tr>
						<%	
							for (int i=0; i < (referencedAreas != null ? referencedAreas.size() : 0); i++) { 
								bo = (PageBO) referencedAreas.get(i);
						%>
					 	    <tr class="listTableVal" onclick="highlightTR(this);">
							    <td align="left"></td>
							    <td align="left"><a class="link" href="#" onclick="openPopup('<%= cryptor.urlEncrypt(editAreaPopupUrl + bo.getAttribute("areaid")) %>', 'modifyArea', 390, 675);"><%= bo.getAttribute("areaid") %></a>&nbsp;</td>	
							    <td align="left"> <%= bo.getAttribute("areadescription") %></td>
							    <td align="left"> <%= bo.getAttribute("parentmenu") %></td>
							    <td>&nbsp;</td>
							</tr>  
						<% } 
 						   bo = null;
						%>		
					</table>
				<% } // edit mode %>
			</td></tr></table>
			<br><br><br>
		</form>
	</body>
</html>
<%
	pageBO = null;
	mode = null;
	referencedRoles = null;
	referencedAreas = null;
	cryptor = null;
%>