package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="titles")
public class TitlesEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long titleid;
	
	@Column(name="titledesc")
	private String titleDesc;
	
	@Column(name="titlenum")
	private String titleNum;
	
	@Column(name="active")
	private String active;
	
	@Column(name="type")
	private String type;
	
	//Constructors
	
	@Override
	public String toString()
	{
		return "TitlesEntity[titleid="+ titleid +",titleDesc="+ titleDesc +",titleNum="+ titleNum +",active="+ active +",type="+ type +"]";				
		
	}
	
	//Getters and setters

	public Long getTitleid() 
	{
		return titleid;
	}

	public void setTitleid(Long titleid) 
	{
		this.titleid=titleid;
	}

	public String getTitleDesc() 
	{
		return titleDesc;
	}

	public void setTitleDesc(String titleDesc) 
	{
		this.titleDesc=titleDesc;
	}

	public String getTitleNum() 
	{
		return titleNum;
	}

	public void setTitleNum(String titleNum) 
	{
		this.titleNum=titleNum;
	}

	public String getActive() 
	{
		return active;
	}

	public void setActive(String active) 
	{
		this.active=active;
	}

	public String getType() 
	{
		return type;
	}

	public void setType(String type) 
	{
		this.type=type;
	}
	
	
	
}
