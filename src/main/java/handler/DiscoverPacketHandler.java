package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

public class DiscoverPacketHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        DatagramPacket udpPacket = (DatagramPacket) msg;
        ByteBuf contentInBuf = udpPacket.content();
        byte[] contentInByte = new byte[contentInBuf.readableBytes()];
        contentInBuf.readBytes(contentInByte);
        System.out.println(new String(contentInByte));
    }
}
