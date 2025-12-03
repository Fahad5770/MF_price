
<%@page import="com.pbc.util.Utilities"%>
<%@page import="java.util.Date"%>





<script>

</script>

  <%
//Filters

int ReportID = Utilities.parseInt(request.getParameter("ReportID"));
long UniqueSessionID = Utilities.parseLong(request.getParameter("UniqueSessionID"));




 boolean isDistributorVisible = true;
 boolean isDateRangeVisible = true;
 boolean isPackageVisible = true;
 boolean isBrandVisible = true;
 boolean isOrderBookerVisible = false;
 boolean isVehicleVisible = true;
 boolean isEmployeeVisible = true;
 boolean isOutletVisible = true;
 boolean isPJPVisible = true;
 boolean isHODVisible = true;
 boolean isRSMVisible = true;
 boolean isWarehouseVisible = true;
 boolean isOutletVisibleManual = true;
 boolean isDateRangeVisibleNew = true;
 boolean isDistributorLiftingVisible = true;
 boolean isDaysRangeVisible = true;
 boolean isOutletTypeVisible = true;
 boolean isComplaintIDVisible = true;
 boolean isComplaintTypeVisible = true;
 boolean isComplaintStatusVisible = true;
 boolean isSMVisible = true;
 boolean isTDMVisible = false;
 boolean isASMVisible = true;
 boolean isSalesTypeVisible= true;
 boolean isRegionVisible = true;
 boolean isAccountTypeVisible = true;
 boolean isCustomerIDVisible = true;
 boolean isCashInstrumentsVisible = true;
 boolean isGlEmployeeVisible = true;
 boolean isPrimaryInvoiceStatusVisible = true;
 boolean isCashInstrumentsMultipleVisible = true;
 boolean isDiscountTypeVisible = true;
 boolean isDistributorIDVisible = true;
 boolean isAssetNumberVisible = true;
 boolean isSamplingCreditSlipTypesVisible = true;
 boolean isOutletContractStatusVisible = true;
 boolean isDateVisible = true;
 boolean isEmptyReasonVisible = true;
 boolean isMovementTypeVisible = false;
 boolean isGTMCategoryVisible = false;
 boolean isEmptyLossTypeVisible = false;
 boolean isPackageEmptyVisible = false;
 boolean isBrandEmptyVisible = false;
 boolean isEmptyReceiptTypeVisible = false;
 boolean isDateTimeRangeVisible = false;
 boolean isPlantIDVisible = false;
 boolean isLiftingTypeVisible = false;
 
 
 //boolean is
 
 
 
 if (ReportID == 157){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 156){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 155){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = true;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 154){
	 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = true;
	  isEmptyReasonVisible = false;
}
 
if (ReportID == 153){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 152){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 151){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
  if (ReportID == 150){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 149){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}

 if (ReportID == 148){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = true;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 147){
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isPlantIDVisible = false;
}
 
 if (ReportID == 146){
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = true;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isPlantIDVisible = false;
}
 
  if (ReportID == 145){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
	  isEmptyReceiptTypeVisible=true;
	  isDateTimeRangeVisible = true;
}
 
 if (ReportID == 144){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = true;
	  isEmptyReasonVisible = false;
	  isPlantIDVisible = false;
}
 
 if (ReportID == 143){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isPlantIDVisible = false;
}
 
 if (ReportID == 142){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isPlantIDVisible = true;
}
 
 if (ReportID == 141){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
	  isEmptyReceiptTypeVisible=true;
	  isDateTimeRangeVisible = true;
}
 
 if (ReportID == 140){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 139){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 138){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 137){
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 136){
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 135){
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 134){ 
 	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
  }
 
 if (ReportID == 133){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = true;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = true;
	  isBrandEmptyVisible = false;
}
 
 if (ReportID == 132){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 
 if (ReportID == 131){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = true;
	  isEmptyReasonVisible = false;
} 
 
 if (ReportID == 130){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = true;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 129){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 128){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 127){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = true;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 126){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 
 if(ReportID == 125){
	 isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
 }
 
 if (ReportID == 124){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = true;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 123){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 
 if (ReportID == 122){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
 }
 
 if (ReportID == 121){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = true;
	  isEmptyReasonVisible = false;
 }
   if (ReportID == 120){ 
 	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
  }

 
 if (ReportID == 119){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
 }
 
 
 if (ReportID == 118){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = true;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = true;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
}
 
  if (ReportID == 117){ 
 	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
  }
 
  if (ReportID == 116){ 
 	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isGTMCategoryVisible = true;
	  }

 
 if (ReportID == 115){
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = true;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
}
 
 if (ReportID == 114){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
	  isDateTimeRangeVisible = true;
	  isEmptyReceiptTypeVisible=true;
}
 
 if (ReportID == 113){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
	  isEmptyReceiptTypeVisible=true;
	  isDateTimeRangeVisible = true;
}
 
 if (ReportID == 112){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = true;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
}
 
 if (ReportID == 111){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
}
  if (ReportID == 110){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
	  isEmptyReceiptTypeVisible=true;
	  isDateTimeRangeVisible = true;
}
 if (ReportID == 109){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = true;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = true;
	  isBrandEmptyVisible = false;
}
 
 if (ReportID == 108){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = false;
	  isPackageEmptyVisible = false;
	  isBrandEmptyVisible = false;
}
 
 if (ReportID == 106){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isEmptyLossTypeVisible = true;
	  isPackageEmptyVisible = true;
	  isBrandEmptyVisible = true;
}
 
 if (ReportID == 105){ 
 	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  isTDMVisible =true;
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isGTMCategoryVisible = true;
	  }
 
 if (ReportID == 104){ 
 	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isGTMCategoryVisible = true;
	  }
 
 if (ReportID == 103){ 
 	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isGTMCategoryVisible = true;
	  }
 
 if (ReportID == 102){
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isMovementTypeVisible = false;
 }
 
  if (ReportID == 101){ 
 	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = true;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  }
 
 
 if (ReportID == 100){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isMovementTypeVisible = false;
 }
 
 if (ReportID == 99){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = true;
	  isMovementTypeVisible = true;
 }
 
 if (ReportID == 98){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
 }
 
 if (ReportID == 97){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
 }
 
 
 if (ReportID == 96){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
 }
 
  if (ReportID == 95){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isEmptyReasonVisible = false;
 }
 
 
 if (ReportID == 94){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 93){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 92){ 
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
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 91){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
 }
 if (ReportID == 90){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 
 if (ReportID == 89){ 
 	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = true;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
  } 
 if (ReportID == 88){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = true;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
  }
 
 if (ReportID == 87){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = true;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
  }
 
 if (ReportID == 86){ 
 	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
  }
 
 
 if (ReportID == 85){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = true;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
  }
 if (ReportID == 84){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
 }
  if (ReportID == 83){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
  }
 
 
 if (ReportID == 82){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 81){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = true;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 80){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;	 
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 79){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = true;	
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 78){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = true;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 77){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 76){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = true;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 75){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = true;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 74){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = true;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 73){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 
 if (ReportID == 72){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = true;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 71){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = true;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 70){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = true;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 69){
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 68){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = true;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
  if (ReportID == 67){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 

 if (ReportID == 66){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = true;	
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 65){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 64){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = true;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 63){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = true;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 62){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 61){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= true;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}  
 
 if (ReportID == 60){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = true;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 59){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = true;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 58){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = true;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 57){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 56){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 55){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 54){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 53){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 52){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = true;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 51){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 
 if (ReportID == 50){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = true;
	  isComplaintStatusVisible = true;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 49){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = true;
	  isComplaintStatusVisible = true;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 48){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 47){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = true;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 46){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = true;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 45){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 44){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true	;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = true;
	  isOutletTypeVisible = true;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 43){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true	;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = true;
	  isOutletTypeVisible = true;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 42){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = true;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 41){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 40){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = true;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = true;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 39){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 38){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = true;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 37){ 
	 isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 36){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 35){ 
	 isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 34){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 32){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = true;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 if (ReportID == 31){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 30){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = true;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 29){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 28){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = true;
	  isEmployeeVisible = true;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}  
 if (ReportID == 27){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}  
 if (ReportID == 26){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}  
 if (ReportID == 25){ 
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
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = true;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}  
 if (ReportID == 24){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 23){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = false;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = true;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 22){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = false;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 21){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 20){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = true;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 19){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 18){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 
 if (ReportID == 17){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = false;
	  isRSMVisible = false;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 16){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = false;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
} 
 if (ReportID == 15){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}
 
 if (ReportID == 33){ 
	  isDistributorVisible = false;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = false;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = false;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = true;
	  
	  isASMVisible = true;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
	  isLiftingTypeVisible = true;
}

 if (ReportID == 107){ 
	  isDistributorVisible = true;
	  isDateRangeVisible = true;
	  isPackageVisible = true;
	  isBrandVisible = true;
	  isOrderBookerVisible = true;
	  isVehicleVisible = false;
	  isEmployeeVisible = false;
	  isOutletVisible = false;
	  isPJPVisible = true;
	  isHODVisible = true;
	  isRSMVisible = true;
	  isWarehouseVisible = false;
	  isOutletVisibleManual = false;
	  isDateRangeVisibleNew = false;
	  isDistributorLiftingVisible = false;
	  isDaysRangeVisible = false;
	  isOutletTypeVisible = false;
	  isComplaintIDVisible = false;
	  isComplaintTypeVisible = false;
	  isComplaintStatusVisible = false;
	  isSMVisible = false;
	  isTDMVisible = false;
	  isASMVisible = false;
	  isSalesTypeVisible= false;
	  isRegionVisible = false;
	  isAccountTypeVisible = false;
	  isCustomerIDVisible = false;
	  isCashInstrumentsVisible = false;
	  isGlEmployeeVisible = false;
	  isPrimaryInvoiceStatusVisible = false;
	  isCashInstrumentsMultipleVisible = false;
	  isDiscountTypeVisible = false;
	  isDistributorIDVisible = false;
	  isAssetNumberVisible = false;
	  isSamplingCreditSlipTypesVisible = false;
	  isOutletContractStatusVisible = false;
	  isDateVisible = false;
	  isEmptyReasonVisible = false;
}

 
/* Turn off for all*/
isSMVisible = false;
//isTDMVisible = false;
isASMVisible = false;
/* */
  
boolean IsPackageSelected=false;
int IsPackagesSelectedSession=0;
int IsPackageSelectedOnLoad=0;
long SelectedPackagesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
	SelectedPackagesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");
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
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
	SelectedBrandsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");
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
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");
	if(SelectedDistributorArray.length>0)
	{
		IsDistributorSelected = true;
		IsDistributorSelectedSession=1;
	}
}

//for Distributors Lifting

boolean IsDistributorLiftingSelected=false;
int IsDistributorLiftingSelectedSession=0;
long SelectedDistributorLiftingArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors") != null){
	SelectedDistributorLiftingArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedDistributors");
	if(SelectedDistributorLiftingArray.length>0)
	{
		IsDistributorLiftingSelected = true;
		IsDistributorLiftingSelectedSession=1;
	}
}


//For Order Bookers
boolean IsOrderBookerSelected=false;
int IsOrderBookerSelectedSession=0;
long SelectedOrderBookerArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers") != null){
	SelectedOrderBookerArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOrderBookers");
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
if (session.getAttribute(UniqueSessionID+"_SR1StartDate") != null){
	SelectedStartDate = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDate");	
	StartDate = Utilities.getDisplayDateFormat(SelectedStartDate);
	if(StartDate != null && StartDate != "")
	{
		IsStartDateSelected=true;
		IsDateRangeSelectedSession=1;
	}
}
if (session.getAttribute(UniqueSessionID+"_SR1EndDate") != null){
	SelectedEndDate = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");	
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
if (session.getAttribute(UniqueSessionID+"_SR1SelectedVehicles") != null){
	SelectedVehicleArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedVehicles");
	if(SelectedVehicleArray.length>0)
	{
		IsVehicleSelected = true;
		IsVehicleSelectedSession=1;
	}
}

boolean IsEmployeeSelected=false;
int IsEmployeeSelectedSession=0;
long SelectedEmployeeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmployees") != null){
	SelectedEmployeeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmployees");
	if(SelectedEmployeeArray.length>0)
	{
		IsEmployeeSelected = true;
		IsEmployeeSelectedSession=1;
	}
}

boolean IsOutletSelected=false;
int IsOutletSelectedSession=0;
long SelectedOutletArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	SelectedOutletArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	if(SelectedOutletArray.length>0)
	{
		IsOutletSelected = true;
		IsOutletSelectedSession=1;
	}
}

boolean PJPSelected=false;
int IsPJPSelectedSession=0;
long SelectedPJPArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPJP") != null){
	SelectedPJPArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPJP");
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
if (session.getAttribute(UniqueSessionID+"_SR1SelectedHOD") != null){
	SelectedHODArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedHOD");
	if(SelectedHODArray.length>0)
	{
		HODSelected = true;
		IsHODSelectedSession=1;
	}
}



//RSM
boolean RSMSelected=false;
int IsRSMSelectedSession=0;
long SelectedRSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRSM") != null){
	SelectedRSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRSM");
	if(SelectedRSMArray.length>0)
	{
		RSMSelected = true;
		IsRSMSelectedSession=1;
	}
}

//warehouse

boolean IsWarehouseSelected=false;
int IsWarehouseSelectedSession=0;
long SelectedWarehouseArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse") != null){
	SelectedWarehouseArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedWarehouse");
	if(SelectedWarehouseArray.length>0)
	{
		IsWarehouseSelected = true;
		IsWarehouseSelectedSession=1;
	}
}

//outlet 1
boolean IsOutletSelectedManual=false;
int IsOutletSelectedSessionManual=0;
long SelectedOutletArrayManual[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets") != null){
	SelectedOutletArrayManual = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutlets");
	if(SelectedOutletArrayManual.length>0)
	{
		IsOutletSelectedManual = true;
		IsOutletSelectedSessionManual=1;
	}
}

//Outlet Type
boolean OutletTypeSelected=false;
int IsOutletTypeSelectedSession=0;
String SelectedOutletTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType") != null){
	SelectedOutletTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutletType");
	if(SelectedOutletTypeArray.length>0)
	{
		OutletTypeSelected = true;
		IsOutletTypeSelectedSession=1;
	}
}

long ComplaintID=0;
boolean IsComplaintIDSelected=false;

if (session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintID") != null){
	ComplaintID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintID");	
	
	if(ComplaintID != 0)
	{
		IsComplaintIDSelected=true;		
	}
}

long ComplaintType=0;
boolean IsComplaintTypeSelected=false;

if (session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintType") != null){
	ComplaintType = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintType");	
	
	if(ComplaintType != 0)
	{
		IsComplaintTypeSelected=true;		
	}
}

//Outlet Type
boolean ComplaintStatusSelected=false;
int IsComplaintStatusSelectedSession=0;
String SelectedComplaintStatusArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintStatus") != null){
	SelectedComplaintStatusArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedComplaintStatus");
	if(SelectedComplaintStatusArray.length>0)
	{
		ComplaintStatusSelected = true;
		IsComplaintStatusSelectedSession=1;
	}
}

//SM
boolean SMSelected=false;
int IsSMSelectedSession=0;
long SelectedSMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSM") != null){
	SelectedSMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSM");
	if(SelectedSMArray.length>0)
	{
		SMSelected = true;
		IsSMSelectedSession=1;
	}
}

//TDM
boolean TDMSelected=false;
int IsTDMSelectedSession=0;
long SelectedTDMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedTDM") != null){
	SelectedTDMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedTDM");
	if(SelectedTDMArray.length>0)
	{
		TDMSelected = true;
		IsTDMSelectedSession=1;
	}
}

//ASM
boolean ASMSelected=false;
int IsASMSelectedSession=0;
long SelectedASMArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedASM") != null){
	SelectedASMArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedASM");
	if(SelectedASMArray.length>0)
	{
		ASMSelected = true;
		IsASMSelectedSession=1;
	}
}


//Sales Type

boolean SalesTypeSelected=false;
int IsSalesTypeSelectedSession=0;
long SelectedSalesTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSalesType") != null){
	SelectedSalesTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSalesType");
	if(SelectedSalesTypeArray.length>0)
	{
		SalesTypeSelected = true;
		IsSalesTypeSelectedSession=1;
	}
}


//Region Filter


boolean RegionSelected=false;
int IsRegionSelectedSession=0;
long SelectedRegionArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedRegion") != null){
	SelectedRegionArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedRegion");
	if(SelectedRegionArray.length>0)
	{
		RegionSelected = true;
		IsRegionSelectedSession=1;
	}
}

//Account Type

boolean AccountTypeSelected=false;
int IsAccountTypeSelectedSession=0;
long SelectedAccountTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType") != null){
	SelectedAccountTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedAccountType");
	if(SelectedAccountTypeArray.length>0)
	{
		AccountTypeSelected = true;
		IsAccountTypeSelectedSession=1;
	}
}

//customer id
long CustomerID=0;
boolean IsCustomerIDSelected=false;

if (session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID") != null){
	CustomerID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedCustomerID");	
	
	if(CustomerID != 0)
	{
		IsCustomerIDSelected=true;		
	}
}



//Cash Instruments Filter


boolean CashInstrumentsSelected=false;
int IsCashInstrumentsSelectedSession=0;
long SelectedCashInstrumentsArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments") != null){
	SelectedCashInstrumentsArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstruments");
	if(SelectedCashInstrumentsArray.length>0)
	{
		CashInstrumentsSelected = true;
		IsCashInstrumentsSelectedSession=1;
	}
}


//Gl Employee

boolean GlEmployeeSelected=false;
int IsGlEmployeeSelectedSession=0;
long SelectedGlEmployeeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedGlEmployee") != null){
	SelectedGlEmployeeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGlEmployee");
	if(SelectedGlEmployeeArray.length>0)
	{
		GlEmployeeSelected = true;
		IsGlEmployeeSelectedSession=1;
	}
}


//Primary Invoice Status
boolean PrimaryInvoiceStatusSelected=false;
int IsPrimaryInvoiceStatusSelectedSession=0;
String SelectedPrimaryInvoiceStatusArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPrimaryInvoiceStatus") != null){
	SelectedPrimaryInvoiceStatusArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedPrimaryInvoiceStatus");
	if(SelectedPrimaryInvoiceStatusArray.length>0)
	{
		PrimaryInvoiceStatusSelected = true;
		IsPrimaryInvoiceStatusSelectedSession=1;
	}
}


//Cash MultipleInstruments Filter


boolean CashInstrumentsMultipleSelected=false;
int IsCashInstrumentsMultipleSelectedSession=0;
long SelectedCashInstrumentsMultipleArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstrumentsMultiple") != null){
	SelectedCashInstrumentsMultipleArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedCashInstrumentsMultiple");
	if(SelectedCashInstrumentsMultipleArray.length>0)
	{
		CashInstrumentsMultipleSelected = true;
		IsCashInstrumentsMultipleSelectedSession=1;
	}
}


//Discount Type
boolean DiscountTypeSelected=false;
int IsDiscountTypeSelectedSession=0;
String SelectedDiscountTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedDiscountType") != null){
	SelectedDiscountTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedDiscountType");
	if(SelectedDiscountTypeArray.length>0)
	{
		DiscountTypeSelected = true;
		IsDiscountTypeSelectedSession=1;
	}
}

//Distributor ID
long DistributorID=0;
boolean IsDistributorIDSelected=false;

if (session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID") != null){
	DistributorID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedDistributorID");	
	
	if(DistributorID != 0)
	{
		IsDistributorIDSelected=true;		
	}
}


//Asset Number
boolean IsAssetNumberSelected=false;

long SelectedAssetNumber = 0;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedAssetNumber") != null){
	SelectedAssetNumber = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedAssetNumber");
	if(SelectedAssetNumber!=0)
	{
		IsAssetNumberSelected = true;
		
	}
}

//Sampling Credit Slip Types
boolean SamplingCreditSlipTypesSelected=false;
int IsSamplingCreditSlipTypesSelectedSession=0;
long SelectedSamplingCreditSlipTypesArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedSamplingCreditSlipTypes") != null){
	SelectedSamplingCreditSlipTypesArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedSamplingCreditSlipTypes");
	if(SelectedSamplingCreditSlipTypesArray.length>0)
	{
		SamplingCreditSlipTypesSelected = true;
		IsSamplingCreditSlipTypesSelectedSession=1;
	}
}


//Outlet Contract Status
boolean OutletContractStatusSelected=false;
int IsOutletContractStatusSelectedSession=0;
String SelectedOutletContractStatusArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedOutletContractStatus") != null){
	SelectedOutletContractStatusArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedOutletContractStatus");
	if(SelectedOutletContractStatusArray.length>0)
	{
		OutletContractStatusSelected = true;
		IsOutletContractStatusSelectedSession=1;
	}
}


//For Date Range

boolean IsEndDateSelected1=false;

int IsDateSelectedSession1=0;
Date SelectedStartDate1;
Date SelectedEndDate1;
String StartDate1 ="";
String EndDate1="";

if (session.getAttribute(UniqueSessionID+"_SR1EndDate") != null){
	SelectedEndDate1 = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDate");	
	EndDate1 = Utilities.getDisplayDateFormat(SelectedEndDate1);
	if(EndDate1 != null && EndDate1 != "")
	{
		IsEndDateSelected1=true;
		IsDateSelectedSession1=1;
	}	
}


//Empty Reason
boolean EmptyReasonSelected=false;
int IsEmptyReasonSelectedSession=0;
String SelectedEmptyReasonArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReason") != null){
	SelectedEmptyReasonArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReason");
	if(SelectedEmptyReasonArray.length>0)
	{
		EmptyReasonSelected = true;
		IsEmptyReasonSelectedSession=1;
	}
}


//Movement Type
boolean MovementTypeSelected=false;
int IsMovementTypeSelectedSession=0;
String SelectedMovementTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedMovementType") != null){
	SelectedMovementTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedMovementType");
	if(SelectedMovementTypeArray.length>0)
	{
		MovementTypeSelected = true;
		IsMovementTypeSelectedSession=1;
	}
}


//GTM Category
boolean GTMCategorySelected=false;
int IsGTMCategorySelectedSession=0;
long SelectedGTMCategoryArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory") != null){
	SelectedGTMCategoryArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedGTMCategory");
	if(SelectedGTMCategoryArray.length>0)
	{
		GTMCategorySelected = true;
		IsGTMCategorySelectedSession=1;
	}
}


//Empty Loss Types

boolean EmptyLossTypeSelected=false;
int IsEmptyLossTypeSelectedSession=0;
long SelectedEmptyLossTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyLossType") != null){
	SelectedEmptyLossTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyLossType");
	if(SelectedEmptyLossTypeArray.length>0)
	{
		EmptyLossTypeSelected = true;
		IsEmptyLossTypeSelectedSession=1;
	}
}


//for packageEmpty
boolean IsPackageEmptySelected=false;
int IsPackagesEmptySelectedSession=0;
int IsPackageEmptySelectedOnLoad=0;
long SelectedPackagesEmptyArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedPackages") != null){
	SelectedPackagesEmptyArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedPackages");
	if(SelectedPackagesEmptyArray.length>0)
	{
		IsPackageEmptySelected = true;
		IsPackageEmptySelectedOnLoad=1;
		IsPackagesEmptySelectedSession=1;
	}
}

//for brands

boolean IsBrandEmptySelected=false;
int IsBrandsEmptySelectedSession=0;
long SelectedBrandsEmptyArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedBrands") != null){
	SelectedBrandsEmptyArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedBrands");
	if(SelectedBrandsEmptyArray.length>0)
	{
		IsBrandEmptySelected = true;
		IsBrandsEmptySelectedSession=1;
	}
}



//Empty Receipt type


boolean EmptyReceiptTypeSelected=false;
int IsEmptyReceiptTypeSelectedSession=0;
long SelectedEmptyReceiptTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReceiptType") != null){
	SelectedEmptyReceiptTypeArray = (long[])session.getAttribute(UniqueSessionID+"_SR1SelectedEmptyReceiptType");
	if(SelectedEmptyReceiptTypeArray.length>0)
	{
		EmptyReceiptTypeSelected = true;
		IsEmptyReceiptTypeSelectedSession=1;
	}
}


//For DateTime Range

boolean IsStartDateTimeSelected=false;
boolean IsEndDateTimeSelected=false;
int IsDateTimeRangeSelectedSession=0;
Date SelectedStartDateTime;
Date SelectedEndDateTime;
String StartDateTime ="";
String EndDateTime="";

if (session.getAttribute(UniqueSessionID+"_SR1StartDateTime") != null){
	SelectedStartDateTime = (Date)session.getAttribute(UniqueSessionID+"_SR1StartDateTime");
	StartDateTime = Utilities.getDisplayDateFormat(SelectedStartDateTime);
	if(StartDateTime != null && StartDateTime != "")
	{
		IsStartDateTimeSelected=true;
		IsDateTimeRangeSelectedSession=1;
	}
}
if (session.getAttribute(UniqueSessionID+"_SR1EndDateTime") != null){
	SelectedEndDateTime = (Date)session.getAttribute(UniqueSessionID+"_SR1EndDateTime");	
	EndDateTime = Utilities.getDisplayDateFormat(SelectedEndDateTime);
	if(EndDateTime != null && EndDateTime != "")
	{
		IsEndDateTimeSelected=true;
		IsDateTimeRangeSelectedSession=1;
	}	
}


//Plant ID
long PlantID=0;
boolean IsPlantIDSelected=false;

if (session.getAttribute(UniqueSessionID+"_SR1SelectedPlantID") != null){
	PlantID = (Long)session.getAttribute(UniqueSessionID+"_SR1SelectedPlantID");	
	
	if(PlantID != 0)
	{
		IsPlantIDSelected=true;		
	}
}

//Lifting Type
boolean LiftingTypeSelected=false;
int IsLiftingTypeSelectedSession=0;
String SelectedLiftingTypeArray[] = null;
if (session.getAttribute(UniqueSessionID+"_SR1SelectedLiftingType") != null){
	SelectedLiftingTypeArray = (String[])session.getAttribute(UniqueSessionID+"_SR1SelectedLiftingType");
	if(SelectedLiftingTypeArray.length>0)
	{
		LiftingTypeSelected = true;
		IsLiftingTypeSelectedSession=1;
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
				if (isDateTimeRangeVisible){
				%>
				<li <%if(IsStartDateTimeSelected==true || IsEndDateTimeSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadDateTimeRange('LoadDateTimeRangeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadDateTimeRangeHyperlink"  >Date Range</a></li>			
				<%
				}
				%>
				<%
				if (isDateVisible){
				%>
				<li <%if(IsEndDateSelected1==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadDate('LoadDateRangeHyperlink1')" style="font-size:10pt; font-weight:normal;" id="LoadDateRangeHyperlink1"  >Date</a></li>			
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
				<li <%if(PJPSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllPJPs('LoadAllPJPHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllPJPHyperlink"  ><%if(PJPSelected){%>Selected<%}else {%>All<%} %> PJPs</a></li>			
				<%
				}
				
				if (isHODVisible){
				%>
				<li <%if(HODSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllHODs('LoadAllHODHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllHODHyperlink"  ><%if(HODSelected){%>Selected<%}else {%>All<%} %> SD Heads</a></li>			
				<%
				}
				if (isRSMVisible){
				%>
				<li <%if(RSMSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllRSM('LoadAllRSMHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllHODHyperlink"  ><%if(RSMSelected){%>Selected<%}else {%>All<%} %> RSMs</a></li>			
				<%
				}
				%>
				<%
				if (isWarehouseVisible){
				%>
				<li <%if(IsWarehouseSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllWarehouse('LoadAllWarehouseHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllWarehouseHyperlink"  >All Warehouse</a></li>
				<%
				}
				%>
				<%
				if (isOutletVisibleManual){
				%>
				<li <%if(IsOutletSelectedManual){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllOutletsManual('LoadAllOutletsHyperlink1')" style="font-size:10pt; font-weight:normal;" id="LoadAllOutletsHyperlink"  ><%if(IsOutletSelectedManual){%>Selected<%}else {%>All<%} %> Outlets</a></li>			
				<%
				}	
				%>
				<%
				if (isDateRangeVisibleNew){
				%>
				<li <%if(IsStartDateSelected==true || IsEndDateSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadDateRangeNew('LoadDateRangeHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadDateRangeHyperlinkNew"  >Date Range</a></li>			
				<%
				}
				%>
				<%
				if (isDistributorLiftingVisible){
				%>
				<li <%if(IsDistributorLiftingSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllDistributorsLifting('LoadAllDistributorsLiftingHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllDistributorsLiftingHyperlink"  >All Distributors</a></li>
				<%
				}
				%>				
				<%
				if (isDaysRangeVisible){
				%>
				<li <%if(IsStartDateSelected==true || IsEndDateSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadDaysRange('LoadDaysRangeHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadDateRangeHyperlinkNew"  >Days Range</a></li>			
				<%
				}
				%>
				<%
				if (isOutletTypeVisible){
				%>
				<li <%if(OutletTypeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllOutletType('LoadAllOutletTypeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllHODHyperlink"  ><%if(OutletTypeSelected){%>Selected<%}else {%>All<%} %> Outlet Types</a></li>			
				<%
				}
				%>
				<%
				if (isComplaintIDVisible){
				%>
				<li <%if(IsComplaintIDSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadComplaintID('LoadComplaintIDHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadComplaintIDHyperlinkNew"  >Complaint ID</a></li>			
				<%
				}
				%>
				<%
				if (isComplaintTypeVisible){
				%>
				<li <%if(IsComplaintTypeSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadComplaintType('LoadComplaintTypeHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadComplaintTypeHyperlinkNew"  >Complaint Type</a></li>			
				<%
				}
				%>
				<%
				if (isComplaintStatusVisible){
				%>
				<li <%if(ComplaintStatusSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadComplaintStatus('LoadComplaintStatusHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadComplaintStatusHyperlinkNew"  >Complaint Status</a></li>			
				<%
				}
				%>
				<%
				if (isSMVisible){
				%>
				<li <%if(SMSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllSM('LoadAllSMHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllSMHyperlink"  ><%if(SMSelected){%>Selected<%}else {%>All<%} %> SMs</a></li>			
				<%
				}
				%>
				<%
				if (isTDMVisible){
				%>
				<li <%if(TDMSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllTDM('LoadAllTDMHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllTDMHyperlink"  ><%if(TDMSelected){%>Selected<%}else {%>All<%} %> TDMs</a></li>			
				<%
				}
				%>
				<%
				if (isASMVisible){
				%>
				<li <%if(ASMSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllASM('LoadAllASMHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllASMHyperlink"  ><%if(ASMSelected){%>Selected<%}else {%>All<%} %> ASMs</a></li>			
				<%
				}
				if (isSalesTypeVisible){
				%>
				<li <%if(SalesTypeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllSalesType('LoadAllSalesTypeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllSalesTypeHyperlink"  ><%if(SalesTypeSelected){%>Selected<%}else {%>All<%} %> Sales Type</a></li>			
				<%
				}
				%>
				<%
				if (isRegionVisible){
				%>
				<li <%if(RegionSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllRegion('LoadAllRegionHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllRegionHyperlink"  ><%if(RegionSelected){%>Selected<%}else {%>All<%} %> Regions</a></li>			
				<%
				}
				%>
				<%
				if (isAccountTypeVisible){
				%>
				<li <%if(AccountTypeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllAccountType('LoadAllAccountTypeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllAccountTypeHyperlink"  ><%if(AccountTypeSelected){%>Selected<%}%> Account Type</a></li>			
				<%
				}
				%>
				<%
				if (isCustomerIDVisible){
				%>
				<li <%if(IsCustomerIDSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadCustomerID('LoadCustomerIDHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadCustomerIDHyperlinkNew"  >Customer</a></li>			
				<%
				}
				%>
				<%
				if (isCashInstrumentsVisible){
				%>
				<li <%if(CashInstrumentsSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadCashInstruments('LoadTransactionAccountHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadTransactionAccountHyperlinkNew"  >Cash Instruments</a></li>			
				<%
				}
				%>
				<%
				if (isGlEmployeeVisible){
				%>				
				<li <%if(GlEmployeeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllGlEmployees('LoadAllGlEmployeeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllHODHyperlink"  ><%if(GlEmployeeSelected){%>Selected<%}else {%><%} %> Cash User</a></li>			
				<%
				}				
				if (isPrimaryInvoiceStatusVisible){
				%>
				<li <%if(PrimaryInvoiceStatusSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllPrimaryInvoiceStatus('LoadAllPrimaryInvoiceStatusHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllPrimaryInvoiceStatusHyperlink"  ><%if(PrimaryInvoiceStatusSelected){%>Selected<%}else {%>All<%} %> Primary Invoice Status</a></li>			
				<%
				}
				
				%>
				<%
				if (isCashInstrumentsMultipleVisible){
				%>
				<li <%if(CashInstrumentsMultipleSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadCashInstrumentsMultiple('LoadCashInstrumentsMultipleHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadTransactionAccountHyperlinkNew"  >Cash Instruments Multiple</a></li>			
				<%
				}
				%>
				<%
				if (isDiscountTypeVisible){
				%>
				<li <%if(DiscountTypeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllDiscountType('LoadAllDiscountTypeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllHODHyperlink"  ><%if(DiscountTypeSelected){%>Selected<%}else {%>All<%} %> Discount Types</a></li>			
				<%
				}
				%>
				<%
				if (isDistributorIDVisible){
				%>
				<li <%if(IsDistributorIDSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadDistributorID('LoadDistributorIDHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadDistributorIDHyperlinkNew"  >Distributor</a></li>			
				<%
				}
				%>
				<%
				if (isAssetNumberVisible){
				%>
				<li <%if(IsAssetNumberSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllAssetNumber('LoadAllAssetNumberHyperlink1')" style="font-size:10pt; font-weight:normal;" id="LoadAllAssetNumberHyperlink"  ><%if(IsAssetNumberSelected){%>Selected<%}else {%>All<%} %> Asset Number</a></li>			
				<%
				}	
				%>
				<%
				if (isSamplingCreditSlipTypesVisible){
				%>
				<li <%if(SamplingCreditSlipTypesSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllSamplingCreditSlipTypes('LoadAllSamplingCreditSlipTypesHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllSamplingCreditSlipTypesHyperlink"  ><%if(SamplingCreditSlipTypesSelected){%>Selected<%}else {%>All<%} %> Credit Slip Types</a></li>			
				<%
				}
				 %>
				 <%
				if (isOutletContractStatusVisible){
				%>
				<li <%if(OutletContractStatusSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadOutletContractStatus('LoadOutletContractStatusHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="OutletContractStatusHyperlinkNew"  >Outlet Contract Status</a></li>			
				<%
				}
				%>
				 <%
				 if (isEmptyReasonVisible){
				%>
				<li <%if(EmptyReasonSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllEmptyReasons('LoadAllEmptyReasonHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllEmptyReasonHyperlink"  ><%if(EmptyReasonSelected){%>Selected<%}else {%><%} %> Empty Transaction Types</a></li>			
				<%
				}
				%>
				 <%
				 if (isMovementTypeVisible){
				%>
				<li <%if(MovementTypeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllMovementTypes('LoadAllMovementTypeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllMovementTypeHyperlink"  ><%if(MovementTypeSelected){%>Selected<%}else {%><%} %> Movement Type</a></li>			
				<%
				}
				%>
				<%
				 if (isGTMCategoryVisible){
				%>
				<li <%if(GTMCategorySelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllGTMCategorys('LoadAllGTMCategoryHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllGTMCategoryHyperlink"  ><%if(GTMCategorySelected){%>Selected<%}else {%>All<%} %> GTM Categories</a></li>			
				<%
				}
				 %>
				<% 				 
				if (isEmptyLossTypeVisible){
				%>
				<li <%if(EmptyLossTypeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllEmptyLossType('LoadAllEmptyLossTypeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllHODHyperlink"  ><%if(EmptyLossTypeSelected){%>Selected<%}else {%>All<%} %> Glass Types</a></li>			
				<%
				}
				%> 
				<%
				if (isPackageEmptyVisible){
				%>
				<li <%if(IsPackageEmptySelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllPackagesEmpty('LoadAllPackagesEmptyHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllPackagesEmptyHyperlink"  >All Packages</a></li>
				<%
				}
				%>
				<%
				if (isBrandEmptyVisible){
				%>
				<li <%if(IsBrandEmptySelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllBrandsEmpty('LoadAllBrandsEmptyHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllBrandsEmptyHyperlink"  >All Brands</a></li>
				<%
				}
				%>
				 <%
				if (isEmptyReceiptTypeVisible){
				%>
				<li <%if(EmptyReceiptTypeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllEmptyReceiptType('LoadAllEmptyReceiptTypeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllEmptyReceiptTypeHyperlink"  ><%if(EmptyReceiptTypeSelected){%>All<%}else {%><%} %> Receipt Types</a></li>			
				<%
				}
				 %>
				 <%
				if (isPlantIDVisible){
				%>
				<li <%if(IsPlantIDSelected==true){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadPlantID('LoadPlantIDHyperlinkNew')" style="font-size:10pt; font-weight:normal;" id="LoadPlantIDHyperlinkNew"  >Plant</a></li>			
				<%
				}
				%>
				<%
				if (isLiftingTypeVisible){
				%>
				<li <%if(LiftingTypeSelected){%>data-theme="e"<%} %>><a href="javaScript:onClick=LoadAllLiftingType('LoadAllLiftingTypeHyperlink')" style="font-size:10pt; font-weight:normal;" id="LoadAllHODHyperlink"  ><%if(LiftingTypeSelected){%>Selected<%}else {%>All<%} %> Lifting Type</a></li>			
				<%
				}
				%>
				<%
				if(ReportID==149){ // for this report we have to open new page
					%>
				<li data-theme="c" data-icon="bars"><a href="ReportCenterR229B.jsp?UniqueSessionID=<%=UniqueSessionID%>" style="font-size:10pt; font-weight:normal;" id=""  target="_blank"> Generate Report</a></li>
					
				<%	
				}
				else if(ReportID==150){ // for this report we have to open new page
					%>
				<li data-theme="c" data-icon="bars"><a href="ReportCenterR230B.jsp?UniqueSessionID=<%=UniqueSessionID%>" style="font-size:10pt; font-weight:normal;" id=""  target="_blank"> Generate Report</a></li>
					
				<%
				}
				else{ //normal case
				%>
					
				<li data-theme="c" data-icon="bars"><a href="javaScript:onClick=ApplyAllFiltersAtOnce()" style="font-size:10pt; font-weight:normal;" id=""  > Generate Report</a></li>	
				<%	
				}
				
				%>
				
				
				
			</ul>		
            
            
        	