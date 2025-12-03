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
public class SyncSales
{
	static String ABAP_AS = "ABAP_AS_PRD";

    public void getSales(int month, int year) throws JCoException
    {
	 SAPUtilities obj = new SAPUtilities();
	 obj.connectPRD();
        JCoDestination destination = JCoDestinationManager.getDestination(ABAP_AS);
        JCoFunction function = destination.getRepository().getFunction("ZBAPI_OUTLET_TRANSACTION_DATA");
        function.getImportParameterList().setValue(0, month);
        function.getImportParameterList().setValue(1, year);

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
        
        JCoTable codes = function.getTableParameterList().getTable(0);
        
        insertSales(codes);
	
	 obj.dropConnection();
    }
    
 
    
    public static void main(String[] args) throws JCoException
    {
    	SyncSales sm = new SyncSales();
    	
    	sm.getSales(Integer.parseInt(args[0]),Integer.parseInt(args[1]));

    }
    
    
    public void insertSales(JCoTable table){
    	
    	//System.out.println(table.toXML());
    	
		Datasource ds = new Datasource();
		
		try {
			
			System.out.println("Synchronization started for Sales:");
			
			ds.createConnection();
			Statement s = ds.createStatement();
			
			int rows = table.getNumRows();
		            
			table.firstRow();
			
			System.out.println( rows + " records found");
			
			System.out.println("Deleting:  Month: " + Utilities.parseInt(table.getString(16))+" Year: " +Utilities.parseInt(table.getString(15)) + "");
			
			s.executeUpdate("delete from sap_sales where month_zmonth = " + Utilities.parseInt(table.getString(16))+" and year_zyear = " +Utilities.parseInt(table.getString(15)) + " and chkdis != 'portal' ");
			
			System.out.println("Inserting:  Month: " + Utilities.parseInt(table.getString(16))+" Year: " +Utilities.parseInt(table.getString(15)) + "");
			
		    for (int i = 0; i < rows; i++) {
		    	
		    	
		    	long OutletID = Utilities.parseLong(table.getString(2));
		    	
		    	//System.out.println("INSERT INTO sap_sales (chkdis,lat,lng,sstid,amount_amtnt,client_mandt,created_by_ernam,created_on_erdat,customer_kunnr,description_maktg,fixsm_fixsm,fixsm_pcassm,material_matnr,materialgroup_1_mvgr1,month_zmonth,outlet_id,personnel_no_asmid,personnel_no_crid,personnel_no_rsmid,quty_quant,transaction_number,vehicle_vehnum,year_zyear) VALUES ('"+table.getString(13)+"','"+Utilities.parseDouble(table.getString(22))+"','"+Utilities.parseDouble(table.getString(23))+"','"+Utilities.parseInt(table.getString(1))+"','"+Utilities.parseDouble(table.getString(6))+"','"+Utilities.parseInt(table.getString(0))+"','"+table.getString(8)+"','"+table.getString(7)+"','"+Utilities.parseInt(table.getString(18))+"','"+table.getString(5)+"','"+Utilities.parseInt(table.getString(19))+"','"+Utilities.parseInt(table.getString(20))+"','"+Utilities.parseInt(table.getString(4))+"','"+table.getString(14)+"','"+Utilities.parseInt(table.getString(16))+"','"+table.getString(2)+"','"+Utilities.parseInt(table.getString(10))+"','"+Utilities.parseInt(table.getString(11))+"','"+Utilities.parseInt(table.getString(9))+"','"+Utilities.parseInt(table.getString(17))+"','"+Utilities.parseInt(table.getString(3))+"','"+table.getString(12)+"','"+Utilities.parseInt(table.getString(15))+"')");
		    	s.executeUpdate("INSERT INTO sap_sales (chkdis,lat,lng,sstid,amount_amtnt,client_mandt,created_by_ernam,created_on_erdat,customer_kunnr,description_maktg,fixsm_fixsm,fixsm_pcassm,material_matnr,materialgroup_1_mvgr1,month_zmonth,outlet_id,personnel_no_asmid,personnel_no_crid,personnel_no_rsmid,quty_quant,transaction_number,vehicle_vehnum,year_zyear) VALUES ('"+table.getString(13)+"','"+Utilities.parseDouble(table.getString(22))+"','"+Utilities.parseDouble(table.getString(23))+"','"+Utilities.parseInt(table.getString(1))+"','"+Utilities.parseDouble(table.getString(6))+"','"+Utilities.parseInt(table.getString(0))+"','"+table.getString(8)+"','"+table.getString(7)+"','"+Utilities.parseInt(table.getString(18))+"','"+table.getString(5)+"','"+Utilities.parseInt(table.getString(19))+"','"+Utilities.parseInt(table.getString(20))+"','"+Utilities.parseInt(table.getString(4))+"','"+table.getString(14)+"','"+Utilities.parseInt(table.getString(16))+"','"+OutletID+"','"+Utilities.parseInt(table.getString(10))+"','"+Utilities.parseInt(table.getString(11))+"','"+Utilities.parseInt(table.getString(9))+"','"+Utilities.parseInt(table.getString(17))+"','"+Utilities.parseInt(table.getString(3))+"','"+table.getString(12)+"','"+Utilities.parseInt(table.getString(15))+"')");
		    	
				table.setRow(i+1);
		    }
		    
		    System.out.println("Done");
			s.close();
			ds.dropConnection();
			
		}catch(Exception e){e.printStackTrace();}
    }

}