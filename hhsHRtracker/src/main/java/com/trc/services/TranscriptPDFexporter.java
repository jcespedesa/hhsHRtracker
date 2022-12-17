package com.trc.services;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
 
import javax.servlet.http.HttpServletResponse;
 
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.trc.entities.ClientATsEntity;
import com.trc.entities.ClientsEntity;

public class TranscriptPDFexporter 
{
	private List<ClientATsEntity> listTrainings;
	private ClientsEntity employee;
    
    public TranscriptPDFexporter(List<ClientATsEntity> listTrainings,ClientsEntity employee) 
    {
        this.listTrainings=listTrainings;
        this.employee=employee;
    }
    
    
    private void writeTableHeader(PdfPTable table) 
    {
    	
        PdfPCell cell=new PdfPCell();
        cell.setBackgroundColor(Color.GRAY);
        cell.setPadding(5);
         
        Font font=FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
         
        cell.setPhrase(new Phrase("Training",font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Completed? Y/N",font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Date Completion",font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Certificate Received? Y/N", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Date Received",font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Mandatory Staff",font));
        table.addCell(cell);  
        
        cell.setPhrase(new Phrase("Time Frame",font));
        table.addCell(cell);  
    }
    
    
    private void writeTableData(PdfPTable table) 
    {
        for(ClientATsEntity clientAT : listTrainings) 
        {
            table.addCell(clientAT.getBufferName());
            table.addCell(clientAT.getCompleted());
            table.addCell(clientAT.getDateCompletion());
            table.addCell(clientAT.getCertReceived());
            table.addCell(clientAT.getDateCertRec());
            table.addCell("");
            table.addCell(clientAT.getPriznakAdd());
        }
    }
    
    private void writeTableInfo(PdfPTable tableInfo) 
    {
    	
    	PdfPCell cell=new PdfPCell();
        cell.setPadding(10);
         
        Font font=FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.GRAY);
         
        cell.setPhrase(new Phrase("Name of Staff",font));
        tableInfo.addCell(cell);
       
    	tableInfo.addCell(employee.getCname());
    	
    	cell.setPhrase(new Phrase("Date of Hire",font));
        tableInfo.addCell(cell);
    	
        tableInfo.addCell(employee.getDateHire());
        
        cell.setPhrase(new Phrase("Contract Number",font));
        tableInfo.addCell(cell);
                
        tableInfo.addCell(employee.getContract());
        
        cell.setPhrase(new Phrase("Contract Period",font));
        tableInfo.addCell(cell);
        
        tableInfo.addCell("");
            
       
    }
    
    
    public void export(HttpServletResponse response) throws DocumentException, IOException 
	{
        Document document=new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
         
        document.open();
        
        
        Font fontHeader=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontHeader.setSize(12);
        fontHeader.setColor(Color.GRAY);
                
        Font fontSubTitle=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontSubTitle.setSize(11);
        fontSubTitle.setColor(Color.BLACK);
        
        
        Paragraph pHeader=new Paragraph("The Community Partnership for the Prevention of Homelessness  \r\n"+ 
        		"Staff Training Transcript – Low Barrier Shelter ",fontHeader);
        pHeader.setAlignment(Paragraph.ALIGN_RIGHT);
        pHeader.setSpacingBefore(20);
        pHeader.setSpacingAfter(20);
         
        Paragraph pSubTitle=new Paragraph("Staff Trainings",fontSubTitle);
        pSubTitle.setAlignment(Paragraph.ALIGN_CENTER);
        pSubTitle.setSpacingBefore(20);
        
        Paragraph pSubTitle2=new Paragraph("Staff Information",fontSubTitle);
        pSubTitle2.setAlignment(Paragraph.ALIGN_CENTER);
        pSubTitle2.setSpacingBefore(20);
        
        document.add(pHeader);
        document.add(pSubTitle2);
         
        PdfPTable tableInfo=new PdfPTable(4);
        tableInfo.setWidthPercentage(100f);
        tableInfo.setWidths(new float[] {2.5f,3.5f,2.5f,3.5f});
        tableInfo.setSpacingBefore(10);
         
        writeTableInfo(tableInfo);
                
        document.add(tableInfo);
        
        document.add(pSubTitle);
         
        PdfPTable table=new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.5f, 2.0f, 2.0f, 2.0f, 2.0f,2.0f,1.5f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);
        writeTableData(table);
         
        document.add(table);
        
        document.close();
         
    }
    
    
    
}
