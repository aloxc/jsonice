package com.github.aloxc.jsonice.testice;

import com.github.aloxc.jsonice.jmx.MXBeanDescription;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by aloxc on 2018/6/24 in project jsonice.
 */
@MXBeanDescription("xxuser")
public interface IUserService {
    @MXBeanDescription("statInfo")
    ConcurrentHashMap<String, AtomicInteger> getStat();
}
