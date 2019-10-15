package the.flash.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import the.flash.protocol.request.ListGroupMemberRequestPacket;
import the.flash.protocol.response.ListGroupMemberResponsePacket;
import the.flash.session.Session;
import the.flash.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

@ChannelHandler.Sharable
public class ListGroupRequestHandler extends SimpleChannelInboundHandler<ListGroupMemberRequestPacket> {
    public static final ListGroupRequestHandler INSTANCE = new ListGroupRequestHandler();

    private ListGroupRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ListGroupMemberRequestPacket listGroupMemberRequestPacket) throws Exception {
        // 1. 获取群的 ChannelGroup
        String groupId = listGroupMemberRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);

        // 2. 遍历群成员的 channel，对应的 session，构造群成员的信息
        List<Session> sessionList = new ArrayList<>();
        for (Channel channel : channelGroup) {
            Session session = SessionUtil.getSession(channel);
            sessionList.add(session);
        }

        // 3. 构建获取成员列表响应写回到客户端
        ListGroupMemberResponsePacket responsePacket = new ListGroupMemberResponsePacket();

        responsePacket.setGroupId(groupId);
        responsePacket.setSessionList(sessionList);
        channelHandlerContext.writeAndFlush(responsePacket);
    }
}
