package com.github.aloxc.jsonice.hello;// **********************************************************************
//
// Copyright (c) 2003-2018 ZeroC, Inc. All rights reserved.
//
// **********************************************************************


public class HelloI implements Hello
{
    @Override
    public void sayHello(int delay, com.zeroc.Ice.Current current)
    {
        if(delay > 0)
        {
            try
            {
                Thread.currentThread();
                Thread.sleep(delay);
            }
            catch(InterruptedException ex1)
            {
            }
        }
        System.out.println("Hello World!" + delay);
    }

    @Override
    public void shutdown(com.zeroc.Ice.Current current)
    {
        System.out.println("Shutting down...");
        current.adapter.getCommunicator().shutdown();
    }
}
