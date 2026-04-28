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
int FeatureID = 149;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToSAPDB();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();


Datasource dsmysql = new Datasource();
dsmysql.createConnection();
Statement smysql = dsmysql.createStatement();


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
	WhereHOD = " and vbak.kunnr in ("+UserAccess.getDistributorQueryString(UserAccess.getSNDDistributors(SelectedHODArray[0]))+") ";
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
	WhereRSM = " and vbak.kunnr in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereSM = " and vbak.kunnr in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and  vbak.kunnr in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and vbak.kunnr in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
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
	WhereDistributors = " and vbak.kunnr in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//









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
							
							
							<th data-priority="1"  style="text-align:center; width: 10% ">Order Date</th>							
							<th data-priority="1"  style="text-align:center; width: 10% ">Entry Date</th>
							<th data-priority="1"  style="text-align:center; width: 50% ">Distributor</th>
							<th data-priority="1"  style="text-align:center; width: 10% ">Order No</th>
							<th data-priority="1"  style="text-align:center; width: 10% ">Amount*</th>
							<th data-priority="1"  style="text-align:center; width: 10% ">Age (Days)</th>
													
					    </tr>
					  </thead> 
					<tbody>
					
					
					
						<%
						
						
						smysql.executeUpdate("create temporary table temp_pending_orders (order_date date, entry_date date, distributor_id bigint(11), distributor_name varchar(200), order_no bigint(11), amount double, is_rejected int(5))");
						
						double TotalOrderAmount = 0;
						//String Query = "SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, (vbak.netwr + (select sum(mwsbp) from sap_vbap where vbeln = vbak.vbeln)) /* Tax Amount from VBAP and Order Amount from VBAP-NETWR*/ order_amount, cd.name distributor_name FROM sap_vbak vbak join sap_vbuk vbuk on vbak.vbeln = vbuk.vbeln join common_distributors cd on vbak.kunnr = cd.distributor_id where vbak.auart in ('ZDIS', 'ZMRS', 'ZCLA', 'ZSAM', 'ZCAN', 'ZTGT', 'ZOSM','ZDFR') and vbak.audat between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+"  "+WhereDistributors+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+" and vbuk.fksak in ('A') /* Billing not processed */ and vbuk.vbtyp = 'C' and cd.is_central_blocked = 0 and cd.is_billing_blocked = 0 /* orders excluding cancelled */ ";
						String Query = "SELECT vbak.vbeln order_number, vbak.kunnr distributor_id, vbak.erdat entry_date, vbak.audat order_date, (vbak.netwr + (select sum(mwsbp) from "+ds.getSAPDatabaseAlias()+".vbap where vbeln = vbak.vbeln)) /* Tax Amount from VBAP and Order Amount from VBAP-NETWR*/ order_amount, kna1.name1 distributor_name, vbuk.fksak, (select ABGRU from "+ds.getSAPDatabaseAlias()+".vbap where vbeln = vbak.vbeln and ABGRU != ' ' and ROWNUM <= 1) is_rejected FROM "+ds.getSAPDatabaseAlias()+".vbak vbak join "+ds.getSAPDatabaseAlias()+".vbuk vbuk on vbak.vbeln = vbuk.vbeln join "+ds.getSAPDatabaseAlias()+".kna1 kna1 on vbak.kunnr = kna1.kunnr where vbak.auart in ('ZDIS','ZMRS','ZDFR') and vbak.audat between "+Utilities.getSQLDateOracle(StartDate)+" and "+Utilities.getSQLDateOracle(EndDate)+" and vbuk.vbtyp = 'C' "+WhereHOD+WhereDistributors+" and kna1.CASSD != 'X' and kna1.FAKSD != 'X' order by vbak.audat";
						//System.out.println(Query);
						/*
						'ZSAM' sampling
						'ZCLA' Claim
						'ZTGT' target
						'ZCAN' canteen
						'ZOSM' officer / need check
						'ZDIS' distributor 
						'ZMRS' key accounts 
						'ZDFR' other depo orders 
						*/
						//System.out.println(Query);
						
						
						ResultSet rs = s.executeQuery(Query);
						while(rs.next()){
							
							int is_rejected = Utilities.parseInt(rs.getString("is_rejected"));
							
							smysql.executeUpdate("insert into temp_pending_orders (order_date, entry_date, distributor_id, distributor_name, order_no, amount, is_rejected) values ("+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("order_date")))+", "+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("entry_date")))+", "+rs.getLong("distributor_id")+", '"+rs.getString("distributor_name")+"', "+rs.getLong("order_number")+", "+rs.getDouble("order_amount")+", "+is_rejected+")");
							//System.out.println("insert into temp_pending_orders (order_date, entry_date, distributor_id, distributor_name, order_no, amount) values ("+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("order_date")))+", "+Utilities.getSQLDate(Utilities.parseDateYYYYMMDDWithoutSeparator(rs.getString("entry_date")))+", "+rs.getLong("distributor_id")+", '"+rs.getString("distributor_name")+"', "+rs.getLong("order_number")+", "+rs.getDouble("order_amount")+")");
							
						}
						
						
						
						ResultSet rs2 = smysql.executeQuery("select *, to_days(curdate()) - to_days(order_date) age from temp_pending_orders where order_no not in (select order_no from gl_order_posting) and order_no not in (select order_no from gl_invoice_posting) and is_rejected = 0");
						while(rs2.next()){
							TotalOrderAmount += rs2.getDouble("amount");
							
								
							
					%>
						<tr>
							<td style="text-align:left"><%=Utilities.getDisplayDateFormat(rs2.getDate("order_date"))%></td>
							<td style="text-align:left"><%=Utilities.getDisplayDateFormat(rs2.getDate("entry_date"))%></td>
							<td style="text-align:left"><%=rs2.getLong("distributor_id") %> - <%=rs2.getString("distributor_name") %></td>							
							<td style="text-align:left"><%=rs2.getLong("order_no") %></td>
							<td style="text-align:right"><%=Utilities.getDisplayCurrencyFormat(rs2.getDouble("amount")) %></td>
							<td style="text-align:right"><%=rs2.getLong("age") %></td>
						</tr>
					<%
						
					 }
					%>	
					<tr>
						<td colspan="4" style="text-align: right">Total</td>
						<td style="text-align: right"><%=Utilities.getDisplayCurrencyFormat(TotalOrderAmount) %></td>
						<td style="text-align: right"></td>
					</tr>
					
					</tbody>
							
				</table>
				* Order Amount is exclusive of special discount on key accounts which are reflected at Invoicing stage
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