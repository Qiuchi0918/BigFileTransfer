package com.hhu.client.handler;

import com.hhu.codec.Codec;
import com.hhu.util.ChannelAttrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Date;

public class FilePacketReceiveHandler extends ChannelInboundHandlerAdapter {

    private static long readLength;

    private long startTime = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;

        int type = byteBuf.getInt(0);

        if (type == Codec.TYPE) {
            super.channelRead(ctx, msg);
            return;
        }

        Boolean newFile = ctx.channel().attr(ChannelAttrUtil.newFile).get();

        if (newFile != null && newFile) {
            startTime = System.currentTimeMillis();
            ctx.channel().attr(ChannelAttrUtil.newFile).set(false);
        }

        RandomAccessFile outputStream = ctx.channel().attr(ChannelAttrUtil.outStream).get();

        int readableByteCount = byteBuf.readableBytes();

        byteBuf.readBytes(outputStream.getChannel(), readLength, readableByteCount);

        byteBuf.release();

        readLength += readableByteCount;

        Long fileLength = ctx.channel().attr(ChannelAttrUtil.fileSize).get();

        System.out.print("\r" + readLength * 1.0 / fileLength);

        if (readLength >= fileLength) {
            System.out.println();
            System.out.println(new Date(startTime));
            System.out.println(new Date(System.currentTimeMillis()));
            readLength = 0;
            outputStream.close();
        }
    }
}
