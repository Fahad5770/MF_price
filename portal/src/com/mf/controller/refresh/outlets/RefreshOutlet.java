package com.mf.controller.refresh.outlets;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.mf.controller.authentication.AuthenticationDetails;
import com.mf.dao.RefreshOutletRequest;
import com.mf.dao.RefreshOutletResponse;
import com.mf.interfaces.IRefreshOutlets;
import com.mf.modals.ResponseModal;
import com.pbc.util.Datasource;

public class RefreshOutlet implements  IRefreshOutlets {
	


	@Override
	public ResponseModal refresh_outlets(JSONObject payload, HttpServletRequest request) {
		// TODO Auto-generated method stub
        ResponseModal ResponseModal = new ResponseModal();
		
		RefreshOutletRequest refreshOutletRequest = new RefreshOutletRequest(payload);
		
		RefreshOutletResponse refreshOutletResponse = new RefreshOutletResponse();
		
		AuthenticationDetails AD = new AuthenticationDetails();
		
		Datasource ds = new Datasource();
		
		System.out.println("Before Calling ");
		
		try {
			
		refreshOutletResponse.setBeatPlanRows(AD.BeatPlanRows(ds, refreshOutletRequest.getUser_id(), refreshOutletRequest.getLat(), refreshOutletRequest.getLng()));
		
		//OBloginResponse.setBeatPlanRows(AD.OrderBookerBeatPlanRows(ds, loginRequest.getUser_id()));

		
		ResponseModal.setData(refreshOutletResponse.getIntoJson());
		
		ResponseModal.setStatus(true);
		
		ResponseModal.setUserMessage("Outlet Loads Successfully");
		
		}catch(Exception e){
			System.out.println(e);
			ResponseModal.setErrorResponse("Outlets are not Refreshed");
		}
		
		return ResponseModal;
	}

	@Override
	public ResponseModal refresh_outlets_OB(JSONObject jsonData, HttpServletRequest request) {
		// TODO Auto-generated method stub
        ResponseModal ResponseModal = new ResponseModal();
		
		RefreshOutletRequest refreshOutletRequest = new RefreshOutletRequest();
		
		refreshOutletRequest.RefreshOutletRequestForOB(jsonData);
		
		System.out.println(refreshOutletRequest.toString());
		
		RefreshOutletResponse refreshOutletResponse = new RefreshOutletResponse();
		
		AuthenticationDetails AD = new AuthenticationDetails();
		
		Datasource ds = new Datasource();
				
		try {
			
		refreshOutletResponse.setBeatPlanRows(AD.OrderBookerBeatPlanRows(ds, refreshOutletRequest.getUser_id()));
		
		ResponseModal.setData(refreshOutletResponse.getIntoJson());
		System.out.println(refreshOutletResponse.toString());
		
		
		ResponseModal.setStatus(true);
		
		ResponseModal.setUserMessage("Outlet Loads Successfully");
		
		}catch(Exception e){
			System.out.println(e);
			ResponseModal.setErrorResponse("Outlets are not Refreshed");
		}
		
		return ResponseModal;
	}

}
