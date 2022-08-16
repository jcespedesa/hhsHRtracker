package com.trc.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.BufferTimeEntity;
import com.trc.repositories.BufferTimeRepository;


@Service
public class BufferTimeService 
{
	
	@Autowired
	BufferTimeRepository repository;
	
	
	public List<BufferTimeEntity> getAllByUsername(String username)
	{
		List<BufferTimeEntity> result=(List<BufferTimeEntity>) repository.getAllByUsername(username);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<BufferTimeEntity>();
		
	}
	
	
	public void create(BufferTimeEntity entity)
	{
		
		entity=repository.save(entity);
							
	}
	
	public void deleteByRecordId(Long recordId) throws RecordNotFoundException
	{
		BufferTimeEntity entity=repository.getByRecordId(recordId);
		
		if(entity == null)
			System.out.println("No Buffer Time record exist for given recordId");
				
	}
	
	public void deleteByUsername(String username) throws RecordNotFoundException
	{
		repository.deleteByUsername(username);
			
				
	}
	
	
	public Long getRecordId(String username) throws RecordNotFoundException
	{
		Long recordId=repository.getRecordId(username);
		
		if(recordId == null)
			System.out.println("No Buffer Time record exist for given username");
			
		return recordId;
			
	}
	
	
		
	public BufferTimeEntity getByUsername(String username) throws RecordNotFoundException
	{
		BufferTimeEntity entity=repository.getByUsername(username);
		
		if(entity == null)
			System.out.println("No Buffer Time record exist for given username");
				
		return entity;
			
	}
	
}
