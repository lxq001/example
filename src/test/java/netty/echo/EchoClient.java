package netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
	EventLoopGroup group = new NioEventLoopGroup();

	public void start() throws InterruptedException {
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).handler(new EchoClientHandler());
			bootstrap.remoteAddress(new InetSocketAddress(8080));
			ChannelFuture future = bootstrap.connect().sync();
			future.channel().closeFuture().sync();
			
		} finally{
			group.shutdownGracefully();
		}

	}
	public static void main(String[] args) throws InterruptedException {
		new EchoClient().start();
	}
}
