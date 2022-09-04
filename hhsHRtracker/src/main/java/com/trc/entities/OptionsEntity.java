package com.trc.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OptionsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long optionid;
	
	private String optionNumber;
	private String optionName;
	
	public Long getOptionid() 
	{
		return optionid;
	}

	public void setOptionid(Long optionid) 
	{
		this.optionid=optionid;
	}
	
	public String getOptionNumber() 
	{
		return optionNumber;
	}

	public void setOptionNumber(String optionNumber) 
	{
		this.optionNumber=optionNumber;
	}

	public String getOptionName() 
	{
		return optionName;
	}

	public void setOptionName(String optionName) 
	{
		this.optionName=optionName;
	}

	
	
}
