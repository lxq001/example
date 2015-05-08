package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ServerSocketTest {
	public static int PORT = 8082;

	public static Selector selector = null;

	public void openSocket() throws IOException {

		//打开服务器套接字通道
		ServerSocketChannel ssc = ServerSocketChannel.open();
		//服务器配置为非阻塞
		ssc.configureBlocking(false);
		//获得服务器的socket用来监听客户端
		ServerSocket socket = ssc.socket();
		//绑定服务
		socket.bind(new InetSocketAddress(PORT));
		//找到selector
		selector = Selector.open();
		//注册到selector
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("start server........");
	}

	//监听
	public void listen() throws IOException {
		while (true) {
			//选择一个已经完成io炒作的channel
			selector.select();

			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectionKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey selectionKey = iterator.next();
				iterator.remove();
				handlerKeys(selectionKey);
			}
		}
	}

	private void handlerKeys(SelectionKey selectionKey) throws IOException {
		SocketChannel client = null;
		ByteBuffer readBuff = ByteBuffer.allocate(1024);
		ByteBuffer sendBuff = ByteBuffer.allocate(1024);
		String readText = null;
		//是否可以接受套接字连接
		if (selectionKey.isAcceptable()) {
			ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
			client = ssc.accept();
			client.configureBlocking(false);
			client.register(selector, SelectionKey.OP_READ );
			//System.out.println("client is connect");
		} else if (selectionKey.isReadable()) {//客户端上行数据
			//获得key对应的通道
			client = (SocketChannel) selectionKey.channel();
			//清除旧数据
			readBuff.clear();
			//获得缓存区的数据
			StringBuilder builder = new StringBuilder();
			int rc = 0;
			while((rc = client.read(readBuff)) > 0){
				readText = new String(readBuff.array(),0,rc);
				builder.append(readText);
				readBuff.clear();
			}
			System.out.println("client say: "+ builder.toString());
			
			client.register(selector,  SelectionKey.OP_WRITE);
			
			
		}else if(selectionKey.isWritable()){//服务器下行数据
			
			client = (SocketChannel) selectionKey.channel();
			sendBuff.clear();
			//写入数据
			String text = "Hello client";
			sendBuff.put(text.getBytes());	
			//将position回复到0
			sendBuff.flip();
			//发送数据
			client.write(sendBuff);
			System.out.println("write to client: " + text);
			client.register(selector, SelectionKey.OP_READ );
			
		}
	}
	public static void main(String[] args) throws IOException {
		ServerSocketTest ss = new ServerSocketTest();
		ss.openSocket();
		ss.listen();
	}

}
