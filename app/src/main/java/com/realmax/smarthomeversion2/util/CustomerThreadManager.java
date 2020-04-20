package com.realmax.smarthomeversion2.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CustomerThreadManager {
    /**
     * corePoolSize: 该线程池中核心线程的数量。
     * maximumPoolSize：该线程池中最大线程数量。(区别于corePoolSize)
     * keepAliveTime：非核心线程空闲时要等待下一个任务到来的时间
     * unit:上面时间属性的单位
     * workQueue:任务队列，后面详述。
     * threadFactory:线程工厂，可用于设置线程名字等等，一般无须设置该参数。
     */
    public final static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            40,
            1,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(100));
}
