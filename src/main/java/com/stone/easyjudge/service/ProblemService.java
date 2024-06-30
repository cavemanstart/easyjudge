package com.stone.easyjudge.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stone.easyjudge.model.dto.problem.ProblemQueryRequest;
import com.stone.easyjudge.model.entity.Problem;
import com.stone.easyjudge.model.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author xy156
* @description 针对表【problem(题目)】的数据库操作Service
* @createDate 2024-06-28 21:57:55
*/
public interface ProblemService extends IService<Problem> {

    void validProblem(Problem problem, boolean add);

    ProblemVO getProblemVO(Problem problem, HttpServletRequest request);

    Wrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest);

    Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, HttpServletRequest request);
}
