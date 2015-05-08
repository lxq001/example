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

		//�򿪷������׽���ͨ��
		ServerSocketChannel ssc = ServerSocketChannel.open();
		//����������Ϊ������
		ssc.configureBlocking(false);
		//��÷�������socket���������ͻ���
		ServerSocket socket = ssc.socket();
		//�󶨷���
		socket.bind(new InetSocketAddress(PORT));
		//�ҵ�selector
		selector = Selector.open();
		//ע�ᵽselector
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("start server........");
	}

	//����
	public void listen() throws IOException {
		while (true) {
			//ѡ��һ���Ѿ����io������channel
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
		//�Ƿ���Խ����׽�������
		if (selectionKey.isAcceptable()) {
			ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
			client = ssc.accept();
			client.configureBlocking(false);
			client.register(selector, SelectionKey.OP_READ );
			//System.out.println("client is connect");
		} else if (selectionKey.isReadable()) {//�ͻ�����������
			//���key��Ӧ��ͨ��
			client = (SocketChannel) selectionKey.channel();
			//���������
			readBuff.clear();
			//��û�����������
			StringBuilder builder = new StringBuilder();
			int rc = 0;
			while((rc = client.read(readBuff)) > 0){
				readText = new String(readBuff.array(),0,rc);
				builder.append(readText);
				readBuff.clear();
			}
			System.out.println("client say: "+ builder.toString());
			
			client.register(selector,  SelectionKey.OP_WRITE);
			
			
		}else if(selectionKey.isWritable()){//��������������
			
			client = (SocketChannel) selectionKey.channel();
			sendBuff.clear();
			//д������
			String text = "Hello client";
			sendBuff.put(text.getBytes());	
			//��position�ظ���0
			sendBuff.flip();
			//��������
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
