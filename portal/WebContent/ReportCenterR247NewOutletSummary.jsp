<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>


<style>
td {
	font-size: 8pt;
}

th {
	font-size: 9pt;
}

#map {
	width: 100%;
	height: 400px;
	margin-top: 10px;
}
</style>



<script type="text/javascript" src="js/Report238.js"></script>



<%
	long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 311;
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));

if (UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false) {
	response.sendRedirect("AccessDenied.jsp");
}

/* Datasource ds = new Datasource();
//ds.createConnectionToReplica();
//Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement(); */
Datasource ds = new Datasource();

ds.createConnection();
Statement s = ds.createStatement();
Statement s2 = ds.createStatement();
Statement s3 = ds.createStatement();

int CensusID = Utilities.parseInt(request.getParameter("CensusID"));
String WhereCensusID = "";

//System.out.println(CensusIDString);

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
	WhereDistributors = " and census_distributor_id in (" + DistributorIDs + ") ";
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
<script>
 	function FormSubmit(ID) {
	
	
		$("#savebutton").addClass("ui-disabled");

		$.mobile.showPageLoadingMsg();
		//alert("hey");
		$.ajax({
			url : "outlet/OutletRequestUpdateExecuteNew",
			data : $('#general_tab').serialize(),
			type : "POST",
			dataType : "json",
			success : function(json) {
				$.mobile.hidePageLoadingMsg();
				$('#savebutton').removeClass("ui-disabled");
				//$('#ResetButton').removeClass("ui-disabled");
				//$('#ViewButton').removeClass("ui-disabled");

				if (json.success == "true") {
					$.mobile.hidePageLoadingMsg();
					alert("Data has been Saved Successfully");

					//window.location = 'NewOutletRequest.jsp';

				} else {
					alert("Data could not be saved.");
				}

			},
			error : function(xhr, status) {
				alert("Server could not be reached.");
				$.mobile.loading('hide');
			}
		});

	} 
	
	function ApproveOutlet(ID) {

		$("#approvebutton").addClass("ui-disabled");
		$("#SuccessData").empty();
		
		$.mobile.showPageLoadingMsg();
		$.ajax({
			url : "outlet/OutletRequestApproveExecuteNew",
			data : $('#general_tab').serialize(),
			type : "POST",
			dataType : "json",
			success : function(json) {
				$.mobile.hidePageLoadingMsg();
				$('#approvebutton').removeClass("ui-disabled");
			

				if (json.success == "true") {
					$.mobile.hidePageLoadingMsg();
					alert("Data has been Saved Successfully");
					var bookedBy=$("#createdBy").val();
					console.log(bookedBy);
					var htmlBox="<div style='background-color: #8f94a3;padding:10px;color:white;'>";
					htmlBox+="<a style='text-decoration:none'>Booked By</a>: <span style='margin-left:5px'>"+bookedBy+"</span><br>";
					htmlBox+="<a style='text-decoration:none'>Generated Outlet ID</a>: <span style='margin-left:5px'>"+json.OutletIDGen+"</span>";

					htmlBox+="</div>";
					$("#SuccessData").append(htmlBox);
					$("#approvebutton").remove();
					$("#savebutton").remove();
					$("#declinebutton").remove();
				} else {
					alert("Data could not be saved.");
				}

			},
			error : function(xhr, status) {
				alert("Server could not be reached.");
				$.mobile.loading('hide');
			}
		});

	}
/* 	function UpdateOutlet(ID) {	
		$("#approvebutton").addClass("ui-disabled");
		$("#SuccessData").empty();
		
		$.mobile.showPageLoadingMsg();
		$.ajax({
			url : "outlet/OutletRequestApproveExecute",
			data : $('#general_tab').serialize(),
			type : "POST",
			dataType : "json",
			success : function(json) {
				$.mobile.hidePageLoadingMsg();
				$('#approvebutton').removeClass("ui-disabled");
			

				if (json.success == "true") {
					$.mobile.hidePageLoadingMsg();
					alert("Data has been Saved Successfully");
					var bookedBy=$("#createdBy").val();
					console.log(bookedBy);
					var htmlBox="<div style='background-color: #8f94a3;padding:10px;color:white;'>";
					htmlBox+="<a style='text-decoration:none'>Updated By</a>: <span style='margin-left:5px'>"+bookedBy+"</span><br>";
					htmlBox+="<a style='text-decoration:none'>Outlet ID</a>: <span style='margin-left:5px'>"+json.OutletIDGen+"</span>";

					htmlBox+="</div>";
					$("#SuccessData").append(htmlBox);
					$("#approvebutton").remove();
					$("#savebutton").remove();
					$("#declinebutton").remove();
				} else {
					alert("Data could not be saved.");
				}

			},
			error : function(xhr, status) {
				alert("Server could not be reached.");
				$.mobile.loading('hide');
			}
		});

	} */
	function DeclineOutlet(ID) {	
		$("#declinebutton").addClass("ui-disabled");

		$("#declineFlag").val(ID);
		$.mobile.showPageLoadingMsg();
		$.ajax({
			url : "outlet/OutletRequestApproveExecuteNew",
			data : $('#general_tab').serialize(),
			type : "POST",
			dataType : "json",
			success : function(json) {
				$.mobile.hidePageLoadingMsg();
				$('#declinebutton').removeClass("ui-disabled");
				if (json.success == "true") {
					$.mobile.hidePageLoadingMsg();
					alert("Outlet Decline Successfully");
					$("#declineFlag").val("");
					//window.location = 'NewOutletRequest.jsp';
					$("#approvebutton").remove();
					$("#savebutton").remove();
					$("#declinebutton").remove();
				} else {
					alert("Data could not be saved.");
				}

			},
			error : function(xhr, status) {
				alert("Server could not be reached.");
				$.mobile.loading('hide');
			}
		});

	}
</script>


<form id="general_tab">
	<ul data-role="listview" data-inset="true"
		style="font-size: 10pt; font-weight: normal; margin-top: -13px;"
		data-icon="false">


		<li data-role="list-divider" data-theme="a">Outlet Summary</li>
		<%
			if (CensusID != 0) {
		%>



		<li>
			<%
				//Outlet Requester
			String Date = null;
			Date date1 = null;

			//Outlet Detail
			String OutletName = "";
			long OutletId = 0;
			String isorder="No";
			double orderAmount=0;
			String channel = "";
			String OutletSubArea = "";
			String OutletArea = "";
			String OwnerName = "";
			String OutletAddress = "";
			String CnicNum = "";
			String ContactNo = "";
			int channelID=0;
			//Area Detail
			String DistributorID = "";
			String DistributorName = "";
			String CreatedBy = "";
			String PurchaserName="";
			String PurchaserMobile="";
			long OutletIDnew=0;
			int isApproved=0;
			int isDeclined=0;
			long mobile_transaction_no=0;
			double lat = 0.0;
			double lng = 0.0;
			int IDs = 0;
			int Order_Amount = 0;
			String City_Name="";
			//String order_available = "";
	System.out.println(
			"SELECT *,(select label from pci_sub_channel where id=cor.sub_channel_id) as channel,(SELECT name FROM common_distributors where distributor_id=cor.distributor_id) as distributor,(SELECT DISPLAY_NAME FROM users where ID=cor.created_by) as display_name,(select city from common_distributors where distributor_id=cor.distributor_id) as city ,id,lat,lng,mobile_transaction_no from common_outlets_request cor where cor.id="
			+ CensusID + "");
			ResultSet rs = s.executeQuery(
					"SELECT *,(select label from pci_sub_channel where id=cor.sub_channel_id) as channel,(SELECT name FROM common_distributors where distributor_id=cor.distributor_id) as distributor,(SELECT DISPLAY_NAME FROM users where ID=cor.created_by) as display_name,(select city from common_distributors where distributor_id=cor.distributor_id) as city ,id,lat,lng,mobile_transaction_no from common_outlets_request cor where cor.id="
					+ CensusID + "");

			while (rs.next()) {
				/* date1 = rs.getDate("created_on");
				Date = Utilities.getDisplayDateFormat(date1); */
				IDs = rs.getInt("id");
				OutletIDnew = rs.getInt("outlet_id");
				channel = rs.getString("channel");
				DistributorID=rs.getString("distributor_id");
				CreatedBy=rs.getString("display_name");
				channelID=rs.getInt("sub_channel_id");
				DistributorName= rs.getString("distributor");
				OutletName = rs.getString("outlet_name");
				OutletAddress = rs.getString("outlet_address");
				OutletArea = rs.getString("area_label");
				OutletSubArea = rs.getString("sub_area_label");
				OwnerName = rs.getString("owner_name");
				CnicNum = rs.getString("owner_cnic_number");
				ContactNo = rs.getString("owner_contact_number");
				PurchaserName=rs.getString("purchaser_name");
				PurchaserMobile=rs.getString("purchaser_number");
				isApproved=rs.getInt("is_approved");
				isDeclined=rs.getInt("is_declined");
				mobile_transaction_no=rs.getLong("mobile_transaction_no");
				lat=rs.getDouble("lat");
				lng=rs.getDouble("lng");
				City_Name=rs.getNString("city");
				System.out.println(lat+"........lat........");
System.out.println(channelID+"................");
ResultSet rsOrder = s2.executeQuery("select total_amount from mobile_order_unregistered where outlet_id="+IDs);
if(rsOrder.first()){
	 isorder="Yes";
	 orderAmount=rsOrder.getDouble("total_amount");
}
                
			}
 

			 

			    // Check if the OrderID exists in the mobile_order_unregistered table
/* 			    ResultSet rsCheck = s.executeQuery("SELECT COUNT(*) , net_amount FROM mobile_order_unregistered WHERE Request_id = " + IDs);
			    if (rsCheck.next()) {
			    	Order_Amount = rsCheck.getInt("net_amount");
			        int count = rsCheck.getInt(1);
			        if (count > 0) {
			            order_available = "Yes"; // Set to "Yes" if ID is found
			        }else{
			            order_available = "No"; // Set to "Yes" if ID is found

			        }
			    } */
			
			%> 
			<input type="hidden" name="ID" value="<%=CensusID%>">
			<input type="hidden" name="lat" value="<%=lat%>">
						<input type="hidden" name="lng" value="<%=lng%>">
						<input type="hidden" name="distributorID" value="<%=DistributorID%>">
			
			<input type="hidden" id="declineFlag" name="declineFlag">
			<input type="hidden" name="channelID" value="<%=channelID%>">
		<input type="hidden" id="createdBy" name="createdBy" value="<%=CreatedBy%>">
				<input type="hidden" id="OutletIDnew" name="OutletIDnew" value="<%=OutletIDnew%>">
<%-- 								<input type="hidden" id="id_for_update" name="id_for_update" value="<%=id_for_update%>">
 --%>				
		
			<table style="width: 100%; margin-top: 15px;">
				<tr>
				</tr>
				<tr style="text-align: left">
					<th style="width: 25%">Distributor</th>

				</tr>
				<tr>
					<td colspan="3" style="width: 25%"><input type="text"
						placeholder="Distributor Name" id="DistributorName" name="DistributorName"
						value="<%=DistributorName%>" readonly></td>

				</tr>
				<tr>
				</tr>
				<tr style="text-align: left">
					<th style="width: 25%">Outlet Name</th>
					<th style="width: 25%">Owner Name</th>
					<th>Outlet Address</th>

				</tr>

				<tr>
					<td  style="width: 25%"><input type="text"
						placeholder="Outlet Name" id="OutletName" name="OutletName"
						value="<%=OutletName%>"></td>
					<td style="width: 25%"><input type="text"
						placeholder="Owner Name" id="OwnerName" name="OwnerName"
						value="<%=OwnerName%>"></td>
					<td><input type="text" placeholder="Outlet Address"
						id="OutletAddress" name="OutletAddress" value="<%=OutletAddress%>"></td>

				</tr>
				<tr>
				</tr>
				<tr style="text-align: left">

					<th>Contact No.</th>
					<th>CNIC No.</th>
					<th>Channel</th>
				</tr>
				<tr>
					<td><input type="text" placeholder="Contact No."
						id="OutletContactNo" name="OutletContactNo" value="<%=ContactNo%>"></td>
					<td><input type="text" placeholder="CNIC No." id="CNICNo"
						name="CNICNo" value="<%=CnicNum%>"></td>
						<%
						String Label="";
						String ID="";

						%>
							<td>
						
									 <select  name="Channel" data-mini="true" id="Channel">
						
									 	<option  id="Channel" value="<%=channelID%>"><%=channel%></option>
						
						<%
						ResultSet rs1 = s.executeQuery("select *from pci_sub_channel");
						while(rs1.next()){
							Label = rs1.getString("label");
							ID = rs1.getString("id");

							%>
						<%-- 	<td><input type="text" placeholder="Channel" id="Channel"
						name="Select Channel" value="<%=channel%>"></td> --%>						
					        <option id="ChannelIDDD" value="<%=ID%>"><%=Label%></option>
							  
							<% 	
						};

						%>
									 </select>	
						</td>
						<%-- 	<td><input type="text" placeholder="Channel" id="Channel"
						name="Select Channel" value="<%=channel%>"></td> --%>
					
				</tr>
				<tr>
				</tr>
				<tr style="text-align: left">

					<th>Area</th>
					<th>Sub Area</th>
					<th>City</th>
					

				</tr>
				<tr>
					<td><input type="text" placeholder="Area" id="OutletArea"
						name="OutletArea" value="<%=OutletArea%>"></td>
					<td><input type="text" placeholder="Sub Area"
						id="OutletSubArea" name="OutletSubArea" value="<%=OutletSubArea%>"></td>
						<td><input type="text" placeholder="City"
						id="City" name="City" value="<%=City_Name%>" Readonly></td>
					
							<%-- <td><input type="text" placeholder="Order"
						id="Order" name="Order" value="<%=order_available%>" disabled></td> --%>
								
				</tr>
					<tr>
				</tr>
				<tr style="text-align: left">

					<th>Purchaser Name</th>
					<th>Purchaser Mobile No.</th>
					<th>Distributor</th>
					
<!-- 	<th>Order Amount</th>
 -->				</tr>
				<tr>
					<td><input type="text" placeholder="Purchaser Name" id="PurchaserName"
						name="PurchaserName" value="<%=PurchaserName%>"></td>
					<td><input type="text" placeholder="Purchaser Mobile No."
						id="PurchaserMobile" name="PurchaserMobile" value="<%=PurchaserMobile%>"></td>
				<td><input type="text" placeholder="Distributor"
						id="Distributor" name="Distributor" value="<%=DistributorID+" - "+DistributorName%>" Readonly></td>
							<%-- <td><input type="text" placeholder="Amount"
						id="Amount" name="Amount"  value="<%=Order_Amount +" "+"Rs"%>" disabled></td> --%>
				</tr>
				<tr>
				</tr>
<tr style="text-align: left">

						<th>Requested By</th>
						<th>Lat</th>
						<th>Lng</th>
					

				</tr>
				<tr>
					<td><input type="text" placeholder="Created By"
						id="CreatedBy" name="CreatedBy" value="<%=CreatedBy%>" Readonly></td>
						<td><input type="text" placeholder="lat"
						id="lat" name="lat" value="<%=lat%>"></td>
								<td><input type="text" placeholder="lng"
						id="lng" name="lng" value="<%=lng%>"></td>
							<%-- <td><input type="text" placeholder="Order"
						id="Order" name="Order" value="<%=order_available%>" disabled></td> --%>
								
				</tr>
				<tr style="text-align: left">
				    <th>Is Order</th>
					<th>Order Amount</th>
				</tr>
				<tr>
					<td><input type="text" placeholder="Is Order"
						id="isorder" name="isorder" readonly value="<%=isorder%>"></td>
					<td><input type="text" placeholder="Order Amount"
						id="orderAmount" name="orderAmount" readonly
						value="<%=orderAmount%>"></td>
				</tr>
				
				<tr>
				</tr>
			</table>
			<table width="100%">
				<tr style="text-align: right">

					<td style="text-align: right"><a href="#" data-icon="check"
						data-ajax="false" data-theme="c" data-role="button" data-mini="true"
						data-inline="true" aclass="ui-disabled" id="savebutton"
						onclick='FormSubmit("<%=CensusID%>")'>Save</a></td>
				</tr>
				
					<tr>
				
				
				<tr></tr>
				
				
			</table> 
		<table class="GridWithBorder">
 
											<%
											String[] arr = { "", "", "", "","","","","","","","","" };
											String[] arrURI = { "", "", "", "","","","","","","","","" };
											int[] arrYear = { 0, 0, 0, 0,0,0,0,0,0,0,0,0 };
											int i = 0;
											System.out.println("select * FROM mobile_outlets_request_files where outlet_request_id=" + mobile_transaction_no);
											ResultSet rsi = s.executeQuery("select * FROM mobile_outlets_request_files where outlet_request_id=" + mobile_transaction_no);
											while (rsi.next()) {
												arr[i] = rsi.getString("filename");
												arrURI[i] =  rsi.getString("uri");
												arrYear[i] = rsi.getInt("year");
												i++;
											}
											System.out.println("i : "+i);
										//	System.out.println("arr[0] : "+arr[0]);
											if (i > 0) {
												System.out.println("arr[0] : "+arr[0]);
											%>
 
											<tr>
 
												<td>
 
													<h3></h3>
													<br />
													<center>
														<img
															src=  "<%=( (arrYear[0]!=0) ? "common/CommonFileDownloadFromPath" : "mobile/MobileFileDownloadOutlet")%>?file=<%=arr[0]%>&filePath=<%=arrURI[0]%>"
															style="width: 200px; height: 200px;" />
													</center>
												</td>
												<%
												if (!arr[1].equals("")) {
													System.out.println("arr[1] : "+arr[1]);
												%>
												<td>
 
													<h3></h3>
													<br />
													<center>
														<img
															src=  "<%=( (arrYear[1]!=0) ? "common/CommonFileDownloadFromPath" : "mobile/MobileFileDownloadOutlet")%>?file=<%=arr[1]%>&filePath=<%=arrURI[1]%>"
															style="width: 200px; height: 200px;" />
													</center>
												</td>
												<%
												}
												%>
											</tr>
 
											<tr>
												<%
												if (!arr[2].equals("")) {
													System.out.println("arr[2] : "+arr[2]);
												%>
												<td>
 
													<h3></h3>
													<br />
													<center>
														<img
															src=  "<%=( (arrYear[2]!=0) ? "common/CommonFileDownloadFromPath" : "mobile/MobileFileDownloadOutlet")%>?file=<%=arr[2]%>&filePath=<%=arrURI[2]%>"
															style="width: 200px; height: 200px;" />
													</center>
												</td>
 
												<td>
													<%
													}
													if (!arr[3].equals("")) {
														System.out.println("arr[3] : "+arr[3]);
													%>
													<h3></h3>
													<br />
													<center>
														<img
															src=  "<%=( (arrYear[3]!=0) ? "common/CommonFileDownloadFromPath" : "mobile/MobileFileDownloadOutlet")%>?file=<%=arr[3]%>&filePath=<%=arrURI[3]%>"
															style="width: 200px; height: 200px;" />
													</center>
												</td>
												<%
												}
												%>
											</tr>
 
 
											<%
											} else {
											%><tr>
												<td><p style="margin-top:12px">No images Found</p></td>
											</tr>
											<%
											}
											%>
										</table>
 
				<div id="SuccessData" style="width:30%;margin-left:40%"></div>
	
		
			<%if(isApproved==0 && isDeclined==0){ %>
			<table style="margin-top:30px">
			<tr style="text-align: right">
<%
//System.out.println("Select *from common_outlets_request where id="+id_for_update);
//ResultSet r = s.executeQuery("Select *from common_outlets where id="+id_for_update);
//if(r.first()){
	%>
<%-- <td style="text-align: right"><a href="#" data-icon="check"
						data-ajax="false" data-theme="b" data-role="button" data-mini="true"
						data-inline="true" aclass="ui-disabled" id="approvebutton"
						onclick='ApproveOutlet("<%=CensusID%>")'>Update</a></td>
						
						<td style="text-align: right"><a href="#" data-icon="check"
						data-ajax="false" data-theme="a" data-role="button" data-mini="true"
						data-inline="true" aclass="ui-disabled" id="declinebutton"
						onclick='DeclineOutlet("<%=CensusID%>")'>Decline</a></td> --%>
	<%
	
//}else{
	
%>
<td style="text-align: right"><a href="#" data-icon="check"
						data-ajax="false" data-theme="b" data-role="button" data-mini="true"
						data-inline="true" aclass="ui-disabled" id="approvebutton"
						onclick='ApproveOutlet("<%=CensusID%>")'>Approve</a></td>
						
						<td style="text-align: right"><a href="#" data-icon="check"
						data-ajax="false" data-theme="a" data-role="button" data-mini="true"
						data-inline="true" aclass="ui-disabled" id="declinebutton"
						onclick='DeclineOutlet("<%=CensusID%>")'>Decline</a></td>

<%
	
	
//}


%>
					
				</tr>
			
			</table> 
			<%
			}
 	}
 %>







			<div data-role="footer" class="ui-bar" data-theme="b"></div>
	</ul>


</form>






<%
	s3.close();
s2.close();
s.close();

ds.dropConnection();
%>