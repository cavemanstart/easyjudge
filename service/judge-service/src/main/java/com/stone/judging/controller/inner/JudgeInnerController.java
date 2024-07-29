package com.stone.judging.controller.inner;
import com.stone.judging.judge.JudgeService;
import com.stone.judging.service.AsyncService;
import com.stone.model.entity.QuestionSubmit;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController{
    @Resource
    private JudgeService judgeService;
    @Resource
    private AsyncService<QuestionSubmit> asyncService;
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        QuestionSubmit questionSubmit = null;
        Callable<QuestionSubmit> task = ()-> judgeService.doJudge(questionSubmitId);
        Future<QuestionSubmit> questionSubmitFuture = asyncService.submitAsyncTask(task);
        try {
            questionSubmit = questionSubmitFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questionSubmit;
    }
}
