package com.stone.judging.judge;


import com.stone.model.entity.ProblemSubmit;

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
