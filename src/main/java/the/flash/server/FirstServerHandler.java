package the.flash.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ":服务端收到数据->" + byteBuf.toString(Charset.forName("utf-8")));

        //回复数据到客户端
        System.out.println(new Date() + ":服务端写出数据");
        ByteBuf out = getByteBuf(ctx);
        ctx.channel().writeAndFlush(out);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        byte[] bytes = "你好，欢迎来到桂林电子科技大学！".getBytes(Charset.forName("utf-8"));

        ByteBuf buf = ctx.alloc().buffer();

        buf.writeBytes(bytes);

        return buf;
    }
}
