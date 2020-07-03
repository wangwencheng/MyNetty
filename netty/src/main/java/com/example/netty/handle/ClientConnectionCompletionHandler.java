package com.example.netty.handle;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

public class ClientConnectionCompletionHandler implements CompletionHandler<Void, Void> {
    /**
     * 异步socket通道
     */
    private AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public ClientConnectionCompletionHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        this.channel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Void result, Void attachment) {
        System.out.println("connection thread: " + Thread.currentThread());
        String msg = "query time order";
        ByteBuffer writeBuffer = ByteBuffer.allocate(msg.length());
        writeBuffer.put(msg.getBytes(StandardCharsets.UTF_8)).flip();
        // 异步写入发送数据，写入完成后会回调WriteCompletionHandler
        channel.write(writeBuffer, null, new ClientWriteCompletionHandler(channel,latch));
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        exc.printStackTrace();
        latch.countDown();
    }
}
