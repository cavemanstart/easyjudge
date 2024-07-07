package com.stone.judging.judge.codesandbox;

/**
 * 代码沙箱工厂（通过反射实现解耦）
 */
public class CodeSandboxFactory {
    public static CodeSandbox newCodeSandboxInstance(Class<? extends CodeSandbox> clazz) {
        CodeSandbox codeSandbox = null;
        try {
            codeSandbox = clazz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return codeSandbox;
    }
}
