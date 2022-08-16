package com.trc.services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.LogsEntity;
import com.trc.repositories.LogsRepository;


@Service
public class LogsService 
{
	@Autowired
	LogsRepository repository;
	
	public List<LogsEntity> getAllLogs()
	{
		List<LogsEntity> result=(List<LogsEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<LogsEntity>();
		
	}
	
	public void saveLog(LogsEntity entity)
	{
		entity=repository.save(entity);
			
	}
		
	
}
