package com.trc.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.TitleATsEntity;
import com.trc.entities.TitlesEntity;
import com.trc.entities.UsersEntity;

import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectTypesService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.TitleATsService;
import com.trc.services.TitlesService;
import com.trc.services.TrainingsService;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/titles")
public class TitlesController 
{
	@Autowired
	TitlesService service;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	ProjectTypesService serviceTypes;
	
	@Autowired
	TitleATsService serviceTitleATs;
	
	@Autowired
	TrainingsService serviceTrainings;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAll(Model model,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
						
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
						
		//Retrieving list of titles
		List<TitlesEntity> list=service.getAllByName();
		
						
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("titles",list);
			
		return "titlesList";
			
	}
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editTitleById(Model model,Optional<Long>id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
		List<Integer> listNumbers=IntStream.range(1,100).boxed().collect(Collectors.toList());
		List<Integer> listOccupied=service.getOccupiedNumbers();
		List<TitleATsEntity> assigTrainings=new ArrayList<TitleATsEntity>();
		
		String priznakNew="false";
		String titleName=null;
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving already existent titles list
		List<TitlesEntity> listTitles=service.getAllTitles();
		
			
						
		if(id.isPresent())
		{
			TitlesEntity entity=service.getTitleById(id.get());
						
			//Retrieving the list of assigned training
			assigTrainings=serviceTitleATs.getATbyTitle(entity.getTitleNum());
			
			//Getting training names
			for(TitleATsEntity titleAT : assigTrainings)
			{
				titleName=serviceTrainings.getTrainingName(titleAT.getTrainingNum());
				titleAT.setBufferName(titleName);
			}
			
			//trying to sort the array of assigned projects
			assigTrainings.sort(Comparator.comparing(TitleATsEntity::getBufferName));
			
			model.addAttribute("title",entity);
						
		}
		else
		{
			model.addAttribute("title",new TitlesEntity());
			
			priznakNew="true";
			
		}
		
		//Filtering already existing title numbers
		listNumbers.removeAll(listOccupied);
						
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("titles",listTitles);
		model.addAttribute("priznakNew",priznakNew);
		model.addAttribute("numbers",listNumbers);
		
		model.addAttribute("assigTrainings",assigTrainings);
		
		return "titlesAddEdit";
	}
	
	@RequestMapping(path="/createTitle", method=RequestMethod.POST)
	public String createOrUpdateTitle(Model model,TitlesEntity title,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		int priznakDuplicate=0;
		
		String localTitleNum=null;
		String message=null;
		
		//System.out.println("Inside the controller to update or create. Object is: "+ training);
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//System.out.println("The value of priznak duplicate for "+ localTraining +" is "+ priznakDuplicate);
		
		if(title.getTitleid()==null)
		{
			//Checking if this training is not already in the system
			localTitleNum=title.getTitleNum();
			priznakDuplicate=service.findDuplicates(localTitleNum);
			
		}
		
		if(priznakDuplicate==0)
		{
			service.createOrUpdate(title);
									
			message="Title information was updated successfully...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Creating/Modiying Title information");
			log.setObject(title.getTitleDesc());
		
		}
		else
		{
			message="Error: Duplicated Title number found, new record was not created at this time. Please review the list of titles again...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Failing to create a new title number due to duplicity");
			log.setObject(title.getTitleDesc());
			
		}
		
		serviceLogs.saveLog(log);
							
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "titlesRedirect";
			
	}
	
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteTitleById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving title
		TitlesEntity entity=service.getTitleById(id);
		
		String message="Item was deleted...";
		
		service.deleteTitleById(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Deleting title definition");
		log.setObject(entity.getTitleDesc());
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "titlesRedirect";
			
	}
}
