package com.trc.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.SettingsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.SettingsService;
import com.trc.services.UsersService;

@Controller
@RequestMapping("/hhsHRtracker/settings")
public class SettingsController 
{
	@Autowired
	SettingsService service;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	LogsService serviceLogs;
	
	//CRUD operations for Settings
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllSettings(Model model,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
				
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
								
		//Retrieving list of settings
		List<SettingsEntity> list=service.getAllSettings();
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("settings",list);
		
		return "settingsList";
		
		
	}
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editSettingsById(Model model,Optional<Long>id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
						
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		if(id.isPresent())
		{
			SettingsEntity entity=service.getSettingById(id.get());
			model.addAttribute("setting",entity);
		}
		else
		{
			model.addAttribute("setting",new SettingsEntity());
			
		}
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		return "settingsAddEdit";
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteSettingById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving project
		SettingsEntity entity=service.getSettingById(id);
		
		String message="Item was deleted...";
		
		service.deleteSettingById(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Deleting setting from the system");
		log.setObject(entity.getSname());
		
		serviceLogs.saveLog(log);
					
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		
		return "settingsRedirect";
		
	}
	
	@RequestMapping(path="/createSetting", method=RequestMethod.POST)
	public String createOrUpdateSetting(Model model,SettingsEntity setting, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		int priznakDuplicate=0;
		
		String localSetting=null;
		String message=null;
				
		//System.out.println("Inside the controller to update or create. Object is: "+ setting);
		LogsEntity log=new LogsEntity();
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		
		//Checking if this setting is already in the system, for new records only
		if(setting.getSettingid()==null)
		{	
		
			localSetting=setting.getSname();
			priznakDuplicate=service.findDuplicates(localSetting);
			
		}	
		
		//System.out.println("The value of priznak duplicate for "+ localSetting +" is "+ priznakDuplicate);
		
		if(priznakDuplicate==0)
		{
					
			message="List was updated...";
			
			service.createOrUpdate(setting);
		
			log.setSubject(quser.getEmail());
			log.setAction("Creating/Modiying setting");
			log.setObject(setting.getSname());
		
					
		}
		else
		{
			message="Error: Setting duplicate found, setting was not created at this time. Please review the list of settings again...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Failing to create a new setting due to duplicity");
			log.setObject(setting.getSname());
			
		}
		
		serviceLogs.saveLog(log);
					
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "settingsRedirect";
		
		
	}

}
