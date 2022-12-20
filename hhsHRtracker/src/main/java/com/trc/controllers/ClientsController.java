package com.trc.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.ClientATsEntity;
import com.trc.entities.ClientsEntity;
import com.trc.entities.ContractsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.TitlesEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.ClientATsService;
import com.trc.services.ClientsService;
import com.trc.services.ContractsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectTypesService;
import com.trc.services.ProjectsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.TitlesService;
import com.trc.services.TrainingsService;
import com.trc.services.UsersService;



@Controller
@RequestMapping("/clients")
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
	
	@Autowired
	ContractsService serviceContracts;
	
	@Autowired
	ProjectTypesService serviceProjectTypes;
	
	@Autowired
	ClientATsService serviceClientATs;
	
	@Autowired
	TrainingsService serviceTrainings;
	
	@RequestMapping(path="/search", method=RequestMethod.POST)
	public String getSearchForm(Model model,Long quserId,String qperiod,Long qdivisionId,String priznakOp) throws RecordNotFoundException, ParseException
	{
				
				
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
				
		//Retrieving list of employees
		List<ClientsEntity> list=service.getAllActives();
		
		model.addAttribute("quser",quser);		
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
				
		model.addAttribute("employees",list);
		model.addAttribute("priznakOp",priznakOp);
					
		return "clientsSearch";
				
	}
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllClients(Model model,Long quserId,String qperiod,Long qdivisionId,String qproject,String stringSearch) throws RecordNotFoundException, ParseException
	{
		String projectName=null;
		String todayDateString=null;
		String clientIdString=null;
		String titleName=null;
		
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
						
		//Trying to get project name, same time trying to get title name(s)
		for(ClientsEntity client : list)
        {
        	projectName=serviceProjects.getProjectNameByNumber(client.getProject());
        	
        	clientIdString=String.valueOf(client.getClientid());
        	
        	titleName=serviceTitles.getTitleByNumber(client.getTitleNum());
        	
        	//Finding expired certs
        	String expiredCert=service.findingNumExpiredCerts(clientIdString,todayDateString,qperiod);
        	
        	client.setExpiredCert(expiredCert);
        	client.setProjectName(projectName);
        	client.setTitle(titleName);
        }
		
				
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		model.addAttribute("qproject",qproject);
		
		model.addAttribute("clients",list);
		model.addAttribute("project",projectEntity);
		model.addAttribute("stringSearch",stringSearch);
			
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
			String lastName,String firstName,String dateHire,String contract,String titleNum,String status,String active,String employeeNum,String stringSearch) throws RecordNotFoundException
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
							
		String message="Employee was created successfully...";
		
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
		
		client.setEmployeeNum(active);
		
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
		model.addAttribute("stringSearch",stringSearch);
		
		model.addAttribute("qproject",qproject);
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "clientsRedirect";
			
	}
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editClientById(Model model,Optional<Long> id,Long quserId,String qperiod,Long qdivisionId,String stringSearch,String path) throws RecordNotFoundException 
	{
		List<ClientATsEntity> assigTrainings=new ArrayList<>();
		
		String titleName=null;
		String priznakNew="false";
		String projectName=null;
		String contractType=null;
		String contractTypeNum=null;
		String clientIdString=null;
		String trainingName=null;
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
					
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
				
		//Retrieving titles
		List<TitlesEntity> titles=serviceTitles.getAllActives();	
		
		//Retrieving list of contracts
		List<ContractsEntity> contracts=serviceContracts.getAll();
		
				
		
		if(id.isPresent())
		{
			ClientsEntity entity=service.getClientById(id.get());
			
			clientIdString=String.valueOf(entity.getClientid());
						
			//Retrieving home project and assigning the project definition
			projectName=serviceProjects.getProjectNameByNumber(entity.getProject());
			
			//Retrieving type of project
			contractTypeNum=serviceContracts.getType(entity.getContract());
			contractType=serviceProjectTypes.getTypeName(contractTypeNum);
			
			//Getting the title name
			titleName=serviceTitles.getTitleByNumber(entity.getTitleNum());
			
			//Retrieving the list of assigned training
			assigTrainings=serviceClientATs.getATbyClient(clientIdString);
					
			//Getting training names
			for(ClientATsEntity clientAT : assigTrainings)
			{
				trainingName=serviceTrainings.getTrainingName(clientAT.getTrainingNum());
				clientAT.setBufferName(trainingName);
			}
					
			//trying to sort the array of assigned projects
			if(assigTrainings.isEmpty())
			{
				//do nothing
			}
			else
				assigTrainings.sort(Comparator.comparing(ClientATsEntity::getBufferName));
			
			model.addAttribute("client",entity);
			model.addAttribute("titleName",titleName);
			
		}
		else
		{
			model.addAttribute("client",new ClientsEntity());
			
			priznakNew="true";
			path="neu";
		}
		
				
		model.addAttribute("titles",titles);
		model.addAttribute("contracts",contracts);
		
		model.addAttribute("projectName",projectName);
		model.addAttribute("titleName",titleName);
		model.addAttribute("priznakNew",priznakNew);
		
		model.addAttribute("stringSearch",stringSearch);
		model.addAttribute("path",path);
		model.addAttribute("contractType",contractType);
		
		model.addAttribute("assigTrainings",assigTrainings);
				
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "clientsEdit";
	}
	
	@RequestMapping(path="/updateClient", method=RequestMethod.POST)
	public String updateClient(Model model,ClientsEntity client,Long quserId,String qperiod,Long qdivisionId,String qproject,Long projectId,String stringSearch,String path) throws RecordNotFoundException
	{
		String title=null;
				
		//System.out.println("Inside the controller to update or create. Object is: "+ project);
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
							
		String message="Employee was updated successfully...";
		
		//finding title name
		title=serviceTitles.getTitleByNumber(client.getTitle());
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		client.setTitle(title);
		
		//System.out.println(client);
		//System.out.println("The value of id is "+ id);
		//System.out.println("The value of path is "+ path);
						
		//Updating client core information
		service.createOrUpdate(client);
		
		log.setSubject(quser.getEmail());
		log.setAction("Updating employee core information");
		log.setObject(client.getCname());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		model.addAttribute("stringSearch",stringSearch);
		model.addAttribute("path",path);
		
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "clientsRedirect";
			
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteClientById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId,String qproject,String stringSearch,String path) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		String clientName=null;
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
						
		//Retrieving client information
		ClientsEntity entity=service.getClientById(id);
		
		clientName=entity.getCname();
		
		String message="Client was deleted...";
		
		service.deleteClientById(id);
				
		log.setSubject(quser.getEmail());
		log.setAction("Deleting employee from the system");
		log.setObject(clientName);
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		model.addAttribute("stringSearch",stringSearch);
		model.addAttribute("path",path);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "clientsRedirect";
		
		
	}
	
	@RequestMapping(path="/transfer", method=RequestMethod.POST)
	public String transferClient(Model model,Long id,Long quserId,String qperiod,Long qdivisionId,Long projectId,Long qproject,String stringSearch) throws RecordNotFoundException 
	{
		String department=null;
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
					
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		department=qdivision.getDnumber();
		
		//Retrieving list of projects
		List<ProjectsEntity> listProjects=serviceProjects.getAllProjectsByDiv(department);
		
		//Retrieving current project
		ProjectsEntity projectEntity=serviceProjects.getProjectById(projectId);
		
				
		//Creating entity based in selected client ID
		ClientsEntity entity=service.getClientById(id);
										
		model.addAttribute("client",entity);
						
		model.addAttribute("project",projectEntity);
		model.addAttribute("projects",listProjects);
				
		model.addAttribute("clientId",id);
		model.addAttribute("stringSearch",stringSearch);
		
		model.addAttribute("qproject",qproject);
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "clientsTransfer";
	}
	
	@RequestMapping(path="/transferEmployee", method=RequestMethod.POST)
	public String tranferringEmployee(Model model, Long id, Long quserId,String qperiod,Long qdivisionId,String qproject,String projectNumber,String stringSearch) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		String clientName=null;
		String projectName=null;
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
						
		//Retrieving client information
		ClientsEntity entity=service.getClientById(id);
		
		clientName=entity.getCname();
		
		String message="Employee was tranferred successfully...Please notice that although the transferring, the period remains the same.";
		
		//finding project name
		projectName=serviceProjects.getProjectNameByNumber(qproject);
		
		
		service.tranferringEmployee(id,projectNumber,projectName);
				
		log.setSubject(quser.getEmail());
		log.setAction("Tranferring employee from one project to another in the same period.");
		log.setObject(clientName +"From Project "+ qproject +" to Project "+ projectNumber);
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		
		model.addAttribute("qproject",qproject);
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "clientsRedirect";
		
		
	}
	
	@RequestMapping(path="/findByName", method=RequestMethod.POST)
	public String findEmployeeByName(Model model, Long quserId,String qperiod,Long qdivisionId,String stringSearch,String path) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
						
		String titleName=null;
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Trying to find the list of employees
		List<ClientsEntity> list=service.searchByName(stringSearch);
		
		//Trying to get title name(s)
		for(ClientsEntity client : list)
		{
			titleName=serviceTitles.getTitleByNumber(client.getTitleNum());
			client.setTitle(titleName);
		}   
		
		//Generating the log file record
		log.setSubject(quser.getEmail());
		log.setAction("Searching by client first or last name");
		log.setObject("Training Section");
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("quser",quser);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("path",path);
		model.addAttribute("clients",list);
		model.addAttribute("stringSearch",stringSearch);
		
		return "clientsView";
	}
	
	@RequestMapping(path="/findBySelection", method=RequestMethod.POST)
	public String findEmployeeBySelection(Model model, Long quserId,String qperiod,Long qdivisionId,Long stringSearch,String path) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		String titleName=null;
						
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Trying to find the list of employees
		List<ClientsEntity> list=service.searchBySelection(stringSearch);
		
		
		//Trying to get title name(s)
		for(ClientsEntity client : list)
		{
		   	titleName=serviceTitles.getTitleByNumber(client.getTitleNum());
		   	client.setTitle(titleName);
		}   	
				
		//Generating the log file record
				
		log.setSubject(quser.getEmail());
		log.setAction("Searching by employee first or last name");
		log.setObject("Training Section");
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("quser",quser);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("path",path);
		model.addAttribute("clients",list);
		
		model.addAttribute("stringSearch",stringSearch);
				
		return "clientsView";
	}
}
