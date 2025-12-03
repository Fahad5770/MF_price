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

$(".tr_class_region").not('first').hover(
  function () {
    $(this).css("background","#ececec");
	$(this).css("cursor","pointer");
  }, 
  function () {
    $(this).css("background","");
  }
);

</script>
	
    <ul data-role="listview" data-theme="d" class="nav-search" style="margin:0px" >
          
            <%
            

			Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
            
            ResultSet rs = s.executeQuery("SELECT * FROM common_regions order by region_id");
            boolean IsFound = false;
            while(rs.next()){
            	IsFound = true;
            	%>
            	<li data-icon="arrow-r"><a href="javascript:lookupRegionOnSelect(<%=rs.getString("region_id")%>, '<%=rs.getString("region_short_name")%>')" data-transition="flow" ><%=rs.getString("region_short_name")+" - "+rs.getString("region_name")%></a></li>
            	<%
            }
			if( IsFound == false ){
				%>
                	<li>No record found.</li>
            	<%
			}
            
            s.close();
            c.close();
            ds.dropConnection();
            %>
        	</ul>
            