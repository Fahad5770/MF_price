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
			
			int status = 0;
	        if(session.getAttribute("MonthlyDiscountStatus") != null){
	        	status = (Integer) session.getAttribute("MonthlyDiscountStatus");
	        }
			
			%>

           
           <input type="hidden" name="FilterType" id="FilterType" value="5" >
           
        
			<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
			
			<li data-role="list-divider" data-theme="a">Sampling Type</li>
			
			<li>
	  			<select name="status" id="status" data-mini="true">
					<!-- <option value="-1">All Outlets</option> -->
		            <option value="0">Pending</option>
		            <!-- <option value="1">Approved</option> -->
		            <option value="2">On Hold</option>
		            <option value="3">Cancelled</option>
				</select>
	  		</li>
	  		
	  		<script>
	           
	           		$('#status').val(<%=status%>).change();
	           		
	           
	           </script>
  			
	  		
	  			<!-- <input type="button" name="Apply" id="Apply" data-theme="c" value="Apply Preference" data-icon="check" onClick="AddIntoSession()" data-mini="true" data-iconpos="right"> -->
	  			
	  			<li data-icon="check" >
     				<a href="#" onClick="AddIntoSession()" data-iconpos="left" data-mini="true" style="font-size:10pt">Apply Preference</a>
     			</li>
  			</ul> 
           	
            
           
            
            
        	