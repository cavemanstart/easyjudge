package com.stone.judging.rabbitmq;

import com.rabbitmq.client.Channel;
import com.stone.feign.question.QuestionFeignClient;
import com.stone.judging.judge.JudgeService;
import com.stone.judging.redis.RedisDistributedLock;
import com.stone.model.enums.QuestionSubmitStatusEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyMessageConsumer {

    @Resource
    private JudgeService judgeService;
    @Resource
    private QuestionFeignClient questionFeignClient;

    // 指定程序监听的消息队列和确认机制
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receiveMessage message = {}", message);
        long questionSubmitId = Long.parseLong(message);

        try {
            String questionSubmitStatus = questionFeignClient.getQuestionSubmitStatus(questionSubmitId);
            RedisDistributedLock redisDistributedLock = new RedisDistributedLock(String.valueOf(questionSubmitId),"default");
            boolean lock = redisDistributedLock.lock();
            if(lock){
                judgeService.doJudge(questionSubmitId);
                channel.basicAck(deliveryTag, false);
            }
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, false);//不重新入队
        }
    }

}