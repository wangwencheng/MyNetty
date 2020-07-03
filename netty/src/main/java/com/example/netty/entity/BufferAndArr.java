package com.example.netty.entity;

import java.nio.ByteBuffer;
import java.util.List;

public class BufferAndArr {
    public BufferAndArr(List<Byte> bytesArr, ByteBuffer buffer) {
        this.bytesArr = bytesArr;
        this.buffer = buffer;
    }

    public List<Byte> bytesArr;
    public ByteBuffer buffer;
}
