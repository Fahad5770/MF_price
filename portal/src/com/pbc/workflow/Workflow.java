package com.pbc.workflow;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;



import com.pbc.util.Datasource;
import com.pbc.util.Utilities;

public class Workflow {
	
	Connection c;
	Datasource ds;
	
	public Workflow() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException{
		ds = new Datasource();
		ds.createConnection();
		c = ds.getConnection();
	}
	
	public long createRequestForOutlerRegistration(int ProcessID, long RequestUserID, long ForwardeeUserID, int ForwardeeActionID, int StepID , String remarks) throws SQLException{
		Statement s = c.createStatement();
		
		long RequestID = 0;
		//System.out.println("insert into workflow_requests (status_id, process_id, created_by, created_on, current_step_id, current_action_id, current_userid, current_step_on) values(1, "+ProcessID+", "+RequestUserID+", now(), 2, "+ForwardeeActionID+", "+ForwardeeUserID+", now())");
		s.executeUpdate("insert into workflow_requests (status_id, process_id, created_by, created_on, current_step_id, current_action_id, current_userid, current_step_on) values(1, "+ProcessID+", "+RequestUserID+", now(), "+StepID+", "+ForwardeeActionID+", "+ForwardeeUserID+", now())");
		
		ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
		if (rs.first()){
			RequestID = rs.getLong(1);
		}		
		//System.out.println("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, completed_on, user_id) VALUES ("+RequestID+", 1, 1, '"+remarks+"', now(), now(), "+RequestUserID+")");
		s.executeUpdate("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, completed_on, user_id) VALUES ("+RequestID+", 1, 1, '"+remarks+"', now(), now(), "+RequestUserID+")");
		System.out.println("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, user_id) VALUES ("+RequestID+", "+StepID+", "+ForwardeeActionID+", '', now(), "+ForwardeeUserID+")");
		s.executeUpdate("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, user_id) VALUES ("+RequestID+", "+StepID+", "+ForwardeeActionID+", '', now(), "+ForwardeeUserID+")");
		
		s.close();
		
		return RequestID;
	}
	
	
	public long createRequest(int ProcessID, long RequestUserID, long ForwardeeUserID, int ForwardeeActionID, String remarks) throws SQLException{
		Statement s = c.createStatement();
		
		long RequestID = 0;
		//System.out.println("insert into workflow_requests (status_id, process_id, created_by, created_on, current_step_id, current_action_id, current_userid, current_step_on) values(1, "+ProcessID+", "+RequestUserID+", now(), 2, "+ForwardeeActionID+", "+ForwardeeUserID+", now())");
		s.executeUpdate("insert into workflow_requests (status_id, process_id, created_by, created_on, current_step_id, current_action_id, current_userid, current_step_on) values(1, "+ProcessID+", "+RequestUserID+", now(), 2, "+ForwardeeActionID+", "+ForwardeeUserID+", now())");
		
		ResultSet rs = s.executeQuery("select LAST_INSERT_ID()");
		if (rs.first()){
			RequestID = rs.getLong(1);
		}		
		//System.out.println("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, completed_on, user_id) VALUES ("+RequestID+", 1, 1, '"+remarks+"', now(), now(), "+RequestUserID+")");
		s.executeUpdate("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, completed_on, user_id) VALUES ("+RequestID+", 1, 1, '"+remarks+"', now(), now(), "+RequestUserID+")");
		//System.out.println("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, user_id) VALUES ("+RequestID+", 2, "+ForwardeeActionID+", '', now(), "+ForwardeeUserID+")");
		s.executeUpdate("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, user_id) VALUES ("+RequestID+", 2, "+ForwardeeActionID+", '', now(), "+ForwardeeUserID+")");
		
		s.close();
		
		return RequestID;
	}

	public void doStepActionForOutlet(long RequestID, int StepID, boolean isLastStep, long ForwardeeUserID, int ForwardeeActionID, String remarks, int nextStep) throws SQLException{
		
		Statement s = c.createStatement();
		
		
		s.executeUpdate("update workflow_requests_steps set completed_on = now(), remarks = '"+remarks+"' where request_id = "+RequestID+" and step_id = "+StepID);
		
		if (isLastStep == false){
			
			System.out.println("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, user_id) VALUES ("+RequestID+", "+(nextStep)+", "+ForwardeeActionID+", '', now(), "+ForwardeeUserID+")");
			s.executeUpdate("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, user_id) VALUES ("+RequestID+", "+(nextStep)+", "+ForwardeeActionID+", '', now(), "+ForwardeeUserID+")");
			
			s.executeUpdate("update workflow_requests set current_step_id = "+(nextStep)+", current_action_id = "+ForwardeeActionID+", current_userid = "+ForwardeeUserID+", current_step_on = now() where request_id = "+RequestID);
			
		}else{
			
			s.executeUpdate("update workflow_requests set current_step_on = now(), status_id = 2 where request_id = "+RequestID);

		}
		
		s.close();
		
	}
	
	public void doStepAction(long RequestID, int StepID, boolean isLastStep, long ForwardeeUserID, int ForwardeeActionID, String remarks) throws SQLException{
		
		Statement s = c.createStatement();
		
		
		s.executeUpdate("update workflow_requests_steps set completed_on = now(), remarks = '"+remarks+"' where request_id = "+RequestID+" and step_id = "+StepID);
		
		if (isLastStep == false){
			
			s.executeUpdate("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, user_id) VALUES ("+RequestID+", "+(StepID+1)+", "+ForwardeeActionID+", '', now(), "+ForwardeeUserID+")");
			
			s.executeUpdate("update workflow_requests set current_step_id = "+(StepID+1)+", current_action_id = "+ForwardeeActionID+", current_userid = "+ForwardeeUserID+", current_step_on = now() where request_id = "+RequestID);
			
		}else{
			
			s.executeUpdate("update workflow_requests set current_step_on = now(), status_id = 2 where request_id = "+RequestID);

		}
		
		s.close();
		
	}

	
	public void doDecline(long RequestID, int StepID, long DeclinedBy, String remarks) throws SQLException{
		
		Statement s = c.createStatement();
		
		s.executeUpdate("INSERT INTO workflow_requests_steps (request_id, step_id, action_id, remarks, created_on, user_id, completed_on) VALUES ("+RequestID+", "+(StepID)+", 7, '"+remarks+"', now(), "+DeclinedBy+", now())");
		
		s.executeUpdate("update workflow_requests set current_step_id = "+(StepID)+", status_id = 3, current_action_id = 7, current_userid = "+DeclinedBy+", current_step_on = now() where request_id = "+RequestID);
		
	//	s.executeUpdate("update workflow_requests set status_id = 3 where request_id = "+RequestID);

		
		s.close();
		
	}
	
	
	public WorkflowDocument[] getActionAwaited(long UserID) throws SQLException{
		
		Statement s = c.createStatement();
		
		List <WorkflowDocument>list = new ArrayList<WorkflowDocument>();
		int num = 0;
		
		ResultSet rs = s.executeQuery("select * from workflow_requests_action_awaited_view where action_userid = "+ UserID + " order by current_step_on desc");
		while(rs.next()){
			WorkflowDocument item = new WorkflowDocument();
			item.REQUEST_ID = rs.getLong("request_id");
			item.CURRENT_STEP.STEP_ID = rs.getInt("current_step_id");
			item.PROCESS_ID = rs.getInt("process_id");
			item.PROCESS_NAME = rs.getString("process_name");
			item.CREATED_ON = rs.getTimestamp("created_on");
			item.CREATED_BY = rs.getLong("created_by");
			
			item.CURRENT_STEP.ACTION_ID = rs.getInt("current_action_id");
			item.CURRENT_STEP.ACTION_LABEL = rs.getString("current_action_label");
			item.CURRENT_STEP.CREATED_ON = rs.getTimestamp("current_step_on");
			item.CURRENT_STEP.USER_ID = UserID;
			
			item.LAST_STEP.USER_ID = rs.getLong("last_user_id");
			item.LAST_STEP.USER_DISPLAY_NAME = rs.getString("last_user_name");
			item.LAST_STEP.USER_DEPARTMENT = rs.getString("last_user_department");
			item.LAST_STEP.USER_DESIGNATION = rs.getString("last_user_designation");
			item.LAST_STEP.ACTION_ID = rs.getInt("last_action_id");
			item.LAST_STEP.ACTION_LABEL_PAST = rs.getString("last_action_label");
			item.LAST_STEP.CREATED_ON = rs.getTimestamp("last_created_on");
			item.LAST_STEP.COMPLETED_ON = rs.getTimestamp("last_completed_on");
			item.LAST_STEP.REMARKS = rs.getString("last_remarks");
			num++;
			list.add(item);
		}
		
		s.close();
		
		WorkflowDocument ret[] = list.toArray(new WorkflowDocument[num]);
		
		return ret;
		
	}

	public WorkflowDocument[] getInProcess(long UserID, int DaysOld) throws SQLException{
		
		Statement s = c.createStatement();
		
		List <WorkflowDocument>list = new ArrayList<WorkflowDocument>();
		int num = 0;
		
		String qry = "select " +
				"`wr`.`current_userid` AS `action_userid`," +
				"(select display_name from users where id = wr.current_userid) `current_user_display_name`," +
				"`wr`.`request_id` AS `request_id`," +
				"`wr`.`current_step_id` AS `current_step_id`," +
				"`wr`.`process_id` AS `process_id`," +
				"(select " +
				"`workflow_processes`.`process_name`" +
				" from " +
				"`workflow_processes`" +
				" where " +
				"(`workflow_processes`.`process_id` = `wr`.`process_id`)) AS `process_name`," +
				"`wr`.`created_on` AS `created_on`," +
				"`wr`.`created_by` AS `created_by`," +
				"`wr`.`current_action_id` AS `current_action_id`," +
				"(select " +
				"`workflow_actions`.`action_label`" +
				" from " +
				"`workflow_actions`" +
				" where " +
				"(`workflow_actions`.`action_id` = `wr`.`current_action_id`)) AS `current_action_label`," +
				"`wr`.`current_step_on` AS `current_step_on`," +
				"`wrs`.`user_id` AS `last_user_id`," +
				"`u`.`DISPLAY_NAME` AS `last_user_name`," +
				"`u`.`DEPARTMENT` AS `last_user_department`," +
				"`u`.`DESIGNATION` AS `last_user_designation`," +
				"`wrs`.`action_id` AS `last_action_id`," +
				"(select " +
				"`workflow_actions`.`action_label_past`" +
				" from " +
				"`workflow_actions`" +
				" where " +
				"(`workflow_actions`.`action_id` = `wrs`.`action_id`)) AS `last_action_label`," +
				"`wrs`.`created_on` AS `last_created_on`," +
				"`wrs`.`completed_on` AS `last_completed_on`," +
				"`wrs`.`remarks` AS `last_remarks`" +
				" from " +
				"((`workflow_requests` `wr`" +
				" join `workflow_requests_steps` `wrs`) " +
				" join `users` `u`)" +
				" where " +
				"((`wr`.`request_id` = `wrs`.`request_id`)" +
				"and (`wrs`.`step_id` = (`wr`.`current_step_id` - 1))" +
				"and (`wrs`.`user_id` = `u`.`ID`)" +
				"and (`wr`.`request_id` in (select distinct " +
				"wrs.request_id" +
				" from " +
				"workflow_requests wr," +
				"workflow_requests_steps wrs" +
				" where " +
				"wr.request_id = wrs.request_id" +
				" and wr.status_id = 1" +
				" and wrs.user_id = "+UserID+" and wr.created_on > from_days(to_days(curdate())-"+DaysOld+"))))";
		System.out.println(qry);
		ResultSet rs = s.executeQuery(qry);
		while(rs.next()){
			WorkflowDocument item = new WorkflowDocument();
			item.REQUEST_ID = rs.getLong("request_id");
			item.CURRENT_STEP.STEP_ID = rs.getInt("current_step_id");
			item.PROCESS_ID = rs.getInt("process_id");
			item.PROCESS_NAME = rs.getString("process_name");
			item.CREATED_ON = rs.getTimestamp("created_on");
			item.CREATED_BY = rs.getLong("created_by");
			
			item.CURRENT_STEP.ACTION_ID = rs.getInt("current_action_id");
			item.CURRENT_STEP.ACTION_LABEL = rs.getString("current_action_label");
			item.CURRENT_STEP.CREATED_ON = rs.getTimestamp("current_step_on");
			item.CURRENT_STEP.USER_ID = rs.getLong("action_userid");
			item.CURRENT_STEP.USER_DISPLAY_NAME = rs.getString("current_user_display_name");
			
			
			
			item.LAST_STEP.USER_ID = rs.getLong("last_user_id");
			item.LAST_STEP.USER_DISPLAY_NAME = rs.getString("last_user_name");
			item.LAST_STEP.USER_DEPARTMENT = rs.getString("last_user_department");
			item.LAST_STEP.USER_DESIGNATION = rs.getString("last_user_designation");
			item.LAST_STEP.ACTION_ID = rs.getInt("last_action_id");
			item.LAST_STEP.ACTION_LABEL_PAST = rs.getString("last_action_label");
			item.LAST_STEP.CREATED_ON = rs.getTimestamp("last_created_on");
			item.LAST_STEP.COMPLETED_ON = rs.getTimestamp("last_completed_on");
			item.LAST_STEP.REMARKS = rs.getString("last_remarks");
			
			
			if (UserID != item.CURRENT_STEP.USER_ID && item.CREATED_BY != UserID){
				num++;
				list.add(item);
			}
		}
		
		s.close();
		
		WorkflowDocument ret[] = list.toArray(new WorkflowDocument[num]);
		
		return ret;
		
	}

	public WorkflowDocument[] getRequested(long UserID, int DaysOld) throws SQLException{
		
		Statement s = c.createStatement();
		
		List <WorkflowDocument>list = new ArrayList<WorkflowDocument>();
		int num = 0;
		
		String qry = "select " +
				"`wr`.`current_userid` AS `action_userid`," +
				"(select display_name from users where id = wr.current_userid) `current_user_display_name`," +
				"`wr`.`request_id` AS `request_id`," +
				"`wr`.`current_step_id` AS `current_step_id`," +
				"`wr`.`process_id` AS `process_id`," +
				"(select " +
				"`workflow_processes`.`process_name`" +
				" from " +
				"`workflow_processes`" +
				" where " +
				"(`workflow_processes`.`process_id` = `wr`.`process_id`)) AS `process_name`," +
				"`wr`.`created_on` AS `created_on`," +
				"`wr`.`created_by` AS `created_by`," +
				"`wr`.`current_action_id` AS `current_action_id`," +
				"(select " +
				"`workflow_actions`.`action_label`" +
				" from " +
				"`workflow_actions`" +
				" where " +
				"(`workflow_actions`.`action_id` = `wr`.`current_action_id`)) AS `current_action_label`," +
				"`wr`.`current_step_on` AS `current_step_on`," +
				"`wrs`.`user_id` AS `last_user_id`," +
				"`u`.`DISPLAY_NAME` AS `last_user_name`," +
				"`u`.`DEPARTMENT` AS `last_user_department`," +
				"`u`.`DESIGNATION` AS `last_user_designation`," +
				"`wrs`.`action_id` AS `last_action_id`," +
				"(select " +
				"`workflow_actions`.`action_label_past`" +
				" from " +
				"`workflow_actions`" +
				" where " +
				"(`workflow_actions`.`action_id` = `wrs`.`action_id`)) AS `last_action_label`," +
				"`wrs`.`created_on` AS `last_created_on`," +
				"`wrs`.`completed_on` AS `last_completed_on`," +
				"`wrs`.`remarks` AS `last_remarks`" +
				" from " +
				"((`workflow_requests` `wr`" +
				" join `workflow_requests_steps` `wrs`) " +
				" join `users` `u`)" +
				" where " +
				"((`wr`.`request_id` = `wrs`.`request_id`)" +
				"and (`wrs`.`step_id` = (`wr`.`current_step_id` - 1))" +
				"and (`wrs`.`user_id` = `u`.`ID`)" +
				"and (`wr`.`request_id` in (select distinct " +
				"wrs.request_id" +
				" from " +
				"workflow_requests wr," +
				"workflow_requests_steps wrs" +
				" where " +
				"wr.request_id = wrs.request_id" +
				" and wr.status_id = 1" +
				" and wr.created_by = "+UserID+" and wr.created_on > from_days(to_days(curdate())-"+DaysOld+"))))";
		
		ResultSet rs = s.executeQuery(qry);
		while(rs.next()){
			WorkflowDocument item = new WorkflowDocument();
			item.REQUEST_ID = rs.getLong("request_id");
			item.CURRENT_STEP.STEP_ID = rs.getInt("current_step_id");
			item.PROCESS_ID = rs.getInt("process_id");
			item.PROCESS_NAME = rs.getString("process_name");
			item.CREATED_ON = rs.getTimestamp("created_on");
			item.CREATED_BY = rs.getLong("created_by");
			
			item.CURRENT_STEP.ACTION_ID = rs.getInt("current_action_id");
			item.CURRENT_STEP.ACTION_LABEL = rs.getString("current_action_label");
			item.CURRENT_STEP.CREATED_ON = rs.getTimestamp("current_step_on");
			item.CURRENT_STEP.USER_ID = rs.getLong("action_userid");
			item.CURRENT_STEP.USER_DISPLAY_NAME = rs.getString("current_user_display_name");
			
			
			
			item.LAST_STEP.USER_ID = rs.getLong("last_user_id");
			item.LAST_STEP.USER_DISPLAY_NAME = rs.getString("last_user_name");
			item.LAST_STEP.USER_DEPARTMENT = rs.getString("last_user_department");
			item.LAST_STEP.USER_DESIGNATION = rs.getString("last_user_designation");
			item.LAST_STEP.ACTION_ID = rs.getInt("last_action_id");
			item.LAST_STEP.ACTION_LABEL_PAST = rs.getString("last_action_label");
			item.LAST_STEP.CREATED_ON = rs.getTimestamp("last_created_on");
			item.LAST_STEP.COMPLETED_ON = rs.getTimestamp("last_completed_on");
			item.LAST_STEP.REMARKS = rs.getString("last_remarks");
			
			
			if (UserID != item.CURRENT_STEP.USER_ID){
				num++;
				list.add(item);
			}
		}
		
		s.close();
		
		WorkflowDocument ret[] = list.toArray(new WorkflowDocument[num]);
		
		return ret;
		
	}
	
	public WorkflowDocument[] getCompleted(long UserID, int DaysOld) throws SQLException{
		
		Statement s = c.createStatement();
		
		List <WorkflowDocument>list = new ArrayList<WorkflowDocument>();
		int num = 0;
		
		String qry = "select " +
				"`wr`.`current_userid` AS `action_userid`," +
				"`wr`.`request_id` AS `request_id`," +
				"`wr`.`current_step_id` AS `current_step_id`," +
				"`wr`.`process_id` AS `process_id`," +
				"(select " +
				"`workflow_processes`.`process_name`" +
				" from " +
				"`workflow_processes`" +
				" where " +
				"(`workflow_processes`.`process_id` = `wr`.`process_id`)) AS `process_name`," +
				"`wr`.`created_on` AS `created_on`," +
				"`wr`.`created_by` AS `created_by`," +
				"`wr`.`current_action_id` AS `current_action_id`," +
				"(select " +
				"`workflow_actions`.`action_label_past`" +
				" from " +
				"`workflow_actions`" +
				" where " +
				"(`workflow_actions`.`action_id` = `wr`.`current_action_id`)) AS `current_action_label_past`," +
				"`wr`.`current_step_on` AS `current_step_on`," +
				"`wrs`.`user_id` AS `last_user_id`," +
				"`u`.`DISPLAY_NAME` AS `last_user_name`," +
				"`u`.`DEPARTMENT` AS `last_user_department`," +
				"`u`.`DESIGNATION` AS `last_user_designation`," +
				"`wrs`.`action_id` AS `last_action_id`," +
				"(select " +
				"`workflow_actions`.`action_label_past`" +
				" from " +
				"`workflow_actions`" +
				" where " +
				"(`workflow_actions`.`action_id` = `wrs`.`action_id`)) AS `last_action_label`," +
				"`wrs`.`created_on` AS `last_created_on`," +
				"`wrs`.`completed_on` AS `last_completed_on`," +
				"`wrs`.`remarks` AS `last_remarks`" +
				" from " +
				"((`workflow_requests` `wr`" +
				" join `workflow_requests_steps` `wrs`) " +
				" join `users` `u`)" +
				" where " +
				"((`wr`.`request_id` = `wrs`.`request_id`)" +
				"and (`wrs`.`step_id` = (`wr`.`current_step_id`))" +
				"and (`wrs`.`user_id` = `u`.`ID`)" +
				"and (`wr`.`request_id` in (select distinct " +
				"wrs.request_id" +
				" from " +
				"workflow_requests wr," +
				"workflow_requests_steps wrs" +
				" where " +
				"wr.request_id = wrs.request_id" +
				" and wr.status_id = 2" +
				" and wrs.user_id = "+UserID+" and wr.created_on > from_days(to_days(curdate())-"+DaysOld+"))))";
		
		ResultSet rs = s.executeQuery(qry);
		while(rs.next()){
			WorkflowDocument item = new WorkflowDocument();
			item.REQUEST_ID = rs.getLong("request_id");
			item.CURRENT_STEP.STEP_ID = rs.getInt("current_step_id");
			item.PROCESS_ID = rs.getInt("process_id");
			item.PROCESS_NAME = rs.getString("process_name");
			item.CREATED_ON = rs.getTimestamp("created_on");
			item.CREATED_BY = rs.getLong("created_by");
			
			item.CURRENT_STEP.ACTION_ID = rs.getInt("current_action_id");
			item.CURRENT_STEP.ACTION_LABEL_PAST = rs.getString("current_action_label_past");
			item.CURRENT_STEP.CREATED_ON = rs.getTimestamp("current_step_on");
			item.CURRENT_STEP.USER_ID = UserID;
			
			item.CURRENT_STEP.USER_ID = rs.getLong("last_user_id");
			item.CURRENT_STEP.USER_DISPLAY_NAME = rs.getString("last_user_name");
			item.CURRENT_STEP.COMPLETED_ON = rs.getTimestamp("last_completed_on");
			
			/*
			item.LAST_STEP.USER_DEPARTMENT = rs.getString("last_user_department");
			item.LAST_STEP.USER_DESIGNATION = rs.getString("last_user_designation");
			item.LAST_STEP.ACTION_ID = rs.getInt("last_action_id");
			item.LAST_STEP.ACTION_LABEL_PAST = rs.getString("last_action_label");
			item.LAST_STEP.CREATED_ON = rs.getTimestamp("last_created_on");
			
			item.LAST_STEP.REMARKS = rs.getString("last_remarks");
			*/
			num++;
			list.add(item);
		}
		
		s.close();
		
		WorkflowDocument ret[] = list.toArray(new WorkflowDocument[num]);
		
		return ret;
		
	}

	public WorkflowDocument[] getDeclined(long UserID, int DaysOld) throws SQLException{
		
		Statement s = c.createStatement();
		
		List <WorkflowDocument>list = new ArrayList<WorkflowDocument>();
		int num = 0;
		
		String qry = "select " +
				"`wr`.`current_userid` AS `action_userid`," +
				"`wr`.`request_id` AS `request_id`," +
				"`wr`.`current_step_id` AS `current_step_id`," +
				"`wr`.`process_id` AS `process_id`," +
				"(select " +
				"`workflow_processes`.`process_name`" +
				" from " +
				"`workflow_processes`" +
				" where " +
				"(`workflow_processes`.`process_id` = `wr`.`process_id`)) AS `process_name`," +
				"`wr`.`created_on` AS `created_on`," +
				"`wr`.`created_by` AS `created_by`," +
				"`wr`.`current_action_id` AS `current_action_id`," +
				"(select " +
				"`workflow_actions`.`action_label_past`" +
				" from " +
				"`workflow_actions`" +
				" where " +
				"(`workflow_actions`.`action_id` = `wr`.`current_action_id`)) AS `current_action_label_past`," +
				"`wr`.`current_step_on` AS `current_step_on`," +
				"`wrs`.`user_id` AS `last_user_id`," +
				"`u`.`DISPLAY_NAME` AS `last_user_name`," +
				"`u`.`DEPARTMENT` AS `last_user_department`," +
				"`u`.`DESIGNATION` AS `last_user_designation`," +
				"`wrs`.`action_id` AS `last_action_id`," +
				"(select " +
				"`workflow_actions`.`action_label_past`" +
				" from " +
				"`workflow_actions`" +
				" where " +
				"(`workflow_actions`.`action_id` = `wrs`.`action_id`)) AS `last_action_label`," +
				"`wrs`.`created_on` AS `last_created_on`," +
				"`wrs`.`completed_on` AS `last_completed_on`," +
				"`wrs`.`remarks` AS `last_remarks`" +
				" from " +
				"((`workflow_requests` `wr`" +
				" join `workflow_requests_steps` `wrs`) " +
				" join `users` `u`)" +
				" where " +
				"((`wr`.`request_id` = `wrs`.`request_id`)" +
				"and (`wrs`.`step_id` = (`wr`.`current_step_id`))" +
				"and (`wrs`.`user_id` = `u`.`ID`)" +
				"and (`wr`.`request_id` in (select distinct " +
				"wrs.request_id" +
				" from " +
				"workflow_requests wr," +
				"workflow_requests_steps wrs" +
				" where " +
				"wr.request_id = wrs.request_id" +
				" and wr.status_id = 3" +
				" and wrs.user_id = "+UserID+" and wr.created_on > from_days(to_days(curdate())-"+DaysOld+"))))";
		
		ResultSet rs = s.executeQuery(qry);
		while(rs.next()){
			WorkflowDocument item = new WorkflowDocument();
			item.REQUEST_ID = rs.getLong("request_id");
			item.CURRENT_STEP.STEP_ID = rs.getInt("current_step_id");
			item.PROCESS_ID = rs.getInt("process_id");
			item.PROCESS_NAME = rs.getString("process_name");
			item.CREATED_ON = rs.getTimestamp("created_on");
			item.CREATED_BY = rs.getLong("created_by");
			
			item.CURRENT_STEP.ACTION_ID = rs.getInt("current_action_id");
			item.CURRENT_STEP.ACTION_LABEL_PAST = rs.getString("current_action_label_past");
			item.CURRENT_STEP.CREATED_ON = rs.getTimestamp("current_step_on");
			item.CURRENT_STEP.USER_ID = UserID;
			
			item.CURRENT_STEP.USER_ID = rs.getLong("last_user_id");
			item.CURRENT_STEP.USER_DISPLAY_NAME = rs.getString("last_user_name");
			item.CURRENT_STEP.COMPLETED_ON = rs.getTimestamp("last_completed_on");
			
			/*
			item.LAST_STEP.USER_DEPARTMENT = rs.getString("last_user_department");
			item.LAST_STEP.USER_DESIGNATION = rs.getString("last_user_designation");
			item.LAST_STEP.ACTION_ID = rs.getInt("last_action_id");
			item.LAST_STEP.ACTION_LABEL_PAST = rs.getString("last_action_label");
			item.LAST_STEP.CREATED_ON = rs.getTimestamp("last_created_on");
			
			item.LAST_STEP.REMARKS = rs.getString("last_remarks");
			*/
			num++;
			list.add(item);
		}
		
		s.close();
		
		WorkflowDocument ret[] = list.toArray(new WorkflowDocument[num]);
		
		return ret;
		
	}
	

	
	
	public String getRequestPreview(int ProcessID, long RequestID) throws SQLException{
		
		//System.out.println(ProcessID+" "+RequestID);
		
		Statement s = c.createStatement();
		
		String content = "";
		
		switch (ProcessID){
		
			case 1: // Sampling
				ResultSet rs = s.executeQuery("SELECT s.outlet_name, s.business_type, s.address, s.region, s.advance_company_share, s.advance_agency_share, s.fixed_company_share, s.fixed_agency_share, s.sampling_id, (select count(sampling_id) from sampling_percase where sampling_id = s.sampling_id) percase, s.outlet_id outlet_id, (SELECT Owner FROM outletmaster where outlet_id = s.outlet_id) outlet_owner FROM sampling s where s.request_id = "+RequestID);
				if (rs.first()){
					
					
					String owner = rs.getString("outlet_owner");
					if (owner == null || owner.length() < 2){
						owner = "N/A";
					}
						
					String type = "";
					if ((rs.getDouble(5)+rs.getDouble(6)) != 0){
						type = "Advance";
					}
					if ((rs.getDouble(7)+rs.getDouble(8)) != 0){
						if (type.length() > 0){
							type += ", ";
						}
						type += "Fixed";
					}
					if (rs.getDouble("percase") > 0){
						if (type.length() > 0){
							type += ", ";
						}
						type += "Per Case";
					}
					
					content = "<p><span style='font-weight: 400; font-size: 13px;'>"+WordUtils.capitalizeFully(rs.getString(1))+" "+WordUtils.capitalizeFully(rs.getString(2))+" ("+rs.getString("outlet_id")+"), "+WordUtils.capitalizeFully(rs.getString(3))+", "+rs.getString(4)+"</span></p>"+
			        "<p><span style='font-weight: 400; font-size: 12px;'><strong>Owner:</strong> "+owner+" | <strong>Type:</strong> "+type+"</p>";
				
				}
				rs.close();
				break;
			case 2: // Monthly Discount
				
				ResultSet rs2 = s.executeQuery("select count(smr.approval_id) total_outlets, date_format(month, '%M %Y') period from sampling_monthly_request_document smrd, sampling_monthly_request smr where smrd.id=smr.document_id and smrd.request_id="+RequestID);
				if(rs2.first()){
				
					content = "<p><span style='font-weight: 400; font-size: 13px;'>"+rs2.getString("period")+"</span></p>"+
				        "<p><span style='font-weight: 400; font-size: 12px;'><strong>"+rs2.getString("total_outlets")+"</strong> Outlets</p>";
				
				}
				
				break;
				
			case 3: // Promotion Request
				
				
				ResultSet rs3 = s.executeQuery("select * from inventory_sales_promotions_request where request_id="+RequestID);
				if(rs3.first()){
				
					content = "<p><span style='font-weight: 400; font-size: 13px;'>"+rs3.getString("label")+" | "+Utilities.getDisplayDateFormat(rs3.getDate("valid_from"))+" - "+Utilities.getDisplayDateFormat(rs3.getDate("valid_to"))+"</span></p>"+
				        "<p><span style='font-weight: 400; font-size: 12px;'></p>";
				
				}
				
				break;
				
			case 4: // Promotion Request
				
				
				ResultSet rs4 = s.executeQuery("select * from inventory_sales_discounts_request where request_id="+RequestID);
				if(rs4.first()){
				
					content = "<p><span style='font-weight: 400; font-size: 13px;'>"+Utilities.getDisplayDateFormat(rs4.getDate("valid_from"))+" - "+Utilities.getDisplayDateFormat(rs4.getDate("valid_to"))+"</span></p>";
				    
				
				}
				
				
				break;
				
			case 5: // Credit Limit Request
				
				
				ResultSet rs5 = s.executeQuery("select *,(select name from common_distributors cd where cd.distributor_id = customer_id) customer_name from gl_customer_credit_limit_request where request_id="+RequestID);
				if(rs5.first()){
				
					content = "<p><span style='font-weight: 400; font-size: 13px;'>"+rs5.getLong("customer_id")+" - "+rs5.getString("customer_name")+" | "+Utilities.getDisplayDateFormat(rs5.getDate("valid_from"))+" - "+Utilities.getDisplayDateFormat(rs5.getDate("valid_to"))+"<br/><b>Credit Limit: "+Utilities.getDisplayCurrencyFormatRounded(rs5.getDouble("credit_limit"))+"</b></span></p>";
				    
				}
				
				break;
				
			case 8: // Promotion Request
				
				
				ResultSet rs8 = s.executeQuery("select * from inventory_primary_percase_request where request_id="+RequestID);
				if(rs8.first()){
				
					content = "<p><span style='font-weight: 400; font-size: 13px;'>"+Utilities.getDisplayDateFormat(rs8.getDate("valid_from"))+" - "+Utilities.getDisplayDateFormat(rs8.getDate("valid_to"))+"</span></p>";
				    
				
				}
				
				
				break;	
			case 9: // Empty Credit Limit Request
				
				
				ResultSet rs7 = s.executeQuery("select *,(select name from common_distributors cd where cd.distributor_id = ececlr.distributor_id) customer_name,ececlr.credit_type,(select label from ec_empty_credit_types where id=ececlr.credit_type) credit_type_name from ec_empty_credit_limit_request ececlr where ececlr.request_id="+RequestID);
				if(rs7.first()){
				
					content = "<p><span style='font-weight: 400; font-size: 13px;'>"+rs7.getLong("distributor_id")+" - "+rs7.getString("customer_name")+" | "+Utilities.getDisplayDateFormat(rs7.getDate("start_date"))+" - "+Utilities.getDisplayDateFormat(rs7.getDate("end_date"))+"<br/><b>Credit Type: "+rs7.getString("credit_type_name")+"</b></span></p>";
				    
				}
				
				break;
		}
		
		
		s.close();
		return content;
	}
	
	public WorkflowAttachment[] getAttachments(long RequestID) throws SQLException{
		
		
		Statement s = c.createStatement();
		
		List <WorkflowAttachment>list = new ArrayList<WorkflowAttachment>();
		int num = 0;
		
		ResultSet rs = s.executeQuery("select wra.filename, wra.attached_by, (select display_name from users where id = wra.attached_by) display_name, wra.attached_on, wra.description from workflow_requests_attachments wra where wra.request_id = "+ RequestID+" order by wra.attached_on desc");
		while(rs.next()){
			WorkflowAttachment item = new WorkflowAttachment();

			item.FILENAME = rs.getString("filename");
			item.ATTACHED_BY_USERID = rs.getLong("attached_by");
			item.ATTACHED_BY_DISPLAY_NAME = rs.getString("display_name");
			item.ATTACHED_ON = rs.getTimestamp("attached_on");
			item.DESCRIPTION = rs.getString("description");
			
			num++;
			list.add(item);
		}
		
		s.close();
		
		WorkflowAttachment ret[] = list.toArray(new WorkflowAttachment[num]);
		
		return ret;
		
	}
	
	public void close() throws SQLException{
		ds.dropConnection();
	}

	
}
