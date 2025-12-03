<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.outlet.Outlet"%>
<%@page import="com.pbc.common.Region"%>
<%@page import="com.pbc.util.UserAccess"%>
<style>
.ui-btn {
text-align: left
}
</style>
<script>

</script>

        <%
        
        String OutletQueryString = "";
        if(session.getAttribute("MonthlyDiscountOutlets") != null){
        	OutletQueryString = (String) session.getAttribute("MonthlyDiscountOutlets");
        }
        
        %>   
           <input type="hidden" name="FilterType" id="FilterType" value="3" >
           
        
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
			
			<li data-role="list-divider" data-theme="a">Outlet</li>
			
			<li>
	  			<input type="text" name="MonthlyDiscountMainOutlets" id="MonthlyDiscountMainOutlets" value="<%=OutletQueryString%>" data-mini="true" >
	  		</li>
	  		
	  			<!-- <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddIntoSession()" data-mini="true" data-iconpos="right"> -->
	  			
	  			<li data-icon="check" >
     				<a href="#" onClick="AddIntoSession()" data-iconpos="left" data-mini="true" style="font-size:10pt">Apply Preference</a>
     			</li>
  			</ul> 
           	
            
           
            
            
        	