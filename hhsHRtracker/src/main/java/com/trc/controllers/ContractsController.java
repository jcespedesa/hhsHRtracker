package com.trc.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.ContractAPsEntity;
import com.trc.entities.ContractATsEntity;
import com.trc.entities.ContractsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.ProjectTypesEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectTypesService;
import com.trc.services.ProjectsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.TrainingsService;
import com.trc.services.ContractAPsService;
import com.trc.services.ContractATsService;
import com.trc.services.ContractsService;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/contracts")
public class ContractsController 
{
	@Autowired
	ContractsService service;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	ProjectsService serviceProjects;
	
	@Autowired
	TrainingsService serviceTrainings;
	
	@Autowired
	ProjectTypesService serviceProjectTypes;
	
	@Autowired
	ContractAPsService serviceContractAPs;
	
	@Autowired
	ContractATsService serviceContractATs;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAll(Model model,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		String projectDef=null;
				
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
				
						
		//Retrieving list of contracts
		List<ContractsEntity> list=service.getAll();
		
		//Updating project definitions
		for(ContractsEntity contract : list)
		{
			projectDef=serviceProjectTypes.getTypeName(contract.getTypeProject());
			
			contract.setProject(projectDef);
		}
				
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("contracts",list);
					
		return "contractsList";
			
	}
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editContractById(Model model,Optional<Long> id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
		String bufferName=null;
		String priznakNew="false";
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving already existent contract list
		List<ContractsEntity> listContracts=service.getAll();
		
		//Retrieving list of projects
		List<ProjectsEntity> listProjects=serviceProjects.getAllHHSbyUB();
		
		//Retrieving list of type of projects
		List<ProjectTypesEntity> listProjectTypes=serviceProjectTypes.getActives();
						
		if(id.isPresent())
		{
			ContractsEntity entity=service.getContractById(id.get());
			
			
			//Retrieving list of assigned projects
			List<ContractAPsEntity> assigProjects=serviceContractAPs.getAPbyContract(entity.getContract());
			
			//filling up the names of the projects
			for(ContractAPsEntity contract : assigProjects)
			{
				bufferName=serviceProjects.getProjectByNum(contract.getProjectNum());
				contract.setBufferName(bufferName);
			}
			
			//trying to sort the array of assigned projects
			assigProjects.sort(Comparator.comparing(ContractAPsEntity::getBufferName));
			
			
			//Retrieving list of assigned training
			List<ContractATsEntity> assigTrainings=serviceContractATs.getATbyContract(entity.getContract());
			
			//filling up the names of the training
			for(ContractATsEntity contract : assigTrainings)
			{
				bufferName=serviceTrainings.getTrainingName(contract.getTrainingNum());
				contract.setBufferName(bufferName);
			}
			
			//trying to sort the array of assigned training
			assigTrainings.sort(Comparator.comparing(ContractATsEntity::getBufferName));
			
			
			model.addAttribute("contract",entity);
			model.addAttribute("assigProjects",assigProjects);
			model.addAttribute("assigTrainings",assigTrainings);
			
			
		}
		else
		{
			model.addAttribute("contract",new ContractsEntity());
			
			priznakNew="true";
			
		}
						
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("contracts",listContracts);
		model.addAttribute("projects",listProjects);
		model.addAttribute("projectTypes",listProjectTypes);
				
		model.addAttribute("priznakNew",priznakNew);
		
		return "contractsAddEdit";
	}
	
	@RequestMapping(path="/createContract", method=RequestMethod.POST)
	public String createOrUpdateContract(Model model,ContractsEntity contract,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		int priznakDuplicate=0;
		
		String localContract=null;
		String message=null;
		
		//System.out.println("Inside the controller to update or create. Object is: "+ contract);
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//System.out.println("The value of priznak duplicate for "+ localContract +" is "+ priznakDuplicate);
		
		if(contract.getContractid()==null)
		{
			//Checking if this contract is not already in the system
			localContract=contract.getContract();
			priznakDuplicate=service.findDuplicates(localContract);
			
		}
		
		if(priznakDuplicate==0)
		{
			service.createOrUpdate(contract);
									
			message="Contract information was updated successfully...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Creating/Modiying Contract information");
			log.setObject(contract.getContract());
		
		}
		else
		{
			message="Error: Duplicated Contract found, new record was not created at this time. Please review the list of contracts again...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Failing to create a new contract due to duplicity");
			log.setObject(contract.getContract());
			
		}
		
		serviceLogs.saveLog(log);
							
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "contractsRedirect";
			
	}
	
}
