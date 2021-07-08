package handler;

import protocol.packet.FileMetaPacket;
import util.ChannelAttrUtil;
import io.netty.channel.*;

import java.io.File;
import java.io.RandomAccessFile;

public class FileMetaPacketHandler extends SimpleChannelInboundHandler<FileMetaPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileMetaPacket msg) throws Exception {

        if (msg.getACK() != 0) {
            writeAndFlushFileRegion(ctx, msg);
        } else {

            File file = msg.getFile();
            System.out.println("receive file from client: " + file.getName());

            ctx.channel().attr(ChannelAttrUtil.outStream).set(new RandomAccessFile("./server-receive-" + file.getName(), "rw"));
            ctx.channel().attr(ChannelAttrUtil.fileSize).set(msg.getFileLength());
            ctx.channel().attr(ChannelAttrUtil.newFile).set(true);

            msg.setACK(msg.getACK() + 1);
            ctx.writeAndFlush(msg);
        }
    }

    private void writeAndFlushFileRegion(ChannelHandlerContext ctx, FileMetaPacket packet) {
        DefaultFileRegion fileRegion = new DefaultFileRegion(packet.getFile(), 0, packet.getFileLength());
        ctx.writeAndFlush(fileRegion, ctx.newProgressivePromise()).addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationComplete(ChannelProgressiveFuture future) {
                System.out.println("Transmission Complete");
            }

            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                System.out.println(progress + "/" + total);
            }
        });
    }
}
