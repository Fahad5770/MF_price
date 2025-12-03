<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<style>
#map {
        width: 100%;
        height: 300px;
        margin-top: 10px;
      }
</style>

<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 380;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


int Flag=Utilities.parseInt(request.getParameter("flag"));
long PJPID=Utilities.parseLong(request.getParameter("PJPID"));


if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s6 = c.createStatement();
Statement s7 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));

Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}

//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	//
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}*/
}

String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and isdic.outlet_id in ("+OutletIds+") ";	
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
	out.print(WhereDistributors);
}




%>



			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Summary</li>
			<li>
			
			
				<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
					<tr>
						
						<td style="width: 100%" valign="top">
							
							<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
									 <thead>
									    <tr style="font-size: 11px;">
										
											<th data-priority="1"  style="text-align:center;" >PSR</th>
											<th data-priority="1"  style="text-align:center; " >Total Outlets</th>
											<th data-priority="1"  style="text-align:center;" >Sale Outlets</th>
											<th data-priority="1"  style="text-align:center; " >ECO</th>
																						
											
									    </tr>
									   
									  </thead> 
									
								<%
									
							//	System.out.println("select count(distinct outlet_id) total_outlets,assigned_to,(select concat(id,'-',display_name) from users u where u.id=assigned_to)psr from distributor_beat_plan_view where 1=1 "+WhereDistributors+" group by assigned_to ");

										ResultSet rs2 = s.executeQuery("select count(distinct outlet_id) total_outlets,assigned_to,(select concat(id,'-',display_name) from users u where u.id=assigned_to)psr from distributor_beat_plan_view where 1=1 "+WhereDistributors+" group by assigned_to ");
										while(rs2.next()){
										
											 String PSR=rs2.getString("psr");
											 int AssignedTo=rs2.getInt("assigned_to");
											 double TotalOutlets=rs2.getDouble("total_outlets");
											 double SaleOutlets=0;
											 double ECO=0;
											 
											// System.out.println("select count(distinct outlet_id) sale_outlets from inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and booked_by="+AssignedTo+" and outlet_id in (select distinct outlet_id from distributor_beat_plan_view where assigned_to="+AssignedTo+") ");

											 ResultSet rs3=s2.executeQuery("select count(distinct outlet_id) sale_outlets from inventory_sales_adjusted where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and booked_by="+AssignedTo+" and outlet_id in (select distinct outlet_id from distributor_beat_plan_view where assigned_to="+AssignedTo+") ");
											 if(rs3.first()){
												 
												 SaleOutlets=rs3.getDouble("sale_outlets");
											 }
											 if(TotalOutlets!=0){
												 
												 ECO=(SaleOutlets/TotalOutlets)*100;
											 }
											
											 
											 %>
												
												<tr style="font-size: 12px;">
							    					<td style="text-1align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=PSR %></td>
							    					<td style="text-1align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(TotalOutlets) %></td>
								    				<td style="text-a1lign:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(SaleOutlets) %></td>	
							    					<td style="text-a1lign:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%=Utilities.getDisplayCurrencyFormatRounded(ECO) %>%</td>							    					
							    					
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
			
			
			%>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>