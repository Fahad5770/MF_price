<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>

<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 135;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

int IsAssigned = 0;
int IsAssignedParam = Utilities.parseInt(request.getParameter("IsAssigned"));
if(IsAssignedParam == 1){
	IsAssigned = 1;
}


%>


<ul data-role="listview" data-inset="true" class="ui-icon-alt" data-theme="d" data-divider-theme="c" data-count-theme="c" style="margin-top:3px;">
				<li data-role="list-divider" data-theme="a">Complaints</li>
				
				
				
				<%
				Date LastDate = Utilities.parseDate("01/01/1997");
				//System.out.println( "SELECT * FROM crm_complaints where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" " );
				String Where = "";
				
				Where = " and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" ";
				
				
				//System.out.println( "SELECT * FROM crm_complaints where is_assigned="+IsAssigned+" "+Where );
				
				String SQL = "SELECT cc.id, ifnull(cc.outlet_id,'') outlet_id, cc.outlet_name, cc.type_id, (select label from crm_complaints_types where id = cc.type_id) type_label, cc.created_on FROM crm_complaints cc where cc.is_assigned=0";
				if( IsAssigned == 1 ){
					SQL = "SELECT cca.id, ifnull(cca.outlet_id,'') outlet_id, cca.outlet_name, cca.type_id, (select label from crm_complaints_types where id = cca.type_id) type_label, cca.created_on FROM crm_complaints_assigned cca where cca.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" ";
				}
				
				//System.out.println(SQL);
				
				ResultSet rs22 = s.executeQuery(SQL);
				int counter = 0;
				while(rs22.next()){
							
					if(!DateUtils.isSameDay(LastDate, rs22.getDate("created_on")) ){
	            		%>
	            		<li data-role="list-divider" style="font-size: 10px;"><%=Utilities.getDisplayFullDateFormat(rs22.getDate("created_on"))%></li>
	            		<%
	            	}
					/*
					int DescriptionLength = 50;
					String Description = rs22.getString("description");
					
					if( rs22.getString("description").length() > DescriptionLength ){
						Description = rs22.getString("description").substring(0, DescriptionLength)+" ...";
					}
					*/
					%>
					<li style="height: 40px" >
						<a data-ajax="false" href="#" onclick="OpenComplaintForm('<%=rs22.getString("id")%>', <%=IsAssigned%>);" style="font-size:10px" ><span style="font-size: 12px; font-weight: bold"><%=rs22.getString("id")%> - <%=rs22.getString("type_label")%><br> <%=rs22.getString("outlet_id")%> - <%=rs22.getString("outlet_name")%></span></a>
					</li>
					<%
					LastDate = rs22.getDate("created_on");
					counter++;
				}
				if(counter==0){
					%>
					<li style="height: 12px">&nbsp;</li>
					<%
				}
					%>		
				
			</ul>


<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>