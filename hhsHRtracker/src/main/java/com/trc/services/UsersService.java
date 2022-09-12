package com.trc.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trc.entities.ClientsEntity;
import com.trc.entities.ProjectsEntity;
import com.trc.entities.UsersEntity;
import com.trc.repositories.AssigCertsRepository;
import com.trc.repositories.ClientsRepository;
import com.trc.repositories.ProjectsRepository;
import com.trc.repositories.UsersRepository;

@Service
public class UsersService 
{

	@Autowired
	UsersRepository repository;
	
	@Autowired
	ProjectsRepository repositoryProjects;
	
	@Autowired
	ClientsRepository repositoryClients;
	
	@Autowired
	AssigCertsRepository repositoryAssigCerts;
	
	public List<UsersEntity> getAllUsers()
	{
		List<UsersEntity> result=(List<UsersEntity>) repository.findAll();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<UsersEntity>();
		
	}
	
	public List<UsersEntity> getAllByName()
	{
		List<UsersEntity> result=(List<UsersEntity>) repository.getAllByUsername();
		
		if(result.size() > 0)
			return result;
		else
			return new ArrayList<UsersEntity>();
		
	}
	
	public UsersEntity getUserById(Long id) throws RecordNotFoundException
	{
		Optional<UsersEntity> user=repository.findById(id);
		
		if(user.isPresent())
			return user.get();
		else
			throw new RecordNotFoundException("No record exist for given id");
	}
	
	public UsersEntity createOrUpdate(UsersEntity entity)
	{
		if(entity.getUserid()==null)
		{
			entity=repository.save(entity);
			
			return entity;
		}
		else
		{
			Optional<UsersEntity> user=repository.findById(entity.getUserid());
			
			if(user.isPresent())
			{
				
				UsersEntity newEntity=user.get();
				
				newEntity.setUsername(entity.getUsername());
				newEntity.setEmail(entity.getEmail());
				newEntity.setRole(entity.getRole());				
				newEntity.setActive(entity.getActive());
				newEntity.setPassword(entity.getPassword());
				newEntity=repository.save(newEntity);
				
				return newEntity;
				
			}
			else
			{
				entity=repository.save(entity);
				
				return entity;
				
			}
			
		}
				
		
	}
	
	public void deleteUserById(Long id) throws RecordNotFoundException
	{
		Optional<UsersEntity> user=repository.findById(id);
		
		if(user.isPresent())
			repository.deleteById(id);
		else
			throw new RecordNotFoundException("No user record exist for given id");
		
		
	}
	
	public UsersEntity getUserByEmail(String Email)
	{
		UsersEntity user=repository.getUserByEmail(Email);
		
		return user;
	}
	
	public String checkString(String str) 
	{
		
	    int i=0;
		
		char ch;
	    
	    boolean capitalFlag=false;
	    boolean numberFlag=false;
	    
	    for(i=0;i<str.length();i++) 
	    {
	        ch=str.charAt(i);
	        
	        if(Character.isDigit(ch)) 
	        {
	            numberFlag=true;
	        }
	        else 
	        	if(Character.isUpperCase(ch)) 
	        	{
	        		capitalFlag=true;
	        		
	        	} 
	        	if(numberFlag && capitalFlag)
	        		return "true";
	      }
	      return "false";
	}

	
	public void changePass(Long id, String pass)
	{
		repository.changeUserPass(id,pass);
		
	}
	
	public int findDuplicates(String email)
	{
		int priznakDuplicate=0;
		
		priznakDuplicate=repository.findEmailDuplicity(email);
		
		
		return priznakDuplicate;
	}
	
	public String getPassByEmail(String email) 
    {
    	String storedPass="";
    	    	
        storedPass=repository.findPassByEmail(email);
         
        return storedPass;
    }
	
	public String createAccessCode(Long id)
	{
		int min=100000;
		int max=999999;
		int passInt=0;
		
		String passNum=null;
		String passString=null;
		String pass=null;
		
		Random r=new Random();
        passInt=r.nextInt((max-min)+1)+ min;
        
      //Converting the password int to password string
		passNum=Integer.toString(passInt);
		
		//Trying to get a random symbol
		passString=this.generateRandomSymbol();
		
		//Concatenation of the password
		pass=passNum+passString;
		
		return pass;
	}
	
	public String generateRandomSymbol() 
    {
    	String[] arr={"!","@","#","$","%","*","&","?"};
    	
    	int idx=new Random().nextInt(arr.length);
    	String s=(arr[idx]);
    	
    	
        return s;
    }
	
	public List<ProjectsEntity> getProjectsWithExpCerts(String divisionNumber,String period) 
    {
		int numExpCerts=0;
		
		String todayDateString=null;
						
		List<ProjectsEntity> projects=repositoryProjects.getAllProjByDiv(divisionNumber);
		
		//Trying to get todayDate
		java.sql.Date todayDate=new java.sql.Date(System.currentTimeMillis());
		
		//System.out.println("Today Date is "+ todayDate);
		
		//Converting date to string
		DateFormat todayDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		todayDateString=todayDateFormat.format(todayDate); 
		
		//System.out.println("Today Date String is "+ todayDateString);
		//System.out.println("Current Period is "+ period);
		
		for(ProjectsEntity project : projects)
		{
			//Checking if every project is having expired certs for current period
			numExpCerts=this.getExpiredCertsByProject(project.getProject(),period,todayDateString);
			
			//System.out.println(numExpCerts);
			
			project.setNumExpCerts(numExpCerts);
					
		}
		         
        return projects;
    }
	
	
	public int getExpiredCertsByProject(String project,String period,String todayDate) 
    {
		int totalExpCerts=0;
		int expCerts=0;
		
		String clientIdString=null;
		
		List<ClientsEntity> clients=repositoryClients.getAllByProject(project);
		
		//System.out.println("Revised projects "+ project);
		
		for(ClientsEntity client : clients)
		{
			clientIdString=String.valueOf(client.getClientid());
			
			//Checking if each client is having an expired cert
			expCerts=repositoryAssigCerts.countExpiredRecordsById(clientIdString,todayDate,period);
			
			totalExpCerts=totalExpCerts + expCerts;
					
		}
		
		return totalExpCerts;
				
    }
	
	
}
