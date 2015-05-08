package nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
	public static void main(String[] args) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(new File("d:/3guu.xml"), "rw");
		FileOutputStream fs = new FileOutputStream(new File("d:/3guutest.xml"));
		FileChannel outChannel = fs.getChannel();
		FileChannel fc = raf.getChannel();
		ByteBuffer cb = ByteBuffer.allocate(1024);
		while(fc.read(cb)!=-1){
			cb.flip();
			outChannel.write(cb);
			cb.clear();
		}
	}
}
