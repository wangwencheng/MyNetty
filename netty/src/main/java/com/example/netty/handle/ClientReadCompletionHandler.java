package com.example.netty.handle;

import com.example.netty.entity.BufferAndArr;
import com.google.common.primitives.Bytes;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ClientReadCompletionHandler implements CompletionHandler<Integer, BufferAndArr> {
    /**
     * 异步socket通道
     */
    private AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public ClientReadCompletionHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        this.channel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer bytesNum, BufferAndArr attachment) {
        System.out.println("read thread: " + Thread.currentThread());
        int size = attachment.buffer.limit();
        attachment.buffer.flip();
        byte[] tempBytes = new byte[bytesNum];
        attachment.buffer.get(tempBytes);
        attachment.bytesArr.addAll(Bytes.asList(tempBytes));
        // 根据读取到的数据长度与缓存总长度比较，相等则继续读取，否则读取结束
        if (bytesNum >= size) {
            attachment.buffer.clear();
            // 继续读取时加入超时时间，如果已经读取完，则会触发超时异常，转到fail中
            channel.read(attachment.buffer, 1000, TimeUnit.MILLISECONDS, attachment, new ClientReadCompletionHandler(channel, latch));
        } else {
            completionAction(attachment.bytesArr);
        }
    }

    @Override
    public void failed(Throwable exc, BufferAndArr attachment) {
        // 当没有数据时会超时抛出InterruptedByTimeoutException异常，然后在这里处理读取结果，因为暂时没有发现更好的方法
        if (exc != null) {
            completionAction(attachment.bytesArr);
        } else {
            exc.printStackTrace();
        }
    }

    private void completionAction(List<Byte> bytesArr) {
        System.out.println("当前时间：" + new String(Bytes.toArray(bytesArr), StandardCharsets.UTF_8));
        latch.countDown();
    }
}
