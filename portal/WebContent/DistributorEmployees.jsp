<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@include file="include/ValidateSession.jsp" %>


<link rel="stylesheet"  href="lib/jqm130/jquery.mobile-1.3.0.min.css">
<script src="lib/jquery-1.9.1.min.js"></script>
<script src="lib/jqm130/jquery.mobile-1.3.0.min.js"></script>
<script src="js/DistributorEmployess.js"></script>
<%

int FeatureID = 54;
long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect(Utilities.getAccessDeniedPageURL(request));
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();


Statement s = c.createStatement();
Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, 54);

if( UserDistributor == null ){
	response.sendRedirect("AccessDenied.jsp");
}


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

%>
<div data-role="page" id="DistributorEmployeesMain" data-url="DistributorEmployeesMain" data-theme="d">
 <jsp:include page="Header3.jsp" >
    	<jsp:param value="Distributor Employee" name="title"/>

    		<jsp:param value="<%=DistributorName%>" name="HeaderValue"/>

    </jsp:include>
    
    <div data-role="content" data-theme="d">
<%
long DistEmpID = Utilities.parseLong(request.getParameter("DistEmpID"));
long EmployeeID=0;
String Name="";
String Department="";
String Designation="";
long CNIC=0;
int IsEdit=0;
%>

<%
if(DistEmpID !=0) //edit case
{
	IsEdit=1;
	ResultSet rs2 = s.executeQuery("select * from distributor_employees where id="+DistEmpID);
	if(rs2.next())
	{
		EmployeeID = Utilities.parseLong(rs2.getString("id"));
		Name = Utilities.filterString(rs2.getString("name"), 1, 100);
		Department = Utilities.filterString(rs2.getString("department"), 1, 100);
		Designation = Utilities.filterString(rs2.getString("designation"), 1, 100);
		CNIC = Utilities.parseLong(rs2.getString("nic"));
	}
}
%>
	<form id="DistributorEmpForm" name="DistributorEmpForm" action="" method="POST">
	<input type="hidden" name="isEditCase" id="isEditCase" value="<%=IsEdit%>"/>
	<input type="hidden" name="DistEmpIDForWhole" id="DistEmpIDForWhole" value="<%=DistEmpID%>"/>
	<input type="hidden" name="DistributorEmpDistID" id="DistributorEmpDistID" value="<%=DistributorID %>"/>
		<ul data-role="listview" data-inset="true">
	        <li data-role="fieldcontain" class="ui-disabled">
	            <label for="DistributorEmpID">Employee ID:</label>
	            <input type="text" name="DistributorEmpID" id="DistributorEmpID" value="<%if(EmployeeID !=0){ %><%=EmployeeID%><%}%>" readonly>
	        </li>
	        <li data-role="fieldcontain">
	            <label for="DistributorEmpName">Name:</label>
	            <input type="text" name="DistributorEmpName" id="DistributorEmpName" value="<%=Name%>">
	        </li>
	        <li data-role="fieldcontain">
	            <label for="DistributorDeptName">Department:</label>
	            <input type="text" name="DistributorDeptName" id="DistributorDeptName" value="<%=Department%>">
	        </li>
	        
	        <li data-role="fieldcontain">
	            <label for="DistributorEmpDesignation">Designation:</label>
	            <input type="text" name="DistributorEmpDesignation" id="DistributorEmpDesignation" value="<%=Designation%>">
	        </li>
	        <li data-role="fieldcontain">
	            <label for="DistributorEmpNIC">National ID Card#:</label>
	            <input type="text" name="DistributorEmpNIC" id="DistributorEmpNIC" maxlength="13" value="<%if(CNIC==0){%><%}else{out.println(CNIC);}%>">
	        </li>	        
	        <!-- <li data-role="list-divider" data-theme="c">For the period</li>  -->
	        

        </ul>
	</form>

    </div><!-- /content -->
    
    <div data-role="footer" data-position="fixed" data-theme="b" id="WorkflowRouteFooter">
	<div>
		<table border="0" style="width: 100%;">
		<td  align="left">
			<button data-icon="check" data-theme="a" data-inline="true" id="DistributorEmpSave" onClick="DistributorEmployeeSubmit()">Save</button>
		</td>
		
		<td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DistributorSearch" >Search</a>
		</td>
		</table>
				
	</div>    	
    </div>



<div data-role="popup" id="popupDialog" data-overlay-theme="a" data-theme="c" data-dismissible="true" style="min-width:700px; overflow-y: auto; min-height:600px; max-height: 600px" aclass="ui-corner-all">
        <div data-role="header" data-theme="a" class="ui-corner-top">
            <h1>Search</h1>
        </div>
        <div data-role="content" data-theme="d" class="ui-corner-bottom ui-content" >
		<ul data-role="listview" data-inset="true" data-filter="true" data-filter-placeholder="Search Employee"> 
			<li data-role="list-divider">Distributor Employees</li>
			
	 		<%
				ResultSet rs1 = s.executeQuery("SELECT * FROM distributor_employees where distributor_id="+DistributorID);
			while(rs1.next())
			{
	        	
	 		%>
	 		<li>
	 			<a data-ajax="false" href="#" onClick="LoadPerticularDistributorEmp(<%=rs1.getString("id")%>)">
						<span style="font-size: 10pt; font-weight: 400;"><%=rs1.getString("name") %></span>
						
						
				</a>
	 		</li>
	 		<%
	 		} 
	 		%> 
	 		
	 		
	 		
 		</ul>      
			
 		<form data-ajax="false" id="DistEmpEditForm" method="POST" action="DistributorEmployees.jsp">
        	<input type="hidden" name="DistEmpID" id="DistEmpID">
        </form>
            
        </div>
    </div>

</div>



</body>
</html>
<%
s.close();
ds.dropConnection();
%>