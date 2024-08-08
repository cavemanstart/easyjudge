package com.stone.model.vo.question;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class QuestionFavourVO implements Serializable {
    private Long id;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 题目 id
     */
    private String questionTitle;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}
