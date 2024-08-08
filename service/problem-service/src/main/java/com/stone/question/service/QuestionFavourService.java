package com.stone.question.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.stone.model.dto.question.QuestionFavourRequest;
import com.stone.model.entity.QuestionFavour;
import com.stone.model.vo.question.QuestionFavourVO;

public interface QuestionFavourService extends IService<QuestionFavour> {

    Page<QuestionFavourVO> getQuestionFavourVoPage(Page<QuestionFavour> page);

    boolean addFavour(QuestionFavourRequest questionFavourRequest);

    boolean deleteFavour(Long id);
}
