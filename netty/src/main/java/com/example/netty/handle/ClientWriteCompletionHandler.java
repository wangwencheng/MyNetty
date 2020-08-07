package com.example.netty.handle;

import com.example.netty.entity.BufferAndArr;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ClientWriteCompletionHandler implements CompletionHandler<Integer, Void> {
    /**
     * 异步socket通道
     */
    private AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public ClientWriteCompletionHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        this.channel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, Void attachment) {
        log.info("write thread: " + Thread.currentThread());
        List<Byte> rtnBytesArr = new ArrayList<>();
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        // 异步读取返回的数据，读取结束后会回调ReadCompletionHandler
        channel.read(readBuffer, 1000, TimeUnit.MILLISECONDS, new BufferAndArr(rtnBytesArr, readBuffer), new ClientReadCompletionHandler(channel, latch));
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        exc.printStackTrace();
        latch.countDown();
    }
}
