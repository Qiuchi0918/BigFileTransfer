package protocol.packet;

import static protocol.command.Command.LOGIN_PACKET_REQUEST;

public class LoginRequestPacket extends Packet {

	String name;

	String id;

	@Override
	public Byte getCommand() {
		return LOGIN_PACKET_REQUEST;
	}

	public LoginRequestPacket() {
	}

	public LoginRequestPacket(String name) {
		this.name = name;
	}

	public LoginRequestPacket(String name, String id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
