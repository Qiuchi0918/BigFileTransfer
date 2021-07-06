package com.hhu.client.handler;

import com.hhu.codec.Codec;
import com.hhu.util.ChannelAttrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.FileOutputStream;
import java.util.Date;

public class FilePacketReceiveHandler extends ChannelInboundHandlerAdapter {

    private static long readLength;

    private int readTime = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf byteBuf = (ByteBuf) msg;
        int type = byteBuf.getInt(0);
        if (type != Codec.TYPE) {
            System.out.println(readTime++);
            readLength += byteBuf.readableBytes();

            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);

            FileOutputStream outputStream = ctx.channel().attr(ChannelAttrUtil.outStream).get();

            outputStream.write(bytes);
            byteBuf.release();

            Long fileLength = ctx.channel().attr(ChannelAttrUtil.fileSize).get();

            if (readLength >= fileLength) {
                readLength = 0;
                System.out.println("文件接收完成...");
                System.out.println(new Date(System.currentTimeMillis()));
                outputStream.close();
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
