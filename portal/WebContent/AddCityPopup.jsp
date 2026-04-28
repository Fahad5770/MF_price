<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.sql.Statement"%>

<%
long SessionUserID = Long.parseLong((String) session.getAttribute("UserID"));
int FeatureID = 450;
if (Utilities.isAuthorized(FeatureID, SessionUserID) == false) {
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
%>

<script>
	$(function() {
		$("#add_City").click(
				function() {
					var flag = false;
					var city = $("#addCity").val();
					if (city == " " || city == "") {
						alert("City Should be in Alphabets");
						flag = true;
					}
					if (flag == false) {
						$.ajax({
							url : "common/CityExecute",
							data : {
								city : city,
								operation : "add"
							},
							type : "POST",
							dataType : "json",
							success : function(json) {

								if (json.success == "true") {
									alert("Add City Succesfully");
									$('#LookupUserSearch').popup("close");
									$("#city").append(
											$("<option>").attr("value",
													json.newId).text(city));
									$('#city').val(json.newId).selectmenu(
											"refresh", true);
								} else {
									alert("City Already Exists");
								}

							},

						});
					}

				});

	});
</script>

<div data-role="popup" id="LookupUserSearch" data-overlay-theme="a"
	data-theme="d" data-dismissible="true" class="ui-corner-all">

	<div data-role="header" data-theme="a" class="ui-corner-top">
		<h1>Add city</h1>
	</div>
	<div data-role="content" data-theme="d"
		class="ui-corner-bottom ui-content">

		<div class="ui-block-a" style="width: 100%">
			<form data-ajax="false" name="LookupUserSearchForm"
				id="LookupUserSearchForm">


				<div class="ui-grid-a">
					<div class="ui-block-a">
						<input type="text" name="addCity" id="addCity" value=""
							placeholder="Add City">

					</div>
					<div class="ui-block-b">
						<a data-icon="plus" data-role="button" data-mini="true"
							style="width: 14%; margin-top: 9px;" data-theme="b" id="add_City"
							href="#">Save</a>
					</div>

				</div>
				<!-- /grid-b -->



			</form>


			<div class="ui-grid-b" style="width: 100%;">
				<div class="ui-block-a" style="width: 100%;">
					<%
					ResultSet rs = s.executeQuery("SELECT GROUP_CONCAT(\" \",label) as name FROM common_cities");
					while (rs.next()) {
					%>

					<%=rs.getString("name")%>

					<%
					}
					%>
				</div>

			</div>



		</div>


	</div>
</div>
