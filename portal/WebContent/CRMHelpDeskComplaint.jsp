<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Date"%>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.reports.SalesIndex"%>
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
th{
font-size: 8pt;
}

#map {
        width: 100%;
        height: 200px;
        margin-top: 10px;
      }

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 303;

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

//System.out.println("sDate "+StartDate);

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

//outlet

boolean IsOutletSelected=false;
String OutletIds="";
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	IsOutletSelected=true;
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	OutletIds = Utilities.serializeForSQL(SelectedOutletArray);
}

String WhereOutlets = "";
if (OutletIds.length() > 0){
	WhereOutlets = " and outlet_id in ("+OutletIds+") ";	
}


String UserAccessWhere = "";
if(FeatureID != 0)
{
	Distributor [] DistributorObj = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	String DistributorsIds = UserAccess.getDistributorQueryString(DistributorObj);
	if(!DistributorsIds.equals("")){
		UserAccessWhere = " and distributor_id in("+DistributorsIds+")";
	}
}

//Asset Number

long AssetNumber=0;
String AssetNumberOutletID="";
String AssetNumberWhere="";

           boolean IsAssetNumberSelected=false;

           if (session.getAttribute(UniqueSessionID+"_SR1SelectedAssetNumber") != null){
        	   AssetNumber = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedAssetNumber");	
           	
           	
           }
           if(AssetNumber!=0){
        	   ResultSet rs = s.executeQuery("select outlet_id FROM common_assets where main_asset_number = "+AssetNumber);
        	   if(rs.first()){
        		   AssetNumberOutletID = rs.getString("outlet_id");  
        	   }
           }
           boolean IsValidAssetNumber=false;
           //System.out.println(AssetNumber);
           
           //updating the outlet ids
           if(!AssetNumberOutletID.equals("")){ //invalide asset number
        	   IsValidAssetNumber = true;
           }
           
           if(AssetNumber !=0 && OutletIds.equals("")){ //mean outlet id not select and only asset number select
        	   //System.out.println("asfsd");
        	   OutletIds =AssetNumberOutletID;
           }
           
           
           //OutletIds="501";
%>



 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCqs-cbqKOGoUtn7gpVFKVo_vyGBG53bAY&callback=initMap"></script> 

<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>



<script type="text/javascript" src="js/CRMHelpDeskComplaint.js?12=12"></script>
</head>
<body >
<div data-role="page" id="GLCreditLimitPage" data-url="GLCreditLimitPage" data-theme="d">


    <jsp:include page="Header2.jsp" >
    	<jsp:param value="CRM Helpdesk" name="title"/>
    </jsp:include>
    
  			 
			 <div data-role="content" data-theme="d"  style="margin-left: 10%;">
	
	
	<div class="ui-grid-a" >
	    <div class="ui-block-a" style="width:75%">
	    	<div class="ui-bar " style="min-height:60px">
	    		
				<form name="CrmRegisterComplaintExecute" id="CrmRegisterComplaintForm" >
				
				
				<ul id="CRMRegCOm" data-role="listview" data-inset="false" data-divider-theme="c" style="margin-top:20px; margin-left:2px;">
	    	    
					
			<li>		
				
				
				<table   style="width: 70%;margin-left: 15%;">
					
					
					<tr>
						
						<td>
						 	<fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
						 	   <legend>Category:</legend>
						<%
							int id=0;
							String categoryName="";
							
							String SQL = "SELECT * FROM crm_help_desk_complaint_category ";
							ResultSet rs = s.executeQuery(SQL);
							while(rs.next()){
								id = rs.getInt("id");
								categoryName = rs.getString("label");
								//System.out.println(id);
								//System.out.println(categoryName);
							%>
								<input type="radio" name="category" onclick='getSub("<%=id%>")' <%if(id==1){%>checked="checked"  <%}%> id="Cat_<%=id%>"  value="<%=id%>">
						        <label for="Cat_<%=id%>"><%=categoryName%></label>
						     
						        <!-- <input type="radio" name="COM1" onclick="populateSalesService()" id="SalesServices" checked="checked" value="1">
						        <label for="SalesServices">Sales  Services</label>
						        <input type="radio" name="COM1" onclick="populateQuality()"  id="Quality" value="2" >
						        <label for="Quality">Quality</label>
						        <input type="radio" name="COM1" onclick="populateMEM()"  id="MEM" value="3">
						        <label for="MEM">MEM</label>
							 </fieldset>
							 <select name="category" data-mini="true" id="categoryddl"> -->
							      
						
							
							<%
							}
							%>
							
							</fieldset>
					 </td>
						
					</tr>				
					
					
					<tr>	
						
						<td>
								<div id="SubCat" >
								<fieldset data-role="controlgroup" data-mini="true"  data-iconpos="right">
							        <legend>Sub Category:</legend>
							
							      <input type="radio" name="subcategory" id="SalesServices4" value="1" checked="checked">
							        <label for="SalesServices4">General Complaint</label>
							        <input type="radio" name="subcategory"   id="Quality5" value="2" >
							        <label for="Quality5">Publicity issues</label>
							        <input type="radio" name="subcategory"   id="MEM6" value="3">
							        <label for="MEM6">Sales Team</label>
							        <input type="radio" name="subcategory"  id="SalesServices1" value="4">
							        <label for="SalesServices1">Claims/Schemes</label>
							        <input type="radio" name="subcategory"   id="Quality2" value="5" >
							        <label for="Quality2">Discounts/Incentives Issues</label>
							        <input type="radio" name="subcategory"   id="MEM3" value="6">
							        <label for="MEM3">Stock/Supply Issue</label>
							        
						 		
								 </fieldset>
							</div>
							
							
						
						</td>
					</tr>
					
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					<tr>
					</tr>
					
					<tr>
						<td>
							<table width="100%">
								
								<tr>
									<td colspan="3">
										<input type="text" name="Asset_code" id="Asset_code" data-mini="true" onchange="getOutletNamebyAssetCode()" placeholder="Asset Code">
									</td>
								</tr>
								<tr>
								
									
									<td width="20%">
										<input type="text" name="outlet_id" data-mini="true" id="outletID" onchange="getOutletName()" placeholder="Outlet ID">
									</td>
									<td width="70%">
										<input type="text" name="outlet_name" id="outlet_name" read-only="read-only" data-mini="true"  placeholder="Outlet Name">
									</td>
									<td>
									 <a href="#popupDialog" data-rel="popup" data-icon="check" data-mini="true" data-theme="b" data-role="button" id="ViewButton" data-inline="true"  onclick='popup()'   data-position-to="window" data-transition="pop" id="DeskSaleSearch" >View</a>
									</td>
								</tr>
								<tr>
									<td colspan="3">
										<input type="text" name="tot_code" id="tot_code" data-mini="true"  placeholder="T0T Code">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					
					<tr>
					
						<td><input type="text" name="PersonContactName" data-mini="true" id="PersonContactName" placeholder="Name "></td>
					</tr>
						
					<tr>
						<td><input type="text"  name="ContactNo" data-mini="true"  id="ContactNo" placeholder="Contact Number"></td>
					</tr>
					<tr  style="font-size:13px;">	
						
						<td>
							<textarea Style="height:100px" cols="50" id="Description" name="Description" placeholder="Description..."></textarea>
						</td>						
						
						
					</tr>
					
				  </table>
				    	
			</li>
					
				</ul>
				</form>
				
	    	</div>
	    </div>
	    
	</div><!-- /grid-a -->
	
	
	
	
	
    </div><!-- /content -->
    
    <div style="position:fixed" data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width:15%;">
				<tr>
					<td>
						<a href="#" data-icon="check" data-ajax="false" data-theme="b"  data-role="button" data-inline="true"  id="SubmitButton" onclick='submitForm()' >Save</a>
						<!--  <input type="button" onClick="FormSubmit()" data-icon="check" data-theme="a" data-role="button" value="Click"/> -->
					</td>
					
					<td>
						
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="ResetButton" href="#"  aclass="ui-disabled" >Reset</a>
											
					</td>
	               
				</tr>
			</table>
		</div>
    </div>
  <!--POP hellow --> 
   <script >
		  function popup(){
			
			  
		 	var url ='CRMHelpDeskComplaintOutletDetail.jsp?oid='+$('#outletID').val();
		 	$.mobile.showPageLoadingMsg();
			$.get(url, function(data) {
				  $.mobile.hidePageLoadingMsg();
				$("#viewpopup").html(data);
				 $("#viewpopup").trigger('create');
				 
			});
			
			 
		  }
		  
		//function popup2(){
			  //if($('#outletID').val()!=0 || $('#outletID').val()!=""){
				  //popup();
			  //}
			  //else{
				  //alert("Enter Outlet ID other then '0' or 'empty space 54645654'");
				  
				  
				  //$( "#popupDialog" ).popup( "close" ); 
			  //}
		//}  
  </script>
  
  <div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
  	<div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Outlet Profile</h1>
        </div>
  	<div id="viewpopup"></div>
  
 
  </div>
  
  
   
						
					
							
				
		</td>
	</tr>
</table>

	</li>	
</ul>
           
        </form>

        
            
        </div>
    </div>
    
    

</div>
-->
</body>
</html>
<%
s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>