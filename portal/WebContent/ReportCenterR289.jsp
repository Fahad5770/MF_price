<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<style>
#map {
        width: 100%;
        height: 300px;
        margin-top: 10px;
      }
</style>

<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 359;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


int Flag=Utilities.parseInt(request.getParameter("flag"));
long PJPID=Utilities.parseLong(request.getParameter("PJPID"));


if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s6 = c.createStatement();
Statement s7 = c.createStatement();

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

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	/*
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	//
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}*/
}

String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and isdic.outlet_id in ("+OutletIds+") ";	
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
	WhereDistributors = " and isdic.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}




%>



			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-13px;" data-icon="false">
			<li data-role="list-divider" data-theme="a">Summary</li>
			<li>
			
			
				<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
					<tr>
						
						<td style="width: 100%" valign="top">
							
						<%-- 	<%if(Flag==1){  --%> 
							<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
									 <thead>
									    <tr style="font-size: 11px;">
										
											<th data-priority="1"  style="text-align:center; width: 10%" >Outlet</th>
											<th data-priority="1"  style="text-align:center; width: 5%" >Dispatch #</th>
											<th data-priority="1"  style="text-align:center; width: 5%" >Invoice #</th>
											<th data-priority="1"  style="text-align:center; width: 10%" >Distributor</th>
											<th data-priority="1"  style="text-align:center; width: 10%" >Cash Received By</th>
											<th data-priority="1"  style="text-align:center; width: 5%" >Cash Received On</th>
											<th data-priority="1"  style="text-align:center; width: 5%" >Received Cash</th>
											
											
											
									    </tr>
									   
									  </thead> 
									
								<%
									double TotalArray[]=new double[1];
									//System.out.println("SELECT distinct id, name FROM common_outlets where id in (select outlet_id from distributor_beat_plan_schedule dbps where id="+PJPID+")");
									
										int OutletID = 0;
										String OutletN= "";
										
										
										long DispatchID = 0;
										long InvoiceID = 0;
										
										
										Date CreatedOn=null;
										long CreatedBy = 0;
										String CreatedByName ="";
										
										double Recieved= 0.0;
										double ActualCash=0.0;
										long DistributorID=0;
										String DistributorName="";
										
										
										
										//System.out.println("sSELECT isdic.dispatch_id,isdic.invoice_id,isdic.distributor_id,(select distinct cd.name from common_distributors cd where cd.distributor_id=isdic.distributor_id) distributor_name,isdic.cash_received,isdic.created_by,(select u.DISPLAY_NAME from users u where u.id=isdic.created_by) createdByname FROM pep.inventory_sales_dispatch_invoices_collection isdic where isdic.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" and  isdic.outlet_id="+OutletID);
										ResultSet rs2 = s2.executeQuery("SELECT isdic.dispatch_id,isdic.invoice_id,isdic.distributor_id,(select distinct cd.name from common_distributors cd where cd.distributor_id=isdic.distributor_id) distributor_name,isdic.cash_received,isdic.created_by,(select u.DISPLAY_NAME from users u where u.id=isdic.created_by) createdByname,isdic.created_on,isdic.outlet_id,(SELECT name FROM common_outlets where id=isdic.outlet_id) outletname FROM pep.inventory_sales_dispatch_invoices_collection isdic where isdic.created_on between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+WhereOutlets+WhereDistributors);
										while(rs2.next()){
											 CreatedOn=rs2.getDate("created_on");
											 CreatedBy = rs2.getLong("created_by");
											 CreatedByName = rs2.getString("createdByname");
											 Recieved=rs2.getDouble("cash_received");
											 DispatchID=rs2.getLong("dispatch_id");
											 InvoiceID=rs2.getLong("invoice_id");
											 OutletID = rs2.getInt("outlet_id");
											 OutletN= rs2.getString("outletname");
											 DistributorID=rs2.getInt("distributor_id");
											 DistributorName=rs2.getString("distributor_name");
											
											 TotalArray[0]+=Recieved;
											 
											 
											 
											 %>
												
												<tr style="font-size: 12px;">
													<td style="padding-left:20px"><%=OutletID%> - <%=OutletN %></td>
							    					<td style="text-1align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%  if(DispatchID!=0){%><%=DispatchID%><%} %></td>
							    					<td style="text-1align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%  if(InvoiceID!=0){%><%=InvoiceID%> <%} %></td>
							    				
							    					<td style="text-a1lign:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"><%  if(DistributorID!=0){%><%=DistributorID%> - <%=DistributorName %><%} %></td>
							    					<td style="text-a1lign:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec"> <%  if(CreatedBy!=0){%><%=CreatedBy%> - <%=CreatedByName %><%} %></td>
							    					<td style="text-1align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" ><%if(CreatedOn!=null){%><%=Utilities.getDisplayDateFormat(CreatedOn) %><%} %></td>
							    					<td style="text-align:right; border-left: 1px solid #ececec; border-right: 1px solid #ececec" ><%if (Recieved!=0){ out.print(Utilities.getDisplayCurrencyFormat(Recieved));} %></td>
							    					
							    					
							    		    	</tr>
												
												<%
											 
											 
											 
											 
											 
											
										}
										
										
									
										
										
										
									
									
									
												
								%>
									<tr>
										
										<td colspan="6"><b>Total</b></td>
										<%
										for(int i=0;i<TotalArray.length;i++){
										%>
										
										<td style="text_alin:right"><b><%=TotalArray[i] %></b></td>
										<%
										}
										%>
									</tr>			
						    		    			
								</table>
								<%
							/* }else {
								
							} */
								%>
								
								
						</td>
					</tr>
					
					
					
				</table>
			
				</li>	
			</ul>
			<%
			
			
			%>

<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>