package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.TitleATsEntity;
import com.trc.repositories.TitleATsRepository;

@Service
public class TitleATsService 
{
	@Autowired
	TitleATsRepository repository;
	
	public List<TitleATsEntity> getATbyTitle(String titleNum)
	{
		List<TitleATsEntity> result=(List<TitleATsEntity>) repository.getAssignated(titleNum);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<TitleATsEntity>();
		
	}
	
	public void deleteTraining(Long id) throws RecordNotFoundException
	{
		Optional<TitleATsEntity> training=repository.findById(id);
		
		if(training.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Assigned Training record exist for given title");
		
		
	}

	public TitleATsEntity createOrUpdate(TitleATsEntity entity)
	{
		if(entity.getRecordid()==null)
		{
			entity=repository.save(entity);
				
						
			return entity;
		}
		else
		{
			Optional<TitleATsEntity> training=repository.findById(entity.getRecordid());
			
			if(training.isPresent())
			{
				
				TitleATsEntity newEntity=training.get();
				
				newEntity.setTitleNum(entity.getTitleNum());
				newEntity.setTrainingNum(entity.getTrainingNum());
				newEntity.setBufferName(entity.getBufferName());
																
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
	
	
	public TitleATsEntity getTitleATbyId(Long Id) throws RecordNotFoundException
	{
		
		Optional<TitleATsEntity> training=repository.findById(Id);
			
		if(training.isPresent())
			return training.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
		
	}
	
	public int findDuplicates(String trainingNum,String titleNum)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findAssignationDuplicity(trainingNum,titleNum);
		
		
		return priznakDuplicate;
	}
	
	public List<TitleATsEntity> getATsByTitle(String titleNum)
	{
		List<TitleATsEntity> result=(List<TitleATsEntity>) repository.getATsByTitle(titleNum);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<TitleATsEntity>();
		
	}
}
