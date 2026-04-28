package com.pbc.sap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pbc.sap.CustomDestinationDataProvider.MyDestinationDataProvider;
import com.pbc.util.Datasource;
import com.sap.conn.jco.AbapException;
import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoField;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoFunctionTemplate;
import com.sap.conn.jco.JCoListMetaData;
import com.sap.conn.jco.JCoParameterField;
import com.sap.conn.jco.JCoParameterFieldIterator;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import com.sap.conn.jco.JCoTable;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.pbc.util.Utilities;


public class SAPUtilities
{
	String ABAP_AS = "ABAP_AS_PRD";
	private MyDestinationDataProvider myProvider;
	
    static Properties getDestinationPropertiesDEV()
    {
        //adapt parameters in order to configure a valid destination
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "155.135.0.15");
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "210");
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "ahaseeb");
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "h@abc55");
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");
        return connectProperties;
    }
    static Properties getDestinationPropertiesPRD()
    {
        //adapt parameters in order to configure a valid destination
        Properties connectProperties = new Properties();
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, "155.135.0.19");
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  "00");
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, "200");
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   "ahaseeb");
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, "h@abc55");
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   "en");
        return connectProperties;
    }

    public void connectDEV(){
        myProvider = new MyDestinationDataProvider();
        try{
            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
        }catch(IllegalStateException providerAlreadyRegisteredException){
            throw new Error(providerAlreadyRegisteredException);
        }
        String destName = "ABAP_AS_DEV";
        CustomDestinationDataProvider test = new CustomDestinationDataProvider();
        
        //set properties for the destination and ...
        myProvider.changeProperties(destName, getDestinationPropertiesDEV());
        
        this.ABAP_AS = "ABAP_AS_DEV";
    }
    public void connectPRD(){
    	//System.out.println("a");
    	
    	
    	if (!com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered()){
    		//System.out.println("b");
	        myProvider = new MyDestinationDataProvider();
	        try{
	            com.sap.conn.jco.ext.Environment.registerDestinationDataProvider(myProvider);
	        }catch(IllegalStateException providerAlreadyRegisteredException){
	            //somebody else registered its implementation, 
	            //stop the execution
	            throw new Error(providerAlreadyRegisteredException);
	        }
	        String destName = "ABAP_AS_PRD";
	        CustomDestinationDataProvider test = new CustomDestinationDataProvider();
	        
	        //set properties for the destination and ...
	        myProvider.changeProperties(destName, getDestinationPropertiesPRD());
    	}
    	//System.out.println("c");
    }
    
    public SalarySlip[] getPayrollList(int UserID) throws JCoException {
    	
        

        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
        JCoFunction function = destination.getRepository().getFunction("BAPI_GET_PAYROLL_RESULT_LIST");
        function.getImportParameterList().setValue("EMPLOYEENUMBER", UserID);
        function.getImportParameterList().setValue("FROMDATE", "18000101");
        function.getImportParameterList().setValue("TODATE", "99991231");
        
        //int c = function.getImportParameterList().getFieldCount();
        
        if(function == null){
            throw new RuntimeException("Function not found");
        }
        
        try{
            function.execute(destination);
        }catch(AbapException e){
            System.out.println(e.toString());
            //return;
        }
        
        /*
        JCoStructure returnStructure = function.getExportParameterList().getStructure(0);
        System.out.println("Return Structure:"+ returnStructure.getString(0));
        
        
        
        if (! (returnStructure.getString(0).equals("")||returnStructure.getString("TYPE").equals("S"))  )   
        {
           throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        */
        JCoTable codes = function.getTableParameterList().getTable(0);
        
        
        int columns = codes.getNumColumns();
        int rows = codes.getNumRows();
        
        //System.out.println(rows + " " + columns);
        
        //System.out.print(codes.toXML());
        
        //codes.lastRow();
        //System.out.println(codes.getString(0));
        
        
        
        List<SalarySlip> list = new ArrayList<SalarySlip>();
        
        codes.firstRow();
        for (int i = 0; i < rows; i++){
        	
        	SalarySlip item = new SalarySlip();
        	
        	item.FROM_DATE = codes.getDate(2);
        	item.TO_DATE = codes.getDate(3);
        	item.GENERATED_ON = codes.getDate(5);
        	item.SEQUENCE_NO = codes.getInt(0);
        	list.add(item);
        	
            codes.setRow(i+1);
            
        }
		
        return list.toArray(new SalarySlip[rows]);
        
    }
    
    
    public JCoTable[] getSalesOrder(String OrderNo) throws JCoException
    {
    	JCoTable tab[] = new JCoTable[3];
    	
        try{
        	
	    	JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
	        
	        //JCoFunction function = destination.getRepository().getFunction("ZBAPI_PEP_SALES_DATA");
	    	JCoFunction function = destination.getRepository().getFunction("ZBAPI_THEIA_GET_SALES_ORDER");
	    	
	        function.getImportParameterList().setValue("ORDERNUMBER", OrderNo);
	        
	        if(function == null)
	            throw new RuntimeException("Function not found in SAP.");
	
	        try
	        {
	            function.execute(destination);
	        }
	        catch(AbapException e)
	        {
	            System.out.println(e.toString());
	        }
	        
	        tab[0] = function.getTableParameterList().getTable("ORDERHEADER");
	        tab[1] = function.getTableParameterList().getTable("ORDERITEMS");
	        tab[2] = function.getTableParameterList().getTable("ORDERFLOW");
	        
	        
        }catch(JCoException e){
        	System.out.println("Could not connect SAP: getSalesOrder()"+e);
        }
        
        return tab;

    }

    public JCoTable getDistributorStatement(long DistributorID, Date FromDate, Date ToDate) throws JCoException
    {
    	JCoTable tab = null;
    	
        try{
        	
	    	JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
	        
	    	JCoFunction function = destination.getRepository().getFunction("ZBAPI_CARD");
	    	
	        function.getImportParameterList().setValue("START_DATE", FromDate);
	        function.getImportParameterList().setValue("END_DATE", ToDate);
	        function.getImportParameterList().setValue("KUNNR", DistributorID);
	        
	        if(function == null)
	            throw new RuntimeException("Function not found in SAP.");
	
	        try
	        {
	            function.execute(destination);
	        }
	        catch(AbapException e)
	        {
	            System.out.println(e.toString());
	        }
	        
	        tab = function.getTableParameterList().getTable("ZRETURN");
	        
	        
        }catch(JCoException e){
        	System.out.println("Could not connect SAP: getDistributorStatement()"+e);
        }
        
        return tab;

    }
    
    
    public boolean setCashSummary(Date date, double ledger, double credit, double security, double vehicle, double empty) throws JCoException
    {
    	boolean success = false;
    	
        try{
        	
        	
	    	JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
	        
	        //JCoFunction function = destination.getRepository().getFunction("ZBAPI_PEP_SALES_DATA");
	    	JCoFunction function = destination.getRepository().getFunction("ZBAPI_THEIA_SET_CASH_SUMMARY");
	    	
	        function.getImportParameterList().setValue("IZCS_DATE", date);
	        function.getImportParameterList().setValue("IZCS_LEDGER", ledger);
	        function.getImportParameterList().setValue("IZCS_CREDIT", credit);
	        function.getImportParameterList().setValue("IZCS_SECURITY", security);
	        function.getImportParameterList().setValue("IZCS_VEHICLE", vehicle);
	        function.getImportParameterList().setValue("IZCS_EMPTY", empty);
	        
	        if(function == null)
	            throw new RuntimeException("Function not found in SAP.");
	
	        try
	        {
	            function.execute(destination);
	            success = true;
	        }
	        catch(AbapException e)
	        {
	            System.out.println(e.toString());
	        }
	        
        }catch(JCoException e){
        	System.out.println("Could not connect SAP: setCashSummary()"+e);
        }
        
        return success;
        
    }
    
    
    public JCoTable[] getSalesOrdersInvoices(String FromDate, String ToDate) throws JCoException
    {
    	JCoTable tab[] = new JCoTable[5];
    	
        try{
        	
	    	JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
	        
	        JCoFunction function = destination.getRepository().getFunction("ZBAPI_PEP_SALES_ALL");
	        function.getImportParameterList().setValue("ERDAT_FROM", FromDate);
	        function.getImportParameterList().setValue("ERDAT_TO", ToDate);
	        
	        if(function == null)
	            throw new RuntimeException("Function not found in SAP.");
	
	        try
	        {
	            function.execute(destination);
	        }
	        catch(AbapException e)
	        {
	            System.out.println(e.toString());
	        }
	        
	        tab[0] = function.getTableParameterList().getTable("VBRK");
	        tab[1] = function.getTableParameterList().getTable("VBRP");
	        tab[2] = function.getTableParameterList().getTable("VBAK");
	        tab[3] = function.getTableParameterList().getTable("VBAP");
	        tab[4] = function.getTableParameterList().getTable("VBUK");
	        
        }catch(JCoException e){
        	System.out.println("Could not connect SAP: getSalesOrdersInvoices()"+e);
        	e.printStackTrace();
        }
        
        return tab;

    }
    
    public JCoTable getSalesInvoice(String InvoiceNo) throws JCoException
    {
    	JCoTable tab = null;
    	
        try{
        	
	    	JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
	        
	    	
	        //JCoFunction function = destination.getRepository().getFunction("ZBAPI_PEP_INVOICE_DATA");
	    	JCoFunction function = destination.getRepository().getFunction("ZBAPI_THEIA_GET_SALES_INVOICE");
	        function.getImportParameterList().setValue("INVOICENUMBER", InvoiceNo);
	        
	        if(function == null)
	            throw new RuntimeException("Function not found in SAP.");
	
	        try
	        {
	            function.execute(destination);
	        }
	        catch(AbapException e)
	        {
	            System.out.println(e.toString());
	        }
	        
	        tab = function.getTableParameterList().getTable("HEADER");
	        
	        
        }catch(JCoException e){
        	System.out.println("Could not connect SAP: getSalesOrder()"+e);
        }
        
        return tab;

    }

    public String getHTMLTable(JCoTable tab) {

		StringBuffer ret = new StringBuffer(
				"<table border=1 style='border-collapse:collapse;'>");

		ret.append("<tr><th colspan='" + tab.getNumColumns() + "'>"
				+ tab.getMetaData().getName() + "</th></tr>");

		ret.append("<tr>");
		for (int i = 0; i < tab.getNumColumns(); i++) {
			ret.append("<th>" + tab.getMetaData().getName(i)
					+ "<br> <font size=1>"
					+ tab.getMetaData().getDescription(i) + "</font></th>");
		}
		ret.append("</tr>");

		int rows = tab.getNumRows();
		if (rows > 0) {
			tab.firstRow();
			for (int i = 0; i < 200; i++) {
				ret.append("<tr>");
				for (int j = 0; j < tab.getNumColumns(); j++) {
					ret.append("<td>" + tab.getString(j) + "</td>");
				}
				ret.append("</tr>");
				tab.setRow(i + 1);
			}
		}
		ret.append("</table>");

		return ret.toString();

	}
     
    public JCoTable[] getHCMMaster() throws JCoException
    {
     JCoTable tab[] = new JCoTable[7];
     
        try{
         
      JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
         
         //JCoFunction function = destination.getRepository().getFunction("ZBAPI_PEP_HCM_MASTERTABLES");
      	 JCoFunction function = destination.getRepository().getFunction("ZBAPI_THEIA_GET_HCM_MASTER_V2");
         if(function == null)
             throw new RuntimeException("Function not found in SAP.");
 
         try
         {
             function.execute(destination);
         }
         catch(AbapException e)
         {
             System.out.println(e.toString());
         }
         
         tab[0] = function.getTableParameterList().getTable("CSKT"); // Cost Centers
         tab[1] = function.getTableParameterList().getTable("T527X"); // Org Units
         tab[2] = function.getTableParameterList().getTable("T528T"); // Positions
         tab[3] = function.getTableParameterList().getTable("PA0000"); // Employee Master Status
         tab[4] = function.getTableParameterList().getTable("PA0001"); // Cost Center, Org Units, Positions
         tab[5] = function.getTableParameterList().getTable("PA0002"); // Employee ID and Name
         tab[6] = function.getTableParameterList().getTable("PA0003"); // Payroll Status
         
        }catch(JCoException e){
         System.out.println("Could not connect SAP: getHCMMaster()"+e);
        }
        
        return tab;

    }

    public JCoTable getKONV() throws JCoException
    {
    	JCoTable tab = null;
    	
        try{
         
         JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
         
      	 JCoFunction function = destination.getRepository().getFunction("ZBAPI_THEIA_GET_KONV");
      	 
         if(function == null)
             throw new RuntimeException("Function not found in SAP.");
 
         try
         {
             function.execute(destination);
         }
         catch(AbapException e)
         {
             System.out.println(e.toString());
         }
         
         tab = function.getTableParameterList().getTable("KONV"); // KONV
         
        }catch(JCoException e){
         System.out.println("Could not connect SAP: getKONV()"+e);
        }
        
        
        return tab;
        
    }
    
    public JCoTable[] getAssetMaster() throws JCoException
    {
     JCoTable tab[] = new JCoTable[6];
     
        try{
         
      JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
         
         //JCoFunction function = destination.getRepository().getFunction("ZBAPI_PEP_ASSET_LIST");
      
      	 JCoFunction function = destination.getRepository().getFunction("ZBAPI_THEIA_GET_ASSET_MASTER");
         if(function == null)
             throw new RuntimeException("Function not found in SAP.");
 
         try
         {
             function.execute(destination);
         }
         catch(AbapException e)
         {
             System.out.println(e.toString());
         }
         
         tab[0] = function.getTableParameterList().getTable("ANLA"); // Asset Master
         tab[1] = function.getTableParameterList().getTable("ANLU"); // 
         tab[2] = function.getTableParameterList().getTable("AUFK"); // Publications
         
        }catch(JCoException e){
         System.out.println("Could not connect SAP: getAssetMaster()"+e);
        }
        
        return tab;

    }
    
    public JCoTable getARMaster() throws JCoException
    {
     JCoTable tab = null;
     
        try{
         
         JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
         
         //JCoFunction function = destination.getRepository().getFunction("ZBAPI_PEP_AR_MASTERTABLES");
         
         JCoFunction function = destination.getRepository().getFunction("ZBAPI_THEIA_GET_AR_MASTER");
         if(function == null)
             throw new RuntimeException("Function not found in SAP.");
 
         try
         {
             function.execute(destination);
         }
         catch(AbapException e)
         {
             System.out.println(e.toString());
         }
         
         tab = function.getTableParameterList().getTable(0); // Customer Master
         
        }catch(JCoException e){
         System.out.println("Could not connect SAP: getSalesOrder()"+e);
        }
        
        return tab;

    }
    
    public JCoTable getOutletList() throws JCoException
    {
     JCoTable tab = null;
     
        try{
         
         JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
         
         //JCoFunction function = destination.getRepository().getFunction("ZBAPI_PEP_OUTLET_LIST");
         JCoFunction function = destination.getRepository().getFunction("ZBAPI_THEIA_GET_OUTLETS");
         
         if(function == null)
             throw new RuntimeException("Function not found in SAP.");
 
         try
         {
             function.execute(destination);
         }
         catch(AbapException e)
         {
             System.out.println(e.toString());
         }
         
         tab = function.getTableParameterList().getTable(0); // Outlet Table
         
        }catch(JCoException e){
         System.out.println("Could not connect SAP: getSalesOrder()"+e);
        }
        
        return tab;

    }
    public void dropConnection() throws JCoException{
    	
    	if (com.sap.conn.jco.ext.Environment.isDestinationDataProviderRegistered()){
    		
	        try{
	            com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(myProvider);
	        }catch(IllegalStateException providerAlreadyRegisteredException){
	            //somebody else registered its implementation, 
	            //stop the execution
	            //throw new Error(providerAlreadyRegisteredException);
	        	System.out.println("SAPUtilities: Exception in dropConnection()");
	        }
        
    	}
    }
    
    
}