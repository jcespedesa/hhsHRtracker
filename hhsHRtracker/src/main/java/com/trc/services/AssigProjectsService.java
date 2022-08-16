package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.AssigProjectsEntity;
import com.trc.repositories.AssigProjectsRepository;


@Service
public class AssigProjectsService 
{
	@Autowired
	AssigProjectsRepository repository;
	
	public List<AssigProjectsEntity> getAllProjectsByDivId(String department,String userId)
	{
		List<AssigProjectsEntity> result=(List<AssigProjectsEntity>) repository.getAllProjectsByDivId(department,userId);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<AssigProjectsEntity>();
		
	}
	
	public void deleteProject(Long id) throws RecordNotFoundException
	{
		Optional<AssigProjectsEntity> project=repository.findById(id);
		
		if(project.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Assigned Project record exist for given id");
		
		
	}
	
	public AssigProjectsEntity createProject(AssigProjectsEntity entity)
	{
		
		entity=repository.save(entity);
			
		return entity;
		
	}
	
	public AssigProjectsEntity getProjectById(Long id) throws RecordNotFoundException
	{
		Optional<AssigProjectsEntity> project=repository.findById(id);
		
		if(project.isPresent())
			return project.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
}
