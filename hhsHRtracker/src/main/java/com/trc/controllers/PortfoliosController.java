package com.trc.controllers;

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
import com.trc.entities.PortfoliosEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.ClientsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.PortfoliosService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/portfolios")
public class PortfoliosController 
{
	@Autowired
	PortfoliosService service;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	ClientsService serviceClients;
	
	//CRUD operations for Portfolios
	
		@RequestMapping(path="/list", method=RequestMethod.POST)
		public String getAllPortfolios(Model model, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
		{
			String directorId=null;
			
			Long directorIdLong=null;
			
			List<PortfoliosEntity> list=service.getAllByName();
			
			//Retrieving user identity
	        UsersEntity quser=serviceUsers.getUserById(quserId);
	        
	      //Retrieving division
			DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
			
			//Completing director names
			for(PortfoliosEntity portfolio : list)
			{
				directorId=portfolio.getDirectorId();
				directorIdLong=Long.parseLong(directorId);
				
				portfolio.setDirector(serviceClients.getName(directorIdLong));
				
			}
	        
	        model.addAttribute("quser",quser);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivision",qdivision);
			
			model.addAttribute("portfolios",list);
			
			return "portfoliosList";
			
			
		}
		
		@RequestMapping(path="/edit", method=RequestMethod.POST)
		public String editPortfoliosById(Model model,Optional<Long> id, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
		{
			String priznakNew=null;
			String directorName;
			
			Long directorIdLong=null;
			
			if(id.isPresent())
			{
				PortfoliosEntity entity=service.getPortfolioById(id.get());
				
				priznakNew="false";
				
				//Retrieving current director's name
							
				directorIdLong=Long.parseLong(entity.getDirectorId());  
				
				directorName=serviceClients.getName(directorIdLong);
				
				model.addAttribute("portfolio",entity);
				model.addAttribute("directorName",directorName);
				
			}
			else
			{
				model.addAttribute("portfolio",new PortfoliosEntity());
				
				priznakNew="true";
						
			}
			
			//Retrieving user identity
	        UsersEntity quser=serviceUsers.getUserById(quserId);
	        
	        //Retrieving division
			DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
			
			//Retrieving list of employees
			List<ClientsEntity> clients=serviceClients.getAllActives();
	        
	        model.addAttribute("quser",quser);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivision",qdivision);
			
			model.addAttribute("clients",clients);
			model.addAttribute("priznakNew",priznakNew);
						
			return "portfoliosAddEdit";
			
		}
		
		@RequestMapping(path="/delete", method=RequestMethod.POST)
		public String deletePortfolioById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
		{
			String message=null;
			
			//Retrieving Portfolio identity
			PortfoliosEntity portfolio=service.getPortfolioById(id);
						
			service.deletePortfolioById(id);
			
			message="Portfolio was deleted...";
			
			//Retrieving user identity
	        UsersEntity quser=serviceUsers.getUserById(quserId);
	        
	      //Retrieving division
			DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
	        
	      //Processing logs
			LogsEntity log=new LogsEntity();
			log.setSubject(quser.getEmail());
			log.setAction("Deleting Portfolio from the database. Item ID is "+ portfolio.getPnumber());
			log.setObject(portfolio.getPname());
			serviceLogs.saveLog(log);
	        
			model.addAttribute("quser",quser);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivision",qdivision);
			
			model.addAttribute("message",message);
			
			return "portfoliosRedirect";
			
		}
		
		@RequestMapping(path="/createPortfolio", method=RequestMethod.POST)
		public String createOrUpdatePortfolio(Model model, PortfoliosEntity portfolio, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
		{
			int priznakDuplicate=0;
			
			String message=null;
			String localPortfolio=null;
			
			//Retrieving user identity
	        UsersEntity quser=serviceUsers.getUserById(quserId);
	        
	      //Retrieving division
			DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
	        
	        if(portfolio.getPid()!=null)
	        {
	        	//Modify the record
	        
				service.createOrUpdate(portfolio);
			
				message="Portfolio was successfully modified...";
				        
				//Processing logs
				LogsEntity log=new LogsEntity();
				log.setSubject(quser.getEmail());
				log.setAction("Modifying Portfolio record. Item ID is "+ portfolio.getPnumber());
				log.setObject(portfolio.getPname());
				serviceLogs.saveLog(log);
			
	        }
	        else
	        {
	        	//Creating a new record
	        	
	        	//Checking if this item is not already in the system
	        	localPortfolio=portfolio.getPnumber();
	        	priznakDuplicate=service.findDuplicates(localPortfolio);
	        	
	        	if(priznakDuplicate==0)
	        	{
	        		service.createOrUpdate(portfolio);
					
	        		message="New Portfolio was created successfully...";
	        		
			   		//Processing logs
	        		LogsEntity log=new LogsEntity();
	        		log.setSubject(quser.getEmail());
	        		log.setAction("Creating new Portfolio record in the database. Portfolio ID is "+ portfolio.getPnumber());
	        		log.setObject(portfolio.getPname());
	        		serviceLogs.saveLog(log);
	        	}
	        	else
	        	{
	        		message="Error: Duplicate portfolio number was found. New record was not created at this time. Please review the list of Portfolios again...";
					
	        		//Processing logs
	        		LogsEntity log=new LogsEntity();
	        		log.setSubject(quser.getEmail());
	        		log.setAction("Failing to create a new Portfolio due to duplicity: "+ portfolio.getPnumber());
	        		log.setObject(portfolio.getPname());
	        		serviceLogs.saveLog(log);
	        	}
	        	        	
	        }
	        	        
	        model.addAttribute("quser",quser);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivision",qdivision);
			
			model.addAttribute("message",message);
			
			return "portfoliosRedirect";
			
			
		}

}
