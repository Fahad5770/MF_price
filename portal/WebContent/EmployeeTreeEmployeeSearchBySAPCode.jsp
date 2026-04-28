<%@page import="java.sql.ResultSet"%>
<jsp:useBean id="EmpBySAP" class="com.pbc.employee.EmployeeTree" scope="page"/>

			<%	
			ResultSet rs=null;
			
		    rs = EmpBySAP.searchEmployeesBySAPCode(request.getParameter("SAPCode"));
			
			//System.out.println(Integer.parseInt(request.getParameter("DepartmentID")));
			int counter =0;
			if(rs.next())
			{
				//System.out.println("There is some data "+rs.getString("sname"));
			%>

			<ul data-role="listview" id="employee_list" class="ui-listview">
				<% do{
					
				%>
				
					<li id="li_id_<%=counter %>" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c"  onclick="selectedColorChange('li_id_<%=counter %>','li_a_id_<%=counter%>')">
							<div class="ui-btn-inner ui-li"><div class="ui-btn-text">
							
							<a href="#" id="<%=rs.getString("pernr") %>" class="ui-link-inherit" onclick="addNodeToList('<%=rs.getString("pernr") %>','<%=rs.getString("ename") %>','<%=rs.getString("orgtx") %>','<%=rs.getString("plstx") %>','<%=rs.getString("orgeh") %>','<%=rs.getString("plans") %>')">
							<span style="font-size:13px;"><%=rs.getString("pernr") %>&nbsp;-&nbsp;<%= rs.getString("ename") %></span><br/>
							<span class="grayColorDesig" id="li_a_id_<%=counter %>" style="font-size:11px;"><%=rs.getString("plstx")+" , "+rs.getString("orgtx")%></span>
							</a>
							</div><span class="ui-icon ui-icon-arrow-r ui-icon-shadow">&nbsp;</span>
						</div></li>
				<% 
				counter++;			
				}while (rs.next());
				%>
			</ul>
			<%
			 }
			else
			{ 
				out.println("<label style='padding-left:5px;'>No Record found.</label>");
			}
			%>