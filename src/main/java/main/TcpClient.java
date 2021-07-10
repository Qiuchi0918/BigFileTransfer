package main;


import handler.*;
import console.ClientConsole;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import protocol.packet.LoginRequestPacket;

@Slf4j
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
                .option(ChannelOption.SO_SNDBUF, 65535)
                .option(ChannelOption.SO_RCVBUF, 65535)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) {
                        ChannelPipeline pipeline = channel.pipeline();

                        pipeline.addLast(new FilePacketHandler());

                        pipeline.addLast(new CodecHandler());

                        pipeline.addLast(new FileMetaPacketHandler());
                        pipeline.addLast(new LoginResponsePacketHandler());
                    }
                });

        ChannelFuture future = bootstrap.connect(HOST, PORT).sync();

        if (future.isSuccess()) {
            log.info("Tcp Client Connected To Server At {}:{}.", HOST, PORT);
            Channel channel = future.channel();
            joinCluster(channel);
            console(channel);
        } else {
            log.info("Failed To Connect To Server At {}:{}.", HOST, PORT);
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
