package com.stone.easyjudge.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stone.easyjudge.common.ErrorCode;
import com.stone.easyjudge.constant.CommonConstant;
import com.stone.easyjudge.exception.BusinessException;
import com.stone.easyjudge.exception.ThrowUtils;
import com.stone.easyjudge.mapper.ProblemMapper;
import com.stone.easyjudge.model.dto.problem.ProblemQueryRequest;
import com.stone.easyjudge.model.entity.Problem;
import com.stone.easyjudge.model.entity.User;
import com.stone.easyjudge.model.vo.ProblemVO;
import com.stone.easyjudge.model.vo.UserVO;
import com.stone.easyjudge.service.ProblemService;
import com.stone.easyjudge.service.UserService;
import com.stone.easyjudge.utils.SqlUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author xy156
* @description 针对表【problem(题目)】的数据库操作Service实现
* @createDate 2024-06-28 21:57:55
*/
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem>
    implements ProblemService {
    @Resource
    private UserService userService;
    @Override
    public void validProblem(Problem problem, boolean add) {
        if (problem == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = problem.getTitle();
        String content = problem.getContent();
        String tags = problem.getTags();
        String answer = problem.getSolution();
        String judgeCase = problem.getJudgeCase();
        String judgeConfig = problem.getJudgeConfig();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, content, tags), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
        if (StringUtils.isNotBlank(answer) && answer.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案过长");
        }
        if (StringUtils.isNotBlank(judgeCase) && judgeCase.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        if (StringUtils.isNotBlank(judgeConfig) && judgeConfig.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题配置过长");
        }
    }

    @Override
    public ProblemVO getProblemVO(Problem problem, HttpServletRequest request) {
        ProblemVO problemVO = ProblemVO.objToVo(problem);
        // 1. 关联查询用户信息
        Long userId = problem.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        problemVO.setUserVO(userVO);
        return problemVO;
    }

    @Override
    public Wrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        if (problemQueryRequest == null) {
            return queryWrapper;
        }
        Long id = problemQueryRequest.getId();
        String title = problemQueryRequest.getTitle();
        String content = problemQueryRequest.getContent();
        List<String> tags = problemQueryRequest.getTags();
        String answer = problemQueryRequest.getSolution();
        Long userId = problemQueryRequest.getUserId();
        String sortField = problemQueryRequest.getSortField();
        String sortOrder = problemQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        queryWrapper.like(StringUtils.isNotBlank(content), "content", content);
        queryWrapper.like(StringUtils.isNotBlank(answer), "answer", answer);
        if (CollectionUtils.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, HttpServletRequest request) {
        List<Problem> problemList = problemPage.getRecords();
        Page<ProblemVO> problemVOPage = new Page<>(problemPage.getCurrent(), problemPage.getSize(), problemPage.getTotal());
        if (CollectionUtils.isEmpty(problemList)) {
            return problemVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = problemList.stream().map(Problem::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<ProblemVO> problemVOList = problemList.stream().map(problem -> {
            ProblemVO problemVO = ProblemVO.objToVo(problem);
            Long userId = problem.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            problemVO.setUserVO(userService.getUserVO(user));
            return problemVO;
        }).collect(Collectors.toList());
        problemVOPage.setRecords(problemVOList);
        return problemVOPage;
    }
}




