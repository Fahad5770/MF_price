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

long UpdateID = Utilities.parseLong(request.getParameter("UpdateID"));
long CaseNo = Utilities.parseLong(request.getParameter("CaseNo"));
if(CaseNo > 0 && UpdateID == 0){
	
	String FirstName = Utilities.filterString(request.getParameter("FirstName"), 1, 100);
	String LastName = Utilities.filterString(request.getParameter("LastName"), 1, 100);
	String ComplaintType = Utilities.filterString(request.getParameter("ComplaintType"), 1, 100);
	String Address = Utilities.filterString(request.getParameter("Address"), 1, 100);
	String City = Utilities.filterString(request.getParameter("City"), 1, 100);
	String ContactNo = Utilities.filterString(request.getParameter("ContactNo"), 1, 100);
	String ConcernedDirectorate = Utilities.filterString(request.getParameter("ConcernedDirectorate"), 1, 100);
	Date DateOfEntry = Utilities.parseDateYYYYMMDD(request.getParameter("DateOfEntry"));
	String Remarks = Utilities.filterString(request.getParameter("Remarks"), 1, 100);
	
	int ProcessingTime = 0;
	String ComplaintTypeLabel = "";
	ResultSet rs = s.executeQuery("select processing_time, label from fda_complaints_types where id="+ComplaintType);
	if(rs.first()){
		ProcessingTime = rs.getInt(1);
		ComplaintTypeLabel = rs.getString(2);
	}
	Date ExpectedDate = Utilities.getDateByDays(ProcessingTime);
	
	
	String Message = "Dear "+FirstName+" "+LastName+",\n";
	Message += "Your case# "+CaseNo+" of '"+ComplaintTypeLabel+"' has been registered. Expected date of completion : "+Utilities.getDisplayDateFormat(ExpectedDate);
	
	sendSMS2(ContactNo, Message);
	
	String sql = "INSERT INTO `fda_complaints`(`first_name`,`last_name`,`type_id`,`address`,`city`,`contact_no`,`concerned_directorrate`,`date_of_entry`,`remarks`,`created_on`,`created_by`, `status_id`, `expected_date`)VALUES('"+FirstName+"', '"+LastName+"', '"+ComplaintType+"', '"+Address+"', '"+City+"', '"+ContactNo+"', '"+ConcernedDirectorate+"', "+Utilities.getSQLDate(DateOfEntry)+", '"+Remarks+"', now(), 1, 1, "+Utilities.getSQLDate(ExpectedDate)+")";
	//System.out.println(sql);
	s.executeUpdate(sql);
	
}

long FormCaseNo = 0;
ResultSet rs = s.executeQuery("select max(case_no) from fda_complaints");
if(rs.first()){
	FormCaseNo = rs.getLong(1)+1;
}

String Update = "0";
String FirstName = "";
String LastName = "";
String ComplaintType = "0";
String Address = "";
String City = "";
String ContactNo = "";
String ConcernedDirectorate = "0";
String DateOfEntry = Utilities.getSQLDate(new Date()).replace("'", "").replace("/", "-");
String Remarks = "";

long EditID = Utilities.parseLong(request.getParameter("EditID"));
if(EditID > 0){
	ResultSet rs2 = s.executeQuery("select * from fda_complaints where case_no="+EditID);
	if(rs2.first()){
		Update = EditID+"";
		FirstName = rs2.getString("first_name");
		LastName = rs2.getString("last_name");
		ComplaintType = rs2.getString("type_id");
		Address = rs2.getString("address");
		City = rs2.getString("city");
		ContactNo = rs2.getString("contact_no");
		ConcernedDirectorate = rs2.getString("concerned_directorrate");
		DateOfEntry = rs2.getString("date_of_entry");
		Remarks = rs2.getString("remarks");
	}
}


if(UpdateID > 0){
	FirstName = Utilities.filterString(request.getParameter("FirstName"), 1, 100);
	LastName = Utilities.filterString(request.getParameter("LastName"), 1, 100);
	ComplaintType = Utilities.filterString(request.getParameter("ComplaintType"), 1, 100);
	Address = Utilities.filterString(request.getParameter("Address"), 1, 100);
	City = Utilities.filterString(request.getParameter("City"), 1, 100);
	ContactNo = Utilities.filterString(request.getParameter("ContactNo"), 1, 100);
	ConcernedDirectorate = Utilities.filterString(request.getParameter("ConcernedDirectorate"), 1, 100);
	DateOfEntry = request.getParameter("DateOfEntry");
	Remarks = Utilities.filterString(request.getParameter("Remarks"), 1, 100);
	
	s.executeUpdate("UPDATE `fda_complaints` SET `first_name` = '"+FirstName+"',`last_name` = '"+LastName+"',`type_id` = '"+ComplaintType+"',`address` = '"+Address+"',`city` = '"+City+"',`contact_no` = '"+ContactNo+"',`concerned_directorrate` = '"+ConcernedDirectorate+"',`date_of_entry` = '"+DateOfEntry+"',`remarks` = '"+Remarks+"' WHERE `case_no` = "+UpdateID);
	response.sendRedirect("index.jsp");
}

String ComplaintTypeOptions = "<option value=''>Select</option>";
ResultSet rs3 = s.executeQuery("SELECT id, label FROM fda_complaints_types");
while(rs3.next()){
	ComplaintTypeOptions += "<option value='"+rs3.getString("id")+"'>"+rs3.getString("label")+"</option>";
}

out.println(DateOfEntry);

%>

<html>


<head>

<script>
var ComplaintType = '<%=ComplaintType%>';
var ConcernedDirectorate = '<%=ConcernedDirectorate%>';

</script>

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
		<script src="FDAComplaintForm.js"></script>
		
</head>

<body>

<div data-role="page" id="FDAComplaintFormPage" data-url="FDAComplaintFormPage" data-theme="d">

	<jsp:include page="Header.jsp" >
    	<jsp:param value="One Window Cell" name="title"/>
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
	    		
				<form name="FDAComplaintFormExecute" id="FDAComplaintFormExecute" action="#" method="post" data-ajax="false" >
				<input type="hidden" name="UpdateID" value="<%=Update%>">
				<ul data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    
				    <!-- <li data-role="list-divider">Order Information</li> -->
				    <li>
					    <table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="CaseNo" data-mini="true">Case No</label>
									<input type="text" name="CaseNo" id="CaseNo" value="<%=FormCaseNo%>" data-mini="true" maxlength="10" readonly="readonly" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="FirstName" data-mini="true">First Name</label>
									<input type="text" name="FirstName" id="FirstName" value="<%=FirstName%>" data-mini="true"  >
								</td>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="LastName" data-mini="true">Last Name</label>
									<input type="text" name="LastName" id="LastName" value="<%=LastName%>" data-mini="true" >
								</td>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ComplaintType" data-mini="true">Type</label>
									<select name="ComplaintType" id="ComplaintType" data-mini="true" >
										<%=ComplaintTypeOptions%>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="width: 20%; border: 0px; padding-right: 2px">
									<label for="Address" data-mini="true">Address</label>
									<input type="text" name="Address" id="Address" value="<%=Address%>" data-mini="true" >
								</td>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="City" data-mini="true">City</label>
									<input type="text" name="City" id="City" value="<%=City%>" data-mini="true" >
								</td>
								
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="ContactNo" data-mini="true">Contact No (e.g 321XXXXXXX)</label>
									<input type="text" name="ContactNo" id="ContactNo" value="<%=ContactNo%>" data-mini="true" >
								</td>
							</tr>
							<tr>
								<td style="width: 20%; border: 0px; padding-right: 2px; display:none">
									<label for="ConcernedDirectorate" data-mini="true">Concerned Directorate</label>
									<select name="ConcernedDirectorate" id="ConcernedDirectorate" data-mini="true">
										<option value="0">Select</option>
										<option value="1">EMI</option>
										<option value="2">EMEC</option>
									</select>
								</td>
								<td style="width: 20%; border: 0px; padding-right: 2px">
									<label for="DateOfEntry" data-mini="true">Date of Entry</label>
									<input type="date" name="DateOfEntry" id="DateOfEntry"  data-mini="true" value="<%=DateOfEntry%>" >
								</td>
							</tr>
							<tr>
								<td colspan="2" style="width: 20%; border: 0px; padding-right: 2px">
									<label for="Remarks" data-mini="true">Remarks</label>
									<input type="text" name="Remarks" id="Remarks" value="<%=Remarks%>" data-mini="true" >
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
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="Submit();" >Save</a>
                    <button data-icon="check" data-theme="b" data-inline="true" onClick="javascript:window.location='FDAComplaintForm.jsp'" >Reset</button>
				</td>
				<td align="right">
					<!-- <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DeliveryNoteSearch" >Search</a>
					 -->
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