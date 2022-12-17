package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="titleats")
public class TitleATsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long recordid;
	
	@Column(name="titlenum")
	private String titleNum;
	
	@Column(name="trainingnum")
	private String trainingNum;
	
	@Column(name="buffername")
	private String bufferName;
	
	//Constructors
	
	@Override
	public String toString()
	{
		return "TitleATsEntity[recordid="+ recordid +",titleNum="+ titleNum +",trainingNum="+ trainingNum +",bufferName="+ bufferName +"]";				
		
	}
	
	//Getters and Setters

	public Long getRecordid() 
	{
		return recordid;
	}

	public void setRecordid(Long recordid) 
	{
		this.recordid=recordid;
	}

	public String getTitleNum() 
	{
		return titleNum;
	}

	public void setTitleNum(String titleNum) 
	{
		this.titleNum=titleNum;
	}

	public String getTrainingNum() 
	{
		return trainingNum;
	}

	public void setTrainingNum(String trainingNum) 
	{
		this.trainingNum=trainingNum;
	}

	public String getBufferName() 
	{
		return bufferName;
	}

	public void setBufferName(String bufferName) 
	{
		this.bufferName=bufferName;
	}
	
	
}
