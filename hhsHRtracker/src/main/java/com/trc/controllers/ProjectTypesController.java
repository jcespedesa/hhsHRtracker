package com.trc.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.ProjectTypesEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectTypesService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/projectTypes")
public class ProjectTypesController 
{
	@Autowired
	ProjectTypesService service;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	LogsService serviceLogs;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAll(Model model,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
				
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
						
		//Retrieving list of types
		List<ProjectTypesEntity> list=service.getAllTypes();
				
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("types",list);
			
		return "projectTypesList";
			
	}
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editTrainingById(Model model,Optional<Long>id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
		
		String priznakNew="false";
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving list of already existent certificate numbers
		List<ProjectTypesEntity> listTypes=service.getAllTypes();
		
		if(id.isPresent())
		{
			ProjectTypesEntity entity=service.getTypeById(id.get());
			model.addAttribute("type",entity);
			
			
		}
		else
		{
			model.addAttribute("type",new ProjectTypesEntity());
			
			priznakNew="true";
			
		}
						
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("types",listTypes);
		model.addAttribute("priznakNew",priznakNew);
		
		return "projectTypesAddEdit";
	}
	
	@RequestMapping(path="/createType", method=RequestMethod.POST)
	public String createOrUpdateType(Model model,ProjectTypesEntity type,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		int priznakDuplicate=0;
		
		String localType=null;
		String message=null;
		
		//System.out.println("Inside the controller to update or create. Object is: "+ training);
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//System.out.println("The value of priznak duplicate for "+ localTraining +" is "+ priznakDuplicate);
		
		if(type.getTypeid()==null)
		{
			//Checking if this type is not already in the system
			localType=type.getTypeNumber();
			priznakDuplicate=service.findDuplicates(localType);
			
		}
		
		if(priznakDuplicate==0)
		{
			service.createOrUpdate(type);
									
			message="Project type information was updated successfully...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Creating/Modiying Training information");
			log.setObject(type.getTypeNumber());
		
		}
		else
		{
			message="Error: Duplicated Type found, new record was not created at this time. Please review the list of types again...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Failing to create a new project type due to duplicity");
			log.setObject(type.getType());
			
		}
		
		serviceLogs.saveLog(log);
							
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "projectTypesRedirect";
			
	}
}
