package the.flash.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import the.flash.protocol.request.LoginRequestPacket;
import the.flash.protocol.response.LoginResponsePacket;
import the.flash.session.Session;
import the.flash.util.SessionUtil;

import java.util.Date;
import java.util.UUID;

@ChannelHandler.Sharable
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    protected LoginRequestHandler() {
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginRequestPacket packet) {
        System.out.println(new Date() + ": 收到客户端登录请求……");
        //登录流程
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();

        loginResponsePacket.setVersion(packet.getVersion());
        loginResponsePacket.setUserName(packet.getUsername());


        if (valid(packet)) {
            loginResponsePacket.setSuccess(true);
            loginResponsePacket.setUserId(randomUserId());
            System.out.println("[" + packet.getUsername() + "]登录成功");
            SessionUtil.bindSession(new Session(loginResponsePacket.getUserId(),packet.getUsername()),channelHandlerContext.channel());
        } else {
            loginResponsePacket.setReason("账号密码校验失败！");
            loginResponsePacket.setSuccess(false);
            System.out.println(new Date() + ":登录失败！");
        }
        //写数据
        channelHandlerContext.writeAndFlush(loginResponsePacket);
    }

    private boolean valid(LoginRequestPacket packet) {
        return true;
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
