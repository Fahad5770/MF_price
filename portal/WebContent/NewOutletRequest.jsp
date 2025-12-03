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
<%@page import="com.pbc.common.Distributor"%>

<%
long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 305;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}

boolean isEditCase = false;
long EditID = Utilities.parseLong(request.getParameter("EditID"));
if(EditID > 0){
	isEditCase = true;
}

int DeActivate = Utilities.parseInt(request.getParameter("DeActivate"));

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s1=c.createStatement();
Statement s2=c.createStatement();
Statement s3=c.createStatement();
Statement s4=c.createStatement();

String CustomerID = "";
String CustomerName = "";
String CreditLimit = "";
String ValidFrom = "";
String ValidTo = "";



if(isEditCase){
	ResultSet rs = s.executeQuery("select *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name from gl_customer_credit_limit where id="+EditID);
	if(rs.first()){
		CustomerID = rs.getString("customer_id");
		CustomerName = rs.getString("customer_name");
		CreditLimit = rs.getString("credit_limit");
		ValidFrom = rs.getString("valid_from");
		ValidTo = rs.getString("valid_to");
	}
}

if(DeActivate > 0){
	s.executeUpdate("update gl_customer_credit_limit set is_active=0, deactivated_on=now(), deactivated_by="+SessionUserID+" where id="+DeActivate);
	response.sendRedirect("GLCreditLimit.jsp");
}



Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
int ScopeDistID = UserDistributor[0].DISTRIBUTOR_ID;


String DistributorIDs = UserAccess.getDistributorQueryString(UserDistributor);
System.out.println("test "+DistributorIDs);

%>
<html>


<head>
		<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
		<script src="js/lookups.js"></script>

		<script src="js/GLCreditLimit.js"></script>
		
</head>

<body>

<div data-role="page" id="GLCreditLimitPage" data-url="GLCreditLimitPage" data-theme="d">

    <jsp:include page="Header2.jsp" >
    	<jsp:param value="New Outlet Request" name="title"/>
    </jsp:include>
    
    <script type="text/javascript" src="js/NewOutletRequest.js?1=123"></script>
    <div data-role="content" data-theme="d">
	
<% 
Date d=new Date();
String StartDate = Utilities.getDisplayDateFormat(d); 
%>	
<form id="AddOutletForm">
<input type="hidden" name="QueryFlag" value="1">
<%-- <ul data-role="listview"  data-divider-theme="b" style="margin-top:5px;">
<li data-role="list-divider">Requester Detail</li> </ul>
<table style="width:100%; margin-top:15px;">
<tr>
	
	<td style="width:25%"><input type = "text" placeholder = "Date" id="Date" name="Date" readonly="readonly" value="<%=StartDate%>" ></td>
<td colspan="2">
		<table style="width:100%">
			<tr>
				<td style="width:25%">
					<input type = "text" placeholder = "Agency ID"   id="AgencyID" name="AgencyID"  onchange="getAgencyName()" onkeypress="if(event.keyCode==13) foucsnext()">
				</td>
				<td style="width:75%">
					<input type = "text" placeholder = "Agency Name"   id="AgencyName" name="AgencyName" readonly="readonly" onkeypress="if(event.keyCode==13) foucsnext()">
				</td>
			</tr>
		</table>
	</td>
</tr> 
</table> --%>

<ul data-role="listview" data-divider-theme="b" style="margin-top:5px;">
<li data-role="list-divider">Outlet Detail</li> </ul>
<table style="width:100%; margin-top:15px;">
<tr> 

	<td><input type = "text" placeholder = "Outlet Name"  id="OutletName"  name="OutletName"  onkeypress="if(event.keyCode==13) foucsnext()" ></td>
	<td><input type = "text" placeholder = "Outlet Address"  id="OutletAddress"  name="OutletAddress"  onkeypress="if(event.keyCode==13) foucsnext()" ></td>
	<td ><input type = "text" placeholder = "CNIC No."   id="CNICNo" name="CNICNo" onchange="CheckCnicDuplication()" onkeypress="if(event.keyCode==13) foucsnext()" ></td>
	<td style="width:25%"><input type = "text" placeholder = "Owner Name"   id="OwnerName"  name="OwnerName"  onkeypress="if(event.keyCode==13) foucsnext()" ></td>
	</tr>

<tr>
	
	<td><input type = "text" placeholder = "Contact No."  id="OutletContactNo"  name="OutletContactNo"  onkeypress="if(event.keyCode==13) foucsnext()" ></td>
	<!--<td><input type = "text" placeholder = "Beat Plan (Days)"   id="BeatPlan" name="BeatPlanDays"></td>-->
	<td  style="text-align:center">
		
			<select  name="BeatPlan"  data-mini="true" id="BeatPlan"  onkeypress="if(event.keyCode==13) foucsnext()">
				<option value="-1">Select Beat Plan</option>
				<% 			
				int BeatPlanID=0;
				String BeatPlanLabel="";
				
				
				ResultSet rsBeatPlan = s3.executeQuery("SELECT * FROM distributor_beat_plan where distributor_id="+ScopeDistID);
				while(rsBeatPlan.next()){
					BeatPlanID = rsBeatPlan.getInt("id");
					BeatPlanLabel = rsBeatPlan.getString("label");
					
					  
				%>	
				 <option value="<%=BeatPlanID%>"><%=BeatPlanID%> - <%=BeatPlanLabel%></option>
				 
				<% 	
				}			  
				%>	
			  </select>
	</td>
	 
	
	
	<td  style="text-align:center">
		
			<select  name="Subchannels"  data-mini="true" id="Subchannels"  onkeypress="if(event.keyCode==13) foucsnext()" >
				<option value="-1">Select Sub channel</option>
	<% 			
	int SubID=0;
	String Label="";
	
	ResultSet rsSub = s1.executeQuery("SELECT * FROM mrd_census_sub_channel  ");
	while(rsSub.next()){
		SubID = rsSub.getInt("id");
		Label = rsSub.getString("label");
		
		  
	%>	
	
				        <option value="<%=SubID%>"><%=Label%></option>
				      
				 
				   <% 	
	}			  
	%>	
			  </select>
		
	</td>
<%-- 	<td style="text-align:center">
		
			 <select  name="vpoclassification" data-mini="true" id="vpoclassification"  onkeypress="if(event.keyCode==13) foucsnext()" >
			 	<option value="-1">Select VPO Classification</option>
			<% 			
	int ClassID=0;
	String ClassLabel="";
	
	ResultSet rsClass = s2.executeQuery("SELECT * FROM common_outlets_vpo_classifications  ");
	while(rsClass.next()){
		ClassID = rsClass.getInt("id");
		ClassLabel = rsClass.getString("label");
		%>
				  
				 
				        <option value="<%=ClassID%>"><%=ClassLabel%></option>
				  
		<% 			   
				   }			  
	%>
			 </select>	
		
	</td> --%>
	<td style="text-align:center">
		
			 <select  name="commoncategory" data-mini="true" id="commoncategory">
			 	<option value="-1">Select Category</option>
			<% 			
	int CCatID=0;
	String CCatLabel="";
	
	ResultSet rsCCat = s2.executeQuery("SELECT * FROM common_outlets_categories  ");
	while(rsCCat.next()){
		CCatID = rsCCat.getInt("id");
		CCatLabel = rsCCat.getString("label");
		%>
				  
				 
				        <option value="<%=CCatID%>"><%=CCatLabel%></option>
				  
		<% 			   
				   }			  
	%>
			 </select>	
		
	</td>
	</tr>
<tr>

	
	<td colspan="6" id="PJPID"> 
	 <fieldset data-role="controlgroup" data-type="horizontal" >
			
				<% 			
					int BeatPlanDayID=0;
					String BeatPlanDayLabel="";
				
					ResultSet rsBeatPlanDay= s4.executeQuery("SELECT * FROM common_days_of_week");
					while(rsBeatPlanDay.next()){
					BeatPlanDayID = rsBeatPlanDay.getInt("id");
					BeatPlanDayLabel = rsBeatPlanDay.getString("short_name");
				%>
						
						<input type="checkbox" name="BeatPlanDays" id="<%=BeatPlanDayID%>" value="<%=BeatPlanDayID%>"   >
        				<label for="<%=BeatPlanDayID%>"><%=BeatPlanDayLabel%></label>
					 	
					 	 
		  			
		  		<% 			   
		 		}			  
				%>
				
	</fieldset>
     </td>
</tr>
</table>
<input type="hidden"  id="DistributionID" name="DistributionID" value="<%=ScopeDistID%>">

<%-- <ul data-role="listview" data-divider-theme="b" style="margin-top:5px;">
<li data-role="list-divider">Area Detail</li> </ul>
<table style="width:100%; margin-top:15px;">
<tr>

	<td style="width:25%"><input type = "text" placeholder = "RM  "   id="RMID" name= "RMID"  onkeypress="if(event.keyCode==13) foucsnext()" ></td>
	
			<td style="width:25%"><input type = "text" readonly="readonly" placeholder = "Distribution ID"   id="DistributionID" name="DistributionID" value="<%=ScopeDistID%>"></td>
	<%
	ResultSet rsDistribuorName = s.executeQuery("SELECT * from common_distributors where distributor_id="+ScopeDistID);
			//System.out.println("SELECT * from common_distributors where distributor_id="+DistributorID);
			if(rsDistribuorName.first()){
				
				String distributorName=rsDistribuorName.getString("name");
			
	 %>
	<td style="width:25%"><input type = "text" placeholder = "Distribution Name" readonly="readonly"  id="DistributionName" name="DistributionName" value="<%=distributorName%>"></td>
	<% 
	}
	%>
	<td></td>
</tr>
</table> --%>
</form>
	
	
	
	
	
	
    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b">
		<div>
			<table style="width: 100%;">
				<tr>
					<td>
						<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="SubmitButton" href="#" onClick="submitForm()" aclass="ui-disabled" >Save</a>
						<button data-icon="check" data-theme="b" data-inline="true" id="ResetButton" onClick="javascript:window.location='NewOutletRequest.jsp'" >Reset</button>
						
					</td>
	                <!-- <td align="right">
	                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="ViewAllButton" >View All</a>
					</td> -->
				</tr>
			</table>
		</div>
    </div>
    
    <div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="width: 500px;" class="ui-corner-all">
	    <div data-role="header" data-theme="a" class="ui-corner-top">
	        <h1>View All</h1>
	    </div>
	    <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content">
	        <ul data-role="listview" data-filter="true" data-filter-placeholder="Search ..." data-inset="true">
			    
			    
			    <%
				ResultSet rs3 = s.executeQuery("SELECT *, (SELECT name FROM common_distributors where distributor_id=customer_id) customer_name FROM gl_customer_credit_limit where is_active=1 order by activated_on ");
				while( rs3.next() ){
					%>
					 <li><a href="#" onclick="window.location='GLCreditLimit.jsp?EditID=<%=rs3.getString("id")%>'"><%=rs3.getString("customer_name")%><span class="ui-li-count"><%= Utilities.getDisplayCurrencyFormatTwoDecimalFixed(rs3.getDouble("credit_limit"))%></span></a></li> 
					<%
				}
				%>
			    
			    
			</ul>
	    </div>
	</div>
    
    <jsp:include page="LookupDistributorSearchPopup.jsp" > 
    	<jsp:param value="DistributorSearchCallBackCashReceipt" name="CallBack" /> 
    	<jsp:param value="<%=FeatureID%>" name="DistributorSearchFeatureID" />
    </jsp:include><!-- Include Outlet Search -->
    

</div>

</body>
</html>
<%
s.close();
ds.dropConnection();
%>