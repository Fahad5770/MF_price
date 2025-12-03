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



/**
 * basic examples for Java to ABAP communication  
 */
public class SyncOutletMaster
{
	static String ABAP_AS = "ABAP_AS_PRD";

    public void getOutletMaster() throws JCoException
    {
	 SAPUtilities obj = new SAPUtilities();
	 obj.connectPRD();
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
        JCoFunction function = destination.getRepository().getFunction("ZBAPI_SD_OUTLET_MASTER");
        function.getImportParameterList().setValue("OSTACT", 1);
        
        if(function == null)
            throw new RuntimeException("ZBAPI_SD_OUTLET_MASTER not found in SAP.");

        try
        {
            function.execute(destination);
        }
        catch(AbapException e)
        {
            System.out.println(e.toString());
            return;
        }
        
        JCoTable codes = function.getTableParameterList().getTable(0);
        
        insertOutletMaster(codes);

        obj.dropConnection();
    }
    
 
    
    public static void main(String[] args) throws JCoException
    {
    	SyncOutletMaster sm = new SyncOutletMaster();
    	
    	sm.getOutletMaster();

    }
    
    
    public void insertOutletMaster(JCoTable table){
		Datasource ds = new Datasource();
		
		try {
			
			
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			int rows = table.getNumRows();
		            
			
			System.out.println("Synchronization started "+rows+" Outlets:");
			
			table.firstRow();
		    for (int i = 0; i < rows; i++) {
		    	
		    	String OutletIDString = table.getString(1);
		    	
		    	long OutletID = 0;
		    	try{
		    		OutletID = Long.parseLong(OutletIDString);
		    	}catch(Exception e){e.printStackTrace();}
		    	
		    	if (OutletID > 0){
		    		
		    		
		    		//for (int j = 0; j < 33; j++){
		    			//System.out.println(table.getString(3));
		    		//}
		    		
		    		double lat = com.pbc.util.Utilities.parseDouble(table.getString(31));
		    		double lng = com.pbc.util.Utilities.parseDouble(table.getString(32));
		    		
		    		
		    		String address = com.pbc.util.Utilities.filterString(table.getString(15), 1, 255);
		    		String telephone = com.pbc.util.Utilities.filterString(table.getString(17), 1, 255);
		    		String vehicle = com.pbc.util.Utilities.filterString(table.getString(28), 1, 255);
		    		
		    		String CustomerName = com.pbc.util.Utilities.filterString(table.getString(3), 1, 255);
		    		String region = com.pbc.util.Utilities.filterString(table.getString(6), 1, 255);
		    		
		    		long CR_ID = com.pbc.util.Utilities.parseLong(table.getString(19));
		    		long Customer_ID = com.pbc.util.Utilities.parseLong(table.getString(2));
		    		
					
					//System.out.println("REPLACE INTO outletmaster (SE_NO,Outlet_ID,Outlet_Name,Bsi_ID,Bsi_Name,Customer_ID,Customer_Name,Region,Region_Name,Market_Code,Market_Name,Owner,Address,Telepohone,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,Created_By,Creation_Date,Latitude,Longitude) VALUES ('"+table.getString(0)+"','"+OutletID+"','"+table.getString(12).replace("\'", "`")+"','"+table.getString(4)+"','"+table.getString(5)+"','"+Customer_ID+"','"+CustomerName+"','"+region+"','"+table.getString(9)+"','"+table.getString(8)+"','"+table.getString(7)+"','"+table.getString(13).replace("\'", "`")+"','"+table.getString(14).replace("\'", "`").replace("\\", "/")+"','"+address+"','0','"+telephone+"','"+table.getString(18)+"','"+CR_ID+"','0','0','0','0','0','0','0','0',1,'"+vehicle+"','0','"+table.getString(10)+"','"+table.getString(11)+"','"+lat+"','"+lng+"')");
			    	
		    		
		    		try{
		    			s.executeUpdate("REPLACE INTO outletmaster (SE_NO,Outlet_ID,Outlet_Name,Bsi_ID,Bsi_Name,Customer_ID,Customer_Name,Region,Region_Name,Market_Code,Market_Name,Owner,Address,Telepohone,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,Created_By,Creation_Date,Latitude,Longitude) VALUES ('"+table.getString(0)+"','"+OutletID+"','"+table.getString(12).replace("\'", "`")+"','"+table.getString(4).replace("\'", "`")+"','"+table.getString(5).replace("\'", "`")+"','"+Customer_ID+"','"+CustomerName+"','"+region+"','"+table.getString(9).replace("\'", "`")+"','"+table.getString(8).replace("\'", "`")+"','"+table.getString(7).replace("\'", "`")+"','"+table.getString(13).replace("\'", "`")+"','"+table.getString(14).replace("\'", "`").replace("\\", "/")+"','"+address+"','0','"+telephone+"','"+table.getString(18).replace("\'", "`")+"','"+CR_ID+"','0','0','0','0','0','0','0','0',1,'"+vehicle+"','0','"+table.getString(10).replace("\'", "`")+"','"+table.getString(11).replace("\'", "`")+"','"+lat+"','"+lng+"')");
		    		}catch(Exception e){
		    			e.printStackTrace();
		    			System.out.println("REPLACE INTO outletmaster (SE_NO,Outlet_ID,Outlet_Name,Bsi_ID,Bsi_Name,Customer_ID,Customer_Name,Region,Region_Name,Market_Code,Market_Name,Owner,Address,Telepohone,NID_Number,RSM_ID,ASM_ID,CR_ID,Samp_Allowed,Samp_Type,Fix_Sampling,PCAS_Sampling,Advance_Sampling,Adavance,Adv_type,Deductions,Status,Vehicle,Discounted,Created_By,Creation_Date,Latitude,Longitude) VALUES ('"+table.getString(0)+"','"+OutletID+"','"+table.getString(12).replace("\'", "`")+"','"+table.getString(4).replace("\'", "`")+"','"+table.getString(5).replace("\'", "`")+"','"+Customer_ID+"','"+CustomerName+"','"+region+"','"+table.getString(9).replace("\'", "`")+"','"+table.getString(8).replace("\'", "`")+"','"+table.getString(7).replace("\'", "`")+"','"+table.getString(13).replace("\'", "`")+"','"+table.getString(14).replace("\'", "`").replace("\\", "/")+"','"+address+"','0','"+telephone+"','"+table.getString(18).replace("\'", "`")+"','"+CR_ID+"','0','0','0','0','0','0','0','0',1,'"+vehicle+"','0','"+table.getString(10).replace("\'", "`")+"','"+table.getString(11).replace("\'", "`")+"','"+lat+"','"+lng+"')");
		    		}
					
		    	}
				
				table.setRow(i+1);
				//break;
		    }
			
		    System.out.println("Done.");
		    
			s.close();
			ds.dropConnection();
		}catch(Exception e){e.printStackTrace();}
    }

}