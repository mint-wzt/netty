package the.flash.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ":客户端写出数据");
        //1.获取数据
        ByteBuf buffer = getByteBuf(ctx);

        //2.写数据
        ctx.channel().writeAndFlush(buffer);

    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        byte[] bytes = "你好，桂电！".getBytes(Charset.forName("utf-8"));

        ByteBuf buf = ctx.alloc().buffer();

        buf.writeBytes(bytes);

        return buf;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date()+":客户端读到数据->"+byteBuf.toString(Charset.forName("utf-8")));
    }
}
