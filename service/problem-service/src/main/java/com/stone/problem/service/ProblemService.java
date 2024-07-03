package com.stone.problem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stone.model.dto.problem.ProblemQueryRequest;
import com.stone.model.entity.Problem;
import com.stone.model.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;

public interface ProblemService extends IService<Problem> {


    /**
     * 校验
     *
     * @param problem
     * @param add
     */
    void validProblem(Problem problem, boolean add);

    /**
     * 获取查询条件
     *
     * @param problemQueryRequest
     * @return
     */
    QueryWrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest);
    
    /**
     * 获取题目封装
     *
     * @param problem
     * @param request
     * @return
     */
    ProblemVO getProblemVO(Problem problem, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param problemPage
     * @param request
     * @return
     */
    Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, HttpServletRequest request);
    
}
