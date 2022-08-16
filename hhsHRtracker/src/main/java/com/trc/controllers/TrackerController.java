package com.trc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.ClientsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.ClientsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("hhsHRtracker/tracker")
public class TrackerController 
{
	@Autowired
	ClientsService service;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	ProjectsService serviceProjects;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	ClientsService serviceClients;
	
	@RequestMapping(path="/view", method=RequestMethod.POST)
	public String trackerView(Model model,Long quserId,String qperiod,Long qdivisionId,String qproject,Long id,Long projectId) throws RecordNotFoundException
	{
				
		//Retrieving project information
		ProjectsEntity projectEntity=serviceProjects.getProjectByNumber(qproject);
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Creating entity based in selected client ID
		ClientsEntity entity=service.getClientById(id);
										
				
		model.addAttribute("client",entity);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		model.addAttribute("qproject",qproject);
		
		model.addAttribute("project",projectEntity);
			
		return "trackerView";
				
	}
	
	@RequestMapping(path="/update", method=RequestMethod.POST)
	public String updateClient(Model model,ClientsEntity client,Long quserId,String qperiod,Long qdivisionId,String qproject,Long projectId) throws RecordNotFoundException
	{
				
		//System.out.println("Inside the controller to update or create. Object is: "+ project);
		LogsEntity log=new LogsEntity();
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving client information
		ClientsEntity localClient=serviceClients.getClientById(client.getClientid());
							
		String message="Client tracking information was updated successfully...";
		
				
		//System.out.println(client);
		//System.out.println("The value of id is "+ id);
						
		//Updating client tracking information
		service.saveTracker(client);
		
		log.setSubject(quser.getEmail());
		log.setAction("Updating client tracking information for: "+ localClient.getCname());
		log.setObject("Client ID is: "+ client.getClientid());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		
		model.addAttribute("qproject",qproject);
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "clientsRedirect";
			
	}
	
}
