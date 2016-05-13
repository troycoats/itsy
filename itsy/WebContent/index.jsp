<!DOCTYPE html>
<%@page import="com.itsy.session.URLEncryption" %>
<%@page import="com.itsy.servlets.LoginServlet" %>
<%@page import="com.itsy.util.TextUtil" %>
<%
	String errorMessage = (String)request.getAttribute("ErrorMessage");
	if (errorMessage == null)
		errorMessage = (String)request.getParameter("ErrorMessage");

	String username = URLEncryption.getParamAsString(request, "username");
	
	URLEncryption encryptor = new URLEncryption("PasswordServlet");
	String forgetURL = encryptor.urlEncrypt("?mode=forgot");
	String expiredURL = encryptor.urlEncrypt("?mode=expired");
	
	String message = "";
	boolean	available = true;
%>
<html>
	<head>
		<script src="/itsy/javascript/common.js"></script>
		<script type="text/javascript">
			if (parent.frames.length!=0) top.location.href = '/itsy/index.jsp';
		</script>
		<script type="text/javascript">
			function browserCheck() {
				var browser = get_browser_info();
				if (browser.name === 'MSIE' && browser.version < 7) {
					var d = document.createElement("div");
					d.setAttribute("id", "noscript_msg");
					d.appendChild(document.createTextNode("Your browser is not supported. Please use Chrome, Firefox, Safari or Internet Explorer 7 and above."));
					document.getElementById('loginForm').appendChild(d);
					document.activeElement.blur();
				}
			}
			function get_browser_info() {
				var ua = navigator.userAgent, tem, M = ua.match(/(opera|chrome|safari|firefox|msie|trident|edge(?=\/))\/?\s*(\d+)/i) || [];
				if (/trident/i.test(M[1])) {
					tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
					return {name:'MSIE', version:(tem[1]||'')};
				}
				if (M[1] === 'Chrome') {
					tem = ua.match(/\b(OPR|Edge)\/(\d+)/i);
					if (tem != null) return {name : tem[1] === 'OPR' ? 'Opera' : tem[1], version : tem[2]};
				}
				M = M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
				if ((tem = ua.match(/version\/(\d+)/i)) != null) {M.splice(1,1,tem[1]);}
				return {
					name: M[0],
					version: M[1]
				};
			}
		</script>
		<script>
			function capLock(e){
 				kc = e.keyCode?e.keyCode:e.which;
 				sk = e.shiftKey?e.shiftKey:((kc == 16)?true:false);
 				if(((kc >= 65 && kc <= 90) && !sk)||((kc >= 97 && kc <= 122) && sk))
  					document.getElementById('divCaps').style.visibility = 'visible';
 				else
  					document.getElementById('divCaps').style.visibility = 'hidden';
			}
		</script>
		<link rel="stylesheet" href="/itsy/css/standard.css" type="text/css">
		<title>Itsy Login</title>
	</head>
	<body onload="browserCheck();">
		<div id="loginContainer">
			<div id="loginCard">
				<div id="loginHeading">Itsy</div>
				<div id="courtMontage">
					<img class="courtMontageCrop" src="/itsy/images/login.jpg" width="350px" height="70px"><img class="courtLogo" src="/itsy/images/logo.gif" width="156px" height="70px">
				</div>
				<% if (!available) { %>
					<div id="login_msg" style="<%= TextUtil.isEmpty(message) ? "display: none;" : "" %>"><br><%= message %><br><br></div>
				<% } else { %>
					<div id="loginSubHeading">Itsy Login</div>
						<form id="loginForm" name="login" action="login" method="post">
							<noscript>
								<div id="noscript_msg">You need to have JavaScript enabled to use this system.</div>
							</noscript>
							<div id="login_msg" style="<%= TextUtil.isEmpty(message) ? "display: none;" : "" %>"><%= message %><br><br></div>
							<table width="100%">
								<tr>
									<td width="38%" align="right">Username&nbsp;&nbsp;</td>
									<td width="62%" align="left"><input class="appField" type="text" name="username" id="username" value="<%= TextUtil.print((String) username) %>" width="14" tabindex="1" /></td>
								</tr>
								<tr>
									<td align="right">Password&nbsp;&nbsp;</td>
									<td align="left"><input class="appField" type="password" name="password" value="" onkeypress="capLock(event)" tabindex="2" /></td>
								</tr>
								<tr><td colspan="2">&nbsp;</td></tr>
								<tr>
									<td colspan="2" align="center"><font color="red"><b>Username and password are case sensitive</b></font></td>
								</tr>
								<tr>
									<td colspan="2" align="center"><div id="caps_msg" style="visibility:hidden">Caps Lock is on.</div></td>
								</tr>
							</table>
							<table width="100%">	
								<tr>
									<td nowrap>
										<a href="#" onclick="openPopup('<%= forgetURL %>', 'Password', 300, 500);">Forgot Password?</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<a href="#" onclick="openPopup('<%= expiredURL %>', 'Password', 300, 500);">Password Expired?</a>
									</td>
									<td align="right"><input type="submit" class="darkButtonBig" name="submit" value="Login" tabindex="3" /></td>
								</tr>
							</table>
							<% if (errorMessage != null) { %>
								<h5 align="center" style="font-size: 12px;"><font color="red"><%= errorMessage %></font></h5>
							<% } %>
						</form>
					</div>
			<% } %>
		</div>
	</body>
	<script type="text/javascript">
		document.getElementById('username').focus();
	</script>
</html>