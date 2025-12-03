<%@page import="com.pbc.employee.EmployeeHierarchy"%>
<%@page import="com.pbc.util.Utilities"%>
<jsp:useBean id="bean" class="com.pbc.workflow.WorkflowManager" scope="page"/>
<jsp:setProperty name="bean" property="*"/>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowAttachment"%>
<%@page import="java.util.Date"%>
 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%
bean.isLoaded();

String PageID = "WorkflowManagerApproval";

if (request.getParameter("SecondPage") == null){
	PageID = "WorkflowManager";
}

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
		<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet"  href="lib/jqm130/jquery.mobile-1.3.0.min.css">
	 <link href="lib/jquery.fineuploader-3.4.1/fineuploader-3.4.1.css" rel="stylesheet">
	
	<script src="lib/jquery-1.9.1.min.js?123=123"></script>
	<script src="lib/jqm130/jquery.mobile-1.3.0.min.js?123=123"></script>
	<script src="lib/jquery-validation/jquery.validate.js?123=123"></script>
	<script src="lib/jqueryui1102/js/jquery-ui-1.10.2.custom.min.js?123=123"></script>
	<script src="lib/jquery.fineuploader-3.4.1/jquery.fineuploader-3.4.1.min.js?123=123"></script>
	
	<title>PBC Enterprise Portal</title>
	<script src="js/WorkflowManagerPromotionRequest.js?1242=1242"></script>
	<link rel="stylesheet"  href="css/home.css">
</head>
<body>

<div data-role="page" id="<%=PageID %>" data-url="<%=PageID %>" data-theme="d">
	  <%
  long RequestID = Utilities.parseLong(request.getParameter("requestID"));
   Datasource ds = new Datasource();
   ds.createConnection();

   Statement s = ds.createStatement();
   Statement s1= ds.createStatement();
   Statement s2= ds.createStatement();
   long PromoID=0;
   
   ResultSet rs11 = s.executeQuery("select id from inventory_sales_promotions_request where request_id="+RequestID);
   if(rs11.first()){
	   PromoID = rs11.getLong("id");  
   }
   
   %>
	
	
	
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Product Promotion Request" name="title"/>
    </jsp:include>
     <!-- /header -->
 <script>
 function CalculateMarginalContribution(){
		$("#MarginalContribution").val($("#SalesSKUPrice").val()-$("#FreeSKUPriceBottles").val()-$("#VariableCost").val());
	}

	function CalculateNetPrice(){
		$("#netprice").val($("#SalesSKUPrice").val()-$("#FreeSKUPriceBottles").val());
	}

 
 
 </script>   
 
    
    
    <div data-role="content" data-theme="d" style="padding: 0px;margin: 0;">
        <fieldset class="ui-grid-a">
				
		        <div class="ui-block-a" style="width: 23%">
						<ul data-role="listview" data-icon="bars" style="margin-top: 1px; padding-top: 0px; margin-left: 0px;">
							<li data-role="list-divider" data-theme="c">Approval Chain<span class="ui-li-count"><%if (bean.isNewRequest){ %>Level 1<%}else{ %> Level <%=bean.STEPS.size() %><%} %></span></li>
							<%
							if (bean.isNewRequest == true){
								%>
								<li>
									<a href="#" >
										<img src="<%=session.getAttribute("UserPictureURL") %>" />
										<h3>Request</h3>
										<p>by <%=session.getAttribute("UserDisplayName") %><br>
										<b>In Process</b></p>
									</a>
								</li>
								<%
							}else{
								for (int i = (bean.STEPS.size()-1); i >= 0 ; i--){

									Date CompletedOn = bean.STEPS.get(i).COMPLETED_ON;
									String ActionLabel = bean.STEPS.get(i).ACTION_LABEL_PAST;
									
									if (CompletedOn == null){
										ActionLabel = bean.STEPS.get(i).ACTION_LABEL;
									}
									
									String UserPicURL = Utilities.getUserPictureURL((bean.STEPS.get(i).USER_ID), request);
								%>
								<li>
									<a href="#" >
										<img src="<%=UserPicURL%>" />
										<h3><%=ActionLabel%></h3>
										<p>by <%=bean.STEPS.get(i).USER_DISPLAY_NAME %><br>
										<b><%
										if (CompletedOn == null){
											if (bean.isDeclined){%>Declined<%}else{%>In Process<%}
										}else{
											%>On <%out.print(Utilities.getDisplayDateTimeFormat(bean.STEPS.get(i).COMPLETED_ON));
										}
										%></b>
										</p>
									</a>
								</li>
								<%
								}
							}
							%>
						</ul>
						<br><br>
						<div id="WorkflowAttachmentsBar">
						</div>									    
		        </div><!-- /ui-block -->
		        <div class="ui-block-b" style="width: 77%">
		        <form name="WorkflowContainerForm" class="WorkflowForm" id="WorkflowContainerForm" method="POST" action="#">
					<ul data-role="listview" data-inset="true" style="margin-left: 22px;margin-top: 6px;margin-right: 5px;">
						<li data-role="list-divider">Promotional Product</li>
						<li>
						
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							<thead>
									
								<tr>
									<th  style="background-color:#efedee; padding:5px;font-size:12px">Package</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px">Brand</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px">Case</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px">Bottle</th>	
								</tr>	
							</thead>
							
							<tbody>
							<%
							String PackageLabel="";
							String BrandLabel="";
							String RawCasesLabel="";
							String UnitLabel="";
							
							String PackageLabel1="";
							String BrandLabel1="";
							String RawCasesLabel1="";
							String UnitLabel1="";
							
							ResultSet rs6 = s1.executeQuery("SELECT distinct isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="+RequestID+" and isprp.type_id=1");
							while(rs6.next()){
								int PackageCounter=1;
								if(PackageCounter==1){
									PackageLabel = rs6.getString("package_labele_id");
									}
								ResultSet rs2 = s2.executeQuery("SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="+RequestID+" and isprp.type_id=1 and isprp.package_id="+rs6.getLong("package_id")+" group by isprp.type_id,isprpb.brand_id");
								while(rs2.next()){
									BrandLabel += rs2.getString("brand_label")+"<br/>";
								}
								
								if (rs6.getLong("raw_cases") != 0){
									RawCasesLabel = rs6.getString("raw_cases");
								}
								if (rs6.getLong("units") != 0){
									UnitLabel = rs6.getString("units");
								}
							
							}
								
							%>
								<tr style="b1ackground-color:#efedee">
									<td style="width:25%; padding:5px;" valign="top"><%=PackageLabel %></td>
									<td style="width:25%; padding:5px;"><%=BrandLabel %></td>
									<td style="width:25%; padding:5px;"valign="top"><%=RawCasesLabel %></td>
									<td style="width:25%; padding:5px;"valign="top"><%=UnitLabel %></td>
								</tr>	
								
							
							</tbody>
						</table> 
						</li>
						
						<li data-role="list-divider">Free Product</li>
						<li>
							<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							<thead>
									
								<tr>
									<th  style="background-color:#efedee; padding:5px;font-size:12px">Package</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px">Brand</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px">Case</th>
									<th  style="background-color:#efedee; padding:5px;font-size:12px">Bottle</th>	
								</tr>	
							</thead>
							
							<tbody>
							
							<%
							
							ResultSet rs17 = s1.executeQuery("SELECT distinct isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_labele_id,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="+RequestID+" and isprp.type_id=2");
							while(rs17.next()){
								int PackageCounter=1;
								if(PackageCounter==1){
									PackageLabel1 = rs17.getString("package_labele_id");
									}
								ResultSet rs2 = s2.executeQuery("SELECT isprp.id,isprp.package_id,(select label from inventory_packages ip where ip.id=isprp.package_id) package_label,isprpb.brand_id,(select label from inventory_brands ib where ib.id=isprpb.brand_id) brand_label,isprp.raw_cases,isprp.units,isprp.total_units,isprp.type_id FROM inventory_sales_promotions_request_products isprp join inventory_sales_promotions_request_products_brands isprpb on isprp.id=isprpb.id and isprp.package_id=isprpb.package_id and isprp.type_id=isprpb.type_id join inventory_sales_promotions_request ispr on ispr.id=isprp.id where ispr.request_id="+RequestID+" and isprp.type_id=2 and isprp.package_id="+rs17.getLong("package_id")+" group by isprp.type_id,isprpb.brand_id");
								while(rs2.next()){
									BrandLabel1 += rs2.getString("brand_label")+"<br/>";
								}
								
								if (rs17.getLong("raw_cases") != 0){
									RawCasesLabel1 = rs17.getString("raw_cases");
								}
								if (rs17.getLong("units") != 0){
									UnitLabel1 = rs17.getString("units");
								}
							
							}
							
							
							%>
							
							
									<tr style="b1ackground-color:#efedee">
									<td style="width:25%; padding:5px;"valign="top"><%=PackageLabel1 %></td>
									<td style="width:25%; padding:5px;"><%=BrandLabel1%></td>
									<td style="width:25%; padding:5px;"valign="top"><%=RawCasesLabel1 %></td>
									<td style="width:25%; padding:5px;"valign="top"><%=UnitLabel1 %></td>
								</tr>	
									
									
									
							</tbody>
						</table> 			
					
						</li>
						
						<li data-role="list-divider">Reason</li>
						<li>
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							
							
							<tbody>
							<%
							ResultSet rs = s.executeQuery("select * from inventory_sales_promotions_request where request_id="+RequestID);
							while(rs.next()){
							%>
							<tr>
							<td><%=rs.getString("comments") %></td>
							
							
							</tr>
							</tbody>

						</table>
													
						</li>
						
						<li data-role="list-divider">Profitability</li>
						<li>
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							
							
							<tbody>
							
								
								<tr>
			<td valign="top">
				<span style="font-size: 10pt; font-weight: 400;">Estimated Sales Volume (Raw Cases)</span>
				<input  type="text" placeholder="Estimated Sales Volume (Raw Cases)" id="EstimatedSalesVolumeRawCases" name="EstimatedSalesVolumeRawCases" data-mini="true" value="<%=rs.getDouble("estimated_sales_volume")%>"<%if(bean.CURRENT_STEP.STEP_ID!=2){ %>readonly<%} %>>
			</td>
			</tr>
			<tr>
			
			<td valign="top">
			<span style="font-size: 10pt; font-weight: 400;">Sales SKU Price</span>
				<input  type="text" placeholder="Sales SKU Price" id="SalesSKUPrice" name="SalesSKUPrice" data-mini="true" onKeyup="CalculateMarginalContribution();CalculateNetPrice();" value="<%=rs.getDouble("sales_sku_price") %>" <%if(bean.CURRENT_STEP.STEP_ID!=2){ %>readonly<%} %>>
			</td>
		</tr>
			<tr>
			<td valign="top">
			<span style="font-size: 10pt; font-weight: 400;">Free SKU Price (Bottle)</span>
				<input  type="text" placeholder="Free SKU Price (Bottle)" id="FreeSKUPriceBottles" name="FreeSKUPriceBottles" data-mini="true" onKeyup="CalculateMarginalContribution();CalculateNetPrice();" value="<%=rs.getDouble("free_sku_price") %>" <%if(bean.CURRENT_STEP.STEP_ID!=2){ %>readonly<%} %>>
			</td>
			<td valign="top">
				
				
			</td>
		</tr>
		<%
		String texttype = "text";
		String vctitle = "Variable Cost + Taxes";
		String mctitle = "Marginal Contribution";
		if (SessionUserID == EmployeeHierarchy.getSDHead(1).USER_ID || SessionUserID == EmployeeHierarchy.getSDHead(2).USER_ID || SessionUserID == EmployeeHierarchy.getSDHead(4).USER_ID){
			texttype = "hidden";
			vctitle = "";
			mctitle = "";
		}
		%>
		<tr>
			<td valign="top">
			<span style="font-size: 10pt; font-weight: 400;"><%=vctitle %></span>
				<input  type="<%=texttype %>" placeholder="Variable Cost + Taxes" id="VariableCost" name="VariableCost" data-mini="true" onKeyup="CalculateMarginalContribution()" value="<%=rs.getDouble("variable_cost_and_taxes") %>" <%if(bean.CURRENT_STEP.STEP_ID!=2){ %>readonly<%} %>>
			</td>
			
		</tr>
		<tr>
			
			<td valign="top">
			<span style="font-size: 10pt; font-weight: 400;"><%=mctitle %></span>
				<input  type="<%=texttype %>" placeholder="Marginal Contribution" id="MarginalContribution" name="MarginalContribution" data-mini="true" class="ui-disabled" value="<%=rs.getDouble("marginal_contribution") %>"/>
			</td>
			<td>
			
			</td>
		</tr>
		
		<tr>
			
			<td valign="top">
			<span style="font-size: 10pt; font-weight: 400;">Net Price</span>
				<input  type="text" placeholder="Net Price" id="netprice" name="netprice" data-mini="true" class="ui-disabled" value="<%=rs.getDouble("net_price") %>">
			</td>
		</tr>
								
								
								
								
								
								
									
								<%
							}
								%>		
							</tbody>

						</table>
													
						</li>
						<%
						ResultSet rs213 = s.executeQuery("SELECT cr.region_short_name FROM inventory_sales_promotions_request_regions isprr join inventory_sales_promotions_request ispr on isprr.product_promotion_id=ispr.id join common_regions cr on cr.region_id=isprr.region_id where ispr.id="+PromoID);
						if(rs213.first()){
						
						%>
						<li data-role="list-divider">Region</li>
						<li>
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							
							
							<tbody>
							<%
							ResultSet rs21 = s.executeQuery("SELECT cr.region_short_name,cr.region_name FROM inventory_sales_promotions_request_regions isprr join inventory_sales_promotions_request ispr on isprr.product_promotion_id=ispr.id join common_regions cr on cr.region_id=isprr.region_id where ispr.id="+PromoID);
							while(rs21.next()){
							%>
								<tr style="backg1round-color:#efedee">
									<td style="width:50%; padding:5px;"><%=rs21.getString("region_short_name") %> - <%=rs21.getString("region_name") %></td>
									
								</tr>	
									
								<%
							}
								%>		
							</tbody>

						</table>
													
						</li>
						<%
						}
						%>
						<%
						ResultSet rs223 = s.executeQuery("SELECT cd.distributor_id,cd.name FROM inventory_sales_promotions_request_distributors isprd join inventory_sales_promotions_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="+PromoID);
						if(rs223.first()){
							
						
						
						%>
						<li data-role="list-divider">Distributor</li>
						<li>
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							
							
							<tbody>
							<%
							ResultSet rs22 = s.executeQuery("SELECT cd.distributor_id,cd.name FROM inventory_sales_promotions_request_distributors isprd join inventory_sales_promotions_request ispr on isprd.product_promotion_id=ispr.id join common_distributors cd on cd.distributor_id=isprd.distributor_id where ispr.id="+PromoID);
							while(rs22.next()){
							%>
								<tr style="backg1round-color:#efedee">
									<td style="width:50%; padding:5px;"><%=rs22.getString("distributor_id")%> - <%=rs22.getString("name") %></td>
									
								</tr>	
									
								<%
							}
								%>		
							</tbody>

						</table>
													
						</li>
						
						<%
						}
						
						%>
						<%
						ResultSet rs234 = s.executeQuery("SELECT dbp.id,dbp.label FROM inventory_sales_promotions_request_pjp isprp join inventory_sales_promotions_request ispr on isprp.product_promotion_id=ispr.id join distributor_beat_plan dbp  on dbp.id=isprp.pjp_id where ispr.id="+PromoID);
						if(rs234.first()){
						
						%>
						<li data-role="list-divider">PJP</li>
						<li>
						<table border="0" style="width: 100%; margin-top: 0px; padding: 0px;" class="workflow_form_font">
							
							
							
							<tbody>
							<%
							ResultSet rs233 = s.executeQuery("SELECT dbp.id,dbp.label FROM inventory_sales_promotions_request_pjp isprp join inventory_sales_promotions_request ispr on isprp.product_promotion_id=ispr.id join distributor_beat_plan dbp  on dbp.id=isprp.pjp_id where ispr.id="+PromoID);
							while(rs233.next()){
							%>
								<tr style="backg1round-color:#efedee">
									<td style="width:50%; padding:5px;"><%=rs233.getString("id")%> - <%=rs233.getString("label") %></td>
									
								</tr>	
									
								<%
							}
								%>		
							</tbody>

						</table>
													
						</li>
						<%
						}
						%>
						


						<li data-role="list-divider">Comments</li>
						<li>
						<table>
							<thead>
										
							</thead>
							<tbody id="PerCaseSamplingContainer">
							</tbody>
						</table>
						<%
						ResultSet rs4 = s.executeQuery("SELECT message,sent_by,(select DISPLAY_NAME from users u where u.id=wrcm.sent_by) sent_by_name,sent_on,DATE_FORMAT(sent_on,'%b %d %Y') sent_date,DATE_FORMAT(sent_on,'%h:%i %p') sent_time FROM workflow_requests_chat wrc join workflow_requests_chat_messages wrcm on wrc.conversation_id=wrcm.conversation_id and  wrc.request_id="+RequestID);
						while(rs4.next()){
						%>
						<p>
						<b><%=rs4.getString("sent_by_name") %></b>:&nbsp;<%=rs4.getString("message") %>&nbsp;[<%=rs4.getString("sent_date") %>&nbsp;/&nbsp;<%=rs4.getString("sent_time") %>]
						
						
						</p>
						<%
						}
						%>
						
						</li>
						<li>
							
						</li>

					</ul> 
					<div data-role="popup" id="popup_workflow_forward" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:600px;min-width:400px;" class="ui-corner-all">
					    <div data-role="header" data-theme="a" class="ui-corner-top">
					        <h1><%=bean.CURRENT_STEP.ACTION_BUTTON_LABEL %></h1>
					    </div>
					    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
							<%
							if (bean.isLastStep == false){
							%>
					        <p>This document will be forwarded <%if (bean.isNewRequest == false){ %>to</p>
					        <h3 class="ui-title"><%=bean.NEXT_STEP.USER_DISPLAY_NAME %>, <%=bean.NEXT_STEP.USER_DESIGNATION %><font style="font-weight: 400;"> <%}%>for <%=bean.NEXT_STEP.ACTION_LABEL %></font></h3>
					        <label for="textarea">Remarks</label>
					        <%}else{ %>
					        <p>This request will be closed and stakeholders will be notified:</p>
					        <label for="textarea">Message for stakeholders:</label>
					        <%} %>
							<textarea cols="40" rows="10" name="WorkflowStepRemarksPopup" id="WorkflowStepRemarksPopup"></textarea>
							
					        <a data-role="button" data-inline="true" data-theme="a" id="WorkflowFormSubmitButton" href="#" class="WorkflowSubmitConfirm">Proceed</a>
					        <a href="#WorkflowManager" data-role="button" data-inline="true" data-theme="c">Cancel</a>
	
					    </div>
					</div>
					<input type="hidden" name="DetailRows" id="DetailRows" value="0"> 
					<input type="hidden" name="RequestID" id="RequestID" value="<%=bean.REQUEST_ID%>">
					<input type="hidden" name="SamplingID" id="SamplingID" value="0">
					<input type="hidden" name="WorkflowStepRemarks" id="WorkflowStepRemarks" value="">
					<input type="hidden" name="StepID" id="StepID" value="<%=bean.CURRENT_STEP.STEP_ID%>">
					<input type="hidden" name="NextStepID" id="NextStepID" value="<%=bean.NEXT_STEP.STEP_ID%>">
					<input type="hidden" name="NextActionID" id="NextActionID" value="<%=bean.NEXT_STEP.ACTION_ID%>">
					<input type="hidden" name="NextUserID" id="NextUserID" value="<%=bean.NEXT_STEP.USER_ID%>">
					<input type="hidden" name="isLastStep" id="isLastStep" value="<%=bean.isLastStep%>">
					<input type="hidden" name="uvid" id="uvid" value="<%=Utilities.getUniqueVoucherID(SessionUserID)%>">
					</form>

				</div><!-- /ui-block -->
			</fieldset>
		<div data-role="popup" id="AJAXErrorPopup" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:600px;min-width:400px;" class="ui-corner-all">
		    <div data-role="header" data-theme="a" class="ui-corner-top">
		        <h1>Error</h1>
		    </div>
		    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
		
		        <p>Sorry, an unexpected error has occurred.<br>Please contact system administrator if you need assistance.</p>
				<div data-role="collapsible" data-mini="true">
				    <h4>View Detail</h4>
				    <p id="AJAXErrorMessage"></p>
				</div>
				<a href="#WorkflowManager" data-role="button" data-inline="false" data-mini="true">Close</a>
		    </div>
		</div>   					      
						
    </div><!-- /content -->
    <div data-role="footer" data-position="fixed" data-theme="b">
       	<div >
			
			<%
			if (!bean.isDeclined){
			%>
			
	  		    
	  		    <a href="#" style="float: right; margin-right: 10px;" data-role="button" data-icon="arrow-u" data-corners="false" data-theme="b" 	<%if (bean.isNewRequest == true){%>class="ui-disabled"<%}else{ %>class="WorkflowAttachButton"<%} %>>Attach Documents</a>
	  		    <a href="#" style="float: right; margin-right: 10px;" data-role="button" data-icon="arrow-u" data-corners="false" data-theme="a" 	<%if (bean.isNewRequest == true){%>class="ui-disabled"<%}else{ %>class="WorkflowConversationButton"<%} %>>Conversation</a>

				<a href="#" id="WorkflowChatPreviewContainer" style="float: right; margin-right: 0px; display:none" data-role="button" data-corners="false" data-theme="a" class="ui-disabled"><span id="WorkflowChatPreview"></span></a>

				<%
				if (bean.CURRENT_STEP.USER_ID == SessionUserID || bean.isNewRequest == true){
					if (bean.isCompleted == false){
				%>
				<button class="WorkflowSubmitButton" data-icon="check" data-theme="a" style="float: left; padding-left: 10px;" data-inline="true"><%=bean.CURRENT_STEP.ACTION_BUTTON_LABEL %></button>
				<%
					}
				}
				%>
				<%
				if (bean.CURRENT_STEP.USER_ID == SessionUserID && bean.isNewRequest == false){
					if (bean.isCompleted == false){
				%>
				<button class="WorkflowDeclineButton" data-icon="check" data-theme="b" data-inline="true">Decline</button>
				<%
					}
				}
				%>
			<%
			}
			%>
		</div>
        
    </div><!-- /footer -->

      

				<div data-role="popup" id="popup_workflow_decline" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:600px;min-width:400px;" class="ui-corner-all">
				    <div data-role="header" data-theme="b" class="ui-corner-top">
				        <h1>Decline</h1>
				    </div>
				    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
				        <p>Please confirm if you want to decline this request.</p>
				        <h3 class="ui-title"><font size="2" style="font-weight: 400"><u></u></font></h3>
				        <label for="textarea">Reason:</label>
						<textarea cols="40" rows="8" name="textarea" id="textarea"></textarea>
						
				        <a data-role="button" class="WorkflowDeclineConfirmButton" data-role="button" data-inline="true" data-theme="b">Decline</a>
				        <a href="#<%=PageID %>" data-role="button" data-inline="true" data-theme="c">Return</a>
				    </div>
				</div>         

				<div data-role="popup" id="popup_workflow_attach" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:600px;min-width:500px;max-height:500px;" class="ui-corner-all">
				    <div data-role="header" data-theme="b" class="ui-corner-top">
				        <h1>Attach Files</h1>
				    </div>				
				    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">

						<div id="manual-fine-uploader"></div>
						<br>
						<label for="textarea">Description (if any):</label>
						<textarea cols="40" rows="10" name="WorkflowAttachmentDescription" id="WorkflowAttachmentDescription"></textarea>						
				        <button id="triggerUpload" data-role="button" data-inline="true" data-theme="b">Attach</button>
				        <a href="#<%= PageID %>" data-role="button" data-inline="true" data-theme="c">Close</a>
				    </div>
				</div>         

				<div data-role="popup" id="popup_workflow_conversation" data-overlay-theme="a" data-theme="c" data-dismissible="false" style="max-width:700px;min-width:700px;max-height:700px;" class="ui-corner-all">
				<a href="#<%=PageID %>" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a>
				    <div data-role="content" data-theme="c" class="ui-corner-bottom ui-content" id="WorkflowChatContent">
					
				    </div>
				</div>         

				<div data-role="popup" id="popup_employee_info" class="ui-content" data-position-to="origin" style="max-width:350px">
					<ul data-role="listview" data-icon="bars">
						<li data-role="list-divider" data-theme="a">Musawwar Rasheed</li>
						<li>
							<a href="#workflow_sampling_new" >
								<img src="images/musawwar.jpg" />
								<p style="font-size: 10pt;">Employee ID: 3476<br>
								Area Sales Manager<br>
								Sales Department<br>
								Since March 13, 2009 (5.3 Years)
								</p>
							</a>
						</li>
					</ul>
				</div>         


</div>
<%
bean.close();
%>
</body>
</html>