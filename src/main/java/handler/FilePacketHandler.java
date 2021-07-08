package handler;

import codec.Codec;
import util.ChannelAttrUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.RandomAccessFile;

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

        System.out.print("\rpg: " + readLength * 100 / fileLength + "%, ps: " + readableByteCount + "     ");

        if (readLength >= fileLength) {
            long secElp = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("\nSeconds Elapsed: " + secElp);
            System.out.println("Packets Received: " + packetCount);
            System.out.println("Average Speed: " + readLength / 1024 / (secElp == 0 ? 1 : secElp) + "KB/s");
            outputStream.close();
        }
    }
}
