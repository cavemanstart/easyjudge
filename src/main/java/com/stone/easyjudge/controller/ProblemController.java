package com.stone.easyjudge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.stone.easyjudge.annotation.AuthCheck;
import com.stone.easyjudge.common.BaseResponse;
import com.stone.easyjudge.common.DeleteRequest;
import com.stone.easyjudge.common.ErrorCode;
import com.stone.easyjudge.common.ResultUtils;
import com.stone.easyjudge.constant.UserConstant;
import com.stone.easyjudge.exception.BusinessException;
import com.stone.easyjudge.exception.ThrowUtils;
import com.stone.easyjudge.model.dto.problem.*;
import com.stone.easyjudge.model.dto.problemsubmit.ProblemSubmitAddRequest;
import com.stone.easyjudge.model.dto.problemsubmit.ProblemSubmitQueryRequest;
import com.stone.easyjudge.model.entity.Problem;
import com.stone.easyjudge.model.entity.ProblemSubmit;
import com.stone.easyjudge.model.entity.User;
import com.stone.easyjudge.model.vo.ProblemSubmitVO;
import com.stone.easyjudge.model.vo.ProblemVO;
import com.stone.easyjudge.service.ProblemService;
import com.stone.easyjudge.service.ProblemSubmitService;
import com.stone.easyjudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/problem")
@Slf4j
public class ProblemController {
    @Resource
    private ProblemService problemService;

    @Resource
    private UserService userService;

    @Resource
    private ProblemSubmitService problemSubmitService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param problemAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addProblem(@RequestBody ProblemAddRequest problemAddRequest, HttpServletRequest request) {
        if (problemAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemAddRequest, problem);
        List<String> tags = problemAddRequest.getTags();
        if (tags != null) {
            problem.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = problemAddRequest.getJudgeCase();
        if (judgeCase != null) {
            problem.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = problemAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            problem.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        problemService.validProblem(problem, true);
        User loginUser = userService.getLoginUser(request);
        problem.setUserId(loginUser.getId());
        problem.setFavourNum(0);
        problem.setThumbNum(0);
        boolean result = problemService.save(problem);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newProblemId = problem.getId();
        return ResultUtils.success(newProblemId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteproblem(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Problem oldproblem = problemService.getById(id);
        ThrowUtils.throwIf(oldproblem == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldproblem.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = problemService.removeById(id);
        return ResultUtils.success(b);
    }
    /**
     * 更新（仅管理员）
     *
     * @param problemUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateProblem(@RequestBody ProblemUpdateRequest problemUpdateRequest) {
        if (problemUpdateRequest == null || problemUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemUpdateRequest, problem);
        List<String> tags = problemUpdateRequest.getTags();
        if (tags != null) {
            problem.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = problemUpdateRequest.getJudgeCase();
        if (judgeCase != null) {
            problem.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = problemUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            problem.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        // 参数校验
        problemService.validProblem(problem, false);
        long id = problemUpdateRequest.getId();
        // 判断是否存在
        Problem oldProblem = problemService.getById(id);
        ThrowUtils.throwIf(oldProblem == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = problemService.updateById(problem);
        return ResultUtils.success(result);
    }
    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<Problem> getProblemById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        // 不是本人或管理员，不能直接获取所有信息
        if (!problem.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(problem);
    }
    /**
     * 根据 id 获取（脱敏）
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<ProblemVO> getProblemVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = problemService.getById(id);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(problemService.getProblemVO(problem, request));
    }
    /**
     * 分页获取列表（封装类）
     *
     * @param problemQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ProblemVO>> listProblemVOByPage(@RequestBody ProblemQueryRequest problemQueryRequest,
                                                               HttpServletRequest request) {
        long current = problemQueryRequest.getCurrent();
        long size = problemQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Problem> problemPage = problemService.page(new Page<>(current, size),
                problemService.getQueryWrapper(problemQueryRequest));
        return ResultUtils.success(problemService.getProblemVOPage(problemPage, request));
    }
    /**
     * 分页获取题目列表（仅管理员）
     *
     * @param problemQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Problem>> listProblemByPage(@RequestBody ProblemQueryRequest problemQueryRequest,
                                                           HttpServletRequest request) {
        long current = problemQueryRequest.getCurrent();
        long size = problemQueryRequest.getPageSize();
        Page<Problem> problemPage = problemService.page(new Page<>(current, size),
                problemService.getQueryWrapper(problemQueryRequest));
        return ResultUtils.success(problemPage);
    }
    /**
     * 编辑（用户）
     *
     * @param problemEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editProblem(@RequestBody ProblemEditRequest problemEditRequest, HttpServletRequest request) {
        if (problemEditRequest == null || problemEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemEditRequest, problem);
        List<String> tags = problemEditRequest.getTags();
        if (tags != null) {
            problem.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = problemEditRequest.getJudgeCase();
        if (judgeCase != null) {
            problem.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = problemEditRequest.getJudgeConfig();
        if (judgeConfig != null) {
            problem.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        // 参数校验
        problemService.validProblem(problem, false);
        User loginUser = userService.getLoginUser(request);
        long id = problemEditRequest.getId();
        // 判断是否存在
        Problem oldProblem = problemService.getById(id);
        ThrowUtils.throwIf(oldProblem == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldProblem.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = problemService.updateById(problem);
        return ResultUtils.success(result);
    }
    /**
     * 提交题目
     *
     * @param problemSubmitAddRequest
     * @param request
     * @return 提交记录的 id
     */
    @PostMapping("/problem_submit/do")
    public BaseResponse<Long> doProblemSubmit(@RequestBody ProblemSubmitAddRequest problemSubmitAddRequest,
                                              HttpServletRequest request) {
        if (problemSubmitAddRequest == null || problemSubmitAddRequest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long problemSubmitId = problemSubmitService.doProblemSubmit(problemSubmitAddRequest, loginUser);
        return ResultUtils.success(problemSubmitId);
    }

    /**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
     *
     * @param problemSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/problem_submit/list/page")
    public BaseResponse<Page<ProblemSubmitVO>> listProblemSubmitByPage(@RequestBody ProblemSubmitQueryRequest problemSubmitQueryRequest,
                                                                       HttpServletRequest request) {
        long current = problemSubmitQueryRequest.getCurrent();
        long size = problemSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<ProblemSubmit> problemSubmitPage = problemSubmitService.page(new Page<>(current, size),
                problemSubmitService.getQueryWrapper(problemSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        // 返回脱敏信息
        return ResultUtils.success(problemSubmitService.getProblemSubmitVOPage(problemSubmitPage, loginUser));
    }
}
