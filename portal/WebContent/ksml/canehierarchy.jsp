<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Calendar"%>

<%
Datasource ds = new Datasource();
ds.createConnectionKSML();
Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
Statement s3 = ds.createStatement();
%>
<html>
  <head>
    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript">
      google.charts.load('current', {packages:["orgchart"]});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Name');
        data.addColumn('string', 'Manager');
        data.addColumn('string', 'ToolTip');

        // For each orgchart box, provide the name, manager, and tooltip to show.
        data.addRows([
                      
                      <%
                      ResultSet rs = s.executeQuery("select * from employee_hierarchy order by id");
                      while(rs.next()){
                    	  
                      %>
          [{v:'<%=rs.getString("id")%>', f:'<div style="color:<%=rs.getString("color")%>; font-size: 14px"><%=rs.getString("area_name")%></div><span style="font-size: 10px;"><b><%=rs.getString("employee_name")%></b><br><%=rs.getString("designation")%></span>'},
           '<%if (rs.getInt("id") != 1){out.print(rs.getString("reporting_to"));}%>', '<%=rs.getString("id")%>'],
       				  <%
                      }
     				  %>
          
        ]);

        // Create the chart.
        var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
        // Draw the chart, setting the allowHtml option to true for the tooltips.
        //
        //data.collapse(1, true);
        
        //chart.collapse(2, true);
        chart.draw(data, {allowHtml:true,allowCollapse: true});
        /*for (var i = 5; i < data.getNumberOfRows(); i++) {
    		chart.collapse(i, true);
  		}*/
        
      }
   </script>
    </head>
  <body>
    <div id="chart_div"></div>
  </body>
</html>
<%
ds.dropConnection();
%>