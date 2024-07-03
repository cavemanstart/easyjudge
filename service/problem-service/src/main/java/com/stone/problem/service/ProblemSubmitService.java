package com.stone.problem.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stone.model.dto.problemsubmit.ProblemSubmitAddRequest;
import com.stone.model.dto.problemsubmit.ProblemSubmitQueryRequest;
import com.stone.model.entity.ProblemSubmit;
import com.stone.model.entity.User;
import com.stone.model.vo.ProblemSubmitVO;

public interface ProblemSubmitService extends IService<ProblemSubmit> {
    
    /**
     * 题目提交
     *
     * @param problemSubmitAddRequest 题目提交信息
     * @param loginUser
     * @return
     */
    long doProblemSubmit(ProblemSubmitAddRequest problemSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param problemSubmitQueryRequest
     * @return
     */
    QueryWrapper<ProblemSubmit> getQueryWrapper(ProblemSubmitQueryRequest problemSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param problemSubmit
     * @param loginUser
     * @return
     */
    ProblemSubmitVO getProblemSubmitVO(ProblemSubmit problemSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param problemSubmitPage
     * @param loginUser
     * @return
     */
    Page<ProblemSubmitVO> getProblemSubmitVOPage(Page<ProblemSubmit> problemSubmitPage, User loginUser);
}
