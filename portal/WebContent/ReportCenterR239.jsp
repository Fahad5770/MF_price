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
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>


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
int FeatureID = 300;





if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();



Datasource dsSAP = new Datasource();
dsSAP.createConnectionToSAPDB();
Connection cSAP = dsSAP.getConnection();
Statement sSAP = cSAP.createStatement();

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

//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and isap.cache_distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and isap.cache_distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
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
	WhereDistributors = " and isap.cache_distributor_id in ("+DistributorIDs+") ";
	
	
	//out.print(WhereDistributors);
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
	WhereSM = " and isap.cache_distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and cd.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and cd.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}


//Sales Type


String AccountTypeIDs="";
long SelectedAccountTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType") != null){
	SelectedAccountTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType");
	AccountTypeIDs = Utilities.serializeForSQL(SelectedAccountTypeArray);
}

String WhereSalesType = "";
if (AccountTypeIDs.length() > 0){
	WhereSalesType = " and type_id in ("+AccountTypeIDs+")";	
}

long CustomerID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID") != null){
	CustomerID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID");
}


//Transaction Account


String CashInstrumentsIDs="";
long SelectedCashInstrumentsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments") != null){
	SelectedCashInstrumentsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments");
	CashInstrumentsIDs = Utilities.serializeForSQL(SelectedCashInstrumentsArray);
}

String WhereCashInstruments = " and account_id=''";
if (CashInstrumentsIDs.length() > 0){
	WhereCashInstruments = " and  account_id in ("+CashInstrumentsIDs+") ";	
}

//


String WarehouseIDs="";
String WarehouseIDs1="";
           long SelectedWarehouseArray[] = null;
           long SelectedWarehouseArray1[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
           	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
           	
           	//check for scope warehouse
           	
           	UserAccess u = new UserAccess();
           	Warehouse[] WarehouseList = u.getUserFeatureWarehouse(SessionUserID,FeatureID);
           			           			
           	
            WarehouseIDs1 = u.getWarehousesQueryString(WarehouseList); 
           	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray1);
           
           }else{
        	   //else getting scope warehouse
        	   UserAccess u = new UserAccess();
               Warehouse[] WarehouseList = u.getUserFeatureWarehouse(SessionUserID,FeatureID);
        	   WarehouseIDs = u.getWarehousesQueryString(WarehouseList);  
           }
           
           
           String WhereWarehouse = "";
           String WhereWarehouse1 = "";
           if (WarehouseIDs.length() > 0){
           	WhereWarehouse = " and glcr.warehouse_id in ("+WarehouseIDs+") ";	
           }
           if (WarehouseIDs1.length() > 0){
              	WhereWarehouse1 = " and glcr.warehouse_id in ("+WarehouseIDs1+") ";	
              }
           
           
         //Gl Employee


           String GlEmployeeIDs="";
           long SelectedGlEmployeeArray[] = null;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedGlEmployee") != null){
           	SelectedGlEmployeeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGlEmployee");
           	GlEmployeeIDs = Utilities.serializeForSQL(SelectedGlEmployeeArray);
           }

           String WhereGlEmployee = "";
           if (GlEmployeeIDs.length() > 0){
           	WhereGlEmployee = " and glcr.created_by="+GlEmployeeIDs;	
           }        
           
           
         //customer filter

           long DistributorID1 = 0;
           if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
           	DistributorID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
           }
           String WhereCustomerID ="";

           if (DistributorID1 != 0){
           	WhereCustomerID = " and customer_id in ("+DistributorID1+") ";

           }




        
           
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<br/>
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				<tr style="font-size:11px;">
					<th style="b1ackground-color:#fafaf8;"></th>
					<th style="b1ackground-color:#fafaf8;"></th>
					<th style="b1ackground-color:#fafaf8;"></th>
					<th style="b1ackground-color:#fafaf8;"></th>
					<th style="b1ackground-color:#fafaf8;"></th>
					<th style="b1ackground-color:#fafaf8;"></th>
					
					<th style="b1ackground-color:#fafaf8; text-align:center;" colspan="3">FTD</th>
					<th style="b1ackground-color:#fafaf8; text-align:center;" colspan="3">YTD</th>
				</tr>
				<tr style="font-size:11px;">
					<th style="b1ackground-color:#fafaf8;">Region</th>
					<th style="b1ackground-color:#fafaf8;">UM</th>
					<th style="b1ackground-color:#fafaf8;">MDE</th>
					<th style="b1ackground-color:#fafaf8;">Start Time</th>
					<th style="b1ackground-color:#fafaf8;">End Time</th>
					<th style="b1ackground-color:#fafaf8;">Duration</th>
					
					<th style="b1ackground-color:#fafaf8;">Target</th>
					<th style="b1ackground-color:#fafaf8;">Achieved</th>
					<th style="b1ackground-color:#fafaf8;">Percentage</th>
					
					<th style="b1ackground-color:#fafaf8;">Target</th>
					<th style="b1ackground-color:#fafaf8;">Achieved</th>
					<th style="b1ackground-color:#fafaf8;">Percentage</th>
				</tr>
				
				<%
				
				int RegionID=0;
				String RegionName="";
				Date StartDateTime=new Date();
				Date EndDateTime=new Date();
				
				//YTD Date
				Date StartDateYTD = Utilities.parseDate("18/12/2016");				
				Date EndDateYTD = EndDate;
				
				
				
				//System.out.println("SELECT mc.created_by,(select display_name from users u where u.id=mc.created_by) created_name, (select region_id from common_distributors cd where cd.distributor_id=mc.census_distributor_id) region_id ,count(id) nshop FROM mrd_census mc where mc.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by mc.created_by");
				long tdmid=-1;
				long tdmid1=-2;
				int counter=0;
				
				
				//System.out.println("SELECT distinct (SELECT cd.tdm_id FROM pep.common_distributors cd where cd.distributor_id=mc.census_distributor_Id) tdm,(select display_name from users u where id=(SELECT cd.tdm_id FROM pep.common_distributors cd where cd.distributor_id=mc.census_distributor_Id)) um FROM mrd_census mc where mc.created_on between "+Utilities.getSQLDate(StartDateYTD)+" and "+Utilities.getSQLDateNext(EndDateYTD)+" order by tdm");
				
				ResultSet rs23 = s4.executeQuery("SELECT distinct (SELECT cd.tdm_id FROM pep.common_distributors cd where cd.distributor_id=mc.census_distributor_Id) tdm,(select display_name from users u where id=(SELECT cd.tdm_id FROM pep.common_distributors cd where cd.distributor_id=mc.census_distributor_Id)) um FROM mrd_census mc where mc.mobile_timestamp between "+Utilities.getSQLDate(StartDateYTD)+" and "+Utilities.getSQLDateNext(EndDateYTD)+" order by tdm");
				while(rs23.next()){
					double UMTotalFTDTarget=0;
					double UMTotalFTDAchieved=0;
					double UMTotalFTDPage=0;
					
					double UMTotalYTDTarget=0;
					double UMTotalYTDAchieved=0;
					double UMTotalYTDPage=0;
				
				
					ResultSet rs = s.executeQuery("SELECT census_distributor_id,(SELECT cd.tdm_id FROM pep.common_distributors cd where cd.distributor_id=mc.census_distributor_Id) tdm,(select display_name from users u where id=(SELECT cd.tdm_id FROM pep.common_distributors cd where cd.distributor_id=mc.census_distributor_Id)) um,mc.created_by,(select display_name from users u where u.id=mc.created_by) mde, (select region_id from common_distributors cd where cd.distributor_id=mc.census_distributor_id) region_id ,count(id) nshop FROM mrd_census mc where mc.mobile_timestamp between "+Utilities.getSQLDate(StartDateYTD)+" and "+Utilities.getSQLDateNext(EndDateYTD)+" and mc.census_distributor_id in (select distributor_id from common_distributors where tdm_id = "+rs23.getLong("tdm")+") and mc.created_by !=0 group by mc.created_by order by tdm");
					while(rs.next()){
						
						
						
						
						ResultSet rs1 = s2.executeQuery("select region_short_name from common_regions cr where cr.region_id="+rs.getLong("region_id"));
						if(rs1.first()){
							RegionName = rs1.getString("region_short_name");
						}
						
						double Duration =0;
						
						ResultSet rs2 = s2.executeQuery("select min(mobile_timestamp) start_time , max(mobile_timestamp) end_time, ((time_to_sec(time(max(mobile_timestamp)))-time_to_sec(time(min(mobile_timestamp))))/60/60) duration from mrd_census where created_by = "+rs.getLong("created_by")+" and mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+"");
						if(rs2.first()){
							StartDateTime = rs2.getTimestamp("start_time");
							EndDateTime = rs2.getTimestamp("end_time");
							Duration = rs2.getDouble("duration");
						}
						
						double FTDAchieved=0;
						
						ResultSet rsFTD = s3.executeQuery("SELECT  count(id) nshopftd FROM mrd_census mc where mc.mobile_timestamp between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and mc.created_by="+rs.getLong("created_by"));
						while(rsFTD.next()){
							FTDAchieved = rsFTD.getLong("nshopftd");
						}
						
						
						
						
						
						double FTDPercentage = (FTDAchieved/15)*100;
						
						//YTD
						
						
						
						
						
						int TotalDaysYTD = (int) ((EndDateYTD.getTime() - StartDateYTD.getTime()) / (1000 * 60 * 60 * 24)); // returns date in mili sec , so we are converting it into hours by diving 1000*60*60*24
						
						
						
						
						
						//Calculating Number of Fridays
						Date CurrentLoopDate = StartDateYTD;
						int NumberOfFridays=0;
						
						
						while(true){
							if(Utilities.getDayOfWeekByDate(CurrentLoopDate)==6){ //if Friday
								NumberOfFridays++;
							}
							
							if(DateUtils.isSameDay(CurrentLoopDate, EndDate)){
								break;
							}
	
							CurrentLoopDate = Utilities.getDateByDays(CurrentLoopDate, 1);
						}
						
						double YTDTarget = (15)*(TotalDaysYTD-NumberOfFridays); //15* number of days - Fridays
						double YTDAchieved=rs.getLong("nshop");
						
						
						
						
						double YTDPercentage = (YTDAchieved/YTDTarget)*100;
						
						
				        
				        
				        
				        
				        
					%>
					<tr>
						
						<td style="b1ackground-color:#f7fff7;"><%=RegionName %></td>
						<td style="b1ackground-color:#f7fff7;"><%if(rs23.getLong("tdm")!=0){%><%=rs23.getLong("tdm") %> - <%=rs23.getString("um") %><%} %></td>
						<td><%=rs.getLong("created_by") %><%if(rs.getLong("created_by")!=0){%> - <%=rs.getString("mde") %><%} %></td>
						<td style="b1ackground-color:#f7fff7;"><%if(StartDateTime!=null){%><%=Utilities.getDisplayTimeFormat(StartDateTime) %><%} %></td>
						<td style="b1ackground-color:#f7fff7;"><%if(EndDateTime!=null){%><%=Utilities.getDisplayTimeFormat(EndDateTime) %><%} %></td>
						<td style="text-align:center;"><%if(Duration!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(Duration) %><%} %></td>
						<td style="text-align:center;">15</td>
						<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatRounded(FTDAchieved) %></td>
						<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatRounded(FTDPercentage) %>%</td>
						<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatRounded(YTDTarget) %></td>
						<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatRounded(YTDAchieved) %></td>
						<td style="text-align:center;"><%=Utilities.getDisplayCurrencyFormatRounded(YTDPercentage) %>%</td>
						
					</tr>
				<%
				UMTotalFTDTarget =UMTotalFTDTarget+15;
				UMTotalFTDAchieved += FTDAchieved;				
				UMTotalFTDPage = (UMTotalFTDAchieved/UMTotalFTDTarget)*100;
				
				UMTotalYTDTarget +=YTDTarget;
				UMTotalYTDAchieved += YTDAchieved;				
				UMTotalYTDPage = (UMTotalYTDAchieved/UMTotalYTDTarget)*100;
				
					}
					
					if(rs23.getLong("tdm")!=0 && rs.first()){
					
					
					%>
					<tr>
						
						<td style="background-color:#f7fff7;"></td>
						<td style="background-color:#f7fff7;"><b>Total</b></td>
						<td style="background-color:#f7fff7;"></td>
						<td style="background-color:#f7fff7;"></td>
						<td style="background-color:#f7fff7;"></td>
						<td style="background-color:#f7fff7; text-align:center;"></td>
						<td style="background-color:#f7fff7; text-align:center;"><b><%=Utilities.getDisplayCurrencyFormatRounded(UMTotalFTDTarget) %></b></td>
						<td style="background-color:#f7fff7; text-align:center;"><b><%=Utilities.getDisplayCurrencyFormatRounded(UMTotalFTDAchieved) %></b></td>
						<td style="background-color:#f7fff7; text-align:center;"><b><%=Utilities.getDisplayCurrencyFormatRounded(UMTotalFTDPage) %>%</b></td>
						<td style="background-color:#f7fff7; text-align:center;"><b><%=Utilities.getDisplayCurrencyFormatRounded(UMTotalYTDTarget) %></b></td>
						<td style="background-color:#f7fff7; text-align:center;"><b><%=Utilities.getDisplayCurrencyFormatRounded(UMTotalYTDAchieved) %></b></td>
						<td style="background-color:#f7fff7; text-align:center;"><b><%=Utilities.getDisplayCurrencyFormatRounded(UMTotalYTDPage) %>%</b></td>
						
					</tr>
					
					
				<%	
					}//end of if
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