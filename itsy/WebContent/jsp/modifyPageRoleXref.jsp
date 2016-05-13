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
<%
	PageBO pageBO = (PageBO) NullUtil.checkNull(request.getAttribute("PageInfo"), new PageBO()); 
	String errorMessage = pageBO.getAttribute("Error"); 
	
	PageMode mode = PageMode.determineMode(pageBO.getAttribute("roleid"));
	
	String title = (mode.equals(PageMode.ADD)       ? "Add " : "Edit ") + "Role";
	String action = (mode.equals(PageMode.ADD) 		? "&action=addRole"  : "&action=editRole");
	
	boolean PAGE_OPTIONS_EDITABLE = "".equals(pageBO.getPageId());
	boolean ROLE_OPTIONS_EDITABLE = "".equals(pageBO.getAttribute("roleid").trim());
	
	String returnServlet = (String) request.getAttribute("returnServlet");
	returnServlet = TextUtil.isEmpty(returnServlet) ? "/itsy/PageServlet" : returnServlet;
	
	String READ_ONLY = permission.isReadOnly() ? "DISABLED" : "";
	
	String availableRoles = pageBO.getAttribute("AvailableRoles"); 
	String assignedRoles = pageBO.getAttribute("AssignedRoles");
%>
<html>
	<head>
		<title><%= title %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="GENERATOR" content="Rational Application Developer">
		<link rel="stylesheet" href="/itsy/css/standard2.css" type="text/css">
		<script src="/itsy/javascript/common.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script language="javascript">
		 	var available_roles_array = new Array(<%= availableRoles %>);  
			var assigned_roles_aray = new Array(<%= assignedRoles %>);
			
			function deletePageRoleXrefEntry(pageId, roleId) {
				if (confirm("WARNING:\n* Are you sure you want to delete this entry ?")) {
					avoidDoubleClick();
					document.location = "/itsy/PageServlet?pageId=" + pageId + "&roleId=" + roleId + "&mode=deleteRole";		
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
			     
			    <% if (ROLE_OPTIONS_EDITABLE) { %>
				    // start -- role
				    var role = document.getElementById('assigned_roles');
					if (role.value == "UNCHANGED") {
				   		submitOK = false;
					 	Error += "\n* Role(s) must be selected";
				    }
				    // end -- role
				<% } %>
			    
			    // start -- access level
			    var accesslevel = document.getElementById('accessLevelId');
				if (accesslevel.value == "" || accesslevel.value == "0") {
			   		submitOK = false;
				 	Error += "\n* Access Level must be selected";
				 	if (focusElement == null) {
					 	focusElement = accesslevel;
					}
			    }
			    // end -- access level
			    
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
		<form name="detail" method="post" action="<%= returnServlet %>?mode=editRole<%= action %>" onsubmit="return validate()">
		   	<input type="hidden" name="saveNew" value="false" />
		   	<input type="hidden" name="closePopup" value="false" />
		   	<input type="hidden" name="isAvailable" value="Y" />
		   	<input type="hidden" id="assigned_roles" name="assigned_roles" value="UNCHANGED" />
		   	
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
						<% if (permission.hasUpdate()) { %>
							<% if (PageMode.EDIT.equals(mode) && permission.hasDelete()) {	%>
								<input type="button" class="appButtonBold" value="Delete" id="deleteButton" onclick="deletePageRoleXrefEntry('<%= pageBO.getPageId() %>', '<%= pageBO.getAttribute("roleid") %>');" tabindex="13"/>
							<% } %>
						<% } %>
					</td>
					<td class="fixedSection" nowrap align="right">
						<% if (permission.hasUpdate()) { %>
							<% if (!ROLE_OPTIONS_EDITABLE) { %>
								<input type="button" class="appButtonBold" value="Save" id="saveButton" onclick="save();" tabindex="14"/>
								<% if (permission.hasInsert()) { %>
									<input type="button" class="appButtonBold" value="Save & New" id="saveAndNewButton" onclick="refreshParent(); saveAndNew();" tabindex="15"/>
								<% } %>
							<% } %>
							<input type="button" class="appButtonBold" value="Save & Close" id="saveAndCloseButton" onclick="refreshParent(); saveAndClose();" tabindex="16"/>
						<% } %>
						<input type="button" value="Cancel" class="appButtonBold" onclick="refreshParent(); window.close();" tabindex="17"/>
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
						<td width="80%" class="appFieldTitle"><%= TextUtil.print(pageBO.getDescription()) %> <input type="hidden" name="pageId" value="<%= pageBO.getPageId() %>" /></td>
					<% } %>
				</tr>
				
				<% if (ROLE_OPTIONS_EDITABLE) { %>
				
					<tr>
				    	<td width="100%" colspan="3" align="center">
				    		<input type="hidden" id="accessLevelId" name="accessLevelId" value="4">
				    		<br>
				    		<div class="appHeaderPanel" align="left">Roles</div>
				    		<br>
					        <table class="boxsolid-active" width="50%">
								<tr>
									<td>&nbsp;</td>
									<td class="appFieldTitle" colspan="3"></td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
								   	<td>
								   		<select class="appField" <%= READ_ONLY %> id="availableRoles" multiple size="10" style="width:260px; height:282px;" tabindex="19">
											<script>
												for (var i=0; i < (available_roles_array != null ? available_roles_array.length : 0); i++) {
													document.write('<option value=\'' + available_roles_array[i] + '\'>' + available_roles_array[i] + '</option>');
												}
											</script>
										</select>
									</td>
									<td>
										<table>
											<tr><td>
												<input <%= READ_ONLY %> type="button" class="rightButton" onclick="moveAttribute('availableRoles', 'assignedRoles'); saveAttributes('assignedRoles', 'assigned_roles');" tabindex="20">
											</td><tr>
											<tr><td>
												<input <%= READ_ONLY %> type="button" class="leftButton" onclick="moveAttribute('assignedRoles', 'availableRoles'); saveAttributes('assignedRoles', 'assigned_roles');" tabindex="21">
											</td><tr>
										</table>
									</td>
									<td>
										<select class="appField" <%= READ_ONLY %> id="assignedRoles" name="assignedRoles" multiple size="10" style="width:260px; height:282px;" tabindex="22">
											<script language="JavaScript">
												for (var j=0; j < (assigned_roles_aray != null ? assigned_roles_aray.length : 0); j++) {
													document.write('<option value=\'' + assigned_roles_aray[j] + '\'>' + assigned_roles_aray[j] + '</option>');
												}
											</script>
										</select>
									</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td class="appFieldHint" align="center">(available)</td>
									<td>&nbsp;</td>
									<td class="appFieldHint" align="center">(assigned)</td>
									<td>&nbsp;</td>
								</tr>
							</table>
						</td>
					</tr>
					
		        <% } else { %> 	
				        	
					<tr>
				    	<td width="5%">&nbsp;</td>
				        <td width="15%" class="appFieldTitle" style="font-weight: normal" align="right" nowrap>Access Level&nbsp;<%= Constants.ASTERISK %></td> 			   
						<td width="80%"><select class="appField" name="accessLevelId" id="accessLevelId" tabindex="3"><%= pageBO.getAttribute("AccessLevelOptionsForRole") %></select></td>
					</tr>
					<tr>
				    	<td width="5%">&nbsp;</td>
				        <td width="15%" class="appFieldTitle" style="font-weight: normal" align="right" valign="top" nowrap>Role&nbsp;&nbsp;</td>
				        <td width="80%" class="appFieldTitle"><%= pageBO.getAttribute("RoleName") %> <input type="hidden" id="roleId" name="roleId" value="<%= pageBO.getAttribute("roleid") %>" /></td>	
					</tr>
					
				<% } %>
				
			</table>
		</form>
	</body>
</html>
<%
	pageBO = null;
	mode = null;
%>