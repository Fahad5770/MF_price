package com.pbc.sap;
import java.sql.Statement;
import java.util.concurrent.CountDownLatch;

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



/**
 * basic examples for Java to ABAP communication  
 */
public class SAPTest
{
	static String ABAP_AS = "ABAP_AS_PRD";

    public void getSales() throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
        JCoFunction function = destination.getRepository().getFunction("BAPI_EMPLOYEE_GETDATA");
        function.getImportParameterList().setValue("EMPLOYEE_ID", 2381);
        //function.getImportParameterList().setValue(1, 2013);
        
        //int c = function.getImportParameterList().getFieldCount();
        //System.out.print(c);
        
        //function.getImportParameterList().setValue("Year", 2013);
        //function.getImportParameterList().setValue("SSOK_FROM", 1623);
        //function.getImportParameterList().setValue("SSOK_TO", 1623);
        if(function == null)
            throw new RuntimeException("ZBAPI_OUTLET_TRANSACTION_DATA not found in SAP.");

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }
        
        /*
        JCoStructure returnStructure = function.getExportParameterList().getStructure(0);
        System.out.println("Return Structure:"+ returnStructure.getString(0));
        
        
        
        if (! (returnStructure.getString(0).equals("")||returnStructure.getString("TYPE").equals("S"))  )   
        {
           throw new RuntimeException(returnStructure.getString("MESSAGE"));
        }
        */
        JCoTable codes = function.getTableParameterList().getTable("PERSONAL_DATA");
        
        //insertSales(codes);
        
        
        int columns = codes.getNumColumns();
        int rows = codes.getNumRows();
        
        System.out.println(rows + " " + columns);
        
        
        System.out.print(codes.toXML());
        
        /*
        codes.firstRow();
        for (int i = 0; i < rows; i++) 
        {
        	for (int j = 0; j < columns; j++){
        		System.out.println(codes.getString(j));
        	}
        	
            codes.setRow(i);
            
        }
*/
        
        
        /*
        String xmlstream = codes.toXML();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( new InputSource(new StringReader(xmlstream)) );
			
			
		    NodeList nodeList = document.getElementsByTagName("item");
		    
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        NodeList inode = nodeList.item(i).getChildNodes();
		        
		        
		        for (int j = 0; j < inode.getLength(); j++) {
			        if (inode.item(j).getNodeType() == Node.ELEMENT_NODE) {
			            System.out.println(inode.item(j).getNodeName() + " " + inode.item(j).getTextContent());
			        }
		        }
		        
		        
		    }			
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
        
        
        
        
        
        /*
        for (int i = 0; i < codes.getNumRows(); i++) 
        {
        	
            codes.setRow(i);
            
            
            System.out.println(codes.getString(0) + " "+ codes.getString(1) + " "+ codes.getString(2));
            //System.out.println(codes.getString(1));

        }
		*/
        //move the table cursor to first row
        /*
        codes.firstRow();
        for (int i = 0; i < codes.getNumRows(); i++, codes.nextRow()) 
        {
            function = destination.getRepository().getFunction("BAPI_COMPANYCODE_GETDETAIL");
            if (function == null) 
                throw new RuntimeException("BAPI_COMPANYCODE_GETDETAIL not found in SAP.");

            function.getImportParameterList().setValue("COMPANYCODEID", codes.getString("COMP_CODE"));
            
            //We do not need the addresses, so set the corresponding parameter to inactive.
            //Inactive parameters will be  either not generated or at least converted.  
            function.getExportParameterList().setActive("COMPANYCODE_ADDRESS",false);
            
            try
            {
                function.execute(destination);
            }
            catch (AbapException e)
            {
                System.out.println(e.toString());
                return;
            }

            returnStructure = function.getExportParameterList().getStructure("RETURN");
            if (! (returnStructure.getString("TYPE").equals("") ||
                   returnStructure.getString("TYPE").equals("S") ||
                   returnStructure.getString("TYPE").equals("W")) ) 
            {
                throw new RuntimeException(returnStructure.getString("MESSAGE"));
            }
            
            JCoStructure detail = function.getExportParameterList().getStructure("COMPANYCODE_DETAIL");
            
            System.out.println(detail.getString("COMP_CODE") + '\t' +
                               detail.getString("COUNTRY") + '\t' +
                               detail.getString("CITY"));
        }//for
        */
    }
    public void getSales2() throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
        JCoFunction function = destination.getRepository().getFunction("BAPI_GET_PAYSLIP_HTML");
        function.getImportParameterList().setValue("EMPLOYEENUMBER", 1707);
        function.getImportParameterList().setValue("SEQUENCENUMBER", 103);
        //function.getImportParameterList().setValue(1, 2013);
        
        //int c = function.getImportParameterList().getFieldCount();
        //System.out.print(c);
        
        //function.getImportParameterList().setValue("Year", 2013);
        //function.getImportParameterList().setValue("SSOK_FROM", 1623);
        //function.getImportParameterList().setValue("SSOK_TO", 1623);
        if(function == null)
            throw new RuntimeException("ZBAPI_OUTLET_TRANSACTION_DATA not found in SAP.");

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
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
        
        //insertSales(codes);
        
        
        int columns = codes.getNumColumns();
        int rows = codes.getNumRows();
        
        System.out.println(rows + " " + columns);
        
        
        //System.out.print(codes.toXML());
        
        
        codes.firstRow();
        for (int i = 0; i < rows; i++) 
        {
        	for (int j = 0; j < columns; j++){
        		System.out.print(codes.getString(j));
        	}
        	
            codes.setRow(i+1);
            //System.out.println("\n");
        }

        
        
        /*
        String xmlstream = codes.toXML();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( new InputSource(new StringReader(xmlstream)) );
			
			
		    NodeList nodeList = document.getElementsByTagName("item");
		    
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        NodeList inode = nodeList.item(i).getChildNodes();
		        
		        
		        for (int j = 0; j < inode.getLength(); j++) {
			        if (inode.item(j).getNodeType() == Node.ELEMENT_NODE) {
			            System.out.println(inode.item(j).getNodeName() + " " + inode.item(j).getTextContent());
			        }
		        }
		        
		        
		    }			
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
        
        
        
        
        
        /*
        for (int i = 0; i < codes.getNumRows(); i++) 
        {
        	
            codes.setRow(i);
            
            
            System.out.println(codes.getString(0) + " "+ codes.getString(1) + " "+ codes.getString(2));
            //System.out.println(codes.getString(1));

        }
		*/
        //move the table cursor to first row
        /*
        codes.firstRow();
        for (int i = 0; i < codes.getNumRows(); i++, codes.nextRow()) 
        {
            function = destination.getRepository().getFunction("BAPI_COMPANYCODE_GETDETAIL");
            if (function == null) 
                throw new RuntimeException("BAPI_COMPANYCODE_GETDETAIL not found in SAP.");

            function.getImportParameterList().setValue("COMPANYCODEID", codes.getString("COMP_CODE"));
            
            //We do not need the addresses, so set the corresponding parameter to inactive.
            //Inactive parameters will be  either not generated or at least converted.  
            function.getExportParameterList().setActive("COMPANYCODE_ADDRESS",false);
            
            try
            {
                function.execute(destination);
            }
            catch (AbapException e)
            {
                System.out.println(e.toString());
                return;
            }

            returnStructure = function.getExportParameterList().getStructure("RETURN");
            if (! (returnStructure.getString("TYPE").equals("") ||
                   returnStructure.getString("TYPE").equals("S") ||
                   returnStructure.getString("TYPE").equals("W")) ) 
            {
                throw new RuntimeException(returnStructure.getString("MESSAGE"));
            }
            
            JCoStructure detail = function.getExportParameterList().getStructure("COMPANYCODE_DETAIL");
            
            System.out.println(detail.getString("COMP_CODE") + '\t' +
                               detail.getString("COUNTRY") + '\t' +
                               detail.getString("CITY"));
        }//for
        */
    }
    
    public void getSales3() throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
        JCoFunction function = destination.getRepository().getFunction("BAPI_GET_PAYROLL_RESULT_LIST");
        function.getImportParameterList().setValue("EMPLOYEENUMBER", 1909);
        function.getImportParameterList().setValue("FROMDATE", "18000101");
        function.getImportParameterList().setValue("TODATE", "99991231");
        //function.getImportParameterList().setValue(1, 2013);
        
        //int c = function.getImportParameterList().getFieldCount();
        //System.out.print(c);
        
        //function.getImportParameterList().setValue("Year", 2013);
        //function.getImportParameterList().setValue("SSOK_FROM", 1623);
        //function.getImportParameterList().setValue("SSOK_TO", 1623);
        if(function == null)
            throw new RuntimeException("ZBAPI_OUTLET_TRANSACTION_DATA not found in SAP.");

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
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
        
        //insertSales(codes);
        
        
        int columns = codes.getNumColumns();
        int rows = codes.getNumRows();
        
        //System.out.println(rows + " " + columns);
        
        
        //System.out.print(codes.toXML());
        
        codes.lastRow();
        System.out.println(codes.getString(0));
        
        /*
        codes.firstRow();
        for (int i = 0; i < rows; i++) 
        {
        	for (int j = 0; j < columns; j++){
        		System.out.println(codes.getString(j));
        	}
        	
            codes.setRow(i+1);
            
        }
		*/
        
        
        /*
        String xmlstream = codes.toXML();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( new InputSource(new StringReader(xmlstream)) );
			
			
		    NodeList nodeList = document.getElementsByTagName("item");
		    
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        NodeList inode = nodeList.item(i).getChildNodes();
		        
		        
		        for (int j = 0; j < inode.getLength(); j++) {
			        if (inode.item(j).getNodeType() == Node.ELEMENT_NODE) {
			            System.out.println(inode.item(j).getNodeName() + " " + inode.item(j).getTextContent());
			        }
		        }
		        
		        
		    }			
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
        
        
        
        
        
        /*
        for (int i = 0; i < codes.getNumRows(); i++) 
        {8
        	
            codes.setRow(i);
            
            
            System.out.println(codes.getString(0) + " "+ codes.getString(1) + " "+ codes.getString(2));
            //System.out.println(codes.getString(1));

        }
		*/
        //move the table cursor to first row
        /*
        codes.firstRow();
        for (int i = 0; i < codes.getNumRows(); i++, codes.nextRow()) 
        {
            function = destination.getRepository().getFunction("BAPI_COMPANYCODE_GETDETAIL");
            if (function == null) 
                throw new RuntimeException("BAPI_COMPANYCODE_GETDETAIL not found in SAP.");

            function.getImportParameterList().setValue("COMPANYCODEID", codes.getString("COMP_CODE"));
            
            //We do not need the addresses, so set the corresponding parameter to inactive.
            //Inactive parameters will be  either not generated or at least converted.  
            function.getExportParameterList().setActive("COMPANYCODE_ADDRESS",false);
            
            try
            {
                function.execute(destination);
            }
            catch (AbapException e)
            {
                System.out.println(e.toString());
                return;
            }

            returnStructure = function.getExportParameterList().getStructure("RETURN");
            if (! (returnStructure.getString("TYPE").equals("") ||
                   returnStructure.getString("TYPE").equals("S") ||
                   returnStructure.getString("TYPE").equals("W")) ) 
            {
                throw new RuntimeException(returnStructure.getString("MESSAGE"));
            }
            
            JCoStructure detail = function.getExportParameterList().getStructure("COMPANYCODE_DETAIL");
            
            System.out.println(detail.getString("COMP_CODE") + '\t' +
                               detail.getString("COUNTRY") + '\t' +
                               detail.getString("CITY"));
        }//for
        */
    }
    public void getSalesOrder() throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
        JCoFunction function = destination.getRepository().getFunction("BAPI_SALESORDER_GETSTATUS");
        function.getImportParameterList().setValue("SALESDOCUMENT", "2000283158");
        //function.getImportParameterList().setValue("MAXROWS", "100");
        //function.getImportParameterList().setValue(0, 10);
        //function.getImportParameterList().setValue("FROMDATE", "18000101");
        //function.getImportParameterList().setValue("TODATE", "99991231");
        //function.getImportParameterList().setValue(1, 2013);
        
        int c = function.getImportParameterList().getFieldCount();
        //System.out.print(c);
        
        JCoParameterFieldIterator fi = function.getImportParameterList().getParameterFieldIterator();

        while(fi.hasNextField()){
        	JCoField jf = fi.nextField();
        	
        	System.out.println(jf.getName() + " / " + jf.getDescription());
        }
        
        
        if(function == null)
            throw new RuntimeException("ZBAPI_OUTLET_TRANSACTION_DATA not found in SAP.");

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
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
        
        //insertSales(codes);
        
        
        int columns = codes.getNumColumns();
        int rows = codes.getNumRows();
        
        System.out.println("R/C: "+rows + "/" + columns);
        
        
        System.out.print(codes.toXML());
        
        //codes.lastRow();
        //System.out.println(codes.getString(0));
        
        /*
        codes.firstRow();
        for (int i = 0; i < rows; i++) 
        {
        	for (int j = 0; j < columns; j++){
        		System.out.println(codes.getString(j));
        	}
        	
            codes.setRow(i+1);
            
        }
		*/
        
        
        /*
        String xmlstream = codes.toXML();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( new InputSource(new StringReader(xmlstream)) );
			
			
		    NodeList nodeList = document.getElementsByTagName("item");
		    
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        NodeList inode = nodeList.item(i).getChildNodes();
		        
		        
		        for (int j = 0; j < inode.getLength(); j++) {
			        if (inode.item(j).getNodeType() == Node.ELEMENT_NODE) {
			            System.out.println(inode.item(j).getNodeName() + " " + inode.item(j).getTextContent());
			        }
		        }
		        
		        
		    }			
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
        
        
        
        
        
        /*
        for (int i = 0; i < codes.getNumRows(); i++) 
        {8
        	
            codes.setRow(i);
            
            
            System.out.println(codes.getString(0) + " "+ codes.getString(1) + " "+ codes.getString(2));
            //System.out.println(codes.getString(1));

        }
		*/
        //move the table cursor to first row
        /*
        codes.firstRow();
        for (int i = 0; i < codes.getNumRows(); i++, codes.nextRow()) 
        {
            function = destination.getRepository().getFunction("BAPI_COMPANYCODE_GETDETAIL");
            if (function == null) 
                throw new RuntimeException("BAPI_COMPANYCODE_GETDETAIL not found in SAP.");

            function.getImportParameterList().setValue("COMPANYCODEID", codes.getString("COMP_CODE"));
            
            //We do not need the addresses, so set the corresponding parameter to inactive.
            //Inactive parameters will be  either not generated or at least converted.  
            function.getExportParameterList().setActive("COMPANYCODE_ADDRESS",false);
            
            try
            {
                function.execute(destination);
            }
            catch (AbapException e)
            {
                System.out.println(e.toString());
                return;
            }

            returnStructure = function.getExportParameterList().getStructure("RETURN");
            if (! (returnStructure.getString("TYPE").equals("") ||
                   returnStructure.getString("TYPE").equals("S") ||
                   returnStructure.getString("TYPE").equals("W")) ) 
            {
                throw new RuntimeException(returnStructure.getString("MESSAGE"));
            }
            
            JCoStructure detail = function.getExportParameterList().getStructure("COMPANYCODE_DETAIL");
            
            System.out.println(detail.getString("COMP_CODE") + '\t' +
                               detail.getString("COUNTRY") + '\t' +
                               detail.getString("CITY"));
        }//for
        */
    }
    public void getInvoice() throws JCoException
    {
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
        JCoFunction function = destination.getRepository().getFunction("BAPI_SALESORDER_GETSTATUS");
        function.getImportParameterList().setValue("SALESDOCUMENT", "2000282332");
        //function.getImportParameterList().setValue("MAXROWS", "100");
        //function.getImportParameterList().setValue(0, 10);
        //function.getImportParameterList().setValue("FROMDATE", "18000101");
        //function.getImportParameterList().setValue("TODATE", "99991231");
        //function.getImportParameterList().setValue(1, 2013);
        
        int c = function.getImportParameterList().getFieldCount();
        //System.out.print(c);
        
        JCoParameterFieldIterator fi = function.getImportParameterList().getParameterFieldIterator();

        while(fi.hasNextField()){
        	JCoField jf = fi.nextField();
        	
        	System.out.println(jf.getName() + " / " + jf.getDescription());
        }
        
        
        if(function == null)
            throw new RuntimeException("ZBAPI_OUTLET_TRANSACTION_DATA not found in SAP.");

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
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
        
        //insertSales(codes);
        
        
        int columns = codes.getNumColumns();
        int rows = codes.getNumRows();
        
        System.out.println("R/C: "+rows + "/" + columns);
        
        
        System.out.print(codes.toXML());
        
        //codes.lastRow();
        //System.out.println(codes.getString(0));
        
        /*
        codes.firstRow();
        for (int i = 0; i < rows; i++) 
        {
        	for (int j = 0; j < columns; j++){
        		System.out.println(codes.getString(j));
        	}
        	
            codes.setRow(i+1);
            
        }
		*/
        
        
        /*
        String xmlstream = codes.toXML();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        
        try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse( new InputSource(new StringReader(xmlstream)) );
			
			
		    NodeList nodeList = document.getElementsByTagName("item");
		    
		    for (int i = 0; i < nodeList.getLength(); i++) {
		        NodeList inode = nodeList.item(i).getChildNodes();
		        
		        
		        for (int j = 0; j < inode.getLength(); j++) {
			        if (inode.item(j).getNodeType() == Node.ELEMENT_NODE) {
			            System.out.println(inode.item(j).getNodeName() + " " + inode.item(j).getTextContent());
			        }
		        }
		        
		        
		    }			
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
        
        
        
        
        
        /*
        for (int i = 0; i < codes.getNumRows(); i++) 
        {8
        	
            codes.setRow(i);
            
            
            System.out.println(codes.getString(0) + " "+ codes.getString(1) + " "+ codes.getString(2));
            //System.out.println(codes.getString(1));

        }
		*/
        //move the table cursor to first row
        /*
        codes.firstRow();
        for (int i = 0; i < codes.getNumRows(); i++, codes.nextRow()) 
        {
            function = destination.getRepository().getFunction("BAPI_COMPANYCODE_GETDETAIL");
            if (function == null) 
                throw new RuntimeException("BAPI_COMPANYCODE_GETDETAIL not found in SAP.");

            function.getImportParameterList().setValue("COMPANYCODEID", codes.getString("COMP_CODE"));
            
            //We do not need the addresses, so set the corresponding parameter to inactive.
            //Inactive parameters will be  either not generated or at least converted.  
            function.getExportParameterList().setActive("COMPANYCODE_ADDRESS",false);
            
            try
            {
                function.execute(destination);
            }
            catch (AbapException e)
            {
                System.out.println(e.toString());
                return;
            }

            returnStructure = function.getExportParameterList().getStructure("RETURN");
            if (! (returnStructure.getString("TYPE").equals("") ||
                   returnStructure.getString("TYPE").equals("S") ||
                   returnStructure.getString("TYPE").equals("W")) ) 
            {
                throw new RuntimeException(returnStructure.getString("MESSAGE"));
            }
            
            JCoStructure detail = function.getExportParameterList().getStructure("COMPANYCODE_DETAIL");
            
            System.out.println(detail.getString("COMP_CODE") + '\t' +
                               detail.getString("COUNTRY") + '\t' +
                               detail.getString("CITY"));
        }//for
        */
    }
 
    
    public static void main(String[] args) throws JCoException
    {
    	SAPTest sm = new SAPTest();
    	
    	//sm.getSales();
    	//sm.getSales2();
    	//sm.getSales3();
    	sm.getSalesOrder();
    }
}