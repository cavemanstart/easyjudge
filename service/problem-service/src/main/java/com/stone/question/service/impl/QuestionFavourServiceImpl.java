package com.stone.question.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stone.model.dto.question.QuestionEditRequest;
import com.stone.model.entity.QuestionFavour;
import com.stone.model.entity.QuestionSubmit;
import com.stone.model.vo.question.QuestionFavourVo;
import com.stone.model.vo.question.QuestionSubmitVO;
import com.stone.model.vo.question.QuestionVO;
import com.stone.question.mapper.QuestionFavourMapper;
import com.stone.question.service.QuestionFavourService;
import com.stone.question.service.QuestionService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionFavourServiceImpl extends ServiceImpl<QuestionFavourMapper, QuestionFavour> implements QuestionFavourService {
    @Override
    public Page<QuestionFavourVo> getQuestionFavourVoPage(Page<QuestionFavour> page) {
        List<QuestionFavour> list = page.getRecords();
        Page<QuestionFavourVo> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (CollectionUtils.isEmpty(list)) {
            return voPage;
        }
        List<QuestionFavourVo> voList = list.stream()
                .map(this::getQuestionFavourVo)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        return voPage;
    }
    private QuestionFavourVo getQuestionFavourVo(QuestionFavour questionFavour) {
        QuestionFavourVo questionFavourVo = new QuestionFavourVo();
        BeanUtils.copyProperties(questionFavour , questionFavourVo);
        return questionFavourVo;
    }
}
