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
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>
 
 
 <script src="js/jquery.excoloSlider.min.js"></script>
<link href="css/jquery.excoloSlider.css" rel="stylesheet">
 
<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

function runSlider(SliderID){
	//alert(SliderID);
	
	
	if($("#IsSliderAlreadyClicked_"+SliderID).val()=="0"){
		$("#slider_"+SliderID).excoloSlider();
		$("#IsSliderAlreadyClicked_"+SliderID).val("1");
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



int FeatureID = 151;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToSAPDB();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();


Datasource dsMySQL = new Datasource();
dsMySQL.createConnection();
Statement sMySQL = dsMySQL.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));



Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

String WhereDate = "";


if(session.getAttribute(UniqueSessionID+"_SR1StartDate") != null && session.getAttribute(UniqueSessionID+"_SR1EndDate") != null){
	WhereDate =" and cca.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate);
}

//if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
//	EndDate = new Date();
//}

//System.out.print("StartDate = "+StartDate);
//System.out.print("EndDate = "+EndDate);


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
	//WhereHOD = " and vbak.kunnr in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
	WhereHOD = " and vbrk.kunag in ("+UserAccess.getDistributorQueryString(UserAccess.getSNDDistributors(SelectedHODArray[0]))+") ";
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
	WhereRSM = " and knkli in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereSM = " and knkli in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and knkli in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and knkli in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}

//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
	*/
}

String DistributorIDs = "";
String WhereDistributors = "";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		if(i == 0){
			DistributorIDs += "0,"+SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and knkli in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//


//Primary Invoice Status


String SelectedPrimaryInvoiceStatusArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPrimaryInvoiceStatus") != null){
	SelectedPrimaryInvoiceStatusArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedPrimaryInvoiceStatus");
	
}

/*

String WherePrimaryInvoiceStatus = "";
if (HODIDs.length() > 0){
	//WhereHOD = " and vbak.kunnr in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";
	WhereHOD = " and vbrk.kunag in ("+UserAccess.getDistributorQueryString(UserAccess.getSNDDistributors(SelectedHODArray[0]))+") ";
}
*/



%>



<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>
<%


//System.out.println(StartDateMonth6+" - "+EndDateMonth6);

%>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							
							
							<th data-priority="1"  style="text-align:center; width: 15% ">Transaction Date</th>
							<th data-priority="1"  style="text-align:center; width: 40% ">Distributor</th>
							<th data-priority="1"  style="text-align:center; width: 15% ">Invoice No</th>
							<th data-priority="1"  style="text-align:center; width: 15% ">Invoice Amount</th>							
							<th data-priority="1"  style="text-align:center; width: 15% ">Posted</th>													
					    </tr>
					  </thead> 
					<tbody>
					
					
					
						<%
						
						//String Query ="SELECT vbeln invoice_number, knkli distributor_id, erdat entry_date, kurrf_dat transaction_date, aedat changed_on, (netwr+mwsbk) invoice_amount, (SELECT name FROM common_distributors where distributor_id=knkli) distributor_name FROM sap_vbrk where 1=1 "+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and vbeln not in (select invoice_no from inventory_delivery_note) and kurrf_dat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate);
						//System.out.println(Query);
						
						//String Query = "SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, (vbak.netwr + (select sum(mwsbp) from sap_vbap where vbeln = vbak.vbeln)) /* Tax Amount from VBAP and Order Amount from VBAP-NETWR*/ order_amount, (SELECT name FROM common_distributors where distributor_id=vbak.kunnr) distributor_name FROM sap_vbak vbak join sap_vbuk vbuk on vbak.vbeln = vbuk.vbeln where vbak.audat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and vbuk.fksak = 'C' /* Billing Status complete */ and vbuk.vbtyp != 'H' /*orders not returned */ and vbuk.spstg != 'C' /*Orders not blocked*/ and vbuk.lfgsk != 'C' and vbak.vbeln not in (select sap_order_no from inventory_delivery_note)";
					
						
						
						//String Query = "SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, (vbak.netwr + (select sum(mwsbp) from sap_vbap where vbeln = vbak.vbeln)) /* Tax Amount from VBAP and Order Amount from VBAP-NETWR*/ order_amount, cd.name distributor_name FROM sap_vbak vbak join sap_vbuk vbuk on vbak.vbeln = vbuk.vbeln join common_distributors cd on vbak.kunnr = cd.distributor_id where vbak.auart in ('ZDIS', 'ZMRS', 'ZCLA', 'ZSAM', 'ZCAN', 'ZTGT', 'ZOSM','ZDFR') and vbak.audat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+"  "+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and vbuk.fksak in ('C') /* Billing not processed */ and vbuk.vbtyp = 'C' and cd.is_central_blocked = 0 and cd.is_billing_blocked = 0 /* orders excluding cancelled */ and vbuk.lfgsk != 'C' and vbak.vbeln not in (select sap_order_no from inventory_delivery_note)";
						
						
						
						//String Query = "SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, (vbak.netwr + (select sum(mwsbp) from sap_vbap where vbeln = vbak.vbeln)) /* Tax Amount from VBAP and Order Amount from VBAP-NETWR*/ order_amount, cd.name distributor_name FROM sap_vbak vbak join sap_vbuk vbuk on vbak.vbeln = vbuk.vbeln join common_distributors cd on vbak.kunnr = cd.distributor_id where vbak.auart in ('ZDIS', 'ZMRS', 'ZCLA', 'ZSAM', 'ZCAN', 'ZTGT', 'ZOSM','ZDFR') and vbak.audat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+"  "+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and vbuk.fksak in ('C') /* Billing not processed */ and vbuk.vbtyp = 'C' and cd.is_central_blocked = 0 and cd.is_billing_blocked = 0 /* orders excluding cancelled */ and vbuk.lfgsk != 'C' and vbak.vbeln not in (select sap_order_no from inventory_delivery_note)";
						
					
						
						
					sMySQL.executeUpdate("create temporary table temp_invoices_list (transaction_date date, distributor_id bigint(11), distributor_name varchar(255),  invoice_number bigint(11), invoice_amount double, is_paid tinyint(1) )");	
					double TotalInvoiceAmount = 0;
					
					
					String Query = "select vbrk.kurrf_dat /*Transaction Date*/, vbrk.fkdat /*Billing Date*/, vbrk.erdat, vbrk.aedat /*Changed On*/, vbrk.vbeln, vbrk.netwr+vbrk.mwsbk invoice_amount, vbrk.fkart, vbrk.kunag /*Customer*/, kna1.name1, vbrk.ZZCDS_STATUS"+
							" from sapsr3.vbrk vbrk join sapsr3.vbuk vbuk on vbrk.vbeln = vbuk.vbeln join sapsr3.kna1 kna1 on vbrk.kunag = kna1.kunnr"+
							" where vbrk.kurrf_dat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+" and vbrk.fksto != 'X' /* Invoice not Cancelled */"+ 
							" "+WhereHOD+" "+WhereDistributors+" and vbrk.fkart in ('ZDIS', 'ZMRS', 'ZCLA', 'ZSAM', 'ZCAN', 'ZTGT', 'ZOSM','ZDFR')"+ 
							" and kna1.CASSD != 'X' and kna1.FAKSD != 'X' and kna1.LIFSD != 'X'";
					
					ResultSet rs = s.executeQuery(Query);
					while(rs.next()){
						
						int isPaid = 0;
						String isPaid_source = rs.getString("ZZCDS_STATUS");
						if (isPaid_source.equals("X")){
							isPaid = 1;
						}
						
						
						sMySQL.executeUpdate("insert into temp_invoices_list values("+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("kurrf_dat")))+", "+rs.getLong("kunag")+",'"+Utilities.filterString(rs.getString("name1"), 1, 200)+"',"+rs.getLong("vbeln")+","+rs.getDouble("invoice_amount")+", "+isPaid+")");
						
					}
					
					ResultSet rsMySQL = sMySQL.executeQuery("select *, ifnull((select id from gl_invoice_posting where invoice_no = til.invoice_number),0) is_paid_portal from temp_invoices_list til where til.invoice_number not in (select invoice_no from inventory_delivery_note) order by transaction_date");
					while(rsMySQL.next()){
						TotalInvoiceAmount += rsMySQL.getDouble("invoice_amount");
						
						long isPaidPortal = rsMySQL.getLong("is_paid_portal");
						
						String isPaidDisplay = "No";
						int isPaid = rsMySQL.getInt("is_paid");
						
						if (isPaid == 1 | isPaidPortal != 0){
							isPaidDisplay = "Yes";
						}
						
					%>
					
						<tr>
							<td style="text-align:left"><%=Utilities.getDisplayDateFormat(rsMySQL.getDate("transaction_date")) %></td>
							<td style="text-align:left"><%=rsMySQL.getLong("distributor_id") %> - <%=rsMySQL.getString("distributor_name") %></td>							
							<td style="text-align:left"><%=rsMySQL.getString("invoice_number") %></td>
							<td style="text-align:right"><%=Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rsMySQL.getDouble("invoice_amount")) %></td>													
							<td style="text-align:center"><%=isPaidDisplay %></td>
						</tr>
					<%
					 }
					%>	
					
					<tr>
						<td colspan=3 style="text-align: right">Total</td>
						<td style="text-align: right"><%=Utilities.getDisplayCurrencyFormat(TotalInvoiceAmount) %></td>
						<td style="text-align: right"></td>
					</tr>
					
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

dsMySQL.dropConnection();

%>