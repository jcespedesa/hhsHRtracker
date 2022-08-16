package com.trc.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.ClientsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.TitlesEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.ClientsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.TitlesService;
import com.trc.services.UsersService;



@Controller
@RequestMapping("hhsHRtracker/clients")
public class ClientsController 
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
	TitlesService serviceTitles;
	
	@Autowired
	LogsService serviceLogs;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllClients(Model model,Long quserId,String qperiod,Long qdivisionId,String qproject) throws RecordNotFoundException, ParseException
	{
		String projectName=null;
		String todayDateString=null;
		String clientIdString=null;
		
		//Retrieving project information
		ProjectsEntity projectEntity=serviceProjects.getProjectByNumber(qproject);
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Trying to get todayDate
		//java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
		
		//System.out.println("Today Date is "+ todayDate);
		
		//Retrieving list of clients
		List<ClientsEntity> list=service.getAllByProject(qproject);
		
		//Trying to get todayDate
		java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
				
		//System.out.println("Today Date is "+ todayDate);
				
		//Converting date to string
		DateFormat todayDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		todayDateString=todayDateFormat.format(todayDate); 
						
		//Trying to get project name
		for(ClientsEntity client : list)
        {
        	projectName=serviceProjects.getProjectNameByNumber(client.getProject());
        	
        	clientIdString=String.valueOf(client.getClientid());
        	
        	//Finding expired certs
        	String expiredCert=service.findingNumExpiredCerts(clientIdString,todayDateString);
        	
        	client.setExpiredCert(expiredCert);
        	client.setProjectName(projectName);
        }
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		model.addAttribute("qproject",qproject);
		
		model.addAttribute("clients",list);
		model.addAttribute("project",projectEntity);
			
		return "clientsList";
				
	}
	
	@RequestMapping(path="/add", method=RequestMethod.POST)
	public String editNewClient(Model model,Optional<Long>id,Long quserId,String qperiod,Long qdivisionId,Long projectId,Long qproject) throws RecordNotFoundException 
	{
				
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
					
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Retrieving project
		ProjectsEntity projectEntity=serviceProjects.getProjectById(projectId);
		
		//Retrieving titles
		List<TitlesEntity> titles=serviceTitles.getAllActives();	
				
		if(id.isPresent())
		{
			ClientsEntity entity=service.getClientById(id.get());
			
			
			model.addAttribute("client",entity);
			
						
		}
		else
		{
			model.addAttribute("client",new ClientsEntity());
			
		}
		
		model.addAttribute("project",projectEntity);
		model.addAttribute("titles",titles);
		
		model.addAttribute("qproject",qproject);
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "clientsNew";
	}
	
	@RequestMapping(path="/createClient", method=RequestMethod.POST)
	public String createClient(Model model,Long quserId,String qperiod,Long qdivisionId,String qproject,Long projectId,
			String lastName,String firstName,String dateHire,String contract,String titleNum,String status,String active) throws RecordNotFoundException
	{
		String cname=null;
		String repitaya=",";
		String pustoy=" ";
		String title=null;
				
		//System.out.println("Inside the controller to update or create. Object is: "+ project);
		LogsEntity log=new LogsEntity();
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving project
		ProjectsEntity projectEntity=serviceProjects.getProjectById(projectId);
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
							
		String message="Client was created successfully...";
		
		//Creating new client entity
		ClientsEntity client=new ClientsEntity();
		
		//concatenate cname
		cname=lastName+repitaya+pustoy+firstName;
		
		//finding title name
		title=serviceTitles.getTitleByNumber(titleNum);
		
		client.setCname(cname);
		client.setDateHire(dateHire);
		client.setRealDateHire(dateHire);
		client.setContract(contract);
		client.setTitle(title);
		client.setStatus(status);
		client.setActive(active);
		
		client.setProgram(qdivision.getDnumber());
		client.setProject(projectEntity.getProject());
		client.setProjectName(projectEntity.getProjectName());
		client.setPeriod(qperiod);
		
		//Creating new record client
		service.create(client);
		
		log.setSubject(quser.getEmail());
		log.setAction("Creating new client");
		log.setObject(client.getCname());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		
		model.addAttribute("qproject",qproject);
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "clientsRedirect";
			
	}
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editClientById(Model model,Long id,Long quserId,String qperiod,Long qdivisionId,Long projectId,Long qproject) throws RecordNotFoundException 
	{
		
		String titleNumber=null;
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
					
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Retrieving project
		ProjectsEntity projectEntity=serviceProjects.getProjectById(projectId);
		
		//Retrieving titles
		List<TitlesEntity> titles=serviceTitles.getAllActives();	
		
		//Creating entity based in selected client ID
		ClientsEntity entity=service.getClientById(id);
		
		titleNumber=serviceTitles.getTitleByNumber(titleNumber);
							
		model.addAttribute("client",entity);
						
		model.addAttribute("project",projectEntity);
		model.addAttribute("titles",titles);
		model.addAttribute("titleNumber",titleNumber);
		
		model.addAttribute("clientId",id);
		
		model.addAttribute("qproject",qproject);
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "clientsEdit";
	}
	
	@RequestMapping(path="/updateClient", method=RequestMethod.POST)
	public String updateClient(Model model,ClientsEntity client,Long quserId,String qperiod,Long qdivisionId,String qproject,Long projectId) throws RecordNotFoundException
	{
		String title=null;
				
		//System.out.println("Inside the controller to update or create. Object is: "+ project);
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
							
		String message="Client was updated successfully...";
		
		//finding title name
		title=serviceTitles.getTitleByNumber(client.getTitle());
		
		client.setTitle(title);
		
		//System.out.println(client);
		//System.out.println("The value of id is "+ id);
						
		//Updating client core information
		service.save(client);
		
		log.setSubject(quser.getEmail());
		log.setAction("Updating client core information");
		log.setObject(client.getCname());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		
		model.addAttribute("qproject",qproject);
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "clientsRedirect";
			
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteClientById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId,String qproject) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		String clientName=null;
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
						
		//Retrieving client information
		ClientsEntity entity=service.getClientById(id);
		
		clientName=entity.getCname();
		
		String message="Client was deleted...";
		
		service.deleteClientById(id);
				
		log.setSubject(quser.getEmail());
		log.setAction("Deleting client from the system");
		log.setObject(clientName);
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		
		model.addAttribute("qproject",qproject);
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "clientsRedirect";
		
		
	}
	
}
