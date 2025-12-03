package com.pbc.employee;

import java.util.LinkedList;
import java.util.List;

public  class EmployeeJSONObj {
    private String name;
    private String parentID;
    private String designation;
    private String department;
    private List<EmployeeJSONObj> children = new LinkedList();
    
    EmployeeJSONObj()
    {

    }
    public void setName(String n)
    {
        name=n ;
    }
    public String getName()
    {
        return name;
    }
    
    public void setParentId(String pid)
    {
    	parentID=pid;
    }
    public String getParentId()
    {
    	return parentID;
    }
    
    public void setDesignation(String d)
    {
    	designation = d;
    }
    public String getDesignation()
    {
    	return designation;
    }
    
    public void setDepartment(String d)
    {
    	department = d;
    }
    public String getDepartment()
    {
    	return department;
    }
    
    public List<EmployeeJSONObj> getChild()
    {
    	return children;
    }

    public String toString() {
        return "name: " + name + ", children = " + children;
    }

}
