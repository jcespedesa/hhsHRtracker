package com.trc.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.CertificatesEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.CertificatesService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("hhsHRtracker/certificates")
public class CertificatesController 
{
	@Autowired
	CertificatesService service;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	LogsService serviceLogs;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAll(Model model,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
				
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);	
						
		//Retrieving list of certificates
		List<CertificatesEntity> list=service.getAllCerts();
				
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("certs",list);
			
		return "certsList";
			
	}
	
	@RequestMapping(path="/add", method=RequestMethod.POST)
	public String editCertById(Model model,Optional<Long>id,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException 
	{
				
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving list of already existent certificate numbers
		List<CertificatesEntity> listCertificates=service.getAllCerts();
		
		if(id.isPresent())
		{
			CertificatesEntity entity=service.getCertById(id.get());
			model.addAttribute("cert",entity);
		}
		else
		{
			model.addAttribute("cert",new CertificatesEntity());
			
		}
						
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		
		model.addAttribute("certificates",listCertificates);
		
		return "certsAddEdit";
	}
	
	@RequestMapping(path="/createCert", method=RequestMethod.POST)
	public String createOrUpdateCert(Model model,CertificatesEntity cert,Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		int priznakDuplicate=0;
		
		String localCert=null;
		String message=null;
		
		//System.out.println("Inside the controller to update or create. Object is: "+ cert);
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//System.out.println("The value of priznak duplicate for "+ localCert +" is "+ priznakDuplicate);
		
		if(cert.getCertid()==null)
		{
			//Checking if this cert is not already in the system
			localCert=cert.getCertNumber();
			priznakDuplicate=service.findDuplicates(localCert);
			
		}
		
		if(priznakDuplicate==0)
		{
			service.createOrUpdate(cert);
									
			message="Certificates information was updated successfully...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Creating/Modiying Certificate");
			log.setObject(cert.getCertName());
		
		}
		else
		{
			message="Error: Duplicated Certificate found, new cert was not created at this time. Please review the list of certs again...";
			
			log.setSubject(quser.getEmail());
			log.setAction("Failing to create a new cert due to duplicity");
			log.setObject(cert.getCertName());
			
		}
		
		serviceLogs.saveLog(log);
							
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "certsRedirect";
			
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteCertById(Model model, Long id, Long quserId,String qperiod,Long qdivisionId) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		//Retrieving user
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving project
		CertificatesEntity entity=service.getCertById(id);
		
		String message="Item was deleted...";
		
		service.deleteCertById(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Deleting Certificate");
		log.setObject(entity.getCertName());
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "certsRedirect";
		
		
	}
}
