<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet"  href="lib/jqm130/jquery.mobile-1.3.0.min.css">
	<script src="lib/jquery-1.9.1.min.js"></script>
	<script src="lib/jqm130/jquery.mobile-1.3.0.min.js"></script>
	<title>PBC Enterprise Portal</title>
	<script>


//if (location.protocol != 'https:'){
	//location = 'https:' + location.href.substring(window.location.protocol.length);
//}
	$(document).ready(function (){

		$("#logindiv").css("height",($(document).height()-120));
		$(".plogo").css("padding-top",(($(document).height()-120)/2)-280);
		$("#loginpane").css("padding-top",20);
		
	});
	</script>
	<style>
		#logindiv{
		  width: 60% ;
		  max-width:500px;
		  margin-left: auto ;
		  margin-right: auto ;
		}
	</style>
<%
if (request.getParameter("signout") != null){
	session.invalidate();
}



%>
</head>
<body>

<div data-role="page" id="panel-fixed-page1" data-url="panel-fixed-page1">

    <div data-role="header" data-theme="b" data-position="fixed">
        <h1>Al Moiz Industries</h1>
    </div><!-- /header -->
    <div data-role="content" data-theme="d">
         
	    <div id="logindiv">
	    	<br>
	    	<br/>
	    	<br/>
	    	<br/>
	    	
	       <img src="images/MoizFoodsLogo.jpg" style="width: 140px;" class="plogo">
			<div data-role="collapsible" data-theme="a" data-content-theme="c" id="loginpane" data-collapsed="false" data-expanded-icon="grid">
			  <h3>Please sign in</h3>
				<form name="login" method="POST" data-ajax="false" action="AuthenticateUser" onSubmit="return validate();">
					<div data-role="fieldcontain" >	
				         <input type="text" name="userid" id="userid" data-theme="d" value="" placeholder="User ID" />
			         </div>
			         <div data-role="fieldcontain">
				         <input type="password" name="password" id="password"  data-theme="d"  value="" placeholder="Password" />
			    	</div>
				<button id="SubmitButton" type="submit" data-theme="c" name="submit" value="submit-value" data-inline="true" data-icon="check">Login</button>
				<%if (request.getParameter("invalid") != null){%><label>Invalid User ID or Password!</label><%}%>
				<input type="hidden" id="iBrowserID" name="iBrowserID">
				<input type="hidden" id="iLat" name="iLat">
				<input type="hidden" id="iLng" name="iLng">
				
				</form>
			</div><br><br><label id="iLabel"></label>
			<div>
			<table>
			<tr>


			<td>
				<!-- <img src="images/icon128.png" height="45"> -->
			</td>
			<td>
				<a style="font-size: 11px;" href="https://chrome.google.com/webstore/detail/punjab-beverages/lnmakdnabninnfidaolgegllpmmpmekg" target="_blank">Chrome Extension</a><br>
				<span style="font-size: 11px">Click above to install portal activation extension to access full features.</span>
			</td>
				</tr>
			</table>
			</div>
		</div>
    </div><!-- /content -->
    <div data-role="footer" data-position="fixed" data-theme="b">
        <h4>&copy; Punjab Beverages Co. (Pvt.) Ltd</h4>
    </div><!-- /footer -->
</div>

</body>
</html>