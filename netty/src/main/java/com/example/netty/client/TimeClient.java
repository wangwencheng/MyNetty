package com.example.netty.client;

import com.example.netty.handle.ClientConnectionCompletionHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class TimeClient {

    private String host;
    private int port;
    private CountDownLatch latch;
    /**
     * 异步socket通道
     */
    private AsynchronousSocketChannel channel;

    private TimeClient(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.latch = new CountDownLatch(1);
        initChannel();
    }


    private void initChannel() throws IOException {
        // 打开异步socket通道
        channel = AsynchronousSocketChannel.open();
        // 异步连接指定地址，连接完成后会回调ConnectionCompletionHandler
        channel.connect(new InetSocketAddress(host, port), null, new ClientConnectionCompletionHandler(channel, latch));
    }

    public static void main(String[] args) {
        while (true) {
            new Thread(() -> {
                try {
                    log.info("time client thread: " + Thread.currentThread());
                    TimeClient client = new TimeClient("localhost", 8088);
                    client.latch.await();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

