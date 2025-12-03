package com.pbc.sap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class SyncSalesOrdersAndInvoices {

	public static void main(String args[]) {

		SAPUtilities obj = new SAPUtilities();
		obj.connectPRD();
		
		try {
			
			
			
			Date Today = new Date();
			Date Yesterday = Utilities.getDateByDays(-1); 
	    	
	    	
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			//JCoTable tab[] = obj.getSalesOrdersInvoices("20140903", "20140904");
			Date StartDate = Yesterday;
			Date EndDate = Today;
			
			if (args.length > 0){
				StartDate = Utilities.parseDate(args[0]);
				EndDate = Utilities.parseDate(args[1]);
			}
			
			System.out.println("SyncSalesOrdersAndInvoices "+Utilities.getSQLDateWithoutSeprator(StartDate)+" "+Utilities.getSQLDateWithoutSeprator(EndDate));
			
			JCoTable tab[] = obj.getSalesOrdersInvoices(Utilities.getSQLDateWithoutSeprator(StartDate), Utilities.getSQLDateWithoutSeprator(EndDate));
			

			// tab[0]
			

			System.out.println("Synchronzation started for VBRK");
			
			tab[0].firstRow();
			
			for(int i = 0; i < tab[0].getNumRows(); i++){
				
				
				int MANDT =tab[0].getInt("MANDT");
				String VBELN=Utilities.filterString(tab[0].getString("VBELN"), 1, 100); 
				String FKART=Utilities.filterString(tab[0].getString("FKART"), 1, 100); 
				String FKTYP=Utilities.filterString(tab[0].getString("FKTYP"), 1, 100); 
				String VBTYP=Utilities.filterString(tab[0].getString("VBTYP"), 1, 100); 
				String WAERK=Utilities.filterString(tab[0].getString("WAERK"), 1, 100);
				String VKORG=Utilities.filterString(tab[0].getString("VKORG"), 1, 100); 
				String VTWEG=Utilities.filterString(tab[0].getString("VTWEG"), 1, 100); 
				String KALSM=Utilities.filterString(tab[0].getString("KALSM"), 1, 100); 
				String KNUMV=Utilities.filterString(tab[0].getString("KNUMV"), 1, 100);
				String VSBED=Utilities.filterString(tab[0].getString("VSBED"), 1, 100);
				
				//java.util.Date utilDate = new java.util.Date();
			    //java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				
				String FKDAT = Utilities.getSQLDate(tab[0].getDate("FKDAT"));
				
				//System.out.println("Tab[0] FKDAT "+tab[0].getDate("FKDAT"));
				//System.out.println("FKDAT "+FKDAT);
				
				String BELNR =Utilities.filterString(tab[0].getString("BELNR"), 1, 100); 
				int GJAHR= tab[0].getInt("GJAHR"); 
				int POPER=tab[0].getInt("POPER"); 
				String KONDA=Utilities.filterString(tab[0].getString("KONDA"), 1, 100); 
				String KDGRP=Utilities.filterString(tab[0].getString("KDGRP"), 1, 100); 
				String BZIRK=Utilities.filterString(tab[0].getString("BZIRK"), 1, 100); 
				String PLTYP=Utilities.filterString(tab[0].getString("PLTYP"), 1, 100); 
				String INCO1=Utilities.filterString(tab[0].getString("INCO1"), 1, 100); 
				String INCO2=Utilities.filterString(tab[0].getString("INCO2"), 1, 100); 
				String EXPKZ=Utilities.filterString(tab[0].getString("EXPKZ"), 1, 100); 
				String RFBSK=Utilities.filterString(tab[0].getString("RFBSK"), 1, 100); 
				String MRNKZ=Utilities.filterString(tab[0].getString("MRNKZ"), 1, 100); 
				Double KURRF=tab[0].getDouble("KURRF"); 
				String CPKUR=Utilities.filterString(tab[0].getString("CPKUR"), 1, 100); 
				int VALTG=tab[0].getInt("VALTG"); 
				String VALDT=Utilities.getSQLDate(tab[0].getDate("VALDT")); 
				String ZTERM=Utilities.filterString(tab[0].getString("ZTERM"), 1, 100); 
				String ZLSCH=Utilities.filterString(tab[0].getString("ZLSCH"), 1, 100); 
				String KTGRD=Utilities.filterString(tab[0].getString("KTGRD"), 1, 100); 
				String LAND1=Utilities.filterString(tab[0].getString("LAND1"), 1, 100); 
				String REGIO=Utilities.filterString(tab[0].getString("REGIO"), 1, 100); 
				String COUNC=Utilities.filterString(tab[0].getString("COUNC"), 1, 100); 
				String CITYC=Utilities.filterString(tab[0].getString("CITYC"), 1, 100); 
				String BUKRS=Utilities.filterString(tab[0].getString("BUKRS"), 1, 100);
				String TAXK1=Utilities.filterString(tab[0].getString("TAXK1"), 1, 100); 
				String TAXK2=Utilities.filterString(tab[0].getString("TAXK2"), 1, 100); 
				String TAXK3=Utilities.filterString(tab[0].getString("TAXK3"), 1, 100); 
				String TAXK4=Utilities.filterString(tab[0].getString("TAXK4"), 1, 100); 
				String TAXK5=Utilities.filterString(tab[0].getString("TAXK5"), 1, 100); 
				String TAXK6=Utilities.filterString(tab[0].getString("TAXK6"), 1, 100); 
				String TAXK7=Utilities.filterString(tab[0].getString("TAXK7"), 1, 100); 
				String TAXK8=Utilities.filterString(tab[0].getString("TAXK8"), 1, 100);
				String TAXK9=Utilities.filterString(tab[0].getString("TAXK9"), 1, 100); 
				String NETWR=Utilities.filterString(tab[0].getString("NETWR"), 1, 100); 
				String ZUKRI=Utilities.filterString(tab[0].getString("ZUKRI"), 1, 100); 
				String ERNAM=Utilities.filterString(tab[0].getString("ERNAM"), 1, 100); 
				String ERZET=Utilities.filterString(tab[0].getString("ERZET"), 1, 100); 
				String ERDAT=Utilities.getSQLDate(tab[0].getDate("ERDAT")); 
				String STAFO=Utilities.filterString(tab[0].getString("STAFO"), 1, 100); 
				String KUNRG=Utilities.filterString(tab[0].getString("KUNRG"), 1, 100); 
				String KUNAG=Utilities.filterString(tab[0].getString("KUNAG"), 1, 100);
				String MABER=Utilities.filterString(tab[0].getString("MABER"), 1, 100); 
				String STWAE=Utilities.filterString(tab[0].getString("STWAE"), 1, 100);
				String EXNUM=Utilities.filterString(tab[0].getString("EXNUM"), 1, 100); 
				String STCEG=Utilities.filterString(tab[0].getString("STCEG"), 1, 100);
				String AEDAT=Utilities.getSQLDate(tab[0].getDate("AEDAT")); 
				String SFAKN=Utilities.filterString(tab[0].getString("SFAKN"), 1, 100); 
				String KNUMA=Utilities.filterString(tab[0].getString("KNUMA"), 1, 100); 
				String FKART_RL=Utilities.filterString(tab[0].getString("FKART_RL"), 1, 100); 
				String FKDAT_RL=Utilities.getSQLDate(tab[0].getDate("FKDAT_RL")); 
				String KURST=Utilities.filterString(tab[0].getString("KURST"), 1, 100); 
				String MSCHL=Utilities.filterString(tab[0].getString("MSCHL"), 1, 100);
				String MANSP=Utilities.filterString(tab[0].getString("MANSP"), 1, 100); 
				String SPART=Utilities.filterString(tab[0].getString("SPART"), 1, 100); 
				String KKBER=Utilities.filterString(tab[0].getString("KKBER"), 1, 100); 
				String KNKLI=Utilities.filterString(tab[0].getString("KNKLI"), 1, 100); 
				String CMWAE=Utilities.filterString(tab[0].getString("CMWAE"), 1, 100); 
				double CMKUF=tab[0].getDouble("CMKUF"); 
				String HITYP_PR=Utilities.filterString(tab[0].getString("HITYP_PR"), 1, 100); 
				//String BSTNK_VF=Utilities.filterString(tab[0].getString("BSTKD "), 1, 100);
				String VBUND=Utilities.filterString(tab[0].getString("VBUND"), 1, 100); 
				String FKART_AB=Utilities.filterString(tab[0].getString("FKART_AB"), 1, 100); 
				//String KAPPL=Utilities.filterString(tab[0].getString("KAPPL "), 1, 100); 
				String LANDTX=Utilities.filterString(tab[0].getString("LANDTX"), 1, 100); 
				String STCEG_H=Utilities.filterString(tab[0].getString("STCEG_H"), 1, 100); 
				String STCEG_L=Utilities.filterString(tab[0].getString("STCEG_L"), 1, 100); 
				String XBLNR=Utilities.filterString(tab[0].getString("XBLNR"), 1, 100); 
				String ZUONR=Utilities.filterString(tab[0].getString("ZUONR"), 1, 100); 
				double MWSBK=tab[0].getDouble("MWSBK"); 
				String LOGSYS=Utilities.filterString(tab[0].getString("LOGSYS"), 1, 100); 
				String FKSTO=Utilities.filterString(tab[0].getString("FKSTO"), 1, 100); 
				String XEGDR=Utilities.filterString(tab[0].getString("XEGDR"), 1, 100); 
				String RPLNR=Utilities.filterString(tab[0].getString("RPLNR"), 1, 100); 
				String LCNUM=Utilities.filterString(tab[0].getString("LCNUM"), 1, 100); 
				String J_1AFITP=Utilities.filterString(tab[0].getString("J_1AFITP"), 1, 100); 
				String KURRF_DAT=Utilities.getSQLDate(tab[0].getDate("KURRF_DAT")); 
				int AKWAE=tab[0].getInt("AKWAE"); 
				double AKKUR=tab[0].getDouble("AKKUR"); 
				String KIDNO=Utilities.filterString(tab[0].getString("KIDNO"), 1, 100); 
				String BVTYP=Utilities.filterString(tab[0].getString("BVTYP"), 1, 100); 
				int NUMPG=tab[0].getInt("NUMPG"); 
				String BUPLA=Utilities.filterString(tab[0].getString("BUPLA"), 1, 100); 
				String VKONT=Utilities.filterString(tab[0].getString("VKONT"), 1, 100);
				//String FKK_DOCSTAT=Utilities.filterString(tab[0].getString("FKK_DOCSTAT "), 1, 100); 
				String NRZAS=Utilities.filterString(tab[0].getString("NRZAS"), 1, 100);
				//String ZCDS_STATUS=Utilities.filterString(tab[0].getString("ZCDS_STATUS"), 1, 100); 
				//String ZZCDS_STATUS=Utilities.filterString(tab[0].getString("ZZCDS_STATUS"), 1, 100); 
				//String ZZVBRK_APPEND=Utilities.filterString(tab[0].getString("ZZVBRK_APPEND"), 1, 100); 
				//Date ZZINTACTAR=tab[0].getDate("ZZINTACTAR"); 
				
				s.executeUpdate("replace INTO sap_vbrk(MANDT,VBELN,FKART,FKTYP,VBTYP,WAERK,VKORG,VTWEG,KALSM,KNUMV,VSBED,FKDAT,BELNR,GJAHR, "+
"POPER,KONDA,KDGRP,BZIRK,PLTYP,INCO1,INCO2,EXPKZ,RFBSK,MRNKZ,KURRF,CPKUR,VALTG,VALDT,ZTERM,ZLSCH,KTGRD,LAND1,REGIO,COUNC,CITYC,BUKRS,TAXK1,TAXK2, "+
"TAXK3,TAXK4,TAXK5,TAXK6,TAXK7,TAXK8,TAXK9,NETWR,ZUKRI,ERNAM,ERZET,ERDAT,STAFO,KUNRG,KUNAG,MABER,STWAE,EXNUM,STCEG,AEDAT,SFAKN,KNUMA,FKART_RL, "+
"FKDAT_RL,KURST,MSCHL,MANSP,SPART,KKBER,KNKLI,CMWAE,CMKUF,HITYP_PR,BSTNK_VF,VBUND,FKART_AB,KAPPL,LANDTX,STCEG_H,STCEG_L,XBLNR,ZUONR,MWSBK,LOGSYS, "+
"FKSTO,XEGDR,RPLNR,LCNUM,J_1AFITP,KURRF_DAT,AKWAE,AKKUR,KIDNO,BVTYP,NUMPG,BUPLA,VKONT,FKK_DOCSTAT,NRZAS) "+
"VALUES("+MANDT+",'"+VBELN+"','"+FKART+"','"+FKTYP+"','"+VBTYP+"','"+WAERK+"','"+VKORG+"','"+VTWEG+"','"+KALSM+"','"+KNUMV+"','"+VSBED+"',"+FKDAT+",'"+BELNR+"',"+GJAHR+","+
POPER+",'"+KONDA+"','"+KDGRP+"','"+BZIRK+"','"+PLTYP+"','"+INCO1+"','"+INCO2+"','"+EXPKZ+"','"+RFBSK+"','"+MRNKZ+"',"+KURRF+",'"+CPKUR+"',"+VALTG+","+VALDT+",'"+ZTERM+"','"+ZLSCH+"','"+KTGRD+"','"+LAND1+"','"+REGIO+"','"+COUNC+"','"+CITYC+"','"+BUKRS+"','"+TAXK1+"','"+TAXK2+"','"+
TAXK3+"','"+TAXK4+"','"+TAXK5+"','"+TAXK6+"','"+TAXK7+"','"+TAXK8+"','"+TAXK9+"','"+NETWR+"','"+ZUKRI+"','"+ERNAM+"','"+ERZET+"',"+ERDAT+",'"+STAFO+"','"+KUNRG+"','"+KUNAG+"','"+MABER+"','"+STWAE+"','"+EXNUM+"','"+STCEG+"',"+AEDAT+",'"+SFAKN+"','"+KNUMA+"','"+FKART_RL+"',"+
FKDAT_RL+",'"+KURST+"','"+MSCHL+"','"+MANSP+"','"+SPART+"','"+KKBER+"','"+KNKLI+"','"+CMWAE+"',"+CMKUF+",'"+HITYP_PR+"','','"+VBUND+"','"+FKART_AB+"','','"+LANDTX+"','"+STCEG_H+"','"+STCEG_L+"','"+XBLNR+"','"+ZUONR+"',"+MWSBK+",'"+LOGSYS+"','"+
FKSTO+"','"+XEGDR+"','"+RPLNR+"','"+LCNUM+"','"+J_1AFITP+"',"+KURRF_DAT+","+AKWAE+","+AKKUR+",'"+KIDNO+"','"+BVTYP+"',"+NUMPG+",'"+BUPLA+"','"+VKONT+"','','"+NRZAS+"')");
				
				/*
				
				
				String SPRAS = Utilities.filterString(tab[0].getString("SPRAS"), 1, 100);
				String KOKRS = Utilities.filterString(tab[0].getString("KOKRS"), 1, 100);
				long KOSTL = tab[0].getLong("KOSTL");
				String DATBI = Utilities.filterString(tab[0].getString("DATBI"), 1, 100);
				String KTEXT = Utilities.filterString(tab[0].getString("KTEXT"), 1, 100);
				String LTEXT = Utilities.filterString(tab[0].getString("LTEXT"), 1, 100);
				String MCTXT = Utilities.filterString(tab[0].getString("MCTXT"), 1, 100);
				*/
				
				/*
				s.executeUpdate("REPLACE INTO VBRK(mandt,spras,kokrs,kostl,datbi,ktext,ltext,mctxt)VALUES("+MANDT+",'"+SPRAS+"','"+KOKRS+"',"+KOSTL+",'"+DATBI+"','"+KTEXT+"','"+LTEXT+"','"+MCTXT+"')");
				*/
				
				tab[0].setRow(i+1);
			}
			// end tab[0]
			
			
			
			System.out.println("Done.");
			
			
			
			
			
			
			// tab[1]

						System.out.println("Synchronzation started for VBRP");
						
						tab[1].firstRow();
						
						for(int i = 0; i < tab[1].getNumRows(); i++){
							
							  int MANDT=tab[1].getInt("MANDT"); 
							  String VBELN=Utilities.filterString(tab[1].getString("VBELN"), 1, 100); 
							  int POSNR=tab[1].getInt("POSNR"); 
							  int UEPOS=tab[1].getInt("UEPOS"); 
							  double FKIMG=tab[1].getDouble("FKIMG"); 
							  String VRKME=Utilities.filterString(tab[1].getString("VRKME"), 1, 100); 
							  double UMVKZ=tab[1].getDouble("UMVKZ"); 
							  double UMVKN=tab[1].getDouble("UMVKN"); 
							  String MEINS=Utilities.filterString(tab[1].getString("MEINS"), 1, 100); 
							  double SMENG=tab[1].getDouble("SMENG"); 
							  double FKLMG=tab[1].getDouble("FKLMG"); 
							  double LMENG=tab[1].getDouble("LMENG"); 
							  double NTGEW=tab[1].getDouble("NTGEW"); 
							  double BRGEW=tab[1].getDouble("BRGEW"); 
							  String GEWEI=Utilities.filterString(tab[1].getString("GEWEI"),1,100); 
							  double VOLUM=tab[1].getDouble("VOLUM"); 
							  int VOLEH=tab[1].getInt("VOLEH"); 
							  String GSBER=Utilities.filterString(tab[1].getString("GSBER"), 1, 100); 
							  String PRSDT=Utilities.getSQLDate(tab[1].getDate("PRSDT")); 
							  String FBUDA=Utilities.getSQLDate(tab[1].getDate("FBUDA")); 
							  double KURSK=tab[1].getDouble("KURSK"); 
							  double NETWR=tab[1].getDouble("NETWR"); 
							  String VBELV=Utilities.filterString(tab[1].getString("VBELV"), 1, 100); 
							  int POSNV=tab[1].getInt("POSNV"); 
							  String VGBEL=Utilities.filterString(tab[1].getString("VGBEL"), 1, 100); 
							  int VGPOS=tab[1].getInt("VGPOS"); 
							  String VGTYP=Utilities.filterString(tab[1].getString("VGTYP"), 1, 100); 
							  String AUBEL=Utilities.filterString(tab[1].getString("AUBEL"), 1, 100); 
							  int AUPOS=tab[1].getInt("AUPOS"); 
							  String AUREF=Utilities.filterString(tab[1].getString("AUREF"), 1, 100); 
							  String MATNR=Utilities.filterString(tab[1].getString("MATNR"), 1, 100); 
							  String ARKTX=Utilities.filterString(tab[1].getString("ARKTX"), 1, 100); 
							  String PMATN=Utilities.filterString(tab[1].getString("PMATN"), 1, 100); 
							  String CHARG=Utilities.filterString(tab[1].getString("CHARG"), 1, 100);
							  String MATKL=Utilities.filterString(tab[1].getString("MATKL"), 1, 100); 
							  String PSTYV=Utilities.filterString(tab[1].getString("PSTYV"), 1, 100); 
							  String POSAR=Utilities.filterString(tab[1].getString("POSAR"), 1, 100); 
							  String PRODH=Utilities.filterString(tab[1].getString("PRODH"), 1, 100); 
							  String VSTEL=Utilities.filterString(tab[1].getString("VSTEL"), 1, 100); 
							  String ATPKZ=Utilities.filterString(tab[1].getString("ATPKZ"), 1, 100);
							 // String SPART=Utilities.filterString(tab[1].getString(" SPART"), 1, 100); 
							  int POSPA=tab[1].getInt("POSPA"); 
							  String WERKS=Utilities.filterString(tab[1].getString("WERKS"), 1, 100); 
							  String ALAND=Utilities.filterString(tab[1].getString("ALAND"), 1, 100); 
							  String WKREG=Utilities.filterString(tab[1].getString("WKREG"), 1, 100); 
							  String WKCOU=Utilities.filterString(tab[1].getString("WKCOU"), 1, 100);
							  String WKCTY=Utilities.filterString(tab[1].getString("WKCTY"), 1, 100); 
							  String TAXM1=Utilities.filterString(tab[1].getString("TAXM1"), 1, 100); 
							  String TAXM2=Utilities.filterString(tab[1].getString("TAXM2"), 1, 100); 
							  String TAXM3=Utilities.filterString(tab[1].getString("TAXM3"), 1, 100); 
							  String TAXM4=Utilities.filterString(tab[1].getString("TAXM4"), 1, 100);
							  String TAXM5 =Utilities.filterString(tab[1].getString("TAXM5"), 1, 100); 
							  String TAXM6=Utilities.filterString(tab[1].getString("TAXM6"), 1, 100); 
							  String TAXM7=Utilities.filterString(tab[1].getString("TAXM7"), 1, 100); 
							  String TAXM8=Utilities.filterString(tab[1].getString("TAXM8"), 1, 100); 
							  String TAXM9=Utilities.filterString(tab[1].getString("TAXM9"), 1, 100); 
							  String KOWRR=Utilities.filterString(tab[1].getString("KOWRR"), 1, 100); 
							  String PRSFD=Utilities.filterString(tab[1].getString("PRSFD"), 1, 100); 
							  String SKTOF=Utilities.filterString(tab[1].getString("SKTOF"), 1, 100); 
							  String SKFBP=Utilities.filterString(tab[1].getString("SKFBP"), 1, 100); 
							  String KONDM=Utilities.filterString(tab[1].getString("KONDM"), 1, 100); 
							  String KTGRM=Utilities.filterString(tab[1].getString("KTGRM"), 1, 100); 
							  String KOSTL=Utilities.filterString(tab[1].getString("KOSTL"), 1, 100); 
							  String BONUS=Utilities.filterString(tab[1].getString("BONUS"), 1, 100); 
							  String PROVG=Utilities.filterString(tab[1].getString("PROVG"), 1, 100); 
							  String EANNR=Utilities.filterString(tab[1].getString("EANNR"), 1, 100); 
							  String VKGRP=Utilities.filterString(tab[1].getString("VKGRP"), 1, 100); 
							  String VKBUR=Utilities.filterString(tab[1].getString("VKBUR"), 1, 100); 
							  String SPARA=Utilities.filterString(tab[1].getString("SPARA"), 1, 100); 
							  String SHKZG=Utilities.filterString(tab[1].getString("SHKZG"), 1, 100); 
							  String ERNAM=Utilities.filterString(tab[1].getString("ERNAM"), 1, 100); 
							  String ERDAT=Utilities.getSQLDate(tab[1].getDate("ERDAT"));
							  String ERZET=Utilities.filterString(tab[1].getString("ERZET"), 1, 100); 
							  String BWTAR=Utilities.filterString(tab[1].getString("BWTAR"), 1, 100); 
							  String LGORT=Utilities.filterString(tab[1].getString("LGORT"), 1, 100); 
							  //String STAFO=Utilities.filterString(tab[1].getString("STAFO "), 1, 100); 
							  double WAVWR=tab[1].getDouble("WAVWR");
							  double KZWI1=tab[1].getDouble("KZWI1"); 
							  double KZWI2=tab[1].getDouble("KZWI2"); 
							  double KZWI3=tab[1].getDouble("KZWI3");
							  double KZWI4=tab[1].getDouble("KZWI4");
							  double KZWI5=tab[1].getDouble("KZWI5"); 
							  double KZWI6=tab[1].getDouble("KZWI6"); 
							  double STCUR=tab[1].getDouble("STCUR"); 
							  String UVPRS=Utilities.filterString(tab[1].getString("UVPRS"), 1, 100); 
							  String UVALL=Utilities.filterString(tab[1].getString("UVALL"), 1, 100); 
							  String EAN11=Utilities.filterString(tab[1].getString("EAN11"), 1, 100); 
							  String PRCTR=Utilities.filterString(tab[1].getString("PRCTR"), 1, 100); 
							  String KVGR1=Utilities.filterString(tab[1].getString("KVGR1"), 1, 100); 
							  String KVGR2=Utilities.filterString(tab[1].getString("KVGR2"), 1, 100);
							  String KVGR3=Utilities.filterString(tab[1].getString("KVGR3"), 1, 100); 
							  String KVGR4=Utilities.filterString(tab[1].getString("KVGR4"), 1, 100); 
							  String KVGR5=Utilities.filterString(tab[1].getString("KVGR5"), 1, 100);
							  String MVGR1=Utilities.filterString(tab[1].getString("MVGR1"), 1, 100); 
							  String MVGR2=Utilities.filterString(tab[1].getString("MVGR2"), 1, 100);
							  String MVGR3=Utilities.filterString(tab[1].getString("MVGR3"), 1, 100); 
							  String MVGR4=Utilities.filterString(tab[1].getString("MVGR4"), 1, 100); 
							  String MVGR5=Utilities.filterString(tab[1].getString("MVGR5"), 1, 100); 
							  String MATWA=Utilities.filterString(tab[1].getString("MATWA"), 1, 100); 
							  double BONBA=tab[1].getDouble("BONBA"); 
							  String KOKRS=Utilities.filterString(tab[1].getString("KOKRS"), 1, 100); 
							  int PAOBJNR=tab[1].getInt("PAOBJNR"); 
							  int PS_PSP_PNR=tab[1].getInt("PS_PSP_PNR"); 
							  String AUFNR=Utilities.filterString(tab[1].getString("AUFNR"), 1, 100); 
							  String TXJCD=Utilities.filterString(tab[1].getString("TXJCD"), 1, 100);
							  double CMPRE=tab[1].getDouble("CMPRE"); 
							  String CMPNT=Utilities.filterString(tab[1].getString("CMPNT"), 1, 100);
							  int CUOBJ=tab[1].getInt("CUOBJ");
							  int CUOBJ_CH=tab[1].getInt("CUOBJ_CH"); 
							  String KOUPD=Utilities.filterString(tab[1].getString("KOUPD"), 1, 100);
							  int UECHA=tab[1].getInt("UECHA"); 
							  String XCHAR=Utilities.filterString(tab[1].getString("XCHAR"), 1, 100); 
							  String ABRVW=Utilities.filterString(tab[1].getString("ABRVW"), 1, 100); 
							  String SERNR=Utilities.filterString(tab[1].getString("SERNR"), 1, 100); 
							  String BZIRK_AUFT=Utilities.filterString(tab[1].getString("BZIRK_AUFT"), 1, 100); 
							  String KDGRP_AUFT=Utilities.filterString(tab[1].getString("KDGRP_AUFT"), 1, 100); 
							  String KONDA_AUFT=Utilities.filterString(tab[1].getString("KONDA_AUFT"), 1, 100);
							  String LLAND_AUFT=Utilities.filterString(tab[1].getString("LLAND_AUFT"), 1, 100); 
							  String MPROK=Utilities.filterString(tab[1].getString("MPROK"), 1, 100);
							  String PLTYP_AUFT=Utilities.filterString(tab[1].getString("PLTYP_AUFT"), 1, 100); 
							  String REGIO_AUFT=Utilities.filterString(tab[1].getString("REGIO_AUFT"), 1, 100); 
							  String VKORG_AUFT=Utilities.filterString(tab[1].getString("VKORG_AUFT"), 1, 100); 
							  String VTWEG_AUFT=Utilities.filterString(tab[1].getString("VTWEG_AUFT"), 1, 100);
							  String ABRBG=Utilities.filterString(tab[1].getString("ABRBG"), 1, 100); //Date
							  String PROSA=Utilities.filterString(tab[1].getString("PROSA"), 1, 100); 
							  String UEPVW=Utilities.filterString(tab[1].getString("UEPVW"), 1, 100);
							  String AUTYP=Utilities.filterString(tab[1].getString("AUTYP"), 1, 100); 
							  String STADAT=Utilities.getSQLDate(tab[1].getDate("STADAT"));
							  String FPLNR=Utilities.filterString(tab[1].getString("FPLNR"), 1, 100); 
							  int FPLTR=tab[1].getInt("FPLTR"); 
							  String AKTNR=Utilities.filterString(tab[1].getString("AKTNR"), 1, 100); 
							  String KNUMA_PI=Utilities.filterString(tab[1].getString("KNUMA_PI"), 1, 100); 
							  String KNUMA_AG=Utilities.filterString(tab[1].getString("KNUMA_AG"), 1, 100); 
							  String PREFE=Utilities.filterString(tab[1].getString("PREFE"), 1, 100); 
							  String MWSBP=Utilities.filterString(tab[1].getString("MWSBP"), 1, 100); 
							  String AUGRU_AUFT=Utilities.filterString(tab[1].getString("AUGRU_AUFT"), 1, 100); 
							  String FAREG=Utilities.filterString(tab[1].getString("FAREG"), 1, 100); 
							  String UPMAT=Utilities.filterString(tab[1].getString("UPMAT"), 1, 100); 
							  String UKONM=Utilities.filterString(tab[1].getString("UKONM"), 1, 100);
							  double CMPRE_FLT=tab[1].getDouble("CMPRE_FLT"); 
							  String ABFOR=Utilities.filterString(tab[1].getString("ABFOR"), 1, 100); 
							  double ABGES=tab[1].getDouble("ABGES"); 
							  String J_1ARFZ=Utilities.filterString(tab[1].getString("J_1ARFZ"), 1, 100); 
							  String J_1AREGIO=Utilities.filterString(tab[1].getString("J_1AREGIO"), 1, 100); 
							  String J_1AGICD=Utilities.filterString(tab[1].getString("J_1AGICD"), 1, 100); 
							  String J_1ADTYP=Utilities.filterString(tab[1].getString("J_1ADTYP"), 1, 100); 
							  String J_1ATXREL=Utilities.filterString(tab[1].getString("J_1ATXREL"), 1, 100); 
							  String J_1BCFOP=Utilities.filterString(tab[1].getString("J_1BCFOP"), 1, 100); 
							  String J_1BTAXLW1=Utilities.filterString(tab[1].getString("J_1BTAXLW1"), 1, 100); 
							  String J_1BTAXLW2=Utilities.filterString(tab[1].getString("J_1BTAXLW2"), 1, 100); 
							  String J_1BTXSDC=Utilities.filterString(tab[1].getString("J_1BTXSDC"), 1, 100); 
							  double BRTWR=tab[1].getDouble("BRTWR"); 
							  String WKTNR=Utilities.filterString(tab[1].getString("WKTNR"), 1, 100); 
							  int WKTPS=tab[1].getInt("WKTPS"); 
							  String RPLNR=Utilities.filterString(tab[1].getString("RPLNR"), 1, 100); 
							  String KURSK_DAT=Utilities.getSQLDate(tab[1].getDate("KURSK_DAT"));
							  String WGRU1=Utilities.filterString(tab[1].getString("WGRU1"), 1, 100);
							  String WGRU2=Utilities.filterString(tab[1].getString("WGRU2"), 1, 100); 
							  String KDKG1=Utilities.filterString(tab[1].getString("KDKG1"), 1, 100);
							  String KDKG2=Utilities.filterString(tab[1].getString("KDKG2"), 1, 100);
							  String KDKG3=Utilities.filterString(tab[1].getString("KDKG3"), 1, 100);
							  String KDKG4=Utilities.filterString(tab[1].getString("KDKG4"), 1, 100); 
							  String KDKG5=Utilities.filterString(tab[1].getString("KDKG5"), 1, 100);
							  String VKAUS=Utilities.filterString(tab[1].getString("VKAUS"), 1, 100); 
							  String J_1AINDXP=Utilities.filterString(tab[1].getString("J_1AINDXP"), 1, 100); 
							  String J_1AIDATEP=Utilities.getSQLDate(tab[1].getDate("J_1AIDATEP")); 
							  String KZFME=Utilities.filterString(tab[1].getString("KZFME"), 1, 100);
							  String MWSKZ=Utilities.filterString(tab[1].getString("MWSKZ"), 1, 100);
							  String VERTT=Utilities.filterString(tab[1].getString("VERTT"), 1, 100); 
							  String VERTN=Utilities.filterString(tab[1].getString("VERTN"), 1, 100); 
							  String SGTXT=Utilities.filterString(tab[1].getString("SGTXT"), 1, 100); 
							  String DELCO=Utilities.filterString(tab[1].getString("DELCO"), 1, 100); 
							  String BEMOT=Utilities.filterString(tab[1].getString("BEMOT"), 1, 100); 
							  String RRREL=Utilities.filterString(tab[1].getString("RRREL"), 1, 100); 
							  double AKKUR=tab[1].getDouble("AKKUR"); 
							  String WMINR=Utilities.filterString(tab[1].getString("WMINR"), 1, 100); 
							  String VGBEL_EX=Utilities.filterString(tab[1].getString("VGBEL_EX"), 1, 100); 
							  int VGPOS_EX=tab[1].getInt("VGPOS_EX"); 
							  String LOGSYS=Utilities.filterString(tab[1].getString("LOGSYS"), 1, 100); 
							  String VGTYP_EX=Utilities.filterString(tab[1].getString("VGTYP_EX"), 1, 100); 
							  String J_1BTAXLW3=Utilities.filterString(tab[1].getString("J_1BTAXLW3"), 1, 100); 
							  //String GMADDONVBRP=Utilities.filterString(tab[1].getString("GMADDONVBRP"), 1, 100);
							  String FONDS=Utilities.filterString(tab[1].getString("FONDS"), 1, 100); 
							  String FISTL=Utilities.filterString(tab[1].getString("FISTL"), 1, 100); 
							  String FKBER=Utilities.filterString(tab[1].getString("FKBER"), 1, 100); 
							  String GRANT_NBR=Utilities.filterString(tab[1].getString("GRANT_NBR"), 1, 100);
							  
							  //String VBRP_CMPD=Utilities.filterString(tab[1].getString("VBRP_CMPD"), 1, 100); 
							  String CAMPAIGN=Utilities.filterString(tab[1].getString("CAMPAIGN"), 1, 100);
							
							
							
							
							s.executeUpdate("replace INTO sap_vbrp(MANDT,VBELN,POSNR,UEPOS,FKIMG,VRKME,UMVKZ,UMVKN,MEINS,SMENG,FKLMG,LMENG,NTGEW,BRGEW,GEWEI,"+
									  "VOLUM,VOLEH,GSBER,PRSDT,FBUDA,KURSK,NETWR,VBELV,POSNV,VGBEL,VGPOS,VGTYP, AUBEL,AUPOS,AUREF,MATNR,ARKTX,PMATN,CHARG,"+
									  "MATKL,PSTYV,POSAR,PRODH,VSTEL,ATPKZ,POSPA,WERKS,ALAND,WKREG,WKCOU,WKCTY,TAXM1,TAXM2,TAXM3,TAXM4,TAXM5,TAXM6,TAXM7,"+
									  "TAXM8,TAXM9,KOWRR,PRSFD,SKTOF,SKFBP,KONDM,KTGRM,KOSTL,BONUS,PROVG,EANNR,VKGRP,VKBUR,SPARA,SHKZG, ERNAM,ERDAT,ERZET,BWTAR,LGORT,"+
									  "WAVWR, KZWI1,KZWI2,KZWI3,KZWI4,KZWI5,KZWI6,STCUR,UVPRS,UVALL,EAN11,PRCTR,KVGR1,KVGR2,KVGR3,KVGR4,"+
									  "KVGR5,MVGR1,MVGR2,MVGR3,MVGR4,MVGR5, MATWA,BONBA,KOKRS,PAOBJNR,PS_PSP_PNR,AUFNR,TXJCD,CMPRE, CMPNT, CUOBJ,"+
									  "CUOBJ_CH,KOUPD, UECHA,XCHAR,ABRVW,SERNR,BZIRK_AUFT,KDGRP_AUFT,KONDA_AUFT,LLAND_AUFT, MPROK, PLTYP_AUFT,REGIO_AUFT,"+
									  "VKORG_AUFT,VTWEG_AUFT, ABRBG,PROSA,UEPVW,AUTYP, STADAT,FPLNR,FPLTR,AKTNR,KNUMA_PI, KNUMA_AG,PREFE,MWSBP,AUGRU_AUFT,"+
									  "FAREG, UPMAT,UKONM,CMPRE_FLT,ABFOR,ABGES,J_1ARFZ,J_1AREGIO, J_1AGICD,J_1ADTYP,J_1ATXREL,J_1BCFOP,J_1BTAXLW1,"+
									  "J_1BTAXLW2, J_1BTXSDC, BRTWR,WKTNR,WKTPS,RPLNR,KURSK_DAT, WGRU1,WGRU2,KDKG1,KDKG2,KDKG3,KDKG4,KDKG5,"+
									  "VKAUS,J_1AINDXP,J_1AIDATEP, KZFME, MWSKZ,VERTT, VERTN, SGTXT,DELCO,BEMOT,RRREL,AKKUR,WMINR,VGBEL_EX,VGPOS_EX,"+
									  "LOGSYS, VGTYP_EX,J_1BTAXLW3, FONDS,FISTL,FKBER,GRANT_NBR,CAMPAIGN)"+
									  "VALUES"+
									  "("+MANDT+",'"+VBELN+"',"+POSNR+","+UEPOS+","+FKIMG+",'"+VRKME+"',"+UMVKZ+","+UMVKN+",'"+MEINS+"',"+SMENG+","+FKLMG+","+LMENG+","+NTGEW+","+BRGEW+",'"+GEWEI+"',"+
									  VOLUM+","+VOLEH+",'"+GSBER+"',"+PRSDT+","+FBUDA+","+KURSK+","+NETWR+",'"+VBELV+"',"+POSNV+",'"+VGBEL+"',"+VGPOS+",'"+VGTYP+"','"+ AUBEL+"',"+AUPOS+",'"+AUREF+"','"+MATNR+"','"+ARKTX+"','"+PMATN+"','"+CHARG+"','"+
									  MATKL+"','"+PSTYV+"','"+POSAR+"','"+PRODH+"','"+VSTEL+"','"+ATPKZ+"',"+POSPA+",'"+WERKS+"','"+ALAND+"','"+WKREG+"','"+WKCOU+"','"+WKCTY+"','"+TAXM1+"','"+TAXM2+"','"+TAXM3+"','"+TAXM4+"','"+TAXM5+"','"+TAXM6+"','"+TAXM7+"','"+
									  TAXM8+"','"+TAXM9+"','"+KOWRR+"','"+PRSFD+"','"+SKTOF+"','"+SKFBP+"','"+KONDM+"','"+KTGRM+"','"+KOSTL+"','"+BONUS+"','"+PROVG+"','"+EANNR+"','"+VKGRP+"','"+VKBUR+"','"+SPARA+"','"+SHKZG+"','"+ ERNAM+"',"+ERDAT+",'"+ERZET+"','"+BWTAR+"','"+LGORT+"',"+
									  +WAVWR+","+ KZWI1+","+KZWI2+","+KZWI3+","+KZWI4+","+KZWI5+","+KZWI6+","+STCUR+",'"+UVPRS+"','"+UVALL+"','"+EAN11+"','"+PRCTR+"','"+KVGR1+"','"+KVGR2+"','"+KVGR3+"','"+KVGR4+"','"+
									  KVGR5+"','"+MVGR1+"','"+MVGR2+"','"+MVGR3+"','"+MVGR4+"','"+MVGR5+"','"+ MATWA+"',"+BONBA+",'"+KOKRS+"','"+PAOBJNR+"','"+PS_PSP_PNR+"','"+AUFNR+"','"+TXJCD+"',"+CMPRE+",'"+ CMPNT+"','"+ CUOBJ+"','"+
									  CUOBJ_CH+"','"+KOUPD+"','"+ UECHA+"','"+XCHAR+"','"+ABRVW+"','"+SERNR+"','"+BZIRK_AUFT+"','"+KDGRP_AUFT+"','"+KONDA_AUFT+"','"+LLAND_AUFT+"','"+ MPROK+"','"+ PLTYP_AUFT+"','"+REGIO_AUFT+"','"+
									  VKORG_AUFT+"','"+VTWEG_AUFT+"','"+ ABRBG+"','"+PROSA+"','"+UEPVW+"','"+AUTYP+"',"+ STADAT+",'"+FPLNR+"','"+FPLTR+"','"+AKTNR+"','"+KNUMA_PI+"','"+ KNUMA_AG+"','"+PREFE+"','"+MWSBP+"','"+AUGRU_AUFT+"','"+
									  FAREG+"','"+ UPMAT+"','"+UKONM+"','"+CMPRE_FLT+"','"+ABFOR+"',"+ABGES+",'"+J_1ARFZ+"','"+J_1AREGIO+"','"+ J_1AGICD+"','"+J_1ADTYP+"','"+J_1ATXREL+"','"+J_1BCFOP+"','"+J_1BTAXLW1+"','"+
									  J_1BTAXLW2+"','"+ J_1BTXSDC+"',"+ BRTWR+",'"+WKTNR+"',"+WKTPS+",'"+RPLNR+"',"+KURSK_DAT+",'"+ WGRU1+"','"+WGRU2+"','"+KDKG1+"','"+KDKG2+"','"+KDKG3+"','"+KDKG4+"','"+KDKG5+"','"+
									  VKAUS+"','"+J_1AINDXP+"',"+J_1AIDATEP+",'"+ KZFME+"','"+ MWSKZ+"','"+VERTT+"','"+ VERTN+"','"+ SGTXT+"','"+DELCO+"','"+BEMOT+"','"+RRREL+"',"+AKKUR+",'"+WMINR+"','"+VGBEL_EX+"',"+VGPOS_EX+",'"+
									  LOGSYS+"','"+ VGTYP_EX+"','"+J_1BTAXLW3+"','"+FONDS+"','"+FISTL+"','"+FKBER+"','"+GRANT_NBR+"','"+CAMPAIGN+"')");
				
							
							
							tab[1].setRow(i+1);
						}
						// end tab[0]
						
						
						
						System.out.println("Done.");
						
						
						// tab[2]

						System.out.println("Synchronzation started for VBAK");
						
						tab[2].firstRow();
						
						for(int i = 0; i < tab[2].getNumRows(); i++){
						//for(int i = 0; i < 1; i++){
							  
							int MANDT = tab[2].getInt("MANDT"); 
							String VBELN=Utilities.filterString(tab[2].getString("VBELN"), 1, 100);
							String ERDAT=Utilities.getSQLDate(tab[2].getDate("ERDAT"));
							
							String ERZET=Utilities.filterString(tab[2].getString("ERZET"),1,100);
							String ERNAM=Utilities.filterString(tab[2].getString("ERNAM"), 1, 100);
							String ANGDT=Utilities.getSQLDate(tab[2].getDate("ANGDT")); 
							
							String BNDDT=Utilities.getSQLDate(tab[2].getDate("BNDDT")); 
							String AUDAT=Utilities.getSQLDate(tab[2].getDate("AUDAT")); 
							
							String VBTYP=Utilities.filterString(tab[2].getString("VBTYP"), 1, 100);
							String TRVOG=Utilities.filterString(tab[2].getString("TRVOG"), 1, 100);
							String AUART=Utilities.filterString(tab[2].getString("AUART"), 1, 100);
							String AUGRU=Utilities.filterString(tab[2].getString("AUGRU"), 1, 100);							
							String GWLDT=Utilities.getSQLDate(tab[2].getDate("GWLDT")); 
							
							String SUBMI=Utilities.filterString(tab[2].getString("SUBMI"), 1, 100);
							String LIFSK=Utilities.filterString(tab[2].getString("LIFSK"), 1, 100);
							String FAKSK=Utilities.filterString(tab[2].getString("FAKSK"), 1, 100);
							double NETWR=tab[2].getDouble("NETWR");
							String WAERK=Utilities.filterString(tab[2].getString("WAERK"), 1, 100);
							String VKORG=Utilities.filterString(tab[2].getString("VKORG"), 1, 100);
							String VTWEG=Utilities.filterString(tab[2].getString("VTWEG"), 1, 100);
							String SPART=Utilities.filterString(tab[2].getString("SPART"), 1, 100);
							String VKGRP=Utilities.filterString(tab[2].getString("VKGRP"), 1, 100);
							String VKBUR=Utilities.filterString(tab[2].getString("VKBUR"), 1, 100);
							String GSBER=Utilities.filterString(tab[2].getString("GSBER"), 1, 100);
							String GSKST=Utilities.filterString(tab[2].getString("GSKST"), 1, 100);
							String GUEBG=Utilities.getSQLDate(tab[2].getDate("GUEBG"));
							String GUEEN=Utilities.getSQLDate(tab[2].getDate("GUEEN"));
							String KNUMV=Utilities.filterString(tab[2].getString("KNUMV"), 1, 100);
							
							String VDATU=Utilities.getSQLDate(tab[2].getDate("VDATU")); 
							String VPRGR=Utilities.filterString(tab[2].getString("VPRGR"), 1, 100);
							String AUTLF=Utilities.filterString(tab[2].getString("AUTLF"), 1, 100);
							String VBKLA=Utilities.filterString(tab[2].getString("VBKLA"), 1, 100);
							String VBKLT=Utilities.filterString(tab[2].getString("VBKLT"), 1, 100);
							String KALSM=Utilities.filterString(tab[2].getString("KALSM"), 1, 100);
							String VSBED=Utilities.filterString(tab[2].getString("VSBED"), 1, 100);
							String FKARA=Utilities.filterString(tab[2].getString("FKARA"), 1, 100);
							int AWAHR = tab[2].getInt("AWAHR"); 
							String KTEXT=Utilities.filterString(tab[2].getString("KTEXT"), 1, 100);
							String BSTNK=Utilities.filterString(tab[2].getString("BSTNK"), 1, 100);
							String BSARK=Utilities.filterString(tab[2].getString("BSARK"), 1, 100);
							String BSTDK=Utilities.getSQLDate(tab[2].getDate("BSTDK")); 
							String BSTZD=Utilities.filterString(tab[2].getString("BSTZD"), 1, 100);
							String IHREZ=Utilities.filterString(tab[2].getString("IHREZ"), 1, 100);
							String BNAME=Utilities.filterString(tab[2].getString("BNAME"), 1, 100);
							
							
							
							String TELF1=Utilities.filterString(tab[2].getString("TELF1"), 1, 100);
							double MAHZA=tab[2].getDouble("MAHZA"); 
							//String MAHDT=Utilities.getSQLDate(tab[0].getDate("MAHDT")); 
							String KUNNR=Utilities.filterString(tab[2].getString("KUNNR"), 1, 100);
							String KOSTL=Utilities.filterString(tab[2].getString("KOSTL"), 1, 100);
							String STAFO=Utilities.filterString(tab[2].getString("STAFO"), 1, 100);
							String STWAE=Utilities.filterString(tab[2].getString("STWAE"), 1, 100);
							String AEDAT=Utilities.getSQLDate(tab[2].getDate("AEDAT")); 
							String KVGR1=Utilities.filterString(tab[2].getString("KVGR1"), 1, 100);
							String KVGR2=Utilities.filterString(tab[2].getString("KVGR2"), 1, 100);
							String KVGR3=Utilities.filterString(tab[2].getString("KVGR3"), 1, 100);
							String KVGR4=Utilities.filterString(tab[2].getString("KVGR4"), 1, 100);
							String KVGR5=Utilities.filterString(tab[2].getString("KVGR5"), 1, 100);
							String KNUMA=Utilities.filterString(tab[2].getString("KNUMA"), 1, 100);
							String KOKRS=Utilities.filterString(tab[2].getString("KOKRS"), 1, 100);
							int PS_PSP_PNR = tab[2].getInt("PS_PSP_PNR"); 
							String KURST=Utilities.filterString(tab[2].getString("KURST"), 1, 100);
							String KKBER=Utilities.filterString(tab[2].getString("KKBER"), 1, 100);
							String KNKLI=Utilities.filterString(tab[2].getString("KNKLI"), 1, 100);
							String GRUPP=Utilities.filterString(tab[2].getString("GRUPP"), 1, 100);
							String SBGRP=Utilities.filterString(tab[2].getString("SBGRP"), 1, 100);
							String CTLPC=Utilities.filterString(tab[2].getString("CTLPC"), 1, 100);
							String CMWAE=Utilities.filterString(tab[2].getString("CMWAE"), 1, 100);
							String CMFRE=Utilities.getSQLDate(tab[2].getDate("CMFRE")); 
							String CMNUP=Utilities.getSQLDate(tab[2].getDate("CMNUP")); 
							String CMNGV=Utilities.getSQLDate(tab[2].getDate("CMNGV")); 
							double AMTBL=tab[2].getDouble("AMTBL");
							String HITYP_PR=Utilities.filterString(tab[2].getString("HITYP_PR"), 1, 100);
							String ABRVW=Utilities.filterString(tab[2].getString("ABRVW"), 1, 100);
							String ABDIS=Utilities.filterString(tab[2].getString("ABDIS"), 1, 100);
							String VGBEL=Utilities.filterString(tab[2].getString("VGBEL"), 1, 100);
							String OBJNR=Utilities.filterString(tab[2].getString("OBJNR"), 1, 100);
							String BUKRS_VF=Utilities.filterString(tab[2].getString("BUKRS_VF"), 1, 100);
							String TAXK1=Utilities.filterString(tab[2].getString("TAXK1"), 1, 100);
							String TAXK2=Utilities.filterString(tab[2].getString("TAXK2"), 1, 100);
							String TAXK3=Utilities.filterString(tab[2].getString("TAXK3"), 1, 100);
							String TAXK4=Utilities.filterString(tab[2].getString("TAXK4"), 1, 100);
							String TAXK5=Utilities.filterString(tab[2].getString("TAXK5"), 1, 100);
							String TAXK6=Utilities.filterString(tab[2].getString("TAXK6"), 1, 100);
							String TAXK7=Utilities.filterString(tab[2].getString("TAXK7"), 1, 100); 
							String TAXK8=Utilities.filterString(tab[2].getString("TAXK8"), 1, 100);
							String TAXK9=Utilities.filterString(tab[2].getString("TAXK9"), 1, 100);
							String XBLNR=Utilities.filterString(tab[2].getString("XBLNR"), 1, 100);
							String ZUONR=Utilities.filterString(tab[2].getString("ZUONR"), 1, 100);
							String VGTYP=Utilities.filterString(tab[2].getString("VGTYP"), 1, 100);
							String KALSM_CH=Utilities.filterString(tab[2].getString("KALSM_CH"), 1, 100);
							int AGRZR = tab[2].getInt("AGRZR"); 
							String AUFNR=Utilities.filterString(tab[2].getString("AUFNR"), 1, 100);
							String QMNUM=Utilities.filterString(tab[2].getString("QMNUM"), 1, 100);
							String VBELN_GRP=Utilities.filterString(tab[2].getString("VBELN_GRP"), 1, 100);
							//String SCHEME_GRP=Utilities.filterString(tab[2].getString(" SCHEME_GRP"), 1, 100);
							String ABRUF_PART=Utilities.filterString(tab[2].getString("ABRUF_PART"), 1, 100);
							String ABHOD=Utilities.getSQLDate(tab[2].getDate("ABHOD")); 
							String ABHOV=Utilities.filterString(tab[2].getString("ABHOV"),1,100);;
							String ABHOB=Utilities.filterString(tab[2].getString("ABHOB"),1,100);
							String RPLNR=Utilities.filterString(tab[2].getString("RPLNR"), 1, 100);
							String VZEIT=Utilities.filterString(tab[2].getString("VZEIT"),1,100);
							String STCEG_L=Utilities.filterString(tab[2].getString("STCEG_L"), 1, 100);
							String LANDTX=Utilities.filterString(tab[2].getString("LANDTX"), 1, 100);
							String XEGDR=Utilities.filterString(tab[2].getString("XEGDR"), 1, 100);
							String ENQUEUE_GRP=Utilities.filterString(tab[2].getString("ENQUEUE_GRP"), 1, 100);
							String DAT_FZAU=Utilities.getSQLDate(tab[2].getDate("DAT_FZAU")); 
							String FMBDAT=Utilities.getSQLDate(tab[2].getDate("FMBDAT")); 
							String VSNMR_V=Utilities.filterString(tab[2].getString("VSNMR_V"), 1, 100);
							String HANDLE=Utilities.filterString(tab[2].getString("HANDLE"), 1, 100);
							String PROLI=Utilities.filterString(tab[2].getString("PROLI"), 1, 100);
							String CONT_DG=Utilities.filterString(tab[2].getString("CONT_DG"), 1, 100);
							String CRM_GUID=Utilities.filterString(tab[2].getString("CRM_GUID"), 1, 100);
							//String HBSVBAK=Utilities.filterString(tab[2].getString("HBSVBAK"), 1, 100);										  
							String SWENR=Utilities.filterString(tab[2].getString("SWENR"), 1, 100);
							String SMENR=Utilities.filterString(tab[2].getString("SMENR"), 1, 100);
							String PHASE=Utilities.filterString(tab[2].getString("PHASE"), 1, 100);
							String MTLAUR=Utilities.filterString(tab[2].getString("MTLAUR"), 1, 100);
							int STAGE = tab[2].getInt("STAGE"); 
							String HB_CONT_REASON=Utilities.filterString(tab[2].getString("HB_CONT_REASON"), 1, 100);
							String HB_EXPDATE=Utilities.getSQLDate(tab[2].getDate("HB_EXPDATE")); 
							String HB_RESDATE=Utilities.getSQLDate(tab[2].getDate("HB_RESDATE")); 
							//String VBAK4CRM=Utilities.filterString(tab[2].getString("VBAK4CRM"), 1, 100);
							String LOGSYSB=Utilities.filterString(tab[2].getString("LOGSYSB"), 1, 100);
							//String VBAK_CMPD=Utilities.filterString(tab[2].getString("VBAK_CMPD"), 1, 100);
							String KALCD=Utilities.filterString(tab[2].getString("KALCD"), 1, 100);
							String MULTI=Utilities.filterString(tab[2].getString("MULTI"), 1, 100); 
							
							s.executeUpdate("replace INTO sap_vbak"+
									"(MANDT,VBELN,ERDAT,ERZET,ERNAM,ANGDT,BNDDT,AUDAT,VBTYP,TRVOG,AUART,AUGRU,GWLDT,SUBMI,LIFSK,FAKSK,"+
									"NETWR,WAERK,VKORG,VTWEG,SPART,VKGRP,VKBUR,GSBER,GSKST,GUEBG,GUEEN,KNUMV,VDATU,VPRGR,AUTLF,VBKLA,VBKLT,KALSM,VSBED,FKARA,AWAHR,KTEXT,BSTNK,BSARK,BSTDK,BSTZD,IHREZ,BNAME,TELF1,MAHZA,"+
									"KUNNR,KOSTL,STAFO,STWAE,AEDAT,KVGR1,KVGR2,KVGR3,KVGR4,KVGR5,KNUMA,KOKRS,PS_PSP_PNR,"+
									"KURST,KKBER,KNKLI,GRUPP,SBGRP,CTLPC,CMWAE,CMFRE,CMNUP,CMNGV,AMTBL,HITYP_PR,ABRVW,ABDIS,VGBEL,"+
								    "OBJNR,BUKRS_VF,TAXK1,TAXK2,TAXK3,TAXK4,TAXK5,TAXK6,TAXK7,TAXK8,TAXK9,XBLNR,ZUONR,VGTYP,KALSM_CH,"+
									"AGRZR,AUFNR,QMNUM,VBELN_GRP,ABRUF_PART,ABHOD,ABHOV,ABHOB,RPLNR,VZEIT,STCEG_L,LANDTX,XEGDR,"+
									"ENQUEUE_GRP,DAT_FZAU,FMBDAT,VSNMR_V,HANDLE,PROLI,CONT_DG,CRM_GUID,SWENR,SMENR,PHASE,MTLAUR,"+
									"STAGE,HB_CONT_REASON,HB_EXPDATE,HB_RESDATE,LOGSYSB,KALCD,MULTI)"+
									"VALUES(" +									
									MANDT+",'"+VBELN+"',"+ERDAT+",'"+ERZET+"','"+ERNAM+"',"+ANGDT+","+BNDDT+","+AUDAT+",'"+VBTYP+"','"+TRVOG+"','"+AUART+"','"+AUGRU+"',"+GWLDT+",'"+SUBMI+"','"+LIFSK+"','"+FAKSK+"',"+
									NETWR+",'"+WAERK+"','"+VKORG+"','"+VTWEG+"','"+SPART+"','"+VKGRP+"','"+VKBUR+"','"+GSBER+"','"+GSKST+"',"+GUEBG+","+GUEEN+",'"+KNUMV+"',"+VDATU+",'"+VPRGR+"','"+AUTLF+"','"+VBKLA+"','"+VBKLT+"','"+KALSM+"','"+VSBED+"','"+FKARA+"',"+AWAHR+",'"+KTEXT+"','"+BSTNK+"','"+BSARK+"',"+BSTDK+",'"+BSTZD+"','"+IHREZ+"','"+BNAME+"','"+TELF1+"',"+MAHZA+","+
									"'"+KUNNR+"','"+KOSTL+"','"+STAFO+"','"+STWAE+"',"+AEDAT+",'"+KVGR1+"','"+KVGR2+"','"+KVGR3+"','"+KVGR4+"','"+KVGR5+"','"+KNUMA+"','"+KOKRS+"',"+PS_PSP_PNR+",'"+
									KURST+"','"+KKBER+"','"+KNKLI+"','"+GRUPP+"','"+SBGRP+"','"+CTLPC+"','"+CMWAE+"',"+CMFRE+","+CMNUP+","+CMNGV+","+AMTBL+",'"+HITYP_PR+"','"+ABRVW+"','"+ABDIS+"','"+VGBEL+"','"+
								    OBJNR+"','"+BUKRS_VF+"','"+TAXK1+"','"+TAXK2+"','"+TAXK3+"','"+TAXK4+"','"+TAXK5+"','"+TAXK6+"','"+TAXK7+"','"+TAXK8+"','"+TAXK9+"','"+XBLNR+"','"+ZUONR+"','"+VGTYP+"','"+KALSM_CH+"',"+
									AGRZR+",'"+AUFNR+"','"+QMNUM+"','"+VBELN_GRP+"','"+ABRUF_PART+"',"+ABHOD+",'"+ABHOV+"','"+ABHOB+"','"+RPLNR+"','"+VZEIT+"','"+STCEG_L+"','"+LANDTX+"','"+XEGDR+"','"+
									ENQUEUE_GRP+"',"+DAT_FZAU+","+FMBDAT+",'"+VSNMR_V+"','"+HANDLE+"','"+PROLI+"','"+CONT_DG+"','"+CRM_GUID+"','"+SWENR+"','"+SMENR+"','"+PHASE+"','"+MTLAUR+"',"+
									STAGE+",'"+HB_CONT_REASON+"',"+HB_EXPDATE+","+HB_RESDATE+",'"+LOGSYSB+"','"+KALCD+"','"+MULTI+"')");
							
							
							
							
							
						
							tab[2].setRow(i+1);
						}
						// end tab[2]
						
						
						
						System.out.println("Done.");
						
						// tab[3]

						System.out.println("Synchronzation started for VBAP");
						
						tab[3].firstRow();
						
						for(int i = 0; i < tab[3].getNumRows(); i++){
							
							int MANDT = tab[3].getInt("MANDT");
							String VBELN=Utilities.filterString(tab[3].getString("VBELN"), 1, 100);
							int POSNR = tab[3].getInt("POSNR");
							String MATNR=Utilities.filterString(tab[3].getString("MATNR"), 1, 100);
							String MATWA=Utilities.filterString(tab[3].getString("MATWA"), 1, 100);
							String PMATN=Utilities.filterString(tab[3].getString("PMATN"), 1, 100);
							String CHARG=Utilities.filterString(tab[3].getString("CHARG"), 1, 100);
							String MATKL=Utilities.filterString(tab[3].getString("MATKL"), 1, 100);
							String ARKTX=Utilities.filterString(tab[3].getString("ARKTX"), 1, 100);
							String PSTYV=Utilities.filterString(tab[3].getString("PSTYV"), 1, 100);
							String POSAR=Utilities.filterString(tab[3].getString("POSAR"), 1, 100);
							String LFREL=Utilities.filterString(tab[3].getString("LFREL"), 1, 100);
							String FKREL=Utilities.filterString(tab[3].getString("FKREL"), 1, 100);
							int UEPOS = tab[3].getInt("UEPOS");
							int GRPOS = tab[3].getInt("GRPOS");
							String ABGRU=Utilities.filterString(tab[3].getString("ABGRU"), 1, 100);
							String PRODH=Utilities.filterString(tab[3].getString("PRODH"), 1, 100);
							String ZWERT=Utilities.filterString(tab[3].getString("ZWERT"), 1, 100);
							double ZMENG=tab[3].getDouble("ZMENG");
							String ZIEME=Utilities.filterString(tab[3].getString("ZIEME"), 1, 100);
							double UMZIZ=tab[3].getDouble("UMZIZ");
							double UMZIN=tab[3].getDouble("UMZIN");
							String MEINS = Utilities.filterString(tab[3].getString("MEINS"),1,100);
							double SMENG=tab[3].getDouble("SMENG");
							double ABLFZ=tab[3].getDouble("ABLFZ");
							String ABDAT=Utilities.getSQLDate(tab[3].getDate("ABDAT")); 
							double ABSFZ=tab[3].getDouble("ABSFZ");
							String POSEX=Utilities.filterString(tab[3].getString("POSEX"), 1, 100);
							String KDMAT=Utilities.filterString(tab[3].getString("KDMAT"), 1, 100);
							double KBVER=tab[3].getDouble("KBVER");
							double KEVER=tab[3].getDouble("KEVER");
							String VKGRU=Utilities.filterString(tab[3].getString("VKGRU"), 1, 100);
							String VKAUS=Utilities.filterString(tab[3].getString("VKAUS"), 1, 100);
							int GRKOR = tab[3].getInt("GRKOR");
							String FMENG=Utilities.filterString(tab[3].getString("FMENG"), 1, 100);
							String UEBTK=Utilities.filterString(tab[3].getString("UEBTK"), 1, 100);
							int UEBTO = tab[3].getInt("UEBTO");
							int UNTTO = tab[3].getInt("UNTTO");
							String FAKSP=Utilities.filterString(tab[3].getString("FAKSP"), 1, 100);
							String ATPKZ=Utilities.filterString(tab[3].getString("ATPKZ"), 1, 100);
							String RKFKF=Utilities.filterString(tab[3].getString("RKFKF"), 1, 100);
							String SPART=Utilities.filterString(tab[3].getString("SPART"), 1, 100);
							String GSBER=Utilities.filterString(tab[3].getString("GSBER"), 1, 100);
							String NETWR=Utilities.filterString(tab[3].getString("NETWR"), 1, 100);
							String WAERK=Utilities.filterString(tab[3].getString("WAERK"), 1, 100);
							String ANTLF=Utilities.filterString(tab[3].getString("ANTLF"), 1, 100);
							String KZTLF=Utilities.filterString(tab[3].getString("KZTLF"), 1, 100);
							String CHSPL=Utilities.filterString(tab[3].getString("CHSPL"), 1, 100);
							double KWMENG=tab[3].getDouble("KWMENG");
							double LSMENG=tab[3].getDouble("LSMENG");
							double KBMENG=tab[3].getDouble("KBMENG");
							double KLMENG=tab[3].getDouble("KLMENG");
							String VRKME = Utilities.filterString(tab[3].getString("VRKME"),1,100);
							double UMVKZ=tab[3].getDouble("UMVKZ");
							double UMVKN=tab[3].getDouble("UMVKN");
							double BRGEW=tab[3].getDouble("BRGEW");
							double NTGEW=tab[3].getDouble("NTGEW");
							String GEWEI = Utilities.filterString(tab[3].getString("GEWEI"),1,100);
							double VOLUM=tab[3].getDouble("VOLUM");
							int VOLEH = tab[3].getInt("VOLEH");
							String VBELV=Utilities.filterString(tab[3].getString("VBELV"), 1, 100);
							int POSNV = tab[3].getInt("POSNV");
							String VGBEL=Utilities.filterString(tab[3].getString("VGBEL"), 1, 100);
							int VGPOS = tab[3].getInt("VGPOS");
							String VOREF=Utilities.filterString(tab[3].getString("VOREF"), 1, 100);
							String UPFLU=Utilities.filterString(tab[3].getString("UPFLU"), 1, 100);
							String ERLRE=Utilities.filterString(tab[3].getString("ERLRE"), 1, 100);
							int LPRIO = tab[3].getInt("LPRIO");
							String WERKS=Utilities.filterString(tab[3].getString("WERKS"), 1, 100);
							String LGORT=Utilities.filterString(tab[3].getString("LGORT"), 1, 100);
							String VSTEL=Utilities.filterString(tab[3].getString("VSTEL"), 1, 100);
							String ROUTE=Utilities.filterString(tab[3].getString("ROUTE"), 1, 100);
							String STKEY=Utilities.filterString(tab[3].getString("STKEY"), 1, 100);
							String STDAT=Utilities.getSQLDate(tab[3].getDate("STDAT")); 
							String STLNR=Utilities.filterString(tab[3].getString("STLNR"), 1, 100);
							double STPOS=tab[3].getDouble("STPOS");
							int AWAHR = tab[3].getInt("AWAHR");
							String ERDAT=Utilities.getSQLDate(tab[3].getDate("ERDAT")); 
							String ERNAM=Utilities.filterString(tab[3].getString("ERNAM"), 1, 100);
							String ERZET=Utilities.filterString(tab[3].getString("ERZET"),1,100);
							String TAXM1=Utilities.filterString(tab[3].getString("TAXM1"), 1, 100);
							String TAXM2=Utilities.filterString(tab[3].getString("TAXM2"), 1, 100);
							String TAXM3=Utilities.filterString(tab[3].getString("TAXM3"), 1, 100);
							String TAXM4=Utilities.filterString(tab[3].getString("TAXM4"), 1, 100);
							String TAXM5=Utilities.filterString(tab[3].getString("TAXM5"), 1, 100);
							String TAXM6=Utilities.filterString(tab[3].getString("TAXM6"), 1, 100);
							String TAXM7=Utilities.filterString(tab[3].getString("TAXM7"), 1, 100);
							String TAXM8=Utilities.filterString(tab[3].getString("TAXM8"), 1, 100);
							String TAXM9=Utilities.filterString(tab[3].getString("TAXM9"), 1, 100);
							double VBEAF=tab[3].getDouble("VBEAF");
							double VBEAV=tab[3].getDouble("VBEAV");
							String VGREF=Utilities.filterString(tab[3].getString("VGREF"), 1, 100);
							double NETPR=tab[3].getDouble("NETPR");
							double KPEIN=tab[3].getDouble("KPEIN");
							String KMEIN = Utilities.filterString(tab[3].getString("KMEIN"),1,100);
							String SHKZG=Utilities.filterString(tab[3].getString("SHKZG"), 1, 100);
							String SKTOF=Utilities.filterString(tab[3].getString("SKTOF"), 1, 100);
							String MTVFP=Utilities.filterString(tab[3].getString("MTVFP"), 1, 100);
							String SUMBD=Utilities.filterString(tab[3].getString("SUMBD"), 1, 100);
							String KONDM=Utilities.filterString(tab[3].getString("KONDM"), 1, 100);
							String KTGRM=Utilities.filterString(tab[3].getString("KTGRM"), 1, 100);
							String BONUS=Utilities.filterString(tab[3].getString("BONUS"), 1, 100);
							String PROVG=Utilities.filterString(tab[3].getString("PROVG"), 1, 100);
							String EANNR=Utilities.filterString(tab[3].getString("EANNR"), 1, 100);
							String PRSOK=Utilities.filterString(tab[3].getString("PRSOK"), 1, 100);
							String BWTAR=Utilities.filterString(tab[3].getString("BWTAR"), 1, 100);
							String BWTEX=Utilities.filterString(tab[3].getString("BWTEX"), 1, 100);
							String XCHPF=Utilities.filterString(tab[3].getString("XCHPF"), 1, 100);
							String XCHAR=Utilities.filterString(tab[3].getString("XCHAR"), 1, 100);
							double LFMNG=tab[3].getDouble("LFMNG");
							String STAFO=Utilities.filterString(tab[3].getString("STAFO"), 1, 100);
							double WAVWR=tab[3].getDouble("WAVWR");
							double KZWI1=tab[3].getDouble("KZWI1");
							double KZWI2=tab[3].getDouble("KZWI2");
							double KZWI3=tab[3].getDouble("KZWI3");
							double KZWI4=tab[3].getDouble("KZWI4");
							double KZWI5=tab[3].getDouble("KZWI5");
							double KZWI6=tab[3].getDouble("KZWI6");
							double STCUR=tab[3].getDouble("STCUR");
							String AEDAT=Utilities.getSQLDate(tab[3].getDate("AEDAT")); 
							String EAN11=Utilities.filterString(tab[3].getString("EAN11"), 1, 100);
							String FIXMG=Utilities.filterString(tab[3].getString("FIXMG"), 1, 100);
							String PRCTR=Utilities.filterString(tab[3].getString("PRCTR"), 1, 100);
							String MVGR1=Utilities.filterString(tab[3].getString("MVGR1"), 1, 100);
							String MVGR2=Utilities.filterString(tab[3].getString("MVGR2"), 1, 100);
							String MVGR3=Utilities.filterString(tab[3].getString("MVGR3"), 1, 100);
							String MVGR4=Utilities.filterString(tab[3].getString("MVGR4"), 1, 100);
							String MVGR5=Utilities.filterString(tab[3].getString("MVGR5"), 1, 100);
							double KMPMG=tab[3].getDouble("KMPMG");
							String SUGRD=Utilities.filterString(tab[3].getString("SUGRD"), 1, 100);
							String SOBKZ=Utilities.filterString(tab[3].getString("SOBKZ"), 1, 100);
							String VPZUO=Utilities.filterString(tab[3].getString("VPZUO"), 1, 100);
							int PAOBJNR = tab[3].getInt("PAOBJNR");
							int PS_PSP_PNR = tab[3].getInt("PS_PSP_PNR");
							String AUFNR=Utilities.filterString(tab[3].getString("AUFNR"), 1, 100);
							String VPMAT=Utilities.filterString(tab[3].getString("VPMAT"), 1, 100);
							String VPWRK=Utilities.filterString(tab[3].getString("VPWRK"), 1, 100);
							String PRBME=Utilities.filterString(tab[3].getString("PRBME"), 1, 100);
							String UMREF=Utilities.filterString(tab[3].getString("UMREF"), 1, 100);
							String KNTTP=Utilities.filterString(tab[3].getString("KNTTP"), 1, 100);
							String KZVBR=Utilities.filterString(tab[3].getString("KZVBR"), 1, 100);
							String SERNR=Utilities.filterString(tab[3].getString("SERNR"), 1, 100);
							String OBJNR=Utilities.filterString(tab[3].getString("OBJNR"), 1, 100);
							String ABGRS=Utilities.filterString(tab[3].getString("ABGRS"), 1, 100);
							String BEDAE=Utilities.filterString(tab[3].getString("BEDAE"), 1, 100);
							String CMPRE=Utilities.filterString(tab[3].getString("CMPRE"), 1, 100);
							String CMTFG=Utilities.filterString(tab[3].getString("CMTFG"), 1, 100);
							String CMPNT=Utilities.filterString(tab[3].getString("CMPNT"), 1, 100);
							double CMKUA=tab[3].getDouble("CMKUA");
							int CUOBJ = tab[3].getInt("CUOBJ");
							int CUOBJ_CH = tab[3].getInt("CUOBJ_CH");
							String CEPOK=Utilities.filterString(tab[3].getString("CEPOK"), 1, 100);
							String KOUPD=Utilities.filterString(tab[3].getString("KOUPD"), 1, 100);
							String SERAIL=Utilities.filterString(tab[3].getString("SERAIL"), 1, 100);
							int ANZSN = tab[3].getInt("ANZSN");
							//String NACHL=Utilities.filterString(tab[3].getString(" NACHL"), 1, 100);
							String MAGRV=Utilities.filterString(tab[3].getString("MAGRV"), 1, 100);
							String MPROK=Utilities.filterString(tab[3].getString("MPROK"), 1, 100);
							String VGTYP=Utilities.filterString(tab[3].getString("VGTYP"), 1, 100);
							String PROSA=Utilities.filterString(tab[3].getString("PROSA"), 1, 100);
							String UEPVW=Utilities.filterString(tab[3].getString("UEPVW"), 1, 100);
							int KALNR = tab[3].getInt("KALNR");
							String KLVAR=Utilities.filterString(tab[3].getString("KLVAR"), 1, 100);
							String SPOSN=Utilities.filterString(tab[3].getString("SPOSN"), 1, 100);
							String KOWRR=Utilities.filterString(tab[3].getString("KOWRR"), 1, 100);
							String STADAT=Utilities.getSQLDate(tab[3].getDate("STADAT")); 
							String EXART=Utilities.filterString(tab[3].getString("EXART"), 1, 100);
							String PREFE=Utilities.filterString(tab[3].getString("PREFE"), 1, 100);
							String KNUMH=Utilities.filterString(tab[3].getString("KNUMH"), 1, 100);
							int CLINT = tab[3].getInt("CLINT");
							int CHMVS = tab[3].getInt("CHMVS");
							String STLTY=Utilities.filterString(tab[3].getString("STLTY"), 1, 100);
							int STLKN = tab[3].getInt("STLKN");
							int STPOZ = tab[3].getInt("STPOZ");
							String STMAN=Utilities.filterString(tab[3].getString("STMAN"), 1, 100);
							String ZSCHL_K=Utilities.filterString(tab[3].getString("ZSCHL_K"), 1, 100);
							String KALSM_K=Utilities.filterString(tab[3].getString("KALSM_K"), 1, 100);
							String KALVAR=Utilities.filterString(tab[3].getString("KALVAR"), 1, 100);
							String KOSCH=Utilities.filterString(tab[3].getString("KOSCH"), 1, 100);
							String UPMAT=Utilities.filterString(tab[3].getString("UPMAT"), 1, 100);
							String UKONM=Utilities.filterString(tab[3].getString("UKONM"), 1, 100);
							String MFRGR=Utilities.filterString(tab[3].getString("MFRGR"), 1, 100);
							String PLAVO=Utilities.filterString(tab[3].getString("PLAVO"), 1, 100);
							String KANNR=Utilities.filterString(tab[3].getString("KANNR"), 1, 100);
							String CMPRE_FLT=Utilities.filterString(tab[3].getString("CMPRE_FLT"), 1, 100);
							String ABFOR=Utilities.filterString(tab[3].getString("ABFOR"), 1, 100);
							String ABGES=Utilities.filterString(tab[3].getString("ABGES"), 1, 100);
							String J_1BCFOP=Utilities.filterString(tab[3].getString("J_1BCFOP"), 1, 100);
							String J_1BTAXLW1=Utilities.filterString(tab[3].getString("J_1BTAXLW1"), 1, 100);
							String J_1BTAXLW2=Utilities.filterString(tab[3].getString("J_1BTAXLW2"), 1, 100);
							String J_1BTXSDC=Utilities.filterString(tab[3].getString("J_1BTXSDC"), 1, 100);
							String WKTNR=Utilities.filterString(tab[3].getString("WKTNR"), 1, 100);
							int WKTPS = tab[3].getInt("WKTPS");
							String SKOPF=Utilities.filterString(tab[3].getString("SKOPF"), 1, 100);
							String KZBWS=Utilities.filterString(tab[3].getString("KZBWS"), 1, 100);
							String WGRU1=Utilities.filterString(tab[3].getString("WGRU1"), 1, 100);
							String WGRU2=Utilities.filterString(tab[3].getString("WGRU2"), 1, 100);
							String KNUMA_PI=Utilities.filterString(tab[3].getString("KNUMA_PI"), 1, 100);
							String KNUMA_AG=Utilities.filterString(tab[3].getString("KNUMA_AG"), 1, 100);
							String KZFME=Utilities.filterString(tab[3].getString("KZFME"), 1, 100);
							String LSTANR=Utilities.filterString(tab[3].getString("LSTANR"), 1, 100);
							String TECHS=Utilities.filterString(tab[3].getString("TECHS"), 1, 100);
							String MWSBP=Utilities.filterString(tab[3].getString("MWSBP"), 1, 100);
							String BERID=Utilities.filterString(tab[3].getString("BERID"), 1, 100);
							String PCTRF=Utilities.filterString(tab[3].getString("PCTRF"), 1, 100);
							String LOGSYS_EXT=Utilities.filterString(tab[3].getString("LOGSYS_EXT"), 1, 100);
							String J_1BTAXLW3=Utilities.filterString(tab[3].getString("J_1BTAXLW3"), 1, 100);
							//String VSNMR_V=Utilities.filterString(tab[3].getString("/BEV1/SRFUND"), 1, 100);
							String FERC_IND=Utilities.filterString(tab[3].getString("FERC_IND"), 1, 100);
							String KOSTL=Utilities.filterString(tab[3].getString("KOSTL"), 1, 100);
							String FONDS=Utilities.filterString(tab[3].getString("FONDS"), 1, 100);
							String FISTL=Utilities.filterString(tab[3].getString("FISTL"), 1, 100);
							String FKBER=Utilities.filterString(tab[3].getString("FKBER"), 1, 100);
							String GRANT_NBR=Utilities.filterString(tab[3].getString("GRANT_NBR"), 1, 100);


							s.executeUpdate("replace INTO sap_vbap"+
									"(MANDT,VBELN,POSNR,MATNR,MATWA,PMATN,CHARG,MATKL,ARKTX,PSTYV,POSAR,LFREL,FKREL,UEPOS,GRPOS,ABGRU,PRODH,ZWERT,ZMENG,ZIEME,"+
									"UMZIZ,UMZIN,MEINS,SMENG,ABLFZ,ABDAT,ABSFZ,POSEX,KDMAT,KBVER,KEVER,VKGRU,VKAUS,GRKOR,FMENG,UEBTK,UEBTO,UNTTO,FAKSP,ATPKZ,"+
									"RKFKF,SPART,GSBER,NETWR,WAERK,ANTLF,KZTLF,CHSPL,KWMENG,LSMENG,KBMENG,KLMENG,VRKME,UMVKZ,UMVKN,BRGEW,NTGEW,GEWEI,VOLUM,VOLEH,"+
									"VBELV,POSNV,VGBEL,VGPOS,VOREF,UPFLU,ERLRE,LPRIO,WERKS,LGORT,VSTEL,ROUTE,STKEY,STDAT,STLNR,STPOS,AWAHR,ERDAT,ERNAM,ERZET,TAXM1,"+
									"TAXM2,TAXM3,TAXM4,TAXM5,TAXM6,TAXM7,TAXM8,TAXM9,VBEAF,VBEAV,VGREF,NETPR,KPEIN,KMEIN,SHKZG,SKTOF,MTVFP,SUMBD,KONDM,KTGRM,BONUS,"+
									"PROVG,EANNR,PRSOK,BWTAR,BWTEX,XCHPF,XCHAR,LFMNG,STAFO,WAVWR,KZWI1,KZWI2,KZWI3,KZWI4,KZWI5,KZWI6,STCUR,AEDAT,EAN11,FIXMG,PRCTR,"+
									"MVGR1,MVGR2,MVGR3,MVGR4,MVGR5,KMPMG,SUGRD,SOBKZ,VPZUO,PAOBJNR,PS_PSP_PNR,AUFNR,VPMAT,VPWRK,PRBME,UMREF,KNTTP,KZVBR,SERNR,OBJNR,"+
									"ABGRS,BEDAE,CMPRE,CMTFG,CMPNT,CMKUA,CUOBJ,CUOBJ_CH,CEPOK,KOUPD,SERAIL,ANZSN,MAGRV,MPROK,VGTYP,PROSA,UEPVW,KALNR,KLVAR,SPOSN,"+
									"KOWRR,STADAT,EXART,PREFE,KNUMH,CLINT,CHMVS,STLTY,STLKN,STPOZ,STMAN,ZSCHL_K,KALSM_K,KALVAR,KOSCH,UPMAT,UKONM,MFRGR,	PLAVO,"+
									"KANNR,CMPRE_FLT,ABFOR,ABGES,J_1BCFOP,J_1BTAXLW1,J_1BTAXLW2,J_1BTXSDC,WKTNR,WKTPS,SKOPF,KZBWS,WGRU1,WGRU2,KNUMA_PI,KNUMA_AG,"+
									"KZFME,LSTANR,TECHS,MWSBP,BERID,PCTRF,LOGSYS_EXT,J_1BTAXLW3,FERC_IND,KOSTL,FONDS,FISTL,FKBER,GRANT_NBR)"+
									"VALUES(" +
									MANDT+",'"+VBELN+"',"+POSNR+",'"+MATNR+"','"+MATWA+"','"+PMATN+"','"+CHARG+"','"+MATKL+"','"+ARKTX+"','"+PSTYV+"','"+POSAR+"','"+LFREL+"','"+FKREL+"',"+UEPOS+","+GRPOS+",'"+ABGRU+"','"+PRODH+"','"+ZWERT+"',"+ZMENG+",'"+ZIEME+"',"+
									UMZIZ+","+UMZIN+",'"+MEINS+"',"+SMENG+","+ABLFZ+","+ABDAT+","+ABSFZ+",'"+POSEX+"','"+KDMAT+"',"+KBVER+","+KEVER+",'"+VKGRU+"','"+VKAUS+"',"+GRKOR+",'"+FMENG+"','"+UEBTK+"',"+UEBTO+","+UNTTO+",'"+FAKSP+"','"+ATPKZ+"','"+
									RKFKF+"','"+SPART+"','"+GSBER+"','"+NETWR+"','"+WAERK+"','"+ANTLF+"','"+KZTLF+"','"+CHSPL+"',"+KWMENG+","+LSMENG+","+KBMENG+","+KLMENG+",'"+VRKME+"',"+UMVKZ+","+UMVKN+","+BRGEW+","+NTGEW+",'"+GEWEI+"',"+VOLUM+","+VOLEH+",'"+
									VBELV+"',"+POSNV+",'"+VGBEL+"',"+VGPOS+",'"+VOREF+"','"+UPFLU+"','"+ERLRE+"',"+LPRIO+",'"+WERKS+"','"+LGORT+"','"+VSTEL+"','"+ROUTE+"','"+STKEY+"',"+STDAT+",'"+STLNR+"',"+STPOS+","+AWAHR+","+ERDAT+",'"+ERNAM+"','"+ERZET+"','"+TAXM1+"','"+
									TAXM2+"','"+TAXM3+"','"+TAXM4+"','"+TAXM5+"','"+TAXM6+"','"+TAXM7+"','"+TAXM8+"','"+TAXM9+"',"+VBEAF+","+VBEAV+",'"+VGREF+"',"+NETPR+","+KPEIN+",'"+KMEIN+"','"+SHKZG+"','"+SKTOF+"','"+MTVFP+"','"+SUMBD+"','"+KONDM+"','"+KTGRM+"','"+BONUS+"','"+
									PROVG+"','"+EANNR+"','"+PRSOK+"','"+BWTAR+"','"+BWTEX+"','"+XCHPF+"','"+XCHAR+"',"+LFMNG+",'"+STAFO+"',"+WAVWR+","+KZWI1+","+KZWI2+","+KZWI3+","+KZWI4+","+KZWI5+","+KZWI6+","+STCUR+","+AEDAT+",'"+EAN11+"','"+FIXMG+"','"+PRCTR+"','"+
									MVGR1+"','"+MVGR2+"','"+MVGR3+"','"+MVGR4+"','"+MVGR5+"',"+KMPMG+",'"+SUGRD+"','"+SOBKZ+"','"+VPZUO+"',"+PAOBJNR+","+PS_PSP_PNR+",'"+AUFNR+"','"+VPMAT+"','"+VPWRK+"','"+PRBME+"','"+UMREF+"','"+KNTTP+"','"+KZVBR+"','"+SERNR+"','"+OBJNR+"','"+
									ABGRS+"','"+BEDAE+"','"+CMPRE+"','"+CMTFG+"','"+CMPNT+"',"+CMKUA+","+CUOBJ+","+CUOBJ_CH+",'"+CEPOK+"','"+KOUPD+"','"+SERAIL+"',"+ANZSN+",'"+MAGRV+"','"+MPROK+"','"+VGTYP+"','"+PROSA+"','"+UEPVW+"',"+KALNR+",'"+KLVAR+"','"+SPOSN+"','"+
									KOWRR+"',"+STADAT+",'"+EXART+"','"+PREFE+"','"+KNUMH+"',"+CLINT+","+CHMVS+",'"+STLTY+"',"+STLKN+","+STPOZ+",'"+STMAN+"','"+ZSCHL_K+"','"+KALSM_K+"','"+KALVAR+"','"+KOSCH+"','"+UPMAT+"','"+UKONM+"','"+MFRGR+"','"+	PLAVO+"','"+
									KANNR+"','"+CMPRE_FLT+"','"+ABFOR+"','"+ABGES+"','"+J_1BCFOP+"','"+J_1BTAXLW1+"','"+J_1BTAXLW2+"','"+J_1BTXSDC+"','"+WKTNR+"',"+WKTPS+",'"+SKOPF+"','"+KZBWS+"','"+WGRU1+"','"+WGRU2+"','"+KNUMA_PI+"','"+KNUMA_AG+"','"+
									KZFME+"','"+LSTANR+"','"+TECHS+"','"+MWSBP+"','"+BERID+"','"+PCTRF+"','"+LOGSYS_EXT+"','"+J_1BTAXLW3+"','"+FERC_IND+"','"+KOSTL+"','"+FONDS+"','"+FISTL+"','"+FKBER+"','"+GRANT_NBR+"')");	


							
							
							
							
							
							


							tab[3].setRow(i+1);
							
						}
						
						// end tab[3]
						System.out.println("Done");
						
						System.out.println("Synchronzation started for VBUK");
						
						tab[4].firstRow();
						
						for(int i = 0; i < tab[4].getNumRows(); i++){
							
							
							
							
							
				int mandt = tab[4].getInt("MANDT");
				String vbeln = Utilities.filterString(tab[4].getString("VBELN"), 1, 100);
				String rfstk = Utilities.filterString(tab[4].getString("RFSTK"), 1, 10);
				String rfgsk = Utilities.filterString(tab[4].getString("RFGSK"), 1, 10);
				String bestk = Utilities.filterString(tab[4].getString("BESTK"), 1, 10);
				String lfstk = Utilities.filterString(tab[4].getString("LFSTK"), 1, 10);
				String lfgsk = Utilities.filterString(tab[4].getString("LFGSK"), 1, 10);
				String wbstk = Utilities.filterString(tab[4].getString("WBSTK"), 1, 10);
				String fkstk = Utilities.filterString(tab[4].getString("FKSTK"), 1, 10);
				String fksak = Utilities.filterString(tab[4].getString("FKSAK"), 1, 10);
				String buchk = Utilities.filterString(tab[4].getString("BUCHK"), 1, 10);
				String abstk = Utilities.filterString(tab[4].getString("ABSTK"), 1, 10);
				String gbstk = Utilities.filterString(tab[4].getString("GBSTK"), 1, 10);
				String kostk= Utilities.filterString(tab[4].getString("KOSTK"), 1, 10);
				String lvstk= Utilities.filterString(tab[4].getString("LVSTK"), 1, 10);
				String uvals= Utilities.filterString(tab[4].getString("UVALS"), 1, 10);
				String uvvls= Utilities.filterString(tab[4].getString("UVVLS"), 1, 10);
				String uvfas= Utilities.filterString(tab[4].getString("UVFAS"), 1, 10);
				String uvall= Utilities.filterString(tab[4].getString("UVALL"), 1, 10);
				String uvvlk= Utilities.filterString(tab[4].getString("UVVLK"), 1, 10);
				String uvfak= Utilities.filterString(tab[4].getString("UVFAK"), 1, 10);
				String uvprs= Utilities.filterString(tab[4].getString("UVPRS"), 1, 10);
				String vbtyp= Utilities.filterString(tab[4].getString("VBTYP"), 1, 10);
				String vbobj= Utilities.filterString(tab[4].getString("VBOBJ"), 1, 10);
				String aedat= Utilities.getSQLDate(tab[4].getDate("AEDAT"));
				String fkivk= Utilities.filterString(tab[4].getString("FKIVK"), 1, 10);
				String relik= Utilities.filterString(tab[4].getString("RELIK"), 1, 10);
				String uvk01= Utilities.filterString(tab[4].getString("UVK01"), 1, 10);
				String uvk02= Utilities.filterString(tab[4].getString("UVK02"), 1, 10);
				String uvk03= Utilities.filterString(tab[4].getString("UVK03"), 1, 10);
				String uvk04= Utilities.filterString(tab[4].getString("UVK04"), 1, 10);
				String uvk05= Utilities.filterString(tab[4].getString("UVK05"), 1, 10);
				String uvs01= Utilities.filterString(tab[4].getString("UVS01"), 1, 10);
				String uvs02= Utilities.filterString(tab[4].getString("UVS02"), 1, 10);
				String uvs03= Utilities.filterString(tab[4].getString("UVS03"), 1, 10);
				String uvs04= Utilities.filterString(tab[4].getString("UVS04"), 1, 10);
				String uvs05= Utilities.filterString(tab[4].getString("UVS05"), 1, 10);
				String pkstk= Utilities.filterString(tab[4].getString("PKSTK"), 1, 10);
				String cmpsa= Utilities.filterString(tab[4].getString("CMPSA"), 1, 10);
				String cmpsb= Utilities.filterString(tab[4].getString("CMPSB"), 1, 10);
				String cmpsc= Utilities.filterString(tab[4].getString("CMPSC"), 1, 10);
				String cmpsd= Utilities.filterString(tab[4].getString("CMPSD"), 1, 10);
				String cmpse= Utilities.filterString(tab[4].getString("CMPSE"), 1, 10);
				String cmpsf= Utilities.filterString(tab[4].getString("CMPSF"), 1, 10);
				String cmpsg= Utilities.filterString(tab[4].getString("CMPSG"), 1, 10);
				String cmpsh= Utilities.filterString(tab[4].getString("CMPSH"), 1, 10);
				String cmpsi= Utilities.filterString(tab[4].getString("CMPSI"), 1, 10);
				String cmpsj= Utilities.filterString(tab[4].getString("CMPSJ"), 1, 10);
				String cmpsk= Utilities.filterString(tab[4].getString("CMPSK"), 1, 10);
				String cmpsl= Utilities.filterString(tab[4].getString("CMPSL"), 1, 10);
				String cmps0= Utilities.filterString(tab[4].getString("CMPS0"), 1, 10);
				String cmps1= Utilities.filterString(tab[4].getString("CMPS1"), 1, 10);
				String cmps2= Utilities.filterString(tab[4].getString("CMPS2"), 1, 10);
				String cmgst= Utilities.filterString(tab[4].getString("CMGST"), 1, 10);
				String trsta= Utilities.filterString(tab[4].getString("TRSTA"), 1, 10);
				String koquk= Utilities.filterString(tab[4].getString("KOQUK"), 1, 10);
				String costa= Utilities.filterString(tab[4].getString("COSTA"), 1, 10);
				String saprl= Utilities.filterString(tab[4].getString("SAPRL"), 1, 50);
				String uvpas= Utilities.filterString(tab[4].getString("UVPAS"), 1, 10);
				String uvpis= Utilities.filterString(tab[4].getString("UVPIS"), 1, 10);
				String uvwas= Utilities.filterString(tab[4].getString("UVWAS"), 1, 10);
				String uvpak= Utilities.filterString(tab[4].getString("UVPAK"), 1, 10);
				String uvpik= Utilities.filterString(tab[4].getString("UVPIK"), 1, 10);
				String uvwak= Utilities.filterString(tab[4].getString("UVWAK"), 1, 10);
				String uvgek= Utilities.filterString(tab[4].getString("UVGEK"), 1, 10);
				String cmpsm= Utilities.filterString(tab[4].getString("CMPSM"), 1, 10);
				String dcstk= Utilities.filterString(tab[4].getString("DCSTK"), 1, 10);
				String vestk= Utilities.filterString(tab[4].getString("VESTK"), 1, 10);
				String vlstk= Utilities.filterString(tab[4].getString("VLSTK"), 1, 10);
				String rrsta= Utilities.filterString(tab[4].getString("RRSTA"), 1, 10);
				String block= Utilities.filterString(tab[4].getString("BLOCK"), 1, 10);
				String fsstk= Utilities.filterString(tab[4].getString("FSSTK"), 1, 10);
				String lsstk= Utilities.filterString(tab[4].getString("LSSTK"), 1, 10);
				String spstg= Utilities.filterString(tab[4].getString("SPSTG"), 1, 10);
				String pdstk= Utilities.filterString(tab[4].getString("PDSTK"), 1, 10);
				String fmstk= Utilities.filterString(tab[4].getString("FMSTK"), 1, 10);
				String manek= Utilities.filterString(tab[4].getString("MANEK"), 1, 10);
				String spe_tmpid= Utilities.filterString(tab[4].getString("SPE_TMPID"), 1, 10);
				String hdall= Utilities.filterString(tab[4].getString("HDALL"), 1, 10);
				String hdals= Utilities.filterString(tab[4].getString("HDALS"), 1, 10);
				String cmps_cm= Utilities.filterString(tab[4].getString("CMPS_CM"), 1, 10);
							
							
							
				s.executeUpdate("replace into sap_vbuk ("+			
				"mandt,vbeln,rfstk,rfgsk,bestk,lfstk,lfgsk,wbstk,fkstk,fksak,buchk,abstk,gbstk,kostk,lvstk,uvals,uvvls,uvfas,uvall,uvvlk,uvfak,"+
				"uvprs,vbtyp,vbobj,aedat,fkivk,relik,uvk01,uvk02,uvk03,uvk04,uvk05,uvs01,uvs02,uvs03,uvs04,uvs05,pkstk,cmpsa,cmpsb,cmpsc,cmpsd,"+
				"cmpse,cmpsf,cmpsg,cmpsh,cmpsi,cmpsj,cmpsk,cmpsl,cmps0,cmps1,cmps2,cmgst,trsta,koquk,costa,saprl,uvpas,uvpis,uvwas,uvpak,uvpik,"+
				"uvwak,uvgek,cmpsm,dcstk,vestk,vlstk,rrsta,block,fsstk,lsstk,spstg,pdstk,fmstk,manek,spe_tmpid,hdall,hdals,cmps_cm) values("+
				mandt+",'"+vbeln+"','"+rfstk+"','"+rfgsk+"','"+bestk+"','"+lfstk+"','"+lfgsk+"','"+wbstk+"','"+fkstk+"','"+fksak+"','"+buchk+"','"+abstk+"','"+gbstk+"','"+kostk+"','"+lvstk+"','"+uvals+"','"+uvvls+"','"+uvfas+"','"+uvall+"','"+uvvlk+"','"+uvfak+"','"+
				uvprs+"','"+vbtyp+"','"+vbobj+"',"+aedat+",'"+fkivk+"','"+relik+"','"+uvk01+"','"+uvk02+"','"+uvk03+"','"+uvk04+"','"+uvk05+"','"+uvs01+"','"+uvs02+"','"+uvs03+"','"+uvs04+"','"+uvs05+"','"+pkstk+"','"+cmpsa+"','"+cmpsb+"','"+cmpsc+"','"+cmpsd+"','"+
				cmpse+"','"+cmpsf+"','"+cmpsg+"','"+cmpsh+"','"+cmpsi+"','"+cmpsj+"','"+cmpsk+"','"+cmpsl+"','"+cmps0+"','"+cmps1+"','"+cmps2+"','"+cmgst+"','"+trsta+"','"+koquk+"','"+costa+"','"+saprl+"','"+uvpas+"','"+uvpis+"','"+uvwas+"','"+uvpak+"','"+uvpik+"','"+
				uvwak+"','"+uvgek+"','"+cmpsm+"','"+dcstk+"','"+vestk+"','"+vlstk+"','"+rrsta+"','"+block+"','"+fsstk+"','"+lsstk+"','"+spstg+"','"+pdstk+"','"+fmstk+"','"+manek+"','"+spe_tmpid+"','"+hdall+"','"+hdals+"','"+cmps_cm+
				"')");	
							
							tab[4].setRow(i+1);
						}						
						
						
						System.out.println("Done.");
			obj.dropConnection();
						s.close();
			ds.dropConnection();			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
