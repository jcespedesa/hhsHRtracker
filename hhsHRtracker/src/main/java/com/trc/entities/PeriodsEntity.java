package com.trc.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="periods")
public class PeriodsEntity 
{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long periodid;
	
	@Column(name="period")
	private String period;
	
	@Column(name="active")
	private String active;
	
	@Column(name="udelnybes")
	private String udelnyBes;
	
		
	//Constructors
	
		@Override
		public String toString()
		{
			return "PeriodsEntity[periodid="+ periodid +",period="+ period +",active="+ active +",udelnyBes="+ udelnyBes +"]";				
			
		}

		public Long getPeriodid() 
		{
			return periodid;
		}

		public void setPeriodid(Long periodid) 
		{
			this.periodid=periodid;
		}

		public String getPeriod() 
		{
			return period;
		}

		public void setPeriod(String period) 
		{
			this.period=period;
		}

		public String getActive() 
		{
			return active;
		}

		public void setActive(String active) 
		{
			this.active=active;
		}

		public String getUdelnyBes() 
		{
			return udelnyBes;
		}

		public void setUdelnyBes(String udelnyBes) 
		{
			this.udelnyBes=udelnyBes;
		}

		
}
