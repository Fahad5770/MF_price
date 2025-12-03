<%@page import="java.sql.ResultSet"%>
<jsp:useBean id="EmpByDpt" class="com.pbc.employee.EmployeeTree" scope="page"/>

			<%			
			ResultSet rs=null;
			
			/*if(request.getParameter("DesignationID").equals("-1")) //no selection of Designation			
			{			 
			  rs = EmpByDpt.getEmployeesByDepartment(Integer.parseInt(request.getParameter("DepartmentID")));
			}
			else
			{
			  rs = EmpByDpt.getEmployeesByDepartmentAndDesignation(Integer.parseInt(request.getParameter("DepartmentID")),Integer.parseInt(request.getParameter("DesignationID")));
			}*/
			rs = EmpByDpt.getEmployeesByDepartment(Integer.parseInt(request.getParameter("DepartmentID")));
			//System.out.println(Integer.parseInt(request.getParameter("DepartmentID")));
			int counter =0;
			if(rs.next())
			{
			%>

			<ul data-role="listview" id="employee_list" class="ui-listview">
				<% do{
					 
				%>
				
					<li id="li_id_<%=counter %>" class="ui-btn ui-btn-icon-right ui-li-has-arrow ui-li ui-btn-up-c"  onclick="selectedColorChange('li_id_<%=counter %>','li_a_id_<%=counter%>')">
							<div class="ui-btn-inner ui-li"><div class="ui-btn-text">
							
							<a href="#" id="<%=rs.getString("sap_code") %>" class="ui-link-inherit" onclick="addNodeToList('<%=rs.getString("sap_code") %>','<%=rs.getString("first_name")+" "+rs.getString("last_name")%>','<%=rs.getString("department_label") %>','<%=rs.getString("designation_label") %>','<%=rs.getString("department_code") %>','<%=rs.getString("designation_code") %>')">
							<span style="font-size:13px;"><%=rs.getString("sap_code") %>&nbsp;-&nbsp;<%= rs.getString("first_name")+" "+rs.getString("last_name") %></span><br/>
							<span class="grayColorDesig" id="li_a_id_<%=counter %>" style="font-size:11px;"><%=rs.getString("designation_label")+" , "+rs.getString("department_label")%></span>
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