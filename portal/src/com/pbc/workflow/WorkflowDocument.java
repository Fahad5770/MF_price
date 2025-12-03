package com.pbc.workflow;

import java.util.Date;

public class WorkflowDocument {
	
	public long REQUEST_ID;
	public int PROCESS_ID;
	public String PROCESS_NAME;
	public long CREATED_BY;
	public String CREATED_BY_NAME;
	public Date CREATED_ON;
	
	public WorkflowStep CURRENT_STEP = new WorkflowStep();
	public WorkflowStep LAST_STEP = new WorkflowStep();
	
}
