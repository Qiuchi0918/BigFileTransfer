package handler;

import codec.Codec;
import lombok.extern.slf4j.Slf4j;
import util.ChannelAttrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.RandomAccessFile;

@Slf4j
public class FilePacketHandler extends ChannelInboundHandlerAdapter {

    private static long readLength;

    private long startTime;

    private long packetCount;

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
            packetCount = 0;
            readLength = 0;
            ctx.channel().attr(ChannelAttrUtil.newFile).set(false);
        }

        RandomAccessFile outputStream = ctx.channel().attr(ChannelAttrUtil.outStream).get();

        int readableByteCount = byteBuf.readableBytes();

        byteBuf.readBytes(outputStream.getChannel(), readLength, readableByteCount);

        byteBuf.release();

        readLength += readableByteCount;

        packetCount++;

        Long fileLength = ctx.channel().attr(ChannelAttrUtil.fileSize).get();

        StringBuilder builder = new StringBuilder();

        builder.append("\rpg: ")
                .append(readLength * 100 / fileLength)
                .append("%, ps: ")
                .append(readableByteCount)
                .append(", pc: ")
                .append(packetCount)
                .append("    ");

        System.out.print(builder.toString());

        if (readLength >= fileLength) {
            long secElp = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println();
            log.info("Seconds Elapsed: {}", secElp);
            log.info("Packets Received: {}", packetCount);
            log.info("Average Speed: {}KB/s", readLength / 1024 / (secElp == 0 ? 1 : secElp));
            outputStream.close();
        }
    }
}
