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
/* $(function(){
    $(".wrapper1").scroll(function(){
        $(".wrapper2")
            .scrollLeft($(".wrapper1").scrollLeft());
    });
    $(".wrapper2").scroll(function(){
        $(".wrapper1")
            .scrollLeft($(".wrapper2").scrollLeft());
    });
});

 */

</script>

<style>
td{
font-size: 8pt;
}



/* .ui-content {
	overflow :auto !important;
	overflow-x: scroll !important
}
  */
/* 
.wrapper1, .wrapper2{width: 27%; border: none 1px RED;
overflow-x: scroll; overflow-y:hidden;}
.wrapper1{height: 20px; }
.wrapper2{height: 200px; }
.div1 {width:4500px; height: 20px; }
.div2 {width:4500px; height: fit-content; bac1kground-color: #88FF88;
overflow: auto;}
 */
</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 350;

//if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	//response.sendRedirect("AccessDenied.jsp");
//}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
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
	WherePJP = " and co.id in (SELECT distinct outlet_id FROM distributor_beat_plan_schedule where id in("+PJPIDs+"))";	
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


//Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
//String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);

%>

<div class="wrapper1">
    <div class="div1">
    </div>
</div>
<div class="wrapper2">
    <div class="div2">
    
    	 <ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;margi1n-bottom:21px" data-icon="false">
<li data-role="list-divider" data-theme="a">Outlet Offtake</li>
<li>


<table style="width: 100%; margin-top:1px" cellpadding="0" cellspacing="0">

	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					 	<tr style="font-size:11px;">
							<th colspan="6" data-priority="1"  style="text-align:center; "></th>
							<th colspan="9"  data-priority="1"  style="text-align:center; ">Previous Shelf Stock</th>
							<th colspan="8"  data-priority="1"  style="text-align:center; ">Sale</th>
							<th colspan="9"  data-priority="1"  style="text-align:center; ">Current Availability</th>
							<th colspan="9"  data-priority="1"  style="text-align:center; ">Offtake</th>
					    </tr>
					    
					    <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; ">Outlet</th>
							<th data-priority="1"  style="text-align:center; ">Channel</th>
							<th data-priority="1"  style="text-align:center; ">Address</th>
							<th data-priority="1"  style="text-align:center; ">Contact Name</th>
							<th data-priority="1"  style="text-align:center; ">Mobile #</th>
							<th data-priority="1"  style="text-align:center; ">PSR</th>
							<%	
								
							
								int PackageCount = 0;
								ResultSet rs23 = s2.executeQuery("select product_id,package_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9) order by product_id asc");
								while(rs23.next()){
							%>
									<th data-priority="1"  style="text-align:center; "><%=rs23.getString("package_label")%></th>
							<%
								}
							%>
							<th data-priority="1"  style="text-align:center; ">Date</th>
							
							
							<!-- --- Previous Shelf Stock End -->
							
							
							
							<%	
								
								ResultSet rs231 = s2.executeQuery("select product_id,package_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9) order by product_id asc");
								while(rs231.next()){
							%>
									<th data-priority="1"  style="text-align:center; background-color:#F6F6F6; border:1px solid #eee;"><%=rs231.getString("package_label")%></th>
									
								
							<%
								}
							%>
							<!-- --- Sale Head End -->
							
							
							
							
							<%	
								
							
								
								ResultSet rs232 = s2.executeQuery("select product_id,package_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9) order by product_id asc");
								while(rs232.next()){
							%>
									<th data-priority="1"  style="text-align:center; "><%=rs232.getString("package_label")%></th>
							<%
								}
							%>
							<th data-priority="1"  style="text-align:center; ">Date</th>
							
							<!-- --- Current Availability End -->
							
							
							<%	
								
							
								
								ResultSet rs233 = s2.executeQuery("select product_id,package_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9) order by product_id asc");
								while(rs233.next()){
							%>
									<th data-priority="1"  style="text-align:center; "><%=rs233.getString("package_label")%></th>
									
								
							<%
								}
							%>
							<th data-priority="1"  style="text-align:center; ">Days</th>
					    </tr>
					    
					    
					     <tr style="font-size:11px;">
							<th data-priority="1"  style="text-align:center; "></th>
							<th data-priority="1"  style="text-align:center; "></th>
							<th data-priority="1"  style="text-align:center; "></th>
							<th data-priority="1"  style="text-align:center; "></th>
							<th data-priority="1"  style="text-align:center; "></th>
							<th data-priority="1"  style="text-align:center; "></th>
							<%	
								
							
								int PackageCount1 = 0;
								ResultSet rs231111 = s2.executeQuery("select product_id,package_label,brand_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9) order by product_id asc");
								while(rs231111.next()){
							%>
									<th data-priority="1"  style="text-align:center; "><%=rs231111.getString("brand_label")%></th>
							<%
								}
							%>
							<th data-priority="1"  style="text-align:center; ">Date</th>
							
							
							<!-- --- Previous Shelf Stock End -->
							
							
							
							<%	
								
								ResultSet rs2311 = s2.executeQuery("select product_id,package_label,brand_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9) order by product_id asc");
								while(rs2311.next()){
							%>
									<th data-priority="1"  style="text-align:center; background-color:#F6F6F6; border:1px solid #eee;"><%=rs2311.getString("brand_label")%></th>
									
								
							<%
								}
							%>
							<!-- --- Sale Head End -->
							
							
							
							
							<%	
								
							
								
								ResultSet rs2322 = s2.executeQuery("select product_id,package_label,brand_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9) order by product_id asc");
								while(rs2322.next()){
							%>
									<th data-priority="1"  style="text-align:center; "><%=rs2322.getString("brand_label")%></th>
							<%
								}
							%>
							<th data-priority="1"  style="text-align:center; ">Date</th>
							
							<!-- --- Current Availability End -->
							
							
							<%	
								
							
								
								ResultSet rs2333 = s2.executeQuery("select product_id,package_label,brand_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9) order by product_id asc");
								while(rs2333.next()){
							%>
									<th data-priority="1"  style="text-align:center; "><%=rs2333.getString("brand_label")%></th>
									
								
							<%
								}
							%>
							<th data-priority="1"  style="text-align:center; "></th>
					    </tr>
					    
					    
					    
					  </thead> 
					<tbody>
						
							
							<%
							
								
						                                    
								String Channel="";
								ResultSet rs = s2.executeQuery("SELECT distinct outlet_id, co.name,co.address , (select contact_name from common_outlets_contacts coc where coc.outlet_id=co.id) contact_name, (select contact_number from common_outlets_contacts coc where coc.outlet_id=co.id) contact_num,(select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id) channel_name FROM pep.mobile_retailer_stock_view mrsv join common_outlets co on mrsv.outlet_id=co.id where 1=1 and mrsv.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" "+OrderBookerIDsWhere+WherePJP+WhereDistributors+"");
								while(rs.next()){
									
									long OutletID=rs.getLong("outlet_id");
									
									Channel=rs.getString("channel_name");
							        
							        if(Channel==null){
							         Channel="";
							        }
							        
							        String PSR="";
							        
							        ResultSet rs85 = s3.executeQuery("SELECT assigned_to, (select display_name from users u where assigned_to=u.id) psr_name FROM pep.distributor_beat_plan_view where outlet_id="+OutletID);
							        if(rs85.first()){
							        	PSR = rs85.getLong("assigned_to")+" - "+rs85.getString("psr_name");
							        }
									
							%>
								<tr>
									<td data-priority="1"  style="text-align: left;" ><%=rs.getString("outlet_id")%> - <%=rs.getString("name") %></td>
									<td data-priority="1"  style="text-align: left;" ><%=Channel%></td>
									<td data-priority="1"  style="text-align: left;" ><%=rs.getString("address")%></td>
									<td data-priority="1"  style="text-align: left;" ><%=rs.getString("contact_name")%></td>
									<td data-priority="1"  style="text-align: left;" ><%=rs.getString("contact_num")%></td>
									<td data-priority="1"  style="text-align: left;" ><%=PSR%></td>
								<%
								
								
								int tdCounter=6;
								
								Date SecondMaxCreated0n=null;
								Date SecondMinCreated0n1=null;
								Date SecondMinCreated0n2=null;
								Date SecondPrintDate=null;
								
								/****
									This logic has been changed by Moiz - Coded by Zulqurnan 08/10/2019- 
									Now previous stock is minimum stock
								**********/
								
								
								ResultSet rs21=s6.executeQuery("select min(date(created_on)) created_on from mobile_retailer_stock where outlet_id="+OutletID+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
								if(rs21.first()){									
									SecondMinCreated0n1=rs21.getDate("created_on");								
									
								}
								
								ResultSet rs212=s6.executeQuery("select min(date(created_on)) created_on from mobile_retailer_sm_stock where outlet_id="+OutletID+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));
								if(rs212.first()){									
									SecondMinCreated0n2=rs212.getDate("created_on");								
									
								}
								
								if (SecondMinCreated0n1 == null){
									//SecondMinCreated0n1 = Utilities.getEndDateByMonth(1, 2001);
								}
								if (SecondMinCreated0n2 == null){
									//SecondMinCreated0n2 = Utilities.getEndDateByMonth(1, 2001);
								}
								
								
								if(SecondMinCreated0n1!=null && SecondMinCreated0n2!=null){
									if(SecondMinCreated0n2.before(SecondMinCreated0n1)){
										SecondMaxCreated0n = SecondMinCreated0n2;
									}else{
										SecondMaxCreated0n = SecondMinCreated0n1;
									}
								}else{
									if(SecondMinCreated0n1==null){
										SecondMaxCreated0n=SecondMinCreated0n2;
									}else{
										SecondMaxCreated0n=SecondMinCreated0n1;
									}
								}
								
								
									
									
									
									//for printing date only
									if(SecondMinCreated0n1==null){
										SecondPrintDate=SecondMinCreated0n2;
									}else{
										SecondPrintDate=SecondMinCreated0n1;
									}
								
								
								int i=1;
							
								int DifferenceOfdays=0;
								long diff1Pre=0;
								long diff2Pre=0;
								
								long diff4Pre=0;
								long diff5Pre=0;
								
								long diff6Pre=0;
								long diff7Pre=0;
								
								long diff8Pre=0;
								long diff9Pre=0;
								
								
								
								long diff1Ava=0;
								long diff2Ava=0;
								
								long diff4Ava=0;
								long diff5Ava=0;
								
								long diff6Ava=0;
								long diff7Ava=0;
								
								long diff8Ava=0;
								long diff9Ava=0;
								
								
								
								
								int ProductID=0;
								
								
								
								
					//---------Query for Previous Stock Starts----------------//	
					
					int NoPreviousRecordflag=0;
					
								ResultSet rs11 = s3.executeQuery("select product_id,package_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9)  order by product_id asc"); //distributor query
								while(rs11.next()){
									ProductID = rs11.getInt("product_id");
									long TotalRawCases=0;
									long RawCases=0;
									
									int ZeroStockOutletFlag=0;
									
										ResultSet rs221 = s4.executeQuery("SELECT sum(raw_cases) rawcase, created_on FROM pep.mobile_retailer_stock where outlet_id="+OutletID+" and created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDateNext(SecondMaxCreated0n)+" and product_id="+ProductID); 
										if(rs221.first()){
											
											TotalRawCases=rs221.getLong("rawcase");
											ZeroStockOutletFlag=1;
											
											
											//RawCases= rs221.getLong("rawcase");
											
										}
										
										
										ResultSet rs222 = s4.executeQuery("SELECT sum(raw_cases) rawcase FROM pep.mobile_retailer_sm_stock where outlet_id="+OutletID+" and created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDateNext(SecondMaxCreated0n)+" and product_id="+ProductID); 
										if(rs222.first()){
											
											TotalRawCases+=rs222.getLong("rawcase");
											ZeroStockOutletFlag=1;
											
											//RawCases= rs222.getLong("rawcase");
											
										}
											
										
										
											RawCases = TotalRawCases;
											
											if(ProductID==1)
											{
												diff1Pre=RawCases;
											
											}else if(ProductID==2)
											{
												diff2Pre=RawCases;
											 
											}
											else if(ProductID==4)
											{
												diff4Pre=RawCases;
											 
											}
											else if(ProductID==5)
											{
												diff5Pre=RawCases;
											 
											}
											else if(ProductID==6)
											{
												diff6Pre=RawCases;
											 
											}
											
											else if(ProductID==7)
											{
												diff7Pre=RawCases;
											 
											}
											else if(ProductID==8)
											{
												diff8Pre=RawCases;
											 
											}
											else if(ProductID==9)
											{
												diff9Pre=RawCases;
											 
											}
											
											tdCounter++;
											%>
											<td style="text-align: right;"><%if(ZeroStockOutletFlag==1){%><%=RawCases %><%} %></td>
											<%  
											
										
										
										
		
									}// End of ProductID Query
								
				//---------Query for Previous Stock Ends----------------//	
				
				
				
				
				
				
				
								if(SecondPrintDate!=null && Utilities.getYearByDate(SecondPrintDate)!=2001){
									%>
									 <td style="text-align: right;"><%=Utilities.getDisplayDateFormat(SecondPrintDate) %></td>
									
									<%
									tdCounter++;
								}else{
									tdCounter++;
									NoPreviousRecordflag=1; // mean no previous record
									%>
									 <td style="text-align: right;"></td>
									
									<%	
								}
								
								Date MaxCreated0n=null;
								
								Date MaxCreated0n1=null;
								Date MaxCreated0n2=null;
								
								ResultSet rs211=s6.executeQuery("SELECT max(created_on) max_date FROM pep.mobile_retailer_stock where outlet_id="+OutletID+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));    //"+OutletID);
								if(rs211.first()){
									MaxCreated0n1=rs211.getDate("max_date");
									
								}
								
								
								ResultSet rs2116=s6.executeQuery("SELECT max(created_on) max_date FROM pep.mobile_retailer_sm_stock where outlet_id="+OutletID+" and created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate));    //"+OutletID);
								if(rs2116.first()){
									MaxCreated0n2=rs2116.getDate("max_date");
									
								}
								
								
								if (MaxCreated0n1 == null){
									//MaxCreated0n1 = Utilities.getEndDateByMonth(1, 2001);
								}
								if (MaxCreated0n2 == null){
									//MaxCreated0n2 = Utilities.getEndDateByMonth(1, 2001);
								}
								
								
									 /* if(MaxCreated0n2.after(MaxCreated0n1)){
										MaxCreated0n =MaxCreated0n2;
									}else{
										MaxCreated0n =MaxCreated0n1;
									}  */
								
								 if(MaxCreated0n1!=null && MaxCreated0n2!=null){
									if(MaxCreated0n2.after(MaxCreated0n1)){
										MaxCreated0n = MaxCreated0n2;
									}else{
										MaxCreated0n = MaxCreated0n1;
									}
								}else{
									if(MaxCreated0n1==null){
										MaxCreated0n=MaxCreated0n2;
									}else{
										MaxCreated0n=MaxCreated0n1;
									}
								} 
									
									
								//for sales
									Date PreviousCurrentStockDate = Utilities.getDateByDays(MaxCreated0n, -1);
									
								

								//Query for Between sales
								/////////////////////////////////////////////////
								
								long Sale250Between=0;
								long Sale500Between=0;
										
								
								long Sale2504Between=0;
								long Sale2505Between=0;		
								
								long Sale5006Between=0;
								long Sale5007Between=0;	
								
								long Sale2508Between=0;
								long Sale2509Between=0;
										
								
								
										//System.out.println("Hello "+" SELECT sum(total_units*ipv.liquid_in_ml)/250 FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(MaxCreated0n)+" and ipv.product_id=1");
										
									//Sales = Minimum stock take date to Max-1 stock take date	
									
											//System.out.println("SELECT sum(raw_cases) /*sum(total_units*ipv.liquid_in_ml)/250*/ FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(PreviousCurrentStockDate)+" and ipv.product_id=1");
											
								ResultSet rs34 = s7.executeQuery("SELECT sum(raw_cases) /*sum(total_units*ipv.liquid_in_ml)/250*/ FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(PreviousCurrentStockDate)+" and ipv.product_id=1");
								if(rs34.first()){
									Sale250Between=rs34.getLong(1);
								}
								
								ResultSet rs3411 = s7.executeQuery("SELECT sum(raw_cases) /*sum(total_units*ipv.liquid_in_ml)/250*/ FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(PreviousCurrentStockDate)+" and ipv.product_id=2");
								if(rs3411.first()){
									Sale500Between=rs3411.getLong(1);
								}
								
								ResultSet rs3412 = s7.executeQuery("SELECT sum(raw_cases) /*sum(total_units*ipv.liquid_in_ml)/250*/ FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(PreviousCurrentStockDate)+" and ipv.product_id=4");
								if(rs3412.first()){
									Sale2504Between=rs3412.getLong(1);
								}
								
								ResultSet rs3413 = s7.executeQuery("SELECT sum(raw_cases) /*sum(total_units*ipv.liquid_in_ml)/250*/ FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(PreviousCurrentStockDate)+" and ipv.product_id=5");
								if(rs3413.first()){
									Sale2505Between=rs3413.getLong(1);
								}
								
								
								ResultSet rs3414 = s7.executeQuery("SELECT sum(raw_cases) /*sum(total_units*ipv.liquid_in_ml)/250*/ FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(PreviousCurrentStockDate)+" and ipv.product_id=6");
								if(rs3414.first()){
									Sale5006Between=rs3414.getLong(1);
								}
								
								ResultSet rs3415 = s7.executeQuery("SELECT sum(raw_cases) /*sum(total_units*ipv.liquid_in_ml)/250*/ FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(PreviousCurrentStockDate)+" and ipv.product_id=7");
								if(rs3415.first()){
									Sale5007Between=rs3415.getLong(1);
								}
								
								ResultSet rs3416 = s7.executeQuery("SELECT sum(raw_cases) /*sum(total_units*ipv.liquid_in_ml)/250*/ FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(PreviousCurrentStockDate)+" and ipv.product_id=8");
								if(rs3416.first()){
									Sale2508Between=rs3416.getLong(1);
								}
								
								ResultSet rs3417 = s7.executeQuery("SELECT sum(raw_cases) /*sum(total_units*ipv.liquid_in_ml)/250*/ FROM pep.inventory_sales_adjusted isa join inventory_sales_adjusted_products isap on isa.id=isap.id join inventory_products_view ipv on isap.product_id=ipv.product_id and isa.outlet_id="+OutletID+" and isa.created_on between "+Utilities.getSQLDate(SecondMaxCreated0n)+" and "+Utilities.getSQLDate(PreviousCurrentStockDate)+" and ipv.product_id=9");
								if(rs3417.first()){
									Sale2509Between=rs3417.getLong(1);
								}
								
								
								
								%>
								<td style="text-align: right; background-color:#F6F6F6; border:1px solid #eee;"><%if(Sale250Between!=0){%><%=Sale250Between %><%} %></td>
								<td style="text-align: right; background-color:#F6F6F6; border:1px solid #eee;"><%if(Sale500Between!=0){%><%=Sale500Between %><%} %></td>
								
								<td style="text-align: right; background-color:#F6F6F6; border:1px solid #eee;"><%if(Sale2504Between!=0){%><%=Sale2504Between %><%} %></td>
								<td style="text-align: right; background-color:#F6F6F6; border:1px solid #eee;"><%if(Sale2505Between!=0){%><%=Sale2505Between %><%} %></td>
								
								<td style="text-align: right; background-color:#F6F6F6; border:1px solid #eee;"><%if(Sale5006Between!=0){%><%=Sale5006Between %><%} %></td>
								<td style="text-align: right; background-color:#F6F6F6; border:1px solid #eee;"><%if(Sale5007Between!=0){%><%=Sale5007Between %><%} %></td>
								
								<td style="text-align: right; background-color:#F6F6F6; border:1px solid #eee;"><%if(Sale2508Between!=0){%><%=Sale2508Between %><%} %></td>
								<td style="text-align: right; background-color:#F6F6F6; border:1px solid #eee;"><%if(Sale2509Between!=0){%><%=Sale2509Between %><%} %></td>
								
								
								<%
								//////
								
								
								
								
								
			//---------Query for Present Stock Starts----------------//		
								
			
			
								ResultSet rs223 = s3.executeQuery("select product_id,package_label from inventory_products_view where product_id in (1,2,4,5,6,7,8,9) order by product_id asc"); //distributor query
								while(rs223.next()){
									ProductID = rs223.getInt("product_id");
									long CurrentTotalRawCases =0;
									long RawCases2=0;
									
									//ResultSet rs22 = s5.executeQuery("SELECT sum(raw_cases) rawcase FROM pep.mobile_retailer_sm_stock where outlet_id="+OutletID+" and created_on between "+Utilities.getSQLDate(MaxCreated0n)+" and "+Utilities.getSQLDateNext(MaxCreated0n)+" and product_id="+ProductID+OrderBookerIDsWhere); //distributor query
									ResultSet rs22 = s5.executeQuery("SELECT sum(raw_cases) rawcase FROM pep.mobile_retailer_sm_stock where outlet_id="+OutletID+" and created_on between "+Utilities.getSQLDate(MaxCreated0n)+" and "+Utilities.getSQLDateNext(MaxCreated0n)+" and product_id="+ProductID+OrderBookerIDsWhere); //distributor query
									if(rs22.first()){
										//RawCases2= rs22.getLong("rawcase");
										
										CurrentTotalRawCases=rs22.getLong("rawcase");
										
									}	
									
									ResultSet rs221 = s5.executeQuery("SELECT sum(raw_cases) rawcase FROM pep.mobile_retailer_stock where outlet_id="+OutletID+" and created_on between "+Utilities.getSQLDate(MaxCreated0n)+" and "+Utilities.getSQLDateNext(MaxCreated0n)+" and product_id="+ProductID+OrderBookerIDsWhere); //distributor query
									if(rs221.first()){
										//RawCases2= rs22.getLong("rawcase");
										
										CurrentTotalRawCases+=rs221.getLong("rawcase");
										
									}
										
										
									RawCases2 = CurrentTotalRawCases;
										
										
										
										if(ProductID==1)
										{
											diff1Ava=RawCases2;
										}
										else if(ProductID==2)
										{
											diff2Ava=RawCases2;
										
										}
										
										else if(ProductID==4)
										{
											diff4Ava=RawCases2;
										
										}
										
										else if(ProductID==5)
										{
											diff5Ava=RawCases2;
										
										}
										
										else if(ProductID==6)
										{
											diff6Ava=RawCases2;
										
										}
										
										else if(ProductID==7)
										{
											diff7Ava=RawCases2;
										
										}
										else if(ProductID==8)
										{
											diff8Ava=RawCases2;
										
										}
										else if(ProductID==9)
										{
											diff9Ava=RawCases2;
										
										}
										
										%>
										<td style="text-align: right;"><%=RawCases2 %></td>
										<%  
										
									
									
		
								}// End of ProductID Query
								
								if(MaxCreated0n!=null){
									%>
									 <td style="text-align: right;"> <%=Utilities.getDisplayDateFormat(MaxCreated0n) %></td>
									
									<%
								}else{
									%>
									 <td style="text-align: right;"></td>
									
									<%
								}
								
								
								
								
								
								
								
								
			//---------Query for  Present Stock Ends----------------//	

								//System.out.println("SELECT DATEDIFF('"+ MaxCreated0n+"','"+SecondPrintDate+"') number_of_days ");
								ResultSet rs31 = s5.executeQuery("SELECT DATEDIFF('"+ MaxCreated0n+"','"+SecondPrintDate+"') number_of_days "); //distributor query
								if(rs31.first()){
									DifferenceOfdays= rs31.getInt("number_of_days");
								}

							if(NoPreviousRecordflag==0){
							%>
								<td style="text-align: right;"><%=(diff1Pre+Sale250Between)-diff1Ava %></td>  
								<td style="text-align: right;"><%=(diff2Pre+Sale500Between)-diff2Ava %></td>  
								
								
								<td style="text-align: right;"><%=(diff4Pre+Sale2504Between)-diff4Ava %></td>  
								<td style="text-align: right;"><%=(diff5Pre+Sale2505Between)-diff5Ava %></td>  
								
								<td style="text-align: right;"><%=(diff6Pre+Sale5006Between)-diff6Ava %></td>  
								<td style="text-align: right;"><%=(diff7Pre+Sale5007Between)-diff7Ava %></td> 
								
								<td style="text-align: right;"><%=(diff8Pre+Sale2508Between)-diff8Ava %></td>  
								<td style="text-align: right;"><%=(diff9Pre+Sale2509Between)-diff9Ava %></td>  
								
								<td style="text-align: right;"><%=DifferenceOfdays %></td>
							 
							</tr>	
								<%
							}else{//end of flag
								%>
								<td style="text-align: right;"></td>  
								<td style="text-align: right;"></td>  
								<td style="text-align: right;"></td>
								
								<td style="text-align: right;"></td>  
								<td style="text-align: right;"></td>  
								<td style="text-align: right;"></td>
								<td style="text-align: right;"></td>
								<td style="text-align: right;"></td>
								
								<td style="text-align: right;"></td>
								
								
								<%
							}
							}// End of Outlet loop
								
								%>
						
						
						<tr>
							<th colspan="41" data-priority="1"  style="text-align:center; ">&nbsp;</th>
							
						</tr>
						
					</tbody>
							
				</table>
		</td>
	</tr>
</table>

	</li>	
</ul> 
    
     </div>
</div>


<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>