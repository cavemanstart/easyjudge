package com.stone.question.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stone.model.entity.Question;
import com.stone.model.entity.QuestionFavour;
import com.stone.model.vo.question.QuestionFavourVo;

public interface QuestionFavourService extends IService<QuestionFavour> {

    Page<QuestionFavourVo> getQuestionFavourVoPage(Page<QuestionFavour> page);
}
