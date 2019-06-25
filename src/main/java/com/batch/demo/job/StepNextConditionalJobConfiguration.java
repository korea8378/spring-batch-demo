package com.batch.demo.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalStep1())
                    .on("FAILED")
                    .to(conditionalStep3())
                    .on("*")
                    .end()
                .from(conditionalStep1())
                    .on("*")
                    .to(conditionalStep2())
                    .next(conditionalStep3())
                    .on("*")
                    .end()
                .end()
                .build();
    }

    @Bean
    public Step conditionalStep1() {
        return stepBuilderFactory.get("conditionalStep1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step1");

                    //on이 캐치하는 상태값이 BatchStatus가 아닌 ExitStatus이다
                    //그래서 분기처리를 위해 상태값 조정이 필요하시다면 ExitStatus를 조정 해야한다.
//                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalStep2() {
        return stepBuilderFactory.get("conditionalStep2")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalStep3() {
        return stepBuilderFactory.get("conditionalStep3")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
