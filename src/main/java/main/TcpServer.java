package main;

import handler.CodecHandler;
import console.ServerConsole;
import handler.FileMetaPacketHandler;
import handler.FilePacketHandler;
import handler.LoginRequestPacketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpServer {

    public static void main(String[] args) throws InterruptedException {

        int PORT = Integer.parseInt(args[0]);

        ServerBootstrap bootstrap = new ServerBootstrap();

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 10)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();

                        pipeline.addLast(new FilePacketHandler());

                        pipeline.addLast(new CodecHandler());

                        pipeline.addLast(new FileMetaPacketHandler());
                        pipeline.addLast(new LoginRequestPacketHandler());
                    }
                });

        ChannelFuture future = bootstrap.bind(PORT).sync();

        if (future.isSuccess()) {
            log.info("Tcp Server Bind To Port {}", PORT);
            Channel channel = future.channel();
            console(channel);
        } else {
            log.info("Failed To Bind Tcp Server To Port {}", PORT);
        }

        future.channel().closeFuture().sync();
    }

    private static void console(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                ServerConsole serverConsole = new ServerConsole();
                serverConsole.exec(channel);
            }
        }).start();
    }
}
