package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ClientSocketTest {
	public static Selector selector = null;
	public void connect() throws IOException{
		SocketChannel sChannel = SocketChannel.open();
		//配置为非阻塞
		sChannel.configureBlocking(false);
		//打开selector
		selector = Selector.open();
		//将连接注册到客户端的selector
		sChannel.register(selector, SelectionKey.OP_CONNECT);
		//连接到服务器
		sChannel.connect(new InetSocketAddress(8082));
		
		System.out.println("connect to server");
	}
	public void listen() throws IOException{
		while(true){
			selector.select();
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectionKeys.iterator();
			while(iterator.hasNext()){
				SelectionKey selectionKey = iterator.next();
				iterator.remove();
				handlerKeys(selectionKey);
			}
		}
	}
	private void handlerKeys(SelectionKey selectionKey) throws IOException {
		ByteBuffer readBuff = ByteBuffer.allocate(1024);
		ByteBuffer sendBuff = ByteBuffer.allocate(1024);
		SocketChannel client = null;
		if(selectionKey.isConnectable()){
			System.out.println("client connect");
			//获得key的channel
			client = (SocketChannel) selectionKey.channel();
			if(client.isConnectionPending()){//判断该通道是否正在执行连接行为
				client.finishConnect();//完成连接
				System.out.println("完成连接");
				sendBuff.clear();
				
				client.write(sendBuff.put("hello server".getBytes()));
				
				client.register(selector, SelectionKey.OP_READ);
			}
		}else if(selectionKey.isReadable()){
			readBuff.clear();
			client = (SocketChannel) selectionKey.channel();
			//获得缓存区的数据
			StringBuilder builder = new StringBuilder();
			String readText = null;
			while(client.read(readBuff) > 0){
				readText = new String(readBuff.array(),0,readBuff.limit());
				builder.append(readText);
				readBuff.clear();
			}
			System.out.println("server say: "+ builder.toString());
			client.register(selector,  SelectionKey.OP_WRITE);
		}else if(selectionKey.isWritable()){
			
			client = (SocketChannel) selectionKey.channel();
			sendBuff.clear();
			//写入数据
			String text = "hello server";
			sendBuff.put(text.getBytes());	
			//将position回复到0
			sendBuff.flip();
			//发送数据
			client.write(sendBuff);
			System.out.println("write to server: " + text);
			client.register(selector, SelectionKey.OP_READ);
			
		}
		
		
	}
	public static void main(String[] args) throws IOException {
		ClientSocketTest cs = new ClientSocketTest();
		cs.connect();
		cs.listen();
	}
}
