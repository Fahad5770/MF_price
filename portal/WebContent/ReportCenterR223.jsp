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

function OpenDeliveryNoteVoucherPrint(ID){
	//alert(ID);
	$("#DeliveryIDHiddenIDForPrint").val(ID);
	document.getElementById("DeliveryReportPrintForm").submit();
	//window.location='DeliveryNotePrintWithoutHeader.jsp?DeliveryID='+ID;
}

function DeleteOutsourceEntry(ID,RowID){
	var r = confirm("Do you want to detete this entry?");
	if (r == true) {
		$.mobile.loading( 'show');	
		$.ajax({
	    		url: "primarysales/PrimarySalesDeleteExecute",		    
	    		data: {
	    			SerialID: ID
	    			
	    		},
			    type: "POST",
			    dataType : "json",
	    		success:function(json){
	    			$.mobile.loading( 'hide');
	    			if(json.success == "true"){
	    				//SaleSummaryLoadReport();
	    				//alert("done");	
	    				$("#DeliveryNotDeleteeButton_"+RowID).addClass('ui-disabled');
	    				$("#DeliveryNoteButton_"+RowID).addClass('ui-disabled');	    				
	    				
	    			}else{
	    				alert("Can't Delete Older Records.");
	    			}
	    		},
	    		error:function(xhr, status){
	    			alert("Server could not be reached.");
	    			$.mobile.loading( 'hide');
	    		}
	    		
	    	});
	}
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
int FeatureID = 284;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();

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
	WhereBrand = " and ipv.brand_id in ("+BrandIDs+") ";
}

System.out.println(WhereBrand);

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


Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

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


//customer filter

long DistributorID1 = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";

if (DistributorID1 != 0){
	WhereCustomerID = " and ipprd.distributor_id in ("+DistributorID1+") ";

}


//Plant filter

long PlantID1 = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPlantID") != null){
	PlantID1 = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedPlantID");
}
String WherePlantID ="";

if (PlantID1 != 0){
	WherePlantID = " and plant_id in ("+PlantID1+") ";

}


%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					   <tr style="font-size:11px;">
					   <th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					   		<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
					   	
					   		
							
							<%
							
							//String[] DiscountTitle= {"Gross Revenue","","","","","","","","","","",""};
							
							
							ResultSet rs21 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in (SELECT distinct package_id FROM inventory_primary_percase_request_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs21.next()){
								%>
								<th data-priority="1"  style="text-align:center; " colspan="<%=rs21.getInt("package_count")*4%>"><%=rs21.getString("type_name") %></th>
								<%
							}
							
							%>
							
							
												
					    </tr>
					   
					   
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">&nbsp;</th>
							<th>Request ID</th>
							<th>Date</th>
							
							
							
							
							
							
							<%
							int PackageCount = 0;
							int TotalConverted=0;
						
							int ArrayCount=0;
							
							ResultSet rs12 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in (SELECT distinct package_id FROM inventory_primary_percase_request_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs12.next()){
								
								ResultSet rs2 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  package_id in (SELECT distinct package_id FROM inventory_primary_percase_request_products) "+WherePackage+" and category_id = 1 and lrb_type_id="+rs12.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs2.next()){
							%>
								<th data-priority="1"  style="text-align:center; " colspan="4"><%=rs2.getString("package_label") %></th>
							<%
							ArrayCount++;
							PackageCount++;
								}
							}
							
							//System.out.println("Array Count : "+ArrayCount+" - Package Count : "+PackageCount);
							
						
							
							
							%>
							<tr>
							<th data-priority="1"  style="text-align:center; width:10px;">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width:10px;">&nbsp;</th>
							<th data-priority="1"  style="text-align:center; width:10px;">&nbsp;</th>
							<%
							ResultSet rs121 = s.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in (SELECT distinct package_id FROM inventory_primary_percase_request_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs121.next()){
								
								ResultSet rs212 = s2.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  package_id in (SELECT distinct package_id FROM inventory_primary_percase_request_products) "+WherePackage+" and category_id = 1 and lrb_type_id="+rs121.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs212.next()){
									%>
									<td style="width:15px;">Quota</td>
									<td style="width:15px;">Rate</td>
									<td style="width:15px;">Lifting</td>
									<td style="width:15px;">Amount</td>
									
									<%
								}
							}
							
							
							
							%>
							
							</tr>
							
							<%
							long PackageTotal[] = new long[PackageCount];
							int PackageTotalUnitPerSKU[] = new int[PackageCount];
							for (int i = 0; i < PackageTotal.length; i++){
								PackageTotal[i] = 0;
								PackageTotalUnitPerSKU[i]=0;
							}
							
							//System.out.println(PackageCount);
							%>	
													
					    </tr>
					  </thead> 
					<tbody>
						<%
						long DistributorID=0;
						long PacakgeID=0;
						long MainTableID=0;
						
						Date LiftingStartDate=new Date();
						Date LiftingEndDate=new Date();
						
						//System.out.println("hasdhfdsaf");
						//System.out.println("SELECT distinct ipprd.distributor_id,(select name from common_distributors cd where cd.distributor_id=ipprd.distributor_id) distributor_name FROM inventory_primary_percase_request ippr join inventory_primary_percase_request_distributors ipprd on ippr.id=ipprd.product_promotion_id where 1=1 and ippr.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
						ResultSet rs1 = s.executeQuery("SELECT distinct id, ipprd.distributor_id,(select name from common_distributors cd where cd.distributor_id=ipprd.distributor_id) distributor_name,request_id,valid_from,valid_to FROM inventory_primary_percase_request ippr join inventory_primary_percase_request_distributors ipprd on ippr.id=ipprd.product_promotion_id where ippr.is_active=1 and ippr.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereCustomerID+" order by distributor_id desc"); //distributor query
						while(rs1.next()){
							
							int counter=0;
							
							DistributorID=rs1.getLong("distributor_id");
							MainTableID = rs1.getLong("id");
							
							LiftingStartDate=rs1.getDate("valid_from");
							LiftingEndDate=rs1.getDate("valid_to");
						%>
						
						<tr>
							<td><%=DistributorID%> - <%=rs1.getString("distributor_name") %></td>
							<td><%=rs1.getLong("request_id") %></td>
							<td><%=Utilities.getDisplayDateFormat(LiftingStartDate) %> - <%=Utilities.getDisplayDateFormat(LiftingEndDate) %></td>
							<%
							ResultSet rs122 = s2.executeQuery("SELECT count(distinct package_id) package_count,lrb_type_id,(select label from inventory_products_lrb_types iplt where iplt.id=lrb_type_id) type_name FROM inventory_products_view where  package_id in (SELECT distinct package_id FROM inventory_primary_percase_request_products) and category_id = 1 and lrb_type_id is not null group by lrb_type_id");
							while(rs122.next()){
								
								ResultSet rs2 = s3.executeQuery("SELECT distinct package_id, package_label FROM inventory_products_view where  package_id in (SELECT distinct package_id FROM inventory_primary_percase_request_products) "+WherePackage+" and category_id = 1 and lrb_type_id="+rs122.getLong("lrb_type_id")+" order by package_sort_order");
								while(rs2.next()){
									
									
									
									long PerCaseDiscount=0;
									long Quota=0;
									
									PacakgeID = rs2.getLong("package_id");
									
									ResultSet rs3 = s4.executeQuery("SELECT ipprp.percase_discount_rate,ipprp.quantity FROM inventory_primary_percase_request ippr join inventory_primary_percase_request_products ipprp on ippr.id=ipprp.id join inventory_primary_percase_request_products_lrb_types ipprpl on ippr.id=ipprpl.id where ipprp.id="+MainTableID+" and ipprp.package_id="+PacakgeID+" and ipprpl.lrb_type_id="+rs122.getLong("lrb_type_id"));
									if(rs3.first()){
										PerCaseDiscount = rs3.getLong("percase_discount_rate");
										Quota = rs3.getLong("quantity");
									}
									
									//lifiting 
									
									long TotalUnitsLifting=0;
									int UnitPerSKU=0;
									
									double Converted=0;
									double Amount =0;
									
									
									ResultSet rs4 = s4.executeQuery("select sum(total_units) total_units, cache_units_per_sku from inventory_delivery_note_source_invoice idnsi where distributor_id = "+DistributorID+" and delivery_created_on between "+Utilities.getSQLDateLifting(LiftingStartDate)+" and "+Utilities.getSQLDateNextLifting(LiftingEndDate)+" and cache_package_id="+PacakgeID+" and is_revenue = 1 and cache_lrb_type_id="+rs122.getLong("lrb_type_id"));
									//ResultSet rs4 = s4.executeQuery("select sum(idnp.total_units) total_units,unit_per_sku  from inventory_delivery_note idn join inventory_delivery_note_products idnp on idn.delivery_id = idnp.delivery_id join inventory_products_view ipv on idnp.product_id = ipv.product_id where ipv.category_id = 1 and distributor_id = "+DistributorID+" and created_on between "+Utilities.getSQLDateLifting(LiftingStartDate)+" and "+Utilities.getSQLDateNextLifting(LiftingEndDate)+" and package_id="+PacakgeID+" and lrb_type_id="+rs122.getLong("lrb_type_id"));
									if(rs4.first()){
										TotalUnitsLifting = rs4.getLong("total_units");
										UnitPerSKU = rs4.getInt("cache_units_per_sku");
										
										
										
										
										if(UnitPerSKU!=0){
											Converted = TotalUnitsLifting/UnitPerSKU;
										}
										
										
									}
									
									
									
									Converted = Math.round(Converted);
									
									if(Converted>=Quota){ // if lifting exceeds quota
										Converted=Quota;
									}
									
									
									
									Amount = Converted*PerCaseDiscount;
									
									
									PackageTotal[counter]+=Amount;
									
									%>
									
									<td style="text-align: right;"><%if(Quota!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(Quota) %><%} %></td>
									<td style="text-align: center;"><%if(PerCaseDiscount!=0){%><%=PerCaseDiscount %><%} %></td>
									<td style="text-align: right;"><%if(PerCaseDiscount!=0){%><%= Utilities.convertToRawCases(TotalUnitsLifting,UnitPerSKU) %><%} %></td>
									<td style="text-align:right;"><%if(Amount!=0){%><%=Utilities.getDisplayCurrencyFormat(Amount) %><%} %></td>
									
									
								<%
								counter++;
								}
							}
							
							
							%>
							
							
							
						</tr>
						
						
						
						<%
						
						}
						%>
						<tr>
						<td style="font-weight:bold"></td>
						<td style="font-weight:bold"></td>
						<td style="font-weight:bold">Total</td>
						
						<%
						
						for (int i = 0; i < PackageTotal.length; i++){
							
							%>
							<td></td>
							<td></td>
							<td></td>
							<td style="text-align: right;"><%if(PackageTotal[i]!=0){%><%=Utilities.getDisplayCurrencyFormat(PackageTotal[i]) %><%} %></td> 
						<%
						}
						%>
						</tr>
						
					</tbody>
							
				</table>
		</td>
	</tr>
	
	 
</table>




	</li>	
</ul>
<form method="post" action="DeliveryNotePrint.jsp" id="DeliveryReportPrintForm" target="_blank">
		<input type="hidden" name="DeliveryID" id="DeliveryIDHiddenIDForPrint"/>
		</form> 
<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>