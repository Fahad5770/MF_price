<%@page import="com.sun.mail.imap.protocol.Status"%>
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
td{
font-size: 8pt;
}
th{
font-size: 8pt;
}

#map {
        width: 100%;
        height: 400px;
        margin-top: 10px;
      }

</style>


<script>
	
function Action(OrderID,ActionID){
	var DeclineReason;
	var HoldReason=document.getElementById("txthold").value;
	var HoldReasonCheckBox = document.querySelector('input[name="radio-choice-v-6"]:checked').value;
	var SapOrder=$('#sapNumber').val();

	
if(ActionID==4)
{
		
	var DeclineReason1 = prompt("Please enter Decline Reason:");
    if (DeclineReason1 == null || DeclineReason1 == " " || DeclineReason1.length<4) {
    alert("Please Enter Decline Reason");

        return false;
   	}else {
    	DeclineReason = DeclineReason1;
    }
}

else if(ActionID==5)
{
		
	
    if (HoldReasonCheckBox == null || HoldReasonCheckBox == " ") {
    alert("Please Enter Hold Reason");

        return false;
   	}
}
else
	{
	if( SapOrder == "" ){
		alert("Please Enter Sap number");
		$('#sapNumber').focus();
		return false;
	}else{
		if( isInteger( $('#sapNumber').val() ) == false ){
			alert("Sap Number must be an Integer.");
			$('#sapNumber').focus();
			return false;
		}
	}
	}
	
	$.mobile.showPageLoadingMsg();
	$.ajax({
		url:'inventory/InventoryDeliveryOrderUpdateExecute',
		data:  {
			OrderID:OrderID,ActionID:ActionID,SapOrder:SapOrder,DeclineReason:DeclineReason,HoldReason:HoldReason,HoldReasonCheckBox:HoldReasonCheckBox	
		}, 
		type: 'post',
		dataType :'json',
		  success: function( json ) {
			  $.mobile.hidePageLoadingMsg();
			  if(json.exists=="true"){
				  if(ActionID==2){
					  alert(json.Cmsg);
					window.location = 'ReportCenter.jsp?ReportID=186';
					'#PendingOrdersList'
				  }else if (ActionID==4){
					  alert(json.Dmsg);
					  window.location = 'ReportCenter.jsp?ReportID=186';
				  }else if (ActionID==5){
					  alert(json.Dmsg);
					  window.location = 'ReportCenter.jsp?ReportID=186';
				  }
					
					
			}else {
				alert("Data could not be saved.");
			}
				
			},
			error:function(xhr, status){
				alert("Server could not be reached.");
				$.mobile.loading( 'hide');
			}
		
	});
}



</script>


<%


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 333;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();
Statement s11=c.createStatement();

//int CensusID = Utilities.parseInt(request.getParameter("CensusID"));
String WhereCensusID ="";

//System.out.println(CensusIDString);




//Distributor

boolean IsDistributorSelected=false;
long SelectedDistributorsArray[] = null;
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
	WhereDistributors = " and census_distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
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
	WhereDistributors = " and dbpauov.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//Outlet Type


String OutletTypes="";
String SelectedOutletTypeArray[]={};
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType") != null){
	SelectedOutletTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType");
	//OutletTypes = Utilities.serializeForSQL(SelectedOutletTypeArray);
}

String WhereDiscountedAll = "";
String WhereDiscountedFixed = "";
String WhereDiscountedPerCase = "";
String WhereActive = "";
String WhereDeactivated = "";
String WhereNonDiscountedAll ="";


for(int i=0;i<SelectedOutletTypeArray.length;i++){
	if(SelectedOutletTypeArray[i].equals("Discounted - All")){	
		WhereDiscountedAll = " and co.id in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Fixed")){	
		WhereDiscountedFixed = " and co.id in (select outlet_id from sampling where active = 1 and date(now()) between fixed_valid_from and fixed_valid_to and fixed_company_share != 0) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Discounted - Per Case")){	
		WhereDiscountedPerCase = " and co.id in (select distinct outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and date(now()) between sp.valid_from and sp.valid_to) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Non Discounted")){	
		WhereNonDiscountedAll = " and co.id not in (select outlet_id from sampling where active = 1) ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Active")){	
		WhereActive = " and co.is_active=1 ";
	}
	
	if(SelectedOutletTypeArray[i].equals("Deactivated")){	
		WhereDeactivated = " and co.is_active=0 ";
	}
}


%>


			
				
<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;width:100%" data-icon="false">
	<li data-role="list-divider" data-theme="a">Summary</li>
	<li width="90%">

<!-- //< %if(OutletIds.length()>0){ %>  -->
		<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
				
				
				<tr>
					<td colspan="2">
					
							<ul data-role="listview" data-inset="true" data-theme="d" data-divider-theme="c" data-count-theme="d" style="width: 100%;margin:0px !important;" > 
							
								<li>
									<table border=0 style="font-size:14px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
						         		<thead>
								           <tr class="ui-bar-d">
								             <th style="width:50%">Packages</th>
								             <th style="text-align: right;width:50%">Cases</th> 
								           </tr>
								         </thead>
								         <tbody>
						         <%
						        
////////////////////////////////////////////////////////////////////////////////////////////////						         
						         
						    	
					   			String dds=request.getParameter("FromDate");
							 	String ID=request.getParameter("ID");
							 	// System.out.println("in SummaryData");
							        
							 	 	int id=0;
							 	 	String Package="";
							 	 	int PackageID=0;
							 	 	long RawCasesPackages=0;
							 	 	String Brands="";
							 	 	long RawCasesBrands=0;
							    	ResultSet rs11 = s11.executeQuery("select ipv.package_id pid,ido.id id,ipv.package_label Packge,sum(idop.raw_cases) RAW_Cases from inventory_delivery_order ido inner join inventory_delivery_order_products idop   on ido.id=idop.id inner join inventory_products_view ipv   on idop.product_id=ipv.product_id where ido.id="+ID+" group by ipv.package_label,ido.id ");
							    //	System.out.println("SELECT DISTINCT id from inventory_delivery_order ");
//							    	ResultSet rs11 = s11.executeQuery("SELECT DISTINCT id from inventory_delivery_order  where created_on between "+Utilities.getSQLDate(SDate)+" and "+Utilities.getSQLDate(EDate)+"");
										while (rs11.next()){
											PackageID=rs11.getInt("pid");
											id=rs11.getInt("id");
											Package=rs11.getString("Packge");	
											RawCasesPackages=rs11.getLong("RAW_Cases");
										   %>
										  
										<tr  class="ui-bar-c">
											<td style="font-weight:bold"><%=Package%></td> 
											<td style="text-align:right;font-weight:bold"><%=RawCasesPackages%></td>
										</tr> 
										<% 
							    		ResultSet rs1 = s2.executeQuery("select ipv.brand_label Brand,idop.raw_cases RAW_Cases from inventory_delivery_order ido inner join inventory_delivery_order_products idop   on ido.id=idop.id inner join inventory_products_view ipv   on idop.product_id=ipv.product_id where ido.id="+ID+" and ipv.package_id="+PackageID+"   ");
										while (rs1.next()){
											Brands=rs1.getString("Brand");	
											RawCasesBrands=rs1.getLong("RAW_Cases");
										   %>
										<tr style="text-align: left; "><td style="padding-left: 30px;padding-right: 224px;"><%=Brands%></td> 
										<td style="text-align: right;padding-left: 30px;padding-right: 30px;"><%=RawCasesBrands%></td></tr>

								<%}%>

							<%}%>		
						         
						           <tr>
						             <th>Total</th>
						             	<%
											long TotalPacks=0;
											ResultSet rs33= s2.executeQuery("select  sum(idop.raw_cases) RAW_Cases  from inventory_delivery_order ido inner join inventory_delivery_order_products idop   on ido.id=idop.id inner join inventory_products_view ipv   on idop.product_id=ipv.product_id where ido.id="+ID+"");
											if(rs33.next())
											{
												TotalPacks=rs33.getLong("RAW_Cases");
											}
										%>
						             <td style="text-align: right;font-weight:bold"><%=TotalPacks%></td>
						            
						             
						           </tr>
						           
						         <%
        
        int Status_type_id=0;
              String CNICNo="";
              String DeliveryMethod="";
              String VehicleNo="";
        System.out.println("select status_type_id from inventory_delivery_order where id="+ID+"");
        ResultSet rsStatus=s.executeQuery("select ido.status_type_id,ido.cnic_no,(select label from inventory_delivery_order_pickup_type idot where idot.id=ido.order_pickup_type ) order_pickup,ido.vehicle_no from inventory_delivery_order ido where id="+ID+"");
        if(rsStatus.first())
        {
         Status_type_id=rsStatus.getInt("status_type_id");
         CNICNo=rsStatus.getString("cnic_no");
         DeliveryMethod=rsStatus.getString("order_pickup");
         VehicleNo=rsStatus.getString("vehicle_no");
        }
        if(CNICNo==null || CNICNo=="null" || CNICNo.length()<1 || CNICNo.equals("null"))
        {
         CNICNo=" ";
        }
        if(VehicleNo==null || VehicleNo=="null" || VehicleNo.length()<1 || VehicleNo.equals("null"))
        {
         VehicleNo=" ";
        }
        if(DeliveryMethod==null || DeliveryMethod=="null" || DeliveryMethod.length()<1 || DeliveryMethod.equals("null"))
        {
         DeliveryMethod=" ";
        }
        //System.out.println(Status_type_id+" Status Type Id4325");
        %>  
              
              
              <tr>
         <td colspan="2" style="border:unset">
          <table width="100%">
           <tr>
            <td colspan="2" style="border:unset">
               <div style="display:inline">
               <h6>Delivery Summary</h6>
                <span style="float: left;line-height: 3.5;font-size:11px"><b>CNIC : </b><%=CNICNo %></span>
                <span style="float: left;line-height: 3.5;margin-left: 10%;font-size:11px"><b>Delivery Method : </b><%=DeliveryMethod %> </span>
                <span style="float: left;line-height: 3.5;margin-left: 10%;font-size:11px"><b>Vehicle No : </b><%=VehicleNo %> </span>
               
               </div>
              </td>
               </tr>
            </table>
          </td>
        </tr>
						           
								<tr style="visibility: <% if(Status_type_id==2 || Status_type_id==3 || Status_type_id==4){ %>hidden;<%} %><% else{ %>visible;<%} %>"  >
									<td colspan="2" style="border:unset">
										<table width="100%">
											<tr>
												<td colspan="2" style="border:unset">
									  				<div style="display:inline">
										  				<span style="float: left;line-height: 3.5;margin-right: 10%;margin-left: 10%;font-size:11px; font-weight: bold">Sap Order : </span>
										  		  		<div  style="float: left;width: 60%;">
										  		  			<input  style="height: 26px;" type="text" name="sapNumber" id="sapNumber" />
									  					</div>
									  				</div>
									  			</td>
									     	</tr>
									   </table>
									 </td>
								</tr>	
								
																<tr>
									<td colspan="2" style="border:unset;" id="declinediv" >
									 </td>
								</tr>	
									
								</tbody>
								</table>
								
								
								
								
								<table width="100%">
								  	<tr style="text-align:right">
								  		<td width="80%"></td>
								  		<td></td>
								  		<td></td>
								  		<td style="text-align:right">
								  		<input type="hidden" name="btnId" id="btnId" value="1">
								  			<a href="#" data-icon="check" data-ajax="false" data-theme="b" style="visibility: <% if(Status_type_id==2 || Status_type_id==3 || Status_type_id==4){ %>hidden;<%} %><% else{ %>visible;<%} %>" data-mini="true" data-role="button" data-inline="true"  onclick="Action(<%=ID %>,2)"   >Confirm</a>
									   	</td>
									   	<td style="text-align:right">
									   		<a href="#" data-icon="check" data-ajax="false" data-theme="a" style="visibility: <% if(Status_type_id==4 || Status_type_id==2 || Status_type_id==3){ %>hidden;<%} %><% else{ %>visible;<%} %>" data-mini="true" data-role="button" data-inline="true"  onclick="Action(<%=ID %>,4)">Decline</a>
									   	</td>
									   	<td style="text-align:right">
<%-- 									   		<a href="#" 		  data-icon="check" data-ajax="false" data-theme="c" data-mini="true" data-role="button" data-inline="true"   onclick="Action(<%=ID %>,5)">On Hold</a> --%>
									   		<a href="#popupLogin"  data-icon="check"  data-rel="popup" style="visibility: <% if(Status_type_id==5 || Status_type_id==4 || Status_type_id==2 || Status_type_id==3){ %>hidden;<%} %><% else{ %>visible;<%} %>"  data-position-to="window" data-role="button" data-mini="true" data-transition="pop">On Hold</a>
									   	</td>
								   	</tr>
								 </table>
								</li>
							</ul>											
									           
		
					</td>
			</tr>
		</table>
<!-- < %
%>
< %}else{%>
	
	
	Please Select Outlet. 
	
	
< %
}
%>  
				
				

			< % }	%>-->
					
			</ul>






 <div data-role="popup" id="popupLogin" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:500px; max-height: 500px" aclass="ui-corner-all">
<div data-role="header" data-theme="a" class="ui-corner-top">
   <h4 style="padding-left: 20px">Please Select Hold Reason:</h4>
   </div>
    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
    <form>
        <div style="padding:10px 20px;">
           
	
    <fieldset data-role="controlgroup" >
       
       
        <input type="radio" name="radio-choice-v-6" id="radio-choice-v-6a" value="Due to Cash" checked="checked">
        <label for="radio-choice-v-6a">Due to Cash</label>
        <input type="radio" name="radio-choice-v-6" id="radio-choice-v-6b" value="Due to Empty">
        <label for="radio-choice-v-6b">Due to Empty</label>
        <input type="radio" name="radio-choice-v-6" id="radio-choice-v-6c" value="Due to Stock Short">
        <label for="radio-choice-v-6c">Due to Stock Short</label>
        <input type="radio" name="radio-choice-v-6" id="radio-choice-v-6d" value="Due to Promo Issue">
        <label for="radio-choice-v-6d">Due to Promo Issue</label>
        
    </fieldset>
<div class="ui-field-contain" style="width: 100%">
<label for="textarea">Comments:</label><br>
  <textarea style="width:100%;height:80px" name="textarea" id="txthold"></textarea>
</div>

            <a href="#" data-icon="check" data-ajax="false" data-theme="c" data-mini="true" data-role="button" data-inline="true"   onclick="Action(<%=ID %>,5)">Update</a>
        </div>
    </form>
</div>
</div>


























<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>