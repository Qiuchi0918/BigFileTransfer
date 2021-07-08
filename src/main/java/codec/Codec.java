package codec;

import protocol.packet.FileMetaPacket;
import protocol.packet.Packet;
import protocol.packet.LoginRequestPacket;
import protocol.packet.LoginResponsePacket;
import protocol.serilizer.Serializer;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

import static protocol.command.Command.*;

public class Codec {

    public static final int TYPE = 0x12345678;

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;

    public static Codec INSTANCE = new Codec();

    private Codec() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(FILE_PACKET, FileMetaPacket.class);
        packetTypeMap.put(LOGIN_PACKET_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_PACKET_RESPONSE, LoginResponsePacket.class);
    }

    public void encode(ByteBuf byteBuf, Packet packet) {
        byte[] bytes = Serializer.Algorithm.FASTJSON.serialize(packet);
        byteBuf.writeInt(TYPE);
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public Packet decode(ByteBuf byteBuf) {
        byteBuf.readInt();
        Byte command = byteBuf.readByte();
        int len = byteBuf.readInt();
        byte[] bytes = new byte[len];
        byteBuf.readBytes(bytes);

        Class<?> clazz = packetTypeMap.get(command);
        if (clazz == null) {
            throw new NullPointerException("解析失败，没有该类型的数据包");
        }

        return (Packet) Serializer.Algorithm.FASTJSON.deSerialize(bytes, clazz);
    }
}
