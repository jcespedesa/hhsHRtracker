package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.SettingsEntity;
import com.trc.repositories.SettingsRepository;

@Service
public class SettingsService 
{
	@Autowired
	SettingsRepository repository;
	
	public List<SettingsEntity> getAllSettings()
	{
		List<SettingsEntity> result=(List<SettingsEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<SettingsEntity>();
		
	}
	
	public SettingsEntity getSettingById(Long id) throws RecordNotFoundException
	{
		Optional<SettingsEntity> setting=repository.findById(id);
		
		if(setting.isPresent())
			return setting.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public SettingsEntity createOrUpdate(SettingsEntity entity)
	{
		if(entity.getSettingid()==null)
		{
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<SettingsEntity> setting=repository.findById(entity.getSettingid());
			
			if(setting.isPresent())
			{
				
				SettingsEntity newEntity=setting.get();
				
				newEntity.setSname(entity.getSname());
				newEntity.setPath(entity.getPath());
				newEntity.setParam1(entity.getParam1());
				newEntity.setParam2(entity.getParam2());
					
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
	
	public void deleteSettingById(Long id) throws RecordNotFoundException
	{
		Optional<SettingsEntity> setting=repository.findById(id);
		
		if(setting.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Setting record exist for given id");
		
		
	}
	
	public int findDuplicates(String setting)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findSettingDuplicity(setting);
		
		
		return priznakDuplicate;
	}
	
	public String findEmailSender(String setting)
	{
		String sender=null;
		
		sender=repository.getEmailSender(setting);
		
		
		return sender;
	}
	
}
