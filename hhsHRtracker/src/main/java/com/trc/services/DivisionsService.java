package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.DivisionsEntity;
import com.trc.repositories.DivisionsRepository;

@Service
public class DivisionsService 
{
	@Autowired
	DivisionsRepository repository;
	
	public List<DivisionsEntity> getAllDivisions()
	{
		List<DivisionsEntity> result=(List<DivisionsEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<DivisionsEntity>();
		
	}
	
	public List<DivisionsEntity> getAllByName()
	{
		List<DivisionsEntity> result=(List<DivisionsEntity>) repository.getAllByName();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<DivisionsEntity>();
		
	}
	
	public List<DivisionsEntity> getAllPriznakApp()
	{
		List<DivisionsEntity> result=(List<DivisionsEntity>) repository.getByPriznakApp();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<DivisionsEntity>();
		
	}
	
	public DivisionsEntity getDivisionById(Long id) throws RecordNotFoundException
	{
		Optional<DivisionsEntity> division=repository.findById(id);
		
		if(division.isPresent())
			return division.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public DivisionsEntity createOrUpdate(DivisionsEntity entity)
	{
		if(entity.getDivisionid()==null)
		{
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<DivisionsEntity> division=repository.findById(entity.getDivisionid());
			
			if(division.isPresent())
			{
				
				DivisionsEntity newEntity=division.get();
				
				newEntity.setDnumber(entity.getDnumber());
				newEntity.setDname(entity.getDname());
								
				newEntity.setActive(entity.getActive());
				
				newEntity=repository.save(newEntity);
				
				return newEntity;
				
			}
			else
			{
				entity=repository.save(entity);
				
				return entity;
				
			}
			
		}
				
		
	}
	
	public void deleteDivisionById(Long id) throws RecordNotFoundException
	{
		Optional<DivisionsEntity> division=repository.findById(id);
		
		if(division.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Division record exist for given id");
		
		
	}
	
	public String getDivisionByNumber(String dnumber)
	{
		String division=repository.getDivisionName(dnumber);
		
		if(division==null)
			return "";
		else
			return division;
		
	}
}
