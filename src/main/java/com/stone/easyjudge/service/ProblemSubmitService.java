package com.stone.easyjudge.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stone.easyjudge.model.dto.problemsubmit.ProblemSubmitAddRequest;
import com.stone.easyjudge.model.dto.problemsubmit.ProblemSubmitQueryRequest;
import com.stone.easyjudge.model.entity.ProblemSubmit;
import com.stone.easyjudge.model.entity.User;
import com.stone.easyjudge.model.vo.ProblemSubmitVO;

/**
* @author xy156
* @description 针对表【problem_submit(题目提交)】的数据库操作Service
* @createDate 2024-06-28 21:58:15
*/
public interface ProblemSubmitService extends IService<ProblemSubmit> {

    long doProblemSubmit(ProblemSubmitAddRequest problemSubmitAddRequest, User loginUser);

    Wrapper<ProblemSubmit> getQueryWrapper(ProblemSubmitQueryRequest problemSubmitQueryRequest);

    ProblemSubmitVO getProblemSubmitVO(ProblemSubmit problemSubmit, User loginUser);
    Page<ProblemSubmitVO> getProblemSubmitVOPage(Page<ProblemSubmit> problemSubmitPage, User loginUser);
}
