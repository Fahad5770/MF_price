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
int FeatureID = 264;
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
	WherePJPID = " and dbpauov.id="+PJPID;
}




//Distributor

boolean IsDistributorSelected=false;
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");  
	IsDistributorSelected = true;
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
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
	WhereDistributors = " and dbpauov.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
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
	WhereDistributors = " and dbpauov.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//Outlet Type


String OutletTypes="";
String SelectedOutletTypeArray[]={};
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType") != null){
	SelectedOutletTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType");
	//OutletTypes = Utilities.serializeForSQL(SelectedOutletTypeArray);
}

String WhereDiscountedAll = "";
String WhereDiscountedFixed = "";
String WhereDiscountedPerCase = "";
String WhereActive = "";
String WhereDeactivated = "";
String WhereNonDiscountedAll ="";


for(int i=0;i<SelectedOutletTypeArray.length;i++){
	if(SelectedOutletTypeArray[i].equals("Discounted - All")){	
		WhereDiscountedAll = " and co.id in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Fixed")){	
		WhereDiscountedFixed = " and co.id in (select outlet_id from sampling where active = 1 and date(now()) between fixed_valid_from and fixed_valid_to and fixed_company_share != 0) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Per Case")){	
		WhereDiscountedPerCase = " and co.id in (select distinct outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and date(now()) between sp.valid_from and sp.valid_to) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Non Discounted")){	
		WhereNonDiscountedAll = " and co.id not in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Active")){	
		WhereActive = " and co.is_active=1 ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Deactivated")){	
		WhereDeactivated = " and co.is_active=0 ";
	}
}



String SecondaryDistributorString="";
int SecondaryDistributor=0;

String WhereSecDistributor="";

SecondaryDistributorString=(String)session.getAttribute("UserDistributorID");
SecondaryDistributor = Utilities.parseInt(SecondaryDistributorString);


%>



			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Outlet Summary</li>
			<li>
			
			
				<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
					<tr>
						
						<td style="width: 100%" valign="top">
							<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
									 <thead>
									    <tr style="font-size: 11px;">
									    	<th data-priority="1"  style="text-align:center; width: 25%" >Order Booker</th>
											<th data-priority="1"  style="text-align:center; width: 20%" >Name</th>
											<th data-priority="1"  style="text-align:center; width: 25%" >Address</th>											
											<th data-priority="1"  style="text-align:center; width: 10%" >Days</th>
											<th data-priority="1"  style="text-align:center; width: 10%" >Last Sale</th>
											<th data-priority="1"  style="text-align:center; width: 10%" >TOT Injected</th>
									    </tr>
									    
									  </thead> 
									
								<%
								//ResultSet rs = s.executeQuery("SELECT co.id,co.name,(select max(created_on) last_order FROM mobile_order mo where mo.outlet_id = co.id) last_order,co.address,(select Concat(coc.contact_name,' ',coc.contact_number) contact from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) contact ,(select group_concat((select short_name from common_days_of_week where id = dbpav.day_number)) days from  distributor_beat_plan_all_view dbpav where 1=1 "+WherePJPID+WhereDistributors+" and outlet_id = co.id) days, (select assigned_to from  distributor_beat_plan_all_view dbpav where 1=1 "+WherePJPID+WhereDistributors+" limit 1) assigned_to FROM common_outlets co where co.id in(select outlet_id from  distributor_beat_plan_all_view where 1=1 "+WherePJPID+WhereDistributors+" )"+WhereDiscountedAll+WhereDiscountedFixed+WhereDiscountedPerCase+WhereNonDiscountedAll+WhereActive+WhereDeactivated+" order by last_order desc");
								ResultSet rs = s.executeQuery("SELECT dbpauov.id, dbpauov.assigned_to, dbpauov.assigned_to_name, dbpauov.outlet_id, co.name, co.address, dbpauov.days, (select max(created_on) from inventory_sales_adjusted where outlet_id = dbpauov.outlet_id ) last_sales, (SELECT count(*) FROM common_assets where outlet_id = dbpauov.outlet_id and tot_status = 'INJECTED') tot_injected FROM distributor_beat_plan_all_unique_outlets_view dbpauov join common_outlets co on dbpauov.outlet_id = co.id where 1 = 1 "+WherePJPID+" and dbpauov.distributor_id = "+SecondaryDistributor+" "+WhereDiscountedAll+WhereDiscountedFixed+WhereDiscountedPerCase+WhereNonDiscountedAll+" order by assigned_to desc, last_sales desc");
								while(rs.next()){
								%>
										<tr style="font-size: 10px;">
											<td><%=rs.getString("assigned_to") + " - "+ rs.getString("assigned_to_name") %></td>
					   	            		<td><%=rs.getString("outlet_id") %> - <%=rs.getString("name") %></td>
					   	            		<td><%=rs.getString("address") %></td>
					   	            		<td><%=rs.getString("days") %></td>
					   	            		<td><%if(rs.getDate("last_sales") != null){%><%= Utilities.getDisplayDateFormat(rs.getDate("last_sales")) %><%} %></td>
					   	            		<td style="text-align: center;"><%=rs.getInt("tot_injected") %></td>
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