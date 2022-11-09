package com.trc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.ProjectsEntity;
import com.trc.repositories.DivisionsRepository;
import com.trc.repositories.ProjectsRepository;
import com.trc.repositories.SitesRepository;

@Service
public class ProjectsService 
{
	@Autowired
	ProjectsRepository repository;
	
	@Autowired
	SitesRepository repositorySites;
	
	@Autowired
	DivisionsRepository repositoryDivisions;
	
		
	public List<ProjectsEntity> getAllProjects()
	{
		List<ProjectsEntity> result=(List<ProjectsEntity>) repository.getAllByProject();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ProjectsEntity>();
		
	}
	
	public List<ProjectsEntity> getAllHHSprojects()
	{
		List<ProjectsEntity> result=(List<ProjectsEntity>) repository.getAllHHSprojects();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ProjectsEntity>();
		
	}
	
	public List<ProjectsEntity> getAllHHSbyUB()
	{
		List<ProjectsEntity> result=(List<ProjectsEntity>) repository.getAllHHSbyUB();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ProjectsEntity>();
		
	}
	
	public List<ProjectsEntity> getAllProjectsByDiv(String department)
	{
		List<ProjectsEntity> result=(List<ProjectsEntity>) repository.getAllProjectsByDiv(department);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ProjectsEntity>();
		
	}
	
	public List<ProjectsEntity> getAllByProject()
	{
		List<ProjectsEntity> result=(List<ProjectsEntity>) repository.getAllByProject();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ProjectsEntity>();
		
	}
	
	public List<ProjectsEntity> getHhsFormView()
	{
		String department="300";
		
		List<ProjectsEntity> result=(List<ProjectsEntity>) repository.getHhsFormView(department);
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<ProjectsEntity>();
		
	}
	
	
	public ProjectsEntity getProjectById(Long id) throws RecordNotFoundException
	{
		Optional<ProjectsEntity> project=repository.findById(id);
		
		if(project.isPresent())
			return project.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	
	
	public ProjectsEntity createOrUpdate(ProjectsEntity entity)
	{
		if(entity.getProjectid()==null)
		{
			entity=repository.save(entity);
				
						
			return entity;
		}
		else
		{
			Optional<ProjectsEntity> project=repository.findById(entity.getProjectid());
			
			if(project.isPresent())
			{
				
				ProjectsEntity newEntity=project.get();
				
				newEntity.setProject(entity.getProject());
				newEntity.setProjectName(entity.getProjectName());
				newEntity.setDepartment(entity.getDepartment());
				
				newEntity.setActive(entity.getActive());
				
				newEntity.setSiteNumber1(entity.getSiteNumber1());
				newEntity.setSiteNumber2(entity.getSiteNumber2());
				
				newEntity.setHhsDivision(entity.getHhsDivision());
				newEntity.setUdelnyBes(entity.getUdelnyBes());
				
				newEntity.setType(entity.getType());
				newEntity.setContract(entity.getContract());
				
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
	
	public void deleteProjectById(Long id) throws RecordNotFoundException
	{
		Optional<ProjectsEntity> project=repository.findById(id);
		
		if(project.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No Project record exist for given id");
		
		
	}
	
	public String getSiteBySiteNumber1(String siteNumber)
	{
		String site=repositorySites.getLocationName(siteNumber);
		
		return site;
		
	}
	
	public String getSiteBySiteNumber2(String siteNumber)
	{
		String site=repositorySites.getLocationName(siteNumber);
		
		return site;
		
	}
	
	
	public String getDivisionByDivisionNumber(String dnumber)
	{
		String division=repositoryDivisions.getDivisionName(dnumber);
		
		return division;
		
	}
	
	public List<ProjectsEntity> searchProjectsByNum(String stringSearch)
	{
		List<ProjectsEntity> result=(List<ProjectsEntity>) repository.getProjectsByNum(stringSearch);
		
		return result;
		
	}
	
	public List<ProjectsEntity> searchProjectsByName(String stringSearch)
	{
		List<ProjectsEntity> result=(List<ProjectsEntity>) repository.getProjectsByName(stringSearch);
		
		return result;
		
	}
	
	public String getProjectByNum(String stringSearch)
	{
		String programName=(String) repository.getProjectName(stringSearch);
		
		return programName;
		
	}
	
	public List<Integer> findNonRepeatedUB(String department)
	{
		int start=1;
		int end=100;
		
				
		//Retrieving list of existent udelny beses
		List<Integer> originalList=repository.getOriginalListUB(department);
		
		//Generating all possible udelny beses to 100
		List<Integer> listUB=IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
		
		//Obtaining the list of UB with no similar in the table
		listUB.removeAll(originalList);
		
		return listUB;
		
	}
	
	public List<ProjectsEntity> getProjectsByDiv(String department)
	{
		List<ProjectsEntity> result=(List<ProjectsEntity>) repository.getAllProjByDiv(department);
		
		return result;
		
	}
	
	public String getProjectNameByNumber(String project)
	{
		String programName=(String) repository.getProjectName(project);
		
		return programName;
		
	}
	
	public ProjectsEntity getProjectByNumber(String project)
	{
		ProjectsEntity entity=(ProjectsEntity) repository.getProjectbyNumber(project);
		
		return entity;
		
	}
	
	public int findDuplicates(String project)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findProjectDuplicity(project);
		
		
		return priznakDuplicate;
	}
		
	
}
