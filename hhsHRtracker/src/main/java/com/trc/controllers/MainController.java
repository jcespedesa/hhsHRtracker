package com.trc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.AssigProjectsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.PeriodsEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.AssigProjectsService;
import com.trc.services.DivisionsService;
import com.trc.services.EmailService;
import com.trc.services.LogsService;
import com.trc.services.PeriodsService;
import com.trc.services.ProjectsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.SettingsService;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/hhsHRtracker")
public class MainController 
{

	@Autowired
	UsersService service;
	
	@Autowired
	PeriodsService servicePeriods;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	ProjectsService serviceProjects;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	AssigProjectsService serviceAssigProjects;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	EmailService serviceEmail;
	
	@Autowired
	SettingsService serviceSettings;
	
		
	@RequestMapping("/index")
	public String index(Model model,String message)
	{
		//Processing messages in the page
		model.addAttribute("message",message);
				
		return "index";
	}
	
	@RequestMapping(path="/selPeriod", method=RequestMethod.POST)
	public String selProject(Model model, String email, String password)
	{
		UsersEntity user=new UsersEntity();
		
		LogsEntity log=new LogsEntity();
		
		String message=null;
		String storedPass=null;
						
				
		//Retrieving user information
		user=service.getUserByEmail(email);
		
		//System.out.println("User is "+ user);
		
		if(user==null)
		{
			message="Username not found...";
			
			log.setSubject(email);
			log.setAction("Warning: Unknown/Unregistered user is trying to login in the system");
			log.setObject("Main login page");
			
			serviceLogs.saveLog(log);
			
			model.addAttribute("message",message);
						
			return "index";
		}
		else
		{
			
			if(user.getActive().equals("No"))
			{
				message="Account is deactivated, please contact your system administrator...";
				
				log.setSubject(email);
				log.setAction("Deactivated user is trying to login in the system");
				log.setObject("Main login page");
				
				serviceLogs.saveLog(log);
				
				model.addAttribute("message",message);
							
				return "index";
			}
			else
			{
				//Retrieving stored password
				storedPass=service.getPassByEmail(email);
				
				//checking if the input password matches with the stored password
		    	boolean isPasswordMatch=passwordEncoder.matches(password,storedPass);
				
				if(isPasswordMatch)
				{
					message="Please select a period to continue...";
				
					//Retrieving list of periods
					List<PeriodsEntity> periods=servicePeriods.getActivesByUdelnyBes();
				
					//Retrieving list of divisions
					List<DivisionsEntity> divisions=serviceDivisions.getAllPriznakApp();
				
				
					log.setSubject(email);
					log.setAction("Successful login in the system");
					log.setObject("Main login page");
				
					serviceLogs.saveLog(log);
								
					model.addAttribute("message",message);
					model.addAttribute("quserId",user.getUserid());
					model.addAttribute("periods",periods);
					model.addAttribute("divisions",divisions);
				
					return "periodSel";
					
				}
				else
				{
					message="Validation failed...";
					
					log.setSubject(email);
					log.setAction("Trying to login with an invalid password");
					log.setObject("Main login page");
					
					serviceLogs.saveLog(log);
					
					model.addAttribute("message",message);
					
					return "index";
				}
			}
			
			
		}
		
	}
	
	@RequestMapping(path="/mainMenu", method=RequestMethod.POST)
	public String mainMenu(Model model,String qperiod,Long quserId,Long qdivisionId) throws RecordNotFoundException
	{
		String department=null;
		String userIdString=null;
		
		//Retrieving user
		UsersEntity quser=service.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		//Finding variables for lists
		
		department=qdivision.getDnumber();
		userIdString=String.valueOf(quserId);
		
		//Retrieving list of projects
		if(quser.getRole().equals("admin"))
		{	
			List<ProjectsEntity> list=serviceProjects.getProjectsByDiv(department);
			model.addAttribute("projects",list);
		}
		else
		{	
			List<AssigProjectsEntity> list=serviceAssigProjects.getAllProjectsByDivId(department,userIdString);
			model.addAttribute("projects",list);
		}						
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("quser",quser);
		model.addAttribute("qdivision",qdivision);
		
				
		return "mainMenu";
	}
	
	@RequestMapping(path="/projectSel", method=RequestMethod.POST)
	public String projectSel(Model model,String qperiod,Long quserId,Long qdivisionId) throws RecordNotFoundException
	{
		//Retrieving user information
		UsersEntity quser=service.getUserById(quserId);
		
		//Retrieving division information
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving list of projects
		List<ProjectsEntity> list=serviceProjects.getAllHHSbyUB();
		
								
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("quser",quser);
		model.addAttribute("projects",list);
		model.addAttribute("qdivision",qdivision);
				
		return "projectsMenu";
	}
	
	@RequestMapping(path="/about", method=RequestMethod.POST)
	public String search(Model model,String stringSearch,Long quserId,String qperiod,Long qdivisionId)
	{
		String message="TRC Corp - Fall 2021";
		
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "about";
		
		
	}
	
	@RequestMapping(path="/portalAcc")
	public String access(Model model)
	{
		String message="Please input a valid CC email to receive your access code...";
		
		model.addAttribute("message",message);
		
				
		return "portalAccess";
		
		
	}
	
	@RequestMapping(path="/sendPass", method=RequestMethod.POST)
	public String sendPass(Model model,String email)
	{
		int existentEmail=0;
				
		String pass=null;
		String localMessage="An email was sent to your inbox, please review...";
		String message="This is your code to access the hhsHRtracker: ";	
		String encodedPass=null;
		String subject="Your code for the hhsHRtracker";
		String pustoy=" ";
		String sender="";
		String senderFromEmailSettings="emailSettings";
		
		//Creating the log 
		LogsEntity log=new LogsEntity();
		
		//Verifying if this is an existent email in the system
		existentEmail=service.findDuplicates(email);
		
		//System.out.println("The value of existentEmail is: "+ existentEmail);
		
		if(existentEmail==0)
		{
			localMessage="Invalid or no registered email. Please contact your System Administrator ";
			
			log.setSubject(email);
			log.setAction("Failed to receive an access code due to misspelled or unregistered email");
			log.setObject("Main login page");
				
		}
		else
		{
			//Retrieving user information
			UsersEntity user=service.getUserByEmail(email);
			
			//Creating the access code
			pass=service.createAccessCode(user.getUserid());
		
			//Assembling the message
			message=message+pustoy+pass;
			
			//Retrieving the sender information
			sender=serviceSettings.findEmailSender(senderFromEmailSettings);
						
			//Sending the registration email
			serviceEmail.sendMail(user.getEmail(), subject, message, sender);
			
			//Encoding password
	    	encodedPass=passwordEncoder.encode(pass);
									
			//Saving the new password for this user
			service.changePass(user.getUserid(),encodedPass);
			
			log.setSubject(email);
			log.setAction("Receiving a new automatic generated code to access the system");
			log.setObject("Main login page");
						
		}
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",localMessage);
		
		return "index";
		
		
	}
	
}
