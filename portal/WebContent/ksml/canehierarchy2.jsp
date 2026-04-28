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
      google.charts.load('current', {packages:['wordtree']});
      google.charts.setOnLoadCallback(drawSimpleNodeChart);
      function drawSimpleNodeChart() {
        var nodeListData = new google.visualization.arrayToDataTable([
          ['id', 'childLabel', 'parent', '',{ role: 'style' }],
          <%
          ResultSet rs = s.executeQuery("select * from employee_hierarchy order by id");
          while(rs.next()){
        	  String color = "black";
        	  String area = rs.getString("area_name");
        	  area = area.replace("<br>", " ");
        	  area = area.replace("<br/>", " ");
        	  area = area.replace("n/a", "");
        	  
        	  String title = rs.getString("designation") +" "+rs.getString("employee_name");
        	  title = title.replace("<br>", " ");
        	  title = title.replace("<br/>", " ");
        	  title = title.replace("n/a", "");
        	  
        	  if (area.length() > 1){
        		  color = rs.getString("color");
        		  title = area + " - " + title;
        	  }
        	  
          %>
          	[<%=rs.getInt("id")-1%>,'<%=title%>',<%if (rs.getInt("reporting_to") == 0){out.print("-1");}else{out.print(rs.getInt("reporting_to")-1);}%>,1,'<%=color%>']<%if (!rs.isLast()){out.print(",");}%>
          <%
          }
          %>
		]);
		
        var options = {
        width: '1200px',
          maxFontSize: 15,
          colors: ['black', 'black', 'black'],
          wordtree: {
            format: 'explicit',
            type: 'suffix'
          }
        };

        var wordtree = new google.visualization.WordTree(
          document.getElementById('wordtree_explicit_maxfontsize'));
        wordtree.draw(nodeListData, options);
      }
    </script>
  </head>
  <body>
    <div id="wordtree_explicit_maxfontsize" style="width: 1200px; height: 600px;"></div>
  </body>
</html>
