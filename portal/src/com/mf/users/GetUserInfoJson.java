package com.mf.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mf.modals.BeatPlans;
import com.mf.modals.User;
import com.mf.utils.MFConfig;
import com.pbc.util.Datasource;

public class GetUserInfoJson {

	public static User user_data(Datasource ds, int user_id) {
		User user = null;
		try {

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();

			System.out.println(
					"select  IS_ACTIVE, DISPLAY_NAME, ID,DESIGNATION , DEPARTMENT from users where ID=" + user_id);
			ResultSet rsUser = s.executeQuery(
					"select  password, IS_ACTIVE, DISPLAY_NAME, ID,DESIGNATION , DEPARTMENT from users where ID="
							+ user_id);
			if (rsUser.first()) {
				user = new User();
				user.setId(user_id);
				user.setName(rsUser.getString("DISPLAY_NAME"));
				user.setDesignation(rsUser.getString("DESIGNATION"));
				user.setDepartment(rsUser.getString("DEPARTMENT"));
				user.setPassword(rsUser.getString("DEPARTMENT"));
				user.setIs_active(rsUser.getInt("IS_ACTIVE"));
				user.setPassword(rsUser.getString("password"));

				System.out.println(
						"select distinct city from common_distributors where distributor_id in(select distributor_id from distributor_beat_plan_view v where assigned_to="
								+ user_id + ") limit 1");
				ResultSet rsCity = s2.executeQuery(
						"select distinct city from common_distributors where distributor_id in(select distributor_id from distributor_beat_plan_view v where assigned_to="
								+ user_id + ") limit 1");
				user.setCity(((rsCity.first()) ? rsCity.getString("city") : "No City"));
			}
			s2.close();
			s.close();

		} catch (SQLException e) {
			try {
				ds.dropConnection();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				System.out.println("User Info Error : " + e1);
				e1.printStackTrace();
			}
			System.out.println("User Info Error : " + e);

		}
		return user;
	}

	public static boolean is_user_password(Datasource ds, int user_id, String password) {
		boolean user_password = false;
		try {

			Statement s = ds.createStatement();

			System.out.println(
					"select md5('" + password + "') as encrypt_password, password from users where ID=" + user_id);
			ResultSet rsUserPassword = s.executeQuery(
					"select md5('" + password + "') as encrypt_password, password from users where ID=" + user_id);
			if (rsUserPassword.first()) {
				user_password = (rsUserPassword.getString("encrypt_password").equals(
						rsUserPassword.getString("password")));
			}

			s.close();

		} catch (SQLException e) {
			System.out.println("User Info Error : " + e);

		}
		
		///System.out.println(user_password);
		return user_password;

	}

	public static List<BeatPlans> get_user_beat_plans(Datasource ds, int user_id) {
		List<BeatPlans> beat_plan = new ArrayList<BeatPlans>();
		try {

			Statement s = ds.createStatement();

			/*
			 * System.out.
			 * println("select distinct id, label PJPName, distributor_id From distributor_beat_plan_view where assigned_to="
			 * + user_id + " or asm_id=" + user_id + " or rsm_id=" + user_id + " or snd_id="
			 * + user_id);
			 */
			System.out.println(
					"select distinct id, label PJPName, distributor_id, city_id,region_id, snd_id ,rsm_id, asm_id From distributor_beat_plan_view where assigned_to="
							+ user_id + " or asm_id=" + user_id + " or rsm_id=" + user_id + " or snd_id=" + user_id);
			ResultSet rsPJP = s.executeQuery(
					"select distinct id, label PJPName, distributor_id, city_id,region_id, snd_id ,rsm_id, asm_id From distributor_beat_plan_view where assigned_to="
							+ user_id + " or asm_id=" + user_id + " or rsm_id=" + user_id + " or snd_id=" + user_id);
			while (rsPJP.next()) {
				BeatPlans BP = new BeatPlans(rsPJP.getInt("id"), rsPJP.getString("PJPName"),
						rsPJP.getLong("distributor_id"), rsPJP.getInt("city_id"), rsPJP.getInt("region_id"),
						rsPJP.getInt("snd_id"), rsPJP.getInt("rsm_id"), rsPJP.getInt("asm_id"));
				beat_plan.add(BP);
			}
			s.close();

		} catch (SQLException e) {
			System.out.println("User Info Error : " + e);

		}
		return beat_plan;

	}

}
