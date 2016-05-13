<!DOCTYPE HTML>
<%@page import="com.itsy.constants.Constants" %>
<%@page import="com.itsy.session.PageVariables" %>
<%@page import="com.itsy.util.TextUtil" %>
<html>
	<head>
		<title>Access Denied</title><meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" href="/itsy/css/standard1.css" type="text/css">
		<script language="javascript" src="/itsy/javascript/standard.js?nocache=<%= Constants.SYSTEM_VERSION %>"></script>
		<meta name="GENERATOR" content="Rational Application Developer">
	</head>
	<% 
		String message = (String)request.getAttribute("ErrorMessage");
		
		PageVariables pageVariables = (PageVariables) request.getAttribute(Constants.PAGE_VARIABLES);
		String uniqueWindowId = pageVariables != null ? pageVariables.getVariable(Constants.UNIQUE_WINDOW_ID) : "";
	%>
	<body>
		<br/><br/><br/>
	
	<table width="70%" align="center">
		<thead>
			<tr>
				<td class="appHeaderShortPanel">Access Denied</td>
			</tr>
		</thead>
	</table>
	<table class="variableSection border-grey" width="70%" align="center">
		<tr><td>
			<table width="100%">
				<tr height="10"><td colspan="2"></td></tr>
				<tr height="10"><td colspan="2">&nbsp;&nbsp;&nbsp;You do not have access to the requested area.</td></tr>
				<tr height="10"><td colspan="2"></td></tr>
				<% if (!TextUtil.isEmpty(message)) { %>
					<tr height="10"><td colspan="2">&nbsp;&nbsp;&nbsp;Reason - <%= message %></td></tr>
				<% } %>
				<tr height="10"><td colspan="2"></td></tr>
			</table>
		</td></tr>
	</table>
	
    <table width="100%" class="table-bottom">
		<tr class="fixedSection">
			<td  nowrap align="right">
				<input	type="button" value="Close" id="btCancel" class="appButtonBold" onclick="closeAndRefreshParentPopin('<%= uniqueWindowId %>'); window.close();">
				&nbsp;
			</td>
		</tr>
	</table>	
	</body>
</html>



