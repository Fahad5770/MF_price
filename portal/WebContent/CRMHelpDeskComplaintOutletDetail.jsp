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

<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyCqs-cbqKOGoUtn7gpVFKVo_vyGBG53bAY&callback=initMap"></script> 
  <%

  

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));


  long OutletId15=0;         
          
  OutletId15=Utilities.parseLong(request.getParameter("oid"));
        
           //OutletIds="501";
%>



        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >


  
  <%
  
  
  
  if(OutletId15!=0){
  
  %>
  

		<form data-ajax="false" id="DeskSaleFormDateRange" onSubmit="return showSearchContent()">
			<ul data-role="listview" data-inset="true"  style=" font-weight: normal; margin-top:-10px;" data-icon="false">

<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
				
			<%if(1==1){ %>
			<div class="ui-grid-a">
			    <div class="ui-block-a" style="width:100%">
			    	<div class="ui-bar" style="min-height:60px;">
			    		<table border="0" style="width:100%; " class="GridWithBorder">
							<%
							double lat=0;
							double longi=0;
							String OutletName="";
							
							String SQL = "SELECT name, address, lat, lng FROM common_outlets where id = '"+OutletId15+"'";
							
							ResultSet rs = s.executeQuery(SQL);
							if(rs.first()){
								lat = rs.getDouble("lat");
								longi = rs.getDouble("lng");
								OutletName = rs.getString("name");
							%>
							
							<tr>
								<td>Outlet</td>
								<td><%=OutletId15 %> - <%=rs.getString("name") %></td>
							</tr>
							
							
							<tr>
								<td>Address</td>
								<td><%=rs.getString("address") %></td>
							</tr>
							<%
							}
							%>
					    </table>
					    <ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">Contact Information</li>
						</ul>
					    <table class="GridWithBorder" style="font-size: 10pt; width:100%; margin-top:10px">
						
							<tr >
								<th data-priority="1" style="width:25%">Contact Name</th>
								<th data-priority="1" style="width:25%">Number</th>
								<th data-priority="1" style="width:25%">NIC</th>
								<th data-priority="1" style="width:25%">Relation</th>
							</tr>
							<%
							//System.out.println("SELECT id, contact_name, contact_number, contact_nic, type_id, (SELECT label FROM common_outlets_contacts_types where id=5) relation FROM common_outlets_contacts where outlet_id="+OutletIds+" and type_id=5 and is_primary=1 ");
							ResultSet rs2 = s2.executeQuery("SELECT id, contact_name, contact_number, contact_nic, type_id, (SELECT label FROM common_outlets_contacts_types where id=5) relation FROM common_outlets_contacts where outlet_id='"+OutletId15+"' and type_id=5 and is_primary=1 ");
							while(rs2.next()){
							%>
							<tr>
								<td style="width:25%"><%=rs2.getString("contact_name") %></td>
								<td style="width:25%"><%=rs2.getString("contact_number") %></td>
								<td style="width:25%"><%=rs2.getString("contact_nic") %></td>
								<td style="width:25%"><%=rs2.getString("relation") %></td>
							</tr>
							<%
							}
							%>
						
					</table>
					 <ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">Area</li>
					 </ul>
					<table class='GridWithBorder' style='font-size: 10pt; width:100%; margin-top:10px'>
						
	   						<tr >
	   							<th style="width:25%">PJP</th>
	   							<th style="width:25%">Distributor</th>
	   							<th style="width:25%">Order Booker</th>
	   							<th style="width:25%">Product Group</th>
	   						</tr>
	   						<%
	   						ResultSet rs21 = s2.executeQuery("SELECT distinct id,label,distributor_id,distributor_name,product_group_id,product_group_name,assigned_to,assigned_to_name FROM distributor_beat_plan_all_view where outlet_id='"+OutletId15+"'");
	   						while(rs21.next()){
	   						
	   						%>
	   						<tr>
	   							<td style="width:25%"><%=rs21.getString("id") %> - <%=rs21.getString("label") %></td>
	   							<td style="width:25%"><%=rs21.getString("distributor_id") %> - <%=rs21.getString("distributor_name") %></td>
	   							<td style="width:25%"><%=rs21.getString("assigned_to") %> - <%=rs21.getString("assigned_to_name") %></td>
	   							<td style="width:25%"><%=rs21.getString("product_group_id") %> - <%=rs21.getString("product_group_name") %></td>
	   						</tr>
	   						<%
	   						}
	   						%>
				    	
					</table>
					 <ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">TOT</li>
					 </ul>
					<table class='GridWithBorder' style='font-size: 10pt; width:100%; margin-top:10px'>
						
	   						<tr >
	   							<th>Asset Number</th>
	   							<th>Inventory Number</th>
	   							<th>Agreement Record</th>
	   							<th>Rnd Verification</th>
	   							<th>250ml Quantity</th>
	   							<th>Litre Quantity</th>
	   							<th>Tot Status</th>
	   							<th>Movement Date</th>
	   							
	   						</tr>
	   						<%
	   						ResultSet rs3 = s2.executeQuery("SELECT inventory_number,agreement_record,rnd_verification,250ml_quantity,litre_quantity,tot_status,movement_date,main_asset_number FROM common_assets where outlet_id_parsed='"+OutletId15+"' order by movement_date_parsed desc");
	   						while(rs3.next()){
	   						
	   						%>
	   						
	   						<tr>
	   							<td><%=rs3.getString("main_asset_number") %></td>
	   							<td><%=rs3.getString("inventory_number") %></td>
	   							<td><%=rs3.getString("agreement_record") %></td>
	   							<td><%=rs3.getString("rnd_verification") %></td>
	   							<td><%=rs3.getInt("250ml_quantity") %></td>
	   							<td><%=rs3.getInt("litre_quantity") %></td>
	   							<td><%=rs3.getString("tot_status") %></td>
	   							<td><%=rs3.getString("movement_date") %></td>
	   							
	   							
	   						</tr>
	   						
	   						<%
	   						}
	   						%>
				    	
					</table>
					 <ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">Map</li>
					 </ul>
					<table class='GridWithBorder' style='font-size: 10pt; width:100%; margin-top:10px'>
						
						<script>
	
	function initMap(){
		
		  var myLatlng = new google.maps.LatLng(31.427335740000000,73.094063400000000);
		  var mapOptions = {
		    zoom: 15,
		    center: myLatlng
		  };

		  var map = new google.maps.Map(document.getElementById('map'), mapOptions);

		  var contentString = '<div id="content">Outlet ID 6205<br>View Dashboard</div>';

		  var infowindow = new google.maps.InfoWindow({
		      content: contentString,
		      maxWidth: 200
		  });
		  
		  var markers = new Array();
		  
			 
		map.setCenter(new google.maps.LatLng(<%=lat%>,<%=longi%>));
				  
			  
			  markers[0] = new google.maps.Marker({
			      position: new google.maps.LatLng(<%=lat%>,<%=longi%>),
			      map: map,
			      title: '<%=OutletName%>'
			      
			  });
			  google.maps.event.addListener(markers[0], 'click', function() {
			    infowindow.open(map,markers[0]);
			    
			    var infoWindowContent = "<%=OutletId15%>-<%=OutletName%><br>"; 
			    infoWindowContent += "<a href='#'>View Detail</a>";
			    infowindow.setContent(infoWindowContent);
			    //infowi
			  });
			  
	}	 
		  
			 
		    </script>
	
						<tr>
							<td>
								<div id="map"></div>
							</td>
						<tr>
					</table>
			    	
			    	
			    	
			    	
			    	<ul data-role="listview" data-inset="true" >
							<li data-role="list-divider" data-theme="c">Open Complaints</li>
					 </ul>
					<table class='GridWithBorder' style='font-size: 10pt; width:100%; margin-top:10px'>
						
	   						<tr >
	   							<th>Complaint ID</th>
	   							<th>Description</th>
	   							<th>Type</th>
	   							<th>Assigned</th>
	   							<th>Resolved</th>
	   							<th>Verified</th>	   							
	   							
	   						</tr>
	   						
	   						<%
	   						
	   						ResultSet rs1 = s.executeQuery("select cc.id,cc.description,cct.label,cc.is_assigned,if(cc.is_assigned=1,'Yes','No') assigned_label,cc.is_verified,if(cc.is_verified=1,'Yes','No') verified_label,cc.is_resolved, if(cc.is_resolved=1,'Yes','No') resolved_label from crm_complaints cc join crm_complaints_types cct on cc.type_id=cct.id join crm_complaints_assigned cca on cc.id=cca.id where cc.is_verified=0 and cca.outlet_id='"+OutletId15+"'");
	   						while(rs1.next()){
	   						%>
	   						
	   						<tr>
	   							<td><%=rs1.getLong("id") %></td>
	   							<td><%=rs1.getString("description") %></td>
	   							<td><%=rs1.getString("label") %></td>
	   							<td><%=rs1.getString("assigned_label") %></td>
	   							<td><%=rs1.getString("resolved_label") %></td>
	   							<td><%=rs1.getString("verified_label") %></td>	   							
	   						</tr>
	   						<%
	   						}
	   						%>
	   						
				    	
					</table>
			    	
			    	
			    	
			    	</div>
			    </div>
			    <div class="ui-block-b" style="width:0%">
			    	<div class="ui-bar" style="min-height:60px;">
			    		
			    	</div>
			    </div>
			</div><!-- /grid-a -->
			<%}else{ %>
			<p style="padding-top:20px;">Please enter Outlet ID</p>
			<%} %>
			
			
						
					
							
				
		</td>
	</tr>
</table>

	</li>	
</ul>
           
        </form>

        <%
  }else{
	  %>
	  	  	
	  	<p style="padding-top:20px;text-align:center">Please enter a Valid Outlet ID</p>
	  	  
	  <%
  }
        
        
        %>
            
        </div>