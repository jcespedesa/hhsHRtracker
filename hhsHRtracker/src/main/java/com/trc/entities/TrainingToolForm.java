package com.trc.entities;

import java.util.List;

public class TrainingToolForm 
{
	
    private List<TrainingsEntity> results;

    public TrainingToolForm(List<TrainingsEntity> results)
    {
        this.setResults(results);
    }

	public List<TrainingsEntity> getResults() 
	{
		return results;
	}

	public void setResults(List<TrainingsEntity> results) 
	{
		this.results=results;
	}
    
       
}
