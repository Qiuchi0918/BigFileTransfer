package main;


import handler.CodecHandler;
import console.ClientConsole;
import handler.FileMetaPacketHandler;
import handler.FilePacketHandler;
import handler.LoginRequestPacketHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocol.packet.LoginRequestPacket;

public class TcpClient {

    public static void main(String[] args) throws InterruptedException {

        String HOST = args[0];

        int PORT = Integer.parseInt(args[1]);

        Bootstrap bootstrap = new Bootstrap();

        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();

                        pipeline.addLast(new FilePacketHandler());

                        pipeline.addLast(new CodecHandler());

                        pipeline.addLast(new FileMetaPacketHandler());
                        pipeline.addLast(new LoginRequestPacketHandler());
                    }
                });

        ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
        if (future.isSuccess()) {
            System.out.println("连接服务器成功");
            Channel channel = future.channel();
            joinCluster(channel);
            console(channel);
        } else {
            System.out.println("连接服务器失败");
        }

        future.channel().closeFuture().sync();
    }

    private static void joinCluster(Channel channel) throws InterruptedException {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket("node1");
        channel.writeAndFlush(loginRequestPacket);
        Thread.sleep(2000);
    }

    private static void console(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                ClientConsole.exec(channel);
            }
        }).start();
    }
}
