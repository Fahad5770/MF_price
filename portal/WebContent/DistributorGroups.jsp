<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.util.UserAccess"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>









<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->
<script src="js/lookups.js"></script>
<script src="js/LiftingReport.js"></script>
<script src="js/DistributorGroups.js"></script>





<%
int FeatureID = 50;
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

long GroupID = Utilities.parseLong(request.getParameter("GroupID"));

%>
<script>


var GlobalGroupID='<%=GroupID%>';


</script>

<div data-role="page" id="AddDistributor" data-url="AddDistributor" data-theme="d">

    <div data-role="header" data-theme="c" data-position="fixed">
    <div>
		

    
    <div style="float:left; width:10%">
    	<a href="home.jsp" data-role="button" data-theme="d" data-inline="true" data-ajax="false" ><img src="images/logofull.svg" style="width: 50px"></a>
    </div>
    <div style="float:left; width:90%;b1ackground-color:Red; text-align:center;"><h1 style="font-size: 14pt;float:left; margin-left:45%; text-align:center;">Distributor Groups</h1>
    
	    
	</div>
    
	</div>
		


</div>
<%
Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
Statement s3 = ds.createStatement();
%>
    
    <div data-role="content" data-theme="d">

	<div class="ui-grid-a ui-corner-all ui-shadow" style="margin: 5px;">
    	<div class="ui-block-a" style="width:30%;">
    		<div class="ui-bar " style="min-height:60px;">
    			<br/>
    			<ul data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search groups">
    			<li data-role="list-divider" data-theme="a">Groups</li>
				  <li><a href="DistributorGroups.jsp" data-ajax="false" style="font-size: 13px;">All</a></li>
				   <%
				   //ResultSet rs = s.executeQuery("select id,label from common_distributor_groups");
				   ResultSet rs3 = s.executeQuery("select *  from common_distributor_groups"); 
				   
					
					while(rs3.next())
					{
						ResultSet rs = s3.executeQuery("select COUNT(id) total from common_distributor_groups_list where id="+rs3.getString("id"));
						if(rs.first())
						{
					%>
						<li onClick="AddGroups(<%=rs3.getString("id") %>)"><a href="#" style="font-size: 13px;"><%=rs3.getString("label") %> (<%=rs.getString("total") %>)</a></li>
					 <%
						}
					}
				   %>
				</ul>
				
				<ul data-role="listview" data-inset="true">
    			<li data-role="list-divider" data-theme="a">Add Group</li>
				  <li>
				  	<form id="AddDistributorFormNew" data-ajax="false"> 
				  	<input type="text" name="AddDistributorNameTxtBx" id="AddDistributorNameTxtBx" placeholder="Group Name"/> 
				  	<input type="button" value="Add Group" onClick="AddDistributorNew()"/>
				  	</form>
				  </li>
				  
				   
				</ul>
				
				
    		</div>
    	</div>
    	<div class="ui-block-b" style="width:70%;">
    		<div class="ui-bar" style="min-height:60px; ">
    			<br/>
    			<div id="FilteredGroupedDistributors">
    			Loading...
				</div>
    		</div>
    	</div>
	</div><!-- /grid-a -->

<div id="AddDistributorIdsDiv">
<form id="AddDistributorForm" data-ajax="false" >
<input type="hidden" name="isDeleteCase" id="isDeleteCase" value="0"/>
<input type="hidden" name="SelectedDistributorCounter" id="SelectedDistributorCounter" value="0"/>
<input type="hidden" name="isFilteredBtnClicked" id="isFilteredBtnClicked" value="0"/>
<input type="hidden" name="TempFilteredGrpID" id="TempFilteredGrpID" value="0"/>

</form>
</div>
	

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<button data-icon="check" data-theme="a" data-inline="true" id="LiftingReportGenerateButton" onClick="">Generate</button>
		
	</div>    	
    </div>
	
</div>
<%
s.close();
s2.close();
s3.close();
ds.dropConnection();
%>
</body>
</html>



