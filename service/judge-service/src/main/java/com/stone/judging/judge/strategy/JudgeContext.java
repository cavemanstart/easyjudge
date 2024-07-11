package com.stone.judging.judge.strategy;

import com.stone.model.codesandbox.JudgeInfo;
import com.stone.model.dto.problem.JudgeCase;
import com.stone.model.entity.Problem;
import com.stone.model.entity.ProblemSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Problem problem;

    private ProblemSubmit problemSubmit;

}
