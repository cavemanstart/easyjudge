package com.stone.user.controller.inner;
import com.stone.feign.user.UserFeignClient;
import com.stone.model.entity.User;
import com.stone.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * 该服务仅内部调用，不是给前端的
 */
@RestController
@RequestMapping("/inner")
public class UserInnerController implements UserFeignClient{

    @Resource
    private UserService userService;

    /**
     * 根据 id 获取用户
     * @param userId
     * @return
     */
    @GetMapping("/get/{userId}")
    public User getById(@PathVariable long userId) {
        return userService.getById(userId);
    }

    /**
     * 根据 id 获取用户列表
     * @param idList
     * @return
     */
    @GetMapping("/get/list/{idList}")
    public List<User> listByIds(@PathVariable Collection<Long> idList) {
        return userService.listByIds(idList);
    }
    /**
     * 获取当前登录用户
     * @param token
     * @return
     */
    @GetMapping("/get/login/{token}")
    public User getLoginUser(@PathVariable String token){
        return userService.getLoginUser(token);
    }
}
