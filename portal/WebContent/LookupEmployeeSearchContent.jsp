<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="org.apache.commons.lang3.text.WordUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>


<script>

$(".tr_class").not('first').hover(
  function () {
    $(this).css("background","#ececec");
	$(this).css("cursor","pointer");
  }, 
  function () {
    $(this).css("background","");
  }
);

</script>
	
    <table border=0 style="width:100%">
          
            <tr>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">SAP Code</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Name</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Department</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Designation</th>
            </tr>
          
            <%
            

			Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
			long SAPCode = Utilities.parseLong(request.getParameter("EmployeeDashboardSearchFormSAPCode"));
			
			String FirstName = Utilities.filterString(request.getParameter("EmployeeDashboardSearchFormFirstName"), 1, 100);
			String LastName = Utilities.filterString(request.getParameter("EmployeeDashboardSearchFormLastName"), 1, 100);
			
			String Department = Utilities.filterString(request.getParameter("EmployeeDashboardSearchFormDepartment"), 1, 100);
			String Designation = Utilities.filterString(request.getParameter("EmployeeDashboardSearchFormDesignation"), 1, 100);
			
            String where = ""; 
            
            if(SAPCode > 0){
            	where += " and sap_code like '"+SAPCode+"%'  ";
            }
            
            if(FirstName != null && FirstName.length() > 0){
            	where += " and first_name like '%"+FirstName+"%'  ";
            }
            
            if(LastName != null && LastName.length() > 0){
            	where += " and last_name like '%"+LastName+"%'  ";
            }
            
            if(Department != null && Department.length() > 0){
            	where += " and department_label like '%"+Department+"%'  ";
            }
            
            if(Designation != null && Designation.length() > 0){
            	where += " and designation_Label like '%"+Designation+"%'  ";
            }
            
            ResultSet rs = s.executeQuery("SELECT * FROM employee_view where 1=1 "+where+" order by first_name, last_name");
            boolean IsFound = false;
            while(rs.next()){
            	IsFound = true;
            	%>
                <tr class="tr_class" onclick="lookupEmployeeOnSelect(<%=rs.getString("sap_code")%>, '<%=rs.getString("first_name")+" "+rs.getString("last_name")%>')">            
	                <td style="font-weight: 100;"><%=rs.getString("sap_code")%></td>
                	<td style="font-weight: 100;" nowrap="nowrap"><%=WordUtils.capitalize(Utilities.truncateStringToMax(rs.getString("first_name")+" "+rs.getString("last_name"), 15))%></td>
                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("department_label"), 15)%></td>
                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("designation_label"), 15)%></td>
                </tr>
            	<%
            	
            }
            
			
			if( IsFound == false ){
				%>
                <tr>
                	<td colspan="4" style="text-align:center">No record found.</td>
                </tr>
            	<%
			}
            
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</table>
            