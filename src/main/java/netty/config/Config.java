package netty.config;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class Config {
	private static int PORT = 8090;
	public static boolean isInit = false;
	public static int threads = 10;
	private static EventLoopGroup boss = new NioEventLoopGroup();
	private static EventLoopGroup work = new NioEventLoopGroup();
	
	
	
	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
}
