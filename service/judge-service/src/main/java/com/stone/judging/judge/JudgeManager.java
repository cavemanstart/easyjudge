package com.stone.judging.judge;
import com.stone.judging.judge.strategy.DefaultJudgeStrategy;
import com.stone.judging.judge.strategy.JavaLanguageJudgeStrategy;
import com.stone.judging.judge.strategy.JudgeContext;
import com.stone.judging.judge.strategy.JudgeStrategy;
import com.stone.model.codesandbox.JudgeInfo;
import com.stone.model.entity.ProblemSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        ProblemSubmit problemSubmit = judgeContext.getProblemSubmit();
        String language = problemSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
