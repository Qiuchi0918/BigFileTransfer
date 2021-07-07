package com.hhu.server.handler;

import com.hhu.protocol.FilePacket;
import com.hhu.util.ChannelAttrUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;

@ChannelHandler.Sharable
public class FilePacketServerHandler extends SimpleChannelInboundHandler<FilePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FilePacket packet) throws Exception {

        File file = packet.getFile();
        System.out.println("receive file from client: " + file.getName());

        ctx.channel().attr(ChannelAttrUtil.outStream).set(new RandomAccessFile("./server-receive-" + file.getName(), "rw"));
        ctx.channel().attr(ChannelAttrUtil.fileSize).set(packet.getFileLength());
        ctx.channel().attr(ChannelAttrUtil.newFile).set(true);

        packet.setACK(packet.getACK() + 1);
        ctx.writeAndFlush(packet);
    }
}
