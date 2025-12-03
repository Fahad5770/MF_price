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
int FeatureID = 355;

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
	WherePJP = " and isa.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
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
	WhereDistributors = " and co.distributor_id in ("+DistributorIDs+") ";
	
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
					 <thead>
					 	<%
					 	if(WherePJP!="" && WherePJP !=null){
					 	%>
					 	
					 	<tr style="font-size:11px;">
							<th  data-priority="1"  style="text-align:center; ">Outlet </th>
							<th  data-priority="1"  style="text-align:center; ">PSR</th>
							<th  data-priority="1"  style="text-align:center; ">500GM</th>
							<th   data-priority="1"  style="text-align:center; ">250Gm</th>
							<th   data-priority="1"  style="text-align:center; ">25GM</th>
							<th   data-priority="1"  style="text-align:center; ">Converted Sales</th>
							
					    </tr>
					   
					  </thead> 
					<tbody>
						
						<%
						
						
						
						long OutletID=0;
						String OutletName="";
						long PutOutletSeparator=0;
						String TrStyle="";
						
						//System.out.println("Select co.id,co.name from common_outlets co where co.id in (select isa.outlet_id from inventory_sales_adjusted isa where isa.outlet_id=co.id and isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereOutlets+WherePJP+")" );
						ResultSet rs=s.executeQuery("Select co.id,co.name from common_outlets co where co.id in (select isa.outlet_id from inventory_sales_adjusted isa where isa.outlet_id=co.id and isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereOutlets+WherePJP+")" );
						while(rs.next()){
							OutletID=rs.getLong("id");
							OutletName=rs.getString("name");
							long PSRID=0;
							String PSRLabel="";
							
							//System.out.println("SELECT assigned_to,(SELECT DISPLAY_NAME FROM users where id=assigned_to) orderbooker_name  FROM pep.distributor_beat_plan_users where id in (SELECT id FROM pep.distributor_beat_plan_schedule where outlet_id="+OutletID+")" );
							ResultSet rs1=s7.executeQuery("SELECT assigned_to,(SELECT DISPLAY_NAME FROM users where id=assigned_to) orderbooker_name  FROM pep.distributor_beat_plan_users where id in (SELECT id FROM pep.distributor_beat_plan_schedule where outlet_id="+OutletID+")" );
							while(rs1.next()){
								
								PSRID=rs1.getLong("assigned_to");
								PSRLabel=rs1.getString("orderbooker_name");
							
										double Units_250G=0;
										double Units_500G=0;
										double Units_25G=0;
										
										double ConvertedUnits500GM=0;
										double ConvertedUnits250GM=0;
										double ConvertedUnits25GM=0;
										
										double Converted=0;
										
										//ResultSet rs21=s2.executeQuery("SELECT sum(total_units*ipv.liquid_in_ml)/250 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletIds+" and isap.product_id=1 order by isa.id desc");
										ResultSet rs21=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=1  order by isa.id desc");
										if(rs21.first()){
											Units_250G = rs21.getDouble(1);
										}
										
										//ResultSet rs22=s2.executeQuery("SELECT sum(total_units*ipv.liquid_in_ml)/250 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(CurrentDate)+" and "+Utilities.getSQLDateNext(CurrentDate)+" and isa.outlet_id="+OutletIds+" and isap.product_id=2 order by isa.id desc");
										ResultSet rs22=s2.executeQuery("SELECT sum(total_units) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=2 order by isa.id desc");
										if(rs22.first()){
											Units_500G = rs22.getDouble(1);
										}
										
											
										// divide by 24 bec they count 24 pieces to 1 hanger
										
										ResultSet rs23=s2.executeQuery("SELECT sum(total_units/24) FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on ipv.product_id=isap.product_id where isa.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.outlet_id="+OutletID+" and isap.product_id=3 order by isa.id desc");
										if(rs23.first()){
											Units_25G = rs23.getDouble(1);
										}
										
										
										ConvertedUnits500GM = (500*Units_500G)/3000;
										
										
										
										
										ConvertedUnits250GM = (250*Units_250G)/3000;
										
										//System.out.println("hello "+ConvertedUnits250GM);
										
										ConvertedUnits25GM = (25*24*Units_25G)/3000;
										
									Converted = ConvertedUnits500GM+ConvertedUnits250GM+ConvertedUnits25GM;
									
									
								
										 %>
										 
								
									
									<tr style="font-size:11px;">
										
										<td style="text-align:left;"><%=OutletID%> - <%=OutletName%></td>
											<td style="text-align:left;"> <%=PSRID%> - <%=PSRLabel %> </td>
										<td style="text-align:center;"><%if(Units_500G!=0){%><%=Units_500G %><%} %></td>
										<td style="text-align:center;"><%if(Units_250G!=0){%><%=Units_250G %><%} %></td>
										<td style="text-align:center;"><%if(Units_25G!=0){%><%=Units_25G %><%} %></td>
										<td style="text-align:center;"><%if(Converted!=0){%><%=Converted %><%} %></td>
									</tr>
									<%
							
								%>
								
								
								
								
								
									
								<%
								
								
								
							}//End oF Main Result Set		
						}
						}//End of IF
					 	
					 	else{
						%>	
						<b>Please Select PJP First.</b>
						
						<%
						}
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