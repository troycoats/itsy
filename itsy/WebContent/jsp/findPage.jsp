<!DOCTYPE HTML>
<jsp:useBean id="permission" type="com.itsy.session.Permission" scope="request" />
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
	String errorMessage = (String)request.getAttribute("Error");

	PageBO params = (PageBO) NullUtil.checkNull(request.getAttribute("searchCriteria"), new PageBO());
	String searchType = params.getAttribute(PageServlet.SEARCH_TYPE);					   
	String availableRoles = params.getAttribute("RoleOptions"); 
	String availableAreas = params.getAttribute("AreaOptions");

    URLEncryption cryptor = new URLEncryption("/itsy/PageServlet");
    
	String editPagePopupUrl = "?mode=editPage&" + Constants.FIRST_TIME + "=Y&id=";
	
	String addPagePopupUrl  = "?mode=addPage";
	String addPopupUrl = addPagePopupUrl;
		
	String title = "Define Page";
	String findButtonCaption = "Find Page";
	String addButtonCaption = "Add";
	if (PageServlet.SEARCH_BY_ROLE.equals(searchType)) { 
		findButtonCaption = "Find by Role";
	} else if (PageServlet.SEARCH_BY_AREA.equals(searchType)) { 
		findButtonCaption = "Find by Area";
	}
	
	int windowHeight = 550;
	int windowWidth = 625;
	
	String READ_ONLY = permission.isReadOnly() ? "DISABLED" : "";
%>
<html>
	<head>
		<title><%= title %></title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="GENERATOR" content="Rational Application Developer">
		<link rel="stylesheet" href="/itsy/css/standard.css?nocache=<%= Constants.SYSTEM_VERSION %>" type="text/css">
		<link rel="stylesheet" href="/itsy/css/dhtmlwindow.css?nocache=<%= Constants.SYSTEM_VERSION %>" type="text/css">
		<script type="text/javascript" src="/itsy/javascript/common.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script type="text/javascript" src="/itsy/javascript/standard.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script type="text/javascript" src="/itsy/javascript/dhtmlwindow.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script type="text/javascript" src="/itsy/javascript/dhtmlmodal.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script type="text/javascript" src="/itsy/javascript/sorttable.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<script>
			function changeSearch(type) {
				document.detail.requested.value = 'false';
				document.detail.<%= PageServlet.SEARCH_TYPE %>.value = type;
				document.detail.submit();
			}
				    
		    function saveDisplayOrders() {
		    	document.getElementById('mode').value = 'findDisplayOrders';
		    	document.detail.submit();
		    }
		    	    
			function validateAddPage() {
			  	var Error = "Error:";
			  	var submitOK = true;
			  	var focusElement = null;
		
				var addUrl = '<%= cryptor.urlEncrypt(addPopupUrl) %>';
	
				<% if (PageServlet.SEARCH_BY_ROLE.equals(searchType)) { %>
				   
				    // start -- role
					var role = document.getElementById('roleId');
					var url = addUrl + '&roleId=' + role.value;
					if (role.value == "" || role.value == "0") {
				   		submitOK = false;
					 	Error += "\n* Role must be selected to add a page.";
						focusElement = role;
				    } else {
				    }
				    // end -- role
				    
				<% } else if (PageServlet.SEARCH_BY_AREA.equals(searchType)) { %>
					
					// start -- area
					var area = document.getElementById('areaId');
					var url = addUrl + '&areaId=' + area.value;
					if (area.value == "" || area.value == "0") {
				   		submitOK = false;
					 	Error += "\n* Area must be selected to add a page.";
						focusElement = area;
				    } else {
				    }
				    // end -- area
				    
				<% } else { %>    
				    
				    var url = addUrl;
				    
				<% } %>    
				    
				if (submitOK) {
					openPopup(url, 'add', <%= windowHeight %>, <%= windowWidth %>);
				} else {
					alert(Error);
					if (focusElement != null) {
						focusElement.focus();
						if (focusElement.type == "text") 
							focusElement.select();
					}
			 	}
			}
		</script>
	</head>
	<body style="overflow:scroll;overflow-y:auto;overflow-x:auto;overflow:auto;">
		<form name="detail" method="post" action="/itsy/PageServlet">
			<input type="hidden" name="mode" id="mode" value="find" />
			<input type="hidden" name="requested" value="true" />
			<input type="hidden" name="<%= PageServlet.SEARCH_TYPE %>" value="<%= searchType %>" />
			
			<div class="appHeaderPanel"><%= title %></div>

			<% if (!TextUtil.isEmpty(errorMessage)) { %>
				<div class="errorMessageSection"><%= TextUtil.print(errorMessage) %></div>
			<% } %>
			
			<div id="tabOptions">
				<ul>
					<li><a href="#" <%= (PageServlet.SEARCH_BY_PAGE.equals(searchType) ? "class='active'" : "") %> onclick="changeSearch('<%= PageServlet.SEARCH_BY_PAGE %>');">&nbsp;Search&nbsp;By&nbsp;Page&nbsp;</a></li>
					<li><a href="#" <%= (PageServlet.SEARCH_BY_ROLE.equals(searchType) ? "class='active'" : "") %> onclick="changeSearch('<%= PageServlet.SEARCH_BY_ROLE %>');">&nbsp;Search&nbsp;By&nbsp;Role&nbsp;</a></li>
					<li><a href="#" <%= (PageServlet.SEARCH_BY_AREA.equals(searchType) ? "class='active'" : "") %> onclick="changeSearch('<%= PageServlet.SEARCH_BY_AREA %>');">&nbsp;Search&nbsp;By&nbsp;Area&nbsp;</a></li>
				</ul>
			</div>
			
			<% if (PageServlet.SEARCH_BY_PAGE.equals(searchType)) { %>
				
				<div id="searchByPageDiv">
					<table style="width: 100%" class="fixedSection" >
					  	<tr> 
					  		<td height="30px" width="1%"></td>
					    	<td width="10%" class="appFieldTitle" style="font-weight: normal" align="left" colspan="2" nowrap>
					    		Page&nbsp;Description&nbsp;
					     		<input class="appField" maxlength="80" type="text" name="entry_description" id="entry_description" value="<%= TextUtil.print(params.getDescription()) %>" tabindex="1">
					     		<script language="javascript">document.getElementById('entry_description').focus()</script>
					    	</td>
					    	<td width="89%" class="appFieldTitle" style="font-weight: normal" align="left" colspan="2" nowrap>
					    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Page&nbsp;URL&nbsp;
					     		<input class="appField" maxlength="80" type="text" name="pageUrl" id="pageUrl" value="<%= TextUtil.print(params.getPageUrl()) %>" tabindex="2">
					    	</td>
							<td nowrap align="right">
								<input type="submit" name="btFind" value="<%= findButtonCaption %>" class="appButtonBold" tabindex="6">&nbsp;
				  				<% if (permission.hasInsert() && !TextUtil.isEmpty(addButtonCaption)) { %>
				      				<input type="button" name="btAdd" value="<%= addButtonCaption %>" class="appButtonBold" onclick="validateAddPage();" tabindex="7">&nbsp;  
				  				<% } %>
			      				<% if (PageServlet.SEARCH_BY_PAGE.equals(searchType) && permission.hasUpdate()) { %>
				      				<!-- <input type="button" name="btSave" value="Save" class="appButtonBold" onclick="saveDisplayOrders();" tabindex="8"> -->  
			      				<% } %>
			      				&nbsp;
							</td>
						</tr>
					</table>
					<div class="appHeaderShortPanel">Pages</div>
					
					<table width="100%" class="sortable" cellspacing="0" cellpadding="1" >
						<tr class="listTableHead" >
						 	<td width="1%" align ="left" class="sorttable_nosort">&nbsp;</td>
						 	<td width="5%" align ="left">ID&nbsp;</td>
							<td align="left"   nowrap>Area</td>
						 	<td align="left"   nowrap>Page Description</td>
						 	<td align="center" nowrap>Available?</td>
							<td align="left"   nowrap>Page URL</td>
						 	<td align="right" class="sorttable_nosort">&nbsp;&nbsp;</td>
						</tr>
						<%
							Collection data = (Collection)request.getAttribute(Constants.RESULTS);
							if (data != null && data.size() > 0) {
								Iterator iterator = data.iterator();	
								PageBO bo = null;
								int row = 0;
							  	while (iterator.hasNext()) {
							  		row++;
									bo = (PageBO) iterator.next();
						%>
								  	<tr class="listTableVal" onclick="highlightTR(this);">
									    <td align="left"></td>
									    <td align="left"><a href="#" class="link" onclick="openPopup('<%= cryptor.urlEncrypt(editPagePopupUrl + bo.getPageId()) %>', 'editPage', <%= windowHeight %>, <%= windowWidth %>);"><%= TextUtil.print(bo.getPageId()) %></a>&nbsp;</td>
										<td align="left"><%= TextUtil.print(bo.getAttribute("areadescription")) %></td>
										<td align="left"><%= TextUtil.print(bo.getDescription()) %></td>
										<td align="center">
											<%= TextUtil.print(bo.getIsAvailable()) %>
											<input type="hidden" id="pageid" name="pageid" value="<%= TextUtil.print(bo.getPageId()) %>">
										</td>
										<td align="left" colspan="2"><%= TextUtil.print(bo.getPageUrl()) %></td>
									</tr>  
						<%
						    	}
						    	bo = null;
						    	iterator = null;
						    	  
						    } else {	
								String message = (String) request.getAttribute("tooMany");
								if (!TextUtil.isEmpty(message)) {
						%>
							    	<tr>
										<td class="listTable" colspan="8" align="center"><br><br><b><%= message %></b></td>
									</tr> 
						<%      }
						    }
					    	data = null; 
						%>
					</table>
				</div>
				
			<% } else if (PageServlet.SEARCH_BY_ROLE.equals(searchType)) { %>
			
				<div id="searchByRoleDiv">
					<table style="width: 100%" class="fixedSection">
					  	<tr> 
					  		<td height="30px" width="1%"></td>
					    	<td width="98%" class="appFieldTitle" style="font-weight: normal" align="left" colspan="2">
					    		Role&nbsp;
					    		<select class="appField" name="roleId" id="roleId" tabindex="11" style="width: 300px"><%= availableRoles %></select>
					    	</td>
							<td nowrap align="right">
								<input type="submit" name="btFind" value="<%= findButtonCaption %>" class="appButtonBold" tabindex="6">&nbsp;
				  				<% if (permission.hasInsert() && !TextUtil.isEmpty(addButtonCaption)) { %>
				      				<input type="button" name="btAdd" value="<%= addButtonCaption %>" class="appButtonBold" onclick="validateAddPage();" tabindex="7">&nbsp;  
				  				<% } %>
			      				<% if (PageServlet.SEARCH_BY_PAGE.equals(searchType) && permission.hasUpdate()) { %>
				      				<!-- <input type="button" name="btSave" value="Save" class="appButtonBold" onclick="saveDisplayOrders();" tabindex="8"> -->  
			      				<% } %>
			      				&nbsp;
							</td>
						</tr>
					</table>
					<div class="appHeaderShortPanel">Pages</div>
					
					<table width="100%" class="sortable" cellspacing="0" cellpadding="1" >
						<tr class="listTableHead" >
						 	<td width="1%" align ="left" class="sorttable_nosort">&nbsp;</td>
						 	<td width="5%" align ="left">ID&nbsp;</td>
							<td align="left" nowrap> Page Description </td>
							<td align="center"> Available? </td>
							<td align="left" nowrap> Page URL </td>
						 	<td align="left"> Role </td>
							<td align="center" nowrap> Available for Role? </td>
							<td align="left" nowrap> Access Level </td>
							<td align="right" class="sorttable_nosort">&nbsp;&nbsp;</td>
						</tr>
						<%
							Collection data = (Collection)request.getAttribute(Constants.RESULTS);
							if (data != null && data.size() > 0) {
								Iterator iterator = data.iterator();	
								PageBO bo = null;
								int row = 0;
							  	while (iterator.hasNext()) {
							  		row++;
									bo = (PageBO) iterator.next();
						%>
									<tr class="listTableVal" onclick="highlightTR(this);">
									    <td width="1%" align="left"></td>
									    <td width="5%" align="left"><a href="#" class="link" onclick="openPopup('<%= editPagePopupUrl + bo.getPageId() %>', 'editPage', <%= windowHeight %>, <%= windowWidth %>);" ><%= TextUtil.print(bo.getPageId()) %></a>&nbsp;</td>
										<td align="left"><%= TextUtil.print(bo.getDescription()) %></td>
										<td align="center"><%= TextUtil.print(bo.getIsAvailable()) %> </td>
										<td align="left"> <%= TextUtil.print(bo.getPageUrl()) %> </td>
										<td align="left"> <%= TextUtil.print(bo.getAttribute("roledescription")) %> </td>
										<td align="center"> <%= TextUtil.print(bo.getAttribute("pageisavailableforrole")) %> </td>
										<td align="left" colspan="2"> <%= TextUtil.print(bo.getAttribute("accessleveldescription")) %> </td>
									</tr>  
						<%
						    	}
						    	bo = null;
						    	iterator = null;
						    	  
						    } else {	
								String message = (String) request.getAttribute("tooMany");
								if (message != null && message.length() > 0) {
						%>
							    	<tr>
										<td class="listTable" colspan="11" align="center"><br><br><b><%= message %></b></td>
									</tr> 
						<%      }
						    } 
					    	data = null;
						%>
					</table>
				</div>
				
			<% } else if (PageServlet.SEARCH_BY_AREA.equals(searchType)) { %>
				
				<div id="searchByAreaDiv">
					<table style="width: 100%" class="fixedSection">
					  	<tr> 
					  		<td height="30px" width="1%"></td>
					    	<td width="98%" class="appFieldTitle" style="font-weight: normal" align="left" colspan="2">
					    		Area&nbsp;
					    		<select class="appField" name="areaId" id="areaId" tabindex="12"><%= availableAreas %></select>
					    	</td>
					  		<td nowrap align="right">
								<input type="submit" name="btFind" value="<%= findButtonCaption %>" class="appButtonBold" tabindex="6">&nbsp;
				  				<% if (permission.hasInsert() && !TextUtil.isEmpty(addButtonCaption)) { %>
				      				<input type="button" name="btAdd" value="<%= addButtonCaption %>" class="appButtonBold" onclick="validateAddPage();" tabindex="7">&nbsp;  
				  				<% } %>
			      				<% if (PageServlet.SEARCH_BY_PAGE.equals(searchType) && permission.hasUpdate()) { %>
				      				<!-- <input type="button" name="btSave" value="Save" class="appButtonBold" onclick="saveDisplayOrders();" tabindex="8"> -->  
			      				<% } %>
			      				&nbsp;
							</td>
						</tr>
					</table>
					<div class="appHeaderShortPanel">Pages</div>
					
					<table width="100%" class="sortable" cellspacing="0" cellpadding="1" >
						<tr class="listTableHead" >
						 	<td width="1%" align ="left" class="sorttable_nosort">&nbsp;</td>
						 	<td width="5%" align ="left">ID&nbsp;</td>
							<td align="left" nowrap> Page Description </td>
							<td align="center" nowrap> Available? </td>
							<td align="left" nowrap> Page URL </td>
						 	<td align="left"> Area </td>
							<!-- <td align="center" nowrap> Display Order </td> -->
							<td align="left"> Menu </td>
							<td align="right" class="sorttable_nosort">&nbsp;&nbsp;</td>
						</tr>
						<%
							Collection data = (Collection)request.getAttribute(Constants.RESULTS);
							if (data != null && data.size() > 0) {
								Iterator iterator = data.iterator();	
								PageBO bo = null;
								int row = 0;
							  	while (iterator.hasNext()) {
							  		row++;
									bo = (PageBO) iterator.next();
						%>
								  	<tr class="listTableVal" onclick="highlightTR(this);">
									    <td width="1%" align="left"></td>
									    <td width="5%" align="left"><a href="#" class="link" onclick="openPopup('<%= editPagePopupUrl + bo.getPageId() %>', 'editPage', <%= windowHeight %>, <%= windowWidth %>);" ><%= TextUtil.print(bo.getPageId()) %></a>&nbsp;</td>
										<td align="left"><%= TextUtil.print(bo.getDescription()) %></td>
										<td align="center"> <%=TextUtil.print(bo.getIsAvailable())%> </td>
										<td align="left"> <%= TextUtil.print(bo.getPageUrl()) %> </td>
										<td align="left"> <%= TextUtil.print(bo.getAttribute("areadescription")) %> </td>
										<!-- <td align="center"> <%= TextUtil.print(bo.getAttribute("displayorder")) %> </td> -->
										<td align="left" colspan="2"> <%= TextUtil.print(bo.getAttribute("parentmenu")) %> </td>
									</tr>  
						<%
						    	}
						    	bo = null;
						    	iterator = null;
						    	  
						    } else {	
								String message = (String) request.getAttribute("tooMany");
								if (message != null && message.length() > 0) {
						%>
							    	<tr>
										<td class="listTable" colspan="10" align="center"><br><br><b><%= message %></b></td>
									</tr> 
						<%      }
						    } 
					    	data = null;
						%>
					</table>
				</div>
					
			<% } %>
			<br><br><br>
		</form>
	</body>
<%
	params = null;
	cryptor = null;
%>
</html>
