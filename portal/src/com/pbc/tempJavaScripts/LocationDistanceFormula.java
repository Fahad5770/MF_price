package com.pbc.tempJavaScripts;

import com.pbc.reports.HaversineDistanceCalculator;

public class LocationDistanceFormula {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		double distance = HaversineDistanceCalculator.calculateHaversineDistance(31.531360000000000, 74.353480000000000, 29.3948516666666680000, 71.6758033333333300000);
	System.out.println("distance : "+distance);
	}

}
