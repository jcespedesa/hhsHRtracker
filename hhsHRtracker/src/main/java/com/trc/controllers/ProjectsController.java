package com.trc.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("hhsHRtracker/projects")
public class ProjectsController 
{
	@Autowired
	ProjectsService service;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	LogsService serviceLogs;
	
	//CRUD operations for projects
	
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllProjects(Model model,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		String department=null;
		String localDivision=null;
			
		//System.out.println("qdivisionId is "+ qdivisionId);
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
						
		department=qdivision.getDnumber();
		
		//Retrieving list of projects
		List<ProjectsEntity> list=service.getAllProjectsByDiv(department);
				
		//Trying to get divisions and sites name
		for(ProjectsEntity dname : list)
        {
        	localDivision=serviceDivisions.getDivisionByNumber(dname.getDepartment());
        	
        	dname.setBuffer1(localDivision);
        }
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("projects",list);
			
		return "projectsList";
			
			
	}
		
		
		@RequestMapping(path="/edit", method=RequestMethod.POST)
		public String editProjectsById(Model model,Optional<Long>id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
		{
			
			String department="300";
			
			List<DivisionsEntity> listDivisions=serviceDivisions.getAllByName();
			List<Integer> listUB=service.findNonRepeatedUB(department);
			
			
			String site1="";
			String site2="";
			String divisionName=null;
			String priznakNewRecord=null;
			
			//Retrieving user information
			UsersEntity quser=serviceUsers.getUserById(quserId);
			
			//Retrieving division
			DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
			
			
			if(id.isPresent())
			{
				ProjectsEntity entity=service.getProjectById(id.get());
				
				
				model.addAttribute("project",entity);
				
				site1=service.getSiteBySiteNumber1(entity.getSiteNumber1());
				site2=service.getSiteBySiteNumber2(entity.getSiteNumber2());
				
								
				model.addAttribute("site1",site1);
				model.addAttribute("site2",site2);
				
				//Finding division name
				divisionName=serviceDivisions.getDivisionByNumber(entity.getDepartment());
				
				priznakNewRecord="false";
				
			}
			else
			{
				model.addAttribute("project",new ProjectsEntity());
				
				priznakNewRecord="true";
				
			}
			
			model.addAttribute("divisions",listDivisions);
			model.addAttribute("udelnyBeses",listUB);
			model.addAttribute("divisionName",divisionName);
			model.addAttribute("priznakNew",priznakNewRecord);
			
			model.addAttribute("quser",quser);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivision",qdivision);
			
			return "projectsAddEdit";
		}
		
		@RequestMapping(path="/delete", method=RequestMethod.POST)
		public String deleteProjectById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
		{
			LogsEntity log=new LogsEntity();
			
			//Retrieving user
			UsersEntity quser=serviceUsers.getUserById(quserId);
			
			//Retrieving project
			ProjectsEntity entity=service.getProjectById(id);
			
			String message="Item was deleted...";
			
			service.deleteProjectById(id);
			
			log.setSubject(quser.getEmail());
			log.setAction("Deleting project");
			log.setObject(entity.getProject());
			
			serviceLogs.saveLog(log);
			
			model.addAttribute("message",message);
			
			model.addAttribute("quserId",quserId);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivisionId",qdivisionId);
			
			return "projectsRedirect";
			
			
		}
		
		@RequestMapping(path="/createProject", method=RequestMethod.POST)
		public String createOrUpdateProject(Model model,ProjectsEntity project,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
		{
			int priznakDuplicate=0;
			
			String projectNumber=null;
			String message=null;
						
			
			//System.out.println("Inside the controller to update or create. Object is: "+ project);
			LogsEntity log=new LogsEntity();
			
			//Retrieving user
			UsersEntity quser=serviceUsers.getUserById(quserId);
			
			
			//Checking if this project is already in the table, for new records only
			
			if(project.getProjectid()==null)
			{	
			
				projectNumber=project.getProject();
				priznakDuplicate=service.findDuplicates(projectNumber);
				
			}	
			
			//System.out.println("The value of priznak duplicate for "+ projectNumber +" is "+ priznakDuplicate);
			
			if(priznakDuplicate==0)
			{
						
				message="List was updated...";
			
				service.createOrUpdate(project);
			
				log.setSubject(quser.getEmail());
				log.setAction("Creating/Modiying project");
				log.setObject(project.getProject());
			
						
			}
			else
			{
				message="Error: Duplicate found, project was not created at this time. Please review the list of projects again...";
				
				log.setSubject(quser.getEmail());
				log.setAction("Failing to create a new project due to duplicity");
				log.setObject(project.getProject());
				
			}
			
			serviceLogs.saveLog(log);
				
			model.addAttribute("message",message);
			
			model.addAttribute("quserId",quserId);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivisionId",qdivisionId);
			
			return "projectsRedirect";
			
			
		}
		
		@RequestMapping(path="/search", method=RequestMethod.POST)
		public String search(Model model,String stringSearch,Long quserId,String qperiod,Long qdivisionId)
		{
			
			model.addAttribute("quserId",quserId);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivisionId",qdivisionId);
			
			return "projectsSearch";
			
			
		}
		
		@RequestMapping(path="/findProjectNum", method=RequestMethod.POST)
		public String findProjectNum(Model model,String stringSearch,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
		{
			//System.out.println("Inside the controller to search by string. Object is: "+ stringSearch);
			
			List<ProjectsEntity> list=service.searchProjectsByNum(stringSearch);
			
			//Retrieving user information
			UsersEntity quser=serviceUsers.getUserById(quserId);
			
			//Retrieving division
			DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
			
			String localDivision=null;
			
			//Trying to get divisions and sites name
			
			for(ProjectsEntity dname : list)
	        {
	        	localDivision=serviceDivisions.getDivisionByNumber(dname.getDepartment());
	        	
	        	dname.setBuffer1(localDivision);
	        }
			
			//System.out.println(list);
						
			model.addAttribute("projects",list);
			model.addAttribute("stringSearch",stringSearch);
			
			model.addAttribute("quser",quser);
			model.addAttribute("qperiod",qperiod);
			model.addAttribute("qdivision",qdivision);
			
			return "projectsList";
			
			
		}
		
		@RequestMapping(path="/findProjectName", method=RequestMethod.POST)
		public String findProjectName(Model model, String stringSearch)
		{
			//System.out.println("Inside the controller to search by string. Object is: "+ stringSearch);
			
			List<ProjectsEntity> list=service.searchProjectsByName(stringSearch);
			
			//System.out.println(list);
			
			model.addAttribute("projects",list);
			model.addAttribute("stringSearch",stringSearch);
			
			return "projectsList";
			
			
		}
		
		@GetMapping("/hhsView")
		public String hhsViewForm(Model model)
		{
					
			List<ProjectsEntity> list=service.getHhsFormView();
			
			String localDivision=null;
						
			
			//Trying to get divisions and sites name
			
			for(ProjectsEntity dname : list)
	        {
	        	localDivision=serviceDivisions.getDivisionByNumber(dname.getDepartment());
	        	
	        	dname.setBuffer1(localDivision);
	        }
			
						
			model.addAttribute("projects",list);
				
			return "projectsList";
				
				
		}
}
