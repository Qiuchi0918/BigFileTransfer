package protocol.attribute;

import protocol.session.Session;
import io.netty.util.AttributeKey;

public interface Attributes {

	AttributeKey<Session> SESSION = AttributeKey.newInstance("session");

}
