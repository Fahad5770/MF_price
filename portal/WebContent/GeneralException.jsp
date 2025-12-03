<%@ page language="java" isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet"  href="<%=request.getContextPath()%>/lib/jqm130/jquery.mobile-1.3.0.min.css">
	<script src="<%=request.getContextPath()%>/lib/jquery-1.9.1.min.js"></script>
	<script src="<%=request.getContextPath()%>/lib/jqm130/jquery.mobile-1.3.0.min.js"></script>
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
        <h1>Exception Report</h1>
    </div><!-- /header -->
    
    <div data-role="content" data-theme="d">
         
	    <div id="logindiv">
	    	<img src="<%=request.getContextPath()%>/images/logofull.svg" style="width: 120px;" class="plogo">
			
			<div data-role="collapsible" data-theme="d" data-content-theme="d" id="loginpane" data-collapsed="false" data-expanded-icon="alert">
			  <h3>Exception</h3>
				<form name="login" method="POST" data-ajax="false" action="home.jsp" onSubmit="return validate();">
					<label>Sorry, an unexpected error has occurred.<br>Please contact system administrator if you need assistance.</label><br><br>
					
					<a href="#popupCloseRight" data-rel="popup" data-role="button" data-inline="true" data-mini="true" data-position-to="window">View Detail</a>
					
				</form>
			</div>
		</div>

		<div data-role="popup" id="popupCloseRight" class="ui-content" style="max-width:400px">
		    <a href="#" data-rel="back" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a>
		    <p><%
			    boolean handled = false;
			    
			    if(pageContext != null) {
			    
			        ErrorData ed = null;
			        try {
			            ed = pageContext.getErrorData();
			        } catch(NullPointerException ne) {
			        }
			
			        if(ed != null) {
			            out.println("ErrorCode: " + ed.getStatusCode());
			            out.println("<br />URL: " + ed.getRequestURI());
			            out.println("<br />Cause:<br>" + ed.getThrowable());
			            out.println("<br />Source: " + ed.getServletName());
			            handled = true;
			        }
			    }
			    if(!handled) {
			    %>
			        <p />Error information unavailable.
			    <%
			    }
			%>		    
		    </p>
		</div>		

    </div><!-- /content -->
    <div data-role="footer" data-position="fixed" data-theme="c">
        <h4><font size="2">&copy; Punjab Beverages Co. (Pvt.) Ltd</font></h4>
    </div><!-- /footer -->
</div>

</body>
</html>