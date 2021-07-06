package com.hhu.client.handler;

import com.hhu.protocol.FilePacket;
import com.hhu.util.ChannelAttrUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class FilePacketClientHandler extends SimpleChannelInboundHandler<FilePacket> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FilePacket packet) throws Exception {
		File file = packet.getFile();
		System.out.println("receive file from server: " + file.getName());
		System.out.println(new Date(System.currentTimeMillis()));

		ctx.channel().attr(ChannelAttrUtil.outStream).set(new FileOutputStream("./client-receive-" + file.getName()));
		ctx.channel().attr(ChannelAttrUtil.fileSize).set(file.length());

		packet.setACK(packet.getACK() + 1);
		ctx.writeAndFlush(packet);
	}
}
