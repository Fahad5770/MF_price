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
int FeatureID = 357;

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
Statement s9 = c.createStatement();
Statement s10 = c.createStatement();
Statement s11 = c.createStatement();
Statement s12 = c.createStatement();

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
	WherePJP = " and isa.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
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
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and isa.distributor_id in ("+DistributorIDs+") ";
	
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
	OrderBookerIDsWhere =" and order_no in (select mobile_order_no from mobile_order_unedited mou where mou.created_by in ("+OrderBookerIDs+"))";
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
	WhereOutlets = " and isa.outlet_id in ("+OutletIds+") ";	
	//System.out.println(WhereOutlets);
}

//Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
//String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Outlet Sales Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <%
					 System.out.println("Hello "+WhereDistributors);
					 
					 if(WhereDistributors!=""){ %>
					 
					 <thead>
					 	<tr style="font-size:11px;">
							
							<th  data-priority="1"  style="text-align:center; " colspan="3"></th>
							<th   data-priority="1"  style="text-align:center; " colspan="12">BEETA</th>
							
							<th   data-priority="1"  style="text-align:center; " colspan="20">SUGAR</th>
							
							<th   data-priority="1"  style="text-align:center; " colspan="12">Gur</th>
							
							
							
					    </tr>
					 	
					 	<tr style="font-size:11px;">
							
							<th  data-priority="1"  style="text-align:center; " colspan="3"></th>
							<th   data-priority="1"  style="text-align:center; " colspan="4">250G</th>
							<th  data-priority="1"  style="text-align:center; " colspan="4">500G</th>
							<th   data-priority="1"  style="text-align:center; " colspan="4">25G</th>
							<th   data-priority="1"  style="text-align:center; " colspan="4">1 KG</th>
							<th   data-priority="1"  style="text-align:center; " colspan="4">2 KG</th>
							<th   data-priority="1"  style="text-align:center; " colspan="4">5 KG</th>
							<th   data-priority="1"  style="text-align:center; " colspan="4">70 G</th>
							<th   data-priority="1"  style="text-align:center; " colspan="4">140 G</th>
							
							<th   data-priority="1"  style="text-align:center; " colspan="4">Jar</th>
							<th   data-priority="1"  style="text-align:center; " colspan="4">Box</th>
							<th   data-priority="1"  style="text-align:center; " colspan="4">Box 450G</th>
					    </tr>
					  </thead> 
					  
					<tbody>
					
					<%
					Date TodaysDate=new Date();
					StartDate =  Utilities.getStartDateByDate(TodaysDate);
				 	EndDate = TodaysDate;	
				 
				 	int month = Utilities.getMonthNumberByDate(EndDate);
					int year = Utilities.getYearByDate(EndDate);
					
					
					
					//For Getting Dates of present and 3 previous months
					Date StartDateLastMonth =(Utilities.getStartDateByMonth(month-1, year));
					Date EndDateLastMonth =(Utilities.getEndDateByMonth(month-1, year));
					int yearr1 =Utilities.getYearByDate(StartDateLastMonth);
										
					Date StartDateLastMonth2 =(Utilities.getStartDateByMonth(month-2, year));
					Date EndDateLastMonth2 =(Utilities.getEndDateByMonth(month-2, year));
					int yearr2 =Utilities.getYearByDate(StartDateLastMonth2);
					
					Date StartDateLastMonth3 =(Utilities.getStartDateByMonth(month-3, year));
					Date EndDateLastMonth3 =(Utilities.getEndDateByMonth(month-3, year));
					int yearr3 =Utilities.getYearByDate(StartDateLastMonth3);
					
					
					Date StartDateLastMonth4 = (Utilities.getStartDateByMonth(month-4, year));
					Date EndDateLastMonth4 =(Utilities.getEndDateByMonth(month-4, year));
					int yearr4 =Utilities.getYearByDate(StartDateLastMonth4);
					
					
					//For Getting Month's Name of present and 3 previous months
					String MonthName=Utilities.getMonthNameByNumber(month);
					
					MonthName = MonthName.substring(0,3);
	           	  
					int month2=Utilities.getMonthNumberByDate((Utilities.getStartDateByMonth(month-2, year)));
					String MonthName2=Utilities.getMonthNameByNumber(month2);
					MonthName2 = MonthName2.substring(0,3);
					
					int month3=Utilities.getMonthNumberByDate((Utilities.getStartDateByMonth(month-3, year)));
					String MonthName3=Utilities.getMonthNameByNumber(month3);
					MonthName3 = MonthName3.substring(0,3);
					
					int month4=Utilities.getMonthNumberByDate((Utilities.getStartDateByMonth(month-4, year)));
					String MonthName4=Utilities.getMonthNameByNumber(month4);
					MonthName4 = MonthName4.substring(0,3);
					
					%>
					<tr style="font-size:11px;">
							<th  data-priority="1"  style="text-align:center; ">Outlet </th>
							<th  data-priority="1"  style="text-align:center; ">Channel</th>
							<th  data-priority="1"  style="text-align:center; ">Order Booker </th>
							
								
								<th style="background-color:#f0f2f3"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#f0f2f3" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#f0f2f3" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#f0f2f3" ><%=MonthName %>-<%=yearr1 %></th>
								
								
								<th style="background-color:#f0f2f3"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#f0f2f3" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#f0f2f3" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#f0f2f3" ><%=MonthName %>-<%=yearr1 %></th>
								
								
								<th style="background-color:#f0f2f3"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName %>-<%=yearr1 %></th>
								
								
								<th style="background-color:#f2fee9"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName %>-<%=yearr1 %></th>
								
								<th style="background-color:#f2fee9"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName %>-<%=yearr1 %></th>
								
								<th style="background-color:#f2fee9"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName %>-<%=yearr1 %></th>
								
								<th style="background-color:#f2fee9"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName %>-<%=yearr1 %></th>
								
								<th style="background-color:#f2fee9"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#f2fee9" ><%=MonthName %>-<%=yearr1 %></th>
								
								
								<th style="background-color:#fff9fe"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName %>-<%=yearr1 %></th>
								
								<th style="background-color:#fff9fe"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName %>-<%=yearr1 %></th>
								
								<th style="background-color:#fff9fe"><%=MonthName4 %>-<%=yearr4 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName3 %>-<%=yearr3 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName2 %>-<%=yearr2 %></th>
								<th style="background-color:#fff9fe" ><%=MonthName %>-<%=yearr1 %></th>
						</tr>
						
					<%
					long OutletID=0;
					String OutletName="";
					Date CreatedOn=null;
					String Channel="";
					
					double TotalArray[]=new double[44];
												 
					//ResultSet rs1=s.executeQuery("Select co.id,co.name,co.pic_channel_id,(select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id) channel from common_outlets co where co.id in (select distinct isa.outlet_id from inventory_sales_adjusted isa where isa.outlet_id=co.id and isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDate)+WherePJP+")"+WhereDistributors+" order by co.id desc" );
					ResultSet rs1=s7.executeQuery("SELECT isa.outlet_id as id,(select name from common_outlets co where co.id=isa.outlet_id ) name,sum(isap.raw_cases),isa.created_on,isa.booked_by assigned_to,(SELECT DISPLAY_NAME FROM users where id=isa.booked_by) orderbooker_name,(select pic_channel_id from common_outlets co where co.id=isa.outlet_id ) channel_id,(select psc.label from pci_sub_channel psc where psc.id=channel_id) channel FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id   where  isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDate)+WherePJP+WhereDistributors+" and booked_by!=0 group by isa.outlet_id order by sum(isap.raw_cases) desc" );
					
					while(rs1.next()){

						OutletID=rs1.getLong("id");
						OutletName=rs1.getString("name");
						Channel=rs1.getString("channel");
						if(Channel==null){
							Channel="";
						}
						String BookedBy="";
						
						
						 //Using date of all four months 
					      ResultSet rs11=s2.executeQuery("SELECT booked_by,(select u.DISPLAY_NAME from users u where u.id=booked_by) booker_name FROM inventory_sales_adjusted isa  where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDate(EndDateLastMonth)+" and isa.outlet_id="+OutletID+"  order by isa.id desc");
					      if(rs11.first()){
					       BookedBy=rs11.getString(1)+" - "+rs11.getString(2);
					       
					       if(rs11.getString(1)==null){
					    	   BookedBy="";
							}
					       
					      }
					      
					      
						
						
						
						
						
						double Units_250G=0;
						double Units_250G2=0;
						double Units_250G3=0;
						double Units_250G4=0;
						
						
						ResultSet rs24=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id in (1,4,5,8,9)  order by isa.id desc");
						if(rs24.first()){
							Units_250G4 = rs24.getDouble(1);
						}
						
						
						ResultSet rs23=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id in (1,4,5,8,9) order by isa.id desc");
						if(rs23.first()){
							Units_250G3 = rs23.getDouble(1);
						}
						
						
						ResultSet rs22=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id in (1,4,5,8,9)  order by isa.id desc");
						if(rs22.first()){
							Units_250G2 = rs22.getDouble(1);
						}
						
						ResultSet rs21=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id in (1,4,5,8,9)  order by isa.id desc");
						if(rs21.first()){
							Units_250G = rs21.getDouble(1);
						}
												
						
						
						
						
						TotalArray[0]+=Units_250G4;
						TotalArray[1]+=Units_250G3;
						TotalArray[2]+=Units_250G2;
						TotalArray[3]+=Units_250G;
						
					
					
						double Units_500G4=0;
						double Units_500G3=0;
						double Units_500G2=0;
						double Units_500G=0;
						
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDate(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=2  order by isa.id desc");
						ResultSet rs244=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id in (2,6,7)  order by isa.id desc");
						if(rs244.first()){
							Units_500G4 = rs244.getDouble(1);
						}
						
					//	System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDate(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=2  order by isa.id desc");
						ResultSet rs233=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id in (2,6,7)  order by isa.id desc");
						if(rs233.first()){
							Units_500G3 = rs233.getDouble(1);
						}
						
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDate(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=2  order by isa.id desc");
						ResultSet rs222=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id in (2,6,7)  order by isa.id desc");
						if(rs222.first()){
							Units_500G2 = rs222.getDouble(1);
						}
						
					//	System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDate(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=2  order by isa.id desc");
						ResultSet rs211=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id in (2,6,7)  order by isa.id desc");
						if(rs211.first()){
							Units_500G = rs211.getDouble(1);
						}
						
						
						
						
						
					//
						
						
						
						
						TotalArray[4]+=Units_500G4;
						TotalArray[5]+=Units_500G3;
						TotalArray[6]+=Units_500G2;
						TotalArray[7]+=Units_500G;
						
						
						
						double Units_25G4=0;
						double Units_25G3=0;
						double Units_25G2=0;
						double Units_25G=0;
						
						ResultSet rs33=s2.executeQuery("SELECT sum(total_units/24) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=3  order by isa.id desc");
						if(rs33.first()){
							Units_25G4 = rs33.getDouble(1);
							
							
						}
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDate(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=3  order by isa.id desc");
						ResultSet rs32=s2.executeQuery("SELECT sum(total_units/24) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=3  order by isa.id desc");
						if(rs32.first()){
							Units_25G3 = rs32.getDouble(1);
						}
						
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDate(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=3 order by isa.id desc");
						ResultSet rs31=s2.executeQuery("SELECT sum(total_units/24) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=3  order by isa.id desc");
						if(rs31.first()){
							Units_25G2 = rs31.getDouble(1);
						}
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDate(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=3 order by isa.id desc");
						ResultSet rs3=s2.executeQuery("SELECT sum(total_units/24) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=3  order by isa.id desc");
						if(rs3.first()){
							Units_25G = rs3.getDouble(1);
						}
						
						
						
						
						
						TotalArray[8]+=Units_25G4;
						TotalArray[9]+=Units_25G3;
						TotalArray[10]+=Units_25G2;
						TotalArray[11]+=Units_25G;
						
						//1KG
						
						double Units_1KG4=0;
						double Units_1KG3=0;
						double Units_1KG2=0;
						double Units_1KG=0;
						
						ResultSet rs34=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=10  order by isa.id desc");
						if(rs34.first()){
							Units_1KG4 = rs34.getDouble(1);
							
							
						}
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDate(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=3  order by isa.id desc");
						ResultSet rs35=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=10  order by isa.id desc");
						if(rs35.first()){
							Units_1KG3 = rs35.getDouble(1);
						}
						
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDate(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=3 order by isa.id desc");
						ResultSet rs36=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=10  order by isa.id desc");
						if(rs36.first()){
							Units_1KG2 = rs36.getDouble(1);
						}
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDate(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=3 order by isa.id desc");
						ResultSet rs37=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=10  order by isa.id desc");
						if(rs37.first()){
							Units_1KG = rs37.getDouble(1);
						}	
						
						TotalArray[12]+=Units_1KG4;
						TotalArray[13]+=Units_1KG3;
						TotalArray[14]+=Units_1KG2;
						TotalArray[15]+=Units_1KG;
						
						
						
						//2 KG
						

						
						double Units_2KG4=0;
						double Units_2KG3=0;
						double Units_2KG2=0;
						double Units_2KG=0;
						
						ResultSet rs38=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=11  order by isa.id desc");
						if(rs38.first()){
							Units_2KG4 = rs38.getDouble(1);
							
							
						}
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDate(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=3  order by isa.id desc");
						ResultSet rs39=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=11  order by isa.id desc");
						if(rs39.first()){
							Units_2KG3 = rs39.getDouble(1);
						}
						
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDate(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=3 order by isa.id desc");
						ResultSet rs40=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=11  order by isa.id desc");
						if(rs40.first()){
							Units_2KG2 = rs40.getDouble(1);
						}
						
						//System.out.println("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDate(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=3 order by isa.id desc");
						ResultSet rs41=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=11  order by isa.id desc");
						if(rs41.first()){
							Units_2KG = rs41.getDouble(1);
						}	
						
						TotalArray[16]+=Units_2KG4;
						TotalArray[17]+=Units_2KG3;
						TotalArray[18]+=Units_2KG2;
						TotalArray[19]+=Units_2KG;
						
					/*Script for 5 KG Starts*/
						
						double Units_5KG4=0;
						double Units_5KG3=0;
						double Units_5KG2=0;
						double Units_5KG=0;
						
						ResultSet rs335KG=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=12  order by isa.id desc");
						if(rs335KG.first()){
							Units_5KG4 = rs335KG.getDouble(1);
							
							
						}
						
						ResultSet rs325KG=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=12  order by isa.id desc");
						if(rs325KG.first()){
							Units_5KG3 = rs325KG.getDouble(1);
						}
						
						
						ResultSet rs315KG=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=12  order by isa.id desc");
						if(rs315KG.first()){
							Units_5KG2 = rs315KG.getDouble(1);
						}
						
						ResultSet rs35KG=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=12  order by isa.id desc");
						if(rs35KG.first()){
							Units_5KG = rs35KG.getDouble(1);
						}
						
						
						
							/*Script for 70G Starts*/
						
						double Units_70G4=0;
						double Units_70G3=0;
						double Units_70G2=0;
						double Units_70G=0;
						
						ResultSet rs335G=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=15  order by isa.id desc");
						if(rs335G.first()){
							Units_70G4 = rs335G.getDouble(1);
							
							
						}
						
						ResultSet rs325G=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=15  order by isa.id desc");
						if(rs325G.first()){
							Units_70G3 = rs325G.getDouble(1);
						}
						
						
						ResultSet rs315G=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=15  order by isa.id desc");
						if(rs315G.first()){
							Units_70G2 = rs315G.getDouble(1);
						}
						
						ResultSet rs35G=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=15  order by isa.id desc");
						if(rs35G.first()){
							Units_70G = rs35G.getDouble(1);
						}
						
						
						
						TotalArray[24]+=Units_70G4;
						TotalArray[25]+=Units_70G3;
						TotalArray[26]+=Units_70G2;
						TotalArray[27]+=Units_70G;
						
						/*Script for 70G KG Ends*/
						
						
						

					/*Script for 140G Starts*/
					
					double Units_140G4=0;
					double Units_140G3=0;
					double Units_140G2=0;
					double Units_140G=0;
					
					ResultSet rs3351G=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=16  order by isa.id desc");
					if(rs3351G.first()){
						Units_140G4 = rs3351G.getDouble(1);
						
						
					}
					
					ResultSet rs3251G=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=16  order by isa.id desc");
					if(rs3251G.first()){
						Units_140G3 = rs3251G.getDouble(1);
					}
					
					
					ResultSet rs3151G=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=16  order by isa.id desc");
					if(rs3151G.first()){
						Units_140G2 = rs3151G.getDouble(1);
					}
					
					ResultSet rs351G=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=16  order by isa.id desc");
					if(rs351G.first()){
						Units_140G = rs351G.getDouble(1);
					}
					
					
					
					TotalArray[28]+=Units_140G4;
					TotalArray[29]+=Units_140G3;
					TotalArray[30]+=Units_140G2;
					TotalArray[31]+=Units_140G;
					
					/*Script for 140G KG Ends*/
					
					
					

					/*Script for Gur Jar Starts*/
					
					double Units_GurG4=0;
					double Units_GurG3=0;
					double Units_GurG2=0;
					double Units_GurG=0;
					
					ResultSet rs3351Gur=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=13  order by isa.id desc");
					if(rs3351Gur.first()){
						Units_GurG4 = rs3351Gur.getDouble(1);
						
						
					}
					
					ResultSet rs3251Gur=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=13  order by isa.id desc");
					if(rs3251Gur.first()){
						Units_GurG3 = rs3251Gur.getDouble(1);
					}
					
					
					ResultSet rs3151Gur=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=13  order by isa.id desc");
					if(rs3151Gur.first()){
						Units_GurG2 = rs3151Gur.getDouble(1);
					}
					
					ResultSet rs351Gur=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=13  order by isa.id desc");
					if(rs351Gur.first()){
						Units_GurG = rs351Gur.getDouble(1);
					}
					
					
					
					TotalArray[32]+=Units_GurG4;
					TotalArray[33]+=Units_GurG3;
					TotalArray[34]+=Units_GurG2;
					TotalArray[35]+=Units_GurG;
					
					/*Script for Gur Jar Ends*/
					
					
					
					/*Script for Gur box Starts*/
					
					double Units_GurBG4=0;
					double Units_GurBG3=0;
					double Units_GurBG2=0;
					double Units_GurBG=0;
					
					ResultSet rs3351GurB=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=14  order by isa.id desc");
					if(rs3351GurB.first()){
						Units_GurBG4 = rs3351GurB.getDouble(1);
						
						
					}
					
					ResultSet rs3251GurB=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=14  order by isa.id desc");
					if(rs3251GurB.first()){
						Units_GurBG3 = rs3251GurB.getDouble(1);
					}
					
					
					ResultSet rs3151GurB=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=14  order by isa.id desc");
					if(rs3151GurB.first()){
						Units_GurBG2 = rs3151GurB.getDouble(1);
					}
					
					ResultSet rs351GurB=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=14  order by isa.id desc");
					if(rs351GurB.first()){
						Units_GurBG = rs351GurB.getDouble(1);
					}
					
					
					
					TotalArray[36]+=Units_GurBG4;
					TotalArray[37]+=Units_GurBG3;
					TotalArray[38]+=Units_GurBG2;
					TotalArray[39]+=Units_GurBG;
					
					/*Script for Gur Box Ends*/
					
					
						/*Script for Gur 450 box Starts*/
					
					double Units_Gur450BG4=0;
					double Units_Gur450BG3=0;
					double Units_Gur450BG2=0;
					double Units_Gur450BG=0;
					
					ResultSet rs3351Gur450B=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth4)+" and "+Utilities.getSQLDateNext(EndDateLastMonth4)+" and isa.outlet_id="+OutletID+" and isap.product_id=17  order by isa.id desc");
					if(rs3351Gur450B.first()){
						Units_Gur450BG4 = rs3351Gur450B.getDouble(1);
						
						
					}
					
					ResultSet rs3251Gur450B=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth3)+" and "+Utilities.getSQLDateNext(EndDateLastMonth3)+" and isa.outlet_id="+OutletID+" and isap.product_id=17  order by isa.id desc");
					if(rs3251Gur450B.first()){
						Units_Gur450BG3 = rs3251Gur450B.getDouble(1);
					}
					
					
					ResultSet rs3151Gur450B=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth2)+" and "+Utilities.getSQLDateNext(EndDateLastMonth2)+" and isa.outlet_id="+OutletID+" and isap.product_id=17  order by isa.id desc");
					if(rs3151Gur450B.first()){
						Units_Gur450BG2 = rs3151Gur450B.getDouble(1);
					}
					
					ResultSet rs351Gur450B=s2.executeQuery("SELECT sum(total_units) FROM inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDateLastMonth)+" and "+Utilities.getSQLDateNext(EndDateLastMonth)+" and isa.outlet_id="+OutletID+" and isap.product_id=17  order by isa.id desc");
					if(rs351Gur450B.first()){
						Units_Gur450BG = rs351Gur450B.getDouble(1);
					}
					
					
					
					TotalArray[40]+=Units_Gur450BG4;
					TotalArray[40]+=Units_Gur450BG3;
					TotalArray[42]+=Units_Gur450BG2;
					TotalArray[43]+=Units_Gur450BG;
					
					/*Script for Gur 450 Box Ends*/
					
					%>
					
						
						
						<tr>
						
							<td><%=OutletID %> - <%=OutletName %></td>
							<td><%=Channel %></td>
							<td><%=BookedBy %></td>
							
							<td  style="background-color:#f0f2f3"><%if(Units_250G4!=0.0 && Units_250G4!=0){%><%=Units_250G4 %> <%} %></td>
							<td style="background-color:#f0f2f3"><%if(Units_250G3!=0.0 && Units_250G3!=0){%><%=Units_250G3 %> <%} %></td>
							<td style="background-color:#f0f2f3"> <%if(Units_250G2!=0.0 && Units_250G2!=0){%><%=Units_250G2 %> <%} %></td>
							<td style="background-color:#f0f2f3"> <%if(Units_250G!=0.0 && Units_250G!=0){%><%=Units_250G %> <%} %></td>
						
							<td style="background-color:#f0f2f3"><%if(Units_500G4!=0.0 && Units_500G4!=0){%><%=Units_500G4 %> <%} %></td>
							<td style="background-color:#f0f2f3"><%if(Units_500G3!=0.0 && Units_500G3!=0){%><%=Units_500G3 %> <%} %></td>	
							<td style="background-color:#f0f2f3"><%if(Units_500G2!=0.0 && Units_500G2!=0){%><%=Units_500G2 %> <%} %></td>
							<td style="background-color:#f0f2f3"><%if(Units_500G!=0.0 && Units_500G!=0){%><%=Units_500G %> <%} %></td>
						
							
							<td style="background-color:#f0f2f3"><%if(Units_25G4!=0.0 && Units_25G4!=0){%><%=Units_25G4 %> <%} %></td>
							<td style="background-color:#f0f2f3"><%if(Units_25G3!=0.0 && Units_25G3!=0){%><%=Units_25G3 %> <%} %></td>
							<td style="background-color:#f0f2f3"><%if(Units_25G2!=0.0 && Units_25G2!=0){%><%=Units_25G2 %> <%} %></td>
							<td style="background-color:#f0f2f3"><%if(Units_25G!=0.0 && Units_25G!=0){%><%=Units_25G %> <%} %></td>
						
							<!-- Script for 1 KG Starts  -->
							<td style="background-color:#f2fee9"><%if(Units_1KG4!=0.0 && Units_1KG4!=0){%><%=Units_1KG4 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_1KG3!=0.0 && Units_1KG3!=0){%><%=Units_1KG3 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_1KG2!=0.0 && Units_1KG2!=0){%><%=Units_1KG2 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_1KG!=0.0 && Units_1KG!=0){%><%=Units_1KG %> <%} %></td>
							<!-- Script for 1 KG Ends  -->
							
							
							<!-- Script for 2 KG Starts  -->
							<td style="background-color:#f2fee9"><%if(Units_2KG4!=0.0 && Units_2KG4!=0){%><%=Units_2KG4 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_2KG3!=0.0 && Units_2KG3!=0){%><%=Units_2KG3 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_2KG2!=0.0 && Units_2KG2!=0){%><%=Units_2KG2 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_2KG!=0.0 && Units_2KG!=0){%><%=Units_2KG %> <%} %></td>
							<!-- Script for 2 KG Ends  -->
							
							
							
							<!-- Script for 5 KG Starts  -->
							<td style="background-color:#f2fee9"><%if(Units_5KG4!=0.0 && Units_5KG4!=0){%><%=Units_5KG4 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_5KG3!=0.0 && Units_5KG3!=0){%><%=Units_5KG3 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_5KG2!=0.0 && Units_5KG2!=0){%><%=Units_5KG2 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_5KG!=0.0 && Units_5KG!=0){%><%=Units_5KG %> <%} %></td>
							<!-- Script for 5 KG Ends  -->
							
							
							<!-- Script for70G Starts  -->
							<td style="background-color:#f2fee9"><%if(Units_70G4!=0.0 && Units_70G4!=0){%><%=Units_70G4 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_70G3!=0.0 && Units_70G3!=0){%><%=Units_70G3 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_70G2!=0.0 && Units_70G2!=0){%><%=Units_70G2 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_70G!=0.0 && Units_70G!=0){%><%=Units_70G %> <%} %></td>
							<!-- Script for70G Ends  -->
							
							<!-- Script for140G Starts  -->
							<td style="background-color:#f2fee9"><%if(Units_140G4!=0.0 && Units_140G4!=0){%><%=Units_140G4 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_140G3!=0.0 && Units_140G3!=0){%><%=Units_140G3 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_140G2!=0.0 && Units_140G2!=0){%><%=Units_140G2 %> <%} %></td>
							<td style="background-color:#f2fee9"><%if(Units_140G!=0.0 && Units_140G!=0){%><%=Units_140G %> <%} %></td>
							<!-- Script for 140GEnds  -->
							
							
							
							<!-- Script for Gur Jar Starts  -->
							<td style="background-color:#fff9fe"><%if(Units_GurG4!=0.0 && Units_GurG4!=0){%><%=Units_GurG4 %> <%} %></td>
							<td style="background-color:#fff9fe"><%if(Units_GurG3!=0.0 && Units_GurG3!=0){%><%=Units_GurG3 %> <%} %></td>
							<td style="background-color:#fff9fe"><%if(Units_GurG2!=0.0 && Units_GurG2!=0){%><%=Units_GurG2 %> <%} %></td>
							<td style="background-color:#fff9fe"><%if(Units_GurG!=0.0 && Units_GurG!=0){%><%=Units_GurG %> <%} %></td>
							<!-- Script for Gur Jar Ends  -->
							
							
							
							<!-- Script for Gur Box Starts  -->
							<td style="background-color:#fff9fe"><%if(Units_GurBG4!=0.0 && Units_GurBG4!=0){%><%=Units_GurBG4 %> <%} %></td>
							<td style="background-color:#fff9fe"><%if(Units_GurBG3!=0.0 && Units_GurBG3!=0){%><%=Units_GurBG3 %> <%} %></td>
							<td style="background-color:#fff9fe"><%if(Units_GurBG2!=0.0 && Units_GurBG2!=0){%><%=Units_GurBG2 %> <%} %></td>
							<td style="background-color:#fff9fe"><%if(Units_GurBG!=0.0 && Units_GurBG!=0){%><%=Units_GurBG %> <%} %></td>
							<!-- Script for Gur Box Ends  -->
							
							
							<!-- Script for Gur 450 Box Starts  -->
							<td style="background-color:#fff9fe"><%if(Units_Gur450BG4!=0.0 && Units_Gur450BG4!=0){%><%=Units_Gur450BG4 %> <%} %></td>
							<td style="background-color:#fff9fe"><%if(Units_Gur450BG3!=0.0 && Units_Gur450BG3!=0){%><%=Units_Gur450BG3 %> <%} %></td>
							<td style="background-color:#fff9fe"><%if(Units_Gur450BG2!=0.0 && Units_Gur450BG2!=0){%><%=Units_Gur450BG2 %> <%} %></td>
							<td style="background-color:#fff9fe"><%if(Units_Gur450BG!=0.0 && Units_Gur450BG!=0){%><%=Units_Gur450BG %> <%} %></td>
							<!-- Script for Gur Box Ends  -->
						</tr>
										
					
						<%
						}
												
						%>	
						<tr>
							<td></td>
							<td></td>
							
							<td ><b>Total</b></td>
							<%
							for(int i=0;i<TotalArray.length;i++){
								
								String ColorTheme="";
								if(i<=3){
									ColorTheme="style='background-color:#f0f2f3'";
								}else if(i>3 && i<=7){
									
									ColorTheme="style='background-color:#f2fee9'";
								}
								else if(i>7 && i<=11){
									
									ColorTheme="style='background-color:#fff9fe'";
								}
								
								if(i>=12 && i<=15){ //1 KG
									ColorTheme="style='background-color:#f0f2f3'";
								}else if(i>15 && i<=19){ //2KG
									
									ColorTheme="style='background-color:#f2fee9'";
								}
								else if(i>19){ //5KG
									
									ColorTheme="style='background-color:#fff9fe'";
								}	
							%>
							
							<td <%=ColorTheme %>><b><%=Utilities.getDisplayCurrencyFormatRounded(TotalArray[i]) %></b></td>
							<%
							}
							%>
						</tr>
									<%
}
else{
%><br/>
<p>Please select filter for Report.
<%} %>
						
						
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