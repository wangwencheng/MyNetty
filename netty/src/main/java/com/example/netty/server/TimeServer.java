package com.example.netty.server;

import com.example.netty.handle.AcceptCompletionHandler;
import lombok.extern.slf4j.Slf4j;
import sun.security.jgss.krb5.Krb5AcceptCredential;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class TimeServer implements Runnable {

    public CountDownLatch countDownLatch;
    public AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public TimeServer(int port) {
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            log.info("the time server is start in port:" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        countDownLatch = new CountDownLatch(1);
        asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        log.info("我是主线程：" + Thread.currentThread());
        new TimeServer(8088).run();
        log.info("监听线程已挂");
    }
}
