package com.stone.easyjudge.judge;
import com.stone.easyjudge.judge.codesandbox.model.JudgeInfo;
import com.stone.easyjudge.judge.strategy.DefaultJudgeStrategy;
import com.stone.easyjudge.judge.strategy.JavaLanguageJudgeStrategy;
import com.stone.easyjudge.judge.strategy.JudgeContext;
import com.stone.easyjudge.judge.strategy.JudgeStrategy;
import com.stone.easyjudge.model.entity.ProblemSubmit;
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
