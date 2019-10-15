package the.flash.protocol.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import the.flash.protocol.Packet;

import static the.flash.protocol.command.Command.GROUP_MESSAGE_REQUEST;

@Data
@AllArgsConstructor
public class GroupMessageRequestPacket extends Packet {
    private String toGroupId;
    private String message;

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_REQUEST;
    }
}
