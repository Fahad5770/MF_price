<%

String RegionTheme = "c";
String DistributorTheme = "c";
String OutletTheme = "c";
String PeriodTheme = "c";
String TypeTheme = "c";

if(session.getAttribute("MonthlyDiscountRegions") != null){
	RegionTheme = "e";
}

if(session.getAttribute("MonthlyDiscountDistributors") != null){
	DistributorTheme = "e";
}

if(session.getAttribute("MonthlyDiscountOutlets") != null){
	OutletTheme = "e";
}

if(session.getAttribute("MonthlyDiscountMonth") != null){
	PeriodTheme = "e";
}

if(session.getAttribute("MonthlyDiscountStatus") != null){
	TypeTheme = "e";
}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; margin-top:-10px; margin-left:-8px" data-icon="false">
	    
   	<li data-role="list-divider" data-theme="a">Filter By</li>
   	
   	<li data-theme="<%=RegionTheme%>"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadRegions()">Region</a></li>
   	<li data-theme="<%=DistributorTheme%>"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadDistributors()">Distributors</a></li>
   	<li data-theme="<%=OutletTheme%>"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadOutlets()">Outlet</a></li>
   	<li data-theme="<%=PeriodTheme%>"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadPeriod()">Period</a></li>
   	<li data-theme="<%=TypeTheme%>"><a href="#" style="font-size:10pt; font-weight:normal;" onclick="LoadStatus()">Type</a></li>
   	
   	<li data-icon="check" >
  				<a href="#" onClick="LoadMonthlyDiscountMainContent()" data-iconpos="left" data-mini="true" style="font-size:10pt">Generate Discounts</a>
  			</li>
  			<li data-icon="refresh" >
  				<a href="#" onClick="window.location='MonthlyDiscountMain.jsp'" data-iconpos="left" data-mini="true" style="font-size:10pt">Reset</a>
  			</li>
   	
</ul>