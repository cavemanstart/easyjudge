package com.stone.judging.judge;

import cn.hutool.json.JSONUtil;
import com.stone.common.base.ErrorCode;
import com.stone.common.exception.BusinessException;
import com.stone.feign.problem.ProblemFeignClient;
import com.stone.judging.judge.codesandbox.CodeSandbox;
import com.stone.judging.judge.codesandbox.CodeSandboxFactory;
import com.stone.judging.judge.codesandbox.MyInvocationHandler;
import com.stone.judging.judge.codesandbox.impl.RemoteCodeSandbox;
import com.stone.judging.judge.strategy.JudgeContext;
import com.stone.model.codesandbox.ExecuteCodeRequest;
import com.stone.model.codesandbox.ExecuteCodeResponse;
import com.stone.model.codesandbox.JudgeInfo;
import com.stone.model.dto.problem.JudgeCase;
import com.stone.model.entity.Problem;
import com.stone.model.entity.ProblemSubmit;
import com.stone.model.enums.ProblemSubmitStatusEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private ProblemFeignClient problemFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public ProblemSubmit doJudge(long problemSubmitId) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        ProblemSubmit problemSubmit = problemFeignClient.getProblemSubmitById(problemSubmitId);
        if (problemSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long problemId = problemSubmit.getProblemId();
        Problem problem = problemFeignClient.getProblemById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!problemSubmit.getStatus().equals(ProblemSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行
        ProblemSubmit problemSubmitUpdate = new ProblemSubmit();
        problemSubmitUpdate.setId(problemSubmitId);
        problemSubmitUpdate.setStatus(ProblemSubmitStatusEnum.RUNNING.getValue());
        boolean update = problemFeignClient.updateProblemSubmitById(problemSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 4）调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newCodeSandboxInstance(RemoteCodeSandbox.class);
        String language = problemSubmit.getLanguage();
        String code = problemSubmit.getCode();
        // 获取输入用例
        String judgeCaseStr = problem.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler(codeSandbox);
        CodeSandbox codeSandboxProxy = (CodeSandbox) myInvocationHandler.getProxy();
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setProblem(problem);
        judgeContext.setProblemSubmit(problemSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6）修改数据库中的判题结果
        problemSubmitUpdate = new ProblemSubmit();
        problemSubmitUpdate.setId(problemSubmitId);
        problemSubmitUpdate.setStatus(ProblemSubmitStatusEnum.SUCCEED.getValue());
        problemSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = problemFeignClient.updateProblemSubmitById(problemSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        ProblemSubmit problemSubmitResult = problemFeignClient.getProblemSubmitById(problemId);
        return problemSubmitResult;
    }
}
