package com.trc.controllers;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.ContractAPsEntity;
import com.trc.entities.ContractsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.UsersEntity;

import com.trc.services.ContractAPsService;
import com.trc.services.ContractsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/contractAPs")
public class ContractAPsController 
{
	@Autowired
	ContractAPsService service;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	ProjectsService serviceProjects;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	ContractsService serviceContracts;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllProjects(Model model,Long quserId,String qperiod,Long qdivisionId,Long id) throws RecordNotFoundException
	{
		String projectName=null;		
				
		//Retrieving contract information
		ContractsEntity contract=serviceContracts.getContractById(id);
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
				
		//Finally retrieving the list of assigned projects
		List<ContractAPsEntity> list=service.getAPbyContract(contract.getContract());
		
		//Getting project names
		for(ContractAPsEntity contractAP : list)
		{
			projectName=serviceProjects.getProjectNameByNumber(contractAP.getProjectNum());
			contractAP.setBufferName(projectName);
		}
		
		//trying to sort the array of assigned projects
		list.sort(Comparator.comparing(ContractAPsEntity::getBufferName));
		
		model.addAttribute("contract",contract);
		model.addAttribute("projects",list);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
					
		return "contractAPsList";
				
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteProjectById(Model model, Long id, Long contractId,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		//Retrieving assigned project
		ContractAPsEntity assignProject=service.getContractAPById(id);
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving contract
		ContractsEntity contract=serviceContracts.getContractById(contractId);
						
		String message="Item was removed from the list...";
		
		service.deleteProject(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Removing assigned project from contract: "+ assignProject.getContract());
		log.setObject("Removed project was: "+assignProject.getProjectNum());
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		model.addAttribute("contract",contract);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
				
		return "contractAPsRedirect";
		
		
	}
	
	
	
	@RequestMapping(path="/addProject", method=RequestMethod.POST)
	public String assignProjects(Model model,ContractAPsEntity contractAP,Long quserId,Long id,String qperiod,Long qdivisionId) throws RecordNotFoundException
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
		duplicates=service.findDuplicates(contractAP.getProjectNum(),contract.getContract());
		
		if(duplicates==0)
		{
			//Finalizing to set up the entity
			contractAP.setContract(contract.getContract());
												
			message="The Project List was updated for this contract...";
			
			service.createContract(contractAP);
		}
		else
			message="This project is already in the list of assigned projects. No changes were made to the list this time.";
		
		
		
		log.setSubject(quser.getEmail());
		log.setAction("Assigning new project to Contract: "+ contract.getContract());
		log.setObject("New Project is: "+ contractAP.getProjectNum());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		model.addAttribute("contract",contract);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "contractAPsRedirect";
			
	}
	
	
	
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editProjectsById(Model model,Long id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
		String bufferName=null;
		
		//Retrieving contract information
		ContractsEntity contract=serviceContracts.getContractById(id);
		
		//Retrieving quser information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Retrieving list of projects
		List<ProjectsEntity> list=serviceProjects.getProjectsByDiv(qdivision.getDnumber());
		
		//Retrieving list of assigned projects
		List<ContractAPsEntity> assigProjects=service.getAPbyContract(contract.getContract());
		
		//filling up the names of the projects
		for(ContractAPsEntity localContract : assigProjects)
		{
			bufferName=serviceProjects.getProjectByNum(localContract.getProjectNum());
			localContract.setBufferName(bufferName);
		}
		
		//trying to sort the array of assigned projects
		assigProjects.sort(Comparator.comparing(ContractAPsEntity::getBufferName));
						
		model.addAttribute("contractAP",new ContractAPsEntity());
				
		model.addAttribute("projects",list);
		model.addAttribute("contract",contract);
		model.addAttribute("assigProjects",assigProjects);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "contractAPsAdd";
	}
}
