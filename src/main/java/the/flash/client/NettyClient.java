package the.flash.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import the.flash.client.console.ConsoleCommandMannager;
import the.flash.client.console.LoginConsoleCommand;
import the.flash.client.handler.*;
import the.flash.codec.PacketDecoder;
import the.flash.codec.PacketEncoder;
import the.flash.codec.Spliter;
import the.flash.handler.IMIdleStateHandler;
import the.flash.protocol.request.LoginRequestPacket;
import the.flash.protocol.request.MessageRequestPacket;
import the.flash.server.handler.LoginRequestHandler;
import the.flash.util.SessionUtil;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8001;

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                //1.指定线程模型
                .group(workerGroup)
                //2.指定IO类型为NIO
                .channel(NioSocketChannel.class)
                //设置TCP底层属性
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                //3.IO处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        // 空闲检测
                        socketChannel.pipeline().addLast(new IMIdleStateHandler());
                        socketChannel.pipeline().addLast(new Spliter());
                        socketChannel.pipeline().addLast(new PacketDecoder());
                        socketChannel.pipeline().addLast(new LoginResponseHandler());
                        socketChannel.pipeline().addLast(new LogoutResponseHandler());
                        socketChannel.pipeline().addLast(new MessageResponseHandler());
                        socketChannel.pipeline().addLast(new CreateGroupResponseHandler());
                        socketChannel.pipeline().addLast(new JoinGroupResponseHandler());
                        socketChannel.pipeline().addLast(new ListGroupMemberResponseHandler());
                        socketChannel.pipeline().addLast(new QuitGroupResponseHandler());
                        socketChannel.pipeline().addLast(new GroupMessageResponseHandler());

                        socketChannel.pipeline().addLast(new PacketEncoder());
                        // 心跳定时器
                        socketChannel.pipeline().addLast(new HeartBeatTimeHandler());
                    }
                });

        //4.建立连接
        connect(bootstrap, HOST, PORT, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ":连接成功,启动控制台程序......");
                Channel channel = ((ChannelFuture) future).channel();
                //开始控制台线程
                startConsoleThread(channel);

            } else if (retry == 0) {
                System.out.println("重试次数已用完，放弃连接！");
            } else {
                //第几次重连
                int order = (MAX_RETRY - retry) + 1;
                //本次重连的间隔
                int delay = 1 << order;
                System.out.println(new Date() + ":连接失败，第" + order + "次重连......");

                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        Scanner scanner = new Scanner(System.in);
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();
        ConsoleCommandMannager consoleCommandMannager = new ConsoleCommandMannager();
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (!SessionUtil.hasLogin(channel)) {
                    loginConsoleCommand.exc(scanner, channel);
                } else {
                    consoleCommandMannager.exc(scanner, channel);
                }
            }
        }).start();
    }

}
