package com.pbc.sap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoTable;

public class SyncHCM {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SAPUtilities obj = new SAPUtilities();
		obj.connectPRD();
		
		try {
			
			Datasource ds = new Datasource();
			ds.createConnection();
			Statement s = ds.createStatement();
			
			JCoTable[] tab = obj.getHCMMaster();
			
			// tab[0]

			System.out.println("Synchronzation started for sap_cskt:");
			
			tab[0].firstRow();
			
			for(int i = 0; i < tab[0].getNumRows(); i++){
				
				int MANDT = tab[0].getInt("MANDT");
				String SPRAS = Utilities.filterString(tab[0].getString("SPRAS"), 1, 100);
				String KOKRS = Utilities.filterString(tab[0].getString("KOKRS"), 1, 100);
				long KOSTL = tab[0].getLong("KOSTL");
				String DATBI = Utilities.filterString(tab[0].getString("DATBI"), 1, 100);
				String KTEXT = Utilities.filterString(tab[0].getString("KTEXT"), 1, 100);
				String LTEXT = Utilities.filterString(tab[0].getString("LTEXT"), 1, 100);
				String MCTXT = Utilities.filterString(tab[0].getString("MCTXT"), 1, 100);
				
				s.executeUpdate("REPLACE INTO `sap_cskt`(`mandt`,`spras`,`kokrs`,`kostl`,`datbi`,`ktext`,`ltext`,`mctxt`)VALUES("+MANDT+",'"+SPRAS+"','"+KOKRS+"',"+KOSTL+",'"+DATBI+"','"+KTEXT+"','"+LTEXT+"','"+MCTXT+"')");
				
				tab[0].setRow(i+1);
			}
			// end tab[0]
			
			
			// tab[1]
			System.out.println("Synchronzation started for sap_t527x");

			tab[1].firstRow();
			
			for(int i = 0; i < tab[1].getNumRows(); i++){
				
				int MANDT = tab[1].getInt("MANDT");
				String SPRSL = Utilities.filterString(tab[1].getString("SPRSL"), 1, 100);
				long ORGEH = tab[1].getLong("ORGEH");
				String ENDDA = Utilities.filterString(tab[1].getString("ENDDA"), 1, 100);
				String BEGDA = Utilities.filterString(tab[1].getString("BEGDA"), 1, 100);
				String ORGTX = Utilities.filterString(tab[1].getString("ORGTX"), 1, 100);
				String MAINT = Utilities.filterString(tab[1].getString("MAINT"), 1, 100);
				
				s.executeUpdate("REPLACE INTO `sap_t527x`(`mandt`,`sprsl`,`orgeh`,`endda`,`begda`,`orgtx`,`maint`)VALUES("+MANDT+",'"+SPRSL+"',"+ORGEH+",'"+ENDDA+"','"+BEGDA+"','"+ORGTX+"','"+MAINT+"')");
				
				tab[1].setRow(i+1);
			}
			// end tab[1]
			
			// tab[2]
			System.out.println("Synchronzation started for sap_t528t");
			tab[2].firstRow();
			
			for(int i = 0; i < tab[2].getNumRows(); i++){
				
				int MANDT = tab[2].getInt("MANDT");
				String SPRSL = Utilities.filterString(tab[2].getString("SPRSL"), 1, 100);
				String OTYPE = Utilities.filterString(tab[2].getString("OTYPE"), 1, 100);
				long PLANS = tab[2].getLong("PLANS");
				String ENDDA = Utilities.filterString(tab[2].getString("ENDDA"), 1, 100);
				String BEGDA = Utilities.filterString(tab[2].getString("BEGDA"), 1, 100);
				String PLSTX = Utilities.filterString(tab[2].getString("PLSTX"), 1, 100);
				String MAINT = Utilities.filterString(tab[2].getString("MAINT"), 1, 100);
				
				s.executeUpdate("REPLACE INTO `sap_t528t`(`mandt`,`sprsl`,`otype`,`plans`,`endda`,`begda`,`plstx`,`maint`)VALUES("+MANDT+",'"+SPRSL+"','"+OTYPE+"',"+PLANS+",'"+ENDDA+"','"+BEGDA+"','"+PLSTX+"','"+MAINT+"')");
				
				tab[2].setRow(i+1); 
			}
			// end tab[2]
			
			
			// tab[3]
			System.out.println("Synchronzation started for sap_pa0000");
			tab[3].firstRow();
			
			for(int i = 0; i < tab[3].getNumRows(); i++){
				
				int MANDT = tab[3].getInt("MANDT");
				long PERNR = tab[3].getLong("PERNR");
				String SUBTY = Utilities.filterString(tab[3].getString("SUBTY"), 1, 100);
				String OBJPS = Utilities.filterString(tab[3].getString("OBJPS"), 1, 100);
				String SPRPS = Utilities.filterString(tab[3].getString("SPRPS"), 1, 100);
				String ENDDA = Utilities.filterString(tab[3].getString("ENDDA"), 1, 100);
				String BEGDA = Utilities.filterString(tab[3].getString("BEGDA"), 1, 100);
				long SEQNR = tab[3].getLong("SEQNR");
				String AEDTM = Utilities.filterString(tab[3].getString("AEDTM"), 1, 100);
				String UNAME = Utilities.filterString(tab[3].getString("UNAME"), 1, 100);
				String HISTO = Utilities.filterString(tab[3].getString("HISTO"), 1, 100);
				String ITXEX = Utilities.filterString(tab[3].getString("ITXEX"), 1, 100);
				String REFEX = Utilities.filterString(tab[3].getString("REFEX"), 1, 100);
				String ORDEX = Utilities.filterString(tab[3].getString("ORDEX"), 1, 100);
				String ITBLD = Utilities.filterString(tab[3].getString("ITBLD"), 1, 100);
				String PREAS = Utilities.filterString(tab[3].getString("PREAS"), 1, 100);
				String FLAG1 = Utilities.filterString(tab[3].getString("FLAG1"), 1, 100);
				String FLAG2 = Utilities.filterString(tab[3].getString("FLAG2"), 1, 100);
				String FLAG3 = Utilities.filterString(tab[3].getString("FLAG3"), 1, 100);
				String FLAG4 = Utilities.filterString(tab[3].getString("FLAG4"), 1, 100);
				String RESE1 = Utilities.filterString(tab[3].getString("RESE1"), 1, 100);
				String RESE2 = Utilities.filterString(tab[3].getString("RESE2"), 1, 100);
				String GRPVL = Utilities.filterString(tab[3].getString("GRPVL"), 1, 100);
				String MASSN = Utilities.filterString(tab[3].getString("MASSN"), 1, 100);
				String MASSG = Utilities.filterString(tab[3].getString("MASSG"), 1, 100);
				long STAT1 = tab[3].getLong("STAT1");
				long STAT2 = tab[3].getLong("STAT2");
				long STAT3 = tab[3].getLong("STAT3");
				
				s.executeUpdate("REPLACE INTO `sap_pa0000`(`mandt`,`pernr`,`subty`,`objps`,`sprps`,`endda`,`begda`,`seqnr`,`aedtm`,`uname`,`histo`,`itxex`,`refex`,`ordex`,`itbld`,`preas`,`flag1`,`flag2`,`flag3`,`flag4`,`rese1`,`rese2`,`grpvl`,`massn`,`massg`,`stat1`,`stat2`,`stat3`)VALUES("+MANDT+","+PERNR+",'"+SUBTY+"','"+OBJPS+"','"+SPRPS+"','"+ENDDA+"','"+BEGDA+"',"+SEQNR+",'"+AEDTM+"','"+UNAME+"','"+HISTO+"','"+ITXEX+"','"+REFEX+"','"+ORDEX+"','"+ITBLD+"','"+PREAS+"','"+FLAG1+"','"+FLAG2+"','"+FLAG3+"','"+FLAG4+"','"+RESE1+"','"+RESE2+"','"+GRPVL+"','"+MASSN+"','"+MASSG+"',"+STAT1+","+STAT2+","+STAT3+")");
				
				tab[3].setRow(i+1); 
			}
			// end tab[3]
			
			
			// tab[4]
			System.out.println("Synchronzation started for sap_pa0001");
			tab[4].firstRow();
			
			for(int i = 0; i < tab[4].getNumRows(); i++){
				
				int MANDT = tab[4].getInt("MANDT");
				long PERNR = tab[4].getLong("PERNR");
				String SUBTY = Utilities.filterString(tab[4].getString("SUBTY"), 1, 100);
				String OBJPS = Utilities.filterString(tab[4].getString("OBJPS"), 1, 100);
				String SPRPS = Utilities.filterString(tab[4].getString("SPRPS"), 1, 100);
				String ENDDA = Utilities.filterString(tab[4].getString("ENDDA"), 1, 100);
				String BEGDA = Utilities.filterString(tab[4].getString("BEGDA"), 1, 100);
				long SEQNR = tab[4].getLong("SEQNR");
				String AEDTM = Utilities.filterString(tab[4].getString("AEDTM"), 1, 100);
				String UNAME = Utilities.filterString(tab[4].getString("UNAME"), 1, 100);
				String HISTO = Utilities.filterString(tab[4].getString("HISTO"), 1, 100);
				String ITXEX = Utilities.filterString(tab[4].getString("ITXEX"), 1, 100);
				String REFEX = Utilities.filterString(tab[4].getString("REFEX"), 1, 100);
				String ORDEX = Utilities.filterString(tab[4].getString("ORDEX"), 1, 100);
				String ITBLD = Utilities.filterString(tab[4].getString("ITBLD"), 1, 100);
				String PREAS = Utilities.filterString(tab[4].getString("PREAS"), 1, 100);
				String FLAG1 = Utilities.filterString(tab[4].getString("FLAG1"), 1, 100);
				String FLAG2 = Utilities.filterString(tab[4].getString("FLAG2"), 1, 100);
				String FLAG3 = Utilities.filterString(tab[4].getString("FLAG3"), 1, 100);
				String FLAG4 = Utilities.filterString(tab[4].getString("FLAG4"), 1, 100);
				String RESE1 = Utilities.filterString(tab[4].getString("RESE1"), 1, 100);
				String RESE2 = Utilities.filterString(tab[4].getString("RESE2"), 1, 100);
				String GRPVL = Utilities.filterString(tab[4].getString("GRPVL"), 1, 100);
				
				String BUKRS = Utilities.filterString(tab[4].getString("BUKRS"), 1, 100);
				String WERKS = Utilities.filterString(tab[4].getString("WERKS"), 1, 100);
				String PERSG = Utilities.filterString(tab[4].getString("PERSG"), 1, 100);
				String PERSK = Utilities.filterString(tab[4].getString("PERSK"), 1, 100);
				String VDSK1 = Utilities.filterString(tab[4].getString("VDSK1"), 1, 100);
				String GSBER = Utilities.filterString(tab[4].getString("GSBER"), 1, 100);
				String BTRTL = Utilities.filterString(tab[4].getString("BTRTL"), 1, 100);
				String JUPER = Utilities.filterString(tab[4].getString("JUPER"), 1, 100);
				String ABKRS = Utilities.filterString(tab[4].getString("ABKRS"), 1, 100);
				String ANSVH = Utilities.filterString(tab[4].getString("ANSVH"), 1, 100);
				long KOSTL = tab[4].getLong("KOSTL");
				long ORGEH = tab[4].getLong("ORGEH");
				long PLANS = tab[4].getLong("PLANS");
				long STELL = tab[4].getLong("STELL");
				String MSTBR = Utilities.filterString(tab[4].getString("MSTBR"), 1, 100);
				String SACHA = Utilities.filterString(tab[4].getString("SACHA"), 1, 100);
				String SACHP = Utilities.filterString(tab[4].getString("SACHP"), 1, 100);
				String SACHZ = Utilities.filterString(tab[4].getString("SACHZ"), 1, 100);
				String SNAME = Utilities.filterString(tab[4].getString("SNAME"), 1, 100);
				String ENAME = Utilities.filterString(tab[4].getString("ENAME"), 1, 100);
				String OTYPE = Utilities.filterString(tab[4].getString("OTYPE"), 1, 100);
				String SBMOD = Utilities.filterString(tab[4].getString("SBMOD"), 1, 100);
				String KOKRS = Utilities.filterString(tab[4].getString("KOKRS"), 1, 100);
				String FISTL = Utilities.filterString(tab[4].getString("FISTL"), 1, 100);
				String GEBER = Utilities.filterString(tab[4].getString("GEBER"), 1, 100);
				String FKBER = Utilities.filterString(tab[4].getString("FKBER"), 1, 100);
				String GRANT_NBR = Utilities.filterString(tab[4].getString("GRANT_NBR"), 1, 100);
				String SGMNT = Utilities.filterString(tab[4].getString("SGMNT"), 1, 100);
				
				s.executeUpdate("REPLACE INTO `sap_pa0001`(`mandt`,`pernr`,`subty`,`objps`,`sprps`,`endda`,`begda`,`seqnr`,`aedtm`,`uname`,`histo`,`itxex`,`refex`,`ordex`,`itbld`,`preas`,`flag1`,`flag2`,`flag3`,`flag4`,`rese1`,`rese2`,`grpvl`,`bukrs`,`werks`,`persg`,`persk`,`vdsk1`,`gsber`,`btrtl`,`juper`,`abkrs`,`ansvh`,`kostl`,`orgeh`,`plans`,`stell`,`mstbr`,`sacha`,`sachp`,`sachz`,`sname`,`ename`,`otype`,`sbmod`,`kokrs`,`fistl`,`geber`,`fkber`,`grant_nbr`,`sgmnt`)VALUES("+MANDT+","+PERNR+",'"+SUBTY+"','"+OBJPS+"','"+SPRPS+"','"+ENDDA+"','"+BEGDA+"',"+SEQNR+",'"+AEDTM+"','"+UNAME+"','"+HISTO+"','"+ITXEX+"','"+REFEX+"','"+ORDEX+"','"+ITBLD+"','"+PREAS+"','"+FLAG1+"','"+FLAG2+"','"+FLAG3+"','"+FLAG4+"','"+RESE1+"','"+RESE2+"','"+GRPVL+"','"+BUKRS+"','"+WERKS+"','"+PERSG+"','"+PERSK+"','"+VDSK1+"','"+GSBER+"','"+BTRTL+"','"+JUPER+"','"+ABKRS+"','"+ANSVH+"',"+KOSTL+","+ORGEH+","+PLANS+","+STELL+",'"+MSTBR+"','"+SACHA+"','"+SACHP+"','"+SACHZ+"','"+SNAME+"','"+ENAME+"','"+OTYPE+"','"+SBMOD+"','"+KOKRS+"','"+FISTL+"','"+GEBER+"','"+FKBER+"','"+GRANT_NBR+"','"+SGMNT+"')");
				
				tab[4].setRow(i+1); 
			}
			// end tab[4]
			
			
			// tab[5]
			System.out.println("Synchronzation started for sap_pa0002");
			tab[5].firstRow();
			
			for(int i = 0; i < tab[5].getNumRows(); i++){
				
				int MANDT = tab[5].getInt("MANDT");
				long PERNR = tab[5].getLong("PERNR");
				String SUBTY = Utilities.filterString(tab[5].getString("SUBTY"), 1, 100);
				String OBJPS = Utilities.filterString(tab[5].getString("OBJPS"), 1, 100);
				String SPRPS = Utilities.filterString(tab[5].getString("SPRPS"), 1, 100);
				String ENDDA = Utilities.filterString(tab[5].getString("ENDDA"), 1, 100);
				String BEGDA = Utilities.filterString(tab[5].getString("BEGDA"), 1, 100);
				long SEQNR = tab[5].getLong("SEQNR");
				String AEDTM = Utilities.filterString(tab[5].getString("AEDTM"), 1, 100);
				String UNAME = Utilities.filterString(tab[5].getString("UNAME"), 1, 100);
				String HISTO = Utilities.filterString(tab[5].getString("HISTO"), 1, 100);
				String ITXEX = Utilities.filterString(tab[5].getString("ITXEX"), 1, 100);
				String REFEX = Utilities.filterString(tab[5].getString("REFEX"), 1, 100);
				String ORDEX = Utilities.filterString(tab[5].getString("ORDEX"), 1, 100);
				String ITBLD = Utilities.filterString(tab[5].getString("ITBLD"), 1, 100);
				String PREAS = Utilities.filterString(tab[5].getString("PREAS"), 1, 100);
				String FLAG1 = Utilities.filterString(tab[5].getString("FLAG1"), 1, 100);
				String FLAG2 = Utilities.filterString(tab[5].getString("FLAG2"), 1, 100);
				String FLAG3 = Utilities.filterString(tab[5].getString("FLAG3"), 1, 100);
				String FLAG4 = Utilities.filterString(tab[5].getString("FLAG4"), 1, 100);
				String RESE1 = Utilities.filterString(tab[5].getString("RESE1"), 1, 100);
				String RESE2 = Utilities.filterString(tab[5].getString("RESE2"), 1, 100);
				String GRPVL = Utilities.filterString(tab[5].getString("GRPVL"), 1, 100);
				
				String INITS = Utilities.filterString(tab[5].getString("INITS"), 1, 100);
				String NACHN = Utilities.filterString(tab[5].getString("NACHN"), 1, 100);
				String NAME2 = Utilities.filterString(tab[5].getString("NAME2"), 1, 100);
				String NACH2 = Utilities.filterString(tab[5].getString("NACH2"), 1, 100);
				String VORNA = Utilities.filterString(tab[5].getString("VORNA"), 1, 100);
				String CNAME = Utilities.filterString(tab[5].getString("CNAME"), 1, 100);
				String TITEL = Utilities.filterString(tab[5].getString("TITEL"), 1, 100);
				String TITL2 = Utilities.filterString(tab[5].getString("TITL2"), 1, 100);
				String NAMZU = Utilities.filterString(tab[5].getString("NAMZU"), 1, 100);
				String VORSW = Utilities.filterString(tab[5].getString("VORSW"), 1, 100);
				
				String VORS2 = Utilities.filterString(tab[5].getString("VORS2"), 1, 100);
				String RUFNM = Utilities.filterString(tab[5].getString("RUFNM"), 1, 100);
				String MIDNM = Utilities.filterString(tab[5].getString("MIDNM"), 1, 100);
				
				long KNZNM = tab[5].getLong("KNZNM");
				long ANRED = tab[5].getLong("ANRED");
				long GESCH = tab[5].getLong("GESCH");
				
				String GBDAT = Utilities.filterString(tab[5].getString("GBDAT"), 1, 100);
				String GBLND = Utilities.filterString(tab[5].getString("GBLND"), 1, 100);
				String GBDEP = Utilities.filterString(tab[5].getString("GBDEP"), 1, 100);
				String GBORT = Utilities.filterString(tab[5].getString("GBORT"), 1, 100);
				String NATIO = Utilities.filterString(tab[5].getString("NATIO"), 1, 100);
				String NATI2 = Utilities.filterString(tab[5].getString("NATI2"), 1, 100);
				String NATI3 = Utilities.filterString(tab[5].getString("NATI3"), 1, 100);
				String SPRSL = Utilities.filterString(tab[5].getString("SPRSL"), 1, 100);
				long KONFE = tab[5].getLong("KONFE");
				long FAMST = tab[5].getLong("FAMST");
				String FAMDT = Utilities.filterString(tab[5].getString("FAMDT"), 1, 100);
				long ANZKD = tab[5].getLong("ANZKD");
				String NACON = Utilities.filterString(tab[5].getString("NACON"), 1, 100);
				String PERMO = Utilities.filterString(tab[5].getString("PERMO"), 1, 100);
				String PERID = Utilities.filterString(tab[5].getString("PERID"), 1, 100);
				
				String GBPAS = Utilities.filterString(tab[5].getString("GBPAS"), 1, 100);
				String FNAMK = Utilities.filterString(tab[5].getString("FNAMK"), 1, 100);
				String LNAMK = Utilities.filterString(tab[5].getString("LNAMK"), 1, 100);
				String FNAMR = Utilities.filterString(tab[5].getString("FNAMR"), 1, 100);
				String LNAMR = Utilities.filterString(tab[5].getString("LNAMR"), 1, 100);
				String NABIK = Utilities.filterString(tab[5].getString("NABIK"), 1, 100);
				String NABIR = Utilities.filterString(tab[5].getString("NABIR"), 1, 100);
				String NICKK = Utilities.filterString(tab[5].getString("NICKK"), 1, 100);
				String NICKR = Utilities.filterString(tab[5].getString("NICKR"), 1, 100);
				String GBJHR = Utilities.filterString(tab[5].getString("GBJHR"), 1, 100);
				String GBMON = Utilities.filterString(tab[5].getString("GBMON"), 1, 100);
				String GBTAG = Utilities.filterString(tab[5].getString("GBTAG"), 1, 100);
				String NCHMC = Utilities.filterString(tab[5].getString("NCHMC"), 1, 100);
				String VNAMC = Utilities.filterString(tab[5].getString("VNAMC"), 1, 100);
				String NAMZ2 = Utilities.filterString(tab[5].getString("NAMZ2"), 1, 100);
				

				s.executeUpdate("REPLACE INTO `sap_pa0002`(`mandt`,`pernr`,`subty`,`objps`,`sprps`,`endda`,`begda`,`seqnr`,`aedtm`,`uname`,`histo`,`itxex`,`refex`,`ordex`,`itbld`,`preas`,`flag1`,`flag2`,`flag3`,`flag4`,`rese1`,`rese2`,`grpvl`,`inits`,`nachn`,`name2`,`nach2`,`vorna`,`cname`,`titel`,`titl2`,`namzu`,`vorsw`,`vors2`,`rufnm`,`midnm`,`knznm`,`anred`,`gesch`,`gbdat`,`gblnd`,`gbdep`,`gbort`,`natio`,`nati2`,`nati3`,`sprsl`,`konfe`,`famst`,`famdt`,`anzkd`,`nacon`,`permo`,`perid`,`gbpas`,`fnamk`,`lnamk`,`fnamr`,`lnamr`,`nabik`,`nabir`,`nickk`,`nickr`,`gbjhr`,`gbmon`,`gbtag`,`nchmc`,`vnamc`,`namz2`)VALUES("+MANDT+","+PERNR+",'"+SUBTY+"','"+OBJPS+"','"+SPRPS+"','"+ENDDA+"','"+BEGDA+"',"+SEQNR+",'"+AEDTM+"','"+UNAME+"','"+HISTO+"','"+ITXEX+"','"+REFEX+"','"+ORDEX+"','"+ITBLD+"','"+PREAS+"','"+FLAG1+"','"+FLAG2+"','"+FLAG3+"','"+FLAG4+"','"+RESE1+"','"+RESE2+"','"+GRPVL+"','"+INITS+"','"+NACHN+"','"+NAME2+"','"+NACH2+"','"+VORNA+"','"+CNAME+"','"+TITEL+"','"+TITL2+"','"+NAMZU+"','"+VORSW+"','"+VORS2+"','"+RUFNM+"','"+MIDNM+"',"+KNZNM+","+ANRED+","+GESCH+",'"+GBDAT+"','"+GBLND+"','"+GBDEP+"','"+GBORT+"','"+NATIO+"','"+NATI2+"','"+NATI3+"','"+SPRSL+"',"+KONFE+","+FAMST+",'"+FAMDT+"',"+ANZKD+",'"+NACON+"','"+PERMO+"','"+PERID+"','"+GBPAS+"','"+FNAMK+"','"+LNAMK+"','"+FNAMR+"','"+LNAMR+"','"+NABIK+"','"+NABIR+"','"+NICKK+"','"+NICKR+"',"+GBJHR+","+GBMON+","+GBTAG+",'"+NCHMC+"','"+VNAMC+"','"+NAMZ2+"')");
				
				tab[5].setRow(i+1); 
			}
			// end tab[5]
			
			System.out.println("Synchronzation started for sap_pa0003");
			tab[6].firstRow();
			
			for(int i = 0; i < tab[6].getNumRows(); i++){
				int MANDT = tab[6].getInt("MANDT");
				long PERNR = tab[6].getLong("PERNR");
				String ABRSP = tab[6].getString("ABRSP");
				
				int ABRSP_int = 0;
				if (ABRSP != null && ABRSP.toLowerCase().equals("x")){
					ABRSP_int = 1;
				}
				
				String ENDDA = Utilities.filterString(tab[6].getString("ENDDA"), 1, 100);
				String BEGDA = Utilities.filterString(tab[6].getString("BEGDA"), 1, 100);
				
				s.executeUpdate("replace INTO `sap_pa0003`(`mandt`,`pernr`,`endda`,`begda`,`abrsp`)VALUES("+MANDT+","+PERNR+",'"+ENDDA+"','"+BEGDA+"',"+ABRSP_int+")");
				tab[6].setRow(i+1); 
			}
			
			System.out.println("Done.");
			
			ImportUsers();
			
			s.close();
			ds.dropConnection();			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void ImportUsers(){
		
		Datasource ds = new Datasource();
		
		try{
			
			ds.createConnection();
			Statement s = ds.createStatement();
			Statement s1 = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			ResultSet rs = s.executeQuery("SELECT * FROM employee_view");
			while(rs.next()){
				
				long UserID = rs.getLong("sap_code");
				String FirstName = rs.getString("first_name");
				String LastName = rs.getString("last_name");
				String DisplayName = FirstName + " " + LastName; 
				String Department = rs.getString("department_label");
				String Designation = rs.getString("designation_label");
				int Status2 = rs.getInt("status2");
				int isPayrollBlocked = rs.getInt("is_payroll_blocked");
				
				if (UserID != 1){
				
					ResultSet rs1 = s1.executeQuery("SELECT * FROM users where id="+UserID);
					if(rs1.first()){
						
						if(isPayrollBlocked == 0){
							s2.executeUpdate("update users set department='"+Department+"', designation='"+Designation+"' where id="+UserID);
						}else{
							s2.executeUpdate("update users set department='"+Department+"', designation='"+Designation+"', is_active=0 where id="+UserID);
						}
						
						
					}else{
						if(isPayrollBlocked == 0){
							s2.executeUpdate("INSERT INTO `users`(`ID`,`PASSWORD`,`FIRST_NAME`,`LAST_NAME`,`DISPLAY_NAME`,`DESIGNATION`,`DEPARTMENT`,`EMAIL`,`IS_ACTIVE`,`type_id`,`distributor_id`,`default_distributor_group`,`current_reporting_to`,`current_reporting_level`)VALUES("+UserID+",'','"+FirstName+"','"+LastName+"','"+DisplayName+"','"+Designation+"','"+Department+"',null,0,1,null,0,null,null)");
						}
					}
					
				}
				
			}
			
			s2.close();
			s1.close();
			s.close();
		
		}catch(Exception e){
			System.out.println(e);
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	} 

}
