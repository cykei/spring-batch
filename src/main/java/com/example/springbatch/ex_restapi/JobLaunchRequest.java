package com.example.springbatch.ex_restapi;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Properties;

public class JobLaunchRequest {
    private String name;
    private Properties jobParameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties getJobParamsParameters() {
        return jobParameters;
    }

    public void setJobParamsParameters(Properties jobParameters) {
        this.jobParameters = jobParameters;
    }

    public JobParameters getJobParameters(){
        Properties properties = new Properties();
        properties.putAll(this.jobParameters);
        return new JobParametersBuilder(properties).toJobParameters();
    }
}
