package com.example.springbatch.ex_jobexplorer;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor //생성자 DI를 생성자를 안만들어도 사용할 수 있게해주는 롬복 어노테이션
@Configuration
public class ExploreJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobExplorer jobExplorer;

    @Bean
    public Tasklet explorerTasklet() {
        return new ExploringTasklet(this.jobExplorer);
    }

    @Bean
    public Step explorerStep() {
        return this.stepBuilderFactory.get("explorerStep")
                .tasklet(explorerTasklet())
                .build();
    }

    @Bean
    public Job explorerJob(){
        return this.jobBuilderFactory.get("explorerJob")
                .start(explorerStep())
                .build();
    }


}
