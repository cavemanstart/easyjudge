package com.stone.question.schedule;

import com.stone.feign.user.UserFeignClient;
import com.stone.question.service.QuestionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class CronTask {
    //@Scheduled(cron = "0/5 * * * * ?")
    //private void questionSubmitCheck(){
    //
    //}
}
