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

<script src="js/lookups.js"></script>
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
int FeatureID = 229;

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
	WherePackage = " and ip.package_id in ("+PackageIDs+") ";
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
	WhereDistributors = " and distributor_id in ("+DistributorIDs+") ";
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

//customer filter

long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereCustomerID ="";

if (DistributorID != 0){
	WhereCustomerID = " and distributor_id in ("+DistributorID+") ";

}

//System.out.println("Hello "+WhereCustomerID);

//EmptyLossType


String EmptyLossTypeIDs="";
long SelectedEmptyLossTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyLossType") != null){
	SelectedEmptyLossTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyLossType");
	EmptyLossTypeIDs = Utilities.serializeForSQL(SelectedEmptyLossTypeArray);
}

String WhereEmptyLossType = "";
String WhereEmptyLossType1 = "";
if (EmptyLossTypeIDs.length() > 0){
	WhereEmptyLossType = " and type_id in ("+EmptyLossTypeIDs+") ";	
	WhereEmptyLossType1 = " and ect.type_id in ("+EmptyLossTypeIDs+") ";	
}


//Brand

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
	WhereBrand = " and ip.brand_id in ("+BrandIDs+") ";
}
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 
					 <%
					 if(DistributorID !=0 && EmptyLossTypeIDs.length()!=0 && PackageIDs.length() !=0){
					 
					 %>
					 
					<tbody>
						<%
						long AccountID =0;
						int counter=0;
						String AccountTitle ="";
						long Balance=0;
						Date CurrentDate = new Date();
						
						
						
						
						long OpeningBalance = 0;
						//opening balance
						
						//debit - total_units_received
						//credit - total_units_issued
						
						int UnitPerSkuOpening=0;
						
						//System.out.println("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  created_on_date < "+Utilities.getSQLDate(StartDate)+WhereEmptyLossType1+WhereCustomerID+WherePackage+WhereBrand+" order by ect.id");
						ResultSet rs2 = s.executeQuery("Select sum(total_units_received), sum(total_units_issued),ip.unit_per_sku  FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  created_on_date < "+Utilities.getSQLDate(StartDate)+WhereEmptyLossType1+WhereCustomerID+WherePackage+WhereBrand+" order by ect.id");
						if(rs2.first()){
							OpeningBalance = rs2.getLong(1) - rs2.getLong(2);
							UnitPerSkuOpening = rs2.getInt(3);
						}
						
						Balance = OpeningBalance;
						%>
						
						<thead>
					    
					    <!-- 
					    <tr>
					   
					    
							<th colspan=8></th>
						</tr>
						
						 -->
					    <tr style="font-size:11px;">							
							
							
							<th data-priority="1"  style="text-align:center;  width:10%">Date</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Remarks</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Receipt#</th>	
							<th data-priority="1"  style="text-align:center;  width:10%">Package</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Brand</th>																		
							<th data-priority="1"  style="text-align:center;  width:10%">Received</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Issued</th>
							<th data-priority="1"  style="text-align:center;  width:10%">Balance</th>
							
																
							
							
													
					    </tr>
					     <tr>
							<td><%=Utilities.getDisplayDateFormat(StartDate) %></td>
							<td>&nbsp;</td>
							<td>Opening Balance</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td style="text-align:right;background-color:#F6F6F6;"><%if(OpeningBalance>=0){ %><%=Utilities.convertToRawCases(OpeningBalance,UnitPerSkuOpening) %><%} %></td>
							<td style="text-align:right;background-color:#F6F6F6;"><%if(OpeningBalance<0){ %><%=Utilities.convertToRawCases(OpeningBalance*-1,UnitPerSkuOpening) %><%} %></td>
							<td style="text-align:right;background-color:#F6F6F6;"><%if(OpeningBalance!=0){ %><%=Utilities.convertToRawCasesAccounting(OpeningBalance,UnitPerSkuOpening) %><%} %></td>
														
						</tr>
					  </thead> 
						
						<%
						//System.out.println("Select *,(select display_name from users u where u.id=glt.created_by) display_name,ifnull((select warehouse_id from gl_cash_receipts where id = glt.cash_receipt_id),(select warehouse_id from gl_invoice_posting where id = glt.invoice_posting_id)) warehouse_id, (select label from common_warehouses cw where cw.id=warehouse_id) warehouse_name   FROM gl_transactions_accounts glta join gl_transactions glt  on glta.id=glt.id where account_id="+AccountID+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" order by document_date, glt.id");
						ResultSet rs1 = s.executeQuery("Select *,(select display_name from users u where u.id=created_by) display_name,ip.unit_per_sku ,(select label from inventory_packages ip1 where ip1.id=ip.package_id) package_label,(select label from inventory_brands ib where ib.id=ip.brand_id) brand_label FROM ec_transactions ect join inventory_products ip on ect.product_id=ip.id  where  created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereEmptyLossType1+WhereCustomerID+WherePackage+WhereBrand+" order by ect.id");
						
						while(rs1.next()){
							
							Balance = (Balance + rs1.getLong("total_units_received")) - rs1.getLong("total_units_issued"); 
							
							String instrument = "";
							
							int UnitPerSKU = rs1.getInt("unit_per_sku");
							
							long ReceiptID=0;
							
							if(rs1.getLong("empty_receipt_id")!=0 && rs1.getLong("empty_adjustment_id")==0){
								ReceiptID=rs1.getLong("empty_receipt_id");
							}else if(rs1.getLong("empty_receipt_id")==0 && rs1.getLong("empty_adjustment_id")!=0){
								ReceiptID=rs1.getLong("empty_adjustment_id");
							}
							
						%>
						
						
						
						<tr>										
										
							<td ><%=Utilities.getDisplayDateFormat(rs1.getDate("created_on")) %></td>
							<td ><%=rs1.getString("remarks") %></td>
							<td><%if(ReceiptID!=0){%><%=ReceiptID %><%} %></td>
							
							<td ><%=rs1.getString("package_label") %></td>
							<td ><%=rs1.getString("brand_label") %></td>	
							 
							
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.convertToRawCases(rs1.getLong("total_units_received"),UnitPerSKU) %></td>
							<td style="text-align:right;background-color:#F6F6F6;"><%=Utilities.convertToRawCases(rs1.getLong("total_units_issued"),UnitPerSKU) %></td>
							
							<%
							String BalanceValue = Utilities.convertToRawCasesAccounting(Balance,UnitPerSKU);
							%>
							
							<td style="text-align:right;background-color:<% if(BalanceValue.contains("(")){ out.print("#ffffb2;"); }else{ out.print("#F6F6F6;"); } %>"><%=BalanceValue%></td>
							
								
						</tr>
						<%
						counter++;
						}
						%>
						
						
					</tbody>
					<%}else{ %>
							<br />
							<p>Please select Customer, Glass Type and Package to view this report</p>
							
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