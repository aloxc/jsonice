package com.github.aloxc.jsonice.testice;

import com.github.aloxc.jsonice.annotation.IceMethod;
import com.github.aloxc.jsonice.annotation.ParamBinder;
import com.github.aloxc.jsonice.annotation.checkparam.PageNum;
import com.github.aloxc.jsonice.io.IceCode;
import com.github.aloxc.jsonice.io.IceResponse;
import com.github.aloxc.jsonice.io.IceSuccess;
import com.github.aloxc.jsonice.io.JsonRequest;
import com.zeroc.Ice.Current;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by aloxc on 2018/6/24 in project jsonice.
 */
@Service
public class UserService implements IUserService{
    private final static Log logger = LogFactory.getLog(UserService.class);


    @IceMethod(name = "ExampleImpl.hello", description = "哈罗", retType = 2)
    @PageNum(defaultValue = 1, min = 1, max = 100, key = "age")
    public IceResponse<?> helloworld(JsonRequest request, Current current,
                                     @ParamBinder(key = "name", required = true) String name,
                                     @ParamBinder(ref = PageNum.class, required = true) int age
    ) {
        IceResponse<Map<String, Object>> res = new IceResponse<Map<String, Object>>(null);
        Map<String, Object> map = null;
        res.setSuccess(IceSuccess.OK);
        res.setCode(IceCode.OK);
        try {
            map = new HashMap<>();
            map.put("name", name);
            map.put("age", String.valueOf(age));
            map.put("time", String.valueOf(System.currentTimeMillis()));
            map.put("thread", Thread.currentThread().getName());
            res.setData(map);
            return res;
        } catch (Exception e) {
            logger.error("读取排行列表", e);
            res.setSuccess(IceSuccess.FAILURE);
            res.setCode(IceCode.FAILURE);
            res.setMessage("程序出错了");
            return res;
        }

    }


    @Override
    public ConcurrentHashMap<String, AtomicInteger> getStat() {
        return null;
    }
}
