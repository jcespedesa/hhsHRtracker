package com.trc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.AssigProjectsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.AssigProjectsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("hhsHRtracker/assigProjects")
public class AssigProjectsController 
{
	@Autowired
	AssigProjectsService service;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	ProjectsService serviceProjects;
	
	@Autowired
	LogsService serviceLogs;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllProjects(Model model,Long quserId,String qperiod,Long qdivisionId,Long id) throws RecordNotFoundException
	{
		String department=null;
		String userIdString=null;
		
		DivisionsEntity division=serviceDivisions.getDivisionById(qdivisionId);
		
		department=division.getDnumber();
		
		//Retrieving user information
		UsersEntity user=serviceUsers.getUserById(id);
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		userIdString=String.valueOf(id);
		
		//Finally retrieving the list of assigned projects
		List<AssigProjectsEntity> list=service.getAllProjectsByDivId(department,userIdString);
		
		model.addAttribute("user",user);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("projects",list);
			
		return "assigProjectsList";
				
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteProjectById(Model model, Long id, Long userId,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		//Retrieving assigned project
		AssigProjectsEntity assignProject=service.getProjectById(id);
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving user
		UsersEntity user=serviceUsers.getUserById(userId);
		
		String message="Item was removed from the list...";
		
		service.deleteProject(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Removing assigned project from "+ user.getUsername());
		log.setObject(assignProject.getProject());
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		
		model.addAttribute("userId",userId);
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "assigProjectsRedirect";
		
		
	}
	
	@RequestMapping(path="/createProject", method=RequestMethod.POST)
	public String createProject(Model model,AssigProjectsEntity assigProject,Long quserId,Long userId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		String projectName=null;
		String department=null;
		String caseManager=null;
		
		//System.out.println("Inside the controller to update or create. Object is: "+ project);
		LogsEntity log=new LogsEntity();
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving user
		UsersEntity user=serviceUsers.getUserById(userId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Finalizing to set up the entity
		
		projectName=serviceProjects.getProjectByNum(assigProject.getProject());
		department=qdivision.getDnumber();
		caseManager=quser.getUsername();
		
		assigProject.setProjectName(projectName);
		assigProject.setDepartment(department);
		assigProject.setCaseManager(caseManager);
							
		String message="Project List was updated...";
		
		service.createProject(assigProject);
		
		log.setSubject(quser.getEmail());
		log.setAction("Assigning new project to "+ user.getUsername());
		log.setObject(assigProject.getProjectName());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		
		model.addAttribute("userId",userId);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "assigProjectsRedirect";
			
	}
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editProjectsById(Model model,Long id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
		
		//Retrieving user information
		UsersEntity user=serviceUsers.getUserById(id);
		
		//Retrieving quser information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
		
		List<ProjectsEntity> list=serviceProjects.getProjectsByDiv(qdivision.getDnumber());
		
		
		model.addAttribute("project",new AssigProjectsEntity());
				
		model.addAttribute("projects",list);
		
		model.addAttribute("user",user);
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "assigProjectsAdd";
	}
}
