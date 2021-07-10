package handler;

import codec.Codec;
import io.netty.channel.DefaultFileRegion;
import lombok.extern.slf4j.Slf4j;
import protocol.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class CodecHandler extends MessageToMessageCodec<ByteBuf, Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object o, List<Object> list) {
        if (o instanceof Packet) {
            ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
            Codec.encode(byteBuf, (Packet) o);
            log.info("Encode Packet");
            list.add(byteBuf);
        } else if (o instanceof DefaultFileRegion) {
            ((DefaultFileRegion) o).retain();
            ctx.writeAndFlush(o);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) {
        log.info("Decode Packet");
        list.add(Codec.decode(byteBuf));
    }
}
