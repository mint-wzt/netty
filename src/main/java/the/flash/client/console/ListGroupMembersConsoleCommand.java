package the.flash.client.console;

import io.netty.channel.Channel;
import the.flash.protocol.request.ListGroupMemberRequestPacket;

import java.util.Scanner;

public class ListGroupMembersConsoleCommand implements ConsoleCommand {
    @Override
    public void exc(Scanner scanner, Channel channel) {
        ListGroupMemberRequestPacket listGroupMembersRequestPacket = new ListGroupMemberRequestPacket();

        System.out.print("输入 groupId，获取群成员列表：");
        String groupId = scanner.next();

        listGroupMembersRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(listGroupMembersRequestPacket);
    }
}
