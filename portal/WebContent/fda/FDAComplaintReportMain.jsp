<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>

<%@page import="java.net.URL"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.net.URLEncoder"%>

<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.IOException"%>

<%

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

%>

<html>


<head>


			<meta charset="utf-8">
			<meta name="viewport" content="width=device-width, initial-scale=1">
			<link rel="stylesheet"  href="../lib/jqm130/jquery.mobile-1.3.0.min.css">
			 <link href="../lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css" rel="stylesheet">
			
			<script src="../lib/jquery-1.9.1.min.js"></script>
			<script src="../lib/jqm130/jquery.mobile-1.3.0.min.js"></script>
			<script src="../lib/jquery-validation/jquery.validate.js"></script>
			<script src="../lib/jqueryui1102/js/jquery-ui-1.10.2.custom.min.js"></script>
			<script src="../lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js"></script>
			
			<title>PBC Enterprise Portal</title>
			<script src="../js/home.js"></script>
			<link rel="stylesheet"  href="../css/home.css">
		
<script>
	function Submit(){
		$('#FDAComplaintReportMainExecute').submit();
	}
</script>
		
</head>

<body>

<div data-role="page" id="FDAComplaintReportMainPage" data-url="FDAComplaintReportMainPage" data-theme="d">

	<jsp:include page="Header.jsp" >
    	<jsp:param value="Complaint Report" name="title"/>
    </jsp:include>
    <div data-role="content" data-theme="d">
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:100%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
	    		<!-- <table style="width: 100%">
	    			<tr>
	    				<td align="left">
	    					<h1>One Window Cell</h1>
	    				</td>
	    				<td align="right">
	    					<img src="fda.jpg" astyle="width: 65px">
	    				</td>
	    			</tr>
	    		</table>
	    		 -->
	    		
				<form name="FDAComplaintReportMainExecute" id="FDAComplaintReportMainExecute" action="FDAComplaintReport.jsp" method="post" data-ajax="false" >
				
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    <!-- <li data-role="list-divider">Order Information</li> -->
				    <li>
					    <table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="FromDate" data-mini="true">From Date</label>
									<input type="text" name="FromDate" id="FromDate" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>" data-mini="true" placeholder="DD/MM/YYYY" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ToDate" data-mini="true">To Date</label>
									<input type="text" name="ToDate" id="ToDate" value="<%=Utilities.getDisplayDateNext(new java.util.Date())%>" data-mini="true" placeholder="DD/MM/YYYY" >
								</td>
								
							</tr>
						</table>
					</li>
				</ul>
				</form>
	    	</div>
	    </div>
	    
	</div><!-- /grid-a -->
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<table style="width: 100%">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="Submit();" >Generate</a>
                    <button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='FDAComplaintReportMain.jsp'" >Reset</button>
				</td>
				<td align="right">
					<a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DeliveryNoteSearch" >Search</a>
				</td>
			</tr>
		</table>
    </div>

<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="DeliveryNoteFormDateRange" onSubmit="return showSearchContent()">
            <table>
            	<tr>
                	<td>
                		<span id="FromDateSpan"><input type="text" data-mini="true" name="FromDate" id="FromDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>"></span>
                    </td>
                    <td>
                    
						<span id="ToDateSpan"><input type="text" data-mini="true" name="ToDate" id="ToDate" placeholder="DD/MM/YYYY" value="<%=Utilities.getDisplayDateFormat(new java.util.Date())%>" ></span>
                    
                    </td>
                    <td>
                    	<button data-role="button" data-icon="search" id="SearchButton" data-iconpos="notext" data-theme="c" data-inline="true" data-corners="false" onClick="showSearchContent();"></button>
                    </td>
                </tr>
            </table>
        </form>

        <div id="SearchContent">
        </div>
            
        </div>
    </div>

</div>

</body>
</html>

<%
s.close();
c.close();
%>

<%!
public static void sendSMS2(String Number, String Message){
	URL url;
	try {
		url = new URL("http://155.135.0.70/default.aspx?number=92"+Number+"&msg="+URLEncoder.encode(Message));
		
		URLConnection urlConnection = url.openConnection();

		HttpURLConnection connection = null;
		if(urlConnection instanceof HttpURLConnection){
		   connection = (HttpURLConnection) urlConnection;
		}else{
		   System.out.println("Utilities.sendSMS: Invalid URL");
		}

		BufferedReader in = new BufferedReader(
		new InputStreamReader(connection.getInputStream()));
		String urlString = "";
		String current;
		while((current = in.readLine()) != null)
		{
		   urlString += current;
		}
	
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
%>