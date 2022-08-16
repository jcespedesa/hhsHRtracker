package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="hhsdivisions")
public class HhsDivisionsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long divisionid;
	
	@Column(name="division")
	private String division;
	
	@Column(name="divisionnumber")
	private String divisionNumber;
	
	@Column(name="director")
	private String director;
	
	@Column(name="clientid")
	private String clientId;
	
	@Column(name="active")
	private String active;
	
	@Override
	public String toString()
	{
		return "HhsEntity[divisionid="+ divisionid +",division="+ division +",divisionNumber="+ divisionNumber +",director="+ director +",clientId="+ clientId +",active="+ active +"]";				
		
	}

	public Long getDivisionid() 
	{
		return divisionid;
	}

	public void setDivisionid(Long divisionid) 
	{
		this.divisionid=divisionid;
	}

	public String getDivision() 
	{
		return division;
	}

	public void setDivision(String division) 
	{
		this.division=division;
	}

	public String getDivisionNumber() 
	{
		return divisionNumber;
	}

	public void setDivisionNumber(String divisionNumber) 
	{
		this.divisionNumber=divisionNumber;
	}

	public String getDirector() 
	{
		return director;
	}

	public void setDirector(String director) 
	{
		this.director=director;
	}

	public String getClientId() 
	{
		return clientId;
	}

	public void setClientId(String clientId) 
	{
		this.clientId=clientId;
	}

	public String getActive() 
	{
		return active;
	}

	public void setActive(String active) 
	{
		this.active=active;
	}
	
}
