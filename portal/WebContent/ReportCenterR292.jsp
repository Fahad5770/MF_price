<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<script>
	function redirect(url) {
		document.getElementById("check").action = url;
		document.getElementById("check").submit();
	}
</script>
<%
	long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
	int FeatureID = 363;
	long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

	if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
		response.sendRedirect("AccessDenied.jsp");
	}

	Datasource ds = new Datasource();
	ds.createConnectionToReplica();
	Connection c = ds.getConnection();
	Statement s = c.createStatement();
	Statement s2 = c.createStatement();
	Statement s3 = c.createStatement();
	Statement s4 = c.createStatement();

	String SelectedUserEmployeesWithText= null;
	if (session.getAttribute(UniqueSessionID + "_SR1UserSelectedEmployeesWithText") != null) {
		SelectedUserEmployeesWithText=(String) session.getAttribute(UniqueSessionID + "_SR1UserSelectedEmployeesWithText");
	}
  String EmployeeIds="''";
  String WhereEmployeeids="";
	if(SelectedUserEmployeesWithText!=null){  
	  if(SelectedUserEmployeesWithText.contains("F") || SelectedUserEmployeesWithText.contains("f"))
		{
			SelectedUserEmployeesWithText =SelectedUserEmployeesWithText.substring(1);
			
					
					ResultSet rs10 = s2.executeQuery("select user_id from user_access where feature_id="+SelectedUserEmployeesWithText +" order by user_id");
							int Count=0;
						while (rs10.next()) 
						{
							if(Count==0)
						 {
								EmployeeIds +=", "+ rs10.getInt(1) + "";	
							 Count++;
						 }
							else
						 {
								
								EmployeeIds +=", "+ rs10.getInt(1) + "";	
						 }
							
						}	
			
		}
		else if(SelectedUserEmployeesWithText.contains("R") ||SelectedUserEmployeesWithText.contains("r"))
		{
			SelectedUserEmployeesWithText ="R"+SelectedUserEmployeesWithText.substring(1);
			
			ResultSet rs9 = s2.executeQuery("select feature_id from features where short_code='"+SelectedUserEmployeesWithText+"'");
			while(rs9.next())
			{
				 ResultSet rs10 = s3.executeQuery("select user_id from user_access where feature_id="+rs9.getInt(1) +" order by user_id");
	 			
				 int Count=0;
				while (rs10.next()) 
				{
					if(Count==0)
				 {
						EmployeeIds += ", "+rs10.getInt(1) + "";	
					 Count++;
				 }
					else
				 {
						
						EmployeeIds +=", "+ rs10.getInt(1) + "";	
				 }
					
				}  
			} 
			
			
		}
	}
	
	

	
	 WhereEmployeeids = "ID in (" + EmployeeIds + ") ";
	 //System.out.print(WhereEmployeeids);
	
	
	
	
%>
<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -10px;"
	data-icon="false">

	<li data-role="list-divider" data-theme="a">Employee Features List</li>
	<li>
		<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
			<thead>
				<tr style="font-size: 11px;">

					<th data-priority="1"
						style="text-align: center; width: 15%;">Employee</th>
					<th data-priority="1"
						style="text-align: center; width: 15%;">DESIGNATION</th>
					<th data-priority="1"
						style="text-align: center; width: 48%;">DEPARTMENT</th>
				</tr>
			</thead>
			<%
				int intable = 0;
				ResultSet rs10 = s4.executeQuery("select ID, DISPLAY_NAME,DESIGNATION,DEPARTMENT from users where "+ WhereEmployeeids+" order by ID");
				
					while (rs10.next()) 
					{
						 
			%>
			
			
			<tr style="font-size: 12px;">
				<td style="text-align: left;"><%=rs10.getString(1)%> - <%=rs10.getString(2)%></td>
				<td style="text-align: left;"><%=rs10.getString(3)%></td>
				<td style="text-align: left;"><%=rs10.getString(4)%></td>
			</tr>
			<%
				
					}
			%>
		</table> 
	</li>

</ul>

<%
	s3.close();
	s2.close();
	s.close();
	c.close();
	ds.dropConnection();
%>