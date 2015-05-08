package netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class Test {
	@org.junit.Test
	public  void derviedBuf(){
		ByteBuf buf = Unpooled.copiedBuffer("hello world".getBytes(CharsetUtil.UTF_8));
		
		ByteBuf slice = buf.slice(1, 4);
		
		ByteBuf copy = buf.copy(0, 7);
		
		System.out.println(buf.toString(CharsetUtil.UTF_8));
		System.out.println(slice.toString(CharsetUtil.UTF_8));
		System.out.println(copy.toString(CharsetUtil.UTF_8));
		
		
	}
}
