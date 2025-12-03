package com.pbc.bi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.pbc.util.Datasource;

public class DistributorBeatPlanLog {
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException { 
		
		
		BiProcesses bip = new BiProcesses();
		bip.createDistributorBeatPlanLog();
		bip.close();
		
		System.out.println("done");
		
	}
}
