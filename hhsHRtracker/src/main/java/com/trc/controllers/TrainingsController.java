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
import com.trc.entities.TrainingsEntity;
import com.trc.entities.UsersEntity;

import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectTypesService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.TrainingsService;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/trainings")
public class TrainingsController 
{
	
	@Autowired
	TrainingsService service;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	ProjectTypesService serviceTypes;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAll(Model model,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		String projectTypeDef=null;
				
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
						
		//Retrieving list of trainings
		List<TrainingsEntity> list=service.getAllTrainings();
		
		//Updating the type of project definitions
		for(TrainingsEntity training : list)
		{
			projectTypeDef=serviceTypes.getTypeName(training.getProjectType());
			
			training.setProjectTypeDef(projectTypeDef);
		}
				
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("trainings",list);
			
		return "trainingsList";
			
	}
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editTrainingById(Model model,Optional<Long>id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
		
		String priznakNew="false";
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving already existent training list
		List<TrainingsEntity> listTrainings=service.getAllTrainings();
		
		//Retrieving list of type of projects
		List<ProjectTypesEntity> types=serviceTypes.getActives();
		
		if(id.isPresent())
		{
			TrainingsEntity entity=service.getTrainingById(id.get());
			model.addAttribute("training",entity);
			
			
		}
		else
		{
			model.addAttribute("training",new TrainingsEntity());
			
			priznakNew="true";
			
		}
						
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("trainings",listTrainings);
		model.addAttribute("types",types);
		model.addAttribute("priznakNew",priznakNew);
		
		return "trainingsAddEdit";
	}
	
	@RequestMapping(path="/createTraining", method=RequestMethod.POST)
	public String createOrUpdateTraining(Model model,TrainingsEntity training,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		int priznakDuplicate=0;
		
		String localTraining=null;
		String message=null;
		
		//System.out.println("Inside the controller to update or create. Object is: "+ training);
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//System.out.println("The value of priznak duplicate for "+ localTraining +" is "+ priznakDuplicate);
		
		if(training.getTrainingid()==null)
		{
			//Checking if this training is not already in the system
			localTraining=training.getTnumber();
			priznakDuplicate=service.findDuplicates(localTraining);
			
		}
		
		if(priznakDuplicate==0)
		{
			service.createOrUpdate(training);
									
			message="Training information was updated successfully...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Creating/Modiying Training information");
			log.setObject(training.getTname());
		
		}
		else
		{
			message="Error: Duplicated Training found, new record was not created at this time. Please review the list of trainings again...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Failing to create a new training due to duplicity");
			log.setObject(training.getTname());
			
		}
		
		serviceLogs.saveLog(log);
							
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "trainingsRedirect";
			
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteTrainingById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving training definition
		TrainingsEntity entity=service.getTrainingById(id);
		
		String message="Item was deleted...";
		
		service.deleteTrainingById(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Deleting training definition");
		log.setObject(entity.getTname());
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "trainingsRedirect";
			
	}
	
}
