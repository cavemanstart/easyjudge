package com.stone.judging.judge.codesandbox;

import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
public class MyInvocationHandler implements InvocationHandler {
    private Object target = null;
    public MyInvocationHandler(Object target) {
        this.target = target;
    }
    public Object getProxy(){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("代码沙箱请求信息：" + args[0].toString());
        Object res = method.invoke(target, args);
        log.info("代码沙箱响应信息：" + res.toString());
        return res;
    }
}
