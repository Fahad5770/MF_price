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
	long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 130;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
	response.sendRedirect("AccessDenied.jsp");
}

String checkHibernation = (SessionUserID != 204220064) ? " and dbpauov.outlet_active=1" : "";

Datasource ds = new Datasource();
ds.createConnectionToReplica();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

String PJPIDsString = request.getParameter("PJPID");
String WherePJPID = "";

if (!PJPIDsString.equals("x125")) { //mean all PJP selected
	long PJPID = Utilities.parseLong(PJPIDsString);
	WherePJPID = " and dbpauov.id=" + PJPID;
}

//Distributor

boolean IsDistributorSelected = false;
long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors") != null) {
	SelectedDistributorsArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedDistributors");
	IsDistributorSelected = true;
} else {
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributorSecondarySales(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];

	for (int x = 0; x < UserDistributor.length; x++) {
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}

String DistributorIDs = "";
String WhereDistributors = "";

if (SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0) {
	for (int i = 0; i < SelectedDistributorsArray.length; i++) {

		if (i == 0) {
	DistributorIDs += SelectedDistributorsArray[i];
		} else {
	DistributorIDs += ", " + SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and dbpauov.distributor_id in (" + DistributorIDs + ") ";
	//out.print(WhereDistributors);
}

//RSM

String RSMIDs = "";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID + "_SR1SelectedRSM") != null) {
	SelectedRSMArray = (long[]) session.getAttribute(UniqueSessionID + "_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
if (RSMIDs.length() > 0) {
	WhereDistributors = " and dbpauov.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("
	+ RSMIDs + ")) ";
}

//Outlet Type

String OutletTypes = "";
String SelectedOutletTypeArray[] = {};
if (session.getAttribute(UniqueSessionID + "_SR1SelectedOutletType") != null) {
	SelectedOutletTypeArray = (String[]) session.getAttribute(UniqueSessionID + "_SR1SelectedOutletType");
	//OutletTypes = Utilities.serializeForSQL(SelectedOutletTypeArray);
}

String WhereDiscountedAll = "";
String WhereDiscountedFixed = "";
String WhereDiscountedPerCase = "";
String WhereActive = "";
String WhereDeactivated = "";
String WhereNonDiscountedAll = "";

for (int i = 0; i < SelectedOutletTypeArray.length; i++) {
	if (SelectedOutletTypeArray[i].equals("Discounted - All")) {
		WhereDiscountedAll = " and co.id in (select outlet_id from sampling where active = 1) ";
	}

	if (SelectedOutletTypeArray[i].equals("Discounted - Fixed")) {
		WhereDiscountedFixed = " and co.id in (select outlet_id from sampling where active = 1 and date(now()) between fixed_valid_from and fixed_valid_to and fixed_company_share != 0) ";
	}

	if (SelectedOutletTypeArray[i].equals("Discounted - Per Case")) {
		WhereDiscountedPerCase = " and co.id in (select distinct outlet_id from sampling s join sampling_percase sp on s.sampling_id = sp.sampling_id where s.active = 1 and date(now()) between sp.valid_from and sp.valid_to) ";
	}

	if (SelectedOutletTypeArray[i].equals("Non Discounted")) {
		WhereNonDiscountedAll = " and co.id not in (select outlet_id from sampling where active = 1) ";
	}

	if (SelectedOutletTypeArray[i].equals("Active")) {
		WhereActive = " and co.is_active=1 ";
	}

	if (SelectedOutletTypeArray[i].equals("Deactivated")) {
		WhereDeactivated = " and co.is_active=0 ";
	}
}
%>



<ul data-role="listview" data-inset="true"
	style="font-size: 10pt; font-weight: normal; margin-top: -13px;"
	data-icon="false">
	<li data-role="list-divider" data-theme="a">Outlet Summary</li>
	<li>


		<table style="width: 100%; margin-top: -8px" cellpadding="0"
			cellspacing="0">
			<tr>

				<td style="width: 100%" valign="top">
					<table border=0
						style="font-size: 13px; font-weight: 400; width: 100%"
						cellpadding="0" cellspacing="0" adata-role="table"
						class="GridWithBorder">
						<thead>
							<tr style="font-size: 11px;">
								<th data-priority="1" style="text-align: center; width: 25%">Order
									Booker</th>
								<th data-priority="1" style="text-align: center; width: 20%">Name</th>
								<th data-priority="1" style="text-align: center; width: 25%">Address</th>
								<th data-priority="1" style="text-align: center; width: 10%">Days</th>
								<th data-priority="1" style="text-align: center; width: 10%">Last
									Sale</th>
								<th data-priority="1" style="text-align: center; width: 10%">Latitude</th>
								<th data-priority="1" style="text-align: center; width: 10%">Longitude</th>
								<th data-priority="1" style="text-align: center; width: 10%">Channel</th>
								<th data-priority="1" style="text-align: center; width: 10%">Created
									On</th>
								<th data-priority="1" style="text-align: center; width: 10%">Week</th>
							</tr>

						</thead>

						<%
							//ResultSet rs = s.executeQuery("SELECT co.id,co.name,(select max(created_on) last_order FROM mobile_order mo where mo.outlet_id = co.id) last_order,co.address,(select Concat(coc.contact_name,' ',coc.contact_number) contact from common_outlets_contacts coc where coc.outlet_id=co.id and coc.is_primary=1) contact ,(select group_concat((select short_name from common_days_of_week where id = dbpav.day_number)) days from  distributor_beat_plan_all_view dbpav where 1=1 "+WherePJPID+WhereDistributors+" and outlet_id = co.id) days, (select assigned_to from  distributor_beat_plan_all_view dbpav where 1=1 "+WherePJPID+WhereDistributors+" limit 1) assigned_to FROM common_outlets co where co.id in(select outlet_id from  distributor_beat_plan_all_view where 1=1 "+WherePJPID+WhereDistributors+" )"+WhereDiscountedAll+WhereDiscountedFixed+WhereDiscountedPerCase+WhereNonDiscountedAll+WhereActive+WhereDeactivated+" order by last_order desc");
						int MonOutletCount = 0;
						int TueOutletCount = 0;
						int WedOutletCount = 0;
						int ThuOutletCount = 0;
						int FriOutletCount = 0;
						int SatOutletCount = 0;
						int SunOutletCount = 0;

						int F1OutletCount = 0;
						int F2OutletCount = 0;
						int F3OutletCount = 0;
						int F4OutletCount = 0;
						int F5OutletCount = 0;
						int F6OutletCount = 0;
						int F7OutletCount = 0;

						String OutletDays = "";
						int counter = 0;

						int TotalTOTInj = 0;

						System.out.println(
								"SELECT dbpauov.id, dbpauov.assigned_to, dbpauov.assigned_to_name, dbpauov.outlet_id, co.name, co.address,co.lat,co.lng,co.pic_channel_id,(select coc.contact_nic from common_outlets_contacts coc where coc.outlet_id=co.id)cnic,(select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id) channel_label, dbpauov.days, (select max(created_on) from inventory_sales_adjusted where outlet_id = dbpauov.outlet_id ) last_sales, (SELECT count(*) FROM common_assets where outlet_id = dbpauov.outlet_id and tot_status = 'INJECTED') tot_injected, co.created_on as outlet_created_on FROM distributor_beat_plan_all_unique_outlets_view dbpauov join common_outlets co on dbpauov.outlet_id = co.id where 1 = 1 "
								+ WherePJPID + WhereDistributors + " " + WhereDiscountedAll + WhereDiscountedFixed
								+ WhereDiscountedPerCase + WhereNonDiscountedAll + checkHibernation +" order by assigned_to desc, last_sales desc");

						ResultSet rs = s.executeQuery(
								"SELECT dbpauov.id, dbpauov.assigned_to, dbpauov.assigned_to_name, dbpauov.outlet_id, co.name, co.address,co.lat,co.lng,co.pic_channel_id,(select coc.contact_nic from common_outlets_contacts coc where coc.outlet_id=co.id)cnic,(select psc.label from pci_sub_channel psc where psc.id=co.pic_channel_id) channel_label, dbpauov.days, (select max(created_on) from inventory_sales_adjusted where outlet_id = dbpauov.outlet_id ) last_sales, (SELECT count(*) FROM common_assets where outlet_id = dbpauov.outlet_id and tot_status = 'INJECTED') tot_injected, co.created_on as outlet_created_on FROM distributor_beat_plan_all_unique_outlets_view dbpauov join common_outlets co on dbpauov.outlet_id = co.id where 1 = 1 "
								+ WherePJPID + WhereDistributors + " " + WhereDiscountedAll + WhereDiscountedFixed
								+ WhereDiscountedPerCase + WhereNonDiscountedAll + checkHibernation +" order by assigned_to desc, last_sales desc");
						while (rs.next()) {

							try {

								System.out.println(rs.getString("days"));
								OutletDays = rs.getString("days");
								TotalTOTInj += rs.getInt("tot_injected");

								//counting the commas

								counter = 0;
								for (int i = 0; i < OutletDays.length(); i++) {
							if (OutletDays.charAt(i) == ',') {
								counter++;
							}
								}

								counter++; //because comma + 1 = number of words

								if (OutletDays.contains("Mon")) {
							MonOutletCount++;
								}
								if (OutletDays.contains("Tue")) {
							TueOutletCount++;
								}
								if (OutletDays.contains("Wed")) {
							WedOutletCount++;
								}
								if (OutletDays.contains("Thu")) {
							ThuOutletCount++;
								}
								if (OutletDays.contains("Fri")) {
							FriOutletCount++;
								}
								if (OutletDays.contains("Sat")) {
							SatOutletCount++;
								}
								if (OutletDays.contains("Sun")) {
							SunOutletCount++;
								}

								//Frequencey count

								if (counter == 1) {
							F1OutletCount++;
								}
								if (counter == 2) {
							F2OutletCount++;
								}
								if (counter == 3) {
							F3OutletCount++;
								}
								if (counter == 4) {
							F4OutletCount++;
								}
								if (counter == 5) {
							F5OutletCount++;
								}
								if (counter == 6) {
							F6OutletCount++;
								}
								if (counter == 7) {
							F7OutletCount++;
								}
								
								ResultSet rsWeek = s2.executeQuery("select is_alternative from distributor_beat_plan_schedule where outlet_id="+rs.getString("outlet_id")+" limit 1");
								int isAlternate = (rsWeek.first()) ? rsWeek.getInt("is_alternative") : 0;
						%>
						<tr style="font-size: 10px;">
							<td><%=rs.getString("assigned_to") + " - " + rs.getString("assigned_to_name")%></td>
							<td><%=rs.getString("outlet_id")%> - <%=rs.getString("name")%></td>
							<td><%=rs.getString("address")%></td>
							<td><%=rs.getString("days")%></td>
							<td>
								<%
									if (rs.getDate("last_sales") != null) {
								%><%=Utilities.getDisplayDateFormat(rs.getDate("last_sales"))%>
								<%
									}
								%>
							</td>
							<td><%=rs.getString("lat")%></td>
							<td><%=rs.getString("lng")%></td>
							<td>
								<%
									if (rs.getString("channel_label") != null) {
								%><%=rs.getString("channel_label")%>
								<%
									}
								%>
							</td>
							<td><%=Utilities.getDisplayDateFormat(rs.getDate("outlet_created_on"))%></td>
							<td>
								<%=( (isAlternate==0) ? "1-3" : "2-4" )%>
								
							</td>
						</tr>
						<%
							} catch (Exception e) {
						e.printStackTrace();
						}
						}

						int TotalOutletCount = MonOutletCount + TueOutletCount + WedOutletCount + ThuOutletCount + FriOutletCount
								+ SatOutletCount + SunOutletCount;
						int FTotalOutletCount = F1OutletCount + F2OutletCount + F3OutletCount + F4OutletCount + F5OutletCount + F6OutletCount
								+ F7OutletCount;
						%>
						<!-- 
								<tr style="font-size: 10px;">
									<td></td>
									<td></td>
									<td></td>
									<td><b></b></td>
									<td></td>
									<td ></td>
								
								</tr>			
								 -->

					</table>
				</td>
			</tr>
		</table> <br />

		<h3>Summary</h3>

		<table style="width: 100%;" cellpadding="0" cellspacing="0">
			<tr>
				<td style="width: 50%" valign="top">
					<table border=0
						style="font-size: 13px; font-weight: 400; width: 90%"
						cellpadding="0" cellspacing="0" adata-role="table"
						class="GridWithBorder">
						<tr style="font-size: 10px;">
							<th>Day</th>
							<th style="text-align: center;">Calls</th>
						</tr>
						<tr style="font-size: 10px;">
							<td>Monday</td>
							<td style="text-align: center;">
								<%
									if (MonOutletCount != 0) {
								%><%=MonOutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>Tuesday</td>
							<td style="text-align: center;">
								<%
									if (TueOutletCount != 0) {
								%><%=TueOutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>Wednesday</td>
							<td style="text-align: center;">
								<%
									if (WedOutletCount != 0) {
								%><%=WedOutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>Thursday</td>
							<td style="text-align: center;">
								<%
									if (ThuOutletCount != 0) {
								%><%=ThuOutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>Friday</td>
							<td style="text-align: center;">
								<%
									if (FriOutletCount != 0) {
								%><%=FriOutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>Saturday</td>
							<td style="text-align: center;">
								<%
									if (SatOutletCount != 0) {
								%><%=SatOutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>Sunday</td>
							<td style="text-align: center;">
								<%
									if (SunOutletCount != 0) {
								%><%=SunOutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td><b>Total Calls</b></td>
							<td style="text-align: center;"><b>
									<%
										if (TotalOutletCount != 0) {
									%><%=TotalOutletCount%>
									<%
										}
									%>
							</b></td>
						</tr>
					</table>
				</td>
				<td style="width: 50%" valign="top">
					<table border=0
						style="font-size: 13px; font-weight: 400; width: 90%"
						cellpadding="0" cellspacing="0" adata-role="table"
						class="GridWithBorder">
						<tr style="font-size: 10px;">
							<th>Frequency</th>
							<th style="text-align: center;">No of Outlets</th>
						</tr>
						<tr style="font-size: 10px;">
							<td>1</td>
							<td style="text-align: center;">
								<%
									if (F1OutletCount != 0) {
								%><%=F1OutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>2</td>
							<td style="text-align: center;">
								<%
									if (F2OutletCount != 0) {
								%><%=F2OutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>3</td>
							<td style="text-align: center;">
								<%
									if (F3OutletCount != 0) {
								%><%=F3OutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>4</td>
							<td style="text-align: center;">
								<%
									if (F4OutletCount != 0) {
								%><%=F4OutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>5</td>
							<td style="text-align: center;">
								<%
									if (F5OutletCount != 0) {
								%><%=F5OutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>6</td>
							<td style="text-align: center;">
								<%
									if (F6OutletCount != 0) {
								%><%=F6OutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td>7</td>
							<td style="text-align: center;">
								<%
									if (F7OutletCount != 0) {
								%><%=F7OutletCount%>
								<%
									}
								%>
							</td>
						</tr>
						<tr style="font-size: 10px;">
							<td><b>Total Outlets</b></td>
							<td style="text-align: center;"><b>
									<%
										if (FTotalOutletCount != 0) {
									%><%=FTotalOutletCount%>
									<%
										}
									%>
							</b></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>




	</li>
</ul>



<%
	s3.close();
s2.close();
s.close();
c.close();
ds.dropConnection();
%>