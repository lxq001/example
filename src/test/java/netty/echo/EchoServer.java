package netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	EventLoopGroup boss = new NioEventLoopGroup();
	EventLoopGroup work = new NioEventLoopGroup();

	public void start() {
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, work);
			//���ݲ���������channelʵ��
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();//�����������handler������
					pipeline.addLast("echohandler", new EchoServerHandler());

				}
			});
			ChannelFuture future = bootstrap.bind(8080).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}finally{
			boss.shutdownGracefully();
			work.shutdownGracefully();
		}

	}
	public static void main(String[] args) {
		new EchoServer().start();
	}
}
