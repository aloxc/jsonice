package com.github.aloxc.jsonice.testice.client;

import com.github.aloxc.jsonice.io.JsonRequest;
import com.github.aloxc.jsonice.testice.ExamplePrx;
import com.github.aloxc.jsonice.utils.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by aloxc on 2018/6/23 in project jsonice.
 */
public class ExampleClient {
    private final static Log logger = LogFactory.getLog(ExampleClient.class);
    public static ConcurrentHashMap<String, AtomicInteger> stat = new ConcurrentHashMap<>();
    static {
        for (int i = 0; i < 200; i++) {
            stat.put("Ice.ThreadPool.Server-" + i,new AtomicInteger());
        }
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "Exampleclient.properties", extraArgs)) {

            if (!extraArgs.isEmpty()) {
                System.err.println("too many arguments");
                status = 1;
            } else {
                status = run(communicator);
            }
        }

        System.exit(status);
    }

    private static int run(com.zeroc.Ice.Communicator communicator) throws InterruptedException, ExecutionException {
        ExamplePrx proxy = ExamplePrx.checkedCast(
                communicator.propertyToProxy("Example.Proxy")).ice_twoway().ice_secure(false);
        if (proxy == null) {
            System.err.println("invalid proxy");
            return 1;
        }
        System.out.println(communicator.getProperties().getProperty("ClientIP"));
        ExecutorService service = Executors.newFixedThreadPool(200);
        CompletionService<String> completionService = new ExecutorCompletionService<String>(service);
        int count = 200000;
        for (int i = 0; i < count; i++) {
            final int fi = i;
            completionService.submit(new Callable<String>() {
                @Override
                public String call() {
                    JsonRequest request = JsonRequest.newInstance(null);
                    request.setMethod("hello");
                    request.setParams("name","aloxc");
                    request.setParams("age",fi);
                    String result = proxy.execute(request.toJson());
                    return result;

                }
            });
        }
        for (int i = 0; i < count; i++) {
            String result = completionService.take().get();
            Map data = JsonUtil.toBean(result, HashMap.class);
            Map data1 = (Map) data.get("data");
            String thread = (String)data1.get("thread");
            stat.get(thread).incrementAndGet();

        }
        for (Map.Entry<String, AtomicInteger> entry : stat.entrySet()){
            if(entry.getValue().get() > 0) {
                System.out.println(entry.getKey() + "=" + entry.getValue().get());
            }
        }
        System.out.println("请求完毕了");

        return 0;
    }
}