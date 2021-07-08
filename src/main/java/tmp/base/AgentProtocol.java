package tmp.base;

public interface AgentProtocol {

    byte getType();

    byte[] toByte();

    void fromByte(byte[] src);
}
