package com.stone.easyjudge.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stone.easyjudge.common.ErrorCode;
import com.stone.easyjudge.constant.CommonConstant;
import com.stone.easyjudge.exception.BusinessException;
import com.stone.easyjudge.judge.JudgeService;
import com.stone.easyjudge.mapper.ProblemSubmitMapper;
import com.stone.easyjudge.model.dto.problemsubmit.ProblemSubmitAddRequest;
import com.stone.easyjudge.model.dto.problemsubmit.ProblemSubmitQueryRequest;
import com.stone.easyjudge.model.entity.Problem;
import com.stone.easyjudge.model.entity.ProblemSubmit;
import com.stone.easyjudge.model.entity.User;
import com.stone.easyjudge.model.enums.ProblemSubmitLanguageEnum;
import com.stone.easyjudge.model.enums.ProblemSubmitStatusEnum;
import com.stone.easyjudge.model.vo.ProblemSubmitVO;
import com.stone.easyjudge.service.ProblemService;
import com.stone.easyjudge.service.ProblemSubmitService;
import com.stone.easyjudge.service.UserService;
import com.stone.easyjudge.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author xy156
* @description 针对表【problem_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-06-28 21:58:15
*/
@Service
public class ProblemSubmitServiceImpl extends ServiceImpl<ProblemSubmitMapper, ProblemSubmit>
    implements ProblemSubmitService {
    @Resource
    private ProblemService problemService;
    @Resource
    private UserService userService;
    @Resource
    private JudgeService judgeService;
    @Override
    public long doProblemSubmit(ProblemSubmitAddRequest problemSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = problemSubmitAddRequest.getLanguage();
        ProblemSubmitLanguageEnum languageEnum = ProblemSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long problemId = problemSubmitAddRequest.getProblemId();
        // 判断实体是否存在，根据类别获取实体
        Problem problem = problemService.getById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();
        // 每个用户串行提交题目
        ProblemSubmit problemSubmit = new ProblemSubmit();
        problemSubmit.setUserId(userId);
        problemSubmit.setProblemId(problemId);
        problemSubmit.setCode(problemSubmitAddRequest.getCode());
        problemSubmit.setLanguage(language);
        // 设置初始状态
        problemSubmit.setStatus(ProblemSubmitStatusEnum.WAITING.getValue());
        problemSubmit.setJudgeInfo("{}");
        boolean save = this.save(problemSubmit);
        if (!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long problemSubmitId = problemSubmit.getId();
        // 执行判题服务
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(problemSubmitId);
        });
        return problemSubmitId;
    }

    @Override
    public Wrapper<ProblemSubmit> getQueryWrapper(ProblemSubmitQueryRequest problemSubmitQueryRequest) {
        QueryWrapper<ProblemSubmit> queryWrapper = new QueryWrapper<>();
        if (problemSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = problemSubmitQueryRequest.getLanguage();
        Integer status = problemSubmitQueryRequest.getStatus();
        Long problemId = problemSubmitQueryRequest.getProblemId();
        Long userId = problemSubmitQueryRequest.getUserId();
        String sortField = problemSubmitQueryRequest.getSortField();
        String sortOrder = problemSubmitQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(problemId), "problemId", problemId);
        queryWrapper.eq(ProblemSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
    @Override
    public ProblemSubmitVO getProblemSubmitVO(ProblemSubmit problemSubmit, User loginUser) {
        ProblemSubmitVO problemSubmitVO = ProblemSubmitVO.objToVo(problemSubmit);
        // 脱敏：仅本人和管理员能看见自己（提交 userId 和登录用户 id 不同）提交的代码
        long userId = loginUser.getId();
        // 处理脱敏
        if (userId != problemSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            problemSubmitVO.setCode(null);
        }
        return problemSubmitVO;
    }
    @Override
    public Page<ProblemSubmitVO> getProblemSubmitVOPage(Page<ProblemSubmit> problemSubmitPage, User loginUser) {
        List<ProblemSubmit> problemSubmitList = problemSubmitPage.getRecords();
        Page<ProblemSubmitVO> problemSubmitVOPage = new Page<>(problemSubmitPage.getCurrent(), problemSubmitPage.getSize(), problemSubmitPage.getTotal());
        if (CollectionUtils.isEmpty(problemSubmitList)) {
            return problemSubmitVOPage;
        }
        List<ProblemSubmitVO> problemSubmitVOList = problemSubmitList.stream()
                .map(problemSubmit -> getProblemSubmitVO(problemSubmit, loginUser))
                .collect(Collectors.toList());
        problemSubmitVOPage.setRecords(problemSubmitVOList);
        return problemSubmitVOPage;
    }
}




