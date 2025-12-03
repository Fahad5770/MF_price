<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>
<%@page import="com.pbc.common.Warehouse"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.common.Distributor"%>


<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/DispatchAdjustment.js?1411=5555244"></script>





<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 60;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	
	
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();


Statement s = ds.createStatement();
Statement s1 = ds.createStatement();
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 60);
%>
<% 
String DistributorName = "";
long DistributorID = 0;


if( UserDistributor != null ){
	if(UserDistributor.length>1) //if it has more than 1 distributor
	{
		response.sendRedirect("AccessDenied.jsp");
	}
	else
	{
		DistributorName = UserDistributor[0].DISTRIBUTOR_NAME;
		DistributorID = UserDistributor[0].DISTRIBUTOR_ID;
	}
}

long DispatchID =Utilities.parseLong(request.getParameter("DispatchID"));
if(DispatchID==0)
{
	// response.sendRedirect("DispatchAdjustmentMain.jsp");
}

%>


<div data-role="page" id="DispatchReturnsMain" data-url="DispatchReturnsMain" data-theme="d">

    
  
    
    <div data-role="header" data-theme="c" data-position="fixed">
	    <div>
		    <div style="float:left; width:10%">
		    	<a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
		    </div>
		    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;"><h1 style="font-size: 14pt;float:left; margin-left:35%; text-align:center;">Dispatch Adjustment</h1>
		    
			    <div data-role="navbar" style="width:20%; float:right; margin-right:3px; margin-top:10px;">
			    	<ul>
			        	<li><a href="#" onclick="changeTab(1)" class="ui-btn-active" data-ajax="false">Invoices</a></li>
			        	<li><a href="#" onclick="changeTab(2)" data-ajax="false">Extra Load</a></li>
			    	</ul>
				</div>
			</div>
		</div>
	</div>
    
<table border="0" style="width:100%;">
<input type="hidden" name="DispatchID" id="DispatchID" value="<%=DispatchID %>"/>
	<tr>
		<td style="width:25%" valign="top">
		
		
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; margin-left:10px;margin-top:10px;">
						    <li data-role="list-divider" data-theme="a">Invoices</li>
						    <%
						    ResultSet rs = s.executeQuery("select * from inventory_sales_dispatch_invoices where id="+DispatchID);
						   // System.out.println("select * from inventory_sales_dispatch_invoices where id="+DispatchID);
						    while(rs.next())
						    {
						    	ResultSet rs1 = s1.executeQuery("SELECT isi.id,isi.created_on,isi.created_by,isi.uvid,isi.outlet_id,(select outlet_name from outletmaster om where om.outlet_id=isi.outlet_id) outlet_name,(select bsi_name from outletmaster om where om.outlet_id=isi.outlet_id) bsi_name FROM inventory_sales_invoices isi where isi.id="+Utilities.parseLong(rs.getString("sales_id")));
						    	//System.out.println("SELECT isi.id,isi.created_on,isi.created_by,isi.uvid,isi.outlet_id,(select outlet_name from outletmaster om where om.outlet_id=isi.outlet_id) outlet_name FROM inventory_sales_invoices isi where isi.id="+Utilities.parseLong(rs.getString("id")));
						    	while(rs1.next())
						    	{
						    		String OuuuutletName = Utilities.parseLong(rs1.getString("outlet_id"))+" - "+Utilities.filterString(rs1.getString("outlet_name"),1,100)+" "+Utilities.filterString(rs1.getString("bsi_name"),1,100);
						    %>
						    	<li ><a data-ajax="false" href="javaScript:onClick=DispatchAdjustmentAddProductLoadPage('<%=Utilities.parseLong(rs1.getString("outlet_id"))%>','<%=DispatchID%>','<%=Utilities.parseLong(rs.getString("sales_id"))%>','<%=OuuuutletName %>')" style="font-size:9pt; font-weight:normal;"><span><%=Utilities.parseLong(rs1.getString("outlet_id"))+" - "+Utilities.filterString(rs1.getString("outlet_name"),1,100)+" "+Utilities.filterString(rs1.getString("bsi_name"),1,100)%></span><span class="ui-li-count"><%=rs.getLong("sales_id") %></span></a></li>	
						    <%
						    	}
						    }
						    %>
						</ul>
		</td>
	
	
		<td style="width:40%" valign="top">
		<ul data-role="listview" data-inset="true" style="margin-top:10px;">						    
				    	<li data-role="list-divider" data-theme="c" id="OutletNamePutHere">Return Adjustment</li>
				    	<li>
				    		<h3 style="font-size:9pt;" id="InvoiceHeadingID">Please select an invoice </h3>
				    		<form id="DispatchAdjustmentForm" data-ajax="false" action="" onSubmit="return DispatchAdjustmentAddProduct();">		
   								<input type="hidden" name="isDelete" id="isDelete" value="0"/>
   								<input type="hidden" name="isAddClicked" id="isAddClicked" value="0"/>
   								<input type="hidden" name="isEditCase" id="isEditCase" value="0"/>
   								<input type="hidden" name="DispatchIDForLastUse" id="DispatchIDForLastUse" value="<%=DispatchID%>"/>
   								<input type="hidden" name="SessionUserID" id="SessionUserID" value="<%=SessionUserID%>"/>
   								<div id="DispatchAdjustmentAddProductDIV">
   								<h4 style="font-weight:normal; font-size:10pt;"></h4>
   								</div>
							    
							</form>
				    	</li>	
						   
						</ul>
		</td>

	
		<td style="width:35%" valign="top">

		<div id="LoadDispatchAdjustmentSummaryDIV">
		</div>

		<div id="LoadDispatchAdjustmentInvoiceSummaryDIV">
		</div>
		
		<div id="LoadDispatchAdjustmentInvoiceBrandDIV">
		</div>
						
		<!-- <div id="LoadDispatchAdjustmentDifferenceDIV">
		</div>	 -->					

		</td>
</tr>	
</table>
    
    <div data-role="content" data-theme="d">



	
		
    	<form id="PassParams" action="DeskSaleExtraLoad.jsp" method="post">
    		<input type="hidden" name="DispatchID" value="<%=DispatchID%>" >
    	</form>	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<!-- <div id="SaveAdjustmentButtonDIV" class="ui-disabled" align="right"> -->
	<div id="SaveAdjustmentButtonDIV" align="right">
		<button data-icon="check" type="button" data-theme="a" data-inline="true" data-ajax="false" id="SaveAdjustmentButton" onClick="SaveDispatchAdjustmentInDB()" >Post Adjustment</button>
		
	</div>    	
    </div>
	

</div>

</body>
</html>



<%
s.close();
ds.dropConnection();
%>