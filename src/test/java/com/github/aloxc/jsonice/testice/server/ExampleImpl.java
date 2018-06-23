package com.github.aloxc.jsonice.testice.server;

import com.github.aloxc.jsonice.io.IceCall;
import com.github.aloxc.jsonice.io.JsonRequest;
import com.github.aloxc.jsonice.testice.Example;
import com.zeroc.Ice.Current;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by aloxc on 2018/6/23 in project jsonice.
 */
public class ExampleImpl implements Example {
    private final static Log logger = LogFactory.getLog(ExampleImpl.class);

    @Override
    public String execute(String json, Current current) {
//        System.out.println("json = " + json + "\t" + Thread.currentThread().getName());
        return IceCall.call(this.getClass(), JsonRequest.newInstance(json),current);
    }

}
