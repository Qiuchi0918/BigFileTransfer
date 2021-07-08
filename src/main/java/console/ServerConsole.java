package console;

import protocol.packet.FileMetaPacket;
import util.SessionUtil;
import io.netty.channel.Channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class ServerConsole {
	public void exec(Channel channel) {
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入文件路径：");
		String path = sc.nextLine();

		File file = new File(path);

		try {
			FileInputStream inputStream=new FileInputStream(path);
			FileMetaPacket fileMetaPacket = new FileMetaPacket(file.getName(),inputStream.getChannel().size(),file);
			channel.writeAndFlush(fileMetaPacket);

			Map<String, Channel> channelMap = SessionUtil.getNodeIdChannelMap();
			for (Map.Entry<String, Channel> entry : channelMap.entrySet()) {
				entry.getValue().writeAndFlush(fileMetaPacket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
