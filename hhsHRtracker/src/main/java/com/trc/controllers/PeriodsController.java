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
import com.trc.entities.PeriodsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.PeriodsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/hhsHRtracker/periods")
public class PeriodsController 
{
	@Autowired
	PeriodsService service;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	LogsService serviceLogs;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllPeriods(Model model,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
				
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
						
		//Retrieving list of periods
		List<PeriodsEntity> list=service.getAllPeriods();
				
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("periods",list);
			
		return "periodsList";
			
	}
	
	@RequestMapping(path="/add", method=RequestMethod.POST)
	public String editPeriodById(Model model,Optional<Long>id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
		List<Integer> listUB=service.findNonRepeatedUB();
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
				
		model.addAttribute("period",new PeriodsEntity());
		model.addAttribute("udelnyBeses",listUB);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "periodsAddEdit";
	}
	
	@RequestMapping(path="/createPeriod", method=RequestMethod.POST)
	public String createOrUpdatePeriod(Model model,PeriodsEntity period,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		int priznakDuplicate=0;
		
		String localPeriod=null;
		String message=null;
		
		//System.out.println("Inside the controller to update or create. Object is: "+ project);
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//System.out.println("The value of priznak duplicate for "+ localPeriod +" is "+ priznakDuplicate);
		
		//Checking if this period is not already in the system
		localPeriod=period.getPeriod();
		priznakDuplicate=service.findDuplicates(localPeriod);
		
		if(priznakDuplicate==0)
		{
			service.createPeriod(period);
									
			message="Period was created successfully...";
			
			
			log.setSubject(quser.getEmail());
			log.setAction("Creating/Modiying project");
			log.setObject(period.getPeriod());
		
		}
		else
		{
			message="Error: Period duplicate found, new period was not created at this time. Please review the list of periods again...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Failing to create a new period due to duplicity");
			log.setObject(period.getPeriod());
			
		}
		
		serviceLogs.saveLog(log);
							
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "periodsRedirect";
			
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteProjectById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving project
		PeriodsEntity entity=service.getPeriodById(id);
		
		String message="Item was deleted...";
		
		service.deletePeriodById(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Deleting project");
		log.setObject(entity.getPeriod());
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "periodsRedirect";
		
		
	}
}
