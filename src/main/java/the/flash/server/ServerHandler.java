package the.flash.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import the.flash.protocol.Packet;
import the.flash.protocol.PacketCodeC;
import the.flash.protocol.request.LoginRequestPacket;
import the.flash.protocol.response.LoginResponsePacket;

import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new Date() + ":客户端开始登录......");
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginRequestPacket) {
            //登录流程
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();

            loginResponsePacket.setVersion(loginRequestPacket.getVersion());

            if (valid(loginRequestPacket)) {
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + ":登录成功！");
            } else {
                loginResponsePacket.setReason("账号密码校验失败！");
                loginResponsePacket.setSuccess(false);
                System.out.println(new Date() + ":登录失败！");
            }

            //登录响应
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket packet) {
        return true;
    }
}
