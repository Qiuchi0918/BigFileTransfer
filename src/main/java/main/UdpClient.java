package main;

import handler.DiscoverPacketHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class UdpClient {

    public static void main(String[] args) {

        int listenPort = Integer.parseInt(args[0]);
        int broadcastPort = Integer.parseInt(args[1]);

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(eventLoopGroup)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new DiscoverPacketHandler());

            ChannelFuture channelFuture = bootstrap.bind(listenPort).sync();

            channelFuture.addListener(f -> {
                if (f.isSuccess()) System.out.println("UDP Client Is Now Running.");
            });

            Channel channel = channelFuture.channel();

            while (true) {

                ByteBuf ipInBuf = Unpooled.copiedBuffer(InetAddress.getLocalHost().getHostAddress().getBytes(StandardCharsets.UTF_8));

                channel.writeAndFlush(new DatagramPacket(ipInBuf, new InetSocketAddress("255.255.255.255", broadcastPort)));

                Thread.sleep(1000L);
            }

        } catch (InterruptedException | UnknownHostException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
