package com.stone.question.controller.inner;

import com.stone.model.entity.Question;
import com.stone.model.entity.QuestionSubmit;
import com.stone.question.service.QuestionService;
import com.stone.question.service.QuestionSubmitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @GetMapping("/get/{questionId}")
    public Question getQuestionById(@PathVariable long questionId) {
        return questionService.getById(questionId);
    }

    @GetMapping("/question_submit/get/{questionSubmitId}")
    public QuestionSubmit getQuestionSubmitById(@PathVariable long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

    @GetMapping("/addSubmit/{questionId}")
    public boolean addSubmit(@PathVariable long questionId){
        return questionService.addSubmit(questionId);
    }

    @GetMapping("/addAccept/{questionId}")
    public boolean addAccept(@PathVariable long questionId){
        return questionService.addAccept(questionId);
    }
    @GetMapping("/getQuestionSubmitStatus/{questionSubmitId}")
    public String getQuestionSubmitStatus(@PathVariable long questionSubmitId){
        return questionSubmitService.getQuestionSubmitStatus(questionSubmitId);
    }
}
