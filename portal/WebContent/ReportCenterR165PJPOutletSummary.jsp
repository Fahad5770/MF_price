<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 196;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

String PJPIDsString = request.getParameter("PJPID");
String WherePJPID ="";

if(!PJPIDsString.equals("x125")){ //mean all PJP selected
	long PJPID = Utilities.parseLong(PJPIDsString);
	WherePJPID = " and id="+PJPID;
}


Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");


if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}



//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){	
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
}

String DistributorIDs = "";
String WhereDistributors = "";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}


// HOD
String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereDistributors += " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
}


//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}


String WhereRSM = "";
if (RSMIDs.length() > 0){
	WhereDistributors += " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}




%>



			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Unproductive Outlets</li>
			<li>
			
			
				<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
					<tr>
						
						<td style="width: 100%" valign="top">
							<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
									 <thead>
									    <tr style="font-size: 11px;">
											<th data-priority="1"  style="text-align:center; width: 25%" >Name</th>
											<th data-priority="1"  style="text-align:center; width: 25%" >Address</th>											
											<th data-priority="1"  style="text-align:center; width: 25%" >Contact</th>											
											<th data-priority="1"  style="text-align:center; width: 15%" >Days</th>
											<th data-priority="1"  style="text-align:center; width: 10%" >Last Sale</th>
									    </tr>
									    
									  </thead> 
									
								<%
								
								ResultSet rs = s.executeQuery("SELECT co.id,co.name,ifnull((select date(max(isa.created_on)) last_order FROM inventory_sales_adjusted isa where isa.outlet_id = co.id),'2013-01-01') last_order,co.address,(select Concat(coc.contact_name,' ',coc.contact_number) contact from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) contact FROM common_outlets co where co.id in(select outlet_id from  distributor_beat_plan_all_view where 1=1 "+WherePJPID+WhereRSM+WhereDistributors+" )"+" having last_order < cast("+Utilities.getSQLDate(EndDate)+" as date) order by last_order desc");
								//System.out.println("SELECT co.id,co.name,ifnull((select date(max(isa.created_on)) last_order FROM inventory_sales_adjusted isa where isa.outlet_id = co.id),'2013-01-01') last_order,co.address,(select Concat(coc.contact_name,' ',coc.contact_number) contact from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) contact FROM common_outlets co where co.id in(select outlet_id from  distributor_beat_plan_all_view where 1=1 "+WherePJPID+WhereRSM+WhereDistributors+" )"+" having last_order < "+Utilities.getSQLDate(EndDate)+" order by last_order desc");
								while(rs.next()){
								%>
										<tr style="font-size: 10px;">
					   	            		<td><%=rs.getString("id") %> - <%=rs.getString("name") %></td>
					   	            		<td><%=rs.getString("address") %></td>
					   	            		<td><%=rs.getString("contact") %></td>					   	            		
					   	            		<td></td>
					   	            		<td><%if(rs.getString("last_order").equals("2013-01-01")){out.print("Never");}else{%><%= Utilities.getDisplayDateFormat(rs.getDate("last_order")) %><%} %></td>
					   	            	</tr>
								<%
								}
								%>		
											
															
						    		    			
								</table>
						</td>
					</tr>
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