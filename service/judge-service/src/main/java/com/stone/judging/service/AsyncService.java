package com.stone.judging.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class AsyncService<T> {
    @Resource
    private ThreadPoolTaskExecutor taskExecutor;

    public void executeAsyncTask(Runnable task) {
        taskExecutor.execute(task);
    }

    public Future<T> submitAsyncTask(Callable<T> task) {
        return taskExecutor.submit(task);
    }
}
