<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<jsp:useBean id="bean" class="com.pbc.outlet.OutletDashboard" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 199;
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}


if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("OrderBookerTargetID"));
if(EditID > 0){
	isEditCase = true;
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();



%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script>
		
			var isEditCase = <%=isEditCase%>;
			var EditID = <%=EditID%>;
		
		</script>
						
		<script src="js/OrderBookerTargets.js"></script>
		<script src="js/lookups.js"></script>
		

		<style>
		
			.radio_style
			  {
			      display: block;
			      width: 80px;
			      height: 50px;
			      background-repeat: no-repeat;
			      background-position: -231px 0px;
			  }
			  
			  .ui-table-reflow.ui-responsive{
			  	display:block;
			  }
		
		</style>
		
		
		
	</head>
	
<body>

<div data-role="page" id="OrderBookerTargetsPage" data-url="OrderBookerTargetsPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Orderbooker Targets" name="title"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
    
    <form action="test2.jsp" name="DistributorTargetsMainForm" id="DistributorTargetsMainForm" >
		<ul data-role="listview" data-inset="false" data-divider-theme="c">
		
			<li>
			
			    	<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>" >
			    	
			        <table style="width:100%" border="0">
			        	<tr>
			        		<td valign="top" style="padding: 0px; width: 50%" colspan="2">
			        			<table border="0" style="width: 100%" cellpadding="0">
			        				<tr> 
										<td style="width:20%">
											<input type="text" name="EmployeeID" id="EmployeeID" data-mini="true" placeholder="Employee ID" onchange="getOrderBookerInfoJson('')" >
											<input type="hidden" name="MonthCycle" id="MonthCycle" value="" >
											<input type="hidden" name="ProductGroupID" id="ProductGroupID" value="" >
										</td>
										<td style="width:70%">
											<input type="text" name="EmployeeName" id="EmployeeName" data-mini="true" placeholder="Employee Name" readonly="readonly" >
										</td>
									</tr>
			        			</table>
			        		</td>
			        	</tr>
			        	<tr>
			        		<td style="width: 25%">
			        			<select name="Month" id="Month" data-mini="true" onchange="getDatePeriod()" >
			        				<option value="">Month</option>
			        				<option value="1">January</option>
			        				<option value="2">February</option>
			        				<option value="3">March</option>
			        				<option value="4">April</option>
			        				<option value="5">May</option>
			        				<option value="6">June</option>
			        				<option value="7">July</option>
			        				<option value="8">August</option>
			        				<option value="9">September</option>
			        				<option value="10">October</option>
			        				<option value="11">November</option>
			        				<option value="12">December</option>
			        			</select>
			        		</td>
			        		<td>
			        			<select name="Year" id="Year" data-mini="true" onchange="getDatePeriod()" >
			        				<option value="">Year</option>
			        				<option value="2014">2014</option>
			        				<option value="2015">2015</option>
			        				<option value="2016" >2016</option>
			        				<option value="2017" >2017</option>
			        				<option value="2018" selected>2018</option>
			        			</select>
			        		</td>
			        		
			        	</tr>
			        	
			        </table>
			        
				
				
			</li>
			
			<li data-role="list-divider">Products</li>
			
			<li>
			
				<table border=0 style="width: 100%">
					<tr>
						<td style="width: 50%" valign="top">
						
							<table border=0 data-role="table" data-mode="reflow" class="ui-body-d table-stripe ui-responsive" style="font-size: 10pt; width:100%">
							  <thead>
							    <tr class="ui-bar-c">
									<th data-priority="1">Package</th>
									<th data-priority="1">Qty</th>
									<th data-priority="1">Action</th>
									<th data-priority="1">&nbsp;</th>
							    </tr>
							  </thead>
							  
								<tbody id="DistributorTargetsTableBody">
									&nbsp;
								</tbody>
							</table>
												
						</td>
						<td style="width: 50%">&nbsp;</td>
					</tr>
				</table>
			
			
									
					
			</li>
			
		
			
	    </ul>   
	    
	    
	</form> 
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						
						<table>
							<tr>
								<td><a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DistributorTargetsSave" href="#" onClick="FormSubmit();">Save</a></td>
								<td><button data-icon="check" data-theme="b" data-inline="true" id="DistributorTargetsReset" onClick="javascript:window.location='OrderBookerTargets.jsp'" >Reset</button></td>
								
							</tr>
						</table>
						
					</td>
					<td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DistributorTargetsSearch" >Search</a>
					</td>
	                
				</tr>
			</table>
		</div>
	    	
    </div>
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="EmployeeSearchCallBack" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->



<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="DistributorTargetsSearch" onSubmit="return showSearchContent()">
	            <table >
	            	<tr>
						<td astyle="width:20%">
							<input type="text" name="SearchEmployeeID" id="SearchEmployeeID" data-mini="true" placeholder="Employee ID" onchange="getOrderBookerName22()" size="12" >
							
						</td>
						<td astyle="width:60%">
							<input type="text" name="SearchEmployeeName" id="SearchEmployeeName" data-mini="true" placeholder="Employee Name" readonly="readonly" >
						</td>
						<td astyle="width:10%"><input type="submit" value="Search" data-mini="true" ></td>
	                </tr>
	                 
	            </table>
	        </form>

	        <div id="SearchContent" style="padding: 5px">
			
				   
	        
	        </div>
            
        </div>
    </div>
</div>
</body>
</html>

<%
s.close();
ds.dropConnection();
%>