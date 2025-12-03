<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.mobile.MobileRequest"%>
<script>


function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<%
//

int FeatureID = 327;

MobileRequest mr = new MobileRequest(Utilities.filterString(request.getParameter("SessionID"), 1, 3000));

if (!mr.isExpired()){

long UserID = Utilities.parseLong(mr.getParameter("UserID"));
String Password = Utilities.filterString(mr.getParameter("Identity"), 1, 100);
String DeviceID = Utilities.filterString(mr.getParameter("UVID"), 1, 100);

//long OutletID = Utilities.parseLong(mr.getParameter("OutletID"));

boolean isValid = true;

boolean isDeviceValid = UserAccess.isMobileDeviceValid(DeviceID);
//if(isDeviceValid){
	boolean isUserValid = UserAccess.isMobileUserValid(UserID, Password);
	if(isUserValid){
		if(Utilities.isAuthorized(FeatureID, UserID) == false){
			isValid = false;
			response.sendRedirect("AccessDenied.jsp");
		}
	}else{
		isValid = false;
		response.sendRedirect("AccessDenied.jsp");
	}
//}else{
	//isValid = false;
	//response.sendRedirect("AccessDenied.jsp");
//}

if (isValid){
	

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();


Date StartDate = Utilities.parseDate(mr.getParameter("FromDate"));
Date EndDate = Utilities.parseDate(mr.getParameter("ToDate"));


String Query = "SELECT * FROM mrd_census mc where mc.created_by="+UserID+"  and mc.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);

//System.out.println(Query);

%>

<table style="width: 100%" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					 <thead>
					    <tr style="font-size: 11px;">
							<th data-priority="2" style="width: 10%" >Sr#</th>
							<th data-priority="1"  style="t1ext-align:center; width: 20%" >Outlet ID</th>							
							<th data-priority="1"  style="t1ext-align:center; width: 20%" >Outlet Name</th>
							<th data-priority="1"  style="t1ext-align:center; width: 20%" >Name on Board</th>
							<th data-priority="1"  style="t1ext-align:center; width: 30%" >Created On</th>
					    </tr>
					    
					  </thead> 
					
				<%					
					int i=1;
					ResultSet rs = s.executeQuery(Query);
					while(rs.next()){
					%>			
		    		  <tr>
		    		  	<td style="t1ext-align:center; width: 10%"><%=i %></td>
		    		  	<td style="t1ext-align:center; width: 20%"><%=rs.getString("outlet_id") %></td>
		    		  	<td style="t1ext-align:center; width: 20%"><%=rs.getString("outlet_name") %></td>
		    		  	<td style="t1ext-align:center; width: 20%"><%=rs.getString("outlet_board_name") %></td>
		    		  	<td style="t1ext-align:center; width: 30%"><%=Utilities.getDisplayDateTimeFormat(rs.getTimestamp("created_on")) %></td>
		    		  
		    		  </tr>
		    		<%
		    		i++;
					}
		    		%>    			
				</table>
		</td>
	</tr>
</table>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();

}

}else{
	out.print("Error Code: 101");
}
%>