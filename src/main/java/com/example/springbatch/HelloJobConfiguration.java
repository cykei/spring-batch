package com.example.springbatch;

import com.example.springbatch.validator.ParameterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@RequiredArgsConstructor //생성자 DI를 생성자를 안만들어도 사용할 수 있게해주는 롬복 어노테이션
@Configuration
public class HelloJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public CompositeJobParametersValidator validator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();
        DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator();
        defaultJobParametersValidator.setRequiredKeys(new String[] {"filename"});
        defaultJobParametersValidator.setOptionalKeys(new String[] {"name"});
        defaultJobParametersValidator.afterPropertiesSet();
        validator.setValidators(Arrays.asList(new ParameterValidator(), defaultJobParametersValidator));
        return validator;
    }

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .start(helloStep1())
                .validator(validator())
                .incrementer(new RunIdIncrementer())
                //.next(helloStep2())
                .build();
    }

    @Bean
    public Step helloStep1(){
        return stepBuilderFactory.get("helloStep1")
                .tasklet(new Tasklet() {
                    // 디폴트 값이 무한 반복이다.
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("==============================");
                        System.out.println(" >> hello spring batch!");
                        System.out.println("name: " + chunkContext.getStepContext().getJobParameters().get("name"));
                        System.out.println("==============================");
                        return RepeatStatus.FINISHED; // 이렇게 리턴하거나 null을 리턴해야 한번 실행하면 끝난다.
                    }
                }).build();
    }


    @Bean
    public Step helloStep2() {
        return stepBuilderFactory.get("helloStep2")
                .tasklet(helloStep2Tasklet(null)).build();
    }

    @StepScope
    @Bean
    public Tasklet helloStep2Tasklet( @Value("#{jobParameters['name']}") String name){
        return (contribution, chunkContext) -> {
            System.out.println(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        };
    }
}
