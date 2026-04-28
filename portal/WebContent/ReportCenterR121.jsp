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
int FeatureID = 122;

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
	WhereHOD = " and cd_top.distributor_id in (SELECT distributor_id FROM common_distributors where snd_id in ("+HODIDs+")) ";	
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
	WhereRSM = " and cd_top.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
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
	WhereDistributors = " and cd_top.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//

//Warehouse


String WarehouseIDs="";
long SelectedWarehouseArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
	WarehouseIDs = Utilities.serializeForSQL(SelectedWarehouseArray);
}
//System.out.println(WarehouseIDs);
String WhereWarehouse = "";
if (WarehouseIDs.length() > 0){
	WhereWarehouse = " and idn.warehouse_id in ("+WarehouseIDs+") ";	
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
	WhereSM = " and cd_top.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
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
	WhereTDM = " and cd_top.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
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
	WhereASM = " and cd_top.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Summary</li>
<li>
<%
Date Month1 = new Date();

Date[] d = Utilities.getPastMonthsInDate(Month1, 6);
SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

//calculating next month date
Calendar calstart=Calendar.getInstance();
calstart.add(Calendar.MONTH, 1);
calstart.set(Calendar.DATE, calstart.getActualMinimum(Calendar.DAY_OF_MONTH));
Date nextMonthFirstDay = calstart.getTime();
String LastNextMonthDate = format.format(nextMonthFirstDay); 


//System.out.println(LastNextMonthDate);
//for heading
String Month1Text = new SimpleDateFormat("MMM").format(d[0]);
String Month2Text = new SimpleDateFormat("MMM").format(d[1]);
String Month3Text = new SimpleDateFormat("MMM").format(d[2]);
String Month4Text = new SimpleDateFormat("MMM").format(d[3]);
String Month5Text = new SimpleDateFormat("MMM").format(d[4]);
String Month6Text = new SimpleDateFormat("MMM").format(d[5]);

String Month1TextYear = new SimpleDateFormat("yyyy").format(d[0]);
String Month2TextYear = new SimpleDateFormat("yyyy").format(d[1]);
String Month3TextYear = new SimpleDateFormat("yyyy").format(d[2]);
String Month4TextYear = new SimpleDateFormat("yyyy").format(d[3]);
String Month5TextYear = new SimpleDateFormat("yyyy").format(d[4]);
String Month6TextYear = new SimpleDateFormat("yyyy").format(d[5]);



//setting startdate and end date
//
String StartDateMonth1 = "'"+format.format(d[0])+" 5:59'";
String EndDateMonth1 =  "'"+format.format(d[1])+" 6:00'";

String StartDateMonth2 = "'"+format.format(d[1])+" 5:59'";
String EndDateMonth2 =  "'"+format.format(d[2])+" 6:00'";

String StartDateMonth3 = "'"+format.format(d[2])+" 5:59'";
String EndDateMonth3 =  "'"+format.format(d[3])+" 6:00'";

String StartDateMonth4 = "'"+format.format(d[3])+" 5:59'";
String EndDateMonth4 =  "'"+format.format(d[4])+" 6:00'";

String StartDateMonth5 = "'"+format.format(d[4])+" 5:59'";
String EndDateMonth5 =  "'"+format.format(d[5])+" 6:00'";

String StartDateMonth6 = "'"+format.format(d[5])+" 5:59'";
String EndDateMonth6 =  "'"+LastNextMonthDate+" 6:00'";

//System.out.println(StartDateMonth6+" - "+EndDateMonth6);

%>

<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr style="font-size:11px;">
							
							<%							
							int PJPID=0;							
							%>
							<th data-priority="1"  style="text-align:center;  ">Distributor</th>							
							<th data-priority="1"  style="text-align:center;  "><%=Month1Text+" "+Month1TextYear %></th>							
							<th data-priority="1"  style="text-align:center;  "><%=Month2Text+" "+Month2TextYear %></th>
							<th data-priority="1"  style="text-align:center;  "><%=Month3Text+" "+Month3TextYear %></th>							
							<th data-priority="1"  style="text-align:center;  "><%=Month4Text+" "+Month4TextYear %></th>
							<th data-priority="1"  style="text-align:center;  "><%=Month5Text+" "+Month5TextYear %></th>							
							<th data-priority="1"  style="text-align:center;  "><%=Month6Text+" "+Month6TextYear %></th>
							<th style="text-align:center;  ">Total</th>						
					    </tr>
					  </thead> 
					<tbody>
					<%
					double VerticalTotal[] = new double[6];
					
					/*System.out.println("select cd_top.distributor_id, cd_top.name, (( "+
							"select invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth1+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth1+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+ 
							"group by idn.distributor_id"+
							")) jan, (( "+
							"select invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth2+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth2+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+
							"group by idn.distributor_id"+

							"))feb, (( "+
							"select invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth3+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth3+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+ 
							"group by idn.distributor_id"+

							")) march, (( "+
							"select invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth4+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth4+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+
							"group by idn.distributor_id"+

							")) april ,(( "+
							"select invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth5+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth5+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+
							"group by idn.distributor_id"+
							
							")) may,(( "+
							"select invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth6+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth6+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+
							"group by idn.distributor_id"+
							
							")) june from common_distributors cd_top where 1=1 "+WhereHOD+WhereRSM+WhereDistributors+" having jan is not null or feb is not null or march is not null or april is not null or may is not null or june is not null");
					
					*/
					ResultSet rs = s.executeQuery("select cd_top.distributor_id, cd_top.name, (( "+
							"select sum(invoice_amount) invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth1+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth1+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+ 
							"group by idn.distributor_id"+
							")) jan, (( "+
							"select sum(invoice_amount) invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth2+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth2+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+
							"group by idn.distributor_id"+

							"))feb, (( "+
							"select sum(invoice_amount) invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth3+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth3+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+ 
							"group by idn.distributor_id"+

							")) march, (( "+
							"select sum(invoice_amount) invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth4+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth4+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+
							"group by idn.distributor_id"+

							")) april ,(( "+
							"select sum(invoice_amount) invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth5+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth5+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+
							"group by idn.distributor_id"+
							
							")) may,(( "+
							"select sum(invoice_amount) invoice_amount from inventory_delivery_note idn "+
							"where idn.created_on between date_format("+StartDateMonth6+", '%Y-%m-%d %H:%i') and date_format("+EndDateMonth6+", '%Y-%m-%d %H:%i') and idn.distributor_id = cd_top.distributor_id "+WhereWarehouse+
							"group by idn.distributor_id"+
							
							")) june from common_distributors cd_top where 1=1 "+WhereHOD+WhereRSM+WhereSM+WhereTDM+WhereASM+WhereDistributors+" having jan is not null or feb is not null or march is not null or april is not null or may is not null or june is not null");
					while(rs.next()){
						double HoriToal=0;
						HoriToal =rs.getDouble("jan")+rs.getDouble("feb")+rs.getDouble("march")+rs.getDouble("april")+rs.getDouble("may")+rs.getDouble("june"); 
						VerticalTotal[0]+=rs.getDouble("jan");
						VerticalTotal[1]+=rs.getDouble("feb");
						VerticalTotal[2]+=rs.getDouble("march");
						VerticalTotal[3]+=rs.getDouble("april");
						VerticalTotal[4]+=rs.getDouble("may");
						VerticalTotal[5]+=rs.getDouble("june");
					%>
						
						
						<tr>
							<td><%=rs.getLong("distributor_id") %> - <%=rs.getString("name") %></td>							
								
							<td style="text-align:right"><%if(rs.getDouble("jan")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("jan")) %><%}else{ %>-<%} %></td>							
							<td style="text-align:right"><%if(rs.getDouble("feb")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("feb")) %><%}else{ %>-<%} %></td>	
							<td style="text-align:right"><%if(rs.getDouble("march")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("march")) %><%}else{ %>-<%} %></td>	
							<td style="text-align:right"><%if(rs.getDouble("april")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("april")) %><%}else{ %>-<%} %></td>	
							<td style="text-align:right"><%if(rs.getDouble("may")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("may")) %><%}else{ %>-<%} %></td>	
							<td style="text-align:right"><%if(rs.getDouble("june")!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(rs.getDouble("june")) %><%}else{ %>-<%} %></td>	 
							<td style="text-align:right"><%if(HoriToal!=0){ %><%=Utilities.getDisplayCurrencyFormatRounded(HoriToal)%><%}else{ %>-<%} %></td>	
						</tr>
					<%
					}
					%>
					<tr>
							<td style="font-weight:bold;">Total</td>	
					<%
					double HoriTotalOfTotal=0;
					for(int i=0;i<VerticalTotal.length;i++){
						HoriTotalOfTotal += VerticalTotal[i];
						//System.out.println(VerticalTotal[i]+" - "+HoriTotalOfTotal);
					%>
					<td style="text-align:right; "><%if(VerticalTotal[i]!=0){%><%=Utilities.getDisplayCurrencyFormatRounded(VerticalTotal[i]) %><%}else{ %>-<%} %></td>	
					<%}
					%>
					<td style="text-align:right"><%=Utilities.getDisplayCurrencyFormatRounded(HoriTotalOfTotal) %></td>	
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
%>