package the.flash.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import the.flash.protocol.request.LoginRequestPacket;
import the.flash.protocol.response.LoginResponsePacket;
import the.flash.util.LoginUtil;

import java.util.Date;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket packet) {
        System.out.println(new Date() + ": 收到客户端登录请求……");
        //登录流程

        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();

        loginResponsePacket.setVersion(packet.getVersion());

        if (valid(packet)) {
            loginResponsePacket.setSuccess(true);
            System.out.println(new Date() + ":登录成功！");
            LoginUtil.markAsLogin(channelHandlerContext.channel());
        } else {
            loginResponsePacket.setReason("账号密码校验失败！");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ":登录失败！");
        }
        //写数据
        channelHandlerContext.channel().writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket packet) {
        return true;
    }
}
