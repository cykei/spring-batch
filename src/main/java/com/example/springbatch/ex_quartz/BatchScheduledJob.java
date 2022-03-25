package com.example.springbatch.ex_quartz;

import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

@RequiredArgsConstructor
public class BatchScheduledJob extends QuartzJobBean {
    private final Job job;
    private final JobExplorer jobExplorer;
    private final JobLauncher jobLauncher;

    @Override  //스케줄링 이벤트가 발생될때마다 한번씩 호출된다.
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        JobParameters jobParameters = new JobParametersBuilder(this.jobExplorer)
                .getNextJobParameters(this.job) // run.id 를 붙여서 JobParameters를 생성하고 Job정보를 통해 run.id를 변경한다.
                .toJobParameters();

        try {
            this.jobLauncher.run(this.job, jobParameters);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }
}
