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
int FeatureID = 132;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));
if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}
//
Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));

Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}
boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and outlet_id in ("+OutletIds+") ";	
}

boolean IsOrderBookerSelected=false;

int OrderBookerArrayLength=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	
	IsOrderBookerSelected=true;
	OrderBookerArrayLength=SelectedOrderBookerArray.length;
}



String OrderBookerIDs = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWher="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWher =" and created_by in ("+OrderBookerIDs+") ";
}


//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	IsOrderBookerSelected=true;
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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") and tab1.outlet_id in (select outlet_id from common_outlets_distributors_view where 1=1";
	//out.print(WhereDistributors);
}



//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
}

//TDM


String TDMIDs="";
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedTDM") != null){
	SelectedTDMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedTDM");
	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
}

String WhereTDM = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
}

//ASM


String ASMIDs="";
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedASM") != null){
	SelectedASMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedASM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0){
	WhereASM = " and distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}

//days range
int DaysGreaterThan=0;
int DaysLessThan=0;

if (session.getAttribute(UniqueSessionID+"_SR1SelectedDaysGreaterThan") != null){
	DaysGreaterThan = (Integer)session.getAttribute(UniqueSessionID+"_SR1SelectedDaysGreaterThan");
}

if (session.getAttribute(UniqueSessionID+"_SR1SelectedDaysLessThan") != null){
	DaysLessThan = (Integer)session.getAttribute(UniqueSessionID+"_SR1SelectedDaysLessThan");
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
		WhereDiscountedAll = " and tab1.outlet_id in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Fixed")){	
		WhereDiscountedFixed = " and tab1.outlet_id in (select outlet_id from sampling where active = 1 and date(now()) between fixed_valid_from and fixed_valid_to and fixed_company_share != 0) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Per Case")){	
		WhereDiscountedPerCase = " and tab1.outlet_id in (select distinct outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and date(now()) between sp.valid_from and sp.valid_to) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Non Discounted")){	
		WhereNonDiscountedAll = " and tab1.outlet_id not in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Active")){	
		WhereActive = " and co.is_active=1 ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Deactivated")){	
		WhereDeactivated = " and co.is_active=0 ";
	}
}


%>




<table border="0" style="width: 100%">
	<tr>
		
		
		<td style="width: 100%" valign="top">
		
			
			
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Outlet Inactivity Aging</li>
			<li>
			
			
				<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
					<tr>
						
						<td style="width: 100%" valign="top">
							<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
									 <thead>
									    <tr style="font-size: 11px;">
											<th data-priority="1"  style="text-align:center; width: 20%" >Name</th>
											<th data-priority="1"  style="text-align:center; width: 20%" >Address</th>											
											<th data-priority="1"  style="text-align:center; width: 20%" >Contact</th>
											<th data-priority="1"  style="text-align:center; width: 20%" >Current Order Booker</th>
											<th data-priority="1"  style="text-align:center; width: 10%" >Last Sale</th>
											<th data-priority="1"  style="text-align:center; width: 10%" >Days Since</th>											
											
									    </tr>
									    
									  </thead> 
									
								<%
								boolean NeverOrderBooked=false;
								ResultSet rs = s.executeQuery("select outlet_id, max_date, datediff(now(),max_date) diff,co.name,co.address,(select concat(contact_name,' ',contact_number) from common_outlets_contacts coc where coc.outlet_id=tab1.outlet_id) outlet_contact ,IFNULL((select concat(assigned_to,' - ',u.display_name) order_boker from distributor_beat_plan_view dbpv join users u on dbpv.assigned_to=u.id  where dbpv.outlet_id=tab1.outlet_id limit 1),' ') order_booker from ("+
																"select co.id outlet_id, ifnull((select max(created_on_erdat)  from sap_sales where outlet_id = co.id),'2008-01-01') max_date from common_outlets co where is_active = 1"+
																") tab1 join common_outlets co on co.id=tab1.outlet_id where 1=1 "+WhereDiscountedAll+WhereDiscountedFixed+WhereDiscountedPerCase+WhereNonDiscountedAll+WhereActive+WhereDeactivated+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" having diff between "+DaysGreaterThan+" and "+DaysLessThan+" order by diff desc");
								
								
								/*System.out.println("select outlet_id, max_date, datediff(now(),max_date) diff,co.name,co.address,(select concat(contact_name,' ',contact_number) from common_outlets_contacts coc where coc.outlet_id=tab1.outlet_id) outlet_contact ,IFNULL((select concat(assigned_to,' - ',u.display_name) order_boker from distributor_beat_plan_view dbpv join users u on dbpv.assigned_to=u.id  where dbpv.outlet_id=tab1.outlet_id limit 1),' ') order_booker from ("+
										"select co.id outlet_id, ifnull((select max(created_on_erdat)  from sap_sales where outlet_id = co.id),'2008-01-01') max_date from common_outlets co where is_active = 1"+
										") tab1 join common_outlets co on co.id=tab1.outlet_id where 1=1 "+WhereDiscountedAll+WhereDiscountedFixed+WhereDiscountedPerCase+WhereNonDiscountedAll+WhereActive+WhereDeactivated+WhereDistributors+WhereHOD+WhereRSM+" having diff between "+DaysGreaterThan+" and "+DaysLessThan+" order by diff desc");
								*/
								
								while(rs.next()){
									if(rs.getDate("max_date").toString().equals("2008-01-01")){
										NeverOrderBooked = true;
									}
								%>
										<tr style="font-size: 10px;">
					   	            		<td><%=rs.getString("outlet_id") %> - <%=rs.getString("name") %></td>
					   	            		<td><%=rs.getString("address") %></td>
					   	            		<td><%=rs.getString("outlet_contact") %></td>
					   	            		<td><%=rs.getString("order_booker") %></td>
					   	            		<td><%if(NeverOrderBooked){%>Never<%} else{%><%=Utilities.getDisplayDateFormat(rs.getDate("max_date")) %><%} %></td>
					   	            		<td><%if(NeverOrderBooked){%><%}else{%><%=rs.getInt("diff") %><%} %></td>
					   	            	</tr>
								<%
								NeverOrderBooked = false;
								}
								%>		
									
								</table>
						</td>
					</tr>
				</table>
			
				</li>	
			</ul>
		
		
		
		</td>
		
			
		
	</tr>
</table>
 
<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>