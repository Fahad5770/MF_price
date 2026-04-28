<%@page import="com.mysql.jdbc.Util"%>
<%@page import="com.pbc.util.Datasource"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>
<%@page import="com.pbc.common.Distributor"%>
<%@page import="com.pbc.util.UserAccess"%>
<%@page import="com.pbc.reports.SalesIndex"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.apache.commons.lang3.time.DateUtils"%>
<%@page import="com.pbc.reports.EmptyReconciliation"%>
<%@page import="java.util.Calendar"%>



<script>

function redirect(url){
	document.getElementById("check").action = url;
	document.getElementById("check").submit();
}

</script>

<style>
td{
font-size: 8pt;
}

</style>


<%
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));


long SessionUserID = Long.parseLong((String)session.getAttribute("UserID"));
int FeatureID = 222;

if(UserAccess.isAuthorized(FeatureID, SessionUserID, request) == false){
	response.sendRedirect("AccessDenied.jsp");
}

Datasource ds = new Datasource();
ds.createConnection();
Connection c = ds.getConnection();
Statement s = c.createStatement();
Statement s2 = c.createStatement();
Statement s3 = c.createStatement();

//Date date = Utilities.parseDate(request.getParameter("Date"));


Date StartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");
Date EndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");

if(session.getAttribute(UniqueSessionID+"_SR1StartDate") == null){
	StartDate = new Date();
}

if(session.getAttribute(UniqueSessionID+"_SR1EndDate") == null){
	EndDate = new Date();
}


//out.print("StartDate = "+StartDate);
//out.print("EndDate = "+EndDate);


long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
   	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");           	
}

String PackageIDs = "";
String WherePackage = "";

if(SelectedPackagesArray!= null && SelectedPackagesArray.length > 0){
	for(int i = 0; i < SelectedPackagesArray.length; i++){
		if(i == 0){
			PackageIDs += SelectedPackagesArray[i]+"";
		}else{
			PackageIDs += ", "+SelectedPackagesArray[i]+"";
		}
	}
	WherePackage = "package_id in ("+PackageIDs+") ";
}

//HOD


String HODIDs="";
long SelectedHODArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	HODIDs = Utilities.serializeForSQL(SelectedHODArray);
}

String WhereHOD = "";
if (HODIDs.length() > 0){
	WhereHOD = " and (snd_id in ("+HODIDs+") or rsm_id in ("+HODIDs+")) ";	
}





//RSM


String RSMIDs="";
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	RSMIDs = Utilities.serializeForSQL(SelectedRSMArray);
}

String WhereRSM = "";
if (RSMIDs.length() > 0){
	WhereRSM = " and mo.distributor_id in (SELECT distributor_id FROM common_distributors where rsm_id in ("+RSMIDs+")) ";	
}


//Distributor

long SelectedDistributorsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");           	
}else{
	Distributor UserDistributor[] = UserAccess.getUserFeatureDistributor(SessionUserID, FeatureID);
	SelectedDistributorsArray = new long[UserDistributor.length];
	
	for(int x=0;x<UserDistributor.length;x++){
		SelectedDistributorsArray[x] = UserDistributor[x].DISTRIBUTOR_ID;
	}
}

String DistributorIDs = "";
String WhereDistributors = "";
if(SelectedDistributorsArray != null && SelectedDistributorsArray.length > 0){
	for(int i = 0; i < SelectedDistributorsArray.length ; i++){
		if(i == 0){
			DistributorIDs += SelectedDistributorsArray[i];
		}else{
			DistributorIDs += ", "+SelectedDistributorsArray[i];
		}
	}
	WhereDistributors = " and mo.distributor_id in ("+DistributorIDs+") ";
	//out.print(WhereDistributors);
}

//OrderBooker

boolean IsOrderBookerSelected=false;

int OrderBookerArrayLength=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
	
	IsOrderBookerSelected=true;
	OrderBookerArrayLength=SelectedOrderBookerArray.length;
}



String OrderBookerIDs = "";
if(SelectedOrderBookerArray != null && SelectedOrderBookerArray.length > 0){
	for(int i = 0; i < SelectedOrderBookerArray.length; i++){
		if(i == 0){
			OrderBookerIDs += SelectedOrderBookerArray[i];
		}else{
			OrderBookerIDs += ", "+SelectedOrderBookerArray[i];
		}
	}
}
String OrderBookerIDsWher="";
if(OrderBookerIDs.length()>0){
	OrderBookerIDsWher =" and mo.created_by in ("+OrderBookerIDs+") ";
}


//SM


String SMIDs="";
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	SMIDs = Utilities.serializeForSQL(SelectedSMArray);
}

String WhereSM = "";
if (SMIDs.length() > 0){
	WhereSM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where sm_id in ("+SMIDs+")) ";	
}

//TDM


String TDMIDs="";
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedTDM") != null){
	SelectedTDMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedTDM");
	TDMIDs = Utilities.serializeForSQL(SelectedTDMArray);
}

String WhereTDM = "";
if (TDMIDs.length() > 0){
	WhereTDM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where tdm_id in ("+TDMIDs+")) ";	
}

//ASM


String ASMIDs="";
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedASM") != null){
	SelectedASMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedASM");
	ASMIDs = Utilities.serializeForSQL(SelectedASMArray);
}

String WhereASM = "";
if (ASMIDs.length() > 0){
	WhereASM = " and mo.distributor_id in (SELECT distributor_id FROM distributor_beat_plan where asm_id in ("+ASMIDs+")) ";	
}



long SelectedBrandsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
   	SelectedBrandsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");           	
}

String BrandIDs = "";
String WhereBrand = "";

if(SelectedBrandsArray!= null && SelectedBrandsArray.length > 0){
	for(int i = 0; i < SelectedBrandsArray.length; i++){
		if(i == 0){
			BrandIDs += SelectedBrandsArray[i]+"";
		}else{
			BrandIDs += ", "+SelectedBrandsArray[i]+"";
		}
	}
	WhereBrand = " and ip.brand_id in ("+BrandIDs+") ";
}


long DistributorID = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");
}
String WhereDistributorID ="";
if (DistributorID != 0){
	WhereDistributorID = " and bieoi.customer_id in ("+DistributorID+") ";	
}



//Empty Reason


String EmptyReasonIDs="";
String SelectedEmptyReasonArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReason") != null){
	SelectedEmptyReasonArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReason");
	EmptyReasonIDs = Utilities.serializeForSQL(SelectedEmptyReasonArray);
}

String WhereEmptyReason = "";
if (EmptyReasonIDs.length() > 0){
	WhereEmptyReason = " and bieoi.reason in("+EmptyReasonIDs+")";	
}


//Empty Reason


String MovementTypeIDs="";
String SelectedMovementTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedMovementType") != null){
	SelectedMovementTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedMovementType");
	MovementTypeIDs = Utilities.serializeForSQL(SelectedMovementTypeArray);
}

String WhereMovementType = "";
if (MovementTypeIDs.length() > 0){
	WhereMovementType = " and bieoi.movmt in("+MovementTypeIDs+")";	
}

%>

<ul data-role="listview" data-inset="true"  style="font-size:10pt; font-weight: normal; margin-top:-10px;" data-icon="false">
<li data-role="list-divider" data-theme="a">Empty Reconciliation</li>
<li>


<table style="width: 100%; margin-top:-8px" cellpadding="0" cellspacing="0">
	<tr>
		
		<td style="width: 100%" valign="top">
			<table border=0 style="font-size:13px; font-weight: 400; width:100%" cellpadding="0" cellspacing="0"  adata-role="table" class="GridWithBorder">
					 <thead>
					    <tr >
							<th data-priority="1"  style=" width:60%;" title=""></th>	 						
							<!-- <th data-priority="1"  style=" width:20%;" title="">240 ML</th> -->
							<th data-priority="1"  style=" width:20%; text-align:center;" title="">250 ML</th>
							<th data-priority="1"  style=" width:20%; text-align:center;" title="">1000 ML</th>							
							
					    </tr>
					  </thead> 
					<tbody>
						<%
						
						//Opening Balance
						
						
						
						Date OpeningStarDate=Utilities.parseDate("01/01/2015");
						Date OpeningEndDate=Utilities.getDateByDays(StartDate,-1);
						
						//System.out.println(OpeningStarDate+" - "+OpeningEndDate);
						
						EmptyReconciliation EROpening = new EmptyReconciliation();
						
						
						long GrandTotalEmpty240Opening=0;
						long GrandTotalEmpty250Opening=0;
						long GrandTotalEmpty1000Opening=0;
						
						long NewEmptyGTotalRawCases240Opening=0;
						long NewEmptyGTotalUnits240Opening=0;
						
						long OpeningBalance250=839310;
						long OpeningBalance1000=0;
						
						
						
						
						
					 	
					 	
						long NewEmptyGTotalRawCases250Opening= EROpening.getNewEmptyOpen(OpeningStarDate,OpeningEndDate,11);
					 	long NewEmptyGTotalRawCases1000Opening=EROpening.getNewEmptyOpen(OpeningStarDate,OpeningEndDate,1);
						
					 	
						long OldEmptyGTotalRawCases250Opening=EROpening.getOldEmptyPurchase(OpeningStarDate,OpeningEndDate,11);
						long OldEmptyGTotalRawCases1000Opening=EROpening.getOldEmptyPurchase(OpeningStarDate,OpeningEndDate,1);
						
						long GTotalRawCases250Opening=EROpening.getAgencyPurchase(OpeningStarDate,OpeningEndDate,11);
						long GTotalRawCases1000Opening=EROpening.getAgencyPurchase(OpeningStarDate,OpeningEndDate,1);
						
						GrandTotalEmpty250Opening = OpeningBalance250+NewEmptyGTotalRawCases250Opening+OldEmptyGTotalRawCases250Opening+GTotalRawCases250Opening;
						GrandTotalEmpty1000Opening = NewEmptyGTotalRawCases1000Opening+OldEmptyGTotalRawCases1000Opening+GTotalRawCases1000Opening;
						
						
						//Losses
						
						double LossTotal240Opening=0;
						double LossTotal250Opening=0;
						double OpeningLossTotal1000=0;
						
						long[] RawCasesUnitsSLCBR250Opening={0,0};
						long[] RawCasesUnitsSpecialBreakage250Opening={0,0};
						long[] RawCasesUnitsSpecialBurst250Opening={0,0};
						long[] RawCasesUnitsSpecialZabt250Opening={0,0};
						
						long[] RawCasesUnitsSLCBR1000Opening={0,0};
						long[] RawCasesUnitsSpecialBreakage1000Opening={0,0};
						long[] RawCasesUnitsSpecialBurst1000Opening={0,0};
						long[] RawCasesUnitsSpecialZabt1000Opening={0,0};
						
						RawCasesUnitsSLCBR250Opening = EROpening.getSLCBR(OpeningStarDate,OpeningEndDate,11);
						RawCasesUnitsSpecialBreakage250Opening = EROpening.getSpecialBreakage(OpeningStarDate,OpeningEndDate,11);
						RawCasesUnitsSpecialBurst250Opening = EROpening.getSpecialBurst(OpeningStarDate,OpeningEndDate,11);
						RawCasesUnitsSpecialZabt250Opening = EROpening.getSpecialZabt(OpeningStarDate,OpeningEndDate,11);
						
						RawCasesUnitsSLCBR1000Opening = EROpening.getSLCBR(OpeningStarDate,OpeningEndDate,1);
						RawCasesUnitsSpecialBreakage1000Opening = EROpening.getSpecialBreakage(OpeningStarDate,OpeningEndDate,1);
						RawCasesUnitsSpecialBurst1000Opening = EROpening.getSpecialBurst(OpeningStarDate,OpeningEndDate,1);
						RawCasesUnitsSpecialZabt1000Opening = EROpening.getSpecialZabt(OpeningStarDate,OpeningEndDate,1);
						
						long [] RawCasesUnitsDepotBurst250Opening={0,0};
						long [] RawCasesUnitsDepotFallenBurst250Opening={0,0};
						long [] RawCasesUnitsLifterBreakage250Opening={0,0};
						long [] RawCasesUnitsLifterBurst250Opening={0,0};
						long [] RawCasesUnitsWashBreakage250Opening={0,0};
						long [] RawCasesUnitsFallenBreakage250Opening={0,0};
						
						long [] RawCasesUnitsDepotBurst1000Opening={0,0};
						long [] RawCasesUnitsDepotFallenBurst1000Opening={0,0};
						long [] RawCasesUnitsLifterBreakage1000Opening={0,0};
						long [] RawCasesUnitsLifterBurst1000Opening={0,0};
						long [] RawCasesUnitsWashBreakage1000Opening={0,0};
						long [] RawCasesUnitsFallenBreakage1000Opening={0,0};
						
						
						RawCasesUnitsDepotBurst250Opening = EROpening.getDepotBurst(OpeningStarDate,OpeningEndDate,11);
						RawCasesUnitsDepotFallenBurst250Opening = EROpening.getDepotFallenBurst(OpeningStarDate,OpeningEndDate,11);
						RawCasesUnitsLifterBreakage250Opening = EROpening.getLifterBreakage(OpeningStarDate,OpeningEndDate,11);
						RawCasesUnitsLifterBurst250Opening = EROpening.getLifterBurst(OpeningStarDate,OpeningEndDate,11);
						RawCasesUnitsWashBreakage250Opening = EROpening.getWashBreakage(OpeningStarDate,OpeningEndDate,11);
						RawCasesUnitsFallenBreakage250Opening = EROpening.getFallenBreakage(OpeningStarDate,OpeningEndDate,11);
						
						RawCasesUnitsDepotBurst1000Opening = EROpening.getDepotBurst(OpeningStarDate,OpeningEndDate,1);
						RawCasesUnitsDepotFallenBurst1000Opening = EROpening.getDepotFallenBurst(OpeningStarDate,OpeningEndDate,1);
						RawCasesUnitsLifterBreakage1000Opening = EROpening.getLifterBreakage(OpeningStarDate,OpeningEndDate,1);
						RawCasesUnitsLifterBurst1000Opening = EROpening.getLifterBurst(OpeningStarDate,OpeningEndDate,1);
						RawCasesUnitsWashBreakage1000Opening = EROpening.getWashBreakage(OpeningStarDate,OpeningEndDate,1);
						RawCasesUnitsFallenBreakage1000Opening = EROpening.getFallenBreakage(OpeningStarDate,OpeningEndDate,1);
						
						long[] RawCasesUnitsDuringPro250Opening=EROpening.getDuringProductionShippingBreakage(OpeningStarDate,OpeningEndDate,11);
						long[] RawCasesUnitsDuringPro1000Opening=EROpening.getDuringProductionShippingBreakage(OpeningStarDate,OpeningEndDate,1);
						
						int TotalUnitsLoss1RawCases250Opening=EROpening.getDuringProductionBreakage(OpeningStarDate,OpeningEndDate,11);
						int TotalUnitsLoss1RawCases1000Opening=EROpening.getDuringProductionBreakage(OpeningStarDate,OpeningEndDate,1);
						
						long TotalLoss250Opening= RawCasesUnitsSLCBR250Opening[0]+RawCasesUnitsSpecialBreakage250Opening[0]+RawCasesUnitsSpecialBurst250Opening[0]+RawCasesUnitsSpecialZabt250Opening[0]+
										   RawCasesUnitsDepotBurst250Opening[0]+RawCasesUnitsDepotFallenBurst250Opening[0]+RawCasesUnitsLifterBreakage250Opening[0]+RawCasesUnitsLifterBurst250Opening[0]+RawCasesUnitsWashBreakage250Opening[0]+RawCasesUnitsFallenBreakage250Opening[0]+
										   RawCasesUnitsDuringPro250Opening[0]+TotalUnitsLoss1RawCases250Opening;
						
						long TotalLoss1000Opening= RawCasesUnitsSLCBR1000Opening[0]+RawCasesUnitsSpecialBreakage1000Opening[0]+RawCasesUnitsSpecialBurst1000Opening[0]+RawCasesUnitsSpecialZabt1000Opening[0]+
								   RawCasesUnitsDepotBurst1000Opening[0]+RawCasesUnitsDepotFallenBurst1000Opening[0]+RawCasesUnitsLifterBreakage1000Opening[0]+RawCasesUnitsLifterBurst1000Opening[0]+RawCasesUnitsWashBreakage1000Opening[0]+RawCasesUnitsFallenBreakage1000Opening[0]+
								   RawCasesUnitsDuringPro1000Opening[0]+TotalUnitsLoss1RawCases1000Opening;
						
						
						long RawCasesUnitsEmptyReturn250Opening=0;
						long RawCasesUnitsEmptyReturn1000Opening=0;
						
						RawCasesUnitsEmptyReturn250Opening = EROpening.getEmptyReturnToStore(OpeningStarDate,OpeningEndDate,11);						
						RawCasesUnitsEmptyReturn1000Opening = EROpening.getEmptyReturnToStore(OpeningStarDate,OpeningEndDate,1);
						
						long[] RawCasesUnitsEmptySale250Opening=EROpening.getEmptySales(OpeningStarDate,OpeningEndDate,11);
						long[] RawCasesUnitsEmptySale1000Opening=EROpening.getEmptySales(OpeningStarDate,OpeningEndDate,1);
						
						
						long EmptyTotal250Opening = TotalLoss250Opening + RawCasesUnitsEmptyReturn250Opening +RawCasesUnitsEmptySale250Opening[0];
						long EmptyTotal1000Opening = TotalLoss1000Opening + RawCasesUnitsEmptyReturn1000Opening +RawCasesUnitsEmptySale1000Opening[0];
						
						long ClosingBalance250Opening = GrandTotalEmpty250Opening - EmptyTotal250Opening;
						long ClosingBalance1000Opening = GrandTotalEmpty1000Opening - EmptyTotal1000Opening;
						
						OpeningBalance250 = ClosingBalance250Opening;
						OpeningBalance1000 = ClosingBalance1000Opening;
						
						////////////////////////////////////////////////////////
						////////////////////////////////////////////////////////
						///////////////////////////////////////////////////////
						
						
						
						
						
						EmptyReconciliation ER = new EmptyReconciliation();
						
						long GrandTotalEmpty240=0;
						long GrandTotalEmpty250=0;
						long GrandTotalEmpty1000=0;
						
						long NewEmptyGTotalRawCases240=0;
						long NewEmptyGTotalUnits240=0;
						
						
						
						
						
					 	
					 	
						long NewEmptyGTotalRawCases250= ER.getNewEmptyOpen(StartDate,EndDate,11);
					 	long NewEmptyGTotalRawCases1000=ER.getNewEmptyOpen(StartDate,EndDate,1);
						
					 	
						long OldEmptyGTotalRawCases250=ER.getOldEmptyPurchase(StartDate,EndDate,11);
						long OldEmptyGTotalRawCases1000=ER.getOldEmptyPurchase(StartDate,EndDate,1);
						
						long GTotalRawCases250=ER.getAgencyPurchase(StartDate,EndDate,11);
						long GTotalRawCases1000=ER.getAgencyPurchase(StartDate,EndDate,1);
						
						GrandTotalEmpty250 = OpeningBalance250+NewEmptyGTotalRawCases250+OldEmptyGTotalRawCases250+GTotalRawCases250;
						GrandTotalEmpty1000 = OpeningBalance1000+NewEmptyGTotalRawCases1000+OldEmptyGTotalRawCases1000+GTotalRawCases1000;
						
						
						//Losses
						
						double LossTotal240=0;
						double LossTotal250=0;
						double LossTotal1000=0;
						
						long[] RawCasesUnitsSLCBR250={0,0};
						long[] RawCasesUnitsSpecialBreakage250={0,0};
						long[] RawCasesUnitsSpecialBurst250={0,0};
						long[] RawCasesUnitsSpecialZabt250={0,0};
						
						long[] RawCasesUnitsSLCBR1000={0,0};
						long[] RawCasesUnitsSpecialBreakage1000={0,0};
						long[] RawCasesUnitsSpecialBurst1000={0,0};
						long[] RawCasesUnitsSpecialZabt1000={0,0};
						
						RawCasesUnitsSLCBR250 = ER.getSLCBR(StartDate,EndDate,11);
						RawCasesUnitsSpecialBreakage250 = ER.getSpecialBreakage(StartDate,EndDate,11);
						RawCasesUnitsSpecialBurst250 = ER.getSpecialBurst(StartDate,EndDate,11);
						RawCasesUnitsSpecialZabt250 = ER.getSpecialZabt(StartDate,EndDate,11);
						
						RawCasesUnitsSLCBR1000 = ER.getSLCBR(StartDate,EndDate,1);
						RawCasesUnitsSpecialBreakage1000 = ER.getSpecialBreakage(StartDate,EndDate,1);
						RawCasesUnitsSpecialBurst1000 = ER.getSpecialBurst(StartDate,EndDate,1);
						RawCasesUnitsSpecialZabt1000 = ER.getSpecialZabt(StartDate,EndDate,1);
						
						long [] RawCasesUnitsDepotBurst250={0,0};
						long [] RawCasesUnitsDepotFallenBurst250={0,0};
						long [] RawCasesUnitsLifterBreakage250={0,0};
						long [] RawCasesUnitsLifterBurst250={0,0};
						long [] RawCasesUnitsWashBreakage250={0,0};
						long [] RawCasesUnitsFallenBreakage250={0,0};
						
						long [] RawCasesUnitsDepotBurst1000={0,0};
						long [] RawCasesUnitsDepotFallenBurst1000={0,0};
						long [] RawCasesUnitsLifterBreakage1000={0,0};
						long [] RawCasesUnitsLifterBurst1000={0,0};
						long [] RawCasesUnitsWashBreakage1000={0,0};
						long [] RawCasesUnitsFallenBreakage1000={0,0};
						
						
						RawCasesUnitsDepotBurst250 = ER.getDepotBurst(StartDate,EndDate,11);
						RawCasesUnitsDepotFallenBurst250 = ER.getDepotFallenBurst(StartDate,EndDate,11);
						RawCasesUnitsLifterBreakage250 = ER.getLifterBreakage(StartDate,EndDate,11);
						RawCasesUnitsLifterBurst250 = ER.getLifterBurst(StartDate,EndDate,11);
						RawCasesUnitsWashBreakage250 = ER.getWashBreakage(StartDate,EndDate,11);
						RawCasesUnitsFallenBreakage250 = ER.getFallenBreakage(StartDate,EndDate,11);
						
						RawCasesUnitsDepotBurst1000 = ER.getDepotBurst(StartDate,EndDate,1);
						RawCasesUnitsDepotFallenBurst1000 = ER.getDepotFallenBurst(StartDate,EndDate,1);
						RawCasesUnitsLifterBreakage1000 = ER.getLifterBreakage(StartDate,EndDate,1);
						RawCasesUnitsLifterBurst1000 = ER.getLifterBurst(StartDate,EndDate,1);
						RawCasesUnitsWashBreakage1000 = ER.getWashBreakage(StartDate,EndDate,1);
						RawCasesUnitsFallenBreakage1000 = ER.getFallenBreakage(StartDate,EndDate,1);
						
						long[] RawCasesUnitsDuringPro250=ER.getDuringProductionShippingBreakage(StartDate,EndDate,11);
						long[] RawCasesUnitsDuringPro1000=ER.getDuringProductionShippingBreakage(StartDate,EndDate,1);
						
						int TotalUnitsLoss1RawCases250=ER.getDuringProductionBreakage(StartDate,EndDate,11);
						int TotalUnitsLoss1RawCases1000=ER.getDuringProductionBreakage(StartDate,EndDate,1);
						
						long TotalLoss250= RawCasesUnitsSLCBR250[0]+RawCasesUnitsSpecialBreakage250[0]+RawCasesUnitsSpecialBurst250[0]+RawCasesUnitsSpecialZabt250[0]+
										   RawCasesUnitsDepotBurst250[0]+RawCasesUnitsDepotFallenBurst250[0]+RawCasesUnitsLifterBreakage250[0]+RawCasesUnitsLifterBurst250[0]+RawCasesUnitsWashBreakage250[0]+RawCasesUnitsFallenBreakage250[0]+
										   RawCasesUnitsDuringPro250[0]+TotalUnitsLoss1RawCases250;
						
						long TotalLoss1000= RawCasesUnitsSLCBR1000[0]+RawCasesUnitsSpecialBreakage1000[0]+RawCasesUnitsSpecialBurst1000[0]+RawCasesUnitsSpecialZabt1000[0]+
								   RawCasesUnitsDepotBurst1000[0]+RawCasesUnitsDepotFallenBurst1000[0]+RawCasesUnitsLifterBreakage1000[0]+RawCasesUnitsLifterBurst1000[0]+RawCasesUnitsWashBreakage1000[0]+RawCasesUnitsFallenBreakage1000[0]+
								   RawCasesUnitsDuringPro1000[0]+TotalUnitsLoss1RawCases1000;
						
						
						long RawCasesUnitsEmptyReturn250=0;
						long RawCasesUnitsEmptyReturn1000=0;
						
						RawCasesUnitsEmptyReturn250 = ER.getEmptyReturnToStore(StartDate,EndDate,11);						
						RawCasesUnitsEmptyReturn1000 = ER.getEmptyReturnToStore(StartDate,EndDate,1);
						
						long[] RawCasesUnitsEmptySale250=ER.getEmptySales(StartDate,EndDate,11);
						long[] RawCasesUnitsEmptySale1000=ER.getEmptySales(StartDate,EndDate,1);
						
						
						long EmptyTotal250 = TotalLoss250 + RawCasesUnitsEmptyReturn250 +RawCasesUnitsEmptySale250[0];
						long EmptyTotal1000 = TotalLoss1000 + RawCasesUnitsEmptyReturn1000 +RawCasesUnitsEmptySale1000[0];
						
						long ClosingBalance250 = GrandTotalEmpty250 - EmptyTotal250;
						long ClosingBalance1000 = GrandTotalEmpty1000 - EmptyTotal1000;
						
						%>
						
						
						<tr>
							<th style="background-color:#dcdbdb;">Opening Balance</th>							
							<th style="text-align:right;background-color:#dcdbdb;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(OpeningBalance250) %></th>
							<th style="text-align:right;background-color:#dcdbdb;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(OpeningBalance1000) %></th>
						</tr>
						<tr>
							<td>New Empty Open</td>
							<!-- <td><%=Utilities.getDisplayCurrencyFormat(NewEmptyGTotalRawCases240) %></td> -->
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(NewEmptyGTotalRawCases250) %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(NewEmptyGTotalRawCases1000) %></td>
						</tr>
						<tr>
							<td>Old Empty Purchase</td>
							
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(OldEmptyGTotalRawCases250) %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(OldEmptyGTotalRawCases1000) %></td>
						</tr>
						<tr>
							<td>Agency Purchase</td>
							
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(GTotalRawCases250) %></td>
							<td style="text-align:right;"><%=Utilities.getDisplayCurrencyFormat(GTotalRawCases1000) %></td>
						</tr>
						<tr>
							<th style="background-color:#dcdbdb;">Total</th>							
							<th style="text-align:right;background-color:#dcdbdb;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(GrandTotalEmpty250) %></th>
							<th style="text-align:right; background-color:#dcdbdb;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(GrandTotalEmpty1000) %></th>
						</tr>
						<tr>
							<th style="background-color:#F6F6F6;" colspan=3>Losses</th>
						
							
						</tr>
								
						<tr>
							<td><b>S.L.C BR</b> <br/>Breakage during unloading</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsSLCBR250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsSLCBR250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsSLCBR1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsSLCBR1000[0]) %><%} %></td>
						</tr>	
						
						<tr>
							<td><b>Special Breakage</b><br/>Transit between factory and depot</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsSpecialBreakage250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialBreakage250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsSpecialBreakage1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialBreakage1000[0]) %><%} %></td>
						</tr>	
						
						<tr>
							<td><b>Special Burst</b><br/>Liquid burst in warehouses</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsSpecialBurst250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialBurst250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsSpecialBurst1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialBurst1000[0]) %><%} %></td>
						</tr>	
						
						<tr>
							<td><b>Special Zabt</b><br/>Breakage received at DPG4 from warehouses and direct route vehicle</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsSpecialZabt250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialZabt250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsSpecialZabt1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsSpecialZabt1000[0]) %><%} %></td>
						</tr>	
							
								
						<tr>
							<td><b>Depot Burst</b><br/>Liquid burst during Loading at DPG 4</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsDepotBurst250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsDepotBurst250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsDepotBurst1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsDepotBurst1000[0]) %><%} %></td>
						
						</tr>	
						
						<tr>
							<td><b>Depot Fallen Burst</b><br/>Liquid fallen and burst in DPG4</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsDepotFallenBurst250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsDepotFallenBurst250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsDepotFallenBurst1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsDepotFallenBurst1000[0]) %><%} %></td>
						
						</tr>	
						
						<tr>
							<td><b>Lifter Breakage</b><br/>During lifter operations at DPG4</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsLifterBreakage250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsLifterBreakage250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsLifterBreakage1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsLifterBreakage1000[0]) %><%} %></td>
						
						</tr>	
						
						<tr>
							<td><b>Lifter Burst</b><br/>Liquid burst during lifter operations</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsLifterBurst250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsLifterBurst250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsLifterBurst1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsLifterBurst1000[0]) %><%} %></td>
						
						</tr>	
						
						<tr>
							<td><b>Wash Breakage</b><br/>Druing sorting and washing</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsWashBreakage250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsWashBreakage250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsWashBreakage1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsWashBreakage1000[0]) %><%} %></td>
						
						</tr>	
						
						<tr>
							<td><b>Fallen Breakage</b><br/> Empty fallen at DPG4 or factory</td>								
							<td style="text-align:right;"><%if(RawCasesUnitsFallenBreakage250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsFallenBreakage250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsFallenBreakage1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsFallenBreakage1000[0]) %><%} %></td>
						
						</tr>	
						
						
						<tr>
							<td><b>Production Breakage</b><br/>During production at filling area</td>
							
							<td style="text-align:right;"><%if(RawCasesUnitsDuringPro250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsDuringPro250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsDuringPro1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsDuringPro1000[0]) %><%} %></td>
						</tr>
						
						
						<tr>
							<td><b>Production Breakage</b><br/>During production at conveyor area</td>
						
							<td style="text-align:right;"><%if(TotalUnitsLoss1RawCases250!=0){%><%=Utilities.getDisplayCurrencyFormat(TotalUnitsLoss1RawCases250) %><%} %></td>
							<td style="text-align:right;"><%if(TotalUnitsLoss1RawCases1000!=0){%><%=Utilities.getDisplayCurrencyFormat(TotalUnitsLoss1RawCases1000) %><%} %></td>
						</tr>
						
						
						
						
						
						
						<tr style="font-size:11px;">
							<th style="background-color:#F6F6F6;">Total Loss</th>
						
							<th style="text-align:right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalLoss250) %></th>
							<th style="text-align:right;background-color:#F6F6F6;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(TotalLoss1000) %></th>
						</tr>
						
						
						
						
						
						<tr>
							<td>Other Brands Returned to Store</td>
							
							<td style="text-align:right;"><%if(RawCasesUnitsEmptyReturn250!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsEmptyReturn250) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsEmptyReturn1000!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsEmptyReturn1000) %><%} %></td>
						</tr>
						
						
						<tr>
							<td>Empty Sale</td>
							
							<td style="text-align:right;"><%if(RawCasesUnitsEmptySale250[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsEmptySale250[0]) %><%} %></td>
							<td style="text-align:right;"><%if(RawCasesUnitsEmptySale1000[0]!=0){%><%=Utilities.getDisplayCurrencyFormat(RawCasesUnitsEmptySale1000[0]) %><%} %></td>
						</tr>
						<tr>
							<th style="background-color:#dcdbdb;">Total</th>
							
							
							<th style="text-align:right;background-color:#dcdbdb;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyTotal250) %></th>
							<th style="text-align:right;background-color:#dcdbdb;"><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(EmptyTotal1000) %></th>
						</tr>
					
						
						<tr>
							<th style="background-color:#dcdbdb;">Closing Balance</th>
							
							<th style="text-align:right;background-color:#dcdbdb;"><%if(ClosingBalance250!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(ClosingBalance250) %><%} %></th>
							<th style="text-align:right;background-color:#dcdbdb;"><%if(ClosingBalance1000!=0){%><%=Utilities.getDisplayCurrencyFormatRoundedAccounting(ClosingBalance1000) %><%} %></th>
						</tr>
						
					</tbody>
							
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