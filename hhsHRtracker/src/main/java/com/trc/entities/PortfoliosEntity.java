package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="portfolios")
public class PortfoliosEntity
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long pid;
	
	@Column(name="pname")
	private String pname;
	
	@Column(name="pnumber")
	private String pnumber;
	
	@Column(name="director")
	private String director;
	
	@Column(name="directorid")
	private String directorId;
	
	@Column(name="active")
	private String active;
	
	//Constructors
	
	@Override
	public String toString()
	{
		return "PortfoliosEntity[pid="+ pid +",pname="+ pname +",pnumber="+ pnumber +",director="+ director +",directorId="+ directorId +",active="+ active +"]";				
		
	}

	public Long getPid() 
	{
		return pid;
	}

	public void setPid(Long pid) 
	{
		this.pid=pid;
	}

	public String getPname() 
	{
		return pname;
	}

	public void setPname(String pname) 
	{
		this.pname=pname;
	}

	public String getPnumber() 
	{
		return pnumber;
	}

	public void setPnumber(String pnumber) 
	{
		this.pnumber=pnumber;
	}

	public String getDirector() 
	{
		return director;
	}

	public void setDirector(String director) 
	{
		this.director=director;
	}

	public String getDirectorId() 
	{
		return directorId;
	}

	public void setDirectorId(String directorId) 
	{
		this.directorId=directorId;
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
