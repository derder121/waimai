package com.kece.fanta.common;

import java.util.concurrent.ThreadLocalRandom;

/*
 * 基于ThreadLocal封装工具类，保持一个线程中的相关变量
 * 这里用于保存用户ID
 * */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     *
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     *
     * @return
     */
    public static Long getCurrentId() {

        /*Long aId;
        if (threadLocal.get() == null){
             aId = Long.valueOf(1);
        }else {
            aId = threadLocal.get();
        }
        return aId;*/

        return threadLocal.get();
    }
}
