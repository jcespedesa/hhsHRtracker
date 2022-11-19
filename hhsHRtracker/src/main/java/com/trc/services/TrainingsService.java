package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.TrainingsEntity;
import com.trc.repositories.TrainingsRepository;

@Service
public class TrainingsService 
{
	@Autowired
	TrainingsRepository repository;
	
	public List<TrainingsEntity> getAllTrainings()
	{
		List<TrainingsEntity> result=(List<TrainingsEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<TrainingsEntity>();
		
	}
	
	public List<TrainingsEntity> getActives()
	{
		List<TrainingsEntity> result=(List<TrainingsEntity>) repository.getActives();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<TrainingsEntity>();
		
	}
	
	public TrainingsEntity getTrainingById(Long id) throws RecordNotFoundException
	{
		Optional<TrainingsEntity> training=repository.findById(id);
		
		if(training.isPresent())
			return training.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public TrainingsEntity createOrUpdate(TrainingsEntity entity)
	{
		if(entity.getTrainingid()==null)
		{
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<TrainingsEntity> training=repository.findById(entity.getTrainingid());
			
			if(training.isPresent())
			{
				
				TrainingsEntity newEntity=training.get();
				
				newEntity.setTnumber(entity.getTnumber());
				newEntity.setTname(entity.getTname());
				
				newEntity.setProjectType(entity.getProjectType());
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
	
	public void deleteTrainingById(Long id) throws RecordNotFoundException
	{
		Optional<TrainingsEntity> training=repository.findById(id);
		
		if(training.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No training record exist for given id");
			
	}
		
	
	public int findDuplicates(String tnumber)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findTrainingDuplicity(tnumber);
		
		
		return priznakDuplicate;
	}

	public String getTrainingName(String tnumber) 
	{
		String tname=null;
		
		tname=repository.getTname(tnumber);
		
		return tname;
	}
}
