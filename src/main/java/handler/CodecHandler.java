package handler;

import codec.Codec;
import io.netty.channel.DefaultFileRegion;
import protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

@ChannelHandler.Sharable
public class CodecHandler extends MessageToMessageCodec<ByteBuf, Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, List<Object> list) throws Exception {
        if (o instanceof Packet) {
            ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
            Codec.INSTANCE.encode(byteBuf, (Packet) o);
            System.out.println("Encode Packet");
            list.add(byteBuf);
        } else if (o instanceof DefaultFileRegion) {
            ((DefaultFileRegion) o).retain();
            ctx.writeAndFlush(o);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("Decode Packet");
        list.add(Codec.INSTANCE.decode(byteBuf));
    }
}
