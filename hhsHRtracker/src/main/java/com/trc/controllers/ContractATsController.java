package com.trc.controllers;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.ContractATsEntity;
import com.trc.entities.ContractsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.TrainingsEntity;
import com.trc.entities.UsersEntity;

import com.trc.services.ContractATsService;
import com.trc.services.ContractsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.TrainingsService;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/contractATs")
public class ContractATsController 
{
	@Autowired
	ContractATsService service;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	TrainingsService serviceTrainings;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	ContractsService serviceContracts;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllTrainings(Model model,Long quserId,String qperiod,Long qdivisionId,Long id) throws RecordNotFoundException
	{
		String trainingName=null;		
				
		//Retrieving contract information
		ContractsEntity contract=serviceContracts.getContractById(id);
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
				
		//Finally retrieving the list of assigned trainings
		List<ContractATsEntity> list=service.getATbyContract(contract.getContract());
		
		//Getting project names
		for(ContractATsEntity contractAT : list)
		{
			trainingName=serviceTrainings.getTrainingName(contractAT.getTrainingNum());
			contractAT.setBufferName(trainingName);
		}
		
		//trying to sort the array of assigned projects
		list.sort(Comparator.comparing(ContractATsEntity::getBufferName));
		
		model.addAttribute("contract",contract);
		model.addAttribute("trainings",list);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
					
		return "contractATsList";
				
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteTrainingById(Model model, Long id, Long contractId,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		//Retrieving assigned project
		ContractATsEntity assignTraining=service.getContractATbyId(id);
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving contract
		ContractsEntity contract=serviceContracts.getContractById(contractId);
						
		String message="Item was removed from the list...";
		
		service.deleteProject(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Removing assigned training from contract: "+ assignTraining.getContract());
		log.setObject("Removed project was: "+assignTraining.getTrainingNum());
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		model.addAttribute("contract",contract);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
				
		return "contractATsRedirect";
		
		
	}
	
	
	
	@RequestMapping(path="/addTraining", method=RequestMethod.POST)
	public String assignTrainings(Model model,ContractATsEntity contractAT,Long quserId,Long id,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		int duplicates=0;
		
		String message=null;
				
		//System.out.println("Inside the controller to update or create. Object is: "+ project);
		LogsEntity log=new LogsEntity();
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving contract
		ContractsEntity contract=serviceContracts.getContractById(id);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Checking if this project is not already assigned
		duplicates=service.findDuplicates(contractAT.getTrainingNum(),contract.getContract());
		
		if(duplicates==0)
		{
			//Finalizing to set up the entity
			contractAT.setContract(contract.getContract());
												
			message="The Training List was updated for this contract...";
			
			service.createContract(contractAT);
		}
		else
			message="This project is already in the list of assigned projects. No changes were made to the list this time.";
		
		
		
		log.setSubject(quser.getEmail());
		log.setAction("Assigning new project to Contract: "+ contract.getContract());
		log.setObject("New Project is: "+ contractAT.getTrainingNum());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		model.addAttribute("contract",contract);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "contractATsRedirect";
			
	}
	
	
	
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editTrainingsById(Model model,Long id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
		String bufferName=null;
		
		//Retrieving contract information
		ContractsEntity contract=serviceContracts.getContractById(id);
		
		//Retrieving quser information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Retrieving list of training
		List<TrainingsEntity> list=serviceTrainings.getActives();
		
		//Retrieving list of assigned training
		List<ContractATsEntity> assigTrainings=service.getATbyContract(contract.getContract());
		
		//filling up the names of the training
		for(ContractATsEntity localTraining : assigTrainings)
		{
			bufferName=serviceTrainings.getTrainingName(localTraining.getTrainingNum());
			localTraining.setBufferName(bufferName);
		}
		
		//trying to sort the array of assigned training
		assigTrainings.sort(Comparator.comparing(ContractATsEntity::getBufferName));
						
		model.addAttribute("contractAT",new ContractATsEntity());
				
		model.addAttribute("trainings",list);
		model.addAttribute("contract",contract);
		model.addAttribute("assigTrainings",assigTrainings);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "contractATsAdd";
	}
}
