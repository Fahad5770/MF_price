<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.reports.SalesIndex"%>
<%@page import="java.util.Calendar"%>



<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<style>
td{
font-size: 8pt;
}

</style>




<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; m1argin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Distributor Beat Plan Log</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<%
			Datasource ds = new Datasource();
			ds.createConnection();
			Connection c = ds.getConnection();
			Statement s = c.createStatement();
			Statement s1 = c.createStatement();
			
			for(int Month=5; Month<=6;Month++){
				int LastDayOfMonth=0;
				String LogDate="";
				String LogDate1="";
				String Day="";
				Date d = new Date();
				if(Month==5){//May
					LastDayOfMonth = 31;
				}else if(Month==6){
					LastDayOfMonth = 30;
				}
					
				for(int i=1;i<=LastDayOfMonth;i++){
					if(i<10){
						Day = "0"+i;
					}else{
						Day =i+"";
					}
					LogDate =Day+"/0"+Month+"/2014";
					//System.out.println(LogDate);
					d = Utilities.parseDate(LogDate);
					LogDate1 = Utilities.getSQLDateTime(d);		
					//System.out.println("insert into distributor_beat_plan_log(id,label,distributor_id,product_group_id,product_group_name,created_on,created_by,updated_on,updated_by,outlet_id,day_number,assigned_to,assigned_on,assigned_by,log_date) SELECT id,label,distributor_id,product_group_id,product_group_name,created_on,created_by,updated_on,updated_by,outlet_id,day_number,assigned_to,assigned_on,assigned_by,"+LogDate1+" as log_date FROM distributor_beat_plan_all_view");
					
					s.executeUpdate("delete from distributor_beat_plan_log where date(log_date)=date(now())");
				    s1.executeUpdate("insert into distributor_beat_plan_log(id,label,distributor_id,product_group_id,product_group_name,created_on,created_by,updated_on,updated_by,outlet_id,day_number,assigned_to,assigned_on,assigned_by,log_date) SELECT id,label,distributor_id,product_group_id,product_group_name,created_on,created_by,updated_on,updated_by,outlet_id,day_number,assigned_to,assigned_on,assigned_by,"+LogDate1+" as log_date FROM distributor_beat_plan_all_view");
					
					
					
					
				}
			}
			s.close();	
			s1.close();
			%>
			
			
			
			
		</td>
	</tr>
</table>

	</li>	
</ul>

