package com.stone.model.vo.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionAddVO implements Serializable {
    /**
     * 题目提交 id
     */
    private Long questionSubmitId;
    /**
     * 题目提交状态 id
     */
    private String status;

    private static final long serialVersionUID = 1L;
}
