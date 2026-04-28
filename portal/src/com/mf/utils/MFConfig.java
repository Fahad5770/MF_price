package com.mf.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class MFConfig {

	private static final long MobileSession = 24 * 60 * 60 * 1000; // Token expires in 1 hours 24 * 60 * 60 * 1000 Token

	private static final String ConstantSecretKey = "kaleLabs@1122"; // Token expires in 1 hours

	private static final String MobileAdminPassword = "moizfoods123";

	private static final String Mobile_OB_AdminPassword = "moizfoods123";

	private static final int Fina_User = 501;

	private static final List<Integer> AdminIds = Arrays.asList(274755, 274766);
	

	public static long getMobileSessionTime() {
		return MobileSession;
	}

	public static String LowaSecretKey() {
		return ConstantSecretKey;
	}

	public static String getMobileMVPassword() {
		return MobileAdminPassword;
	}

	public static String getMobile_OB_AdminPassword() {
		return Mobile_OB_AdminPassword;
	}

	public static int getMobile_NO_FinalUser() {
		return Fina_User;
	}

	public static List<Integer> getAdminIds() {
		return AdminIds;
	}

	public enum Folders {
		Attendance("AttendanceImages"), Order("OrderImages"), NoOrder("NoOrderImages"), OutletImages("OutletImages"),
		PJPFiles("PJPFiles"), NewOutletImages("NewOutletImages"), MerchendisingImages("MerchendisingImages"), UserFiles("UserFiles");

		private final String FolderName;

		// Constructor
		Folders(String FolderName) {
			this.FolderName = FolderName;
		}

		// Getter method
		public String getFolderName() {
			return FolderName;
		}

	}

	public static LinkedHashMap<String, Integer> getFeatureGroupIds() {
		LinkedHashMap<String, Integer> FeatureGroupIds = new LinkedHashMap<>();
		FeatureGroupIds.put("mobile", 7);
		return FeatureGroupIds;
	}
	

}
