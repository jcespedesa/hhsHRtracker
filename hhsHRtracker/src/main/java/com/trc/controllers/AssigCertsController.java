package com.trc.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.trc.entities.AssigCertsEntity;
import com.trc.entities.CertificatesEntity;
import com.trc.entities.ClientsEntity;
import com.trc.entities.DivisionsEntity;
import com.trc.entities.LogsEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.UsersEntity;
import com.trc.services.AssigCertsService;
import com.trc.services.CertificatesService;
import com.trc.services.ClientsService;
import com.trc.services.DivisionsService;
import com.trc.services.LogsService;
import com.trc.services.ProjectsService;
import com.trc.services.RecordNotFoundException;
import com.trc.services.UsersService;

@Controller
@RequestMapping("hhsHRtracker/assigCerts")
public class AssigCertsController 
{
	@Autowired
	AssigCertsService service;
	
	@Autowired
	DivisionsService serviceDivisions;
	
	@Autowired
	UsersService serviceUsers;
	
	@Autowired
	ProjectsService serviceProjects;
	
	@Autowired
	LogsService serviceLogs;
	
	@Autowired
	CertificatesService serviceCertificates;
	
	@Autowired
	ClientsService serviceClients;
	
	@RequestMapping(path="/list", method=RequestMethod.POST)
	public String getAllAssigs(Model model,Long quserId,String qperiod,Long qdivisionId,String qproject,Long id,Long projectId) throws RecordNotFoundException
	{
				
		String clientIdString=null;
		
		String todayDateString=null;
		String yesterdayDateString=null;
		String fifteenDaysAgoDateString=null;
		String fifteenDaysToDateString=null;
		
		String status=null;
		String priznakSoonExpiration=null;
		
		//Trying to get todayDate
		java.sql.Date todayDate=new java.sql.Date(System.currentTimeMillis());
		
		//Trying to get yesterday date
		java.sql.Date yesterdayDate=new java.sql.Date(System.currentTimeMillis()-24*60*60*1000);
		
		//Trying to get fifteen days ago date
		java.sql.Date fifteenDaysAgoDate=new java.sql.Date(System.currentTimeMillis()-360*60*60*1000);
		
		//Trying to get fifteen days ago date
		java.sql.Date fifteenDaysToDate=new java.sql.Date(System.currentTimeMillis()+360*60*60*1000);
		
		//System.out.println("Today Date is "+ todayDate);
		//System.out.println("Yesterday Date is "+ yesterdayDate);
		//System.out.println("15 days ago Date is "+ fifteenDaysAgoDate);
		
		//Preparing the required custom format
		DateFormat todayDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		
		//Converting dates to strings
		todayDateString=todayDateFormat.format(todayDate);
		yesterdayDateString=todayDateFormat.format(yesterdayDate);
		fifteenDaysAgoDateString=todayDateFormat.format(fifteenDaysAgoDate);
		fifteenDaysToDateString=todayDateFormat.format(fifteenDaysToDate);
				
		//System.out.println("Today Date in string format is "+ todayDateString);
		//System.out.println("Yesterday Date in string format is "+ yesterdayDateString);
		//System.out.println("Fifteen Days Ago Date in string format is "+ fifteenDaysAgoDateString);
		//System.out.println("Fifteen Days To Date in string format is "+ fifteenDaysToDateString);
		
		//Retrieving project information
		ProjectsEntity projectEntity=serviceProjects.getProjectByNumber(qproject);
				
		//Retrieving client information
		ClientsEntity client=serviceClients.getClientById(id);
		
		//Retrieving user information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving division
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		clientIdString=String.valueOf(id);
		
		//Updating list of assigned certifications
		service.reviewCerts(clientIdString,qperiod);
		
		//Finally retrieving the list of assigned certifications
		List<AssigCertsEntity> list=service.getAllCertsByClientId(qperiod,clientIdString);
		
		//Getting the names for the assigned certificates
		for(AssigCertsEntity cert : list)
		{
			String certName=service.getCertName(cert.getCertNumber());
			
			//Setting the status for every field
			status=service.compareDates(cert.getRecordid(),todayDateString,qperiod);
			
			//Setting the priznak of expiration in the next fifteen days
			priznakSoonExpiration=service.expireIn15days(cert.getRecordid(),fifteenDaysToDateString,qperiod);
			
			if(cert.getExpirationDate()==null)
			{
				cert.setStatus(null);
				cert.setAboutToExpire(null);
			}
			else
			{	
			
				if(status.equals("Expired"))
					cert.setAboutToExpire(null);
				else
					cert.setAboutToExpire(priznakSoonExpiration);
				
				cert.setStatus(status);
			}
			
			
			cert.setCertName(certName);
			
			
		}
		
		model.addAttribute("client",client);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		model.addAttribute("qproject",qproject);
		
		model.addAttribute("certs",list);
		model.addAttribute("project",projectEntity);
			
		return "assigCertsList";
				
	}
	
	@RequestMapping(path="/delete", method=RequestMethod.POST)
	public String deleteCertById(Model model, Long id, Long userId,Long quserId,String qperiod,Long qdivisionId,String qproject) throws RecordNotFoundException
	{
		LogsEntity log=new LogsEntity();
		
		//Retrieving assigned project
		AssigCertsEntity assignCert=service.getCertById(id);
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving user
		UsersEntity user=serviceUsers.getUserById(userId);
		
		String message="Item was removed from the list...";
		
		service.deleteCert(id);
		
		log.setSubject(quser.getEmail());
		log.setAction("Removing assigned certification from "+ user.getUsername());
		log.setObject(assignCert.getCertNumber());
		
		serviceLogs.saveLog(log);
		
		model.addAttribute("message",message);
		
		model.addAttribute("userId",userId);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qproject",qproject);
		model.addAttribute("qdivisionId",qdivisionId);
		
		return "assigCertsRedirect";
		
		
	}
	
	@RequestMapping(path="/createCert", method=RequestMethod.POST)
	public String createCert(Model model,AssigCertsEntity assigCert,Long quserId,Long userId,String qperiod,Long qdivisionId,String qproject) throws RecordNotFoundException
	{
		String userIdString=null;
				
		userIdString=String.valueOf(userId);
		
		//System.out.println("Inside the controller to update or create. Object is: "+ project);
		LogsEntity log=new LogsEntity();
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving user
		UsersEntity user=serviceUsers.getUserById(userId);
		
				
		//Finalizing to set up the entity
		assigCert.setClientId(userIdString);
		assigCert.setPeriod(qperiod);
									
		String message="Certificates List was updated...";
		
		service.createCert(assigCert);
		
		log.setSubject(quser.getEmail());
		log.setAction("Assigning new certificate to "+ user.getUsername());
		log.setObject(assigCert.getCertNumber());
		
		serviceLogs.saveLog(log);
		
					
		model.addAttribute("message",message);
		
		model.addAttribute("userId",userId);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		model.addAttribute("qproject",qproject);
		
		return "assigCertsRedirect";
			
	}
	
	@RequestMapping(path="/edit", method=RequestMethod.POST)
	public String editCertById(Model model,Long id,Long quserId,String qperiod,Long qdivisionId,String qproject) throws RecordNotFoundException 
	{
		String clientIdString=null;
		String certNameLocal=null;
		String message=null;
		
		List<String> optionsLongList=new ArrayList<String>();  
		List<String> optionsShortList=new ArrayList<String>(); 
		
		List<CertificatesEntity> availablesList=new ArrayList<CertificatesEntity>();
						
		//Retrieving quser information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		//Retrieving client information
		ClientsEntity client=serviceClients.getClientById(id);
		
		//Retrieving list of active certificates
		List<CertificatesEntity> listCerts=serviceCertificates.getActives();
		
		//Converting active certificates to list of options
		for(CertificatesEntity certificate : listCerts)
		{
			optionsLongList.add(certificate.getCertNumber());
			
		}
		
		System.out.println("The long list options is "+ optionsLongList);	
		
		clientIdString=String.valueOf(id);
		
		//Finally retrieving the list of already assigned certifications
		List<AssigCertsEntity> listAssigCerts=service.getAllCertsByClientId(qperiod,clientIdString);
		
				
		//Getting the names for the assigned certificates
		for(AssigCertsEntity cert : listAssigCerts)
		{
			String certName=service.getCertName(cert.getCertNumber());
					
			//System.out.println("found name of the cert is "+ certName);
					
			cert.setCertName(certName);
					
		}
		
		//Converting active certificates to short list of options
		for(AssigCertsEntity certificate : listAssigCerts)
		{
			optionsShortList.add(certificate.getCertNumber());
			
		}
				
		System.out.println("The short list options is "+ optionsShortList);	
		
		//Removing duplicates from one list using the ones from other list	
		optionsLongList.removeAll(optionsShortList);
		
		if(optionsLongList.isEmpty())
		{
			message="No options available, seems like all certs were already assigned to this employee...";
		}
		else
		{	
		
			//Creating the list of available certificates for current user from a list of strings
			for(String option : optionsLongList)
			{
				CertificatesEntity cert=new CertificatesEntity();
				
				cert.setCertNumber(option);
				
				certNameLocal=service.getCertName(option);
				
				cert.setCertName(certNameLocal);
				
				availablesList.add(cert);
				
			}
			
			//Getting the names for the resulting list of certificates
			for(CertificatesEntity cert : availablesList)
			{
				String certName=service.getCertName(cert.getCertNumber());
						
				//System.out.println("found name of the cert is "+ certName);
						
				cert.setCertName(certName);
						
			}
		}
		
		System.out.println("The resulting list of certs "+ optionsLongList);
		System.out.println("The resulting list of options "+ availablesList);
		
		
		model.addAttribute("cert",new AssigCertsEntity());
				
		model.addAttribute("options",optionsLongList);
		model.addAttribute("assigCerts",listAssigCerts);
		
		model.addAttribute("client",client);
		model.addAttribute("message",message);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		model.addAttribute("qproject",qproject);
		
		return "assigCertsAdd";
	}

	@RequestMapping(path="/modify", method=RequestMethod.POST)
	public String modifyCert(Model model,Long id,Long quserId,String qperiod,Long qdivisionId,String qproject,String clientId) throws RecordNotFoundException 
	{
		Long clientIdLong=null;
				
		//Retrieving quser information
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving qdivision
		DivisionsEntity qdivision=serviceDivisions.getDivisionById(qdivisionId);
		
		clientIdLong=Long.parseLong(clientId);
		
		//Retrieving client information
		ClientsEntity client=serviceClients.getClientById(clientIdLong);
		
		//Retrieving assigCert information
		AssigCertsEntity cert=service.getCertById(id);
		
		//Retrieving name of the certificate
		String certName=serviceCertificates.getCertName(cert.getCertNumber());
				
						
		model.addAttribute("certName",certName);				
		model.addAttribute("client",client);
		model.addAttribute("cert",cert);
		
		model.addAttribute("quser",quser);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivision",qdivision);
		model.addAttribute("qproject",qproject);
		
		return "assigCertsMod";
	}
	
	@RequestMapping(path="/updateCert", method=RequestMethod.POST)
	public String updateCertificate(Model model,Long quserId,Long clientId,String qperiod,Long qdivisionId,String qproject,Long id,String notes,String expirationDate) throws RecordNotFoundException
	{
				
		//System.out.println("Inside the controller to update or create. Object is: "+ project);
		LogsEntity log=new LogsEntity();
		
		//Retrieving quser
		UsersEntity quser=serviceUsers.getUserById(quserId);
		
		//Retrieving user
		ClientsEntity client=serviceClients.getClientById(clientId);
		
		//Retrieving assigCert information
		AssigCertsEntity cert=service.getCertById(id);
		
		//Data send for update
		//System.out.println("Date sent to be updated: recordId="+ id +" ExpirationDate="+ expirationDate +" notes="+ notes);
						
		//Updating the record
		service.updateCert(id,expirationDate,notes);
									
		String message="Certificate record was updated...";
						
		log.setSubject(quser.getEmail());
		log.setAction("Updating certificate for "+ client.getCname());
		log.setObject("Certificate number is: "+ cert.getCertNumber());
		
		serviceLogs.saveLog(log);
							
		model.addAttribute("message",message);
					
		model.addAttribute("clientId",clientId);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		model.addAttribute("qproject",qproject);
		
		return "assigCertsRedirect";
			
	}
	
	
	@RequestMapping(path="/voidCert", method=RequestMethod.POST)
	public String voidCertificate(Model model,Long quserId,Long clientId,String qperiod,Long qdivisionId,String qproject,Long id) throws RecordNotFoundException
	{
									
		String message="This option is not available, please contact to your System Administrator for more information...";
						
									
		model.addAttribute("message",message);
					
		model.addAttribute("clientId",clientId);
		
		model.addAttribute("quserId",quserId);
		model.addAttribute("qperiod",qperiod);
		model.addAttribute("qdivisionId",qdivisionId);
		model.addAttribute("qproject",qproject);
		
		return "assigCertsRedirect";
			
	}
	
}
