package handler;

import protocol.packet.LoginResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

@ChannelHandler.Sharable
public class LoginResponsePacketHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket packet) throws Exception {
		System.out.println(new Date() + " " + packet.getId() + " " + packet.getName() + " 登陆成功");
	}
}
