package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="logs")
public class LogsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long logid;
	
	@Column(name="subject")
	private String subject;
	
	@Column(name="action")
	private String action;
	
	@Column(name="object")
	private String object;
	
	@Column(name="datecreation",updatable=false, insertable=false)
	private String dateCreation;
		
	//Constructors
	
	@Override
	public String toString()
	{
		return "LogsEntity[logid="+ logid +",subject="+ subject +",action="+ action +",object="+ object +"]";				
			
	}

	public Long getLogid() 
	{
		return logid;
	}

	public void setLogid(Long logid) 
	{
		this.logid=logid;
	}

	public String getAction() 
	{
		return action;
	}

	public void setAction(String action) 
	{
		this.action=action;
	}

	public String getObject() 
	{
		return object;
	}

	public void setObject(String object) 
	{
		this.object=object;
	}

	public String getSubject() 
	{
		return subject;
	}

	public void setSubject(String subject) 
	{
		this.subject=subject;
	}
	
	public String getDateCreation() 
	{
		return dateCreation;
	}
	
}
