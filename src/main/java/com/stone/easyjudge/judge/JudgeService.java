package com.stone.easyjudge.judge;


import com.stone.easyjudge.model.entity.ProblemSubmit;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     * @param problemSubmitId
     * @return
     */
    ProblemSubmit doJudge(long problemSubmitId);
}
