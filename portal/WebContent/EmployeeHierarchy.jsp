<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>

<jsp:include page="include/StandaloneSrc.jsp" /> <!-- JQM Base -->


<script type='text/javascript' src='https://www.google.com/jsapi'></script>
<script src="js/lookups.js"></script>
<script src="js/EmployeeHierarchy.js?123=123"></script>


<script type="text/javascript" src="lib/touchswipe/jquery.touchSwipe.min.js"></script>

<script>

</script>

<style>
li{
	font-size:10pt;
	font-weight: 400;
}
li a{
	font-size:10pt;
	font-weight: 400;
}
span {
	font-size:10pt;
	font-weight: 400;
	text-align: left; !important
}
</style>
<%
Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();

long SessionUserID = Utilities.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 74;
if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}
%>
<div data-role="page" id="EmployeeHierarchyMain" data-url="EmployeeHierarchyMain" data-theme="d" >
    
    <jsp:include page="Header2.jsp" >
    	<jsp:param value="Employee Hierarchy " name="title"/>
    </jsp:include>
    
    
    <div data-role="content" data-theme="d" >
    




    <table style="width: 100%;" >
    <tr>
	 <td style="width:20%" valign="top" style=" position: relative; z-index: 1;">
	 
	 
	 
		 <ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-left:-10px; margin-top:-10px; position: relative; z-index: 1;">
		 		<li data-role="list-divider" data-theme="a"><span>Hierarchy</span></li>            
	            <li><a href="#popupDialog" data-rel="popup" data-transition="pop">Company Level</a></li>
	            
	            <li data-role="list-divider" data-theme="a"><span>Selected Node</span></li>
	            <li data-icon="false">
	            	<a href="#">
	            		<span id="ParentSelectedNode">Not selected</span>
	            	</a>
	            	<input type="hidden" name="DispSapCode" id="DispSapCode" value=""/>
	            	<input type="hidden" name="DispDisplayname" id="DispDisplayname" value=""/>
	            	<input type="hidden" name="DispDesignation" id="DispDesignation" value=""/>
	            </li>
	           </ul>
	           <fieldset data-role="controlgroup" style="margin-left: -10px;display:none; position: relative; z-index: 1;" id="ActionList" >
		            <input type="button" name="Apply" id="Apply" data-theme="a" value="&nbsp;Actions" data-mini="true">
			        <input type="radio" name="MoveUnderRadio" id="MoveUnderRadio-1" value="1" onClick="SelectAction();AddUnder();">
			        <label for="MoveUnderRadio-1" id="MoveUnderRadio-1_label">Move Under</label>
			        <input type="radio" name="MoveUnderRadio" id="MoveUnderRadio-4" value="4" onClick="SelectAction();AddUnder();">
			        <label for="MoveUnderRadio-4" id="MoveUnderRadio-4_label">Add Under</label>
			        <input type="radio" name="MoveUnderRadio" id="MoveUnderRadio-2" value="2" onClick="SelectAction();AddUnder();">
			        <label for="MoveUnderRadio-2" id="MoveUnderRadio-2_label">Remove</label>
			        <input type="radio" name="MoveUnderRadio" id="MoveUnderRadio-3" value="3" onClick="SelectAction();AddUnder();">
			        <label for="MoveUnderRadio-3" id="MoveUnderRadio-3_label">Move Child Nodes</label>	
			       
			        <%if(Utilities.isAuthorized(77, Utilities.parseLong(SessionUserID+""))==true || Utilities.isAuthorized(78, Utilities.parseLong(SessionUserID+""))==true){ %>
			        <input type="radio" name="MoveUnderRadio" id="MoveUnderRadio-5" value="5" onClick="SelectAction();AddUnder();">
			        <label for="MoveUnderRadio-5" id="MoveUnderRadio-5_label">Change Hierarchy Level</label>
			        <%} %>						        
				
				</fieldset>
				<br/>
	           <ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-left:-10px; margin-top:-10px; display:none; position: relative; z-index: 1;" id="ActionMoveUnderID">
	            <li data-role="list-divider" data-theme="a"  id="SelectNodeToMoveID">Select node to move</li>
	            
	            <li data-icon="false" id="MoveUnderLiIDText" >
	            	<a href="#">
	            		<span id="DesignationMoveUnder">Not Selected</span>
	            		<table style="width:100%;display:none; border="0" style="font-size:10pt;font-weight: 400;" id="AddUnderTableID">
	            	<tr>
	            		
	            		<td style="font-size:10pt;font-weight: 400;"><input type="text" name="AddUnderTxtBx" id="AddUnderTxtBx" data-mini="true" placeholder="Employee ID" onKeyup="EnableSaveBtn()" /></td>
	            	
	            	</tr>
	            	
	            	</table>	
	            	</a>
	            	       	
	            	<input type="hidden" name="DispSapCodeMoveUnder" id="DispSapCodeMoveUnder" value=""/>
	            	<input type="hidden" name="DispDisplaynameMoveUnder" id="DispDisplaynameMoveUnder" value=""/>
	            	<input type="hidden" name="DispDesignationMoveUnder" id="DispDesignationMoveUnder" value=""/>	            	
	            </li>
	             <fieldset data-role="controlgroup" style="margin-left: 0px; margin-top:10px; display:none; position: relative; z-index: 1;" id="ReportingLevelControlAddUnder">
		            <input type="button" name="SelectReportingLevelAddUnder" id="Apply" data-theme="a" value="&nbsp;Select Reporting Level" data-mini="true">
			        <%
			        String HierarchyWhere1 = "";
			        if(Utilities.isAuthorized(77, Utilities.parseLong(SessionUserID+""))==false && Utilities.isAuthorized(78, Utilities.parseLong(SessionUserID+""))==false && Utilities.isAuthorized(74, Utilities.parseLong(SessionUserID+""))==true){
		    			
		    			
		    			HierarchyWhere1 = " where authority_order > (SELECT chl.authority_order FROM users u,common_hierarchy_levels chl where u.current_reporting_level=chl.id and u.id="+SessionUserID+")";
		    		}
				    //System.out.println("select * from common_hierarchy_levels "+HierarchyWhere+" order by authority_order"); 
		    		
			        
			        ResultSet rs3 = s.executeQuery("select * from common_hierarchy_levels "+HierarchyWhere1);
			        while(rs3.next()){
			        
			        %>
			        <input type="radio" name="MoveReportingLevelRadioAddUnder" id="MoveReportingLevelRadioAddUnder_<%=rs3.getInt("id") %>" value="<%=rs3.getInt("id") %>" onClick="EnableSaveBtn()">
			        <label for="MoveReportingLevelRadioAddUnder_<%=rs3.getInt("id") %>"><%=rs3.getString("short_name") %></label>
			        <%
			        }
			        %>
			        
			       					        
				</fieldset>    
		 </ul>
		
		 		<fieldset data-role="controlgroup" style="margin-left: -10px;display:none; margin-top:-10px;position: relative; z-index: 1;" id="ReportingLevelControl">
		           
		            <input type="button" name="SelectReportingLevel" id="Apply" data-theme="a" value="&nbsp;Select Reporting Level 16546678" data-mini="true">
			        <%
			        
		    		String HierarchyWhere = "";
		    		if (SessionUserID != 2062 || SessionUserID != 2381){
		    			HierarchyWhere = " where authority_order > 3 ";
		    		}			        
			        
		    		
			        ResultSet rs = s.executeQuery("select * from common_hierarchy_levels "+HierarchyWhere+" order by authority_order");
			        while(rs.next()){
			        
			        %>
			        <input type="radio" name="MoveReportingLevelRadio" id="MoveReportingLevelRadio_<%=rs.getInt("id") %>" value="<%=rs.getInt("id") %>" onClick="SelectReportingLevel()">
			        <label for="MoveReportingLevelRadio_<%=rs.getInt("id") %>"><%=rs.getString("short_name") %></label>
			        <%
			        }
			        %>
			        <input type="hidden" name="reportlevelhiddenid" id="reportlevelhiddenid" value="0"/>
			       					        
				</fieldset>
		 
		</td>
		<td style="width:80%; " valign="top">
		<input type="hidden" name="ShowAllNodes" id="ShowAllNodes" value="0"/>
		 <div id='chart_div' style="position:relative; z-index: 0;">
			
			 </div>
		</td>
		</tr>
		</table>
		<input type="hidden" id="SessionUserID" value="<%=SessionUserID%>">
		
</div><!-- /content -->

    <div data-role="footer" data-position="fixed" data-theme="b" data-tap-toggle="false">
    
	<div>
		<table style="width: 100%;">
			<tr>
				<td>
					<a data-icon="check" data-theme="a" data-role="button" data-inline="true" id="ButtonApplyID" href="#" class="ui-disabled" onClick="ApplyChanges();">Apply</a>
                    <button data-icon="check" data-theme="b" data-inline="true" id="DeskSaleReset" onClick="ResetFields()" >Clear Selection</button>
				</td>
                <td align="right">
                    <a href="#popupDialog" data-rel="popup" data-icon="check" data-theme="b" data-role="button" data-inline="true" data-position-to="window" data-transition="pop" id="DeskSaleSearch" >Refresh</a>
				</td>
			</tr>
		</table>
	</div>
	
	
	
	<div data-role="popup" id="popupDialog" style="width:300px;">
         <ul data-role="listview" data-theme="c">
		  
		    <%
		    
		          
		    ResultSet rs1 = s.executeQuery("select * from common_hierarchy_levels "+HierarchyWhere+" order by authority_order");
			        while(rs1.next()){
			        
			        %>
			         <li><a href="#" id="MoveReportingLevelCmpnyLvlRadio_<%=rs1.getInt("id") %>" onClick="SeeCompanyLevel(<%=rs1.getInt("id")%>)"><%=rs1.getString("short_name") %></a></li>
			        
			        
			       <%
			        }
			        %>
		</ul>
         
         
        
	</div>
	<jsp:include page="LookupUserSearchPopup.jsp" > 
    	<jsp:param value="UserRightsCallBack" name="CallBack" />
    </jsp:include><!-- Include User Search -->
	
	    	
    </div>

</div>
</body>
</html>

