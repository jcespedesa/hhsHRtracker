package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.ProjectTypesEntity;
import com.trc.repositories.ProjectTypesRepository;


@Service
public class ProjectTypesService 
{
	@Autowired
	ProjectTypesRepository repository;
	
	public List<ProjectTypesEntity> getAllTypes()
	{
		List<ProjectTypesEntity> result=(List<ProjectTypesEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ProjectTypesEntity>();
		
	}
	
	public List<ProjectTypesEntity> getActives()
	{
		List<ProjectTypesEntity> result=(List<ProjectTypesEntity>) repository.getActives();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ProjectTypesEntity>();
		
	}
	
	public ProjectTypesEntity getTypeById(Long id) throws RecordNotFoundException
	{
		Optional<ProjectTypesEntity> type=repository.findById(id);
		
		if(type.isPresent())
			return type.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public ProjectTypesEntity createOrUpdate(ProjectTypesEntity entity)
	{
		if(entity.getTypeid()==null)
		{
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<ProjectTypesEntity> type=repository.findById(entity.getTypeid());
			
			if(type.isPresent())
			{
				
				ProjectTypesEntity newEntity=type.get();
				
				newEntity.setTypeNumber(entity.getTypeNumber());
				newEntity.setType(entity.getType());
				
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
	
	public void deleteTypeById(Long id) throws RecordNotFoundException
	{
		Optional<ProjectTypesEntity> type=repository.findById(id);
		
		if(type.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Project Type record exist for given id");
			
	}
		
	
	public int findDuplicates(String typeNumber)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findTypesDuplicity(typeNumber);
		
		
		return priznakDuplicate;
	}

	public String getTypeName(String typeNumber) 
	{
		String type=null;
		
		type=repository.getType(typeNumber);
		
		return type;
	}
}
