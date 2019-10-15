package the.flash.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import the.flash.protocol.response.ListGroupMemberResponsePacket;

public class ListGroupMemberResponseHandler extends SimpleChannelInboundHandler<ListGroupMemberResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ListGroupMemberResponsePacket listGroupMemberResponsePacket) {
        System.out.println("群[" + listGroupMemberResponsePacket.getGroupId() + "]中的人包括：" + listGroupMemberResponsePacket.getSessionList());
    }
}
