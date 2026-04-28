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
int FeatureID = 84;
if ((String)session.getAttribute("UserID") == null){
	response.sendRedirect("index.jsp");
}


if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


long DistributorID = Utilities.parseLong(request.getParameter("DistributorID"));
String DistributorName = "";

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("MobileDeviceID"));
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
		<script src="js/MobileDevices.js"></script>
		<script src="js/lookups.js"></script>
		
		<script>
		
			var isEditCase = <%=isEditCase%>;
			var EditID = <%=EditID%>;
			
			if(isEditCase){
				getMobileDeviceInfoJson(EditID);
			}
			
		
		</script>
		
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

<div data-role="page" id="MobileDevices" data-url="MobileDevices" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Mobile Devices" name="title"/>
    </jsp:include>
    <!-- /header -->
    
    <div data-role="content" data-theme="d">
		<ul data-role="listview" data-inset="false" data-divider-theme="c">
		
			<li>
			
				<form action="test2.jsp" name="MobileDevicesMainForm" id="MobileDevicesMainForm" >
					
			    	<input type="hidden" name="EditID" id="EditID" value="<%=EditID%>" >
			    	
			        <table style="width:100%" border="0">
			        	<tr>
			        		<td valign="top" style="width: 50%">
			        			<input type="text" name="MobileDevicesDeviceID" id="MobileDevicesDeviceID" data-mini="true" placeholder="Device ID" >
			        		</td>
			        		<td valign="top">
			        			<input type="text" name="MobileDevicesMobileNo" id="MobileDevicesMobileNo" data-mini="true" placeholder="Mobile #" >
			        		</td>
			        	</tr>
			        	<tr>
			        		<td valign="top" style="padding: 0px">
			        			
			        			<table border="0" style="width: 100%" cellpadding="0">
			        				<tr> 
										<td style="width:20%">
											<input type="text" name="MobileDevicesAssignToID" id="MobileDevicesAssignToID" data-mini="true" placeholder="User ID" onchange="getEmployeeName();" >
										</td>
										<td style="width:70%">
											<input type="text" name="MobileDevicesAssignTo" id="MobileDevicesAssignTo" data-mini="true" placeholder="User Name" readonly="readonly" >
										</td>
									</tr>
			        			</table>
			        			
			        			
			        			
			        		</td>
			        	</tr>
			        </table>
			        
				</form>
				
			</li>
	    </ul>    
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
    
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						
						<table>
							<tr>
								<td><a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="MobileDevicesSave" href="#" onClick="MobileDevicesSubmit();">Save</a></td>
								<td><button data-icon="check" data-theme="b" data-inline="true" id="MobileDevicesReset" onClick="javascript:window.location='MobileDevices.jsp'" >Reset</button></td>
								<td>
									<span id="ActivateButtonSpan" style="display: none"><button data-icon="check" data-theme="b" data-inline="true" id="MobileDevicesActivate" onClick="MobileDevicesSetActive(1)" >Activate</button></span>
									<span id="DeActivateButtonSpan" style="display: none"><button data-icon="check" data-theme="b" data-inline="true" id="MobileDevicesDeActivate" onClick="MobileDevicesSetActive(0)" >Deactivate</button></span>
								</td>
							</tr>
						</table>
						
					</td>
					<td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="MobileDevicesSearch" >Search</a>
					</td>
	                
				</tr>
			</table>
		</div>
	    	
    </div>
    
    <jsp:include page="LookupEmployeeSearchPopup.jsp" > 
    	<jsp:param value="setEmployeeLookupAtMobileDevices" name="CallBack" />
    </jsp:include><!-- Include Employee Search -->



<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >

        <div id="SearchContent" style="padding: 5px">
		
			<ul data-role="listview" data-inset="false" data-divider-theme="c">
		
			
			<li data-role="list-divider">Deactivated</li>
			<%
			ResultSet rs = s.executeQuery("select *, (SELECT DISPLAY_NAME FROM users where id=issued_to) user_name from mobile_devices where is_active=0 order by user_name");
			while(rs.next()){
				%>
					<li><a href="MobileDevices.jsp?MobileDeviceID=<%=rs.getString("id")%>" data-ajax="false" style="font-weight: normal; font-size: 12px"><%=rs.getString("issued_to")+" - "+rs.getString("user_name")%><span class="ui-li-count"><%=rs.getString("uuid")%></span></a></li>
				<%
			}
			%> 
			
			<li data-role="list-divider">Activated</li>
			<%
			ResultSet rs2 = s.executeQuery("select *, (SELECT DISPLAY_NAME FROM users where id=issued_to) user_name from mobile_devices where is_active=1 order by user_name");
			while(rs2.next()){
				%>
					<li><a href="MobileDevices.jsp?MobileDeviceID=<%=rs2.getString("id")%>" data-ajax="false" style="font-weight: normal; font-size: 12px"><%=rs2.getString("issued_to")+" - "+rs2.getString("user_name")%><span class="ui-li-count"><%=rs2.getString("uuid")%></span></a></li>
				<%
			}
			%> 
			   
			
			</ul>    
        
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