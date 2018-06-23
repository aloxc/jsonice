package com.github.aloxc.jsonice.testice.server;

import com.github.aloxc.jsonice.io.ProjectUtil;
import com.zeroc.Ice.Communicator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by aloxc on 2018/6/23 in project jsonice.
 */
public class ExampleServer {
    private final static Log logger = LogFactory.getLog(ExampleServer.class);

    public static void main(String[] args) {

        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<String>();

        // Try with resources block - communicator is automatically destroyed
        // at the end of this try block
        //
        try (Communicator communicator = com.zeroc.Ice.Util.initialize(args,
                "exampleserver.properties", extraArgs)) {
            //
            // Install shutdown hook to (also) destroy communicator during JVM shutdown.
            // This ensures the communicator gets destroyed when the user interrupts the application with Ctrl-C.
            //
            Runtime.getRuntime().addShutdownHook(new Thread(() -> communicator.destroy()));

            if (!extraArgs.isEmpty()) {
                System.err.println("too many arguments");
                status = 1;
            } else {
                com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Example");
                adapter.add(new ExampleImpl(), com.zeroc.Ice.Util.stringToIdentity("example"));
                adapter.activate();
                ProjectUtil.getContext();
                System.out.println("ice启动了");
                communicator.waitForShutdown();
            }
        }

        System.exit(status);
    }
}
