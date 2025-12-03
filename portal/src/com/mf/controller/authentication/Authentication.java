package com.mf.controller.authentication;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mf.dao.LoginRequest;
import com.mf.dao.LoginResponseMV;
import com.mf.dao.OBLoginResponse;
import com.mf.interfaces.IAuthentication;
import com.mf.modals.ResponseModal;
import com.mf.modals.User;
import com.mf.users.GetUserInfoJson;
import com.mf.users.UserInfoExecute;
import com.mf.utils.MFConfig;
import com.mf.utils.MFJWTToken;
import com.mf.utils.MFSQLUtils;
import com.pbc.util.Datasource;
import java.sql.SQLException;
import java.sql.Statement;

public class Authentication implements IAuthentication {

	public Authentication() {
		super();
		// Constructor logic (if needed)
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseModal market_visit_login(JSONObject Payload, HttpServletRequest request) {

		ResponseModal responseModal = new ResponseModal();

		if (Payload.get("app_version") == null) {
			responseModal.setErrorResponse("Please Update you application !!!");
			return responseModal;
		}

		LoginRequest loginRequest = new LoginRequest(Payload);

		Datasource ds = new Datasource();

		try {

			ds.createConnection();

			Statement s = ds.createStatement();
			Statement s2 = ds.createStatement();
			Statement s3 = ds.createStatement();
			Statement s4 = ds.createStatement();

			// check app version
			if (!AuthenticationUtils.verify_MV_app_version(ds, loginRequest.getApp_version())) {
				responseModal.setErrorResponse("Please Update you application !!!");
				return responseModal;
			}

			User user = GetUserInfoJson.user_data(ds, loginRequest.getUser_id());

			if (user == null) {
				UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 5, loginRequest.getDevice_id(), request);
				responseModal.setErrorResponse("In Valid User");
				System.out.println("checking user is not active");
			}

			// check user is active
			if (user.getIs_active() == 0) {
				UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 5, loginRequest.getDevice_id(), request);
				responseModal.setErrorResponse("In Actice User");
				System.out.println("checking user is not active");

				return responseModal;
			}
			// check user password
			if (!GetUserInfoJson.is_user_password(ds, loginRequest.getUser_id(), loginRequest.getPassword())) {
				
				if (!loginRequest.getPassword().equals(MFConfig.getMobileMVPassword())) {
					UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 7, loginRequest.getDevice_id(), request);
					responseModal.setErrorResponse("Invalid Password");
					System.out.println("checking user password");

					return responseModal;
				}
//				UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 7, loginRequest.getDevice_id(), request);
//				responseModal.setErrorResponse("Invalid Password");
//				System.out.println("checking user password");
//
//				return responseModal;
			}
			

			LoginResponseMV loginResponseMV = new LoginResponseMV();

			AuthenticationDetails AD = new AuthenticationDetails();

			int productGroupId = AD.ProductGroup(ds, loginRequest.getUser_id());

			// Generate Token
			String token = MFJWTToken.generateToken(String.valueOf(loginRequest.getUser_id()));
			if (token == null) {
				UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 9, loginRequest.getDevice_id(), request);
				responseModal.setErrorResponse("Generating Token Error");
				System.out.println("Generating Token");

				return responseModal;
			}

			loginResponseMV.setToken(token);
			/* create array of user */
			JSONArray UserData = new JSONArray();
			UserData.add(user.getIntoJson());
			loginResponseMV.setUserData(UserData);
			loginResponseMV.setBeatPlanRows((loginRequest.getApp_version().equals("1.0.3") || loginRequest.getApp_version().equals("1.0.4"))
					? AD.BeatPlanRows(ds, loginRequest.getUser_id(), loginRequest.getLat(), loginRequest.getLng())
					: AD.BeatPlanRows(ds, loginRequest.getUser_id()));
			loginResponseMV.setProducts(AD.Products(ds, productGroupId));
			loginResponseMV.setCities(AD.Cities(ds, loginRequest.getUser_id()));
			loginResponseMV.setBeatPlans(AD.get_pjp_list(ds, loginRequest.getUser_id()));

			/*
			 * loginResponseMV.setDistributions( AD.Distributions(ds,
			 * loginRequest.getUser_id(), loginRequest.getLat(), loginRequest.getLng()));
			 */

			UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 4, loginRequest.getDevice_id(), request);
			responseModal.setData(loginResponseMV.getIntoJson());
			responseModal.setStatus(true);
			responseModal.setUserMessage("Login Successfully");

			s.close();
			s2.close();
			s3.close();
			s4.close();
			ds.dropConnection();

		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Authentication Error");
			responseModal.setErrorResponse("server Error : " + e);
		}

		return responseModal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseModal order_booker_login(JSONObject Payload, HttpServletRequest request) {
		// TODO Auto-generated method stub
		ResponseModal responseModal = new ResponseModal();
		LoginRequest loginRequest = new LoginRequest();

		loginRequest.LoginRequestForOB(Payload);

		Datasource ds = new Datasource();

		try {
			ds.createConnection();
			// check app version
			if (!AuthenticationUtils.verify_OB_app_version(ds, loginRequest.getApp_version())) {
				responseModal.setErrorResponse("Please Update you application !!!....");
				System.out.println("checking app version");

				return responseModal;
			}

			User user = GetUserInfoJson.user_data(ds, loginRequest.getUser_id());

			if (user == null) {
				UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 5, loginRequest.getDevice_id(), request);
				responseModal.setErrorResponse("In Valid User");
				System.out.println("checking user is not active");
				return responseModal;
			}

			// check user is active
			if (user.getIs_active() == 0) {
				UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 5, loginRequest.getDevice_id(), request);
				responseModal.setErrorResponse("In Actice User");
				System.out.println("checking user is not active");

				return responseModal;
			}
			// check user password
			System.out.println(!GetUserInfoJson.is_user_password(ds, loginRequest.getUser_id(), loginRequest.getPassword()));
			System.out.println(!loginRequest.getPassword().equals(MFConfig.getMobile_OB_AdminPassword()));
			System.out.println(loginRequest.getPassword());
			System.out.println(MFConfig.getMobile_OB_AdminPassword());
			if (!GetUserInfoJson.is_user_password(ds, loginRequest.getUser_id(), loginRequest.getPassword())) {
				
				if (!loginRequest.getPassword().equals(MFConfig.getMobile_OB_AdminPassword())) {
					UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 7, loginRequest.getDevice_id(), request);
					responseModal.setErrorResponse("Invalid Password");
					System.out.println("checking user password");

					return responseModal;
				}
//				UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 7, loginRequest.getDevice_id(), request);
//				responseModal.setErrorResponse("Invalid Password");
//				System.out.println("checking user password");
//
//				return responseModal;
			}

			// Generate Token
			String token = MFJWTToken.generateToken(String.valueOf(loginRequest.getUser_id()));
			if (token == null) {
				UserInfoExecute.user_logs(ds, loginRequest.getUser_id(), 9, loginRequest.getDevice_id(), request);
				responseModal.setErrorResponse("Generating Token Error");
				System.out.println("Generating Token");

				return responseModal;
			}

			OBLoginResponse OBloginResponse = new OBLoginResponse();

			AuthenticationDetails AD = new AuthenticationDetails();

			int productGroupId = AD.ProductGroup(ds, loginRequest.getUser_id());
			String AllOutlets = AD.all_outlet_ids_ob(ds, loginRequest.getUser_id());

			AllOutlets = (AllOutlets.equals("") ? "0" : AllOutlets);

			OBloginResponse.setToken(token);
			/* create array of user */
			JSONArray UserData = new JSONArray();
			UserData.add(user.getIntoJson());
			OBloginResponse.setUserData(UserData);

			OBloginResponse.setBeatPlanRows(AD.OrderBookerBeatPlanRows(ds, loginRequest.getUser_id()));
			
			OBloginResponse.setProductgroupId(productGroupId);
			OBloginResponse.setProducts(AD.Products(ds, productGroupId));
			OBloginResponse.setPriceList(AD.get_ob_price_list(ds));
			OBloginResponse.setPcisubChannnel(AD.get_pci_sub_channels(ds));
			OBloginResponse.setPromotionsProductsFree(AD.get_free_promotion_products(ds, AllOutlets));
			OBloginResponse.setActivePriceList(AD.get_active_price_list(ds, AllOutlets));
			OBloginResponse.setNoOrderReason(AD.NoOrderReason(ds));
			OBloginResponse.setSpotDiscount(AD.SpotDiscount(ds));
			OBloginResponse.setPriceHandDiscount(AD.get_price_hand_discount(ds, AllOutlets));
			OBloginResponse.setAccessFeatures(AD.AllMobileFeatures(ds));
			OBloginResponse.setAllFeatures(AD.AllMobileFeatures(ds));
			OBloginResponse.setActivePromotion(AD.get_active_promotions(ds, AllOutlets));
			OBloginResponse.setPromoProducts(AD.get_promotion_products(ds, AllOutlets));
			OBloginResponse.setPjpList(AD.get_pjp_list(ds, loginRequest.getUser_id()));
			OBloginResponse.setStockPosition(AD.StockPosition(ds, loginRequest.getUser_id()));
			responseModal.setData(OBloginResponse.getIntoJson());
			responseModal.setStatus(true);
			responseModal.setUserMessage("Login Successfully");

			ds.dropConnection();
		} catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			System.out.println("Authentication Error");
			responseModal.setErrorResponse("server Error : " + e);
		}

		return responseModal;
	}

}
