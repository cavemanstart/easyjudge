package com.stone.question.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stone.model.dto.question.QuestionFavourRequest;
import com.stone.model.entity.Question;
import com.stone.model.entity.QuestionFavour;
import com.stone.model.vo.question.QuestionFavourVO;
import com.stone.question.mapper.QuestionFavourMapper;
import com.stone.question.service.QuestionFavourService;
import com.stone.question.service.QuestionService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionFavourServiceImpl extends ServiceImpl<QuestionFavourMapper, QuestionFavour> implements QuestionFavourService {
    @Resource
    private QuestionService questionService;
    @Override
    public Page<QuestionFavourVO> getQuestionFavourVoPage(Page<QuestionFavour> page) {
        List<QuestionFavour> list = page.getRecords();
        Page<QuestionFavourVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollectionUtils.isEmpty(list)) {
            return voPage;
        }
        List<QuestionFavourVO> voList = list.stream()
                .map(this::getQuestionFavourVo)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public boolean addFavour(QuestionFavourRequest questionFavourRequest) {
        QuestionFavour questionFavour = new QuestionFavour();
        BeanUtils.copyProperties(questionFavourRequest,questionFavour);
        int insert = baseMapper.insert(questionFavour);
        return insert >= 1;
    }

    @Override
    public boolean deleteFavour(Long id) {
        int i = baseMapper.deleteById(id);
        return i>=1;
    }

    private QuestionFavourVO getQuestionFavourVo(QuestionFavour questionFavour) {
        QuestionFavourVO questionFavourVo = new QuestionFavourVO();
        BeanUtils.copyProperties(questionFavour , questionFavourVo);
        Question question = questionService.getById(questionFavour.getQuestionId());
        questionFavourVo.setQuestionTitle(question.getTitle());
        return questionFavourVo;
    }
}
