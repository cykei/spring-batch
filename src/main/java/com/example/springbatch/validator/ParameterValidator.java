package com.example.springbatch.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class ParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {

        String filename = parameters.getString("filename");
        if(!StringUtils.hasText(filename)){
            throw new JobParametersInvalidException("filename parameter is missing");
        }
        else if(!StringUtils.endsWithIgnoreCase(filename,"csv")) {
            throw new JobParametersInvalidException("filename parameter does not use the csv file extension");
        }
    }
}
