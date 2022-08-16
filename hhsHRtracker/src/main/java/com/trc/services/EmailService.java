package com.trc.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService 
{
	
	private JavaMailSender javaMailSender;
	
	public EmailService(JavaMailSender javaMailSender) 
    {
        this.javaMailSender=javaMailSender;
    }

    public void sendMail(String toEmail, String subject, String message, String sender) 
    {

    	SimpleMailMessage mailMessage=new SimpleMailMessage();

        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailMessage.setFrom(sender);

        javaMailSender.send(mailMessage);
    }
    
}
