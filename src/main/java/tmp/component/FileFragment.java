package tmp.component;

import tmp.base.AgentProtocol;

public class FileFragment implements AgentProtocol {

    @Override
    public byte getType() {
        return 0;
    }

    @Override
    public byte[] toByte() {
        return new byte[0];
    }

    @Override
    public void fromByte(byte[] src) {

    }
}
