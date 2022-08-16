package com.trc.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.AssigProjectsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.AssigProjectsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("hhsHRtracker/users")
public class UsersController 
{

	@Autowired
	UsersService service;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	AssigProjectsService serviceAssigProjects;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllUsers(Model model,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
				
		List<UsersEntity> list=service.getAllByName();
		
		//Retrieving user information
		UsersEntity quser=service.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
				
					
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("users",list);
			
		return "usersList";
			
			
	}
		
		
		@RequestMapping(path="/edit", method=RequestMethod.POST)
		public String editUsersById(Model model,Optional<Long>id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
		{
			String department=null;
			String userIdString=null;
			String priznakNewRecord=null;
			
			//Retrieving user information
			UsersEntity quser=service.getUserById(quserId);
			
			//Retrieving division
			DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
			
			
			
			if(id.isPresent())
			{
				UsersEntity entity=service.getUserById(id.get());
				
				department=qdivision.getDnumber();
				userIdString=String.valueOf(id.get());
				
				//Finally retrieving the list of assigned projects
				List<AssigProjectsEntity> projects=serviceAssigProjects.getAllProjectsByDivId(department,userIdString);
				
				priznakNewRecord="false";
				
				model.addAttribute("user",entity);
				model.addAttribute("projects",projects);
											
			}
			else
			{
				model.addAttribute("user",new UsersEntity());
				
				priznakNewRecord="true";
				
			}
			
			model.addAttribute("priznakNew",priznakNewRecord);
			
			model.addAttribute("quser",quser);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivision",qdivision);
			
			return "usersAddEdit";
		}
		
		@RequestMapping(path="/delete", method=RequestMethod.POST)
		public String deleteUserById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
		{
			LogsEntity log=new LogsEntity();
			
			//Retrieving user
			UsersEntity quser=service.getUserById(quserId);
			
			//Retrieving project
			UsersEntity entity=service.getUserById(id);
			
			String message="Item was deleted...";
			
			service.deleteUserById(id);
			
			log.setSubject(quser.getEmail());
			log.setAction("Deleting User from the system");
			log.setObject(entity.getEmail());
			
			serviceLogs.saveLog(log);
						
			model.addAttribute("message",message);
			
			model.addAttribute("quserId",quserId);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivisionId",qdivisionId);
			
			return "usersRedirect";
			
			
		}
		
		@RequestMapping(path="/createUser", method=RequestMethod.POST)
		public String createOrUpdateUser(Model model,UsersEntity user,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
		{
			int priznakDuplicate=0;
			
			String localEmail=null;
			String message=null;
			String encodedPass=null;
			
			//System.out.println("Inside the controller to update or create. Object is: "+ project);
			LogsEntity log=new LogsEntity();
			
			//Retrieving quser
			UsersEntity quser=service.getUserById(quserId);
			
			
			//Checking if this email is already in the system, for new records only
			if(user.getUserid()==null)
			{	
			
				localEmail=user.getEmail();
				priznakDuplicate=service.findDuplicates(localEmail);
				
			}	
			
			//System.out.println("The value of priznak duplicate for "+ localEmail +" is "+ priznakDuplicate);
			
			if(priznakDuplicate==0)
			{
						
				message="List was updated...";
				
				//Encoding password
				encodedPass=passwordEncoder.encode(user.getPassword());
				user.setPassword(encodedPass);
			
				service.createOrUpdate(user);
			
				log.setSubject(quser.getEmail());
				log.setAction("Creating/Modiying user");
				log.setObject(user.getUsername());
			
						
			}
			else
			{
				message="Error: Email duplicate found, user was not created at this time. Please review the list of users again...";
				
				log.setSubject(quser.getEmail());
				log.setAction("Failing to create a new user due to email duplicity");
				log.setObject(user.getUsername());
				
			}
			
			serviceLogs.saveLog(log);
						
			model.addAttribute("message",message);
			
			model.addAttribute("quserId",quserId);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivisionId",qdivisionId);
			
			return "usersRedirect";
			
			
		}
		
		@RequestMapping(path="/changePass", method=RequestMethod.POST)
		public String changePass(Model model,UsersEntity user,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
		{
			String username=null;
			
			//Retrieving user information
			UsersEntity quser=service.getUserById(quserId);
			
			username=quser.getUsername();
			
			model.addAttribute("username",username);
						
			model.addAttribute("quserId",quserId);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivisionId",qdivisionId);
			
			return "usersChangePass";
			
			
		}
		
		@RequestMapping(path="/changePassExe", method=RequestMethod.POST)
		public String changePassExe(Model model,UsersEntity user,Long quserId,String qperiod,Long qdivisionId,String pass1,String pass2) throws RecordNotFoundException
		{
			
			String message=null;
			String priznak=null;
			String fileLocator=null;
			String encodedPass=null;
			
						
			if(pass1.equals(pass2))
			{
				
				if(pass1.length() < 6)
				{
					message="Password is too short...";
					fileLocator="false";
				}
				else
				{	
					//Checking for at least one capital letter and one number
					priznak=service.checkString(pass1);
					
					if(priznak.equals("false"))
					{
						message="Password is invalid...";
						fileLocator="false";
					}
					if(priznak.equals("true"))
					{
						//Encoding password
				    	encodedPass=passwordEncoder.encode(pass1);
						
						service.changePass(quserId,encodedPass);
						message="Password was updated successfully...";
						fileLocator="true";
						
					}
					
				}
				
			}
			else
			{
				message="Password fields do not match...";
				fileLocator="false";
			}
				
			//System.out.println("file locator is "+ fileLocator);
			
			model.addAttribute("message",message);
			model.addAttribute("fileLocator",fileLocator);
						
			model.addAttribute("quserId",quserId);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivisionId",qdivisionId);
			
			return "usersChangePassRedirect";
					
		}
}
