package com.trc.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lowagie.text.DocumentException;
import com.trc.entities.ClientATsEntity;
import com.trc.entities.ClientsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.TrainingsEntity;
import com.trc.entities.UsersEntity;

import com.trc.services.ClientATsService;
import com.trc.services.ClientsService;
import com.trc.services.ContractsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectTypesService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.TrainingsService;
import com.trc.services.TranscriptPDFexporter;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/clientATs")
public class ClientATsController 
{
	@Autowired
	ClientATsService service;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	TrainingsService serviceTrainings;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	ClientsService serviceClients;
	
	@Autowired
	ContractsService serviceContracts;
	
	@Autowired
	ProjectTypesService serviceProjectTypes;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllTrainings(Model model,Long quserId,String qperiod,Long qdivisionId,Long id) throws RecordNotFoundException
	{
		String trainingName=null;
		String clientId=null;
		String contractType=null;
		String contractTypeNum=null;
		
		//Converting id long to string
		clientId=String.valueOf(id);  
				
		//Retrieving client information
		ClientsEntity client=serviceClients.getClientById(id);
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
						
		//Finally retrieving the list of assigned training
		List<ClientATsEntity> list=service.getATbyClient(clientId);
		
		//Getting training names
		for(ClientATsEntity clientAT : list)
		{
			trainingName=serviceTrainings.getTrainingName(clientAT.getTrainingNum());
			clientAT.setBufferName(trainingName);
		}
		
		//trying to sort the array of assigned projects
		list.sort(Comparator.comparing(ClientATsEntity::getBufferName));
		
		//Retrieving contract, type of contract
		
		contractTypeNum=serviceContracts.getType(client.getContract());
		contractType=serviceProjectTypes.getTypeName(contractTypeNum);
		
		model.addAttribute("client",client);
		model.addAttribute("trainings",list);
		model.addAttribute("contractType",contractType);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
					
		return "clientATsList";
				
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteTrainingById(Model model, Long id, Long clientId,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		//Retrieving assigned training
		ClientATsEntity assignTraining=service.getClientATbyId(id);
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving client
		ClientsEntity client=serviceClients.getClientById(clientId);
						
		String message="Item was removed from the list...";
		
		service.deleteTraining(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Removing assigned training from employee: "+ client.getCname());
		log.setObject("Removed training was: "+ assignTraining.getTrainingNum());
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		model.addAttribute("client",client);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
				
		return "clientATsRedirect";
		
		
	}
	
	
	
	@RequestMapping(path="/addTraining", method=RequestMethod.POST)
	public String assignTrainings(Model model,ClientATsEntity clientAT,Long quserId,Long id,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		int duplicates=0;
		
		String message=null;
		String clientId=null;
		
		//Converting id long to string
		clientId=String.valueOf(id);  
				
		//System.out.println("Inside the controller to update or create. Object is: "+ clientAT);
		LogsEntity log=new LogsEntity();
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving client
		ClientsEntity client=serviceClients.getClientById(id);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Checking if this project is not already assigned
		duplicates=service.findDuplicates(clientAT.getTrainingNum(),clientId);
		
		if(duplicates==0)
		{
			//Finalizing to set up the entity
			clientAT.setContract(client.getContract());
			clientAT.setClientId(clientId);
												
			message="The Training List was updated for this employee...";
			
			service.createOrUpdate(clientAT);
		}
		else
			message="This training is already in the list of assigned training. No changes were made to the list this time.";
		
		
		
		log.setSubject(quser.getEmail());
		log.setAction("Assigning new training to Employees: "+ client.getCname());
		log.setObject("New Training is: "+ clientAT.getTrainingNum());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		model.addAttribute("client",client);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "clientATsRedirect";
			
	}
	
	
	
	
	@RequestMapping(path="/add", method=RequestMethod.POST)
	public String addingTraining(Model model,Long id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
		String bufferName=null;
		String clientId=null;
		
		//Converting id long to string
		clientId=String.valueOf(id);  
		
		//Retrieving client information
		ClientsEntity client=serviceClients.getClientById(id);
		
		//Retrieving quser information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Retrieving list of training
		List<TrainingsEntity> list=serviceTrainings.getActives();
		
		//Retrieving list of assigned training
		List<ClientATsEntity> assigTrainings=service.getATbyClient(clientId);
		
		//filling up the names of the training
		for(ClientATsEntity localTraining : assigTrainings)
		{
			bufferName=serviceTrainings.getTrainingName(localTraining.getTrainingNum());
			localTraining.setBufferName(bufferName);
		}
		
		//trying to sort the array of assigned training
		assigTrainings.sort(Comparator.comparing(ClientATsEntity::getBufferName));
						
		model.addAttribute("clientAT",new ClientATsEntity());
				
		model.addAttribute("trainings",list);
		model.addAttribute("client",client);
		model.addAttribute("assigTrainings",assigTrainings);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "clientATsAdd";
	}
	
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editTrainingsById(Model model,Long id,Long quserId,String qperiod,Long qdivisionId,Long clientId) throws RecordNotFoundException 
	{
		String bufferName=null;
		
		//Retrieving client information
		ClientsEntity client=serviceClients.getClientById(clientId);
		
		//Retrieving quser information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Retrieving training information
		ClientATsEntity entity=service.getClientATbyId(id);
		
		//Retrieving training name
		bufferName=serviceTrainings.getTrainingName(entity.getTrainingNum());
		
		entity.setBufferName(bufferName);								
		
		
		model.addAttribute("client",client);
		model.addAttribute("clientAT",entity);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "clientATsEdit";
	}
	
	
	@RequestMapping(path="/updateTraining", method=RequestMethod.POST)
	public String saveTrainings(Model model,ClientATsEntity clientAT,Long quserId,Long id,String qperiod,Long qdivisionId,Long clientId) throws RecordNotFoundException
	{
				
		String message=null;
				
						
		//System.out.println("Inside the controller to update training. Object is: "+ clientAT);
		LogsEntity log=new LogsEntity();
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving client
		ClientsEntity client=serviceClients.getClientById(clientId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
														
		message="The Training info was updated for this employee...";
			
				
		//Updating training info
		service.createOrUpdate(clientAT);
		
		log.setSubject(quser.getEmail());
		log.setAction("Assigning new training to Employees: "+ client.getCname());
		log.setObject("New Training is: "+ clientAT.getTrainingNum());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		model.addAttribute("client",client);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "clientATsRedirect";
			
	}
	
	@RequestMapping(path="/createTranscript", method=RequestMethod.POST)
	public String generateTranscript(Model model,HttpServletResponse response, Long id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException, DocumentException, IOException
	{
		LogsEntity log=new LogsEntity();
		
		String clientId=null;
		String bufferName=null;
		String tframe=null;
		
		//Converting id long to string
		clientId=String.valueOf(id);  
				
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving client
		ClientsEntity client=serviceClients.getClientById(id);
		
		//Retrieving list of assigned training
		List<ClientATsEntity> assigTrainings=service.getATbyClient(clientId);
				
		//filling up the names of the training
		for(ClientATsEntity localTraining : assigTrainings)
		{
			bufferName=serviceTrainings.getTrainingName(localTraining.getTrainingNum());
			localTraining.setBufferName(bufferName);
			
			tframe=serviceTrainings.getTimeFrame(localTraining.getTrainingNum());
			localTraining.setPriznakAdd(tframe);
		}
				
		//trying to sort the array of assigned training
		assigTrainings.sort(Comparator.comparing(ClientATsEntity::getBufferName));
						
		String message="Data is being processed...";
		
		//Start processing the export to pdf file
		response.setContentType("application/pdf");
        DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime=dateFormatter.format(new Date());
         
        String headerKey="Content-Disposition";
        String headerValue="attachment; filename=transcript_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
		
		//Transmitting info to the export service
        TranscriptPDFexporter exporter=new TranscriptPDFexporter(assigTrainings,client);
        exporter.export(response);
		
		log.setSubject(quser.getEmail());
		log.setAction("Generating transcrip for employee: "+ client.getCname());
		log.setObject("Type of contract is: ");
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		model.addAttribute("client",client);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
				
		return "clientATsRedirect";
		
	}
}
