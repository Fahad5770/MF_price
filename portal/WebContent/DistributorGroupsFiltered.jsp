<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="com.pbc.workflow.Workflow"%>
<%@page import="com.pbc.workflow.WorkflowDocument"%>
<%@page import="com.pbc.util.UserAccess"%>








<%
Datasource ds = new Datasource();
ds.createConnection();

Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
long GroupID = Utilities.parseLong(request.getParameter("GroupID"));
ResultSet rs1;
%>
    
    
    			<ul data-role="listview" data-filter="true" data-filter-placeholder="Search distributor..." data-inset="true" >
				     <li data-role="list-divider" data-theme="a">Distributors</li>
				    
				    <%
				  if(GroupID==0)
				  {
					   rs1 = s.executeQuery("SELECT distributor_id,name,city,address FROM common_distributors");  
				  }
				  else
				  {
					   rs1 = s.executeQuery("SELECT cg.id,cgl.distributor_id,cg.label,cd.name,cd.city,cd.address FROM common_distributor_groups cg,common_distributor_groups_list cgl,common_distributors cd where cg.id=cgl.id and cgl.distributor_id=cd.distributor_id and cg.id="+GroupID);
				  }
					
					while(rs1.next())
					{%>
						<li onClick="AddDistributor(<%=rs1.getString("distributor_id") %>)" data-theme="c" id="DistributorLi_<%=rs1.getString("distributor_id") %>">
										    	<a href="#" style="font-size: 13px;">									   
				    	<div style="float:left;">
				    		<%=rs1.getString("distributor_id")+" - "+rs1.getString("name") %><br/>
				    			<div style="font-size:12px;"><%=rs1.getString("address") %> - <%=rs1.getString("city") %></div>
				    			<input type="hidden" id="isSelected_<%=rs1.getString("distributor_id") %>" value="0"/>
				    	</div>
				    	
				    	<%
				    	ResultSet rs2 = s2.executeQuery("SELECT dg.id,distributor_id,label FROM pep.common_distributor_groups_list dl,common_distributor_groups dg where dg.id=dl.id and distributor_id="+rs1.getLong("distributor_id"));
				    	while(rs2.next())
				    	{
				    	%>
				    	<span style="font-size:13px; float:right;min-width:80px; min-height:20px; background-color:#cecece; padding:2px; text-align:center;margin-left:5px;"><%=rs2.getString("label") %><label style="color:white; margin-left:10px;cursor:pointer;" onClick="DeleteGroup(<%=rs1.getLong("distributor_id")%>,<%=rs2.getLong("id")%>)">X</label></span>				    	
				    	
				    	<%
				    	}
				    	%>				    	
				    	
				    	</a>
				    </li>
					 <%}
				   %>
				</ul>
				

   
<%
s.close();
s2.close();

ds.dropConnection();
%>




