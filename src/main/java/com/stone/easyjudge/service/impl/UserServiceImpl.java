package com.stone.easyjudge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stone.easyjudge.common.ErrorCode;
import com.stone.easyjudge.exception.BusinessException;
import com.stone.easyjudge.mapper.UserMapper;
import com.stone.easyjudge.model.entity.User;
import com.stone.easyjudge.model.enums.UserRoleEnum;
import com.stone.easyjudge.model.vo.LoginUserVO;
import com.stone.easyjudge.model.vo.UserVO;
import com.stone.easyjudge.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;

import static com.stone.easyjudge.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author xy156
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-06-28 21:57:15
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    private static final String SALT = "stone";
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword){
        // 1. 校验
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {//同步机制
            // 账户不能重复
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getUserAccount,userAccount);
            Long cnt = this.baseMapper.selectCount(lambdaQueryWrapper);
            if (cnt>0){
                throw new BusinessException(ErrorCode.USER_EXIST,"用户已存在");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2.查询用户是否存在
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount,userAccount);
        User user = this.baseMapper.selectOne(lambdaQueryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 3. 校验密码
        String userPassword2 = user.getUserPassword();
        if (!userPassword2.equals(encryptPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 4. 将用户状态存入session
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 5. 返回VO
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        return loginUser!=null && UserRoleEnum.ADMIN.getValue().equals(loginUser.getUserRole());
    }

    @Override
    public boolean isAdmin(User loginUser) {
        return loginUser != null && UserRoleEnum.ADMIN.getValue().equals(loginUser.getUserRole());
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}