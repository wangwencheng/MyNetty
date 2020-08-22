package com.example.netty.demo.handle;

import com.example.netty.demo.protobuf.SubscribeReqProto;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

public class SubReqClientHandler extends ChannelHandlerAdapter {
    public SubReqClientHandler() {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        for (int i = 0; i < 10; i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("Receive Server response: [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private SubscribeReqProto.SubscribeReq subReq(int i) {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqId(i);
        builder.setUserName("Wangwencheng");
        builder.setProductName("Netty Book For Protobuf");
        List<String> address = new ArrayList<>();
        address.add("NanJing YuHuaTai");
        address.add("Beijing liulichang");
        address.add("Shenzhen hongshulin");
        builder.addAllAddress(address);
        return builder.build();
    }
}
