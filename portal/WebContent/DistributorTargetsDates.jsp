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
int FeatureID = 87;
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}


if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("DistributorTargetID"));
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
						
		<script src="js/DistributorTargets.js"></script>
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

<div data-role="page" id="DistributorTargets" data-url="DistributorTargets" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Distributor Targets Dates" name="title"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
    
    <%
    
    
    int Month = Utilities.parseInt(request.getParameter("Month"));
    int Year = Utilities.parseInt(request.getParameter("Year"));
    int RegionID = Utilities.parseInt(request.getParameter("Region"));
    
    
    
    %>
    
    
    
    <form action="DistributorTargetsDates.jsp" method="post" data-ajax="false" id="DistributorTargetsDatesMainForm">
		<ul data-role="listview" data-inset="false" data-divider-theme="c">
		
			<li>
			
			    	<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>" >
			    	
			        <table style="width:100%" border="0">
			        	
			        	<tr>
			        		<td style="width: 25%">
			        			<select name="Month" id="Month" data-mini="true" onchange="getDatePeriod()" >
			        				<option value="">Month</option>
			        				<option value="1" <%if(Month==1){ %> selected <%} %>>January</option>
			        				<option value="2" <%if(Month==2){ %> selected <%} %>>February</option>
			        				<option value="3" <%if(Month==3){ %> selected <%} %>>March</option>
			        				<option value="4" <%if(Month==4){ %> selected <%} %>>April</option>
			        				<option value="5" <%if(Month==5){ %> selected <%} %>>May</option>
			        				<option value="6" <%if(Month==6){ %> selected <%} %>>June</option>
			        				<option value="7" <%if(Month==7){ %> selected <%} %>>July</option>
			        				<option value="8" <%if(Month==8){ %> selected <%} %>>August</option>
			        				<option value="9" <%if(Month==9){ %> selected <%} %>>September</option>
			        				<option value="10" <%if(Month==10){ %> selected <%} %>>October</option>
			        				<option value="11" <%if(Month==11){ %> selected <%} %>>November</option>
			        				<option value="12" <%if(Month==12){ %> selected <%} %>>December</option>
			        			</select>
			        		</td>
			        		<td style="width: 25%">
			        			<select name="Year" id="Year" data-mini="true" onchange="getDatePeriod()" >
			        				<option value="">Year</option>
			        				<option value="2014" <%if(Year==2014){ %> selected <%} %>>2014</option>
			        				<option value="2015" <%if(Year==2015){ %> selected <%} %>>2015</option>
			        				
			        			</select>
			        		</td>
			        		<td style="width: 25%">
			        			<select name="Region" id="Region" data-mini="true" onchange="getDatePeriod()" >
			        				<option value="">Region</option>
			        				<%
			        				ResultSet rs = s.executeQuery("select * from common_regions");
			        				while(rs.next()){
			        					%>
			        					
			        					<option value="<%=rs.getInt("region_id")%>" <%if(RegionID==rs.getInt("region_id")){ %>selected<%} %>><%=rs.getString("region_short_name") %> - <%=rs.getString("region_name") %></option>
			        					
			        					<%
			        				}
			        				
			        				%>
			        				
			        			</select>
			        		</td>
			        		<td style="width: 5%"><input type="submit" value="Search" data-ajax="fase" data-mini="true" data-icon="search"/></td>
			        	</tr>
			        	
			        	
			        </table>
			       
				
				
			</li>
			<%if(Month!=0){ %>
			<li data-role="list-divider">Distributors</li>
			<li>
			
			<table>
			<tr>
			<td>&nbsp;</td>
			<td>From Date</td>
			<td>To Date</td>
			</tr>
			<%
			//System.out.println("SELECT dt.distributor_id,cd.name,dt.month,dt.year,dt.start_date,dt.end_date,cd.region_id FROM distributor_targets dt join common_distributors cd on dt.distributor_id=cd.distributor_id where dt.month="+Month+" and dt.year="+Year+" and cd.region_id="+RegionID);
			ResultSet rs1 = s.executeQuery("SELECT dt.distributor_id,cd.name,dt.month,dt.year,dt.start_date,dt.end_date,cd.region_id FROM distributor_targets dt join common_distributors cd on dt.distributor_id=cd.distributor_id where dt.month="+Month+" and dt.year="+Year+" and cd.region_id="+RegionID);
			while(rs1.next()){
			%>
			
			
			<tr>
				<td style="f1ont-weight:normal; font-size:16px;">
				<input type="hidden" name="DistributorID" id="DistributorID" value="<%=rs1.getString("distributor_id") %>"/>
				<%=rs1.getString("distributor_id") %> - <%=rs1.getString("name") %>
				</td>
				<td><input type="text" name="StartDateDis" id="StartDateDis" value="<%=Utilities.getDisplayDateFormat(rs1.getDate("start_date"))%>" data-mini="true"/></td>
				<td><input type="text" name="EndDateDis" id="EndDateDis" value="<%=Utilities.getDisplayDateFormat(rs1.getDate("end_date"))%>" data-mini="true"/></td>
			
			</tr>
			
			<%
			}
			%>
			
			</table>
			
			</li>
		<%
			}
		%>
			
		
			
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
								<td><a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="DistributorTargetsUpdate" href="#" onClick="DistributorTargetsDatesSubmit();">Update</a></td>
								<td><button data-icon="check" data-theme="b" data-inline="true" id="DistributorTargetsReset" onClick="javascript:window.location='DistributorTargetsDates.jsp'" >Reset</button></td>
								
							</tr>
						</table>
						
					</td>
					<td align="right">
	                   <a href="DistributorTargets.jsp"  data-icon="check" data-ajax="false" data-theme="b" data-role="button" data-inline="true" data-position-to="window"   >Back</a>
					</td>
	                
				</tr>
			</table>
		</div>
	    	
    </div>
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="setDistributorLookupAtDistributorTargets" name="CallBack" />
    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Distributor Search -->



<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

			<form data-ajax="false" id="DistributorTargetsSearch" onSubmit="return showSearchContent()">
	            <table >
	            	<tr>
						<td astyle="width:20%">
							<input type="text" name="DistributorTargetsSearchDistributorID" id="DistributorTargetsSearchDistributorID" data-mini="true" placeholder="Distributor ID" onchange="getDistributorName()" size="12" >
							
						</td>
						<td astyle="width:60%">
							<input type="text" name="DistributorTargetsSearchDistributorName" id="DistributorTargetsSearchDistributorName" data-mini="true" placeholder="Distributor Name" readonly="readonly" >
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