package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.TitlesEntity;
import com.trc.repositories.TitlesRepository;

@Service
public class TitlesService 
{
	@Autowired
	TitlesRepository repository;
	
	public List<TitlesEntity> getAllTitles()
	{
		List<TitlesEntity> result=(List<TitlesEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<TitlesEntity>();
		
	}
	
	public List<TitlesEntity> getAllByName()
	{
		List<TitlesEntity> result=(List<TitlesEntity>) repository.getAllByName();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<TitlesEntity>();
		
	}
	
	public TitlesEntity getTitleById(Long id) throws RecordNotFoundException
	{
		Optional<TitlesEntity> title=repository.findById(id);
		
		if(title.isPresent())
			return title.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public TitlesEntity createOrUpdate(TitlesEntity entity)
	{
		if(entity.getTitleid()==null)
		{
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<TitlesEntity> title=repository.findById(entity.getTitleid());
			
			if(title.isPresent())
			{
				
				TitlesEntity newEntity=title.get();
				
				newEntity.setTitleDesc(entity.getTitleDesc());
				newEntity.setTitleNum(entity.getTitleNum());
								
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
	
	public void deleteTitleById(Long id) throws RecordNotFoundException
	{
		Optional<TitlesEntity> title=repository.findById(id);
		
		if(title.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Title record exist for given id");
		
		
	}
	
	public String getTitleByNumber(String titleNum)
	{
		String title=repository.getTitleName(titleNum);
		
		if(title==null)
			return "";
		else
			return title;
		
	}
	
	public List<TitlesEntity> getAllActives()
	{
		List<TitlesEntity> result=(List<TitlesEntity>) repository.getAllActives();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<TitlesEntity>();
		
	}
}
