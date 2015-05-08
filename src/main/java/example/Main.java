package example;

import java.lang.reflect.Method;

public class Main {
	public static void main(String[] args) {
		
		try { 
			// 每次都创建出一个新的类加载器
			CustomCL cl = new CustomCL(System.getProperty("user.dir")+"/src/main/java/example/script/", new String[]{"Foo"}); 
			Class cls = cl.loadClass("Foo"); 
			Object foo = cls.newInstance(); 
			
			Method m = foo.getClass().getMethod("sayHello", new Class[]{}); 
			m.invoke(foo, new Object[]{}); 
			
		}  catch(Exception ex) { 
			ex.printStackTrace(); 
		} 
	}
}
