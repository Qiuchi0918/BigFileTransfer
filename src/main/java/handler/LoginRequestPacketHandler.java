package handler;

import protocol.packet.LoginRequestPacket;
import protocol.packet.LoginResponsePacket;
import protocol.session.Session;
import util.IDUtil;
import util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

@ChannelHandler.Sharable
public class LoginRequestPacketHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
		String id = IDUtil.randomId();
		System.out.println(new Date() + " [" + loginRequestPacket.getName() + "-" + id + "]加入集群");
		SessionUtil.bindSession(new Session(id, loginRequestPacket.getName()), ctx.channel());

		ctx.writeAndFlush(new LoginResponsePacket(id, loginRequestPacket.getName()));
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SessionUtil.unBindSession(ctx.channel());
	}
}
