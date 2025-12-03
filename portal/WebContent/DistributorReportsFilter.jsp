<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.common.DocumentHeader"%>
<%@page import="com.pbc.inventory.DeliveryNote"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Statement"%>

<script>

</script>

  <%
//Filters

int ReportID = Utilities.parseInt(request.getParameter("ReportID"));

 boolean isDistributorVisible = true;
 boolean isDateRangeVisible = true;
 boolean isPackageVisible = true;
 boolean isBrandVisible = true;
 boolean isOrderBookerVisible = true;
 boolean isVehicleVisible = true;
 boolean isEmployeeVisible = true;
 boolean isOutletVisible = true;
 boolean isPJPVisible = true;
 boolean isHODVisible = true;
 //boolean is
 
 if (ReportID == 14){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = true;
	  isPJPVisible = true;
	  isHODVisible = true;
}
 if (ReportID == 13){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
}
 if (ReportID == 12){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
}
 if (ReportID == 11){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
}
 
 if (ReportID == 10){ // Adjusted Sales Summary
	isVehicleVisible = false;
	isEmployeeVisible = false;
	isOutletVisible = false;
	 isPJPVisible = false;
	 isHODVisible = false;
 }
  
 if (ReportID == 8){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = true;
	  isPJPVisible = false;
	  isHODVisible = false;
}
 
 if (ReportID == 9){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
}
 
 if (ReportID == 7){ 
	  //isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
}

 if (ReportID == 6){ 
	  isDistributorVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
}

 if (ReportID == 5){ // Outlet List
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
 }
 
 if (ReportID == 4){ // Dispatch Summary
	 isPackageVisible = false;
	 isBrandVisible = false;
	 isOrderBookerVisible = false;
	 isOutletVisible = false;
	 isPJPVisible = false;
	 isHODVisible = false;
 }
 if (ReportID == 3){ // Order Activity
	isVehicleVisible = false;
	isEmployeeVisible = false;
	isOutletVisible = false;
	 isPJPVisible = false;
	 isHODVisible = false;
 }
 if (ReportID == 2){ // Stock Position
	isVehicleVisible = false;
	isEmployeeVisible = false;
	isOrderBookerVisible = false;
	isOutletVisible = false;
	 isPJPVisible = false;
	 isHODVisible = false;
 }
 if (ReportID == 1){ // Activity Summary
	isVehicleVisible = false;
	isEmployeeVisible = false;
	isOutletVisible = false;
	 isPJPVisible = false;
	 isHODVisible = false;
 }
  
boolean IsPackageSelected=false;
int IsPackagesSelectedSession=0;
int IsPackageSelectedOnLoad=0;
long SelectedPackagesArray[] = null;
if (session.getAttribute("SR1SelectedPackages") != null){
	SelectedPackagesArray = (long[])session.getAttribute("SR1SelectedPackages");
	if(SelectedPackagesArray.length>0)
	{
		IsPackageSelected = true;
		IsPackageSelectedOnLoad=1;
		IsPackagesSelectedSession=1;
	}
}

//for brands

boolean IsBrandSelected=false;
int IsBrandsSelectedSession=0;
long SelectedBrandsArray[] = null;
if (session.getAttribute("SR1SelectedBrands") != null){
	SelectedBrandsArray = (long[])session.getAttribute("SR1SelectedBrands");
	if(SelectedBrandsArray.length>0)
	{
		IsBrandSelected = true;
		IsBrandsSelectedSession=1;
	}
}

//for Distributors

boolean IsDistributorSelected=false;
int IsDistributorSelectedSession=0;
long SelectedDistributorArray[] = null;
if (session.getAttribute("SR1SelectedDistributors") != null){
	SelectedDistributorArray = (long[])session.getAttribute("SR1SelectedDistributors");
	if(SelectedDistributorArray.length>0)
	{
		IsDistributorSelected = true;
		IsDistributorSelectedSession=1;
	}
}

//For Order Bookers
boolean IsOrderBookerSelected=false;
int IsOrderBookerSelectedSession=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute("SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute("SR1SelectedOrderBookers");
	if(SelectedOrderBookerArray.length>0)
	{
		IsOrderBookerSelected = true;
		IsOrderBookerSelectedSession=1;
	}
}

//For Date Range

boolean IsStartDateSelected=false;
boolean IsEndDateSelected=false;
int IsDateRangeSelectedSession=0;
Date SelectedStartDate;
Date SelectedEndDate;
String StartDate ="";
String EndDate="";
if (session.getAttribute("SR1StartDate") != null){
	SelectedStartDate = (Date)session.getAttribute("SR1StartDate");	
	StartDate = Utilities.getDisplayDateFormat(SelectedStartDate);
	if(StartDate != null && StartDate != "")
	{
		IsStartDateSelected=true;
		IsDateRangeSelectedSession=1;
	}
}
if (session.getAttribute("SR1EndDate") != null){
	SelectedEndDate = (Date)session.getAttribute("SR1EndDate");	
	EndDate = Utilities.getDisplayDateFormat(SelectedEndDate);
	if(EndDate != null && EndDate != "")
	{
		IsEndDateSelected=true;
		IsDateRangeSelectedSession=1;
	}	
}


boolean IsVehicleSelected=false;
int IsVehicleSelectedSession=0;
long SelectedVehicleArray[] = null;
if (session.getAttribute("SR1SelectedVehicles") != null){
	SelectedVehicleArray = (long[])session.getAttribute("SR1SelectedVehicles");
	if(SelectedVehicleArray.length>0)
	{
		IsVehicleSelected = true;
		IsVehicleSelectedSession=1;
	}
}

boolean IsEmployeeSelected=false;
int IsEmployeeSelectedSession=0;
long SelectedEmployeeArray[] = null;
if (session.getAttribute("SR1SelectedEmployees") != null){
	SelectedEmployeeArray = (long[])session.getAttribute("SR1SelectedEmployees");
	if(SelectedEmployeeArray.length>0)
	{
		IsEmployeeSelected = true;
		IsEmployeeSelectedSession=1;
	}
}

boolean IsOutletSelected=false;
int IsOutletSelectedSession=0;
long SelectedOutletArray[] = null;
if (session.getAttribute("SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute("SR1SelectedOutlets");
	if(SelectedOutletArray.length>0)
	{
		IsOutletSelected = true;
		IsOutletSelectedSession=1;
	}
}

boolean PJPSelected=false;
int IsPJPSelectedSession=0;
long SelectedPJPArray[] = null;
if (session.getAttribute("SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute("SR1SelectedPJP");
	if(SelectedPJPArray.length>0)
	{
		PJPSelected = true;
		IsPJPSelectedSession=1;
	}
}


//HOD
boolean HODSelected=false;
int IsHODSelectedSession=0;
long SelectedHODArray[] = null;
if (session.getAttribute("SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute("SR1SelectedHOD");
	if(SelectedHODArray.length>0)
	{
		HODSelected = true;
		IsHODSelectedSession=1;
	}
}

%>      	
        	
           <ul data-role="listview" data-inset="true"  style="font-size:10pt; margin-top:-10px; margin-left:-8px" data-icon="false">
				<input type="hidden" name="IsOrderBookerSessionSet" id="IsOrderBookerSessionSet" value="<%=IsOrderBookerSelectedSession %>"/>
				<input type="hidden" name="IsDistributorSessionSet" id="IsDistributorSessionSet" value="<%=IsDistributorSelectedSession %>"/>
				<input type="hidden" name="IsBrandSessionSet" id="IsBrandSessionSet" value="<%=IsBrandsSelectedSession %>"/>
				<input type="hidden" name="IsPackageSessionSet" id="IsPackageSessionSet" value="<%=IsPackagesSelectedSession %>"/>
				<input type="hidden" name="IsDateRangeSessionSet" id="IsDateRangeSessionSet" value="<%=IsDateRangeSelectedSession %>"/>			
				
				
				<li data-role="list-divider" data-theme="a">Filter By</li>
				<%
				if (isDistributorVisible){
				%>
				<li <%if(IsDistributorSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllDistributors('LoadAllDistributorsHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllDistributorsHyperlink"  >All Distributors</a></li>
				<%
				}
				%>
				<%
				if (isDateRangeVisible){
				%>
				<li <%if(IsStartDateSelected==true || IsEndDateSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadDateRange('LoadDateRangeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadDateRangeHyperlink"  >Date Range</a></li>			
				<%
				}
				%>
				<%
				if (isPackageVisible){
				%>
				<li <%if(IsPackageSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllPackages('LoadAllPackagesHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllPackagesHyperlink"  >All Packages</a></li>
				<%
				}
				%>
				<%
				if (isBrandVisible){
				%>
				<li <%if(IsBrandSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllBrands('LoadAllBrandsHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllBrandsHyperlink"  >All Brands</a></li>
				<%
				}
				%>
				<%
				if (isOrderBookerVisible){
				%>
				<li <%if(IsOrderBookerSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllOrderBookers('LoadAllOrderBookersHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllOrderBookersHyperlink"  >All Order Bookers</a></li>			
				<%
				}
				%>
				<%
				if (isVehicleVisible){
				%>
				<li <%if(IsVehicleSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllVehicles('LoadAllVehiclesHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllVehiclesHyperlink"  ><%if(IsVehicleSelected){%>Selected<%}else {%>All<%} %> Vehicles</a></li>
				<%
				}
				%>
				<%
				if (isEmployeeVisible){
				%>
				<li <%if(IsEmployeeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllEmployees('LoadAllEmployeesHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllEmployeesHyperlink"  ><%if(IsEmployeeSelected){%>Selected<%}else {%>All<%} %> Employees</a></li>			
				<%
				}
				%>				
				<%
				if (isOutletVisible){
				%>
				<li <%if(IsOutletSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllOutlets('LoadAllOutletsHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllOutletsHyperlink"  ><%if(IsOutletSelected){%>Selected<%}else {%>All<%} %> Outlets</a></li>			
				<%
				}				
				if (isPJPVisible){
				%>
				<li <%if(PJPSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllPJPs('LoadAllPJPHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllPJPHyperlink"  ><%if(PJPSelected){%>Selected<%}else {%>All<%} %> PJP</a></li>			
				<%
				}
				if (isHODVisible){
				%>
				<li <%if(HODSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllHODs('LoadAllHODHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllHODHyperlink"  ><%if(HODSelected){%>Selected<%}else {%>All<%} %> HOD</a></li>			
				<%
				}
				%>
								
			</ul>		
            
            
        	