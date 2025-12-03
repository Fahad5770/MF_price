<%@ page language="java"%>
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
</head>
<body>

<div data-role="page" id="panel-fixed-page1" data-url="panel-fixed-page1">

    <div data-role="header" data-theme="c" data-position="fixed">
        <h1>Session Expired</h1>
    </div><!-- /header -->
    
    <div data-role="content" data-theme="d">
         
	    <div id="logindiv">
	    	<img src="images/logofull.svg" style="width: 120px;" class="plogo">
			
			<div data-role="collapsible" data-theme="d" data-content-theme="d" id="loginpane" data-collapsed="false" data-expanded-icon="alert">
			  <h3>Session Expired</h3>
				<form name="login" method="POST" data-ajax="false" action="home.jsp" onSubmit="return validate();">
					<label>Sorry, the session has been expired.<br>Please click below to login again.</label><br><br>
					
					<a href="<%=request.getContextPath()%>/index.jsp" data-rel="popup" data-ajax="false" data-role="button" data-inline="true" data-mini="false" data-icon="check">Login</a>
					
				</form>
			</div>
		</div>

    </div><!-- /content -->
    <div data-role="footer" data-position="fixed" data-theme="c">
        <h4><font size="2">&copy; Punjab Beverages Co. (Pvt.) Ltd</font></h4>
    </div><!-- /footer -->
</div>

</body>
</html>