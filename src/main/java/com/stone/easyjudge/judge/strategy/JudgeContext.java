package com.stone.easyjudge.judge.strategy;

import com.stone.easyjudge.judge.codesandbox.model.JudgeInfo;
import com.stone.easyjudge.model.dto.problem.JudgeCase;
import com.stone.easyjudge.model.entity.Problem;
import com.stone.easyjudge.model.entity.ProblemSubmit;
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
