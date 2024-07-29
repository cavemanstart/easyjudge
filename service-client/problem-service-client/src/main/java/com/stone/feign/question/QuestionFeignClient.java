package com.stone.feign.question;

import com.stone.model.entity.Question;
import com.stone.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2023-08-07 20:58:00
*/
@FeignClient(name = "problem-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    @GetMapping("/get/{questionId}")
    Question getQuestionById(@PathVariable long questionId);

    @GetMapping("/question_submit/get/{questionSubmitId}")
    QuestionSubmit getQuestionSubmitById(@PathVariable long questionSubmitId);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);

    @GetMapping("/addSubmit/{questionId}")
    boolean addSubmit(@PathVariable long questionId);

    @GetMapping("/addAccept/{questionId}")
    boolean addAccept(@PathVariable long questionId);
}
