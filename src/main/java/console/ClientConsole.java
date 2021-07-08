package console;

import protocol.packet.FileMetaPacket;
import io.netty.channel.Channel;

import java.io.*;
import java.util.Scanner;

public class ClientConsole {

    public static void exec(Channel channel) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入文件路径：");
        String path = sc.nextLine();
        File file = new File(path);

        try {
            FileInputStream inputStream = new FileInputStream(path);
            FileMetaPacket fileMetaPacket = new FileMetaPacket(file.getName(), inputStream.getChannel().size(),file);
            channel.writeAndFlush(fileMetaPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
