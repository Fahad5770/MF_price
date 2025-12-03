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

%>      	
        	
           <ul data-role="listview" data-inset="true"  style="font-size:10pt; margin-left:10px;margin-top:10px;" data-icon="false">
				<input type="hidden" name="IsOrderBookerSessionSet" id="IsOrderBookerSessionSet" value="<%=IsOrderBookerSelectedSession %>"/>
				<input type="hidden" name="IsDistributorSessionSet" id="IsDistributorSessionSet" value="<%=IsDistributorSelectedSession %>"/>
				<input type="hidden" name="IsBrandSessionSet" id="IsBrandSessionSet" value="<%=IsBrandsSelectedSession %>"/>
				<input type="hidden" name="IsPackageSessionSet" id="IsPackageSessionSet" value="<%=IsPackagesSelectedSession %>"/>
				<input type="hidden" name="IsDateRangeSessionSet" id="IsDateRangeSessionSet" value="<%=IsDateRangeSelectedSession %>"/>			
				
				
				<li data-role="list-divider" data-theme="c">Filter By</li>
				<li <%if(IsDistributorSelected){%>data-theme="b"<%} %>><a href="javaScript:onClick=LoadAllDistributors('LoadAllDistributorsHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllDistributorsHyperlink"  >All Distributors</a></li>
				<li <%if(IsStartDateSelected==true || IsEndDateSelected==true){%>data-theme="b"<%} %>><a href="javaScript:onClick=LoadDateRange('LoadDateRangeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadDateRangeHyperlink"  >Date Range</a></li>			
				<li <%if(IsPackageSelected){%>data-theme="b"<%} %>><a href="javaScript:onClick=LoadAllPackages('LoadAllPackagesHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllPackagesHyperlink"  >All Packages</a></li>
				<li <%if(IsBrandSelected){%>data-theme="b"<%} %>><a href="javaScript:onClick=LoadAllBrands('LoadAllBrandsHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllBrandsHyperlink"  >All Brands</a></li>
				<li <%if(IsOrderBookerSelected){%>data-theme="b"<%} %>><a href="javaScript:onClick=LoadAllOrderBookers('LoadAllOrderBookersHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllOrderBookersHyperlink"  >All Order Bookers</a></li>			
				
			</ul>		
            
            
        	