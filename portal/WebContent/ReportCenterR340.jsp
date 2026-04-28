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


function openInNewTab(url) {
	  var win = window.open(url, '_blank');
	  win.focus();
}


</script>

<style>
td{
font-size: 8pt;
}

.ui-content {
    border-width: 0;
    overflow: visible;
    overflow-x: scroll;
    padding: 15px;
}  

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 435;

 if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}
 


Datasource ds = new Datasource();
ds.createConnectionToReplica2();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s4 = c.createStatement();
Statement s5 = c.createStatement();
Statement s11 = c.createStatement();
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

System.out.println("WhereRSM == "+WhereRSM);
//Distributor

long SelectedDistributorsArray[] = null;
boolean IsDistributorSelected=false;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors"); 
	IsDistributorSelected = true;
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
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

//PJP


String PJPIDs="";
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
	PJPIDs = Utilities.serializeForSQL(SelectedPJPArray);
}

String WherePJP = "";
if (PJPIDs.length() > 0){
	WherePJP = " and codv.outlet_id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
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
	OrderBookerIDsWhere =" and mo.created_by in ("+OrderBookerIDs+") ";
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

if(SelectedDistributorsArray == null){
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Please select at least one Distributor   </li>
</ul>
<%
}else{
%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Distributor Sales</li>
<li>
<table style="width: 100%; margin-top: -8px" cellpadding="0"
			cellspacing="0">
			<tr>
				
				<td style="width: 100%" valign="top">
					<table border=0
						style="font-size: 18px; font-weight: 400; width: 100%"
						cellpadding="0" cellspacing="0" adata-role="table"
						class="GridWithBorder">
						
					
						<tbody>
							
							<%-- <%if(!DistributorIDs.equals("")){ %> --%>
							<tr>
								<td style="text-align: left">
   									
   									<div id="aExcelFileReady">
   										<a data-mini="true" href="reports/ReportsR340DownloadExcel?UniqueSessionID=<%=UniqueSessionID%>&stock=1"  target="_blank"  >Export to Excel With Stock</a>
   								</div>
						    									
						   </td>							
								
							</tr>
							<tr>
								<td style="text-align: left">
   									
   									<div id="aExcelFileReady">
   										<a data-mini="true" href="reports/ReportsR340DownloadExcel?UniqueSessionID=<%=UniqueSessionID%>&stock=0" target="_blank"  >Export to Excel Without Stock</a>
   								</div>
						    									
						   </td>							
								
							</tr>
					<%-- 	<%}else{ %>
						<br/>
						<p><b>Please select at least one distributor</b></p>
						<%} %> --%>
		
						</tbody>
									
					</table>
					
						
					
				</td>
			</tr>
		</table>
		</li>
<%-- <li>
	<a data-mini="true" href="reports/ReportsR328DownloadExcel?UniqueSessionID=<%=UniqueSessionID%>" target="_blank"  >Download</a>
</li> --%>
<%
if(false){
ResultSet rs14 = s.executeQuery("SELECT id, display_name FROM users where id = "+OrderBookerIDs);
        if(rs14.first()) {
        	 %><li><%=rs14.getString("id")%> - <%=rs14.getString("display_name")%></li><%
        }
        %>
<li>
 <table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>

<%
 int count = 0;
        
        int PackageCount = 1;

        

        Date Yesterday;
        long SND_ID;
        
        ResultSet rs13 = null;
        int groupId = 0;
        String groupLabel = "";

        %>
        <th  style="text-align: center">Date</th> 
          <%
          int packageCount = 0;
          ResultSet rss4 = s.executeQuery("select * from inventory_products_groups where id=1 order by id ");
          while (rss4.next()) {
              groupId = rss4.getInt(1);
              groupLabel = rss4.getString(2);
              
               ResultSet rss5 = s2.executeQuery("SELECT count(*) FROM inventory_products_view where  group_id=" + groupId + " and  product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products where id in (select id from inventory_sales_adjusted where created_on between " + Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + " ))  " + WherePackage + "  order by group_id,package_sort_order ");
              if (rss5.first()) {
                  packageCount = rss5.getInt(1);
              }
              rs13 = s2.executeQuery("SELECT distinct package_id, package_label, package_sort_order, group_id,(select label from inventory_products_groups ipg where ipg.id=group_id) group_label FROM inventory_products_view where  group_id=" + groupId + " and product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products where id in (select id from inventory_sales_adjusted where created_on between " + Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + " )) " + WherePackage + " order by group_id,package_sort_order");
              while (rs13.next()) {
              	 %><th data-priority="1"  style="text-align:center; min-width: 50px;"><%=rs13.getString("package_label")%></th>
                   
                   <%
              }
           
            	  %><th data-priority="1"  style="text-align:center; min-width: 50px;"><%=String.valueOf(groupLabel) + " Total Amount"%></th>
                  
                  <%
              
          }
              %></thead>
              
             
         <%
          long[] PackageTotal = new long[packageCount + 1];
          long[] array = PackageTotal;
          double totalNetAmountCosmetics=0;
          double totalNetAmountConsumer=0;
          double GrandAmountTotal=0.0;
       

          Date CurrentDate = StartDate;
	       	double AmountCosmetics=0.0;
	       	double AmountConsumer=0.0;
	       	double AmountTotal=0.0;
	       	int PacakgeID2 = 0;
	       	 int PackageIndex = 0;
        	 rss4 = s.executeQuery("select * from inventory_products_groups where id=1 order by id ");
            while (rss4.next()) {
                groupId = rss4.getInt(1);
                groupLabel = rss4.getString(2);
                int packageCount2 = 0;
                ResultSet rss7 = s2.executeQuery("SELECT count(*) FROM inventory_products_view where  group_id=" + groupId + " and  product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products where id in (select id from inventory_sales_adjusted where created_on between " + Utilities.getSQLDate(StartDate) + " and " + Utilities.getSQLDateNext(EndDate) + " ))  " + WherePackage + "  order by group_id,package_sort_order ");
                if (rss7.first()) {
                    packageCount2 = rss7.getInt(1);
                }
                double NetAmountCosmetics=0.0;
                while(true){
                	 %>
                     
                     <tr><td data-priority="1"  style="text-align:center; min-width: 50px;"><%=Utilities.getDisplayDateFormat(CurrentDate)%></td>
               <%
	                rs13 = s2.executeQuery("SELECT distinct package_id, package_label, package_sort_order, group_id,(select label from inventory_products_groups ipg where ipg.id=group_id) group_label FROM inventory_products_view where  group_id=1 and product_id in (SELECT distinct product_id FROM inventory_sales_adjusted_products where id in (select id from inventory_sales_adjusted where created_on between " + Utilities.getSQLDate(CurrentDate) + " and " + Utilities.getSQLDateNext(CurrentDate) + " )) " + WherePackage + " order by group_id,package_sort_order");
	                while (rs13.next()) {
	                	PacakgeID2 = rs13.getInt("package_id");
			        	  ResultSet rs161 = s3.executeQuery("select sum(total_units) qty,sum(if(isap.is_promotion=0,isap.net_amount,0)) net_amount from inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id = isap.id join inventory_products_view ipv on isap.product_id = ipv.product_id where   isa.created_on_date between " + Utilities.getSQLDate(CurrentDate) + " and " + Utilities.getSQLDate(CurrentDate) + "  and isa.booked_by = " + OrderBookerIDs +"  and ipv.package_id =" + PacakgeID2 +  WherePackage +" and (isa.id in (select isdi.sales_id from inventory_sales_dispatch isd join inventory_sales_dispatch_invoices isdi on isd.id = isdi.id where isd.is_adjusted=1) or isa.id in (select isdei.sales_id from inventory_sales_dispatch isd join inventory_sales_dispatch_extra_invoices isdei on isd.id = isdei.id where isd.is_adjusted=1)) order by ipv.group_id");
			    	        while (rs161.next()) {
			    	        	
			    	        	int n = PackageIndex;
			    	        	long qty = rs161.getLong("qty");
			    	        	array[n] += qty;
			    	        	
			    	            AmountCosmetics= rs161.getDouble("net_amount");
			    	            %><td data-priority="1"  style="text-align:center; min-width: 50px;"><%=qty%></td>
				                 
				                 <%
				                 ++PackageIndex;
			    	        }
			    	        totalNetAmountCosmetics +=AmountCosmetics;
			    	        NetAmountCosmetics+=AmountCosmetics;
			    	        //totalNetAmountConsumer +=AmountConsumer;
			    	        //AmountTotal= AmountCosmetics+AmountConsumer;
			    	        
			    	       
			                
	               
	                }
	                %><td data-priority="1"  style="text-align:center; min-width: 50px;"><%=Utilities.getDisplayCurrencyFormatRounded(NetAmountCosmetics)%></td>
	                  
	                  <%
                if(DateUtils.isSameDay(CurrentDate, EndDate)){
					break;
				}

				CurrentDate = Utilities.getDateByDays(CurrentDate, 1);
				 %>
                 
                 </tr>
           <%
                }
            }
            %></tr> <th data-priority="1"  style="text-align:left; min-width: 50px;">Total</th><%
        
        
       
       GrandAmountTotal= totalNetAmountCosmetics+totalNetAmountConsumer;
            for (int k = 0; k < array.length; ++k) {
          %>

                
          <td data-priority="1"  style="text-align:center; min-width: 50px;"><%=array[k]%></td>
           

        <%
            }
        %>

      
       
        </tr>
        </table>
        </li>	 
</ul>

<%
}
}
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>