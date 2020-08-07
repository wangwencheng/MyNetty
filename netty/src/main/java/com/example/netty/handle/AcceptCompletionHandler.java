package com.example.netty.handle;

import com.example.netty.server.TimeServer;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

@Slf4j
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, TimeServer> {
    @Override
    public void completed(AsynchronousSocketChannel channel, TimeServer attachment) {
        try {
            log.info("我是处理线程：" + Thread.currentThread());
            //循环监听，进行监听操作的是SimpleTimeServer运行的线程
            attachment.asynchronousServerSocketChannel.accept(attachment, this);
            //这里休眠20秒，可以看到当处理线程没有处理完成时，会启用新的线程来处理后面的请求
            Thread.sleep(20);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            //从请求通道中读取数据
            channel.read(buffer, buffer, new ReadCompletionHandler(channel));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void failed(Throwable exc, TimeServer attachment) {
         exc.printStackTrace();
         attachment.countDownLatch.countDown();
    }
}
