<html>
    <head>
		<title>Log Out</title>
		<link rel="stylesheet" href="/itsy/css/standard.css" type="text/css">
	</head>
	<body onload="document.forwardToLoginPage.submit()">
	  <div id="loginContainer">
		<form name="forwardToLoginPage" action="/itsy/index.jsp">
	    	<br><br><br><br><br><br><br><br><br>
			<table width="400px" align="center">
				<tr>
					<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
					<td>
						<div class="appHeaderShortPanel" width="400px">Session Timeout</div>		
						<table class="variableSection border-grey" width="400px">
							<tr><td height="20">&nbsp;</td></tr>
							<tr>
								<td align="center">Session has expired. Please close all popups, logout and re-login again.</td>
							</tr>
							<tr><td height="20">&nbsp;</td></tr>
						</table>
					</td>
				</tr>
			</table>
	  	</form>
	  </div>
	</body>
</html>