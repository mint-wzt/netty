package the.flash.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import the.flash.protocol.response.QuitGroupResponsePacket;

public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, QuitGroupResponsePacket quitGroupResponsePacket) {
        if (quitGroupResponsePacket.isSuccess()) {
            System.out.println("退出群聊[" + quitGroupResponsePacket.getGroupId() + "]成功！");
        } else {
            System.out.println("退出群聊[" + quitGroupResponsePacket.getGroupId() + "]失败！");
        }
    }
}
