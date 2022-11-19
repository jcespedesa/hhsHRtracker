package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="contractats")
public class ContractATsEntity 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long recordid;
	
	@Column(name="contract")
	private String contract;
	
	@Column(name="trainingnum")
	private String trainingNum;
	
	@Column(name="buffername")
	private String bufferName;
	
	//Constructors
	
	@Override
	public String toString()
	{
		return "ContractATsEntity[recordid="+ recordid +",contract="+ contract +",trainingNum="+ trainingNum +",byfferName="+ bufferName +"]";				
		
	}

	public Long getRecordid() 
	{
		return recordid;
	}

	public void setRecordid(Long recordid) 
	{
		this.recordid=recordid;
	}

	public String getContract() 
	{
		return contract;
	}

	public void setContract(String contract) 
	{
		this.contract=contract;
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
