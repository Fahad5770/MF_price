package com.pbc.reports;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.pbc.util.Utilities;
import java.util.Date;
import com.pbc.util.Datasource;

public class EmptyReconciliation {
	
	
	public int getUnitPerSKU240() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		
		
		int UnitPerSKU240=0;
		
		ResultSet rs3 = s2.executeQuery("select unit_per_case from inventory_packages where id=12");
		if(rs3.first()){
			UnitPerSKU240 = rs3.getInt("unit_per_case");
		}
		s.close();
		s2.close();
		
		ds.dropConnection();
		
		return UnitPerSKU240;
		
	}
	
	public int getUnitPerSKU250() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		int UnitPerSKU250=0;
		
		ResultSet rs3 = s2.executeQuery("select unit_per_case from inventory_packages where id=11");
		if(rs3.first()){
			UnitPerSKU250 = rs3.getInt("unit_per_case");
		}
		s.close();
		s2.close();
		ds.dropConnection();
		return UnitPerSKU250;
		
	}
	
	public int getUnitPerSKU1000() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		Statement s = ds.createStatement();
		Statement s2 = ds.createStatement();
		int UnitPerSKU1000=0;
		
		ResultSet rs3 = s2.executeQuery("select unit_per_case from inventory_packages where id=1");
		if(rs3.first()){
			UnitPerSKU1000 = rs3.getInt("unit_per_case");
		}
		
		
		s.close();
		s2.close();
		
		ds.dropConnection();
		
		return UnitPerSKU1000;
		
	}
	
	public long getNewEmptyOpen(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		
		long NewEmptyGTotalRawCases=0;
		long NewEmptyGTotalUnits=0;
		
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		//System.out.println("SELECT bieni.total_units FROM "+ds.logDatabaseName()+".bi_empty_new_issuance bieni join inventory_products ip on ip.sap_code=bieni.product_sap_code where bieni.is_old=0 and ip.category_id !=3 and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ip.package_id=11");
		
		if(PackID==11){	
			long[] RawCasesUnits2;
			long TotalUnits2=0;
			ResultSet rss = s.executeQuery("SELECT bieni.total_units FROM "+ds.logDatabaseName()+".bi_empty_new_issuance bieni join inventory_products ip on ip.sap_code=bieni.product_sap_code where bieni.is_old=0 and ip.category_id !=3 and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ip.package_id=11");
		 	while(rss.next()){		
		 		
		 		
				
							
				TotalUnits2 += rss.getInt("total_units");
				 		
			} 
		 	RawCasesUnits2 = Utilities.getRawCasesAndUnits(TotalUnits2, this.getUnitPerSKU250());
			
			NewEmptyGTotalRawCases+=RawCasesUnits2[0];
			NewEmptyGTotalUnits+=RawCasesUnits2[1];	
			
		}else if(PackID==1){ //for 1000
			long[] RawCasesUnits2;
			long TotalUnits2=0;
			ResultSet rss1 = s.executeQuery("SELECT bieni.total_units FROM "+ds.logDatabaseName()+".bi_empty_new_issuance bieni join inventory_products ip on ip.sap_code=bieni.product_sap_code where bieni.is_old=0 and ip.category_id !=3 and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ip.package_id=1");
		 	while(rss1.next()){
				
		 		
			
				
				TotalUnits2 += rss1.getInt("total_units");
				
		 	}	
		 	RawCasesUnits2 = Utilities.getRawCasesAndUnits(TotalUnits2, this.getUnitPerSKU1000());
			
			NewEmptyGTotalRawCases+=RawCasesUnits2[0];
			NewEmptyGTotalUnits+=RawCasesUnits2[1];
			
		}
		
		s.close();		
		ds.dropConnection();		
		return NewEmptyGTotalRawCases;
		
	}
	
	
	
	public long getOldEmptyPurchase(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long OldEmptyGTotalRawCases=0;
		long OldEmptyGTotalUnits=0;
		
		if(PackID==11){
			long[] RawCasesUnits1;
			long TotalUnits1=0;
			ResultSet rss2 = s.executeQuery("SELECT bieni.total_units FROM "+ds.logDatabaseName()+".bi_empty_new_issuance bieni join inventory_products ip on ip.sap_code=bieni.product_sap_code where bieni.is_old=1 and ip.category_id !=3 and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ip.package_id=11");
			while(rss2.next()){
				
				
				
				
				
				
				TotalUnits1 += rss2.getInt("total_units");
				
				
			}
			
			RawCasesUnits1 = Utilities.getRawCasesAndUnits(TotalUnits1, this.getUnitPerSKU250());
			
			OldEmptyGTotalRawCases+=RawCasesUnits1[0];
			OldEmptyGTotalUnits+=RawCasesUnits1[1];
			
		}else if(PackID==1){
			
			long[] RawCasesUnits1;
			long TotalUnits1=0;
			
			ResultSet rss3 = s.executeQuery("SELECT bieni.total_units FROM "+ds.logDatabaseName()+".bi_empty_new_issuance bieni join inventory_products ip on ip.sap_code=bieni.product_sap_code where bieni.is_old=1 and ip.category_id !=3 and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ip.package_id=1");
			while(rss3.next()){
				
				TotalUnits1 += rss3.getInt("total_units");
				
				
			}
			
			RawCasesUnits1 = Utilities.getRawCasesAndUnits(TotalUnits1, this.getUnitPerSKU1000());
			
			OldEmptyGTotalRawCases+=RawCasesUnits1[0];
			OldEmptyGTotalUnits+=RawCasesUnits1[1];
		}
		
		s.close();
		
		ds.dropConnection();
		
		return OldEmptyGTotalRawCases;
		
	}
	
	
	
	public long getAgencyPurchase(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long GTotalRawCases=0;
		long GTotalUnits=0;
		
		if(PackID==11){
			long TotalUnits1=0;
			long[] RawCasesUnits;
			ResultSet rs4 = s.executeQuery("SELECT bieoi.total_units FROM "+ds.logDatabaseName()+".bi_empty_other_issuance bieoi where bieoi.document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bieoi.package_id=11  and bieoi.reason in('18')");
			while(rs4.next()){
				
				int UnitPerSKU=0;
				
				long TotalUnits=0;
				
				
				TotalUnits = rs4.getInt("total_units");
				if(TotalUnits<0){ TotalUnits=TotalUnits*-1;}
				TotalUnits1+=TotalUnits;
			}
			
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnits1, this.getUnitPerSKU250());
			
			GTotalRawCases+=RawCasesUnits[0];
			GTotalUnits+=RawCasesUnits[1];
			
		}else if(PackID==1){
			long TotalUnits1=0;
			long[] RawCasesUnits;
			ResultSet rs5 = s.executeQuery("SELECT bieoi.total_units FROM "+ds.logDatabaseName()+".bi_empty_other_issuance bieoi where bieoi.document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bieoi.package_id=1  and bieoi.reason in('18')");
			while(rs5.next()){
				
				
				
				long TotalUnits=0;
				
				
				TotalUnits = rs5.getInt("total_units");
				if(TotalUnits<0){ TotalUnits=TotalUnits*-1;}
				
				TotalUnits1+=TotalUnits;
				
			}
			
			
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnits1, this.getUnitPerSKU1000());
			GTotalRawCases+=RawCasesUnits[0];
			GTotalUnits=RawCasesUnits[1];
		}
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return GTotalRawCases;
		
	}
	
	
	
	public long[] getSLCBR(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		
		
		if(PackID==11){
			int TotalUnitsLoss=0;
			ResultSet rs8 = s.executeQuery("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='B03' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" group by reason,package_id");
			if(rs8.first()){								
				 TotalUnitsLoss += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss=0;
			ResultSet rs8 = s.executeQuery("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 1 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='B03' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" group by reason,package_id");
			if(rs8.first()){								
				 TotalUnitsLoss += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss, this.getUnitPerSKU1000());
		}
	
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	public long[] getSpecialBreakage(Date StartDate, Date EndDate,int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		
		
		if(PackID==11){
			int TotalUnitsLoss=0;
			ResultSet rs8 = s.executeQuery("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='16' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" group by reason,package_id");
			if(rs8.first()){								
				 TotalUnitsLoss += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss=0;
			ResultSet rs8 = s.executeQuery("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 1 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='16' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" group by reason,package_id");
			if(rs8.first()){								
				 TotalUnitsLoss += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss, this.getUnitPerSKU1000());
		}
	
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	public long[] getSpecialBurst(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		
		if(PackID==11){
			int TotalUnitsLoss=0;
			ResultSet rs8 = s.executeQuery("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='15' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" group by reason,package_id");
			if(rs8.first()){								
				 TotalUnitsLoss += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss=0;
			ResultSet rs8 = s.executeQuery("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 1 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='15' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" group by reason,package_id");
			if(rs8.first()){								
				 TotalUnitsLoss += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss, this.getUnitPerSKU1000());
		}
	
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	public long[] getSpecialZabt(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		
		if(PackID==11){
			int TotalUnitsLoss=0;
			ResultSet rs8 = s.executeQuery("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='22' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" group by reason,package_id");
			if(rs8.first()){								
				 TotalUnitsLoss += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss=0;
			ResultSet rs8 = s.executeQuery("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 1 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='22' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" group by reason,package_id");
			if(rs8.first()){								
				 TotalUnitsLoss += rs8.getInt("total");
				
				
			}
			
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss, this.getUnitPerSKU1000());
		}
	
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	
	
	
	public long[] getDepotBurst(Date StartDate, Date EndDate,int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		if(PackID==11){
			int TotalUnitsLoss1=0;
			ResultSet rs8 = s.executeQuery("SELECT sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '13' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R01' and reason in (3,7,5)) or (material_group_pack in ('P01','P12') and reason in (13,3,4,6)))");
			if(rs8.first()){
				
				 TotalUnitsLoss1 += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss1=0;
			ResultSet rs10 = s.executeQuery("SELECT sum(total_units) total2 FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '13' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R02' and reason in (3,7,5)) or (material_group_pack in ('P02') and reason in (13,3,4,6)))");
			if(rs10.first()){
				
				 TotalUnitsLoss1 += rs10.getInt("total2");
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU1000());
		}
	
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	public long[] getDepotFallenBurst(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		
		if(PackID==11){
			int TotalUnitsLoss1=0;
			ResultSet rs8 = s.executeQuery("SELECT sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '6' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R01' and reason in (3,7,5)) or (material_group_pack in ('P01','P12') and reason in (13,3,4,6)))");
			if(rs8.first()){
				
				 TotalUnitsLoss1 += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU250());
			
		}else if(PackID==1){
			int TotalUnitsLoss1=0;
			ResultSet rs10 = s.executeQuery("SELECT sum(total_units) total2 FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '6' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R02' and reason in (3,7,5)) or (material_group_pack in ('P02') and reason in (13,3,4,6)))");
			if(rs10.first()){
				
				 TotalUnitsLoss1 += rs10.getInt("total2");
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU1000());
		}
		
	
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	public long[] getLifterBreakage(Date StartDate, Date EndDate,int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		
		if(PackID==11){
			int TotalUnitsLoss1=0;
			ResultSet rs8 = s.executeQuery("SELECT sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '3' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R01' and reason in (3,7,5)) or (material_group_pack in ('P01','P12') and reason in (13,3,4,6)))");
			if(rs8.first()){
				
				 TotalUnitsLoss1 += rs8.getInt("total");
			
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss1=0;
			ResultSet rs10 = s.executeQuery("SELECT sum(total_units) total2 FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '3' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R02' and reason in (3,7,5)) or (material_group_pack in ('P02') and reason in (13,3,4,6)))");
			if(rs10.first()){
				
				 TotalUnitsLoss1 += rs10.getInt("total2");
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU1000());
		}
		
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	public long[] getLifterBurst(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		
		if(PackID==11){
			int TotalUnitsLoss1=0;
			ResultSet rs8 = s.executeQuery("SELECT sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '4' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R01' and reason in (3,7,5)) or (material_group_pack in ('P01','P12') and reason in (13,3,4,6)))");
			if(rs8.first()){
				
				 TotalUnitsLoss1 += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss1=0;
			ResultSet rs10 = s.executeQuery("SELECT sum(total_units) total2 FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '4' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R02' and reason in (3,7,5)) or (material_group_pack in ('P02') and reason in (13,3,4,6)))");
			if(rs10.first()){
				
				 TotalUnitsLoss1 += rs10.getInt("total2");
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU1000());
		}
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	public long[] getWashBreakage(Date StartDate, Date EndDate,int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		
		if(PackID==11){
			int TotalUnitsLoss1=0;
			ResultSet rs8 = s.executeQuery("SELECT sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '7' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R01' and reason in (3,7,5)) or (material_group_pack in ('P01','P12') and reason in (13,3,4,6)))");
			if(rs8.first()){
				
				 TotalUnitsLoss1 += rs8.getInt("total");
				
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss1=0;
			ResultSet rs10 = s.executeQuery("SELECT sum(total_units) total2 FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '7' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R02' and reason in (3,7,5)) or (material_group_pack in ('P02') and reason in (13,3,4,6)))");
			if(rs10.first()){
				
				 TotalUnitsLoss1 += rs10.getInt("total2");
				
			}
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU1000());
		}
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	public long[] getFallenBreakage(Date StartDate, Date EndDate,int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnits={0,0};
		
	
		//System.out.println("SELECT reason,(select label from "+ds.logDatabaseName()+".bi_empty_transaction_reason bietr where bietr.id=reason) reason_label, sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance where package_id = 11 and botyp IN ('001','002','003','004','005','006','007','008','009','010','012','013') and reason ='"+ReasonArray[i]+"' and status = 'Confirmed' and movmt IN ('Empty In' , 'Empty Out') and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDateNext(EndDate)+" group by reason,package_id");
		
		if(PackID==11){
			int TotalUnitsLoss1=0;
			ResultSet rs8 = s.executeQuery("SELECT sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '5' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R01' and reason in (3,7,5)) or (material_group_pack in ('P01','P12') and reason in (13,3,4,6)))");
			if(rs8.first()){
				
				 TotalUnitsLoss1 += rs8.getInt("total");
				
				
			}
			
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU250());
		}else if(PackID==1){
			
			int TotalUnitsLoss1=0;
			ResultSet rs10 = s.executeQuery("SELECT sum(total_units) total2 FROM "+ds.logDatabaseName()+".bi_empty_losses where  reason = '5' and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and ((material_group = 'R02' and reason in (3,7,5)) or (material_group_pack in ('P02') and reason in (13,3,4,6)))");
			if(rs10.first()){
				
				 TotalUnitsLoss1 += rs10.getInt("total2");
				
			}
			
			RawCasesUnits = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU1000());
		}
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnits;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public long[] getDuringProductionShippingBreakage(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnitsDuringPro={0,0};
		
		if(PackID==11){
			int TotalUnitsLoss1=0;
			ResultSet rs11 = s.executeQuery("SELECT sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_production_shipping_loss bieps join pep.inventory_products ip  on ip.id=bieps.product_id where ip.package_id=11 and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
			if(rs11.first()){
				
				 TotalUnitsLoss1 += rs11.getInt("total");
				
				
			}
			
			RawCasesUnitsDuringPro = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss1=0;
			ResultSet rs12 = s.executeQuery("SELECT sum(total_units) total FROM "+ds.logDatabaseName()+".bi_empty_production_shipping_loss bieps join pep.inventory_products ip  on ip.id=bieps.product_id where ip.package_id=1 and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
			if(rs12.first()){
				
				 TotalUnitsLoss1 += rs12.getInt("total");
				
				
			}
			
			RawCasesUnitsDuringPro = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU1000());
		}
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnitsDuringPro;
		
	}
	
	
	
	public int getDuringProductionBreakage(Date StartDate, Date EndDate,int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		int TotalUnitsLoss1RawCases=0;
		
		if(PackID==11){
			ResultSet rs13 = s.executeQuery("SELECT sum(quantity) raw_cases FROM "+ds.logDatabaseName()+".bi_empty_production_loss where pack='P01' and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and loss_type in(30,90,140,190) and flag =''");
			if(rs13.first()){
				TotalUnitsLoss1RawCases = rs13.getInt("raw_cases");
			}
		}else if(PackID==1){
			ResultSet rs14 = s.executeQuery("SELECT sum(quantity) raw_cases FROM "+ds.logDatabaseName()+".bi_empty_production_loss where pack='P02' and document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and loss_type in(30,90,140,190) and flag =''");
			if(rs14.first()){
				TotalUnitsLoss1RawCases = rs14.getInt("raw_cases");
			}
		}
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return TotalUnitsLoss1RawCases;
		
	}
	
	
	
	public int getEmptyReturnToStore(Date StartDate, Date EndDate,int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		int RawCasesUnitsEmptyReturn=0;
		
		if(PackID==11){
			ResultSet rs17 = s.executeQuery("SELECT sum(total_units)/24 total FROM "+ds.logDatabaseName()+".empty_store_return_other_brands bieps where product_sap_code in(5017,5018,5019,5020,5022,5023) and posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
			if(rs17.first()){		
				
				RawCasesUnitsEmptyReturn = rs17.getInt("total");
				
			}
		}else if(PackID==1){
			ResultSet rs18 = s.executeQuery("SELECT sum(total_units)/6 total FROM "+ds.logDatabaseName()+".empty_store_return_other_brands bieps where product_sap_code in(5024,5026) and  posting_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate));
			if(rs18.first()){ 
				RawCasesUnitsEmptyReturn = rs18.getInt("total");
				
			}
		}
	
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnitsEmptyReturn;
		
	}
	
	
	
	public long[] getEmptySales(Date StartDate, Date EndDate,int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long[] RawCasesUnitsEmptySale={0,0};
	
		if(PackID==11){
			int TotalUnitsLoss1=0;
			ResultSet rs19 = s.executeQuery("SELECT sum(bieoi.total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance bieoi where bieoi.document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bieoi.package_id in (11)  and bieoi.reason in('01')");
			if(rs19.first()){
				
				 TotalUnitsLoss1 += rs19.getInt("total");
				
			}
			RawCasesUnitsEmptySale = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU250());
		}else if(PackID==1){
			int TotalUnitsLoss1=0;
			ResultSet rs20 = s.executeQuery("SELECT sum(bieoi.total_units) total FROM "+ds.logDatabaseName()+".bi_empty_other_issuance bieoi where bieoi.document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bieoi.package_id in (1)  and bieoi.reason in('01')");
			if(rs20.first()){
				
				 TotalUnitsLoss1 += rs20.getInt("total");
				
				
			}
			RawCasesUnitsEmptySale = Utilities.getRawCasesAndUnits(TotalUnitsLoss1, this.getUnitPerSKU1000());
		}
		
		
		
		s.close();
		
		ds.dropConnection();
		
		return RawCasesUnitsEmptySale;
		
	}
	
	//New Market Loss
	
	public long getZabtAllowance(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long ZabtAllowanceGTotalRawCases=0;
		long ZabtAllowanceGTotalUnits=0;
		
		if(PackID==11){
			
			long[] RawCasesUnits1;
			long TotalUnits1=0;
			ResultSet rss2 = s.executeQuery("SELECT bieoi.total_units FROM "+ds.logDatabaseName()+".bi_empty_other_issuance bieoi where bieoi.document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bieoi.package_id in (11)  and bieoi.reason in('10')");
			while(rss2.next()){
				TotalUnits1 += rss2.getInt("total_units");
				
			}
			
			RawCasesUnits1 = Utilities.getRawCasesAndUnits(TotalUnits1, this.getUnitPerSKU250());
			
			ZabtAllowanceGTotalRawCases+=RawCasesUnits1[0];
			ZabtAllowanceGTotalUnits+=RawCasesUnits1[1];
			
		}else if(PackID==1){
			long[] RawCasesUnits1;
			long TotalUnits1=0;
			ResultSet rss3 = s.executeQuery("SELECT bieoi.total_units FROM "+ds.logDatabaseName()+".bi_empty_other_issuance bieoi where bieoi.document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bieoi.package_id in (1)  and bieoi.reason in('10')");
			while(rss3.next()){
				
				
				
				
				
			
				TotalUnits1 += rss3.getInt("total_units");
				
				
			}
			RawCasesUnits1 = Utilities.getRawCasesAndUnits(TotalUnits1, this.getUnitPerSKU1000());
			
			ZabtAllowanceGTotalRawCases+=RawCasesUnits1[0];
			ZabtAllowanceGTotalUnits+=RawCasesUnits1[1];
		}
		
		s.close();
		
		ds.dropConnection();
		
		return ZabtAllowanceGTotalRawCases;
		
	}
	
	
	public long getBreakageAllowance(Date StartDate, Date EndDate, int PackID) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		Datasource ds = new Datasource();
		ds.createConnection();
		
		Statement s = ds.createStatement();
		
		long BreakageAllowanceGTotalRawCases=0;
		long BreakageAllowanceGTotalUnits=0;
		
		if(PackID==11){
			long[] RawCasesUnits1;
			long TotalUnits1=0;
			ResultSet rss2 = s.executeQuery("SELECT bieoi.total_units FROM "+ds.logDatabaseName()+".bi_empty_other_issuance bieoi where bieoi.document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bieoi.package_id in (11)  and bieoi.reason in('11')");
			while(rss2.next()){
				
				
				
				
				
				
				TotalUnits1 += rss2.getInt("total_units");
				
				
			}
			RawCasesUnits1 = Utilities.getRawCasesAndUnits(TotalUnits1, this.getUnitPerSKU250());
			
			BreakageAllowanceGTotalRawCases+=RawCasesUnits1[0];
			BreakageAllowanceGTotalUnits+=RawCasesUnits1[1];
			
		}else if(PackID==1){
			long[] RawCasesUnits1;
			long TotalUnits1=0;
			ResultSet rss3 = s.executeQuery("SELECT bieoi.total_units FROM "+ds.logDatabaseName()+".bi_empty_other_issuance bieoi where bieoi.document_date between "+Utilities.getSQLDate(StartDate)+" and "+Utilities.getSQLDate(EndDate)+" and bieoi.package_id in (1)  and bieoi.reason in('11')");
			while(rss3.next()){
				
				
			
				
				
			
				TotalUnits1 += rss3.getInt("total_units");
				
				
			}
			RawCasesUnits1 = Utilities.getRawCasesAndUnits(TotalUnits1, this.getUnitPerSKU1000());
			
			BreakageAllowanceGTotalRawCases+=RawCasesUnits1[0];
			BreakageAllowanceGTotalUnits+=RawCasesUnits1[1];
		}
		
		s.close();
		
		ds.dropConnection();
		
		return BreakageAllowanceGTotalRawCases;
		
	}
	
	
	
	
}
