package netty.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerManager {
	private static ServerBootstrap serverBootstrap = null;
	

	public static void init(int port, int bcount, int wcount,ChannelInitializer<Channel> channelInitializer) {
			
			
		
	}

}
