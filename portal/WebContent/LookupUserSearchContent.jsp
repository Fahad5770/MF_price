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
                <th data-priority="1" nowrap="nowrap" style="text-align:left">User ID</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Name</th>                               
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Email</th>
                <th data-priority="1" nowrap="nowrap" style="text-align:left">Distributor ID</th>
            </tr>
          
            <%
            

			Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
			long SAPCode = Utilities.parseLong(request.getParameter("UserSearchFormUserID"));
			
			String FirstName = Utilities.filterString(request.getParameter("UserSearchFormFirstName"), 1, 100);
			String LastName = Utilities.filterString(request.getParameter("UserSearchFormLastName"), 1, 100);
			
			String Email = Utilities.filterString(request.getParameter("UserSearchFormEmail"), 1, 100);
			long DistributorID = Utilities.parseLong(request.getParameter("UserSearchFormDistributorID"));
			//System.out.println("hello "+FirstName);
            String where = ""; 
            
            if(SAPCode > 0){
            	where += " and id like '"+SAPCode+"%'  ";
            }
            
            if(FirstName != null && FirstName.length() > 0){
            	where += " and first_name like '%"+FirstName+"%'  ";
            }
            
            if(LastName != null && LastName.length() > 0){
            	where += " and last_name like '%"+LastName+"%'  ";
            }
            
            if(Email != null && Email.length() > 0){
            	where += " and email like '%"+Email+"%'  ";
            }
            
            if(DistributorID > 0){
            	where += " and distributor_id like '%"+DistributorID+"%'  ";
            }
            
            ResultSet rs = s.executeQuery("SELECT * FROM pep.users where 1=1 "+where+" order by first_name, last_name");
            boolean IsFound = false;
            while(rs.next()){
            	IsFound = true;
            	int iddddd = Integer.parseInt(rs.getString("id"));
            	%>
                <tr class="tr_class" onclick="lookupUserOnSelect(<%=iddddd%>, '<%=rs.getString("first_name")+" "+rs.getString("last_name")%>')">            
	                <td style="font-weight: 100;"><%=rs.getString("id")%></td>
                	<td style="font-weight: 100;" nowrap="nowrap"><%=WordUtils.capitalize(Utilities.truncateStringToMax(rs.getString("first_name")+" "+rs.getString("last_name"), 15))%></td>
                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("email"), 15)%></td>
                    <td style="font-weight: 100;" nowrap="nowrap"><%=Utilities.truncateStringToMax(rs.getString("distributor_id"), 15)%></td>
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
            