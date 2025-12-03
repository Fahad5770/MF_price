<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<style>
td{
font-size: 8pt;
}

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 354;

//if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	//response.sendRedirect("AccessDenied.jsp");
//}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();
Statement s6 = c.createStatement();

Statement s7 = c.createStatement();
Statement s8 = c.createStatement();

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


long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

String PackageIDs = "";
String WherePackage = "";

if(SelectedPackagesArray!= null && SelectedPackagesArray.length > 0){
	for(int i = 0; i < SelectedPackagesArray.length; i++){
		if(i == 0){
			PackageIDs += SelectedPackagesArray[i]+"";
		}else{
			PackageIDs += ", "+SelectedPackagesArray[i]+"";
		}
	}
	WherePackage = " and package_id in ("+PackageIDs+") ";
}

long SelectedBrandsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
   	SelectedBrandsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}

String BrandIDs = "";
String WhereBrand = "";

if(SelectedBrandsArray!= null && SelectedBrandsArray.length > 0){
	for(int i = 0; i < SelectedBrandsArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandsArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandsArray[i]+"";
		}
	}
	WhereBrand = " and brand_id in ("+BrandIDs+") ";
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
//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and co.id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
}


//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	/* Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	} */
}

String DistributorIDs = "";
String WhereDistributors = "";
/* if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and created_by in ( select distinct dbpv.assigned_to from distributor_beat_plan_view dbpv  where dbpv.distributor_id in ("+DistributorIDs+") UNION select distinct dbpv.asm_id from distributor_beat_plan_view dbpv  where dbpv.distributor_id in("+DistributorIDs+") UNION SELECT kpo_id FROM pep.common_distributors_kpos where distributor_id in ("+DistributorIDs+"))";
	
} */

if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and created_by in ( select distinct dbpv.assigned_to from distributor_beat_plan_view dbpv  where dbpv.distributor_id in ("+DistributorIDs+") UNION select distinct dbpv.asm_id from distributor_beat_plan_view dbpv  where dbpv.distributor_id in("+DistributorIDs+") UNION SELECT kpo_id FROM pep.common_distributors_kpos where distributor_id in ("+DistributorIDs+"))";
	
	
}




//OrderBooker

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
String OrderBookerIDsWhere="";
if(OrderBookerIDs.length()>0){
	//OrderBookerIDsWhere =" and order_no in (select mobile_order_no from mobile_order_unedited mou where mou.created_by in ("+OrderBookerIDs+"))";
	OrderBookerIDsWhere =" and id in ("+OrderBookerIDs+") ";
}



//outlet
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
	WhereOutlets = " and id in ("+OutletIds+") ";	
	//System.out.println(WhereOutlets);
}

//Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
//String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Attendance Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					 	
					 	<tr style="font-size:11px;">
					 	
							<th  data-priority="1"  style="text-align:center; ">Name</th>
							<th   data-priority="1"  style="text-align:center; ">Department</th>
							<th   data-priority="1"  style="text-align:center; ">Designation</th>
							<th   data-priority="1"  style="text-align:center; ">Attendance Date</th>
							<th   data-priority="1"  style="text-align:center; ">Punch/Time  IN</th>
							<th   data-priority="1"  style="text-align:center; ">Punch/Time OUT</th>
							
					    </tr>
					   
					  </thead> 
					<tbody>
						
						<%
							long EmpID=0;
							long PutEmpSeparator=0;
							String EmpName="";
							String DepartmentLabel="";
							String DesignationLabel="";
						
						//String S1="select id,DISPLAY_NAME as name,department,designation from users where  ID in (SELECT distinct created_by FROM pep.mobile_order_booker_attendance where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors+") ";
							//System.out.println(S1);
							
							String S1="select distinct created_by as id, (select display_name from users u where u.id=created_by) name, (select department from users u where u.id=created_by) department, (select designation from users u where u.id=created_by) designation  from mobile_order_booker_attendance where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereDistributors;
							
							//System.out.println("R286 - "+S1);
							
							ResultSet rs34 = s3.executeQuery(S1 );
							while(rs34.next()){
								
								EmpID= rs34.getLong("id");
								//System.out.println(EmpID);
								EmpName= rs34.getString("name");
								DepartmentLabel= rs34.getString("department");
								DesignationLabel= rs34.getString("designation");
								
								Date CreatedOn=null;
								
								
								//System.out.println("SELECT created_on FROM pep.mobile_order_booker_attendance where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and created_by ="+EmpID);
								ResultSet rs21=s8.executeQuery("SELECT distinct date(created_on) as created_on FROM pep.mobile_order_booker_attendance where created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
								while(rs21.next()){
									String timeIn="";
									String timeOut="";
									CreatedOn =  rs21.getTimestamp ("created_on");
									//System.out.println(CreatedOn);
									
									if(CreatedOn!=null){
										String MinTime=null;
										ResultSet rs22=s2.executeQuery("SELECT min(created_on) check_in FROM pep.mobile_order_booker_attendance where created_on between "+Utilities.getSQLDate(CreatedOn)+" and "+Utilities.getSQLDateNext(CreatedOn)+"  and created_by="+EmpID+" and attendance_type=1;");
										if(rs22.first()){
											
											MinTime = rs22.getString("check_in");
											if(MinTime!=null)
											{
										
											String[] split = MinTime.split("\\s+");
								           	String extraction = split[1];
								           	timeIn = extraction.substring(0,extraction.length()-2);
								         
											}
								           	    
											
										}
										
										String MaxTime=null;
										ResultSet rs23=s4.executeQuery("SELECT max(created_on) check_out FROM pep.mobile_order_booker_attendance where created_on between "+Utilities.getSQLDate(CreatedOn)+" and "+Utilities.getSQLDateNext(CreatedOn)+"  and created_by="+EmpID+" and attendance_type=2;");
										if(rs23.first()){
											
											MaxTime = rs23.getString("check_out");
											
											String time="";
											if(MaxTime!=null){
											String[] split = MaxTime.split("\\s+");
								           	String extraction = split[1];
								           	
								           	timeOut = extraction.substring(0,extraction.length()-2);
								          	
											}
											
										}
									
									}
									
									if(EmpID!=PutEmpSeparator){
										
										PutEmpSeparator=EmpID;
									%>
									
									<tr style="font-size:11px;background-color:#E8E8E8">
											<td Style="color:#E8E8E8" colspan="6"></td>
									</tr>
									<tr style="font-size:11px;">
										<td style="text-align:left;"><%=EmpID%> - <%=EmpName %></td>
										<td style="text-align:left;"><%if(DepartmentLabel!=null){%><%=DepartmentLabel %><%} %></td>
										<td style="text-align:left;"><%if(DesignationLabel!=null){%><%=DesignationLabel %><%} %></td>
										<td style="text-align:center;"><%if(CreatedOn!=null){%><%=Utilities.getDisplayDateFormat (CreatedOn)  %><%} %></td>
										<td style="text-align:center;"><%if(timeIn!=null){%><%=timeIn %><%} %></td>
										<td style="text-align:center;"><%if(timeOut!=null){%><%=timeOut %><%} %></td>
								
								
									</tr>
									
									<%
								
								
									}
									else{
										
									%>
										
										
									<tr style="font-size:11px;">
										<td style="text-align:left;"><%=EmpID%> - <%=EmpName %></td>
										<td style="text-align:left;"><%if(DepartmentLabel!=null){%><%=DepartmentLabel %><%} %></td>
										<td style="text-align:left;"><%if(DesignationLabel!=null){%><%=DesignationLabel %><%} %></td>
										<td style="text-align:center;"><%if(CreatedOn!=null){%><%=Utilities.getDisplayDateFormat (CreatedOn)  %><%} %></td>
										<td style="text-align:center;"><%if(timeIn!=null){%><%=timeIn %><%} %></td>
										<td style="text-align:center;"><%if(timeOut!=null){%><%=timeOut %><%} %></td>
								
								
									</tr>
										<%
										
									}
								}//Sub loop Created on
								
							
							}//Main loop PSRID
					
						%>
						
					</tbody>
							
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