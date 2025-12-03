package com.pbc.workflow;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Date;

import com.pbc.util.Datasource;

public class WorkflowManager {

	public boolean isNewRequest = true;
	public boolean isLastStep = false;
	public boolean isDeclined = false;
	public boolean isCompleted = false;
	
	public long REQUEST_ID;
	public int REQUEST_STATUS_ID;
	public String REQUEST_STATUS_LABEL;
	public int PROCESS_ID;
	public String PROCESS_NAME;
	
	public ArrayList<WorkflowStep> STEPS = new ArrayList<WorkflowStep>();
	
	public WorkflowStep CURRENT_STEP;
	
	public WorkflowStep NEXT_STEP;
	
	Connection c;
	Datasource ds;	
	
	public WorkflowManager() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public void setRequestID(long RequestID) throws SQLException{
		this.REQUEST_ID = RequestID;
		isNewRequest = false;
		setApprovalChain();
	}
	
	public void setIsNewRequest(boolean isNewRequest) throws SQLException{
		this.isNewRequest = true;
		this.CURRENT_STEP = new WorkflowStep();
		this.CURRENT_STEP.STEP_ID = 1;
		this.CURRENT_STEP.ACTION_BUTTON_LABEL = "Submit & Forward";
		this.CURRENT_STEP.ACTION_ID = 1;
		this.CURRENT_STEP.SEQUENCE_ID = 1;
		this.REQUEST_ID = 0;
	}
	
	public void setProcessID(int ProcessID) throws SQLException{
		this.PROCESS_ID = ProcessID;
	}
	
	public void isLoaded() throws SQLException{
		setNextStep();
	}
	
	public void setNextStep() throws SQLException{
		
		Statement s = c.createStatement();
		
		this.NEXT_STEP = new WorkflowStep();
		
//		if (this.CURRENT_STEP.STEP_ID == 5 && isSamplingAdvanceType() == false){
//			
//			
//			this.isLastStep = true;
//		}else{
			System.out.println("SELECT wps.step_id, wps.action_id, wa.action_label, wps.user_id, u.display_name user_display_name, u.designation, u.department, u.email FROM workflow_processes_steps wps, workflow_actions wa, users u where wps.action_id = wa.action_id and wps.user_id = u.id and wps.process_id = "+this.PROCESS_ID+" and wps.step_id = ("+this.CURRENT_STEP.STEP_ID+"+1)");
			ResultSet rs = s.executeQuery("SELECT wps.step_id, wps.action_id, wa.action_label, wps.user_id, u.display_name user_display_name, u.designation, u.department, u.email FROM workflow_processes_steps wps, workflow_actions wa, users u where wps.action_id = wa.action_id and wps.user_id = u.id and wps.process_id = "+this.PROCESS_ID+" and wps.step_id = ("+this.CURRENT_STEP.STEP_ID+"+1)");
			if (rs.first()){
				this.NEXT_STEP.STEP_ID = rs.getInt(1);
				this.NEXT_STEP.ACTION_ID = rs.getInt(2);
				this.NEXT_STEP.ACTION_LABEL = rs.getString(3);
				this.NEXT_STEP.USER_ID = rs.getLong(4);
				this.NEXT_STEP.USER_DISPLAY_NAME = rs.getString(5);
				this.NEXT_STEP.USER_DESIGNATION = rs.getString(6);
				this.NEXT_STEP.USER_DEPARTMENT = rs.getString(7);
				this.NEXT_STEP.USER_EMAIL = rs.getString(8);
			}else{
				this.isLastStep = true;
			}
			rs.close();
			
		//}
		
		s.close();
		
	}
	
	public void setApprovalChain() throws SQLException{
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery("select request_status_id, request_status_label, process_id, process_name, step_sequence_id, step_id, step_action_id, step_action_label, step_action_label_past, step_remarks, step_created_on, step_completed_on, step_by_userid, step_by_name, step_by_designation, step_by_department, step_by_email, step_action_button_label from workflow_requests_steps_view where request_id = "+this.REQUEST_ID);
		while(rs.next()){
			
			if (rs.isFirst()){
				this.REQUEST_STATUS_ID = rs.getInt(1);
				if (this.REQUEST_STATUS_ID == 3){
					this.isDeclined = true;
				}
				if (this.REQUEST_STATUS_ID == 2){
					this.isCompleted = true;
				}
				
				this.REQUEST_STATUS_LABEL = rs.getString(2);
				this.PROCESS_ID = rs.getInt(3);
				this.PROCESS_NAME = rs.getString(4);
			}
			
			WorkflowStep ws = new WorkflowStep();
			
			ws.SEQUENCE_ID = rs.getLong(5);
			ws.STEP_ID = rs.getInt(6);
			ws.ACTION_ID = rs.getInt(7);
			ws.ACTION_LABEL = rs.getString(8);
			ws.ACTION_LABEL_PAST = rs.getString(9);
			ws.REMARKS = rs.getString(10);
			ws.CREATED_ON = rs.getDate(11);
			ws.COMPLETED_ON = rs.getTimestamp(12);
			ws.USER_ID = rs.getLong(13);
			ws.USER_DISPLAY_NAME = rs.getString(14);
			ws.USER_DESIGNATION = rs.getString(15);
			ws.USER_DEPARTMENT = rs.getString(16);
			ws.USER_EMAIL = rs.getString(17);
			ws.ACTION_BUTTON_LABEL = rs.getString(18);
			
			if (rs.isLast()){
				CURRENT_STEP = ws;
			}
			

			
			this.STEPS.add(ws);
			
			
		}
		rs.close();
		s.close();
	}
	
	public boolean isSamplingAdvanceType() throws SQLException{
		
		boolean val = false;
		
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery("select advance_company_share from sampling where request_id = "+this.REQUEST_ID);
		if (rs.first()){
			double amount = rs.getDouble(1);
			if (amount != 0){
				val = true;
			}
		}

		s.close();
		
		return val;
	}
	
	public String getSamplingRegion() throws SQLException{
		
		String region = null;
		
		Statement s = c.createStatement();
		
		ResultSet rs = s.executeQuery("select region from sampling where request_id = "+this.REQUEST_ID);
		if (rs.first()){
			region = rs.getString(1);
		}

		s.close();
		
		return region;
	}
	
	public void close() throws SQLException{
		ds.dropConnection();
	}
	
}
