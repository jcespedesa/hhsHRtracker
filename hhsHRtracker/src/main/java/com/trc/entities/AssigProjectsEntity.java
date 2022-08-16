package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="assignedprojects")
public class AssigProjectsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long itemid;
	
	@Column(name="project")
	private String project;
	
	@Column(name="projectname")
	private String projectName;
	
	@Column(name="userid")
	private String userId;
	
	@Column(name="department")
	private String department;
	
	@Column(name="casemanager")
	private String caseManager;
	
	//Constructors
	
	@Override
	public String toString()
	{
		return "AssigProjectsEntity[itemid="+ itemid +",project="+ project +",projectName="+ projectName +",department="+ department +",userId="+ userId +",caseManager="+ caseManager +"]";				
			
	}

	public Long getItemid() 
	{
		return itemid;
	}

	public void setItemid(Long itemid) 
	{
		this.itemid=itemid;
	}

	public String getProject() 
	{
		return project;
	}

	public void setProject(String project) 
	{
		this.project=project;
	}

	public String getProjectName() 
	{
		return projectName;
	}

	public void setProjectName(String projectName) 
	{
		this.projectName=projectName;
	}

	public String getUserId() 
	{
		return userId;
	}

	public void setUserId(String userId) 
	{
		this.userId=userId;
	}

	public String getDepartment() 
	{
		return department;
	}

	public void setDepartment(String department) 
	{
		this.department=department;
	}

	public String getCaseManager() 
	{
		return caseManager;
	}

	public void setCaseManager(String caseManager) 
	{
		this.caseManager=caseManager;
	}
	
	
}
