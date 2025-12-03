package com.pbc.mrd;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.pbc.util.Datasource;
import com.pbc.util.Utilities;
import com.pbc.util.MaxLength;
import com.pbc.workflow.Workflow;
import com.pbc.workflow.WorkflowChat;


@WebServlet(description = "MRD Generate Report", urlPatterns = { "/mrd/MRDGenerateReport" })
public class MRDGenerateReport extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private double HighVolumnPepsiShare = 0;
	private double HighVolumnCokeShare = 0;
	private double HighVolumnGourmetShare = 0;
	
	private double HighVolumnSSRBPepsiShare = 0;
	private double HighVolumnSSRBCokeShare = 0;
	private double HighVolumnSSRBGourmetShare = 0;
	
	private double CaptivePepsiShare = 0;
	private double CaptiveCokeShare = 0;
	private double CaptiveGourmetShare = 0;
	
	private double CaptiveSSRBPepsiShare = 0;
	private double CaptiveSSRBCokeShare = 0;
	private double CaptiveSSRBGourmetShare = 0;
	
	private double MainSideVillagePepsiShare = 0;
	private double MainSideVillageCokeShare = 0;
	private double MainSideVillageGourmetShare = 0;
	
	private double MainSideVillageSSRBPepsiShare = 0;
	private double MainSideVillageSSRBCokeShare = 0;
	private double MainSideVillageSSRBGourmetShare = 0;
	
	private int HighVolumeWeightage = 0;
	private int HighVolumeSSRBWeightage = 0;
	private int CaptiveWeightage = 0;
	private int CaptiveSSRBWeightage = 0;
	private int MainSideVillageWeightage = 0;
	private int MainSideVillageSSRBWeightage = 0;
	
	private Datasource ds;
	
	private int TotalPepsi250mlStock = 0;
	private int TotalCoke250mlStock = 0;
	
	private int TotalPepsi250mlSSRBStock = 0;
	private int TotalCoke250mlSSRBStock = 0;
       
    public MRDGenerateReport() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		 
		String UserID = null;

		if (session.getAttribute("UserID") != null){
			UserID = (String)session.getAttribute("UserID");
		}
		
		if (UserID == null){
			response.sendRedirect(com.pbc.util.Utilities.getSessionExpiredPageURL(request));
		}
		
		long DistributorID =  Utilities.parseLong(request.getParameter("DistributorID"));
		Date Date = Utilities.parseDate(request.getParameter("Date"));

		PrintWriter out = response.getWriter();
		
		this.ds = new Datasource();
		JSONObject obj = new JSONObject();
		
		try {
			
			
			ds.createConnection();
			ds.startTransaction();
			
			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			
			int OutOfStockAllPacks = 0;
			int OutOfStock250MLPack = 0;
			int OutOfStock2250MLPack = 0;
			int OutOfStock1500MLPack = 0;
			int OutOfStock1000MLPack = 0;
			int OutOfStock500MLPack = 0;
			
			int TotalNumOfOutlets = 0;
			double TotalSKU = 0;
			double SurveySKU = 0;
			
			//double TotalNoOfChillers = 0;
			double SurveyPepsiChillerSellingCoke = 0;
			double SurveyPepsiChillerSellingGourmet = 0;
			
			String SQLOutOfStock = "SELECT sum(ifnull( (SELECT 0 FROM mrd_research_stock_availability where id=mr.id and company_id=1 limit 1), 1 )) all_packs, sum(ifnull( (SELECT 0 FROM mrd_research_stock_availability where id=mr.id and company_id=1 and package_id=11 limit 1), 1 )) 250ml_pack, sum(ifnull( (SELECT 0 FROM mrd_research_stock_availability where id=mr.id and company_id=1 and package_id=5 limit 1), 1 )) 2250ml_pack, sum(ifnull( (SELECT 0 FROM mrd_research_stock_availability where id=mr.id and company_id=1 and package_id=2 limit 1), 1 )) 1500ml_pack, sum(ifnull( (SELECT 0 FROM mrd_research_stock_availability where id=mr.id and company_id=1 and package_id=3 limit 1), 1 )) 1000ml_pack, sum(ifnull( (SELECT 0 FROM mrd_research_stock_availability where id=mr.id and company_id=1 and package_id=6 limit 1), 1 )) 500ml_pack, count(mr.id) total_outlets, sum((SELECT count(mss.id) FROM mrd_research_stock_availability_brands mrsab join mrd_survey_sku mss on mrsab.package_id=mss.package_id and mrsab.brand_id=mss.brand_id where mrsab.id=mr.id and mrsab.company_id=1)) survey_sku, sum(mr.pepsi_chiller_selling_coke) total_pepsi_chiller_selling_coke, sum(mr.pepsi_chiller_selling_gourmet) total_pepsi_chiller_selling_gourmet, sum(mrcc.no_of_chillers) total_no_of_chillers FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrcc.company_id=1";
			//System.out.println(SQLOutOfStock);
			ResultSet rs2 = s.executeQuery(SQLOutOfStock);
			if(rs2.first()){
				OutOfStockAllPacks = rs2.getInt("all_packs");
				OutOfStock250MLPack = rs2.getInt("250ml_pack");
				OutOfStock2250MLPack = rs2.getInt("2250ml_pack");
				OutOfStock1500MLPack = rs2.getInt("1500ml_pack");
				OutOfStock1000MLPack = rs2.getInt("1000ml_pack");
				OutOfStock500MLPack = rs2.getInt("500ml_pack");
				TotalNumOfOutlets = rs2.getInt("total_outlets");
				SurveySKU = rs2.getInt("survey_sku");
				SurveyPepsiChillerSellingCoke = rs2.getInt("total_pepsi_chiller_selling_coke");
				SurveyPepsiChillerSellingGourmet = rs2.getInt("total_pepsi_chiller_selling_gourmet");
				//TotalNoOfChillers = rs2.getInt("total_no_of_chillers");
			}
			
			TotalSKU = TotalNumOfOutlets * 26;
			double SKUPercentage = (SurveySKU / TotalSKU) * 100;
			
			double PepsiChillerSellingCokePercentage = ( SurveyPepsiChillerSellingCoke / TotalNumOfOutlets ) * 100;
			double PepsiChillerSellingGourmetPercentage = ( SurveyPepsiChillerSellingGourmet / TotalNumOfOutlets ) * 100;
			
			s.executeUpdate("delete FROM mrd_survey_summary where distributor_id="+DistributorID+" and report_date="+Utilities.getSQLDate(Date));			
			s.executeUpdate("INSERT INTO `mrd_survey_summary`(`distributor_id`, `report_date`, `gourmet_all_packs`,`gourmet_ssrb`,`coke_all_packs`,`coke_ssrb`,`pepsi_all_packs`,`pepsi_ssrb`,`pago_all_packs`,`pago_ssrb`,`yago_all_packs`,`yago_ssrb`,`sku_coverage_current`,`sku_coverage_pago`,`out_of_stock_outlet_all_packs`,`out_of_stock_outlet_250`,`out_of_stock_outlet_2250`,`out_of_stock_outlet_1500`,`out_of_stock_outlet_1000`,`out_of_stock_outlet_500`,`pepsi_chiller_selling_coke_current`,`pepsi_chiller_selling_coke_pago`,`pepsi_chiller_selling_gourmet_current`,`pepsi_chiller_selling_gourmet_pago`, `created_on`, `created_by`)VALUES("+DistributorID+","+Utilities.getSQLDate(Date)+",null,null,null,null,null,null,null,null,null,null,"+SKUPercentage+",null,"+OutOfStockAllPacks+","+OutOfStock250MLPack+","+OutOfStock2250MLPack+","+OutOfStock1500MLPack+","+OutOfStock1000MLPack+","+OutOfStock500MLPack+","+PepsiChillerSellingCokePercentage+",null,"+PepsiChillerSellingGourmetPercentage+",null, now(), "+UserID+")");
			
			long SurveySummaryID = 0;
			ResultSet rs = s.executeQuery("select last_insert_id()");
			if(rs.first()){
				SurveySummaryID = rs.getLong(1);
			}
			
			///// update pago, yago
			
			Date DateYearAgo = Utilities.getDateByDays(Date, -365);
			ResultSet rs12 = s.executeQuery("select (SELECT pepsi FROM mrd_research_history_market_share_all_packs where distributor_id="+DistributorID+" order by survey_date desc limit 1) pago, (SELECT pepsi FROM mrd_research_history_market_share_all_packs where distributor_id="+DistributorID+" order by abs(datediff(survey_date, "+Utilities.getSQLDateNext(DateYearAgo)+"))  limit 1) yago, (SELECT pepsi FROM mrd_research_history_market_share_ssrb where distributor_id="+DistributorID+" order by survey_date desc limit 1) pago_ssrb, (SELECT pepsi FROM mrd_research_history_market_share_ssrb where distributor_id="+DistributorID+" order by abs(datediff(survey_date, "+Utilities.getSQLDateNext(DateYearAgo)+"))  limit 1) yago_ssrb, (SELECT coverage FROM mrd_research_history_market_share_26_sku_coverage where distributor_id="+DistributorID+" order by survey_date desc limit 1) page_sku_coverage, (SELECT percentage FROM mrd_research_history_market_share_pcsc where distributor_id="+DistributorID+" order by survey_date desc limit 1) pago_pepsi_chiller_selling_coke, (SELECT percentage FROM mrd_research_history_market_share_pcsc where distributor_id="+DistributorID+" order by abs(datediff(survey_date, "+Utilities.getSQLDateNext(DateYearAgo)+")) limit 1) yago_pepsi_chiller_selling_coke, (SELECT percentage FROM mrd_research_history_market_share_pcsg where distributor_id="+DistributorID+" order by survey_date desc limit 1) pago_pepsi_chiller_selling_gourmet, (SELECT percentage FROM mrd_research_history_market_share_pcsg where distributor_id="+DistributorID+" order by abs(datediff(survey_date, "+Utilities.getSQLDateNext(DateYearAgo)+")) limit 1) yago_pepsi_chiller_selling_gourmet");
			if(rs12.first()){
				s2.executeUpdate("update mrd_survey_summary set pago_all_packs="+rs12.getString("pago")+", yago_all_packs="+rs12.getString("yago")+", pago_ssrb="+rs12.getString("pago_ssrb")+", yago_ssrb="+rs12.getString("yago_ssrb")+", sku_coverage_pago="+rs12.getString("page_sku_coverage")+", pepsi_chiller_selling_coke_pago="+rs12.getString("pago_pepsi_chiller_selling_coke")+", pepsi_chiller_selling_coke_yago="+rs12.getString("yago_pepsi_chiller_selling_coke")+", pepsi_chiller_selling_gourmet_pago="+rs12.getString("pago_pepsi_chiller_selling_gourmet")+", pepsi_chiller_selling_gourmet_yago="+rs12.getString("yago_pepsi_chiller_selling_gourmet")+"  where id="+SurveySummaryID);
			}
			
			///////////////////////////////////
			
			double TotalNumOfOutletsWithChillers = 0;
			ResultSet rs11 = s.executeQuery("SELECT count( distinct mr.outlet_id) FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" ");
			if(rs11.first()){
				TotalNumOfOutletsWithChillers = rs11.getDouble(1);
			}
			
			ResultSet rs3 = s.executeQuery("SELECT mr.id, mr.outlet_id, mrsab.company_id, mrsab.package_id, count(mrsab.brand_id) FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsab on mr.id=mrcc.id and mr.id=mrsab.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsab.company_id in (1, 2, 3) and mrsab.package_id in (11, 1, 5, 2, 3, 6) and mrsab.quantity >= 3 group by mr.id, mr.outlet_id, mrsab.company_id, mrsab.package_id");
			while(rs3.next()){
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_distribution_csd`(`id`,`outlet_id`,`company_id`,`package_id`)VALUES("+SurveySummaryID+","+rs3.getString("outlet_id")+","+rs3.getString("company_id")+","+rs3.getString("package_id")+")");
			}
			
			ResultSet rs4 = s.executeQuery("SELECT count(distinct outlet_id) total_outlets, "+
							"(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=1 and package_id=11 and id=mssdc.id) total_pepsi_250ml_packs,"+ 
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=1 and package_id=1 and id=mssdc.id) total_pepsi_1000mlg_packs,"+ 
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=1 and package_id=5 and id=mssdc.id) total_pepsi_2250ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=1 and package_id=2 and id=mssdc.id) total_pepsi_1500ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=1 and package_id=3 and id=mssdc.id) total_pepsi_1000ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=1 and package_id=6 and id=mssdc.id) total_pepsi_500ml_packs,"+
						    
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=2 and package_id=11 and id=mssdc.id) total_coke_250ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=2 and package_id=1 and id=mssdc.id) total_coke_1000mlg_packs, "+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=2 and package_id=5 and id=mssdc.id) total_coke_2250ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=2 and package_id=2 and id=mssdc.id) total_coke_1500ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=2 and package_id=3 and id=mssdc.id) total_coke_1000ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=2 and package_id=6 and id=mssdc.id) total_coke_500ml_packs,"+
						    
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=3 and package_id=11 and id=mssdc.id) total_gourmet_250ml_packs,"+ 
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=3 and package_id=1 and id=mssdc.id) total_gourmet_1000mlg_packs, "+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=3 and package_id=5 and id=mssdc.id) total_gourmet_2250ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=3 and package_id=2 and id=mssdc.id) total_gourmet_1500ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=3 and package_id=3 and id=mssdc.id) total_gourmet_1000ml_packs,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_csd where company_id=3 and package_id=6 and id=mssdc.id) total_gourmet_500ml_packs"+
					" FROM mrd_survey_summary_distribution_csd mssdc where mssdc.id="+SurveySummaryID);
			if(rs4.first()){
				double TotalOutlets = TotalNumOfOutletsWithChillers;
				
				double TotalPepsi250ml = rs4.getDouble("total_pepsi_250ml_packs");
				double TotalPepsi1000mlg = rs4.getDouble("total_pepsi_1000mlg_packs");
				double TotalPepsi2250ml = rs4.getDouble("total_pepsi_2250ml_packs");
				double TotalPepsi1500ml = rs4.getDouble("total_pepsi_1500ml_packs");
				double TotalPepsi1000ml = rs4.getDouble("total_pepsi_1000ml_packs");
				double TotalPepsi500ml = rs4.getDouble("total_pepsi_500ml_packs");
				
				double TotalCoke250ml = rs4.getDouble("total_coke_250ml_packs");
				double TotalCoke1000mlg = rs4.getDouble("total_coke_1000mlg_packs");
				double TotalCoke2250ml = rs4.getDouble("total_coke_2250ml_packs");
				double TotalCoke1500ml = rs4.getDouble("total_coke_1500ml_packs");
				double TotalCoke1000ml = rs4.getDouble("total_coke_1000ml_packs");
				double TotalCoke500ml = rs4.getDouble("total_coke_500ml_packs");
				
				double TotalGourmet250ml = rs4.getDouble("total_gourmet_250ml_packs");
				double TotalGourmet1000mlg = rs4.getDouble("total_gourmet_1000mlg_packs");
				double TotalGourmet2250ml = rs4.getDouble("total_gourmet_2250ml_packs");
				double TotalGourmet1500ml = rs4.getDouble("total_gourmet_1500ml_packs");
				double TotalGourmet1000ml = rs4.getDouble("total_gourmet_1000ml_packs");
				double TotalGourmet500ml = rs4.getDouble("total_gourmet_500ml_packs");
				
				double TotalPepsi250mlPercent = (TotalPepsi250ml / TotalOutlets) * 100;
				double TotalPepsi1000mlgPercent = (TotalPepsi1000mlg / TotalOutlets) * 100;
				double TotalPepsi2250mlPercent = (TotalPepsi2250ml / TotalOutlets) * 100;
				double TotalPepsi1500mlPercent = (TotalPepsi1500ml / TotalOutlets) * 100;
				double TotalPepsi1000mlPercent = (TotalPepsi1000ml / TotalOutlets) * 100;
				double TotalPepsi500mlPercent = (TotalPepsi500ml / TotalOutlets) * 100;
				
				double TotalCoke250mlPercent = (TotalCoke250ml / TotalOutlets) * 100;
				double TotalCoke1000mlgPercent = (TotalCoke1000mlg / TotalOutlets) * 100;
				double TotalCoke2250mlPercent = (TotalCoke2250ml / TotalOutlets) * 100;
				double TotalCoke1500mlPercent = (TotalCoke1500ml / TotalOutlets) * 100;
				double TotalCoke1000mlPercent = (TotalCoke1000ml / TotalOutlets) * 100;
				double TotalCoke500mlPercent = (TotalCoke500ml / TotalOutlets) * 100;
				
				double TotalGourmet250mlPercent = (TotalGourmet250ml / TotalOutlets) * 100;
				double TotalGourmet1000mlgPercent = (TotalGourmet1000mlg / TotalOutlets) * 100;
				double TotalGourmet2250mlPercent = (TotalGourmet2250ml / TotalOutlets) * 100;
				double TotalGourmet1500mlPercent = (TotalGourmet1500ml / TotalOutlets) * 100;
				double TotalGourmet1000mlPercent = (TotalGourmet1000ml / TotalOutlets) * 100;
				double TotalGourmet500mlPercent = (TotalGourmet500ml / TotalOutlets) * 100;
				
				
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_csd`(`id`,`package_id`,`gourmet`,`coke`,`pepsi`)VALUES("+SurveySummaryID+", 11, "+TotalGourmet250mlPercent+", "+TotalCoke250mlPercent+", "+TotalPepsi250mlPercent+")");
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_csd`(`id`,`package_id`,`gourmet`,`coke`,`pepsi`)VALUES("+SurveySummaryID+", 1, "+TotalGourmet1000mlgPercent+", "+TotalCoke1000mlgPercent+", "+TotalPepsi1000mlgPercent+")");
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_csd`(`id`,`package_id`,`gourmet`,`coke`,`pepsi`)VALUES("+SurveySummaryID+", 5, "+TotalGourmet2250mlPercent+", "+TotalCoke2250mlPercent+", "+TotalPepsi2250mlPercent+")");
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_csd`(`id`,`package_id`,`gourmet`,`coke`,`pepsi`)VALUES("+SurveySummaryID+", 2, "+TotalGourmet1500mlPercent+", "+TotalCoke1500mlPercent+", "+TotalPepsi1500mlPercent+")");
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_csd`(`id`,`package_id`,`gourmet`,`coke`,`pepsi`)VALUES("+SurveySummaryID+", 3, "+TotalGourmet1000mlPercent+", "+TotalCoke1000mlPercent+", "+TotalPepsi1000mlPercent+")");
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_csd`(`id`,`package_id`,`gourmet`,`coke`,`pepsi`)VALUES("+SurveySummaryID+", 6, "+TotalGourmet500mlPercent+", "+TotalCoke500mlPercent+", "+TotalPepsi500mlPercent+")");
				
			}
			
			ResultSet rs5 = s.executeQuery("SELECT mr.id, mr.outlet_id, mrsab.company_id, mrsab.package_id, count(mrsab.brand_id) FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsab on mr.id=mrcc.id and mr.id=mrsab.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsab.company_id in (1) and mrsab.package_id in (2, 6) and mrsab.brand_id=12 and mrsab.quantity >= 3 group by mr.id, mr.outlet_id, mrsab.company_id, mrsab.package_id");
			while(rs5.next()){
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_distribution_ncb`(`id`,`outlet_id`,`package_id`,`brand_id`)VALUES("+SurveySummaryID+", "+rs5.getString("outlet_id")+","+rs5.getString("package_id")+",12)");				
			}
			
			ResultSet rs6 = s.executeQuery("SELECT count(distinct outlet_id) total_outlets,"+ 
							"(SELECT count(id) FROM mrd_survey_summary_distribution_ncb where package_id=2 and brand_id=12 and id=mssdn.id) total_pepsi_1500ml_aquafina,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_ncb where package_id=6 and brand_id=12 and id=mssdn.id) total_pepsi_500ml_aquafina"+							
						" FROM mrd_survey_summary_distribution_ncb mssdn where mssdn.id="+SurveySummaryID);
			if(rs6.first()){
				double TotalOutlets = TotalNumOfOutletsWithChillers;
				
				double TotalPepsi1500mlAquafina = rs6.getDouble("total_pepsi_1500ml_aquafina");
				double TotalPepsi500mlAquafina = rs6.getDouble("total_pepsi_500ml_aquafina");
				
				double TotalPepsi1500mlAquafinaPercent = (TotalPepsi1500mlAquafina / TotalOutlets) * 100;
				double TotalPepsi500mlAquafinaPercent = (TotalPepsi500mlAquafina / TotalOutlets) * 100;
				
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_ncb`(`id`,`package_id`,`brand_id`,`nd`)VALUES("+SurveySummaryID+",2,12,"+TotalPepsi1500mlAquafinaPercent+")");
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_ncb`(`id`,`package_id`,`brand_id`,`nd`)VALUES("+SurveySummaryID+",6,12,"+TotalPepsi500mlAquafinaPercent+")");
				
			}
			
			ResultSet rs7 = s.executeQuery("SELECT mr.id, mr.outlet_id, mrsab.company_id, mrsab.package_id, count(mrsab.brand_id) FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsab on mr.id=mrcc.id and mr.id=mrsab.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsab.company_id in (1) and mrsab.package_id in (6, 15) and mrsab.brand_id=18 and mrsab.quantity >= 3 group by mr.id, mr.outlet_id, mrsab.company_id, mrsab.package_id");
			while(rs7.next()){
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_distribution_ncb`(`id`,`outlet_id`,`package_id`,`brand_id`)VALUES("+SurveySummaryID+", "+rs7.getString("outlet_id")+","+rs7.getString("package_id")+",18)");				
			}
			
			ResultSet rs8 = s.executeQuery("SELECT count(distinct outlet_id) total_outlets,"+ 
							"(SELECT count(id) FROM mrd_survey_summary_distribution_ncb where package_id=6 and brand_id=18 and id=mssdn.id) total_pepsi_500ml_sting,"+
						    "(SELECT count(id) FROM mrd_survey_summary_distribution_ncb where package_id=15 and brand_id=18 and id=mssdn.id) total_pepsi_240ml_sting"+							
						" FROM mrd_survey_summary_distribution_ncb mssdn where mssdn.id="+SurveySummaryID);
			if(rs8.first()){
				double TotalOutlets = TotalNumOfOutletsWithChillers;
				
				double TotalPepsi500mlSting = rs8.getDouble("total_pepsi_500ml_sting");
				double TotalPepsi240mlSting = rs8.getDouble("total_pepsi_240ml_sting");
				
				double TotalPepsi500mlStingPercent = (TotalPepsi500mlSting / TotalOutlets) * 100;
				double TotalPepsi240mlStingPercent = (TotalPepsi240mlSting / TotalOutlets) * 100;
				
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_ncb`(`id`,`package_id`,`brand_id`,`nd`)VALUES("+SurveySummaryID+",6,18,"+TotalPepsi500mlStingPercent+")");
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_ncb`(`id`,`package_id`,`brand_id`,`nd`)VALUES("+SurveySummaryID+",15,18,"+TotalPepsi240mlStingPercent+")");
				
			}
			
			ResultSet rs9 = s.executeQuery("SELECT mr.id, mr.outlet_id, mrsab.company_id, mrsab.package_id, count(mrsab.brand_id) FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsab on mr.id=mrcc.id and mr.id=mrsab.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsab.company_id in (1) and mrsab.package_id in (16) and mrsab.brand_id=16 and mrsab.quantity >= 3 group by mr.id, mr.outlet_id, mrsab.company_id, mrsab.package_id");
			while(rs9.next()){
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_distribution_ncb`(`id`,`outlet_id`,`package_id`,`brand_id`)VALUES("+SurveySummaryID+", "+rs9.getString("outlet_id")+",16,16)");				
			}
						
			ResultSet rs10 = s.executeQuery("SELECT count(distinct outlet_id) total_outlets,"+ 
					"(SELECT count(id) FROM mrd_survey_summary_distribution_ncb where package_id=16 and brand_id=16 and id=mssdn.id) total_pepsi_200ml_slice"+
				" FROM mrd_survey_summary_distribution_ncb mssdn where mssdn.id="+SurveySummaryID);
			if(rs10.first()){
				double TotalOutlets = rs10.getDouble("total_outlets");
				
				double TotalPepsi200mlSlice = rs10.getDouble("total_pepsi_200ml_slice");
				
				double TotalPepsi200mlSlicePercent = (TotalPepsi200mlSlice / TotalOutlets) * 100;
				
				s2.executeUpdate("INSERT INTO `mrd_survey_summary_nd_ncb`(`id`,`package_id`,`brand_id`,`nd`)VALUES("+SurveySummaryID+",16,16,"+TotalPepsi200mlSlicePercent+")");
				
			}
			
			s.executeUpdate("delete from mrd_survey_summary_distribution_csd");
			s.executeUpdate("delete from mrd_survey_summary_distribution_ncb");
			
			///// update pago
			
			ResultSet rs13 = s.executeQuery("SELECT * FROM mrd_research_history_market_share_nd where distributor_id="+DistributorID+" order by survey_date desc limit 1");
			if(rs13.first()){
				 
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `pago`="+rs13.getString("250ml_glass")+" where `id`= "+SurveySummaryID+" and `package_id`=11");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `pago`="+rs13.getString("1000ml_glass")+" where `id`= "+SurveySummaryID+" and `package_id`=1");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `pago`="+rs13.getString("2250ml_pet")+" where `id`= "+SurveySummaryID+" and `package_id`=5");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `pago`="+rs13.getString("1500ml_pet")+" where `id`= "+SurveySummaryID+" and `package_id`=2");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `pago`="+rs13.getString("1000ml_pet")+" where `id`= "+SurveySummaryID+" and `package_id`=3");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `pago`="+rs13.getString("500ml_pet")+" where `id`= "+SurveySummaryID+" and `package_id`=6");
				
				s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `pago`="+rs13.getString("1500ml_aqua")+" where `id`= "+SurveySummaryID+" and `package_id`=2 and `brand_id`=12");
				//s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `pago`=0 where `id`= "+SurveySummaryID+" and `package_id`=6 and `brand_id`=12");
				
				s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `pago`="+rs13.getString("500ml_sting")+" where `id`= "+SurveySummaryID+" and `package_id`=6 and `brand_id`=18");
				s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `pago`="+rs13.getString("240ml_sting")+" where `id`= "+SurveySummaryID+" and `package_id`=15 and `brand_id`=18");
				
				s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `pago`="+rs13.getString("200ml_slice")+" where `id`= "+SurveySummaryID+" and `package_id`=16 and `brand_id`=16");
				
			}
			
			
			///////////////////////////////////
			
			///// update yago
			
			ResultSet rs14 = s.executeQuery("SELECT * FROM mrd_research_history_market_share_nd where distributor_id="+DistributorID+" order by abs(datediff(survey_date, "+Utilities.getSQLDateNext(DateYearAgo)+")) limit 1");
			if(rs14.first()){
				 
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `yago`="+rs14.getString("250ml_glass")+" where `id`= "+SurveySummaryID+" and `package_id`=11");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `yago`="+rs14.getString("1000ml_glass")+" where `id`= "+SurveySummaryID+" and `package_id`=1");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `yago`="+rs14.getString("2250ml_pet")+" where `id`= "+SurveySummaryID+" and `package_id`=5");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `yago`="+rs14.getString("1500ml_pet")+" where `id`= "+SurveySummaryID+" and `package_id`=2");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `yago`="+rs14.getString("1000ml_pet")+" where `id`= "+SurveySummaryID+" and `package_id`=3");
				s2.executeUpdate("update `mrd_survey_summary_nd_csd` set `yago`="+rs14.getString("500ml_pet")+" where `id`= "+SurveySummaryID+" and `package_id`=6");
				
				s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `yago`="+rs14.getString("1500ml_aqua")+" where `id`= "+SurveySummaryID+" and `package_id`=2 and `brand_id`=12");
				//s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `yago`=0 where `id`= "+SurveySummaryID+" and `package_id`=6 and `brand_id`=12");
				
				s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `yago`="+rs14.getString("500ml_sting")+" where `id`= "+SurveySummaryID+" and `package_id`=6 and `brand_id`=18");
				s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `yago`="+rs14.getString("240ml_sting")+" where `id`= "+SurveySummaryID+" and `package_id`=15 and `brand_id`=18");
				
				s2.executeUpdate("update `mrd_survey_summary_nd_ncb` set `yago`="+rs14.getString("200ml_slice")+" where `id`= "+SurveySummaryID+" and `package_id`=16 and `brand_id`=16");
				
			}
			
			
			///////////////////////////////////
			
			
			CalculateHighVolumeShare(s, DistributorID, Date);
			CalculateCaptiveShare(DistributorID, Date);			
			CalculateMainSideVillageShare(s, DistributorID, Date);
			
			CalculateHighVolumeSSRBShare(s, DistributorID, Date);
			CalculateCaptiveSSRBShare(DistributorID, Date);			
			CalculateMainSideVillageSSRBShare(s, DistributorID, Date);
			
			double TotalPepsiShare = HighVolumnPepsiShare + CaptivePepsiShare + MainSideVillagePepsiShare;
			double TotalCokeShare = HighVolumnCokeShare + CaptiveCokeShare + MainSideVillageCokeShare;
			double TotalGourmetShare = HighVolumnGourmetShare + CaptiveGourmetShare + MainSideVillageGourmetShare;
			
			double TotalSSRBPepsiShare = HighVolumnSSRBPepsiShare + CaptiveSSRBPepsiShare + MainSideVillageSSRBPepsiShare;			
			double TotalSSRBCokeShare = HighVolumnSSRBCokeShare + CaptiveSSRBCokeShare + MainSideVillageSSRBCokeShare;
			double TotalSSRBGourmetShare = HighVolumnSSRBGourmetShare + CaptiveSSRBGourmetShare + MainSideVillageSSRBGourmetShare;
			
			s.executeUpdate("update mrd_survey_summary set pepsi_all_packs="+TotalPepsiShare+", pepsi_ssrb="+TotalSSRBPepsiShare+", coke_all_packs="+TotalCokeShare+", coke_ssrb="+TotalSSRBCokeShare+", gourmet_all_packs="+TotalGourmetShare+", gourmet_ssrb="+TotalSSRBGourmetShare+" where id="+SurveySummaryID);
			
			obj.put("success", "true");
			obj.put("SurveySummaryID", SurveySummaryID);
			
			ds.commit();
			
			s2.close();
			s.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block			
			try {				
				ds.rollback();				
				obj.put("success", "false");
				obj.put("error", e.toString());
				e.printStackTrace();				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			try {
				ds.dropConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		out.print(obj);
		out.close();
		
	}
	
	public void CalculateHighVolumeShare(Statement s, long DistributorID, Date Date) throws SQLException{
		double ConvertedCasesPepsiCoolerStock = 0;
		double ConvertedCasesCokeCoolerStock = 0;
		double ConvertedCasesGourmetCoolerStock = 0;
		double ConvertedCasesPepsiFloorStock = 0;
		double ConvertedCasesCokeFloorStock = 0;
		double ConvertedCasesGourmetFloorStock = 0;
		double TotalConvertedCasesCoolerStock = 0;
		double TotalConvertedCasesFloorStock = 0;
		
		ResultSet rs12 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=1 and stock_type_id=1 and co.segment_id=1 ");
		if(rs12.first()){
			ConvertedCasesPepsiCoolerStock = rs12.getDouble(1);
		}
		
		ResultSet rs13 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=2 and stock_type_id=1 and co.segment_id=1 ");
		if(rs13.first()){
			ConvertedCasesCokeCoolerStock = rs13.getDouble(1);
		}
		
		ResultSet rs14 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=3 and stock_type_id=1 and co.segment_id=1 ");
		if(rs14.first()){
			ConvertedCasesGourmetCoolerStock = rs14.getDouble(1);
		}
		
		ResultSet rs15 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=1 and stock_type_id=2 and co.segment_id=1 ");
		if(rs15.first()){
			ConvertedCasesPepsiFloorStock = rs15.getDouble(1);
		}
		
		ResultSet rs16 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=2 and stock_type_id=2 and co.segment_id=1 ");
		if(rs16.first()){
			ConvertedCasesCokeFloorStock = rs16.getDouble(1);
		}
		
		ResultSet rs17 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=3 and stock_type_id=2 and co.segment_id=1 ");
		if(rs17.first()){
			ConvertedCasesGourmetFloorStock = rs17.getDouble(1);
		}
		
		TotalConvertedCasesCoolerStock = ConvertedCasesPepsiCoolerStock + ConvertedCasesCokeCoolerStock + ConvertedCasesGourmetCoolerStock;
		TotalConvertedCasesFloorStock = ConvertedCasesPepsiFloorStock + ConvertedCasesCokeFloorStock + ConvertedCasesGourmetFloorStock;
		
		double ConvertedCasesPepsiCoolerStockPercent = (ConvertedCasesPepsiCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesCokeCoolerStockPercent = (ConvertedCasesCokeCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesGourmetCoolerStockPercent = (ConvertedCasesGourmetCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		
		if(TotalConvertedCasesCoolerStock == 0){
			ConvertedCasesPepsiCoolerStockPercent = 0;
			ConvertedCasesCokeCoolerStockPercent = 0;
			ConvertedCasesGourmetCoolerStockPercent = 0;
		}
		
		double ConvertedCasesPepsiFloorStockPercent = (ConvertedCasesPepsiFloorStock / TotalConvertedCasesFloorStock) * 100;
		double ConvertedCasesCokeFloorStockPercent = (ConvertedCasesCokeFloorStock / TotalConvertedCasesFloorStock) * 100;
		double ConvertedCasesGourmetFloorStockPercent = (ConvertedCasesGourmetFloorStock / TotalConvertedCasesFloorStock) * 100;
		
		if(TotalConvertedCasesFloorStock == 0){
			ConvertedCasesPepsiFloorStockPercent = 0;
			ConvertedCasesCokeFloorStockPercent = 0;
			ConvertedCasesGourmetFloorStockPercent = 0;
		}
		
		int CoolerStockWeightage = 60;
		int FloorStockWeightage = 40;
		
		double PepsiShareCoolerStock = (ConvertedCasesPepsiCoolerStockPercent * CoolerStockWeightage)/100;
		double CokeShareCoolerStock = (ConvertedCasesCokeCoolerStockPercent * CoolerStockWeightage)/100;
		double GourmetShareCoolerStock = (ConvertedCasesGourmetCoolerStockPercent * CoolerStockWeightage)/100;
		
		double PepsiShareFloorStock = (ConvertedCasesPepsiFloorStockPercent * FloorStockWeightage)/100;
		double CokeShareFloorStock = (ConvertedCasesCokeFloorStockPercent * FloorStockWeightage)/100;
		double GourmetShareFloorStock = (ConvertedCasesGourmetFloorStockPercent * FloorStockWeightage)/100;
		
		double TotalPepsiStock = PepsiShareCoolerStock+PepsiShareFloorStock;
		double TotalCokeStock = CokeShareCoolerStock+CokeShareFloorStock;
		double TotalGourmetStock = GourmetShareCoolerStock+GourmetShareFloorStock;
		
		this.HighVolumeWeightage = 30;
		
		this.HighVolumnPepsiShare = (TotalPepsiStock * this.HighVolumeWeightage)/100;
		this.HighVolumnCokeShare = (TotalCokeStock * this.HighVolumeWeightage)/100;
		this.HighVolumnGourmetShare = (TotalGourmetStock * this.HighVolumeWeightage)/100;
		
	}
	
	public void CalculateHighVolumeSSRBShare(Statement s, long DistributorID, Date Date) throws SQLException{
		double ConvertedCasesPepsiCoolerStock = 0;
		double ConvertedCasesCokeCoolerStock = 0;
		double ConvertedCasesGourmetCoolerStock = 0;
		double ConvertedCasesPepsiFloorStock = 0;
		double ConvertedCasesCokeFloorStock = 0;
		double ConvertedCasesGourmetFloorStock = 0;
		double TotalConvertedCasesCoolerStock = 0;
		double TotalConvertedCasesFloorStock = 0;
		
		ResultSet rs12 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=1 and mrsa.stock_type_id=1 and mrsa.package_id in (11, 12) and co.segment_id=1 ");
		if(rs12.first()){
			ConvertedCasesPepsiCoolerStock = rs12.getDouble(1);
		}
		
		ResultSet rs13 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=2 and mrsa.stock_type_id=1 and mrsa.package_id in (11, 12) and co.segment_id=1 ");
		if(rs13.first()){
			ConvertedCasesCokeCoolerStock = rs13.getDouble(1);
		}
		
		ResultSet rs14 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=3 and mrsa.stock_type_id=1 and mrsa.package_id in (11, 12) and co.segment_id=1 ");
		if(rs14.first()){
			ConvertedCasesGourmetCoolerStock = rs14.getDouble(1);
		}
		
		ResultSet rs15 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=1 and mrsa.stock_type_id=2 and mrsa.package_id in (11, 12) and co.segment_id=1 ");
		if(rs15.first()){
			ConvertedCasesPepsiFloorStock = rs15.getDouble(1);
		}
		
		ResultSet rs16 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=2 and mrsa.stock_type_id=2 and mrsa.package_id in (11, 12) and co.segment_id=1 ");
		if(rs16.first()){
			ConvertedCasesCokeFloorStock = rs16.getDouble(1);
		}
		
		ResultSet rs17 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=3 and mrsa.stock_type_id=2 and mrsa.package_id in (11, 12) and co.segment_id=1 ");
		if(rs17.first()){
			ConvertedCasesGourmetFloorStock = rs17.getDouble(1);
		}
		
		TotalConvertedCasesCoolerStock = ConvertedCasesPepsiCoolerStock + ConvertedCasesCokeCoolerStock + ConvertedCasesGourmetCoolerStock;
		TotalConvertedCasesFloorStock = ConvertedCasesPepsiFloorStock + ConvertedCasesCokeFloorStock + ConvertedCasesGourmetFloorStock;
		
		double ConvertedCasesPepsiCoolerStockPercent = (ConvertedCasesPepsiCoolerStock / TotalConvertedCasesCoolerStock) * 100;		
		double ConvertedCasesCokeCoolerStockPercent = (ConvertedCasesCokeCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesGourmetCoolerStockPercent = (ConvertedCasesGourmetCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		
		if(TotalConvertedCasesCoolerStock == 0){
			ConvertedCasesPepsiCoolerStockPercent = 0;
			ConvertedCasesCokeCoolerStockPercent = 0;
			ConvertedCasesGourmetCoolerStockPercent = 0;
		}
		
		double ConvertedCasesPepsiFloorStockPercent = (ConvertedCasesPepsiFloorStock / TotalConvertedCasesFloorStock) * 100;
		double ConvertedCasesCokeFloorStockPercent = (ConvertedCasesCokeFloorStock / TotalConvertedCasesFloorStock) * 100;
		double ConvertedCasesGourmetFloorStockPercent = (ConvertedCasesGourmetFloorStock / TotalConvertedCasesFloorStock) * 100;
		
		if(TotalConvertedCasesFloorStock == 0){
			ConvertedCasesPepsiFloorStockPercent = 0;
			ConvertedCasesCokeFloorStockPercent = 0;
			ConvertedCasesGourmetFloorStockPercent = 0;
		}
		
		int CoolerStockWeightage = 60;
		int FloorStockWeightage = 40;
		
		double PepsiShareCoolerStock = (ConvertedCasesPepsiCoolerStockPercent * CoolerStockWeightage)/100;		
		double CokeShareCoolerStock = (ConvertedCasesCokeCoolerStockPercent * CoolerStockWeightage)/100;
		double GourmetShareCoolerStock = (ConvertedCasesGourmetCoolerStockPercent * CoolerStockWeightage)/100;
		
		double PepsiShareFloorStock = (ConvertedCasesPepsiFloorStockPercent * FloorStockWeightage)/100;
		double CokeShareFloorStock = (ConvertedCasesCokeFloorStockPercent * FloorStockWeightage)/100;
		double GourmetShareFloorStock = (ConvertedCasesGourmetFloorStockPercent * FloorStockWeightage)/100;
		
		double TotalPepsiStock = PepsiShareCoolerStock+PepsiShareFloorStock;
		double TotalCokeStock = CokeShareCoolerStock+CokeShareFloorStock;
		double TotalGourmetStock = GourmetShareCoolerStock+GourmetShareFloorStock;
		
		this.HighVolumeSSRBWeightage = 30;
		
		this.HighVolumnSSRBPepsiShare = (TotalPepsiStock * this.HighVolumeSSRBWeightage)/100;
		this.HighVolumnSSRBCokeShare = (TotalCokeStock * this.HighVolumeSSRBWeightage)/100;
		this.HighVolumnSSRBGourmetShare = (TotalGourmetStock * this.HighVolumeSSRBWeightage)/100;
		
	}
	
	public void CalculateCaptiveShare(long DistributorID, Date Date) throws SQLException{
		
		Statement s = this.ds.c.createStatement();
		Statement s2 = this.ds.c.createStatement();
		Statement s3 = this.ds.c.createStatement();
		
		double ConvertedCasesPepsiCoolerStock = 0;
		double ConvertedCasesCokeCoolerStock = 0;
		double ConvertedCasesGourmetCoolerStock = 0;
		double TotalConvertedCasesCoolerStock = 0;
		
		int TotalNumOfOutlets = 0;
		ResultSet rs = s.executeQuery("SELECT count(id) FROM common_outlets where distributor_id="+DistributorID);
		if(rs.first()){
			TotalNumOfOutlets = rs.getInt(1);
		}
		
		int TotalNumOfCaptiveOutlets = 0;
		ResultSet rs2 = s.executeQuery("SELECT count(id) FROM common_outlets where distributor_id="+DistributorID+" and segment_id=5");
		if(rs2.first()){
			TotalNumOfCaptiveOutlets = rs2.getInt(1);
		}
		
		int CaptiveWeightage = TotalNumOfCaptiveOutlets * 3;
		if(CaptiveWeightage >= 30){
			CaptiveWeightage = 30;
		}
		this.CaptiveWeightage = CaptiveWeightage;
		
		ResultSet rs12 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=1 and stock_type_id=1 and co.segment_id=5 and mrsa.package_id != 11  ");
		if(rs12.first()){
			ConvertedCasesPepsiCoolerStock = rs12.getDouble(1);
		}
		
		ResultSet rs13 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=2 and stock_type_id=1 and co.segment_id=5 and mrsa.package_id != 11  ");
		if(rs13.first()){
			ConvertedCasesCokeCoolerStock = rs13.getDouble(1);
		}
		
		ResultSet rs14 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=3 and stock_type_id=1 and co.segment_id=5 and mrsa.package_id != 11  ");
		if(rs14.first()){
			ConvertedCasesGourmetCoolerStock = rs14.getDouble(1);
		}
		
		TotalPepsi250mlStock = 0;
		TotalCoke250mlStock = 0;
		ResultSet rs3 = s.executeQuery("SELECT id, agreed_daily_average_sales FROM common_outlets where distributor_id="+DistributorID+" and segment_id=5 and agreed_daily_average_sales is not null");
		while(rs3.next()){
			int DailyAvgSale = rs3.getInt("agreed_daily_average_sales");
			
			ResultSet rs4 = s2.executeQuery("SELECT id, (select count(no_of_chillers) from mrd_research_company_chillers where id=mr.id and company_id=1) pepsi_chillers, (select count(no_of_chillers) from mrd_research_company_chillers where id=mr.id and company_id=2) coke_chillers FROM mrd_research mr where mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mr.outlet_id="+rs3.getString(1));
			if(rs4.first()){// if outlet survey exists
				
				int PepsiChillers = rs4.getInt("pepsi_chillers");
				int CokeChillers = rs4.getInt("coke_chillers");
				if(PepsiChillers > 0 || CokeChillers > 0){
					AllocateAvgSale250ml(rs3.getInt(1), DailyAvgSale, PepsiChillers, CokeChillers);
				}
			}else{// otherwise
				ResultSet rs5 = s2.executeQuery("SELECT id, (select count(no_of_chillers) from mrd_research_company_chillers where id=mr.id and company_id=1) pepsi_chillers, (select count(no_of_chillers) from mrd_research_company_chillers where id=mr.id and company_id=2) coke_chillers FROM mrd_research mr where mr.outlet_id="+rs3.getString(1)+" order by created_on desc limit 1 ");
				if(rs5.first()){// pick last survey
					int PepsiChillers = rs5.getInt("pepsi_chillers");
					int CokeChillers = rs5.getInt("coke_chillers");
					if(PepsiChillers > 0 || CokeChillers > 0){
						AllocateAvgSale250ml(rs3.getInt(1), DailyAvgSale, PepsiChillers, CokeChillers);
					}
				}
			}
		}
		
		ConvertedCasesPepsiCoolerStock += TotalPepsi250mlStock;
		ConvertedCasesCokeCoolerStock += TotalCoke250mlStock;
		
		TotalConvertedCasesCoolerStock = ConvertedCasesPepsiCoolerStock + ConvertedCasesCokeCoolerStock + ConvertedCasesGourmetCoolerStock;
		
		double ConvertedCasesPepsiCoolerStockPercent = (ConvertedCasesPepsiCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesCokeCoolerStockPercent = (ConvertedCasesCokeCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesGourmetCoolerStockPercent = (ConvertedCasesGourmetCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		
		if(TotalConvertedCasesCoolerStock == 0){
			ConvertedCasesPepsiCoolerStockPercent = 0;
			ConvertedCasesCokeCoolerStockPercent = 0;
			ConvertedCasesGourmetCoolerStockPercent = 0;
		}
		
		double PepsiShareCoolerStock = (ConvertedCasesPepsiCoolerStockPercent * this.CaptiveWeightage)/100;
		double CokeShareCoolerStock = (ConvertedCasesCokeCoolerStockPercent * this.CaptiveWeightage)/100;
		double GourmetShareCoolerStock = (ConvertedCasesGourmetCoolerStockPercent * this.CaptiveWeightage)/100;
		
		this.CaptivePepsiShare = PepsiShareCoolerStock;
		this.CaptiveCokeShare = CokeShareCoolerStock;
		this.CaptiveGourmetShare = GourmetShareCoolerStock;
		
		s3.close();
		s2.close();
		s.close();				
	}
	
public void CalculateCaptiveSSRBShare(long DistributorID, Date Date) throws SQLException{
		
		Statement s = this.ds.c.createStatement();
		Statement s2 = this.ds.c.createStatement();
		Statement s3 = this.ds.c.createStatement();
		
		double ConvertedCasesPepsiCoolerStock = 0;
		double ConvertedCasesCokeCoolerStock = 0;
		double ConvertedCasesGourmetCoolerStock = 0;		
		double TotalConvertedCasesCoolerStock = 0;
		
		int TotalNumOfOutlets = 0;
		ResultSet rs = s.executeQuery("SELECT count(id) FROM common_outlets where distributor_id="+DistributorID);
		if(rs.first()){
			TotalNumOfOutlets = rs.getInt(1);
		}
		
		int TotalNumOfCaptiveOutlets = 0;
		ResultSet rs2 = s.executeQuery("SELECT count(id) FROM common_outlets where distributor_id="+DistributorID+" and segment_id=5");
		if(rs2.first()){
			TotalNumOfCaptiveOutlets = rs2.getInt(1);
		}
		
		int CaptiveWeightage = TotalNumOfCaptiveOutlets * 3;
		if(CaptiveWeightage >= 30){
			CaptiveWeightage = 30;
		}
		this.CaptiveSSRBWeightage = CaptiveWeightage;
		
		ResultSet rs12 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=1 and stock_type_id=1 and co.segment_id=5 and mrsa.package_id in (12)  ");
		if(rs12.first()){
			ConvertedCasesPepsiCoolerStock = rs12.getDouble(1);
		}
		
		ResultSet rs13 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=2 and stock_type_id=1 and co.segment_id=5 and mrsa.package_id in (12)  ");
		if(rs13.first()){
			ConvertedCasesCokeCoolerStock = rs13.getDouble(1);
		}
		
		ResultSet rs14 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM mrd_research mr join mrd_research_company_chillers mrcc join mrd_research_stock_availability mrsa join inventory_packages ip join common_outlets co on mr.id=mrcc.id and mr.id=mrsa.id and mrsa.package_id = ip.id and mr.outlet_id=co.id where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mrsa.company_id=3 and stock_type_id=1 and co.segment_id=5 and mrsa.package_id in (12) ");
		if(rs14.first()){
			ConvertedCasesGourmetCoolerStock = rs14.getDouble(1);
		}
		
		TotalPepsi250mlSSRBStock = 0;
		TotalCoke250mlSSRBStock = 0;
		ResultSet rs3 = s.executeQuery("SELECT id, agreed_daily_average_sales FROM common_outlets where distributor_id="+DistributorID+" and segment_id=5 and agreed_daily_average_sales is not null");
		while(rs3.next()){
			int DailyAvgSale = rs3.getInt("agreed_daily_average_sales");
			
			ResultSet rs4 = s2.executeQuery("SELECT id, (select count(no_of_chillers) from mrd_research_company_chillers where id=mr.id and company_id=1) pepsi_chillers, (select count(no_of_chillers) from mrd_research_company_chillers where id=mr.id and company_id=2) coke_chillers FROM mrd_research mr where mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+" and mr.outlet_id="+rs3.getString(1));
			if(rs4.first()){// if outlet survey exists
				
				int PepsiChillers = rs4.getInt("pepsi_chillers");
				int CokeChillers = rs4.getInt("coke_chillers");
				if(PepsiChillers > 0 || CokeChillers > 0){
					AllocateAvgSale250mlSSRB(rs3.getInt(1), DailyAvgSale, PepsiChillers, CokeChillers);
				}
			}else{// otherwise
				ResultSet rs5 = s2.executeQuery("SELECT id, (select count(no_of_chillers) from mrd_research_company_chillers where id=mr.id and company_id=1) pepsi_chillers, (select count(no_of_chillers) from mrd_research_company_chillers where id=mr.id and company_id=2) coke_chillers FROM mrd_research mr where mr.outlet_id="+rs3.getString(1)+" order by created_on desc limit 1 ");
				if(rs5.first()){// pick last survey
					int PepsiChillers = rs5.getInt("pepsi_chillers");
					int CokeChillers = rs5.getInt("coke_chillers");
					if(PepsiChillers > 0 || CokeChillers > 0){
						AllocateAvgSale250mlSSRB(rs3.getInt(1), DailyAvgSale, PepsiChillers, CokeChillers);
					}
				}
			}
		}
		
		ConvertedCasesPepsiCoolerStock += TotalPepsi250mlSSRBStock;
		ConvertedCasesCokeCoolerStock += TotalCoke250mlSSRBStock;
		
		TotalConvertedCasesCoolerStock = ConvertedCasesPepsiCoolerStock + ConvertedCasesCokeCoolerStock + ConvertedCasesGourmetCoolerStock;
		
		double ConvertedCasesPepsiCoolerStockPercent = (ConvertedCasesPepsiCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesCokeCoolerStockPercent = (ConvertedCasesCokeCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesGourmetCoolerStockPercent = (ConvertedCasesGourmetCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		
		if(TotalConvertedCasesCoolerStock == 0){
			ConvertedCasesPepsiCoolerStockPercent = 0;
			ConvertedCasesCokeCoolerStockPercent = 0;
			ConvertedCasesGourmetCoolerStockPercent = 0;
		}
		
		double PepsiShareCoolerStock = (ConvertedCasesPepsiCoolerStockPercent * this.CaptiveSSRBWeightage)/100;
		double CokeShareCoolerStock = (ConvertedCasesCokeCoolerStockPercent * this.CaptiveSSRBWeightage)/100;
		double GourmetShareCoolerStock = (ConvertedCasesGourmetCoolerStockPercent * this.CaptiveSSRBWeightage)/100;
		
		this.CaptiveSSRBPepsiShare = PepsiShareCoolerStock;
		this.CaptiveSSRBCokeShare = CokeShareCoolerStock;
		this.CaptiveSSRBGourmetShare = GourmetShareCoolerStock;
		
		s3.close();
		s2.close();
		s.close();				
	}
	
	public void CalculateMainSideVillageShare(Statement s, long DistributorID, Date Date) throws SQLException{
		double ConvertedCasesPepsiCoolerStock = 0;
		double ConvertedCasesCokeCoolerStock = 0;
		double ConvertedCasesGourmetCoolerStock = 0;
		double ConvertedCasesPepsiFloorStock = 0;
		double ConvertedCasesCokeFloorStock = 0;
		double ConvertedCasesGourmetFloorStock = 0;
		double TotalConvertedCasesCoolerStock = 0;
		double TotalConvertedCasesFloorStock = 0;
		
		ResultSet rs12 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=1 and mrsa.stock_type_id=1 and survey.segment_id not in (1, 5)");
		if(rs12.first()){
			ConvertedCasesPepsiCoolerStock = rs12.getDouble(1);
		}
		
		ResultSet rs13 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=2 and mrsa.stock_type_id=1 and survey.segment_id not in (1, 5)");
		if(rs13.first()){
			ConvertedCasesCokeCoolerStock = rs13.getDouble(1);
		}
		
		ResultSet rs14 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=3 and mrsa.stock_type_id=1 and survey.segment_id not in (1, 5)");
		if(rs14.first()){
			ConvertedCasesGourmetCoolerStock = rs14.getDouble(1);
		}
		
		ResultSet rs15 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=1 and mrsa.stock_type_id=2 and survey.segment_id not in (1, 5)");
		if(rs15.first()){
			ConvertedCasesPepsiFloorStock = rs15.getDouble(1);
		}
		
		ResultSet rs16 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=2 and mrsa.stock_type_id=2 and survey.segment_id not in (1, 5)");
		if(rs16.first()){
			ConvertedCasesCokeFloorStock = rs16.getDouble(1);
		}
		
		ResultSet rs17 = s.executeQuery("SELECT sum(((if(package_id = 11 or package_id = 12,quantity*unit_per_case,quantity))*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=3 and mrsa.stock_type_id=2 and survey.segment_id not in (1, 5)");
		if(rs17.first()){
			ConvertedCasesGourmetFloorStock = rs17.getDouble(1);
		}
		
		TotalConvertedCasesCoolerStock = ConvertedCasesPepsiCoolerStock + ConvertedCasesCokeCoolerStock + ConvertedCasesGourmetCoolerStock;
		TotalConvertedCasesFloorStock = ConvertedCasesPepsiFloorStock + ConvertedCasesCokeFloorStock + ConvertedCasesGourmetFloorStock;
		
		double ConvertedCasesPepsiCoolerStockPercent = (ConvertedCasesPepsiCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesCokeCoolerStockPercent = (ConvertedCasesCokeCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesGourmetCoolerStockPercent = (ConvertedCasesGourmetCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		
		if(TotalConvertedCasesCoolerStock == 0){
			ConvertedCasesPepsiCoolerStockPercent = 0;
			ConvertedCasesCokeCoolerStockPercent = 0;
			ConvertedCasesGourmetCoolerStockPercent = 0;
		}
		
		double ConvertedCasesPepsiFloorStockPercent = (ConvertedCasesPepsiFloorStock / TotalConvertedCasesFloorStock) * 100;
		double ConvertedCasesCokeFloorStockPercent = (ConvertedCasesCokeFloorStock / TotalConvertedCasesFloorStock) * 100;
		double ConvertedCasesGourmetFloorStockPercent = (ConvertedCasesGourmetFloorStock / TotalConvertedCasesFloorStock) * 100;
		
		if(TotalConvertedCasesFloorStock == 0){
			ConvertedCasesPepsiFloorStockPercent = 0;
			ConvertedCasesCokeFloorStockPercent = 0;
			ConvertedCasesGourmetFloorStockPercent = 0;
		}
		
		int CoolerStockWeightage = 60;
		int FloorStockWeightage = 40;
		
		double PepsiShareCoolerStock = (ConvertedCasesPepsiCoolerStockPercent * CoolerStockWeightage)/100;
		double CokeShareCoolerStock = (ConvertedCasesCokeCoolerStockPercent * CoolerStockWeightage)/100;
		double GourmetShareCoolerStock = (ConvertedCasesGourmetCoolerStockPercent * CoolerStockWeightage)/100;
		
		double PepsiShareFloorStock = (ConvertedCasesPepsiFloorStockPercent * FloorStockWeightage)/100;
		double CokeShareFloorStock = (ConvertedCasesCokeFloorStockPercent * FloorStockWeightage)/100;
		double GourmetShareFloorStock = (ConvertedCasesGourmetFloorStockPercent * FloorStockWeightage)/100;
		
		double TotalPepsiStock = PepsiShareCoolerStock+PepsiShareFloorStock;
		double TotalCokeStock = CokeShareCoolerStock+CokeShareFloorStock;
		double TotalGourmetStock = GourmetShareCoolerStock+GourmetShareFloorStock;
		
		this.MainSideVillageWeightage = 100 - ( this.HighVolumeWeightage + this.CaptiveWeightage );
		
		this.MainSideVillagePepsiShare = (TotalPepsiStock * MainSideVillageWeightage)/100;
		this.MainSideVillageCokeShare = (TotalCokeStock * MainSideVillageWeightage)/100;
		this.MainSideVillageGourmetShare = (TotalGourmetStock * MainSideVillageWeightage)/100;
		
	}
	
	public void CalculateMainSideVillageSSRBShare(Statement s, long DistributorID, Date Date) throws SQLException{
		double ConvertedCasesPepsiCoolerStock = 0;
		double ConvertedCasesCokeCoolerStock = 0;
		double ConvertedCasesGourmetCoolerStock = 0;
		double ConvertedCasesPepsiFloorStock = 0;
		double ConvertedCasesCokeFloorStock = 0;
		double ConvertedCasesGourmetFloorStock = 0;
		double TotalConvertedCasesCoolerStock = 0;
		double TotalConvertedCasesFloorStock = 0;
		
		ResultSet rs12 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=1 and mrsa.stock_type_id=1 and survey.segment_id not in (1, 5) and mrsa.package_id in (11, 12) ");
		if(rs12.first()){
			ConvertedCasesPepsiCoolerStock = rs12.getDouble(1);
		}
		
		ResultSet rs13 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=2 and mrsa.stock_type_id=1 and survey.segment_id not in (1, 5) and mrsa.package_id in (11, 12)");
		if(rs13.first()){
			ConvertedCasesCokeCoolerStock = rs13.getDouble(1);
		}
		
		ResultSet rs14 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=3 and mrsa.stock_type_id=1 and survey.segment_id not in (1, 5) and mrsa.package_id in (11, 12)");
		if(rs14.first()){
			ConvertedCasesGourmetCoolerStock = rs14.getDouble(1);
		}
		
		ResultSet rs15 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=1 and mrsa.stock_type_id=2 and survey.segment_id not in (1, 5) and mrsa.package_id in (11, 12)");
		if(rs15.first()){
			ConvertedCasesPepsiFloorStock = rs15.getDouble(1);
		}
		
		ResultSet rs16 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=2 and mrsa.stock_type_id=2 and survey.segment_id not in (1, 5) and mrsa.package_id in (11, 12)");
		if(rs16.first()){
			ConvertedCasesCokeFloorStock = rs16.getDouble(1);
		}
		
		ResultSet rs17 = s.executeQuery("SELECT sum(((quantity*unit_per_case)*liquid_in_ml)/conversion_rate_in_ml) converted_cases FROM (SELECT mr.id, ifnull((select segment_id from common_outlets where id=mr.outlet_id), 0) segment_id FROM mrd_research mr join mrd_research_company_chillers mrcc on mr.id=mrcc.id  where mr.distributor_id="+DistributorID+" and mr.created_on between "+Utilities.getSQLDate(Date)+" and "+Utilities.getSQLDateNext(Date)+") survey join mrd_research_stock_availability mrsa join inventory_packages ip on survey.id=mrsa.id and mrsa.package_id = ip.id where mrsa.company_id=3 and mrsa.stock_type_id=2 and survey.segment_id not in (1, 5) and mrsa.package_id in (11, 12)");
		if(rs17.first()){
			ConvertedCasesGourmetFloorStock = rs17.getDouble(1);
		}
		
		TotalConvertedCasesCoolerStock = ConvertedCasesPepsiCoolerStock + ConvertedCasesCokeCoolerStock + ConvertedCasesGourmetCoolerStock;
		TotalConvertedCasesFloorStock = ConvertedCasesPepsiFloorStock + ConvertedCasesCokeFloorStock + ConvertedCasesGourmetFloorStock;
		
		double ConvertedCasesPepsiCoolerStockPercent = (ConvertedCasesPepsiCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesCokeCoolerStockPercent = (ConvertedCasesCokeCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		double ConvertedCasesGourmetCoolerStockPercent = (ConvertedCasesGourmetCoolerStock / TotalConvertedCasesCoolerStock) * 100;
		
		if(TotalConvertedCasesCoolerStock == 0){
			ConvertedCasesPepsiCoolerStockPercent = 0;
			ConvertedCasesCokeCoolerStockPercent = 0;
			ConvertedCasesGourmetCoolerStockPercent = 0;
		}
		
		double ConvertedCasesPepsiFloorStockPercent = (ConvertedCasesPepsiFloorStock / TotalConvertedCasesFloorStock) * 100;
		double ConvertedCasesCokeFloorStockPercent = (ConvertedCasesCokeFloorStock / TotalConvertedCasesFloorStock) * 100;
		double ConvertedCasesGourmetFloorStockPercent = (ConvertedCasesGourmetFloorStock / TotalConvertedCasesFloorStock) * 100;
		
		if(TotalConvertedCasesFloorStock == 0){
			ConvertedCasesPepsiFloorStockPercent = 0;
			ConvertedCasesCokeFloorStockPercent = 0;
			ConvertedCasesGourmetFloorStockPercent = 0;
		}
		
		int CoolerStockWeightage = 60;
		int FloorStockWeightage = 40;
		
		double PepsiShareCoolerStock = (ConvertedCasesPepsiCoolerStockPercent * CoolerStockWeightage)/100;
		double CokeShareCoolerStock = (ConvertedCasesCokeCoolerStockPercent * CoolerStockWeightage)/100;
		double GourmetShareCoolerStock = (ConvertedCasesGourmetCoolerStockPercent * CoolerStockWeightage)/100;
		
		double PepsiShareFloorStock = (ConvertedCasesPepsiFloorStockPercent * FloorStockWeightage)/100;
		double CokeShareFloorStock = (ConvertedCasesCokeFloorStockPercent * FloorStockWeightage)/100;
		double GourmetShareFloorStock = (ConvertedCasesGourmetFloorStockPercent * FloorStockWeightage)/100;
		
		double TotalPepsiStock = PepsiShareCoolerStock+PepsiShareFloorStock;
		double TotalCokeStock = CokeShareCoolerStock+CokeShareFloorStock;
		double TotalGourmetStock = GourmetShareCoolerStock+GourmetShareFloorStock;
		
		this.MainSideVillageSSRBWeightage = 100 - ( this.HighVolumeSSRBWeightage + this.CaptiveSSRBWeightage );
				
		this.MainSideVillageSSRBPepsiShare = (TotalPepsiStock * MainSideVillageSSRBWeightage)/100;
		this.MainSideVillageSSRBCokeShare = (TotalCokeStock * MainSideVillageSSRBWeightage)/100;
		this.MainSideVillageSSRBGourmetShare = (TotalGourmetStock * MainSideVillageSSRBWeightage)/100;
		
	}
	
	public void AllocateAvgSale250ml(int OutletID, int DailyAvgSale, int PepsiChillers, int CokeChillers) throws SQLException{
		
		Statement s = this.ds.c.createStatement();
		
		boolean isPepsi250mlAvailable = false;
		ResultSet rs5 = s.executeQuery("SELECT mr.id FROM mrd_research mr join mrd_research_stock_availability mrsa on mr.id=mrsa.id where mr.id="+OutletID+" and mrsa.company_id=1 and mrsa.package_id=11 and mrsa.stock_type_id=1 limit 1");
		if(rs5.first()){
			isPepsi250mlAvailable = true;
		}
		
		boolean isCoke250mlAvailable = false;
		ResultSet rs6 = s.executeQuery("SELECT mr.id FROM mrd_research mr join mrd_research_stock_availability mrsa on mr.id=mrsa.id where mr.id="+OutletID+" and mrsa.company_id=2 and mrsa.package_id=11 and mrsa.stock_type_id=1 limit 1");
		if(rs6.first()){
			isCoke250mlAvailable = true;
		}
		
		if(isPepsi250mlAvailable && isCoke250mlAvailable){	// both stock available
			int DailyAvgSale60Percent = (DailyAvgSale * 60) / 100;	// result would be an integer bcoz of int
			int DailyAvgSale40Percent = (DailyAvgSale * 40) / 100;
			
			TotalPepsi250mlStock += DailyAvgSale60Percent;
			TotalCoke250mlStock += DailyAvgSale40Percent;
		}else if(isPepsi250mlAvailable){
			TotalPepsi250mlStock += DailyAvgSale;
		}else if(isCoke250mlAvailable){
			TotalCoke250mlStock += DailyAvgSale;
		}else{	// both stock unavailable
			
			if(PepsiChillers > 0 && CokeChillers > 0){
				int DailyAvgSale60Percent = (DailyAvgSale * 60) / 100;	// result would be an integer bcoz of int
				int DailyAvgSale40Percent = (DailyAvgSale * 40) / 100;
				
				TotalPepsi250mlStock += DailyAvgSale60Percent;
				TotalCoke250mlStock += DailyAvgSale40Percent;
			}else if(PepsiChillers == 1){
				TotalPepsi250mlStock += DailyAvgSale;
			}else if(CokeChillers == 1){
				TotalCoke250mlStock += DailyAvgSale;
			}
		}
		
		s.close();
		
	}
	
	public void AllocateAvgSale250mlSSRB(int OutletID, int DailyAvgSale, int PepsiChillers, int CokeChillers) throws SQLException{
		
		Statement s = this.ds.c.createStatement();
		
		boolean isPepsi250mlAvailable = false;
		ResultSet rs5 = s.executeQuery("SELECT mr.id FROM mrd_research mr join mrd_research_stock_availability mrsa on mr.id=mrsa.id where mr.id="+OutletID+" and mrsa.company_id=1 and mrsa.package_id=11 and mrsa.stock_type_id=1 limit 1");
		if(rs5.first()){
			isPepsi250mlAvailable = true;
		}
		
		boolean isCoke250mlAvailable = false;
		ResultSet rs6 = s.executeQuery("SELECT mr.id FROM mrd_research mr join mrd_research_stock_availability mrsa on mr.id=mrsa.id where mr.id="+OutletID+" and mrsa.company_id=2 and mrsa.package_id=11 and mrsa.stock_type_id=1 limit 1");
		if(rs6.first()){
			isCoke250mlAvailable = true;
		}
		
		if(isPepsi250mlAvailable && isCoke250mlAvailable){	// both stock available
			int DailyAvgSale60Percent = (DailyAvgSale * 60) / 100;	// result would be an integer bcoz of int
			int DailyAvgSale40Percent = (DailyAvgSale * 40) / 100;
			
			TotalPepsi250mlSSRBStock += DailyAvgSale60Percent;
			TotalCoke250mlSSRBStock += DailyAvgSale40Percent;
		}else if(isPepsi250mlAvailable){
			TotalPepsi250mlSSRBStock += DailyAvgSale;
		}else if(isCoke250mlAvailable){
			TotalCoke250mlSSRBStock += DailyAvgSale;
		}else{	// both stock unavailable
			
			if(PepsiChillers > 0 && CokeChillers > 0){
				int DailyAvgSale60Percent = (DailyAvgSale * 60) / 100;	// result would be an integer bcoz of int
				int DailyAvgSale40Percent = (DailyAvgSale * 40) / 100;
				
				TotalPepsi250mlSSRBStock += DailyAvgSale60Percent;
				TotalCoke250mlSSRBStock += DailyAvgSale40Percent;
			}else if(PepsiChillers == 1){
				TotalPepsi250mlSSRBStock += DailyAvgSale;
			}else if(CokeChillers == 1){
				TotalCoke250mlSSRBStock += DailyAvgSale;
			}
		}
		
		s.close();
		
	}
	
}