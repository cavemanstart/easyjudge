package com.stone.easyjudge.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stone.easyjudge.exception.BusinessException;
import com.stone.easyjudge.model.entity.User;
import com.stone.easyjudge.model.vo.LoginUserVO;
import com.stone.easyjudge.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author xy156
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2024-06-28 21:57:15
*/
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    boolean isAdmin(HttpServletRequest request);

    boolean isAdmin(User loginUser);

    UserVO getUserVO(User user);
}
