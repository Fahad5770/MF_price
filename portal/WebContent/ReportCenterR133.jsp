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
<%@page import="java.util.Calendar"%>
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
int FeatureID = 136;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

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
	//StartDate = new Date(); // add code of start of current month if first time report opens
	Calendar cc = Calendar.getInstance();   // this takes current date
    cc.set(Calendar.DAY_OF_MONTH, 1);
    StartDate = cc.getTime();
     
	
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

Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);



%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Sales versus Targets</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th colspan="3" style="text-align:center; ">Converted Cases</th>
							<%
							int PackageCount = 0;
							
							ResultSet rs = s2.executeQuery("SELECT distinct package_id, package_label, unit_per_sku FROM inventory_products_view where  product_id in (SELECT distinct idnp.product_id FROM inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id where idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+") "+WherePackage+ "order by package_sort_order");
							int PacakgeID=0;
							int DistributorID=0;
							double GrandTotal=0;
							while(rs.next()){
								
								PackageCount++;
							
							%>
							<th data-priority="1" colspan="3"  style="text-align:center; " >
							<%=rs.getString("package_label")%>							
							
							</th>
							
							<%
							}
							PackageCount = PackageCount+1; //1 plus for extra column of converted cases
							
							long SalesTotal[] = new long[PackageCount];
							int TargetTotal[] = new int[PackageCount];
							long PercentageTotal[] = new long[PackageCount];
							
							for (int i = 0; i < SalesTotal.length; i++){
								SalesTotal[i] = 0;								
								TargetTotal[i]=0;
								PercentageTotal[i]=0;
							}
							%>	
												
					    </tr>
					    <tr style="font-size:11px;">
						    <th data-priority="1"  style="text-align:center; ">&nbsp;</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;">L</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;">T</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;background-color:#F6F6F6;">P</th>
						    
						    <%
						    rs.beforeFirst();
						    while(rs.next()){
						    	
						    %>
						   
						    <th data-priority="1"  style="text-align:center; min-width:30px;">L</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;">T</th>
						    <th data-priority="1"  style="text-align:center; min-width:30px;background-color:#F6F6F6;">P</th>
					    <%} %>
					    
					    </tr>
					  </thead> 
					<tbody>
						<%
						
						//System.out.println("select * from common_distributors where is_active=1 and type_id=2"+WhereHOD);
						ResultSet rs1 = s.executeQuery("select * from common_distributors where 1=1 "+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and distributor_id in ("+DistributorIDs+") and distributor_id in (select dt.distributor_id from distributor_targets dt where month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.type_id=1)"); //distributor query
						//System.out.println("select * from common_distributors where 1=1 "+WhereHOD+WhereRSM+" and distributor_id in ("+DistributorIDs+") and distributor_id in (SELECT distinct idn.distributor_id FROM inventory_delivery_note idn where idn.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+")");
						while(rs1.next()){
							DistributorID = rs1.getInt("distributor_id");
						%>
						<tr>
								<td><%=DistributorID + " - "+ rs1.getString("name") %></td>
							<%
							
							//ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  product_id in (SELECT distinct product_id FROM pep.mobile_order_products) "+WherePackage+" order by package_sort_order");//package query
							rs.beforeFirst();
							int PackageIndex = 0;
							long SalesConvertedCases=0;
							long TargetConvertedCases=0;
							double ConvertedCasesPercentage=0;
							
							//Converted Cases 
							
							ResultSet rs6 = s3.executeQuery("select "+    
										"sum(((total_units*ipv.liquid_in_ml)/ipv.conversion_rate_in_ml ) ) sale_amount "+																		        
								",ipv.unit_per_sku,ipv.conversion_rate_in_ml,ipv.liquid_in_ml "+
								"from "+
								    "inventory_delivery_note idn "+
								        "join "+
								    "inventory_delivery_note_products idnp ON idn.delivery_id = idnp.delivery_id "+
								        "join "+
								    "inventory_products_view ipv ON idnp.product_id = ipv.product_id "+
								"where ipv.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" and idn.distributor_id = "+DistributorID);
							
							if(rs6.first()){								
									SalesConvertedCases = rs6.getLong("sale_amount");								
							}
							 
							
							ResultSet rs7 = s3.executeQuery("select sum((dtp.quantity*ip.unit_per_case*ip.liquid_in_ml)/ip.conversion_rate_in_ml) quantity,ip.liquid_in_ml,ip.conversion_rate_in_ml from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join inventory_packages ip on dtp.package_id = ip.id where dt.distributor_id="+DistributorID+" and month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.type_id=1");
							
							//System.out.println("select sum((dtp.quantity*ip.unit_per_case*ip.liquid_in_ml)/ip.conversion_rate_in_ml) quantity,ip.liquid_in_ml,ip.conversion_rate_in_ml from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id join inventory_packages ip on dtp.package_id = ip.id where dt.distributor_id="+DistributorID+" and month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dt.type_id=1");
							if(rs7.first()){								
									TargetConvertedCases = rs7.getLong("quantity");									
							}
							
							if(TargetConvertedCases!=0){
								ConvertedCasesPercentage = ((SalesConvertedCases * 1d) / (TargetConvertedCases * 1d) )*100;
							}
							
							SalesTotal[PackageIndex] += SalesConvertedCases;
							TargetTotal[PackageIndex] += TargetConvertedCases;							
							
							
							PackageIndex++;
							%>
							<td style="text-align: right;"><%if(SalesConvertedCases!=0){%><%=Utilities.getDisplayCurrencyFormat(SalesConvertedCases) %><%} %></td>
							<td style="text-align: right;"><%if(TargetConvertedCases!=0){%><%=Utilities.getDisplayCurrencyFormat(TargetConvertedCases) %><%} %></td>
							<td style="text-align: right;<%if(ConvertedCasesPercentage == 0){%>background-color:#F6F6F6;<%} else if(ConvertedCasesPercentage<40){%>background-color:#F6F6F6;<%} else if(ConvertedCasesPercentage >=40 && ConvertedCasesPercentage <70){%>background-color:#E9F0FF;<%} else if(ConvertedCasesPercentage>=70 && ConvertedCasesPercentage<90){%>background-color:#FFFFB2;<%} else if(ConvertedCasesPercentage>=90){%>background-color:#C9FFC9;<%}%>"><%if(ConvertedCasesPercentage!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(ConvertedCasesPercentage) %>%<%} %></td>
							<%
							while(rs.next()){
							int unit_per_sku=0;
							PacakgeID=rs.getInt("package_id");
								
								//System.out.println("select sum(total_units) qty,ipv.unit_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.distributor_id = "+DistributorID+" and ipv.package_id ="+PacakgeID+WherePackage);	
								//ResultSet rs3 = s3.executeQuery("select sum(total_units) qty,ipv.unit_per_sku from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where isa.adjusted_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and isa.distributor_id = "+DistributorID+" and ipv.package_id ="+PacakgeID+WherePackage);	
								
																
								ResultSet rs3 = s3.executeQuery("select "+    
										"sum(total_units) sale_amount "+																		        
								",ipv.unit_per_sku "+
								"from "+
								    "inventory_delivery_note idn "+
								        "join "+
								    "inventory_delivery_note_products idnp ON idn.delivery_id = idnp.delivery_id "+
								        "join "+
								    "inventory_products_view ipv ON idnp.product_id = ipv.product_id "+
								"where ipv.category_id = 1 and idn.created_on between "+Utilities.getSQLDateLifting(StartDate)+" and "+Utilities.getSQLDateNextLifting(EndDate)+" and idn.distributor_id = "+DistributorID+" and ipv.package_id ="+PacakgeID+WherePackage);
								
								long sales_amount=0;
								double target_sales_amount=0;
								int target_unit_per_sku=0;
								
								double percentage=0;
								
								if(rs3.first()){
									 sales_amount = rs3.getLong("sale_amount");									
									unit_per_sku = rs3.getInt("unit_per_sku");								
							%>
								 
							<%
								}
								
								long SalesRawCases = Utilities.getRawCasesAndUnits(sales_amount, unit_per_sku)[0];
								
								ResultSet rs4 = s3.executeQuery("select dtp.quantity from distributor_targets dt join distributor_targets_packages dtp on dt.id=dtp.id where dt.distributor_id="+DistributorID+" and month=date_format("+Utilities.getSQLDate(StartDate)+",'%m') and year=date_format("+Utilities.getSQLDate(StartDate)+",'%Y') and dtp.package_id="+PacakgeID+WherePackage+" and dt.type_id=1");
								if(rs4.first()){
									target_sales_amount=rs4.getDouble("quantity");	
									
								}
								if(target_sales_amount!=0){
									
									
									
									percentage = (Utilities.parseDouble(SalesRawCases+"")/target_sales_amount)*100;	
								}	
								
								SalesTotal[PackageIndex] += SalesRawCases;
								TargetTotal[PackageIndex] += target_sales_amount;
								
							%>
							
								<td style="text-align: right;"><%if(SalesRawCases!=0){%><%=  Utilities.getDisplayCurrencyFormatRounded(SalesRawCases)  %><%} %></td> 
								<td style="text-align: right;"><%if(target_sales_amount!=0){%><%= Utilities.getDisplayCurrencyFormatRounded(target_sales_amount)%><%} %></td>
								<td style="text-align: right;<%if((int)percentage == 0){%>background-color:#F6F6F6;<%} else if((int)percentage<40){%>background-color:#F6F6F6;<%} else if((int)percentage >=40 && (int)percentage <70){%>background-color:#E9F0FF;<%} else if((int)percentage>=70 && (int)percentage<90){%>background-color:#FFFFB2;<%} else if((int)percentage>=90){%>background-color:#C9FFC9;<%}%>"><%if((int)percentage !=0){%><%=Utilities.getDisplayCurrencyFormatRounded(percentage) %>%<%} %></td>
								
								<%
								
								PackageIndex++;
							}
							%>
						</tr>
						<%
						}
						%>
						<tr>
						<td style="font-weight:bold">Total</td>
						<%
						
						for (int i = 0; i < SalesTotal.length; i++){
							double TotalPercentage=0;
							TotalPercentage = (SalesTotal[i]*1d/TargetTotal[i]*1d)*100;
							
							%>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(SalesTotal[i]) %></td>
							<td style="text-align: right;"><%=Utilities.getDisplayCurrencyFormat(TargetTotal[i]) %></td>
							<td style="text-align: right;<%if((int)TotalPercentage == 0){%>background-color:#F6F6F6;<%} else if((int)TotalPercentage<40){%>background-color:#F6F6F6;<%} else if((int)TotalPercentage >=40 && (int)TotalPercentage <70){%>background-color:#E9F0FF;<%} else if((int)TotalPercentage>=70 && (int)TotalPercentage<90){%>background-color:#FFFFB2;<%} else if((int)TotalPercentage>=90){%>background-color:#C9FFC9;<%}%>"><%=Utilities.getDisplayCurrencyFormatRounded(TotalPercentage) %>%</td> 
							
						<%
						}
						%>
						</tr>
						
					</tbody>
							
				</table>
		</td>
	</tr>
</table>
<table border=0 style="font-size:13px; font-weight: 400; width:25%; margin-top:10px;" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
				
				 <tr style="font-size:11px;background:#ececec;">
						 	<th>Legend</th>
						 </tr>
						 <tr>
						 <td>L = Lifting</td>
						 </tr>
						 <tr>
						 <td>T = Target</td>
						 </tr>
						 <tr>
						 <td>P = Percentage</td>
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