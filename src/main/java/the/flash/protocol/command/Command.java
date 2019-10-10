package the.flash.protocol.command;

import io.netty.buffer.ByteBuf;

public interface Command {
    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;
}
