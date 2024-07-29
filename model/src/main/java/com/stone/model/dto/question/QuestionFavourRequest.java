package com.stone.model.dto.question;

import com.stone.common.base.PageRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionFavourRequest extends PageRequest implements Serializable {
    /**
     * 收藏用户 id
     */
    private Long userId;

    /**
     * 收藏题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
