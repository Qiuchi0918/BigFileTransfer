package handler;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedFile;
import lombok.extern.slf4j.Slf4j;
import protocol.packet.FileMetaPacket;
import util.ChannelAttrUtil;

import java.io.File;
import java.io.RandomAccessFile;

@Slf4j
public class FileMetaPacketHandler extends SimpleChannelInboundHandler<FileMetaPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileMetaPacket msg) throws Exception {

        if (msg.getACK() != 0) {
            writeAndFlushFileRegion(ctx, msg);
        } else {

            File file = msg.getFile();
            log.info("Receiving File {}", file.getName());

            ctx.channel().attr(ChannelAttrUtil.outStream).set(new RandomAccessFile("./server-receive-" + file.getName(), "rw"));
            ctx.channel().attr(ChannelAttrUtil.fileSize).set(msg.getFileLength());
            ctx.channel().attr(ChannelAttrUtil.newFile).set(true);

            msg.setACK(msg.getACK() + 1);
            ctx.writeAndFlush(msg);
        }
    }

    private void writeAndFlushFileRegion(ChannelHandlerContext ctx, FileMetaPacket packet) throws Exception {
//        DefaultFileRegion fileRegion = new DefaultFileRegion(packet.getFile(), 0, packet.getFileLength());
//        ctx.writeAndFlush(fileRegion).addListener((ChannelFutureListener) future -> log.info("Send Complete"));

        ChunkedFile chunkedFile = new ChunkedFile(packet.getFile());

        while (!chunkedFile.isEndOfInput()) {
            ctx.write(chunkedFile.readChunk(ByteBufAllocator.DEFAULT));
        }

        ctx.flush();
    }
}
