<!DOCTYPE HTML>
<%@page import="com.itsy.constants.Constants" %>
<%@page import="com.itsy.util.MenuUtil" %>
<%@page import="com.itsy.session.PageVariables" %>
<%@page import="com.itsy.session.SessionVariables" %>
<%
	String userfullname = (String) request.getSession().getAttribute(SessionVariables.FULL_NAME);
	MenuUtil menu = new MenuUtil(request.getSession());
%>
<html>
	<head>
		<title>HeaderMenu</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<meta name="GENERATOR" content="Rational Application Developer">
		<link rel="stylesheet" href="/itsy/css/standard.css" type="text/css">
		<style type="text/css">
			input {
				background: #eee;
				border-width: 2px 1px;
				margin: -1px -1px; 
				padding: 0;
				color: black;
				height: 22px;
				width: 55px;
				font-family: "Segoe UI", Tahoma, Arial, Verdana, Sans-serif;
				font-size: 11px;
				text-decoration: none;
			}
		</style>
		<script src="/itsy/javascript/AnchorPosition.js?nocache=<%= Constants.SYSTEM_VERSION %>" type="text/javascript"></script>
		<script type="text/javascript">
			var coordinates;
			var menuArray = new Array();
			var menuList = new Array(0);
			
			function setMain(elm, content) {
				elm.blur();
				var btn = elm.name.replace(/[^0-9]/g,'');
				coordinates = getAnchorPosition("A" + btn);
				if (content == null) {
					content = buildMenu(menuArray[btn]);
					if (content.length > 2) window.parent.setInnerHTML(coordinates.x, content);
				} else {
					eval(content);
				}
			}
			
			function buildMenu(array) {
				var arrayStr = "";
				var isList = true;
				for (var inx = 0; inx < array.length; inx++) {
					var menuElem = array[inx].split("|");
					// handles BUTTON functionality
					if (menuElem.length == 1 && inx == 0) {
						osList = false;
						window.top.goMenu(menuElem[0]);
						arrayStr = "";
						return false;
					}
					arrayStr += addMenuItem(menuElem);
				}
				// close any exiting item in menuList
				while (menuList.length > 0) {
					menuList.pop();
					arrayStr += "</ul></li>\n";
				}
				if (isList) {
					arrayStr = '<ul id="suckermenu">' + arrayStr + '</ul>';
				}
				
				return arrayStr;
			}
			
			function addMenuItem(subArray) {
				var retVal = "";
				var menuStr = "";
				var reqMenuDepth = subArray.length - 2;
				var processingSubMenus = true;
				var target = "";
				HERE : 
				while (processingSubMenus) {
					if (reqMenuDepth > 0) {
						while (menuList.length > reqMenuDepth) {
							menuList.pop();
							retVal += "</ul></li>\n";
						}
						// sub menu needed (we may already have it)
						for (inx = 0; inx < reqMenuDepth; inx++) {
							menuStr = subArray.shift();
							if (menuList.length > inx) {
								if (menuList[inx] != menuStr) {
									// If sub menu has changed pop off previous
									menuList.pop();
									subArray.unshift(menuStr);
									retVal += "</ul></li>\n";
									for (t=inx-1;t>=0;t--)
										subArray.unshift(menuList[t]);
									continue HERE;
								}
							} else {
								// If we define a new sub menu - add it
								menuList.push(menuStr);
								retVal += "<li><a href='#'>" + menuStr + "</a>\n<ul>\n";
							}
						}
					} else if (menuList.length > 0) {
						menuList.pop();
						retVal += "</ul></li>\n";
						continue HERE;
					}
					processingSubMenus = false;
				}
				if (subArray[0].charAt(0) == "s") {
					retVal += "<li><hr></li>\n";
				} else {
					retVal += "<li><a style=\"min-width: 30px\" href=\"javascript: void window.top.goMenu('"
						   + subArray[1] + "');\" onmouseover=\"window.status='"
						   + subArray[0] + "'; return true;\" onmouseout=\"window.status='Done';\">"
						   + subArray[0] + "</a></li>\n";
				}
				return retVal;
			}
			
			function hasMenu(label) {
				var has = false;
				for (i=0; i < menuList.length; i++) {
					if (label == menuList[i]) {
						has = true;
						break;
					}
				}
				return has;
			}
			
			<%= menu.getJavascriptVariables() %>
		</script>
	</head>
	<body style="margin-top: 0; margin-left: 0; margin: 0; background-color: #000000"
		topmargin="0" leftmargin="0" marginwidth="0" marginheight="0"
		text="#000099" link="#000099" vlink="#000099" alink="#000099"
		bgcolor="#000000">
		<table id="mnuTable" width="100%">
			<tr>
				<td nowrap style="font-family: 'Segoe UI', Tahoma, Arial, Verdana, Sans-serif; font-size: 14px; font-weight: normal; color: #ffffff;"><b>&nbsp;Welcome: <%= userfullname %></b></td>
				<td nowrap style="font-family: 'Segoe UI', Tahoma, Arial, Verdana, Sans-serif; font-size: 14px; font-weight: normal; color: #ffffff; text-align: right;"></td>
			</tr>
		</table>
		<table style="background-color: #dde0e4; border-color: #abb4bc;" width="100%">
			<tr>
				<%= menu.getSystemAreasHtml() %>
			</tr>
		</table>
	</body>
</html>
<%
	menu = null;
%>