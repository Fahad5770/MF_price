<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>

<%

long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));

int FeatureID = 97;

if(Utilities.isAuthorized(FeatureID, SessionUserID) == false){
	response.sendRedirect("AccessDenied.jsp");
}


Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();

int region_id = Utilities.parseInt(request.getParameter("region_id"));

%>

		<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
			<tr>
				
				<td style="width: 100%" valign="top">
					<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
							 
							 <thead>
							    <tr style="font-size:11px;">
									<th data-priority="2" style="width: 25%" >&nbsp;</th>
									<th data-priority="1"  style="text-align:center; width: 2%">Status</th>
									<th data-priority="1"  style="text-align:center; width: 5%">Day Off</th>
									<th data-priority="1"  style="text-align:center; width: 5%">Type</th>
									<th data-priority="1"  style="text-align:center; width: 5%">Month Cycle</th>
									<th data-priority="1"  style="text-align:center; width: 20%">RSM</th>
									<th data-priority="1"  style="text-align:center; width: 20%">ASM</th>
									<th data-priority="1"  style="text-align:center; width: 20%">Coordinator</th>
							    </tr>
							  </thead>
							  
							  <%
						   //	  System.out.println("select *, (SELECT DISPLAY_NAME FROM users where id=snd_id) snd_name, (SELECT DISPLAY_NAME FROM users where id=rsm_id) rsm_name, (SELECT DISPLAY_NAME FROM users where id=tdm_id) tdm_name from common_distributors where region_id="+region_id+" order by is_active desc, type_id desc, snd_id desc, rsm_id desc");
								  ResultSet rs = s.executeQuery("select *, (SELECT DISPLAY_NAME FROM users where id=snd_id) snd_name, (SELECT DISPLAY_NAME FROM users where id=rsm_id) rsm_name, (SELECT DISPLAY_NAME FROM users where id=tdm_id) tdm_name from common_distributors where region_id="+region_id+" order by is_active desc, type_id desc, snd_id desc, rsm_id desc");
								  while(rs.next()){
									  
									  String MonthCycle = "";
									  if(rs.getString("month_cycle") != null && !rs.getString("month_cycle").equals("0") ){
										  MonthCycle = rs.getString("month_cycle");
									  }
									  
									  String SNDID = "";
									  if(rs.getString("snd_id") != null && !rs.getString("snd_id").equals("0") ){
										  SNDID = rs.getString("snd_id");
									  }
									  
									  String SNDName = "";
									  if(rs.getString("snd_name") != null ){
										  SNDName = rs.getString("snd_name");
									  }
									  
									  String RSMID = "";
									  if(rs.getString("rsm_id") != null && !rs.getString("rsm_id").equals("0") ){
										  RSMID = rs.getString("rsm_id");
									  }
									  
									  String RSMName = "";
									  if(rs.getString("rsm_name") != null ){
										  RSMName = rs.getString("rsm_name");
									  }
									  
									  String TDMID = "";
									  if(rs.getString("tdm_id") != null && !rs.getString("tdm_id").equals("0") ){
										  TDMID = rs.getString("tdm_id");
									  }
									  
									  String TDMName = "";
									  if(rs.getString("tdm_name") != null ){
										  TDMName = rs.getString("tdm_name");
									  }
									  String SundaySelection = (rs.getInt("is_sunday_off") == 0 ? "selected" : "");
									  String FridaySelection = (rs.getInt("is_sunday_off") == 1 ? "selected" : "");
							  %> 
							
						
									<tr>
				    					<td style="padding-left:20px; font-size:10px">
				    						<%=rs.getString("distributor_id")%> - <%=rs.getString("name")%>
				    						<input type="hidden" name="DistributorID" id="DistributorID" value="<%=rs.getString("distributor_id")%>" >
				    					</td>
				    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;">
				    						<select name="IsActive" id="IsActive_<%=rs.getString("distributor_id")%>" data-mini="true">
				    							<option value="1">Active</option>
				    							<option value="0">In-Active</option>
				    						</select>
				    					</td>
				    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;">
				    						<select name="IsSundayOff" id="IsSunday_<%=rs.getInt("is_sunday_off")%>" value="<%=rs.getInt("is_sunday_off")%>" data-mini="true">
				    							<option value="1"  <%=FridaySelection  %>>Friday</option>
				    							<option value="0" <%=SundaySelection  %>>Sunday</option>
				    						</select>
				    					</td>
				    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;">
											<select name="Type" id="Type_<%=rs.getString("distributor_id")%>" data-mini="true">
											<%
												ResultSet rs2 = s2.executeQuery("SELECT * FROM common_distributors_types order by id");
												while(rs2.next()){
											%>
				    							<option value="<%=rs2.getString("id")%>"><%=rs2.getString("label")%></option>
			    							<%
											}
			    							%>
				    						</select>
				    						
				    						<script>
				    							$('#IsActive_<%=rs.getString("distributor_id")%>').val(<%=rs.getString("is_active")%>).change();
				    							$('#Type_<%=rs.getString("distributor_id")%>').val(<%=rs.getString("type_id")%>).change();
				    						</script>
				    						
										</td>
				    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;">
				    						<input type="text" name="MonthCycle" id="MonthCycle" value="<%=MonthCycle%>" data-mini="true" >
				    					</td>
				    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;">
				    						 
				    						 <div style="float: left; width: 29%;"><input type="text" name="SND_ID" id="SND_ID_<%=rs.getString("distributor_id")%>" value="<%=SNDID%>" data-mini="true" onchange="getEmployeeName(this.value, 'snd_name_<%=rs.getString("distributor_id")%>')" ondblclick="OpenEmployeeSearchPopup(this.id, 'snd_name_<%=rs.getString("distributor_id")%>')" ></div>
				    						 <div style="float: left; width: 1%;">&nbsp;</div>
				    						 <div style="float: left; width: 70%;"><input type="text" name="snd_name" id="snd_name_<%=rs.getString("distributor_id")%>" value="<%=SNDName%>" data-mini="true" readonly="readonly" ></div>
				    						 
				    					</td>
				    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;">
				    						
				    						<div style="float: left; width: 29%;"><input type="text" name="RSM_ID" id="RSM_ID_<%=rs.getString("distributor_id")%>" value="<%=RSMID%>" data-mini="true" onchange="getEmployeeName(this.value, 'rsm_name_<%=rs.getString("distributor_id")%>')" ondblclick="OpenEmployeeSearchPopup(this.id, 'rsm_name_<%=rs.getString("distributor_id")%>')" ></div>
				    						<div style="float: left; width: 1%;">&nbsp;</div>
				    						<div style="float: left; width: 70%;"><input type="text" name="rsm_name" id="rsm_name_<%=rs.getString("distributor_id")%>" value="<%=RSMName%>" data-mini="true" readonly="readonly" ></div>
				    						
				    					</td>
				    					<td style="text-align:right; padding-right:10px; border-left: 1px solid #ececec; border-right: 1px solid #ececec; text-align: center;">
				    						
				    						<div style="float: left; width: 29%;"><input type="text" name="TDM_ID" id="TDM_ID_<%=rs.getString("distributor_id")%>" value="<%=TDMID%>" data-mini="true" onchange="getEmployeeName(this.value, 'tdm_name_<%=rs.getString("distributor_id")%>')" ondblclick="OpenEmployeeSearchPopup(this.id, 'tdm_name_<%=rs.getString("distributor_id")%>')" ></div>
				    						<div style="float: left; width: 1%;">&nbsp;</div>
				    						<div style="float: left; width: 70%;"><input type="text" name="tdm_name" id="tdm_name_<%=rs.getString("distributor_id")%>" value="<%=TDMName%>" data-mini="true" readonly="readonly" ></div>
				    						
				    					</td>
				    					
				    		    	</tr>
				    		    	
				    		    	
				    		    	
				    		  <%
							  	}
				    		  %>
									
									
						</table>
				</td>
			</tr>
		</table>

<%
s2.close();
s.close();
ds.dropConnection();
%>
